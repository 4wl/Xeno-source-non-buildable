/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

public class RoundedUtils {
    static final Minecraft mc = Minecraft.getMinecraft();
    static final FontRenderer fr = RoundedUtils.mc.fontRenderer;

    public static void enableGL2D() {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
    }

    public static void disableGL2D() {
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    public static void drawSmoothRoundedRect(float x, float y, float x1, float y1, float radius, int color) {
        int i;
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        x = (float)((double)x * 2.0);
        y = (float)((double)y * 2.0);
        x1 = (float)((double)x1 * 2.0);
        y1 = (float)((double)y1 * 2.0);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        RoundedUtils.setColor(color);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)9);
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)((double)(x + radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius * -1.0), (double)((double)(y + radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius * -1.0));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)((double)(x + radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius * -1.0), (double)((double)(y1 - radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius * -1.0));
        }
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)((double)(x1 - radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius), (double)((double)(y1 - radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)((double)(x1 - radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius), (double)((double)(y + radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius));
        }
        GL11.glEnd();
        GL11.glBegin((int)2);
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)((double)(x + radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius * -1.0), (double)((double)(y + radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius * -1.0));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)((double)(x + radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius * -1.0), (double)((double)(y1 - radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius * -1.0));
        }
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)((double)(x1 - radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius), (double)((double)(y1 - radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)((double)(x1 - radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius), (double)((double)(y + radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
    }

    public static void drawRoundedRect(float x, float y, float x1, float y1, float radius, int color) {
        int i;
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        x = (float)((double)x * 2.0);
        y = (float)((double)y * 2.0);
        x1 = (float)((double)x1 * 2.0);
        y1 = (float)((double)y1 * 2.0);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        RoundedUtils.setColor(color);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)9);
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)((double)(x + radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius * -1.0), (double)((double)(y + radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius * -1.0));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)((double)(x + radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius * -1.0), (double)((double)(y1 - radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius * -1.0));
        }
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)((double)(x1 - radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius), (double)((double)(y1 - radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)((double)(x1 - radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius), (double)((double)(y + radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
    }

    public static void drawRoundedOutline(float x, float y, float x1, float y1, float radius, float lineWidth, int color) {
        int i;
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        x = (float)((double)x * 2.0);
        y = (float)((double)y * 2.0);
        x1 = (float)((double)x1 * 2.0);
        y1 = (float)((double)y1 * 2.0);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        RoundedUtils.setColor(color);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)lineWidth);
        GL11.glBegin((int)2);
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)((double)(x + radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius * -1.0), (double)((double)(y + radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius * -1.0));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)((double)(x + radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius * -1.0), (double)((double)(y1 - radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius * -1.0));
        }
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)((double)(x1 - radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius), (double)((double)(y1 - radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)((double)(x1 - radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius), (double)((double)(y + radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
    }

    public static void drawRectOutline(float x, float y, float x1, float y1, float radius, float lineWidth, int color) {
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        x = (float)((double)x * 2.0);
        y = (float)((double)y * 2.0);
        x1 = (float)((double)x1 * 2.0);
        y1 = (float)((double)y1 * 2.0);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        RoundedUtils.setColor(color);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)lineWidth);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x1, (double)y);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glVertex2d((double)x, (double)y1);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
    }

    public static void drawSelectRoundedRect(float x, float y, float x1, float y1, float radius1, float radius2, float radius3, float radius4, int color) {
        int i;
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        x = (float)((double)x * 2.0);
        y = (float)((double)y * 2.0);
        x1 = (float)((double)x1 * 2.0);
        y1 = (float)((double)y1 * 2.0);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        RoundedUtils.setColor(color);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)9);
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)((double)(x + radius1) + Math.sin((double)i * Math.PI / 180.0) * (double)radius1 * -1.0), (double)((double)(y + radius1) + Math.cos((double)i * Math.PI / 180.0) * (double)radius1 * -1.0));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)((double)(x + radius2) + Math.sin((double)i * Math.PI / 180.0) * (double)radius2 * -1.0), (double)((double)(y1 - radius2) + Math.cos((double)i * Math.PI / 180.0) * (double)radius2 * -1.0));
        }
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)((double)(x1 - radius3) + Math.sin((double)i * Math.PI / 180.0) * (double)radius3), (double)((double)(y1 - radius3) + Math.cos((double)i * Math.PI / 180.0) * (double)radius3));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)((double)(x1 - radius4) + Math.sin((double)i * Math.PI / 180.0) * (double)radius4), (double)((double)(y + radius4) + Math.cos((double)i * Math.PI / 180.0) * (double)radius4));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
    }

    public static void drawSelectRoundedOutline(float x, float y, float x1, float y1, float radius1, float radius2, float radius3, float radius4, float lineWidth, int color) {
        int i;
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        x = (float)((double)x * 2.0);
        y = (float)((double)y * 2.0);
        x1 = (float)((double)x1 * 2.0);
        y1 = (float)((double)y1 * 2.0);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        RoundedUtils.setColor(color);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)lineWidth);
        GL11.glBegin((int)2);
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)((double)(x + radius1) + Math.sin((double)i * Math.PI / 180.0) * (double)radius1 * -1.0), (double)((double)(y + radius1) + Math.cos((double)i * Math.PI / 180.0) * (double)radius1 * -1.0));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)((double)(x + radius2) + Math.sin((double)i * Math.PI / 180.0) * (double)radius2 * -1.0), (double)((double)(y1 - radius2) + Math.cos((double)i * Math.PI / 180.0) * (double)radius2 * -1.0));
        }
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)((double)(x1 - radius3) + Math.sin((double)i * Math.PI / 180.0) * (double)radius3), (double)((double)(y1 - radius3) + Math.cos((double)i * Math.PI / 180.0) * (double)radius3));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)((double)(x1 - radius4) + Math.sin((double)i * Math.PI / 180.0) * (double)radius4), (double)((double)(y + radius4) + Math.cos((double)i * Math.PI / 180.0) * (double)radius4));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
    }

    public static void setColor(int color) {
        float a = (float)(color >> 24 & 0xFF) / 255.0f;
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f((float)r, (float)g, (float)b, (float)a);
    }

    public static void drawRoundedGradientRectCorner(float x, float y, float x1, float y1, float radius, int color, int color2, int color3, int color4) {
        int i;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        x = (float)((double)x * 2.0);
        y = (float)((double)y * 2.0);
        x1 = (float)((double)x1 * 2.0);
        y1 = (float)((double)y1 * 2.0);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        RoundedUtils.setColor(color);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)9);
        for (i = 0; i <= 90; i += 3) {
            RoundedUtils.setColor(color);
        }
        GL11.glVertex2d((double)((double)(x + radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius * -1.0), (double)((double)(y + radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius * -1.0));
        for (i = 90; i <= 180; i += 3) {
            RoundedUtils.setColor(color2);
        }
        GL11.glVertex2d((double)((double)(x + radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius * -1.0), (double)((double)(y1 - radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius * -1.0));
        for (i = 0; i <= 90; i += 3) {
            RoundedUtils.setColor(color3);
        }
        GL11.glVertex2d((double)((double)(x1 - radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius), (double)((double)(y1 - radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius));
        for (i = 90; i <= 180; i += 3) {
            RoundedUtils.setColor(color4);
        }
        GL11.glVertex2d((double)((double)(x1 - radius) + Math.sin((double)i * Math.PI / 180.0) * (double)radius), (double)((double)(y + radius) + Math.cos((double)i * Math.PI / 180.0) * (double)radius));
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }
}

