/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonSyntaxException
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.client.shader.Shader
 *  net.minecraft.client.shader.ShaderGroup
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.utils.render;

import com.google.gson.JsonSyntaxException;
import java.io.IOException;

import me.wolfsurge.mixin.mixins.accessor.IShaderGroup;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class BlurUtil {
    private static final Minecraft MC = Minecraft.getMinecraft();
    private final ResourceLocation resourceLocation = new ResourceLocation("xeno", "shader/blur.json");
    private ShaderGroup shaderGroup;
    private Framebuffer framebuffer;
    private int lastFactor;
    private int lastWidth;
    private int lastHeight;

    public void init() {
        try {
            shaderGroup = new ShaderGroup(MC.getTextureManager(), MC.getResourceManager(), MC.getFramebuffer(), this.resourceLocation);
            shaderGroup.createBindFramebuffers(BlurUtil.MC.displayWidth, BlurUtil.MC.displayHeight);
            framebuffer = ((IShaderGroup)shaderGroup).getMainFramebuffer();
        }
        catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    private void setValues(int strength) {
        ((IShaderGroup)shaderGroup).getListShaders().get(0).getShaderManager().getShaderUniform("Radius").set(strength);
        ((IShaderGroup)shaderGroup).getListShaders().get(1).getShaderManager().getShaderUniform("Radius").set((float)strength);
        ((IShaderGroup)shaderGroup).getListShaders().get(2).getShaderManager().getShaderUniform("Radius").set((float)strength);
        ((IShaderGroup)shaderGroup).getListShaders().get(3).getShaderManager().getShaderUniform("Radius").set((float)strength);
    }

    public final void blur(int blurStrength) {
        int height;
        int width;
        ScaledResolution scaledResolution = new ScaledResolution(MC);
        int scaleFactor = scaledResolution.getScaleFactor();
        if (this.sizeHasChanged(scaleFactor, width = scaledResolution.getScaledWidth(), height = scaledResolution.getScaledHeight()) || this.framebuffer == null || this.shaderGroup == null) {
            this.init();
        }
        this.lastFactor = scaleFactor;
        this.lastWidth = width;
        this.lastHeight = height;
        this.setValues(blurStrength);
        this.framebuffer.bindFramebuffer(true);
        this.shaderGroup.render(MC.getRenderPartialTicks());
        MC.getFramebuffer().bindFramebuffer(true);
        GlStateManager.enableAlpha();
    }

    public final void blur(double x, double y, double areaWidth, double areaHeight, int blurStrength) {
        int height;
        int width;
        ScaledResolution scaledResolution = new ScaledResolution(MC);
        int scaleFactor = scaledResolution.getScaleFactor();
        if (this.sizeHasChanged(scaleFactor, width = scaledResolution.getScaledWidth(), height = scaledResolution.getScaledHeight()) || this.framebuffer == null || this.shaderGroup == null) {
            this.init();
        }
        this.lastFactor = scaleFactor;
        this.lastWidth = width;
        this.lastHeight = height;
        GL11.glEnable((int)3089);
        RenderUtils2D.scissor(x, y + 1.0, areaWidth, areaHeight - 1.0);
        this.framebuffer.bindFramebuffer(true);
        this.shaderGroup.render(MC.getRenderPartialTicks());
        this.setValues(blurStrength);
        MC.getFramebuffer().bindFramebuffer(false);
        GL11.glDisable((int)3089);
    }

    private boolean sizeHasChanged(int scaleFactor, int width, int height) {
        return this.lastFactor != scaleFactor || this.lastWidth != width || this.lastHeight != height;
    }
}

