/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.item.ItemStack
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.utils.render;

import java.awt.Point;
import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.utils.render.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class RenderUtils2D
implements Globals {
    private static final BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
    private static final Tessellator tessellator = Tessellator.getInstance();

    public static void drawRectMC(int left, int top, int right, int bottom, int color) {
        Gui.drawRect((int)left, (int)top, (int)right, (int)bottom, (int)color);
    }

    public static void scissor(int x, int y, int x2, int y2) {
        GL11.glScissor((int)(x * new ScaledResolution(mc).getScaleFactor()), (int)((new ScaledResolution(mc).getScaledHeight() - y2) * new ScaledResolution(mc).getScaleFactor()), (int)((x2 - x) * new ScaledResolution(mc).getScaleFactor()), (int)((y2 - y) * new ScaledResolution(mc).getScaleFactor()));
    }

    public static void gradient(int minX, int minY, int maxX, int maxY, int startColor, int endColor, boolean left) {
        if (left) {
            GL11.glEnable((int)3042);
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glShadeModel((int)7425);
            GL11.glBegin((int)9);
            GL11.glColor4f((float)((float)(startColor >> 16 & 0xFF) / 255.0f), (float)((float)(startColor >> 8 & 0xFF) / 255.0f), (float)((float)(startColor & 0xFF) / 255.0f), (float)((float)(startColor >> 24 & 0xFF) / 255.0f));
            GL11.glVertex2f((float)minX, (float)minY);
            GL11.glVertex2f((float)minX, (float)maxY);
            GL11.glColor4f((float)((float)(endColor >> 16 & 0xFF) / 255.0f), (float)((float)(endColor >> 8 & 0xFF) / 255.0f), (float)((float)(endColor & 0xFF) / 255.0f), (float)((float)(endColor >> 24 & 0xFF) / 255.0f));
            GL11.glVertex2f((float)maxX, (float)maxY);
            GL11.glVertex2f((float)maxX, (float)minY);
            GL11.glEnd();
            GL11.glShadeModel((int)7424);
            GL11.glEnable((int)3553);
            GL11.glDisable((int)3042);
        } else {
            RenderUtils2D.drawGradientRect(minX, minY, maxX, maxY, startColor, endColor, false);
        }
    }

    public static void gradient(double mx, double my, double max, double may, int startColor, int endColor, boolean left) {
        float minX = (float)mx;
        float minY = (float)my;
        float maxX = (float)max;
        float maxY = (float)may;
        if (left) {
            GL11.glEnable((int)3042);
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glShadeModel((int)7425);
            GL11.glBegin((int)9);
            GL11.glColor4f((float)((float)(startColor >> 16 & 0xFF) / 255.0f), (float)((float)(startColor >> 8 & 0xFF) / 255.0f), (float)((float)(startColor & 0xFF) / 255.0f), (float)((float)(startColor >> 24 & 0xFF) / 255.0f));
            GL11.glVertex2f((float)minX, (float)minY);
            GL11.glVertex2f((float)minX, (float)maxY);
            GL11.glColor4f((float)((float)(endColor >> 16 & 0xFF) / 255.0f), (float)((float)(endColor >> 8 & 0xFF) / 255.0f), (float)((float)(endColor & 0xFF) / 255.0f), (float)((float)(endColor >> 24 & 0xFF) / 255.0f));
            GL11.glVertex2f((float)maxX, (float)maxY);
            GL11.glVertex2f((float)maxX, (float)minY);
            GL11.glEnd();
            GL11.glShadeModel((int)7424);
            GL11.glEnable((int)3553);
            GL11.glDisable((int)3042);
        } else {
            RenderUtils2D.drawGradientRect(minX, minY, maxX, maxY, startColor, endColor, false);
        }
    }

    public static void drawLeftGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel((int)7425);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)right, (double)top, 0.0).color((float)(endColor >> 24 & 0xFF) / 255.0f, (float)(endColor >> 16 & 0xFF) / 255.0f, (float)(endColor >> 8 & 0xFF) / 255.0f, (float)(endColor >> 24 & 0xFF) / 255.0f).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0).color((float)(startColor >> 16 & 0xFF) / 255.0f, (float)(startColor >> 8 & 0xFF) / 255.0f, (float)(startColor & 0xFF) / 255.0f, (float)(startColor >> 24 & 0xFF) / 255.0f).endVertex();
        bufferbuilder.pos((double)left, (double)bottom, 0.0).color((float)(startColor >> 16 & 0xFF) / 255.0f, (float)(startColor >> 8 & 0xFF) / 255.0f, (float)(startColor & 0xFF) / 255.0f, (float)(startColor >> 24 & 0xFF) / 255.0f).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0).color((float)(endColor >> 24 & 0xFF) / 255.0f, (float)(endColor >> 16 & 0xFF) / 255.0f, (float)(endColor >> 8 & 0xFF) / 255.0f, (float)(endColor >> 24 & 0xFF) / 255.0f).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel((int)7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawLeftGradientRect(double left, double top, double right, double bottom, int startColor, int endColor) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel((int)7425);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 0.0).color((float)(endColor >> 24 & 0xFF) / 255.0f, (float)(endColor >> 16 & 0xFF) / 255.0f, (float)(endColor >> 8 & 0xFF) / 255.0f, (float)(endColor >> 24 & 0xFF) / 255.0f).endVertex();
        bufferbuilder.pos(left, top, 0.0).color((float)(startColor >> 16 & 0xFF) / 255.0f, (float)(startColor >> 8 & 0xFF) / 255.0f, (float)(startColor & 0xFF) / 255.0f, (float)(startColor >> 24 & 0xFF) / 255.0f).endVertex();
        bufferbuilder.pos(left, bottom, 0.0).color((float)(startColor >> 16 & 0xFF) / 255.0f, (float)(startColor >> 8 & 0xFF) / 255.0f, (float)(startColor & 0xFF) / 255.0f, (float)(startColor >> 24 & 0xFF) / 255.0f).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).color((float)(endColor >> 24 & 0xFF) / 255.0f, (float)(endColor >> 16 & 0xFF) / 255.0f, (float)(endColor >> 8 & 0xFF) / 255.0f, (float)(endColor >> 24 & 0xFF) / 255.0f).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel((int)7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawRectWH(int x, int y, int width, int height, int colour) {
        RenderUtils2D.drawRectMC(x, y, x + width, y + height, colour);
    }

    public static void drawRectWH(double x, double y, double width, double height, int colour) {
        RenderUtils2D.drawRect(x, y, x + width, y + height, colour);
    }

    public static void drawRoundedRect(double x, double y, double width, double height, double radius, int color) {
        int i;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        double x1 = x + width;
        double y1 = y + height;
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        x *= 2.0;
        y *= 2.0;
        x1 *= 2.0;
        y1 *= 2.0;
        GL11.glDisable((int)3553);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)9);
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x + radius + Math.sin((double)i * Math.PI / 180.0) * (radius * -1.0)), (double)(y + radius + Math.cos((double)i * Math.PI / 180.0) * (radius * -1.0)));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)(x + radius + Math.sin((double)i * Math.PI / 180.0) * (radius * -1.0)), (double)(y1 - radius + Math.cos((double)i * Math.PI / 180.0) * (radius * -1.0)));
        }
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x1 - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(y1 - radius + Math.cos((double)i * Math.PI / 180.0) * radius));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)(x1 - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(y + radius + Math.cos((double)i * Math.PI / 180.0) * radius));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRoundedRect(double x, double y, double width, double height, int r1, int r2, int r3, double r4, int color) {
        int i;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        double x1 = x + width;
        double y1 = y + height;
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        x *= 2.0;
        y *= 2.0;
        x1 *= 2.0;
        y1 *= 2.0;
        GL11.glDisable((int)3553);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)9);
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x + (double)r1 + Math.sin((double)i * Math.PI / 180.0) * (double)(r1 * -1)), (double)(y + (double)r1 + Math.cos((double)i * Math.PI / 180.0) * (double)(r1 * -1)));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)(x + (double)r2 + Math.sin((double)i * Math.PI / 180.0) * (double)(r2 * -1)), (double)(y1 - (double)r2 + Math.cos((double)i * Math.PI / 180.0) * (double)(r2 * -1)));
        }
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x1 - (double)r3 + Math.sin((double)i * Math.PI / 180.0) * (double)r3), (double)(y1 - (double)r3 + Math.cos((double)i * Math.PI / 180.0) * (double)r3));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)(x1 - r4 + Math.sin((double)i * Math.PI / 180.0) * r4), (double)(y + r4 + Math.cos((double)i * Math.PI / 180.0) * r4));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static final void scissor(double x, double y, double width, double height) {
        ScaledResolution sr = new ScaledResolution(mc);
        double scale = sr.getScaleFactor();
        y = (double)sr.getScaledHeight() - y;
        GL11.glScissor((int)((int)(x *= scale)), (int)((int)((y *= scale) - (height *= scale))), (int)((int)(width *= scale)), (int)((int)height));
    }

    public static void drawRoundedOutline(double x, double y, double width, double height, double radius, double lineWidth, int color) {
        int i;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        double x1 = x + width;
        double y1 = y + height;
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        x *= 2.0;
        y *= 2.0;
        x1 *= 2.0;
        y1 *= 2.0;
        GL11.glDisable((int)3553);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glLineWidth((float)Float.parseFloat(String.valueOf(lineWidth)));
        GL11.glEnable((int)2848);
        GL11.glBegin((int)2);
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x + radius + Math.sin((double)i * Math.PI / 180.0) * (radius * -1.0)), (double)(y + radius + Math.cos((double)i * Math.PI / 180.0) * (radius * -1.0)));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)(x + radius + Math.sin((double)i * Math.PI / 180.0) * (radius * -1.0)), (double)(y1 - radius + Math.cos((double)i * Math.PI / 180.0) * (radius * -1.0)));
        }
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x1 - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(y1 - radius + Math.cos((double)i * Math.PI / 180.0) * radius));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)(x1 - radius + Math.sin((double)i * Math.PI / 180.0) * radius), (double)(y + radius + Math.cos((double)i * Math.PI / 180.0) * radius));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRoundedOutline(double x, double y, double width, double height, double r1, double r2, double r3, double r4, double lineWidth, int color) {
        int i;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        double x1 = x + width;
        double y1 = y + height;
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glPushAttrib((int)0);
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        x *= 2.0;
        y *= 2.0;
        x1 *= 2.0;
        y1 *= 2.0;
        GL11.glDisable((int)3553);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glLineWidth((float)Float.parseFloat(String.valueOf(lineWidth)));
        GL11.glEnable((int)2848);
        GL11.glBegin((int)2);
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x + r1 + Math.sin((double)i * Math.PI / 180.0) * (r1 * -1.0)), (double)(y + r1 + Math.cos((double)i * Math.PI / 180.0) * (r1 * -1.0)));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)(x + r2 + Math.sin((double)i * Math.PI / 180.0) * (r2 * -1.0)), (double)(y1 - r2 + Math.cos((double)i * Math.PI / 180.0) * (r2 * -1.0)));
        }
        for (i = 0; i <= 90; i += 3) {
            GL11.glVertex2d((double)(x1 - r3 + Math.sin((double)i * Math.PI / 180.0) * r3), (double)(y1 - r3 + Math.cos((double)i * Math.PI / 180.0) * r3));
        }
        for (i = 90; i <= 180; i += 3) {
            GL11.glVertex2d((double)(x1 - r4 + Math.sin((double)i * Math.PI / 180.0) * r4), (double)(y + r4 + Math.cos((double)i * Math.PI / 180.0) * r4));
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        GL11.glPopAttrib();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRect(float left, float top, float right, float bottom, int startColor, int endColor, boolean hovered) {
        if (hovered) {
            startColor = ColorUtil.shadeColour(startColor, -20);
            endColor = ColorUtil.shadeColour(endColor, -20);
        }
        float c = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float c1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float c2 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float c3 = (float)(startColor & 0xFF) / 255.0f;
        float c4 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float c5 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float c6 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float c7 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel((int)7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)right, (double)top, 0.0).color(c1, c2, c3, c).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0).color(c1, c2, c3, c).endVertex();
        bufferbuilder.pos((double)left, (double)bottom, 0.0).color(c5, c6, c7, c4).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0).color(c5, c6, c7, c4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel((int)7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawGradientRect(double left, double top, double right, double bottom, int startColor, int endColor, boolean hovered) {
        if (hovered) {
            startColor = ColorUtil.shadeColour(startColor, -20);
            endColor = ColorUtil.shadeColour(endColor, -20);
        }
        float c = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float c1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float c2 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float c3 = (float)(startColor & 0xFF) / 255.0f;
        float c4 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float c5 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float c6 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float c7 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel((int)7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 0.0).color(c1, c2, c3, c).endVertex();
        bufferbuilder.pos(left, top, 0.0).color(c1, c2, c3, c).endVertex();
        bufferbuilder.pos(left, bottom, 0.0).color(c5, c6, c7, c4).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).color(c5, c6, c7, c4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel((int)7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void renderItem(ItemStack item, Point pos) {
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask((boolean)true);
        GL11.glPushAttrib((int)524288);
        GL11.glDisable((int)3089);
        GlStateManager.clear((int)256);
        GL11.glPopAttrib();
        GlStateManager.enableDepth();
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getRenderItem().zLevel = -150.0f;
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(item, pos.x, pos.y);
        Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRenderer, item, pos.x, pos.y);
        RenderHelper.disableStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().zLevel = 0.0f;
        GlStateManager.popMatrix();
        GlStateManager.disableDepth();
        GlStateManager.depthMask((boolean)false);
    }

    public static void drawBorderedRect(double left, double top, double right, double bottom, int borderWidth, int insideColor, int borderColor, boolean hover) {
        if (hover) {
            insideColor = ColorUtil.shadeColour(insideColor, -20);
            borderColor = ColorUtil.shadeColour(borderColor, -20);
        }
        RenderUtils2D.drawRectBase(left + (double)borderWidth, top + (double)borderWidth, right - (double)borderWidth, bottom - (double)borderWidth, insideColor);
        RenderUtils2D.drawRectBase(left, top + (double)borderWidth, left + (double)borderWidth, bottom - (double)borderWidth, borderColor);
        RenderUtils2D.drawRectBase(right - (double)borderWidth, top + (double)borderWidth, right, bottom - (double)borderWidth, borderColor);
        RenderUtils2D.drawRectBase(left, top, right, top + (double)borderWidth, borderColor);
        RenderUtils2D.drawRectBase(left, bottom - (double)borderWidth, right, bottom, borderColor);
    }

    public static void drawPickerBase(double px, double py, double pw, double ph, float red, float green, float blue, float alpha) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        GL11.glBegin((int)9);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        float pickerX = (float)px;
        float pickerY = (float)py;
        float pickerHeight = (float)ph;
        float pickerWidth = (float)pw;
        GL11.glVertex2f((float)pickerX, (float)pickerY);
        GL11.glVertex2f((float)pickerX, (float)(pickerY + pickerHeight));
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glVertex2f((float)(pickerX + pickerWidth), (float)(pickerY + pickerHeight));
        GL11.glVertex2f((float)(pickerX + pickerWidth), (float)pickerY);
        GL11.glEnd();
        GL11.glDisable((int)3008);
        GL11.glBegin((int)9);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glVertex2f((float)pickerX, (float)pickerY);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glVertex2f((float)pickerX, (float)(pickerY + pickerHeight));
        GL11.glVertex2f((float)(pickerX + pickerWidth), (float)(pickerY + pickerHeight));
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glVertex2f((float)(pickerX + pickerWidth), (float)pickerY);
        GL11.glEnd();
        GL11.glEnable((int)3008);
        GL11.glShadeModel((int)7424);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
    }

    public static void drawRectBase(double left, double top, double right, double bottom, int color) {
        double side;
        if (left < right) {
            side = left;
            left = (int)right;
            right = (int)side;
        }
        if (top < bottom) {
            side = top;
            top = bottom;
            bottom = side;
        }
        GlStateManager.enableBlend();
        GL11.glDisable((int)3042);
        GL11.glDisable((int)3008);
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.color((float)((float)(color >> 16 & 0xFF) / 255.0f), (float)((float)(color >> 8 & 0xFF) / 255.0f), (float)((float)(color & 0xFF) / 255.0f), (float)((float)(color >> 24 & 0xFF) / 255.0f));
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, top, 0.0).endVertex();
        bufferbuilder.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GL11.glEnable((int)3042);
        GL11.glEnable((int)3008);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GlStateManager.color((float)f, (float)f1, (float)f2, (float)f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawModalRectWithCustomSizedTexture(double x, double y, float u, float v, double width, double height, float textureWidth, float textureHeight) {
        float f = 1.0f / textureWidth;
        float f1 = 1.0f / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0.0).tex((double)(u * f), (double)((v + (float)height) * f1)).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0).tex((double)((u + (float)width) * f), (double)((v + (float)height) * f1)).endVertex();
        bufferbuilder.pos(x + width, y, 0.0).tex((double)((u + (float)width) * f), (double)(v * f1)).endVertex();
        bufferbuilder.pos(x, y, 0.0).tex((double)(u * f), (double)(v * f1)).endVertex();
        tessellator.draw();
    }
}

