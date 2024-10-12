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
public class MyFirstPlugin extends CaffeinatedPlugin {

    @Override
    public void onInit() {
        this.getLogger().info("Hello World!");
    }

    @Override
    public void onClose() {
        this.getLogger().info("Goodbye World!");
    }

    @Override
    public String getName() {
        return "My First Plugin";
    }

    @Override
    public String getId() {
        return "com.xstarwake.MyFirstPlugin";
    }

}
