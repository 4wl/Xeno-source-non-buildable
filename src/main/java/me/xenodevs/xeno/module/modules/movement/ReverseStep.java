/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package me.xenodevs.xeno.module.modules.movement;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.NumberSetting;
import net.minecraft.entity.Entity;

public class ReverseStep
extends Module {
    public static ReverseStep INSTANCE;
    NumberSetting down = new NumberSetting("Down Height", 2.0, 1.0, 3.0, 0.5);

    public ReverseStep() {
        super("ReverseStep", "step but down", 0, Category.MOVEMENT);
        this.addSettings(this.down);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        if (this.mc.world == null || this.mc.player == null || this.mc.player.isInWater() || this.mc.player.isInLava() || this.mc.player.isOnLadder() || this.mc.gameSettings.keyBindJump.isKeyDown()) {
            return;
        }
        if (this.mc.player != null && this.mc.player.onGround && !this.mc.player.isInWater() && !this.mc.player.isOnLadder()) {
            for (double y = 0.0; y < this.down.getVal(); y += 0.01) {
                if (this.mc.world.getCollisionBoxes((Entity)this.mc.player, this.mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) continue;
                this.mc.player.motionY = -10.0;
                break;
            }
        }
    }
}

