package com.xstarwake;

import co.casterlabs.caffeinated.pluginsdk.CaffeinatedPlugin;
import co.casterlabs.caffeinated.pluginsdk.CaffeinatedPluginImplementation;
import co.casterlabs.caffeinated.pluginsdk.widgets.Widget;
import co.casterlabs.caffeinated.pluginsdk.widgets.WidgetDetails;
import co.casterlabs.caffeinated.pluginsdk.widgets.WidgetDetails.WidgetDetailsCategory;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.WidgetSettingsItem;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.WidgetSettingsLayout;
import co.casterlabs.caffeinated.pluginsdk.widgets.settings.WidgetSettingsSection;
import co.casterlabs.caffeinated.pluginsdk.widgets.WidgetInstanceMode;
import co.casterlabs.caffeinated.pluginsdk.widgets.WidgetInstance;

import co.casterlabs.caffeinated.util.MimeTypes;

import co.casterlabs.commons.functional.tuples.Pair;

import co.casterlabs.rakurai.io.IOUtil;
import co.casterlabs.rakurai.json.element.JsonElement;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;
import co.casterlabs.rakurai.json.element.JsonObject;

import lombok.NonNull;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@CaffeinatedPluginImplementation
public class CustomWidget extends CaffeinatedPlugin {

    private WidgetSettingsLayout layout;

    @Override
    public void onInit() {
        this.getLogger().info("Hello World!");

        // Register the widget with its settings
        this.getPlugins().registerWidget(
            this,
            new WidgetDetails()
                .withNamespace("com.xstarwake.CustomWidget")
                .withIcon("bell-alert")
                .withCategory(WidgetDetailsCategory.OTHER)
                .withFriendlyName("My Custom Widget"),
            CustomWidgetInstance.class
        );

        // Initialize and set settings layout
        layout = new WidgetSettingsLayout()
            .addSection(
                new WidgetSettingsSection("text_style", "Style")
                    .addItem(WidgetSettingsItem.asFont("font", "Font", "Poppins"))
                    .addItem(WidgetSettingsItem.asNumber("font_size", "Font Size (px)", 16, 1, 0, 128))
                    .addItem(WidgetSettingsItem.asColor("text_color", "Text Color", "#ffffff"))
            );

        if (layout != null) {
            this.setSettingsLayout(layout);
        } else {
            this.getLogger().warn("Failed to set settings layout - Layout is null.");
        }
    }

    @Override
    public void onClose() {
        this.getLogger().info("Goodbye World!");
    }

    @Override
    public String getName() {
        return "My First Widget";
    }

    @Override
    public String getId() {
        return "com.xstarwake.CustomWidget";
    }

    // Define the widget instance class
    public static class CustomWidgetInstance extends Widget {

        @Override
        public void onInit() {
            FastLogger.logStatic("Widget Initialized!");
        }

        @Override
        public void onNameUpdate() {
            FastLogger.logStatic("Hello World! I am: \"%s\"", this.getName());
        }

        @Override
        public void onNewInstance(@NonNull WidgetInstance instance) {
            instance.on("message", (JsonElement e) -> {
                FastLogger.logStatic("Received message from a widget instance: %s", e);
            });
        }

        @Override
        public @NonNull String getWidgetBasePath(WidgetInstanceMode mode) {
            return "/chat";
        }

        public @Nullable Pair<String, String> getResource(String resource) throws IOException {
            String mimeType = "application/octet-stream";
            String[] split = resource.split("\\.");
            if (split.length > 1) {
                mimeType = MimeTypes.getMimeForType(split[split.length - 1]);
            }
            try (InputStream in = MyFirstPlugin.class.getClassLoader().getResourceAsStream(resource)) {
                return new Pair<>(IOUtil.readInputStreamString(in, StandardCharsets.UTF_8), mimeType);
            }
        }
    }
}
