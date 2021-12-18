/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.module.modules.movement;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;

public class Fly
extends Module {
    public static Fly INSTANCE;

    public Fly() {
        super("Fly", "lets u fly lol", 0, Category.MOVEMENT);
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        if (this.mc.player != null) {
            this.mc.player.capabilities.isFlying = false;
        }
    }

    @Override
    public void onUpdate() {
        if (this.mc.player != null) {
            this.mc.player.capabilities.isFlying = true;
        }
    }
}

