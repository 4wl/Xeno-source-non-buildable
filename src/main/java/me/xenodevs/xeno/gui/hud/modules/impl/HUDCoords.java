/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.TextFormatting
 */
package me.xenodevs.xeno.gui.hud.modules.impl;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.module.modules.client.Colors;
import net.minecraft.util.text.TextFormatting;

public class HUDCoords
extends HUDMod {
    public HUDCoords() {
        super("Coordinates", 0, 30, Xeno.moduleManager.getModule("Coordinates"));
    }

    @Override
    public void draw() {
        TextUtil.drawStringWithShadow("XYZ " + (Object)TextFormatting.WHITE + HUDCoords.mc.player.getPosition().getX() + " " + HUDCoords.mc.player.getPosition().getY() + " " + HUDCoords.mc.player.getPosition().getZ(), this.getX() + 1, this.getY(), Colors.colourInt);
        super.draw();
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        super.renderDummy(mouseX, mouseY);
        this.drag.setHeight(this.getHeight());
        this.drag.setWidth(this.getWidth());
        TextUtil.drawStringWithShadow("XYZ " + (Object)TextFormatting.WHITE + HUDCoords.mc.player.getPosition().getX() + " " + HUDCoords.mc.player.getPosition().getY() + " " + HUDCoords.mc.player.getPosition().getZ(), this.getX() + 1, this.getY(), this.parent.enabled ? Colors.colourInt : -7340032);
    }

    @Override
    public int getWidth() {
        try {
            return TextUtil.getStringWidth("XYZ " + (Object)TextFormatting.WHITE + HUDCoords.mc.player.getPosition().getX() + " " + HUDCoords.mc.player.getPosition().getY() + " " + HUDCoords.mc.player.getPosition().getZ());
        }
        catch (NullPointerException e) {
            return 0;
        }
    }

    @Override
    public float getHeight() {
        return 11.0f;
    }
}

