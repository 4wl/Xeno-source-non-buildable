/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.ISound$AttenuationType
 *  net.minecraft.client.audio.Sound
 *  net.minecraft.client.audio.Sound$Type
 *  net.minecraft.client.audio.SoundEventAccessor
 *  net.minecraft.client.audio.SoundHandler
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.SoundCategory
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.annotation.Nullable;
import me.wolfsurge.api.TextUtil;
import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.HudConfig;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import org.lwjgl.opengl.GL11;

public class GuiUtil
implements Globals {
    public static boolean customMainMenu = true;
    static Minecraft mc = Minecraft.getMinecraft();
    private static final BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
    private static final Tessellator tessellator = Tessellator.getInstance();

    public static void renderButtons(int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int x = sr.getScaledWidth() / 2 - 70;
        int y = 0;
        int width = 70;
        int height = 15;
        RenderUtils2D.drawRoundedRect(x, y, width, height, 1, 2, 1, 1.0, GuiUtil.mouseOver(x, y, x + width, y + height, mouseX, mouseY) ? Colors.colourInt : Color.DARK_GRAY.darker().getRGB());
        TextUtil.drawCenteredString("ClickGUI", x + width / 2, y + 2, -1);
        int x2 = sr.getScaledWidth() / 2;
        int y2 = 0;
        int width2 = 70;
        int height2 = 15;
        RenderUtils2D.drawRoundedRect(x2, y2, width2, height2, 1, 1, 2, 1.0, GuiUtil.mouseOver(x2, y2, x2 + width2, y2 + height2, mouseX, mouseY) ? Colors.colourInt : Color.DARK_GRAY.darker().getRGB());
        TextUtil.drawCenteredString("HUD", x2 + width / 2, y2 + 2, -1);
        RenderUtils2D.drawRoundedOutline(x, (double)y - 5.0, width + width2, height + 5, 2.0, 2.0, Colors.colourInt);
    }

    public static void handleButtons(int mouseX, int mouseY) {
        int height2;
        int width2;
        int y2;
        int x2;
        int height;
        int width;
        int y;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int x = sr.getScaledWidth() / 2 - 70;
        if (GuiUtil.mouseOver(x, y = 0, x + (width = 70), y + (height = 15), mouseX, mouseY)) {
            mc.displayGuiScreen((GuiScreen)Xeno.clickGui);
            if (ClickGuiModule.clickSound.enabled) {
                GuiUtil.clickSound();
            }
        }
        if (GuiUtil.mouseOver(x2 = sr.getScaledWidth() / 2, y2 = 0, x2 + (width2 = 70), y2 + (height2 = 15), mouseX, mouseY)) {
            mc.displayGuiScreen((GuiScreen)new HudConfig());
            if (ClickGuiModule.clickSound.enabled) {
                GuiUtil.clickSound();
            }
        }
    }

    public static boolean mouseOver(double minX, double minY, double maxX, double maxY, int mX, int mY) {
        return (double)mX >= minX && (double)mY >= minY && (double)mX <= maxX && (double)mY <= maxY;
    }

    public static void clickSound() {
        mc.getSoundHandler().playSound(new ISound(){

            public ResourceLocation getSoundLocation() {
                return new ResourceLocation("cosmos", "sounds/click.ogg");
            }

            @Nullable
            public SoundEventAccessor createAccessor(SoundHandler handler) {
                return new SoundEventAccessor(new ResourceLocation("cosmos", "sounds/click.ogg"), "click");
            }

            public Sound getSound() {
                return new Sound("click", 1.0f, 1.0f, 1, Sound.Type.SOUND_EVENT, false);
            }

            public SoundCategory getCategory() {
                return SoundCategory.VOICE;
            }

            public boolean canRepeat() {
                return false;
            }

            public int getRepeatDelay() {
                return 0;
            }

            public float getVolume() {
                return 1.0f;
            }

            public float getPitch() {
                return 1.0f;
            }

            public float getXPosF() {
                return GuiUtil.mc.player != null ? (float)GuiUtil.mc.player.posX : 0.0f;
            }

            public float getYPosF() {
                return GuiUtil.mc.player != null ? (float)GuiUtil.mc.player.posY : 0.0f;
            }

            public float getZPosF() {
                return GuiUtil.mc.player != null ? (float)GuiUtil.mc.player.posZ : 0.0f;
            }

            public ISound.AttenuationType getAttenuationType() {
                return ISound.AttenuationType.LINEAR;
            }
        });
    }

    public static final void scissor(double x, double y, double width, double height) {
        ScaledResolution sr = new ScaledResolution(mc);
        double scale = sr.getScaleFactor();
        y = (double)sr.getScaledHeight() - y;
        GL11.glScissor((int)((int)(x *= scale)), (int)((int)((y *= scale) - (height *= scale))), (int)((int)(width *= scale)), (int)((int)height));
    }

    public static void drawBorderedRect(double x, double y, double width, double height, double lineSize, int borderColor, int color) {
        RenderUtils2D.drawRect(x, y, x + width, y + height, color);
        RenderUtils2D.drawRect(x, y, x + width, y + lineSize, borderColor);
        RenderUtils2D.drawRect(x, y, x + lineSize, y + height, borderColor);
        RenderUtils2D.drawRect(x + width, y, x + width - lineSize, y + height, borderColor);
        RenderUtils2D.drawRect(x, y + height, x + width, y + height - lineSize, borderColor);
    }

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

    public static int getDisplayWidth() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)size.getWidth() / 2;
        return width;
    }

    public static int getDisplayHeight() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int)size.getHeight() / 2;
        return height;
    }

    public static enum type {
        HUE,
        SATURATION,
        BRIGHTNESS,
        SPECIAL;

    }
}

