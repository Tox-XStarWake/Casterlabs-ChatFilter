package com.xstarwake;

import co.casterlabs.caffeinated.pluginsdk.CaffeinatedPlugin;
import co.casterlabs.caffeinated.pluginsdk.CaffeinatedPluginImplementation;
import co.casterlabs.caffeinated.pluginsdk.widgets.WidgetInstance;
import co.casterlabs.caffeinated.pluginsdk.widgets.WidgetInstanceMode;
import co.casterlabs.rakurai.json.element.JsonElement;
import co.casterlabs.rakurai.json.element.JsonObject;
import co.casterlabs.commons.functional.tuples.Pair;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@CaffeinatedPluginImplementation
public class ChatFilterWidget extends CaffeinatedPlugin {

    private List<String> bannedWords = Arrays.asList("followed", "shared");

    @Override
    public void onInit() {
        // Log to console when the widget is initialized
        this.getLogger().info("Chat Filter Widget has been initialized!");
    }

    public void onNewInstance(@NonNull WidgetInstance instance) {
        // Log when a new instance of the widget is created
        this.getLogger().info("New instance of Chat Filter Widget created.");

        instance.on("message", (JsonElement e) -> {
            try {
                JsonObject messageObject = e.getAsObject();
                String message = messageObject.get("message").getAsString();
                String platform = messageObject.get("platform").getAsString();
                String eventId = messageObject.get("eventId").getAsString();

                if (containsBannedWords(message, bannedWords)) {
                    // Notify the stream side to remove the message
                    instance.emit("deleteMessage", e);
                    this.getLogger().info("Deleted a message: \"%s\"", message);
                }
            } catch (IOException ex) {
                // Handle the exception by logging it
                this.getLogger().severe("An error occurred while processing a message: " + ex.getMessage());
            }
        });

        instance.on("addBannedWord", (JsonElement addWordEvent) -> {
            JsonObject addWordObject = addWordEvent.getAsObject();
            String newWord = addWordObject.get("word").getAsString().toLowerCase();
            bannedWords.add(newWord);

            // Log when a new banned word is added
            this.getLogger().info("Added new banned word: \"%s\"", newWord);
        });
    }

    public @NonNull String getWidgetBasePath(WidgetInstanceMode mode) {
        return "/chat";
    }

    @Override
    public String getId() {
        return "com.xstarwake.chatfilterwidget";
    }

    @Override
    public String getName() {
        return "Chat Filter Widget";
    }

    @Override
    public void onClose() {
        this.getLogger().info("Chat Filter Widget is closing.");
    }

    // Helper function to check for banned words in messages
    private boolean containsBannedWords(String message, List<String> bannedWords) {
        String lowerCaseMessage = message.toLowerCase();
        for (String word : bannedWords) {
            if (lowerCaseMessage.contains(word)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @Nullable Pair<String, String> getResource(String resource) throws IOException {
        // Determine the MIME type of the requested file
        String mimeType = "application/octet-stream";
        if (resource.endsWith(".html")) {
            mimeType = "text/html";
        } else if (resource.endsWith(".json")) {
            mimeType = "application/json";
        }

        // Read the file from resources
        try (InputStream in = ChatFilterWidget.class.getClassLoader().getResourceAsStream(resource)) {
            if (in != null) {
                return new Pair<>(readInputStreamString(in, StandardCharsets.UTF_8), mimeType);
            }
        }

        return null;
    }

    private String readInputStreamString(InputStream inputStream, java.nio.charset.Charset charset) throws IOException {
        try (java.util.Scanner scanner = new java.util.Scanner(inputStream, charset.name())) {
            return scanner.useDelimiter("\\A").next();
        }
    }
}
