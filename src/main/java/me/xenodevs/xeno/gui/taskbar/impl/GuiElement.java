/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiScreen
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.gui.taskbar.impl;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.taskbar.TaskbarElement;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

public class GuiElement
extends TaskbarElement {
    public GuiElement() {
        super("ClickGUI", "textures/gui.png", 0);
    }

    @Override
    public void render() {
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        mc.getTextureManager().bindTexture(this.imageLocation);
        Gui.drawModalRectWithCustomSizedTexture((int)(this.x * 2), (int)(this.y * 2), (float)0.0f, (float)0.0f, (int)64, (int)64, (float)64.0f, (float)64.0f);
    }

    @Override
    public void onClick() {
        mc.displayGuiScreen((GuiScreen)Xeno.clickGui);
    }
}

