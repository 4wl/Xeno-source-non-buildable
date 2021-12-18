/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Vector3d
 *  javax.vecmath.Vector4d
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderGlobal
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.culling.ICamera
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.glu.GLU
 */
package me.xenodevs.xeno.utils.render;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.utils.entity.EntityHelper;
import me.xenodevs.xeno.utils.entity.EntityUtil;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import me.xenodevs.xeno.utils.render.builder.RenderBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class RenderUtils3D {
    static Minecraft mc = Minecraft.getMinecraft();
    public static ICamera camera = new Frustum();
    public static Tessellator tessellator = Tessellator.getInstance();
    public static BufferBuilder bufferbuilder = tessellator.getBuffer();
    private static final IntBuffer viewport = GLAllocation.createDirectIntBuffer((int)16);
    private static final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer((int)16);
    private static final FloatBuffer projection = GLAllocation.createDirectFloatBuffer((int)16);
    private static final FloatBuffer vector = GLAllocation.createDirectFloatBuffer((int)4);

    public static void drawLine3D(double lineWidth, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        RenderUtils3D.drawLine3D(x1, y1, z1, x2, y2, z2, color, true, lineWidth);
    }

    public static void drawLine3D(double x1, double y1, double z1, double x2, double y2, double z2, int color, boolean disableDepth, double lineWidth) {
        RenderUtils3D.enableRender3D(disableDepth);
        RenderUtils3D.setColor(color);
        GL11.glLineWidth((float)((float)lineWidth));
        GL11.glBegin((int)1);
        GL11.glVertex3d((double)x1, (double)y1, (double)z1);
        GL11.glVertex3d((double)x2, (double)y2, (double)z2);
        GL11.glEnd();
        RenderUtils3D.disableRender3D(disableDepth);
    }

    public static void drawTracer(Entity e, float lineWidth, Color col) {
        double[] pos = EntityHelper.interpolate(e);
        double x = pos[0] - RenderUtils3D.mc.getRenderManager().viewerPosX;
        double y = pos[1] - RenderUtils3D.mc.getRenderManager().viewerPosY;
        double z = pos[2] - RenderUtils3D.mc.getRenderManager().viewerPosZ;
        Vec3d eyes = new Vec3d(0.0, 0.0, 1.0).rotatePitch(-((float)Math.toRadians(RenderUtils3D.mc.player.rotationPitch))).rotateYaw(-((float)Math.toRadians(RenderUtils3D.mc.player.rotationYaw)));
        RenderUtils3D.drawLine3D(lineWidth, eyes.x, eyes.y + (double)RenderUtils3D.mc.player.getEyeHeight(), eyes.z, x, y + (double)(e.height / 2.0f), z, new Color(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha()).getRGB());
    }

    public static void drawBlockOutline(AxisAlignedBB bb, Color color, float linewidth) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)linewidth);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GL11.glDisable((int)2848);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void enableRender3D(boolean disableDepth) {
        if (disableDepth) {
            GL11.glDepthMask((boolean)false);
            GL11.glDisable((int)2929);
        }
        GL11.glDisable((int)3008);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)0.1f);
    }

    public static void drawCircle(RenderBuilder renderBuilder, Vec3d vec3d, double radius, double height, Color color) {
        RenderUtils3D.renderCircle(bufferbuilder, vec3d, radius, height, color);
        renderBuilder.build();
    }

    public static void renderCircle(BufferBuilder buffer, Vec3d vec3d, double radius, double height, Color color) {
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel((int)7425);
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i < 361; ++i) {
            buffer.pos(vec3d.x + Math.sin(Math.toRadians(i)) * radius - RenderUtils3D.mc.getRenderManager().viewerPosX, vec3d.y + height - RenderUtils3D.mc.getRenderManager().viewerPosY, vec3d.z + Math.cos(Math.toRadians(i)) * radius - RenderUtils3D.mc.getRenderManager().viewerPosZ).color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, 1.0f).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel((int)7424);
    }

    public static void solidBlockESPBox(BlockPos blockPos, float lineThickness, Color color, float alpha) {
        double x = (double)blockPos.getX() - Minecraft.getMinecraft().getRenderManager().viewerPosX;
        double y = (double)blockPos.getY() - Minecraft.getMinecraft().getRenderManager().viewerPosY;
        double z = (double)blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)lineThickness);
        GL11.glColor4d((double)0.0, (double)1.0, (double)0.0, (double)0.15f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4d((double)0.0, (double)0.0, (double)1.0, (double)alpha);
        RenderGlobal.renderFilledBox((AxisAlignedBB)new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)alpha);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static void disableRender3D(boolean enableDepth) {
        if (enableDepth) {
            GL11.glDepthMask((boolean)true);
            GL11.glEnable((int)2929);
        }
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3008);
        GL11.glDisable((int)2848);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void setColor(int colorHex) {
        float alpha = (float)(colorHex >> 24 & 0xFF) / 255.0f;
        float red = (float)(colorHex >> 16 & 0xFF) / 255.0f;
        float green = (float)(colorHex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(colorHex & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)(alpha == 0.0f ? 1.0f : alpha));
    }

    public static void drawESPBox(Entity entity, float lineThickness, Color color) {
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)lineThickness);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        Minecraft.getMinecraft().getRenderManager();
        RenderGlobal.drawSelectionBoundingBox((AxisAlignedBB)new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + (entity.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX), entity.getEntityBoundingBox().minY - entity.posY + (entity.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY), entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + (entity.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ), entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + (entity.posX - Minecraft.getMinecraft().getRenderManager().viewerPosX), entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + (entity.posY - Minecraft.getMinecraft().getRenderManager().viewerPosY), entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + (entity.posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ)), (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static void blockESPBox(BlockPos blockPos, float lineThickness, Color color, int alpha) {
        double x = (double)blockPos.getX() - Minecraft.getMinecraft().getRenderManager().viewerPosX;
        double y = (double)blockPos.getY() - Minecraft.getMinecraft().getRenderManager().viewerPosY;
        double z = (double)blockPos.getZ() - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)lineThickness);
        GL11.glColor4d((double)0.0, (double)1.0, (double)0.0, (double)0.15f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4d((double)0.0, (double)0.0, (double)1.0, (double)0.5);
        RenderGlobal.drawSelectionBoundingBox((AxisAlignedBB)new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)alpha);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
    }

    public static void drawBoxBlockPos(BlockPos blockPos, double height, double length, double width, Color color, RenderBuilder.RenderMode renderMode) {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB((double)blockPos.getX() - RenderUtils3D.mc.getRenderManager().viewerPosX, (double)blockPos.getY() - RenderUtils3D.mc.getRenderManager().viewerPosY, (double)blockPos.getZ() - RenderUtils3D.mc.getRenderManager().viewerPosZ, (double)(blockPos.getX() + 1) - RenderUtils3D.mc.getRenderManager().viewerPosX, (double)(blockPos.getY() + 1) - RenderUtils3D.mc.getRenderManager().viewerPosY, (double)(blockPos.getZ() + 1) - RenderUtils3D.mc.getRenderManager().viewerPosZ);
        RenderBuilder.glSetup();
        switch (renderMode) {
            case Fill: {
                RenderUtils3D.drawSelectionBox(axisAlignedBB, height, length, width, color);
                break;
            }
            case Outline: {
                RenderUtils3D.drawSelectionBoundingBox(axisAlignedBB, height, length, width, new Color(color.getRed(), color.getGreen(), color.getBlue(), 144));
                break;
            }
            case Both: {
                RenderUtils3D.drawSelectionBox(axisAlignedBB, height, length, width, color);
                RenderUtils3D.drawSelectionBoundingBox(axisAlignedBB, height, length, width, new Color(color.getRed(), color.getGreen(), color.getBlue(), 144));
                break;
            }
            case Glow: {
                RenderBuilder.glPrepare();
                RenderUtils3D.drawSelectionGlowFilledBox(axisAlignedBB, height, length, width, color, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0));
                RenderBuilder.glRestore();
                break;
            }
            case Claw: {
                RenderUtils3D.drawClawBox(axisAlignedBB, height, length, width, new Color(color.getRed(), color.getGreen(), color.getBlue(), 255));
            }
        }
        RenderBuilder.glRelease();
    }

    public static void drawSelectionBox(AxisAlignedBB axisAlignedBB, double height, double length, double width, Color color) {
        bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        RenderUtils3D.addChainedFilledBoxVertices(bufferbuilder, axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX + length, axisAlignedBB.maxY + height, axisAlignedBB.maxZ + width, color);
        tessellator.draw();
    }

    public static void addChainedFilledBoxVertices(BufferBuilder builder, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color color) {
        builder.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(minX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(minX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(minX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(minX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(minX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(maxX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(maxX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(maxX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(maxX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(maxX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(maxX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(maxX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(maxX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(minX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(maxX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(minX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(maxX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(maxX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(minX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(minX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(minX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(maxX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(maxX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(maxX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        builder.pos(maxX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB axisAlignedBB, double height, double length, double width, Color color) {
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        RenderUtils3D.addChainedBoundingBoxVertices(bufferbuilder, axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX + length, axisAlignedBB.maxY + height, axisAlignedBB.maxZ + width, color);
        tessellator.draw();
    }

    public static void addChainedBoundingBoxVertices(BufferBuilder buffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color color) {
        buffer.pos(minX, minY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
    }

    public static void drawSelectionGlowFilledBox(AxisAlignedBB axisAlignedBB, double height, double length, double width, Color startColor, Color endColor) {
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        RenderUtils3D.addChainedGlowBoxVertices(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX + length, axisAlignedBB.maxY + height, axisAlignedBB.maxZ + width, startColor, endColor);
        tessellator.draw();
    }

    public static void addChainedGlowBoxVertices(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color startColor, Color endColor) {
        bufferbuilder.pos(minX, minY, minZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, minY, minZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, minY, maxZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, minY, maxZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, maxY, minZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, maxY, maxZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, maxY, maxZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, maxY, minZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, minY, minZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, maxY, minZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, maxY, minZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, minY, minZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, minY, minZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, maxY, minZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, maxY, maxZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, minY, maxZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, minY, maxZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, minY, maxZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(maxX, maxY, maxZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, maxY, maxZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, minY, minZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, minY, maxZ).color((float)startColor.getRed() / 255.0f, (float)startColor.getGreen() / 255.0f, (float)startColor.getBlue() / 255.0f, (float)startColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, maxY, maxZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
        bufferbuilder.pos(minX, maxY, minZ).color((float)endColor.getRed() / 255.0f, (float)endColor.getGreen() / 255.0f, (float)endColor.getBlue() / 255.0f, (float)endColor.getAlpha() / 255.0f).endVertex();
    }

    public static void drawClawBox(AxisAlignedBB axisAlignedBB, double height, double length, double width, Color color) {
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        RenderUtils3D.addChainedClawBoxVertices(bufferbuilder, axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX + length, axisAlignedBB.maxY + height, axisAlignedBB.maxZ + width, color);
        tessellator.draw();
    }

    public static void draw2D(Entity entity, Color color) {
        GL11.glPushMatrix();
        ScaledResolution sr = new ScaledResolution(mc);
        double scaling = (double)sr.getScaleFactor() / Math.pow(sr.getScaleFactor(), 2.0);
        GlStateManager.scale((double)scaling, (double)scaling, (double)scaling);
        double x = EntityUtil.getInterpolatedPos((Entity)entity, (float)RenderUtils3D.mc.getRenderPartialTicks()).x;
        double y = EntityUtil.getInterpolatedPos((Entity)entity, (float)RenderUtils3D.mc.getRenderPartialTicks()).y;
        double z = EntityUtil.getInterpolatedPos((Entity)entity, (float)RenderUtils3D.mc.getRenderPartialTicks()).z;
        double width = (double)entity.width / 1.5;
        double height = (double)entity.height + (entity.isSneaking() ? -0.3 : 0.2);
        AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
        List<Vector3d> vectors = Arrays.asList(new Vector3d[]{new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ)});
        RenderUtils3D.mc.entityRenderer.setupCameraTransform(mc.getRenderPartialTicks(), 0);
        Vector4d position = null;
        for (Vector3d vector : vectors) {
            vector = RenderUtils3D.project2D(sr, vector.x - RenderUtils3D.mc.getRenderManager().viewerPosX, vector.y - RenderUtils3D.mc.getRenderManager().viewerPosY, vector.z - RenderUtils3D.mc.getRenderManager().viewerPosZ);
            if (vector == null || !(vector.z >= 0.0) || !(vector.z < 1.0)) continue;
            if (position == null) {
                position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
            }
            position.x = Math.min(vector.x, position.x);
            position.y = Math.min(vector.y, position.y);
            position.z = Math.max(vector.x, position.z);
            position.w = Math.max(vector.y, position.w);
        }
        RenderUtils3D.mc.entityRenderer.setupOverlayRendering();
        if (position != null) {
            double posX = position.x;
            double posY = position.y;
            double endPosX = position.z;
            double endPosY = position.w;
            int color2 = 0;
            RenderUtils2D.drawRect(posX + 0.5, posY, posX - 1.0, posY + (endPosY - posY) / 4.0 + 0.5, color.getRGB());
            RenderUtils2D.drawRect(posX - 1.0, endPosY, posX + 0.5, endPosY - (endPosY - posY) / 4.0 - 0.5, color.getRGB());
            RenderUtils2D.drawRect(posX - 1.0, posY - 0.5, posX + (endPosX - posX) / 3.0 + 0.5, posY + 1.0, color.getRGB());
            RenderUtils2D.drawRect(endPosX - (endPosX - posX) / 3.0 - 0.5, posY - 0.5, endPosX, posY + 1.0, color.getRGB());
            RenderUtils2D.drawRect(endPosX - 1.0, posY, endPosX + 0.5, posY + (endPosY - posY) / 4.0 + 0.5, color.getRGB());
            RenderUtils2D.drawRect(endPosX - 1.0, endPosY, endPosX + 0.5, endPosY - (endPosY - posY) / 4.0 - 0.5, color.getRGB());
            RenderUtils2D.drawRect(posX - 1.0, endPosY - 1.0, posX + (endPosX - posX) / 3.0 + 0.5, endPosY + 0.5, color.getRGB());
            RenderUtils2D.drawRect(endPosX - (endPosX - posX) / 3.0 - 0.5, endPosY - 1.0, endPosX + 0.5, endPosY + 0.5, color.getRGB());
            RenderUtils2D.drawRect(posX, posY, posX - 0.5, posY + (endPosY - posY) / 4.0, color2);
            RenderUtils2D.drawRect(posX, endPosY, posX - 0.5, endPosY - (endPosY - posY) / 4.0, color2);
            RenderUtils2D.drawRect(posX - 0.5, posY, posX + (endPosX - posX) / 3.0, posY + 0.5, color2);
            RenderUtils2D.drawRect(endPosX - (endPosX - posX) / 3.0, posY, endPosX, posY + 0.5, color2);
            RenderUtils2D.drawRect(endPosX - 0.5, posY, endPosX, posY + (endPosY - posY) / 4.0, color2);
            RenderUtils2D.drawRect(endPosX - 0.5, endPosY, endPosX, endPosY - (endPosY - posY) / 4.0, color2);
            RenderUtils2D.drawRect(posX, endPosY - 0.5, posX + (endPosX - posX) / 3.0, endPosY, color2);
            RenderUtils2D.drawRect(endPosX - (endPosX - posX) / 3.0, endPosY - 0.5, endPosX - 0.5, endPosY, color2);
        }
        GL11.glPopMatrix();
        GlStateManager.enableBlend();
        RenderUtils3D.mc.entityRenderer.setupOverlayRendering();
    }

    private static Vector3d project2D(ScaledResolution scaledResolution, double x, double y, double z) {
        GL11.glGetFloat((int)2982, (FloatBuffer)modelview);
        GL11.glGetFloat((int)2983, (FloatBuffer)projection);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
        if (GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)modelview, (FloatBuffer)projection, (IntBuffer)viewport, (FloatBuffer)vector)) {
            return new Vector3d((double)(vector.get(0) / (float)scaledResolution.getScaleFactor()), (double)(((float)Display.getHeight() - vector.get(1)) / (float)scaledResolution.getScaleFactor()), (double)vector.get(2));
        }
        return null;
    }

    public static void setColor(Color c) {
        GL11.glColor4d((double)((float)c.getRed() / 255.0f), (double)((float)c.getGreen() / 255.0f), (double)((float)c.getBlue() / 255.0f), (double)((float)c.getAlpha() / 255.0f));
    }

    public static void addChainedClawBoxVertices(BufferBuilder buffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color color) {
        buffer.pos(minX, minY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX, minY, maxZ - 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, minY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX, minY, minZ + 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX, minY, maxZ - 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX, minY, minZ + 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, minY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX - 0.8, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, minY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX - 0.8, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX + 0.8, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX + 0.8, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, minY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX, minY + 0.2, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, minY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX, minY + 0.2, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX, minY + 0.2, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, minY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX, minY + 0.2, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX, maxY, maxZ - 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX, maxY, minZ + 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX, maxY, maxZ - 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX, maxY, minZ + 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX - 0.8, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX - 0.8, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX + 0.8, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX + 0.8, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX, maxY - 0.2, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(minX, maxY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(minX, maxY - 0.2, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, minZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX, maxY - 0.2, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        buffer.pos(maxX, maxY, maxZ).color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), 0.0f).endVertex();
        buffer.pos(maxX, maxY - 0.2, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    }

    public static void drawNametagFromBlockPos(BlockPos pos, float height, boolean customFont, String text) {
        GlStateManager.pushMatrix();
        RenderUtils3D.glBillboardDistanceScaled((float)pos.getX() + 0.5f, (float)pos.getY() + height, (float)pos.getZ() + 0.5f, (EntityPlayer)RenderUtils3D.mc.player, 1.0f);
        GlStateManager.disableDepth();
        GlStateManager.translate((double)(-((double)FontManager.comfortaa.getStringWidth(text) / 2.0)), (double)0.0, (double)0.0);
        if (customFont) {
            FontManager.comfortaa.drawStringWithShadow(text, 2.0f, 4.0f, -1);
        } else {
            RenderUtils3D.mc.fontRenderer.drawStringWithShadow(text, 2.0f, 4.0f, -1);
        }
        GlStateManager.popMatrix();
    }

    public static void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player, float scale) {
        RenderUtils3D.glBillboard(x, y, z);
        int distance = (int)player.getDistance((double)x, (double)y, (double)z);
        float scaleDistance = (float)distance / 2.0f / (2.0f + (2.0f - scale));
        if (scaleDistance < 1.0f) {
            scaleDistance = 1.0f;
        }
        GlStateManager.scale((float)scaleDistance, (float)scaleDistance, (float)scaleDistance);
    }

    public static void glBillboard(float x, float y, float z) {
        float scale = 0.02666667f;
        GlStateManager.translate((double)((double)x - RenderUtils3D.mc.getRenderManager().renderPosX), (double)((double)y - RenderUtils3D.mc.getRenderManager().renderPosY), (double)((double)z - RenderUtils3D.mc.getRenderManager().renderPosZ));
        GlStateManager.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(-RenderUtils3D.mc.player.rotationYaw), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)RenderUtils3D.mc.player.rotationPitch, (float)(RenderUtils3D.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.scale((float)(-scale), (float)(-scale), (float)scale);
    }
}

