/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package me.xenodevs.xeno.gui.hud.modules.impl;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.module.modules.client.Colors;
import net.minecraft.client.Minecraft;

public class HUDFPS
extends HUDMod {
    public HUDFPS() {
        super("FPS", 0, 20, Xeno.moduleManager.getModule("FPS"));
    }

    @Override
    public void draw() {
        TextUtil.drawStringWithShadow("FPS: " + Minecraft.getDebugFPS(), this.getX() + 1, this.getY(), Colors.colourInt);
        super.draw();
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        super.renderDummy(mouseX, mouseY);
        this.drag.setHeight(this.getHeight());
        this.drag.setWidth(this.getWidth());
        TextUtil.drawStringWithShadow("FPS: " + Minecraft.getDebugFPS(), this.getX() + 1, this.getY(), this.parent.enabled ? Colors.colourInt : -7340032);
    }

    @Override
    public int getWidth() {
        return TextUtil.getStringWidth("FPS: " + Minecraft.getDebugFPS());
    }

    @Override
    public float getHeight() {
        return 11.0f;
    }
}

