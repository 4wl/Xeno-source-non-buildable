/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.gui.hud.modules.impl;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.module.modules.hud.ClientName;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class HUDClientName
extends HUDMod {
    public HUDClientName() {
        super("Client Name", 0, 0, Xeno.moduleManager.getModule("ClientName"));
    }

    @Override
    public void draw() {
        if (ClientName.mode.is("Text")) {
            TextUtil.drawStringWithShadow(Xeno.NAME + " " + "1.1", this.getX(), this.getY(), Colors.colourInt);
        } else if (ClientName.mode.is("Image")) {
            GL11.glPushMatrix();
            GL11.glColor3f((float)1.0f, (float)1.0f, (float)1.0f);
            if (!this.parent.enabled) {
                GL11.glColor3f((float)0.9f, (float)0.0f, (float)0.0f);
            }
            GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
            ResourceLocation down = new ResourceLocation("xeno", "textures/xeno_logo_full.png");
            Minecraft.getMinecraft().getTextureManager().bindTexture(down);
            Gui.drawModalRectWithCustomSizedTexture((int)(this.getX() * 2), (int)(this.getY() * 2), (float)0.0f, (float)0.0f, (int)190, (int)70, (float)190.0f, (float)70.0f);
            GL11.glPopMatrix();
        }
        super.draw();
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        super.renderDummy(mouseX, mouseY);
        this.drag.setHeight(this.getHeight());
        this.drag.setWidth(this.getWidth());
        if (ClientName.mode.is("Text")) {
            TextUtil.drawStringWithShadow(Xeno.NAME + " " + "1.1", this.getX(), this.getY(), this.parent.enabled ? Colors.colourInt : -7340032);
        } else if (ClientName.mode.is("Image")) {
            GL11.glPushMatrix();
            if (!this.parent.enabled) {
                GL11.glColor3f((float)0.9f, (float)0.0f, (float)0.0f);
            }
            GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
            ResourceLocation down = new ResourceLocation("xeno", "textures/xeno_logo_full.png");
            Minecraft.getMinecraft().getTextureManager().bindTexture(down);
            Gui.drawModalRectWithCustomSizedTexture((int)(this.getX() * 2), (int)(this.getY() * 2), (float)0.0f, (float)0.0f, (int)190, (int)70, (float)190.0f, (float)70.0f);
            GL11.glPopMatrix();
        }
    }

    @Override
    public int getWidth() {
        return ClientName.mode.is("Text") ? TextUtil.getStringWidth(Xeno.NAME + " " + "1.1") : 95;
    }

    @Override
    public float getHeight() {
        return ClientName.mode.is("Text") ? 11 : 35;
    }
}

