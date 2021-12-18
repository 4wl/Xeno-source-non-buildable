/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderLivingBase
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.player.EntityPlayer
 *  org.lwjgl.opengl.GL11
 */
package me.wolfsurge.mixin.mixins;

import java.awt.Color;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.module.modules.render.Chams;
import me.xenodevs.xeno.module.modules.render.ESP;
import me.xenodevs.xeno.utils.render.OutlineUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderLivingBase.class})
public abstract class MixinRenderLivingBase<T extends EntityLivingBase>
extends Render<T> {
    @Shadow
    protected ModelBase mainModel;

    protected MixinRenderLivingBase(RenderManager renderManager) {
        super(null);
    }

    @Inject(method={"renderModel"}, at={@At(value="HEAD")})
    protected void renderModel(T entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float scaleFactor, CallbackInfo g) {
        boolean flag1;
        boolean flag = !entitylivingbaseIn.isInvisible();
        boolean bl = flag1 = !flag && !entitylivingbaseIn.isInvisibleToPlayer((EntityPlayer)Minecraft.getMinecraft().player);
        if (flag || flag1) {
            if (!this.bindEntityTexture((Entity)entitylivingbaseIn)) {
                return;
            }
            if (flag1) {
                GlStateManager.pushMatrix();
                GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)0.15f);
                GlStateManager.depthMask((boolean)false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc((int)770, (int)771);
                GlStateManager.alphaFunc((int)516, (float)0.003921569f);
            }
            Color passivec = Chams.passiveColour.getColor();
            Color mobc = Chams.mobColour.getColor();
            Color playerc = Chams.playerColour.getColor();
            if (Xeno.moduleManager.isModuleEnabled("Chams") && Chams.mode.is("Fill")) {
                if (entitylivingbaseIn instanceof EntityLiving && !(entitylivingbaseIn instanceof EntityMob) && Chams.passive.enabled) {
                    GL11.glPushAttrib((int)1048575);
                    GL11.glDisable((int)3008);
                    GL11.glDisable((int)3553);
                    GL11.glDisable((int)2896);
                    GL11.glEnable((int)3042);
                    GL11.glBlendFunc((int)770, (int)771);
                    GL11.glLineWidth((float)1.5f);
                    GL11.glEnable((int)2960);
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                    GL11.glEnable((int)10754);
                    OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)240.0f, (float)240.0f);
                    GL11.glColor3f((float)((float)passivec.getRed() / 255.0f), (float)((float)passivec.getGreen() / 255.0f), (float)((float)passivec.getBlue() / 255.0f));
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    GL11.glEnable((int)3042);
                    GL11.glEnable((int)2896);
                    GL11.glEnable((int)3553);
                    GL11.glEnable((int)3008);
                    GL11.glPopAttrib();
                }
                if (entitylivingbaseIn instanceof EntityMob && Chams.mobs.enabled) {
                    GL11.glPushAttrib((int)1048575);
                    GL11.glDisable((int)3008);
                    GL11.glDisable((int)3553);
                    GL11.glDisable((int)2896);
                    GL11.glEnable((int)3042);
                    GL11.glBlendFunc((int)770, (int)771);
                    GL11.glLineWidth((float)1.5f);
                    GL11.glEnable((int)2960);
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                    GL11.glEnable((int)10754);
                    OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)240.0f, (float)240.0f);
                    GL11.glColor3f((float)((float)mobc.getRed() / 255.0f), (float)((float)mobc.getGreen() / 255.0f), (float)((float)mobc.getBlue() / 255.0f));
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    GL11.glEnable((int)3042);
                    GL11.glEnable((int)2896);
                    GL11.glEnable((int)3553);
                    GL11.glEnable((int)3008);
                    GL11.glPopAttrib();
                }
                if (entitylivingbaseIn instanceof EntityPlayer && Chams.players.enabled) {
                    GL11.glPushAttrib((int)1048575);
                    GL11.glDisable((int)3008);
                    GL11.glDisable((int)3553);
                    GL11.glDisable((int)2896);
                    GL11.glEnable((int)3042);
                    GL11.glBlendFunc((int)770, (int)771);
                    GL11.glLineWidth((float)1.5f);
                    GL11.glEnable((int)2960);
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                    GL11.glEnable((int)10754);
                    OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)240.0f, (float)240.0f);
                    GL11.glColor3f((float)((float)playerc.getRed() / 255.0f), (float)((float)playerc.getGreen() / 255.0f), (float)((float)playerc.getBlue() / 255.0f));
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    GL11.glEnable((int)3042);
                    GL11.glEnable((int)2896);
                    GL11.glEnable((int)3553);
                    GL11.glEnable((int)3008);
                    GL11.glPopAttrib();
                }
            } else {
                this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
            }
            if (Xeno.moduleManager.getModule("ESP") != null && Xeno.moduleManager.getModule("ESP").isEnabled() && ESP.mode.is("Outline")) {
                if (entitylivingbaseIn.isInvisible() && !ESP.invisibles.enabled) {
                    return;
                }
                if (entitylivingbaseIn instanceof EntityPlayer && entitylivingbaseIn != Minecraft.getMinecraft().player && ESP.players.enabled) {
                    Color n = ESP.playerColour.getColor();
                    OutlineUtils.setColor(n);
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderOne((float)ESP.lineWidth.getDoubleValue());
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderTwo();
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderThree();
                    OutlineUtils.renderFour(n);
                    OutlineUtils.setColor(n);
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderFive();
                } else if (entitylivingbaseIn instanceof EntityLiving && !(entitylivingbaseIn instanceof EntityMob) && ESP.passive.enabled) {
                    Color n = ESP.passiveColour.getColor();
                    OutlineUtils.setColor(n);
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderOne((float)ESP.lineWidth.getDoubleValue());
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderTwo();
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderThree();
                    OutlineUtils.renderFour(n);
                    OutlineUtils.setColor(n);
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderFive();
                } else if (entitylivingbaseIn instanceof EntityMob && ESP.mobs.enabled) {
                    Color n = ESP.mobColour.getColor();
                    OutlineUtils.setColor(n);
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderOne((float)ESP.lineWidth.getDoubleValue());
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderTwo();
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderThree();
                    OutlineUtils.renderFour(n);
                    OutlineUtils.setColor(n);
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderFive();
                }
                OutlineUtils.setColor(Color.WHITE);
            }
            this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
            if (flag1) {
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc((int)516, (float)0.1f);
                GlStateManager.popMatrix();
                GlStateManager.depthMask((boolean)true);
            }
        }
    }
}

