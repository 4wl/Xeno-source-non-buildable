/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.client.shader.ShaderGroup
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.utils.render;

import me.wolfsurge.api.util.Globals;
import me.wolfsurge.mixin.mixins.accessor.IShaderGroup;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CBlur
implements Globals {
    private static ShaderGroup blurShader;
    private static Framebuffer framebuffer;
    private static int lastScale;
    private static int lastScaleWidth;
    private static int lastScaleHeight;

    public static void initShaderAndFrameBuffer() {
        try {
            blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), new ResourceLocation("shader/blur/blur.json"));
            blurShader.createBindFramebuffers(CBlur.mc.displayWidth, CBlur.mc.displayHeight);
            framebuffer = ((IShaderGroup)blurShader).getMainFramebuffer();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static void configureShader(float intensity, float blurWidth, float blurHeight) {
        if (((IShaderGroup)blurShader).getListShaders().get(0).getShaderManager().getShaderUniform("Radius") != null && ((IShaderGroup)blurShader).getListShaders().get(0).getShaderManager().getShaderUniform("Radius") != null && ((IShaderGroup)blurShader).getListShaders().get(0).getShaderManager().getShaderUniform("BlurDir") != null && ((IShaderGroup)blurShader).getListShaders().get(1).getShaderManager().getShaderUniform("BlurDir") != null) {
            ((IShaderGroup)blurShader).getListShaders().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
            ((IShaderGroup)blurShader).getListShaders().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);
            ((IShaderGroup)blurShader).getListShaders().get(0).getShaderManager().getShaderUniform("BlurDir").set(blurWidth, blurHeight);
            ((IShaderGroup)blurShader).getListShaders().get(1).getShaderManager().getShaderUniform("BlurDir").set(blurHeight, blurWidth);
        }
    }

    public static void blurRect(int x, int y, int width, int height, float intensity, float blurWidth, float blurHeight) {
        ScaledResolution resolution = new ScaledResolution(mc);
        CBlur.initShaderAndFrameBuffer();
        int scaleFactor = resolution.getScaleFactor();
        int widthFactor = resolution.getScaledWidth();
        int heightFactor = resolution.getScaledHeight();
        if (lastScale != scaleFactor || lastScaleWidth != widthFactor || lastScaleHeight != heightFactor || framebuffer == null || blurShader == null) {
            CBlur.initShaderAndFrameBuffer();
        }
        lastScale = scaleFactor;
        lastScaleWidth = widthFactor;
        lastScaleHeight = heightFactor;
        if (!OpenGlHelper.isFramebufferEnabled()) {
            return;
        }
        GL11.glScissor((int)(x * scaleFactor), (int)(CBlur.mc.displayHeight - y * scaleFactor - height * scaleFactor), (int)(width * scaleFactor), (int)(height * scaleFactor - 12));
        GL11.glEnable((int)3089);
        CBlur.configureShader(intensity, blurWidth, blurHeight);
        framebuffer.bindFramebuffer(true);
        blurShader.render(mc.getRenderPartialTicks());
        mc.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable((int)3089);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        framebuffer.framebufferRenderExt(CBlur.mc.displayWidth, CBlur.mc.displayHeight, false);
        GlStateManager.disableBlend();
        GL11.glScalef((float)scaleFactor, (float)scaleFactor, (float)0.0f);
    }
}

