/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.module.modules.movement;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.ModeSetting;

public class Sprint
extends Module {
    public static Sprint INSTANCE;
    ModeSetting mode = new ModeSetting("Mode", "Legit", "Omni");

    public Sprint() {
        super("Sprint", "run all the time lmao", 0, Category.MOVEMENT);
        this.addSetting(this.mode);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        if (this.mode.is("Legit")) {
            if (this.mc.gameSettings.keyBindForward.isKeyDown()) {
                this.mc.player.setSprinting(true);
            } else {
                this.mc.player.setSprinting(false);
            }
        } else if (this.mode.is("Omni")) {
            this.mc.player.setSprinting(true);
        }
    }
}

