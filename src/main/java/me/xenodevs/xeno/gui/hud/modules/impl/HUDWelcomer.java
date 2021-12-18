/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.gui.hud.modules.impl;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.module.modules.client.Colors;

public class HUDWelcomer
extends HUDMod {
    public HUDWelcomer() {
        super("Welcomer", 100, 0, Xeno.moduleManager.getModule("Welcomer"));
    }

    @Override
    public void draw() {
        TextUtil.drawStringWithShadow("Welcome to Xeno, " + mc.getSession().getUsername() + "!", this.getX(), this.getY(), Colors.colourInt);
        super.draw();
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        super.renderDummy(mouseX, mouseY);
        this.drag.setHeight(this.getHeight());
        this.drag.setWidth(this.getWidth());
        TextUtil.drawStringWithShadow("Welcome to Xeno, " + mc.getSession().getUsername() + "!", this.getX(), this.getY(), this.parent.enabled ? Colors.colourInt : -7340032);
    }

    @Override
    public int getWidth() {
        return TextUtil.getStringWidth("Welcome to Xeno, " + mc.getSession().getUsername() + "!");
    }

    @Override
    public float getHeight() {
        return 11.0f;
    }
}

