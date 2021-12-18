/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package me.wolfsurge.api.gui.font;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import me.wolfsurge.api.gui.font.MinecraftFontRenderer;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.managers.FontManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FontUtil {
    public static volatile int completed;
    public static MinecraftFontRenderer comfortaa;
    private static Font comfortaa_;
    public static MinecraftFontRenderer comfortaaBig;
    private static Font comfortaaBig_;
    public static Map<String, Font> locationMap;

    private static Font getFont(Map<String, Font> locationMap, String fontName, int size) {
        Font font = null;
        Logger logger = LogManager.getLogger();
        try {
            if (locationMap.containsKey(fontName)) {
                font = locationMap.get(fontName).deriveFont(0, size);
            } else {
                InputStream inputStream = FontManager.class.getResourceAsStream("/assets/xeno/font/" + fontName + ".ttf");
                Font awtClientFont = Font.createFont(0, inputStream);
                locationMap.put(fontName, font);
                font = awtClientFont.deriveFont(0, size);
                inputStream.close();
            }
        }
        catch (Exception e) {
            logger.error("Error loading font");
            font = new Font("default", 0, 18);
        }
        return font;
    }

    public static boolean hasLoaded() {
        return completed >= 3;
    }

    public static void bootstrap() {
        Xeno.logger.info("Initializing fonts...");
        new Thread(() -> {
            locationMap = new HashMap<String, Font>();
            comfortaa_ = FontUtil.getFont(locationMap, "comfortaa", 18);
            comfortaaBig_ = FontUtil.getFont(locationMap, "comfortaa", 40);
            ++completed;
        }).start();
        new Thread(() -> {
            HashMap locationMap = new HashMap();
            ++completed;
        }).start();
        new Thread(() -> {
            HashMap locationMap = new HashMap();
            ++completed;
        }).start();
        while (!FontUtil.hasLoaded()) {
        }
        comfortaa = new MinecraftFontRenderer(comfortaa_, true, true);
        comfortaaBig = new MinecraftFontRenderer(comfortaaBig_, true, true);
    }

    static {
        locationMap = new HashMap<String, Font>();
    }
}

