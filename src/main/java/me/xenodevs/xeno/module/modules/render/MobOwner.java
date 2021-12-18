/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.passive.EntityTameable
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.module.modules.render;

import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.utils.render.Colour;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import org.lwjgl.opengl.GL11;

public class MobOwner
extends Module {
    BooleanSetting customFont = new BooleanSetting("Custom Font", true);
    BooleanSetting wolf = new BooleanSetting("Wolf", true);
    ColourPicker fontColour = new ColourPicker("Font Colour", Colour.WHITE);

    public MobOwner() {
        super("MobOwner", "shows who tamed mobs belong to", Category.RENDER);
        this.addSettings(this.customFont, this.fontColour);
    }

    @Override
    public void onRenderWorld() {
        for (Entity e : this.mc.world.loadedEntityList) {
            if (!(e instanceof EntityTameable)) continue;
            try {
                this.renderNametag(e, (Entity)((EntityTameable)e).getOwner(), e.posX, e.posY + (double)e.height + 1.0, e.posZ);
            }
            catch (NullPointerException nullPointerException) {}
        }
    }

    public void renderNametag(Entity entity, Entity owner, double x, double y, double z) {
        String text = entity.getName();
        String text2 = owner.getName() + (owner.getName() == this.mc.player.getName() ? " (You)" : "");
        double dist = this.mc.player.getDistance(x, y, z);
        double scale = 3.0;
        double offset = 0.0;
        scale = dist / 20.0 * Math.pow(1.2589254, 0.1 / (dist < 25.0 ? 0.5 : 2.0));
        scale = Math.min(Math.max(scale, 0.5), 5.0);
        offset = -0.5;
        GlStateManager.pushMatrix();
        GlStateManager.translate((double)(x - this.mc.getRenderManager().viewerPosX), (double)(y + offset - this.mc.getRenderManager().viewerPosY), (double)(z - this.mc.getRenderManager().viewerPosZ));
        GlStateManager.rotate((float)(-this.mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)this.mc.getRenderManager().playerViewX, (float)(this.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.scale((double)(-(scale /= 40.0)), (double)(-scale), (double)scale);
        int width = this.customFont.getValue() ? FontManager.comfortaa.getStringWidth(this.name) / 2 + 1 : this.mc.fontRenderer.getStringWidth(this.name) / 2 + 1;
        GlStateManager.enableTexture2D();
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        if (this.customFont.isEnabled()) {
            FontManager.comfortaa.drawStringWithShadow(text, -width, (FontManager.comfortaa.FONT_HEIGHT - 20 - FontManager.comfortaa.FONT_HEIGHT) / 2, this.fontColour.getColor().getRGB());
            FontManager.comfortaa.drawStringWithShadow(text2, -width, (FontManager.comfortaa.FONT_HEIGHT - 5) / 2, this.fontColour.getColor().getRGB());
        } else {
            this.mc.fontRenderer.drawStringWithShadow(text, (float)(-width), (float)((this.mc.fontRenderer.FONT_HEIGHT - 20 - this.mc.fontRenderer.FONT_HEIGHT) / 2), this.fontColour.getColor().getRGB());
            this.mc.fontRenderer.drawStringWithShadow(text2, (float)(-width), (float)((this.mc.fontRenderer.FONT_HEIGHT - 5) / 2), this.fontColour.getColor().getRGB());
        }
        GlStateManager.disableTexture2D();
        GlStateManager.popMatrix();
    }
}

