/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package me.wolfsurge.api;

import me.wolfsurge.api.gui.font.FontUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.utils.render.ColorUtil;
import net.minecraft.client.Minecraft;

public class TextUtil {
    public static Minecraft mc = Minecraft.getMinecraft();
    static boolean cFont = false;

    public static void drawStringWithShadow(String text, float x, float y, int color) {
        if (Xeno.moduleManager.getModule("CustomFont").isEnabled()) {
            if (cFont) {
                FontManager.comfortaa.drawStringWithShadow(text, x, y + 4.0f, color);
                return;
            }
            FontUtil.comfortaa.drawStringWithShadow(text, x, y + 4.0f, color);
        } else {
            TextUtil.mc.fontRenderer.drawStringWithShadow(text, x, y + 2.0f, color);
        }
    }

    public static int getStringWidth(String text) {
        try {
            if (Xeno.moduleManager.getModule("CustomFont").isEnabled()) {
                if (cFont) {
                    return FontManager.comfortaa.getStringWidth(text);
                }
                return FontUtil.comfortaa.getStringWidth(text);
            }
            return TextUtil.mc.fontRenderer.getStringWidth(text);
        }
        catch (NullPointerException nullPointerException) {
            return TextUtil.mc.fontRenderer.getStringWidth(text);
        }
    }

    public static void drawCenteredString(String text, float x, float y, int color) {
        if (Xeno.moduleManager.getModule("CustomFont").isEnabled()) {
            if (cFont) {
                FontManager.comfortaa.drawStringWithShadow(text, x - (float)(FontManager.comfortaa.getStringWidth(text) / 2), y + 4.0f, color);
                return;
            }
            FontUtil.comfortaa.drawStringWithShadow(text, x - (float)(FontUtil.comfortaa.getStringWidth(text) / 2), y + 4.0f, color);
        } else {
            TextUtil.mc.fontRenderer.drawStringWithShadow(text, x - (float)(TextUtil.mc.fontRenderer.getStringWidth(text) / 2), y + 2.0f, color);
        }
    }

    public static void drawClickGuiString(String text, float x, float y, float centeredX, float rightX, int color) {
        if (Xeno.moduleManager.getModule("CustomFont").isEnabled()) {
            if (ClickGuiModule.textPos.is("Center")) {
                if (cFont) {
                    FontManager.comfortaa.drawStringWithShadow(text, centeredX - (float)(FontManager.comfortaa.getStringWidth(text) / 2), y + 4.0f, color);
                    return;
                }
                FontUtil.comfortaa.drawStringWithShadow(text, centeredX - (float)(FontUtil.comfortaa.getStringWidth(text) / 2), y + 4.0f, color);
            }
            if (ClickGuiModule.textPos.is("Left")) {
                if (cFont) {
                    FontManager.comfortaa.drawStringWithShadow(text, x, y + 4.0f, color);
                    return;
                }
                FontUtil.comfortaa.drawStringWithShadow(text, x, y + 4.0f, color);
            }
            if (ClickGuiModule.textPos.is("Right")) {
                if (cFont) {
                    FontManager.comfortaa.drawStringWithShadow(text, rightX - (float)FontManager.comfortaa.getStringWidth(text) - 2.0f, y + 4.0f, color);
                    return;
                }
                FontUtil.comfortaa.drawStringWithShadow(text, rightX - (float)FontUtil.comfortaa.getStringWidth(text) - 2.0f, y + 4.0f, color);
            }
        } else {
            if (ClickGuiModule.textPos.is("Center")) {
                TextUtil.mc.fontRenderer.drawStringWithShadow(text, centeredX - (float)(TextUtil.mc.fontRenderer.getStringWidth(text) / 2), y + 1.0f, color);
            }
            if (ClickGuiModule.textPos.is("Left")) {
                TextUtil.mc.fontRenderer.drawStringWithShadow(text, x, y + 1.0f, color);
            }
            if (ClickGuiModule.textPos.is("Right")) {
                TextUtil.mc.fontRenderer.drawStringWithShadow(text, rightX - (float)TextUtil.mc.fontRenderer.getStringWidth(text) - 2.0f, y + 1.0f, color);
            }
        }
    }

    public static void drawRainbowString(String text, float x, float y, float speed, float saturation, int seperation) {
        if (Xeno.moduleManager.getModule("CustomFont").isEnabled()) {
            if (cFont) {
                FontManager.comfortaa.drawRainbowStringWithShadow(text, x, y, 0, speed, saturation, seperation);
                return;
            }
            FontUtil.comfortaa.drawRainbowStringWithShadow(text, x, y, 0, speed, saturation, seperation);
        } else {
            int count = text.length() * seperation;
            int off = 0;
            String chars = "";
            for (char c : text.toCharArray()) {
                TextUtil.mc.fontRenderer.drawStringWithShadow(String.valueOf(c), x + (float)off, y, ColorUtil.rainbowWave(speed, saturation, 1.0f, count));
                off += TextUtil.mc.fontRenderer.getStringWidth(String.valueOf(c));
                count -= seperation;
            }
        }
    }

    public static void drawVanillaCenteredString(String text, float x, float y, int color) {
        TextUtil.mc.fontRenderer.drawStringWithShadow(text, x - (float)(TextUtil.mc.fontRenderer.getStringWidth(text) / 2), y, color);
    }
}

