/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.module.modules.movement;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;

public class Jetpack
extends Module {
    public Jetpack() {
        super("Jetpack", "Makes you go up when you jump.", Category.MOVEMENT);
    }

    @Override
    public void onUpdate() {
        if (this.mc.gameSettings.keyBindJump.pressed) {
            this.mc.player.jump();
        }
    }
}

