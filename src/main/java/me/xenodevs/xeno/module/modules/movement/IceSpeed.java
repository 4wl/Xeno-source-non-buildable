/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Blocks
 */
package me.xenodevs.xeno.module.modules.movement;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.NumberSetting;
import net.minecraft.init.Blocks;

public class IceSpeed
extends Module {
    NumberSetting speed = new NumberSetting("Speed", 0.5, 0.3, 1.0, 0.1);

    public IceSpeed() {
        super("IceSpeed", "change the speed of ice", Category.MOVEMENT);
        this.addSettings(this.speed);
    }

    @Override
    public void onDisable() {
        Blocks.ICE.slipperiness = 0.98f;
        Blocks.PACKED_ICE.slipperiness = 0.98f;
        Blocks.FROSTED_ICE.slipperiness = 0.98f;
    }

    @Override
    public void onUpdate() {
        Blocks.ICE.slipperiness = this.speed.getFloatValue();
        Blocks.PACKED_ICE.slipperiness = this.speed.getFloatValue();
        Blocks.FROSTED_ICE.slipperiness = this.speed.getFloatValue();
    }
}

