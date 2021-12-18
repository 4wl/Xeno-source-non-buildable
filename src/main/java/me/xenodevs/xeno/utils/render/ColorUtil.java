/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.utils.render;

import java.awt.Color;
import org.lwjgl.opengl.GL11;

public class ColorUtil {
    public static Color shadeColour(Color color, int precent) {
        int r = color.getRed() * (100 + precent) / 100;
        int g = color.getGreen() * (100 + precent) / 100;
        int b = color.getBlue() * (100 + precent) / 100;
        return new Color(r, g, b);
    }

    public static int shadeColour(int color, int precent) {
        int r = ((color & 0xFF0000) >> 16) * (100 + precent) / 100;
        int g = ((color & 0xFF00) >> 8) * (100 + precent) / 100;
        int b = (color & 0xFF) * (100 + precent) / 100;
        return new Color(r, g, b).hashCode();
    }

    public static Color releasedDynamicRainbow(int delay, int alpha) {
        double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 20.0);
        Color c = Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), 1.0f, 1.0f);
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }

    public static void hexColor(int hexColor) {
        float red = (float)(hexColor >> 16 & 0xFF) / 255.0f;
        float green = (float)(hexColor >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hexColor & 0xFF) / 255.0f;
        float alpha = (float)(hexColor >> 24 & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static Color getSinState(Color c1, double delay, int a, type t) {
        double sineState = Math.sin(2400.0 - (double)System.currentTimeMillis() / delay) * Math.sin(2400.0 - (double)System.currentTimeMillis() / delay);
        float[] hsb = Color.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), null);
        Color c = null;
        switch (t) {
            case HUE: {
                sineState /= (double)hsb[0];
                sineState = Math.min(1.0, sineState);
                c = Color.getHSBColor((float)sineState, 1.0f, 1.0f);
                break;
            }
            case SATURATION: {
                sineState /= (double)hsb[1];
                sineState = Math.min(1.0, sineState);
                c = Color.getHSBColor(hsb[0], (float)sineState, 1.0f);
                break;
            }
            case BRIGHTNESS: {
                sineState /= (double)hsb[2];
                sineState = Math.min(1.0, sineState);
                c = Color.getHSBColor(hsb[0], 1.0f, (float)sineState);
                break;
            }
            case SPECIAL: {
                sineState /= (double)hsb[1];
                sineState = Math.min(1.0, sineState);
                c = Color.getHSBColor(hsb[0], 1.0f, (float)sineState);
            }
        }
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
    }

    public static Color getSinState(Color c1, Color c2, double delay, int a) {
        double sineState = Math.sin(2400.0 - (double)System.currentTimeMillis() / delay) * Math.sin(2400.0 - (double)System.currentTimeMillis() / delay);
        float[] hsb = Color.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), null);
        float[] hsb2 = Color.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(), null);
        sineState /= (double)hsb[0];
        sineState *= sineState / (double)hsb2[0];
        sineState = Math.min(1.0, sineState);
        Color c = Color.getHSBColor((float)sineState, 1.0f, 1.0f);
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
    }

    public static int rainbow(int delay) {
        double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 20.0);
        return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), 0.5f, 1.0f).getRGB();
    }

    public static int rainbowVibrant(int delay) {
        double rainbowState = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 20.0);
        return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), 1.0f, 1.0f).getRGB();
    }

    public static int rainbowWave(float seconds, float saturation, float brightness, int index) {
        float hue = (float)((System.currentTimeMillis() + (long)index) % (long)((int)(seconds * 1000.0f))) / (seconds * 1000.0f);
        int color = Color.HSBtoRGB(hue, saturation, brightness);
        return color;
    }

    public static enum type {
        HUE,
        SATURATION,
        BRIGHTNESS,
        SPECIAL;

    }
}

