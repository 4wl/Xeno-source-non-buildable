/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.gui.hud.modules.impl;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.managers.TickManager;
import me.xenodevs.xeno.module.modules.client.Colors;

public class HUDTPS
extends HUDMod {
    public HUDTPS() {
        super("TPS", 0, 50, Xeno.moduleManager.getModule("TPS"));
    }

    @Override
    public void draw() {
        TextUtil.drawStringWithShadow("TPS: " + Xeno.tickManager.getTPS(TickManager.TPS.CURRENT), this.getX() + 1, this.getY(), Colors.colourInt);
        super.draw();
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        super.renderDummy(mouseX, mouseY);
        this.drag.setHeight(this.getHeight());
        this.drag.setWidth(this.getWidth());
        TextUtil.drawStringWithShadow("TPS: " + Xeno.tickManager.getTPS(TickManager.TPS.CURRENT), this.getX() + 1, this.getY(), this.parent.enabled ? Colors.colourInt : -7340032);
    }

    @Override
    public int getWidth() {
        try {
            return TextUtil.getStringWidth("TPS: " + Xeno.tickManager.getTPS(TickManager.TPS.CURRENT));
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

