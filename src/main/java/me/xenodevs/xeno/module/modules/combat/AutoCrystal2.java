/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemEndCrystal
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 */
package me.xenodevs.xeno.module.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.combat.CrystalUtil;
import me.xenodevs.xeno.utils.other.MathUtils;
import me.xenodevs.xeno.utils.other.Timer;
import me.xenodevs.xeno.utils.player.InventoryUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AutoCrystal2
extends Module {
    ModeSetting logic = new ModeSetting("Logic", "B->P", "P->B");
    BooleanSetting breakk = new BooleanSetting("Break", true);
    NumberSetting breakRange = new NumberSetting("B Range", 7.0, 2.0, 7.0, 0.5);
    ModeSetting breakMode = new ModeSetting("B Mode", "Packet", "Swing");
    ModeSetting breakHand = new ModeSetting("B Hand", "Both", "Mainhand", "Offhand");
    NumberSetting breakSpeed = new NumberSetting("BreakSpeed", 15.0, 1.0, 40.0, 1.0);
    BooleanSetting antiWeakness = new BooleanSetting("AntiWeak", false);
    ModeSetting awmode = new ModeSetting("AWeak Mode", "Legit", "Packet");
    ModeSetting breakLogic = new ModeSetting("BreakLogic", "All", "Self");
    BooleanSetting place = new BooleanSetting("Place", true);
    NumberSetting placeDelayMS = new NumberSetting("PlaceDelayMS", 10.0, 0.0, 20.0, 1.0);
    NumberSetting placeRange = new NumberSetting("PlaceRange", 7.0, 0.0, 10.0, 1.0);
    NumberSetting targetRange = new NumberSetting("TargetRange", 7.0, 0.0, 10.0, 1.0);
    BooleanSetting specEntity = new BooleanSetting("SpecEntityCheck", false);
    BooleanSetting fifteen = new BooleanSetting("Fifteen", false);
    BooleanSetting ignoreTerrain = new BooleanSetting("IgnoreTerrain", false);
    BooleanSetting seeCheck = new BooleanSetting("See Check", false);
    NumberSetting minDmg = new NumberSetting("Minimum DMG", 3.5, 0.0, 36.0, 0.5);
    NumberSetting maxLocalDMG = new NumberSetting("Max Local DMG", 7.0, 1.0, 20.0, 1.0);
    BooleanSetting placeSwing = new BooleanSetting("Place Swing", false);
    Timer breakTimer = new Timer();
    Timer placeTimer = new Timer();
    ArrayList<BlockPos> placedCrystals = new ArrayList();

    public AutoCrystal2() {
        super("AutoCrystal2", "crystals go brrr", Category.COMBAT);
        this.addSettings(this.logic);
        this.addSettings(this.breakk, this.breakRange, this.breakMode, this.breakHand, this.breakSpeed, this.antiWeakness, this.awmode, this.breakLogic);
        this.addSettings(this.place, this.placeDelayMS, this.placeRange, this.targetRange, this.specEntity, this.fifteen, this.ignoreTerrain, this.seeCheck, this.minDmg, this.maxLocalDMG, this.placeSwing);
    }

    @Override
    public void onEnable() {
        this.placedCrystals.clear();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.placedCrystals.clear();
        super.onDisable();
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        this.doAutoCrystal();
    }

    public void doAutoCrystal() {
        if (this.logic.is("B->P")) {
            if (this.breakk.isEnabled()) {
                this.breakCrystals();
            }
            if (this.place.isEnabled()) {
                this.placeCrystals();
            }
        } else if (this.logic.is("P->B")) {
            if (this.place.isEnabled()) {
                this.placeCrystals();
            }
            if (this.breakk.isEnabled()) {
                this.breakCrystals();
            }
        }
    }

    public void breakCrystals() {
        for (Entity e : this.mc.world.loadedEntityList) {
            List crystals = this.mc.world.loadedEntityList.stream().filter(EntityEnderCrystal.class::isInstance).collect(Collectors.toList());
            crystals.stream().filter(crystal -> crystal instanceof EntityEnderCrystal && (double)this.mc.player.getDistance(crystal) <= this.breakRange.getDoubleValue());
            crystals.sort(Comparator.comparingDouble(crystal -> this.mc.player.getDistanceSq(crystal)));
            int oldSlot = 0;
            for (Entity crystal2 : crystals) {
                if (this.breakLogic.is("Self") && !this.placedCrystals.contains((Object)crystal2.getPosition()) || !(crystal2 instanceof EntityEnderCrystal) || !(this.mc.player.getDistanceSq(crystal2) <= MathUtils.square(this.breakRange.getFloatValue())) || !this.breakTimer.hasTimeElapsed((long)(1000.0 / this.breakSpeed.getDoubleValue()), true)) continue;
                if (this.antiWeakness.isEnabled()) {
                    for (PotionEffect p : this.mc.player.getActivePotionEffects()) {
                        if (!p.getEffectName().equalsIgnoreCase("Weakness")) continue;
                        if (this.awmode.is("Legit")) {
                            InventoryUtil.switchToSlot(InventoryUtil.getBestHotbarSword());
                            continue;
                        }
                        if (!this.awmode.is("Packet")) continue;
                        oldSlot = this.mc.player.inventory.currentItem;
                        InventoryUtil.switchToSlotGhost(InventoryUtil.getBestHotbarSword());
                    }
                }
                if (this.breakMode.is("Packet")) {
                    this.mc.getConnection().sendPacket((Packet)new CPacketUseEntity(crystal2));
                } else if (this.breakMode.is("Swing")) {
                    this.mc.playerController.attackEntity((EntityPlayer)this.mc.player, crystal2);
                }
                CrystalUtil.swingArm(this.breakHand.getMode());
                if (this.antiWeakness.isEnabled() && this.awmode.is("Packet")) {
                    InventoryUtil.switchToSlot(oldSlot);
                }
                this.placedCrystals.remove((Object)crystal2.getPosition());
            }
        }
    }

    public void placeCrystals() {
        EnumHand hand = null;
        if (this.mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal) {
            hand = EnumHand.MAIN_HAND;
        } else if (this.mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal) {
            hand = EnumHand.OFF_HAND;
        }
        if (this.placeTimer.hasTimeElapsed((long)this.placeDelayMS.getDoubleValue(), true) && hand != null && CrystalUtil.getBestPos((Entity)CrystalUtil.getNearestTarget(this.targetRange.getFloatValue()), this.placeRange.getFloatValue(), this.specEntity.isEnabled(), this.fifteen.isEnabled(), this.ignoreTerrain.isEnabled(), this.seeCheck.isEnabled(), this.minDmg.getDoubleValue(), this.maxLocalDMG.getDoubleValue()) != null && CrystalUtil.getNearestTarget(this.targetRange.getFloatValue()) != null) {
            CrystalUtil.placeCrystalOnBlock(CrystalUtil.getBestPos((Entity)CrystalUtil.getNearestTarget(this.targetRange.getFloatValue()), this.placeRange.getFloatValue(), this.specEntity.isEnabled(), this.fifteen.isEnabled(), this.ignoreTerrain.isEnabled(), this.seeCheck.isEnabled(), this.minDmg.getDoubleValue(), this.maxLocalDMG.getDoubleValue()), hand, this.placeSwing.isEnabled());
            this.placedCrystals.add(CrystalUtil.getBestPos((Entity)CrystalUtil.getNearestTarget(this.targetRange.getFloatValue()), this.placeRange.getFloatValue(), this.specEntity.isEnabled(), this.fifteen.isEnabled(), this.ignoreTerrain.isEnabled(), this.seeCheck.isEnabled(), this.minDmg.getDoubleValue(), this.maxLocalDMG.getDoubleValue()));
        }
    }
}

