/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.xenodevs.xeno.module.modules.combat;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.block.BlockUtil;
import me.xenodevs.xeno.utils.block.hole.Hole;
import me.xenodevs.xeno.utils.block.hole.HoleUtil;
import me.xenodevs.xeno.utils.other.MathUtils;
import me.xenodevs.xeno.utils.other.Timer;
import me.xenodevs.xeno.utils.player.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class HoleFill
extends Module {
    NumberSetting range = new NumberSetting("Range", 5.0, 2.0, 7.0, 1.0);
    NumberSetting placeMS = new NumberSetting("PlaceDelayMS", 20.0, 0.0, 40.0, 1.0);
    BooleanSetting swing = new BooleanSetting("Swing", true);
    BooleanSetting rotate = new BooleanSetting("Rotate", false);
    BooleanSetting rotateBack = new BooleanSetting("RotateBack", false);
    Timer timer = new Timer();

    public HoleFill() {
        super("HoleFill", "fills all the holes :smirk: :smirk: :smirk:", Category.COMBAT);
        this.addSettings(this.range, this.placeMS, this.swing, this.rotate, this.rotateBack);
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        this.doHoleFill();
    }

    public void doHoleFill() {
        for (EntityPlayer entityPlayer : this.mc.world.playerEntities) {
            if (entityPlayer == this.mc.player || !(this.mc.player.getDistanceSq((Entity)entityPlayer) <= MathUtils.square(this.range.getFloatValue()))) continue;
            for (Hole h : HoleUtil.getHoles(5.0, entityPlayer)) {
                if (!this.timer.hasTimeElapsed((long)this.placeMS.getDoubleValue(), true) || HoleUtil.isInHole((EntityPlayer)this.mc.player)) continue;
                BlockUtil.placeBlock(h.hole, PlayerUtil.findObiInHotbar(), this.rotate.isEnabled(), this.rotateBack.isEnabled(), this.swing.isEnabled());
            }
        }
    }
}

