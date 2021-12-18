/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.gui.hud.modules.impl;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.module.modules.client.Colors;

public class HUDPing
extends HUDMod {
    public HUDPing() {
        super("Ping", 0, 40, Xeno.moduleManager.getModule("Ping"));
    }

    @Override
    public void draw() {
        TextUtil.drawStringWithShadow("Ping: " + HUDPing.getPing(), this.getX() + 1, this.getY(), Colors.colourInt);
        super.draw();
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        super.renderDummy(mouseX, mouseY);
        this.drag.setHeight(this.getHeight());
        this.drag.setWidth(this.getWidth());
        TextUtil.drawStringWithShadow("Ping: " + HUDPing.getPing(), this.getX() + 1, this.getY(), this.parent.enabled ? Colors.colourInt : -7340032);
    }

    private static int getPing() {
        int p = -1;
        p = HUDPing.mc.player == null || mc.getConnection() == null || mc.getConnection().getPlayerInfo(HUDPing.mc.player.getName()) == null ? -1 : mc.getConnection().getPlayerInfo(HUDPing.mc.player.getName()).getResponseTime();
        return p;
    }

    @Override
    public int getWidth() {
        return TextUtil.getStringWidth("Ping: " + HUDPing.getPing());
    }

    @Override
    public float getHeight() {
        return 11.0f;
    }
}

