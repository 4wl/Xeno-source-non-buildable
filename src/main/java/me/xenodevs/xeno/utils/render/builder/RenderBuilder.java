/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.math.BlockPos
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.utils.render.builder;

import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public class RenderBuilder {
    private boolean setup = false;
    private boolean depth = false;
    private boolean blend = false;
    private boolean texture = false;
    private boolean cull = false;
    private boolean alpha = false;
    private boolean shade = false;
    private BlockPos blockPos = BlockPos.ORIGIN;
    private Box box = Box.FILL;
    private double height = 0.0;
    private double length = 0.0;
    private double width = 0.0;
    private Color color = Color.WHITE;

    public static void glSetup() {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)1.5f);
    }

    public static void glRelease() {
        GL11.glDisable((int)2848);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void glPrepare() {
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel((int)7425);
    }

    public static void glRestore() {
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel((int)7424);
    }

    public RenderBuilder setup() {
        GlStateManager.pushMatrix();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        this.setup = true;
        return this;
    }

    public RenderBuilder depth(boolean in) {
        if (in) {
            GlStateManager.disableDepth();
            GlStateManager.depthMask((boolean)false);
        }
        this.depth = in;
        return this;
    }

    public RenderBuilder blend() {
        GlStateManager.enableBlend();
        this.blend = true;
        return this;
    }

    public RenderBuilder texture() {
        GlStateManager.disableTexture2D();
        this.texture = true;
        return this;
    }

    public RenderBuilder line(float width) {
        GlStateManager.glLineWidth((float)width);
        return this;
    }

    public RenderBuilder cull(boolean in) {
        if (this.cull) {
            GlStateManager.disableCull();
        }
        this.cull = in;
        return this;
    }

    public RenderBuilder alpha(boolean in) {
        if (this.alpha) {
            GlStateManager.disableAlpha();
        }
        this.alpha = in;
        return this;
    }

    public RenderBuilder shade(boolean in) {
        if (in) {
            GlStateManager.shadeModel((int)7425);
        }
        this.shade = in;
        return this;
    }

    public RenderBuilder build() {
        if (this.depth) {
            GlStateManager.depthMask((boolean)true);
            GlStateManager.enableDepth();
        }
        if (this.texture) {
            GlStateManager.enableTexture2D();
        }
        if (this.blend) {
            GlStateManager.disableBlend();
        }
        if (this.cull) {
            GlStateManager.enableCull();
        }
        if (this.alpha) {
            GlStateManager.enableAlpha();
        }
        if (this.shade) {
            GlStateManager.shadeModel((int)7424);
        }
        if (this.setup) {
            GL11.glDisable((int)2848);
            GlStateManager.popMatrix();
        }
        return this;
    }

    public RenderBuilder position(BlockPos in) {
        this.blockPos = in;
        return this;
    }

    public RenderBuilder height(double in) {
        this.height = in;
        return this;
    }

    public RenderBuilder width(double in) {
        this.width = in;
        return this;
    }

    public RenderBuilder length(double in) {
        this.length = in;
        return this;
    }

    public RenderBuilder color(Color in) {
        this.color = in;
        return this;
    }

    public RenderBuilder box(Box in) {
        this.box = in;
        return this;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public double getHeight() {
        return this.height;
    }

    public double getWidth() {
        return this.width;
    }

    public double getLength() {
        return this.length;
    }

    public Color getColor() {
        return this.color;
    }

    public Box getBox() {
        return this.box;
    }

    public static enum Box {
        FILL,
        OUTLINE,
        BOTH,
        GLOW,
        REVERSE,
        CLAW,
        NONE;

    }

    public static enum RenderMode {
        Fill,
        Outline,
        Both,
        Claw,
        Glow;

    }
}

