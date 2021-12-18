/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.module.modules.player;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.utils.other.Timer;

public class AntiAFK
extends Module {
    int tickCount = 1;
    int afkCount = 1;
    Timer timer = new Timer();

    public AntiAFK() {
        super("AntiAFK", "stops you from being kicked by the server's AFK timer", Category.PLAYER);
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindForward.isKeyDown() = false;
    }

    @Override
    public void onUpdate() {
        if (this.timer.hasTimeElapsed(1L, true)) {
            mc.player.rotationYaw -= 10.0f;
        }
    }
}

