/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.managers;

import java.awt.Font;
import java.io.InputStream;
import me.wolfsurge.api.gui.font.FontRender;
import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.Xeno;

public class FontManager
implements Globals {
    public static FontRender comfortaa;
    public static FontRender comfortaaBig;
    public static FontRender aquireBig;
    public static FontRender aquire;

    public static void load() {
        comfortaa = new FontRender(FontManager.getFont("comfortaa", 50.0f));
        comfortaaBig = new FontRender(FontManager.getFont("comfortaa", 80.0f));
        aquire = new FontRender(FontManager.getFont("aquire", 50.0f));
        aquireBig = new FontRender(FontManager.getFont("aquire", 120.0f));
    }

    public static void drawStringWithShadow(String text, float x, float y, int color) {
        if (Xeno.moduleManager.isModuleEnabled("CustomFont")) {
            comfortaa.drawStringWithShadow(text, x, y, color);
        } else {
            FontManager.mc.fontRenderer.drawStringWithShadow(text, x, y, color);
        }
    }

    public static int getStringWidth(String text) {
        return comfortaa.getStringWidth(text);
    }

    public static int getFontHeight() {
        return FontManager.comfortaa.FONT_HEIGHT;
    }

    public static int getFontString(String text, float x, float y, int color) {
        return comfortaa.drawStringWithShadow(text, x, y, color);
    }

    private static Font getFont(String fontName, float size) {
        try {
            InputStream inputStream = FontManager.class.getResourceAsStream("/assets/xeno/font/" + fontName + ".ttf");
            Font awtClientFont = Font.createFont(0, inputStream);
            awtClientFont = awtClientFont.deriveFont(0, size);
            inputStream.close();
            return awtClientFont;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return new Font("default", 0, (int)size);
        }
    }
}

