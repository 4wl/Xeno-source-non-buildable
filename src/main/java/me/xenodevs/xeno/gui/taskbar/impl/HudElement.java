/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.gui.taskbar.impl;

import me.xenodevs.xeno.gui.taskbar.TaskbarElement;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class HudElement
extends TaskbarElement {
    public HudElement() {
        super("HUD", "textures/hud.png", 64);
    }

    @Override
    public void render() {
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        mc.getTextureManager().bindTexture(this.imageLocation);
        Gui.drawModalRectWithCustomSizedTexture((int)(this.x * 2), (int)(this.y * 2), (float)0.0f, (float)0.0f, (int)64, (int)64, (float)64.0f, (float)64.0f);
    }

    @Override
    public void onClick() {
    }
}

