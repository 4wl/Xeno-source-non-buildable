/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.utils.render.builder;

import me.wolfsurge.api.util.Globals;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class Render2DBuilder
implements Globals {
    public static void prepareScissor(int x, double y, int width, double height) {
        GL11.glPushAttrib((int)524288);
        GL11.glScissor((int)(x * new ScaledResolution(mc).getScaleFactor()), (int)((int)((double)new ScaledResolution(mc).getScaledHeight() - height) * new ScaledResolution(mc).getScaleFactor()), (int)((width - x) * new ScaledResolution(mc).getScaleFactor()), (int)((int)(height - y) * new ScaledResolution(mc).getScaleFactor()));
        GL11.glEnable((int)3089);
    }

    public static void restoreScissor() {
        GL11.glDisable((int)3089);
        GL11.glPopAttrib();
    }

    public static void prepareScale(float factorX, float factorY) {
        GlStateManager.pushMatrix();
        GlStateManager.scale((float)factorX, (float)factorY, (float)1.0f);
    }

    public static void restoreScale() {
        GlStateManager.popMatrix();
    }

    public static void scaleProportion(int x, int y, float factorX, float factorY) {
        GlStateManager.scale((float)factorX, (float)factorY, (float)1.0f);
        GlStateManager.translate((float)x, (float)y, (float)1.0f);
    }

    public static void rotate(int x, int y, int z, int angle) {
        GlStateManager.rotate((float)x, (float)y, (float)z, (float)angle);
    }

    public static void translate(int x, int y) {
        GlStateManager.translate((float)x, (float)y, (float)1.0f);
    }

    public static enum Render2DMode {
        Normal,
        Border,
        Both;

    }
}

