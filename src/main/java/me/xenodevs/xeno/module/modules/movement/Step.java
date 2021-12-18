/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.module.modules.movement;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.NumberSetting;

public class Step
extends Module {
    public static Step INSTANCE;
    NumberSetting up = new NumberSetting("Up Height", 2.5, 1.0, 3.0, 0.5);

    public Step() {
        super("Step", "auto jump but better", 0, Category.MOVEMENT);
        this.addSettings(this.up);
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        this.mc.player.stepHeight = 0.5f;
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck() || !this.isEnabled()) {
            return;
        }
        this.mc.player.stepHeight = (float)this.up.getVal();
    }
}

