/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.ItemEndCrystal
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.CombatRules
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package me.xenodevs.xeno.module.modules.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.module.modules.misc.AutoEZ;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.other.MathUtils;
import me.xenodevs.xeno.utils.other.Timer;
import me.xenodevs.xeno.utils.player.InventoryUtil;
import me.xenodevs.xeno.utils.player.PlayerUtil;
import me.xenodevs.xeno.utils.render.RenderUtils3D;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class AutoCrystal
extends Module {
    ModeSetting logic = new ModeSetting("Logic", "B -> P", "P -> B");
    BooleanSetting breakk = new BooleanSetting("Break", true);
    NumberSetting breakRange = new NumberSetting("B Range", 7.0, 2.0, 7.0, 0.5);
    ModeSetting breakMode = new ModeSetting("B Mode", "Packet", "Swing");
    ModeSetting breakHand = new ModeSetting("B Hand", "Both", "Mainhand", "Offhand");
    NumberSetting breakSpeed = new NumberSetting("BreakSpeed", 15.0, 1.0, 40.0, 1.0);
    BooleanSetting antiWeakness = new BooleanSetting("AntiWeak", false);
    ModeSetting awmode = new ModeSetting("AWeak Mode", "Legit", "Packet");
    BooleanSetting place = new BooleanSetting("Place", true);
    BooleanSetting placeSwing = new BooleanSetting("Place Swing", false);
    NumberSetting placeRange = new NumberSetting("Place Range", 7.0, 1.0, 10.0, 1.0);
    NumberSetting placeSpeed = new NumberSetting("PlaceSpeed", 15.0, 1.0, 40.0, 1.0);
    NumberSetting targetRange = new NumberSetting("Target Range", 15.0, 0.0, 20.0, 1.0);
    NumberSetting minDmg = new NumberSetting("Min Damage", 0.0, 0.0, 36.0, 0.5);
    BooleanSetting multi = new BooleanSetting("Multiplace", false);
    BooleanSetting ignoreTerrain = new BooleanSetting("IgnoreTerrain", false);
    BooleanSetting thirteen = new BooleanSetting("Thirteen", false);
    BooleanSetting crystalCheck = new BooleanSetting("CrystalCheck", false);
    NumberSetting maxLocal = new NumberSetting("MaxLocalDMG", 6.0, 1.0, 20.0, 1.0);
    BooleanSetting render = new BooleanSetting("Render", true);
    BooleanSetting customFont = new BooleanSetting("Custom Font", true);
    BooleanSetting damageValue = new BooleanSetting("Damage Value", true);
    ModeSetting renderMode = new ModeSetting("R Mode", "Outline", "Off");
    ColourPicker renderColour = new ColourPicker("Render Colour", Colors.col);
    double placeDamage = 0.0;
    BlockPos renderBlockPos = null;
    Timer breakTimer = new Timer();
    Timer placeTimer = new Timer();
    boolean multiNotify = false;
    EntityPlayer target = null;

    public AutoCrystal() {
        super("AutoCrystal", "crystals go brrrrrrrr", Category.COMBAT);
        this.addSettings(this.logic);
        this.addSettings(this.breakk, this.breakRange, this.breakMode, this.breakHand, this.breakSpeed, this.antiWeakness, this.awmode);
        this.addSettings(this.place, this.placeSwing, this.placeRange, this.placeSpeed, this.targetRange, this.minDmg, this.multi, this.ignoreTerrain, this.thirteen, this.crystalCheck, this.maxLocal);
        this.addSettings(this.render, this.customFont, this.damageValue, this.renderMode, this.renderColour);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        this.logic();
        if (this.multi.isEnabled() && !this.multiNotify) {
            this.multiNotify = true;
        }
        if (!this.multi.isEnabled() && this.multiNotify) {
            this.multiNotify = false;
        }
    }

    @Override
    public void onRenderWorld() {
        if (this.render.isEnabled() && this.renderBlockPos != null && this.target != null && this.target != this.mc.player) {
            if (this.renderMode.is("Outline")) {
                RenderUtils3D.blockESPBox(this.renderBlockPos, 1.0f, this.renderColour.getColor(), 1);
            }
            if (this.damageValue.isEnabled()) {
                RenderUtils3D.drawNametagFromBlockPos(this.renderBlockPos, 0.75f, this.customFont.isEnabled(), String.valueOf(Math.round(this.placeDamage)));
            }
        }
    }

    public void logic() {
        if (this.logic.is("B -> P")) {
            if (this.breakk.isEnabled()) {
                this.breakCrystals();
            }
            if (this.place.isEnabled()) {
                this.placeCrystals();
            }
        } else if (this.logic.is("P -> B")) {
            if (this.place.isEnabled()) {
                this.placeCrystals();
            }
            if (this.breakk.isEnabled()) {
                this.breakCrystals();
            }
        }
    }

    public void breakCrystals() {
        List crystals = this.mc.world.loadedEntityList.stream().filter(EntityEnderCrystal.class::isInstance).collect(Collectors.toList());
        crystals.stream().filter(crystal -> crystal instanceof EntityEnderCrystal && (double)this.mc.player.getDistance(crystal) <= this.breakRange.getDoubleValue());
        crystals.sort(Comparator.comparingDouble(crystal -> this.mc.player.getDistanceSq(crystal)));
        int oldSlot = 0;
        for (Entity crystal2 : crystals) {
            if (!(crystal2 instanceof EntityEnderCrystal) || !(this.mc.player.getDistanceSq(crystal2) <= MathUtils.square(this.breakRange.getFloatValue())) || !this.breakTimer.hasTimeElapsed((long)(1000.0 / this.breakSpeed.getDoubleValue()), true)) continue;
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
            this.swingArm(this.breakHand.getMode());
            if (!this.antiWeakness.isEnabled() || !this.awmode.is("Packet")) continue;
            InventoryUtil.switchToSlot(oldSlot);
        }
    }

    public void placeCrystals() {
        if (!(this.mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal) && !(this.mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal)) {
            return;
        }
        double bestdmg = 0.0;
        BlockPos finalPlace = null;
        EntityOtherPlayerMP target = null;
        double closestPos = this.placeRange.getMaximum();
        if (!this.multi.isEnabled()) {
            for (Entity e : this.mc.world.loadedEntityList.stream().filter(EntityOtherPlayerMP.class::isInstance).collect(Collectors.toList())) {
                if (!((double)this.mc.player.getDistance(e) < closestPos)) continue;
                target = (EntityOtherPlayerMP)e;
                closestPos = this.mc.player.getDistance(e);
                AutoEZ.addTarget(target.getName());
            }
            for (EntityPlayer ep : this.mc.world.playerEntities) {
                if (ep == this.mc.player || !((double)this.mc.player.getDistance((Entity)ep) <= this.targetRange.getDoubleValue())) continue;
                target = ep;
            }
            if (target != null) {
                if (!this.placeTimer.hasTimeElapsed((long)(1000.0 / this.placeSpeed.getDoubleValue()), true)) {
                    return;
                }
                for (BlockPos bp : this.possiblePlacePositions(this.placeRange.getFloatValue(), !this.multi.isEnabled(), this.thirteen.isEnabled())) {
                    double newdmg = this.calculateDamage(bp, (Entity)target, this.ignoreTerrain.isEnabled());
                    if (!(newdmg > bestdmg) || !((double)this.calculateDamage(bp, (Entity)this.mc.player, this.ignoreTerrain.isEnabled()) <= this.maxLocal.getDoubleValue())) continue;
                    bestdmg = newdmg;
                    finalPlace = bp;
                }
                if (finalPlace != null && bestdmg >= this.minDmg.getDoubleValue() && (double)this.calculateDamage(finalPlace, (Entity)this.mc.player, this.ignoreTerrain.isEnabled()) <= this.maxLocal.getDoubleValue()) {
                    this.placeCrystalOnBlock(finalPlace, EnumHand.MAIN_HAND, this.placeSwing.isEnabled());
                    this.placeDamage = bestdmg;
                    this.renderBlockPos = finalPlace;
                }
            } else {
                this.target = null;
                this.placeDamage = 0.0;
                this.renderBlockPos = null;
                return;
            }
            this.target = target;
        } else {
            Object hand;
            for (Entity e : this.mc.world.loadedEntityList.stream().filter(EntityOtherPlayerMP.class::isInstance).collect(Collectors.toList())) {
                if (!((double)this.mc.player.getDistance(e) < closestPos)) continue;
                target = (EntityOtherPlayerMP)e;
                closestPos = this.mc.player.getDistance(e);
                AutoEZ.addTarget(target.getName());
            }
            for (EntityPlayer ep : this.mc.world.playerEntities) {
                if (ep == this.mc.player || !((double)this.mc.player.getDistance((Entity)ep) <= this.targetRange.getDoubleValue())) continue;
                target = ep;
            }
            if (target != null) {
                if (!this.multi.isEnabled() && !this.placeTimer.hasTimeElapsed((long)(1000.0 / this.placeSpeed.getDoubleValue()), true)) {
                    return;
                }
                for (BlockPos bp : this.possiblePlacePositions(this.placeRange.getFloatValue(), !this.multi.isEnabled(), this.thirteen.isEnabled())) {
                    double newdmg = this.calculateDamage(bp, (Entity)target, this.ignoreTerrain.isEnabled());
                    if (!(newdmg > bestdmg) || !((double)this.calculateDamage(bp, (Entity)this.mc.player, this.ignoreTerrain.isEnabled()) <= this.maxLocal.getDoubleValue())) continue;
                    bestdmg = newdmg;
                    finalPlace = bp;
                }
                if (finalPlace != null && bestdmg >= this.minDmg.getDoubleValue() && (double)this.calculateDamage(finalPlace, (Entity)this.mc.player, this.ignoreTerrain.isEnabled()) <= this.maxLocal.getDoubleValue()) {
                    this.placeCrystalOnBlock(finalPlace, EnumHand.MAIN_HAND, this.placeSwing.isEnabled());
                    this.placeDamage = bestdmg;
                    this.renderBlockPos = finalPlace;
                }
            } else {
                this.target = null;
                this.placeDamage = 0.0;
                this.renderBlockPos = null;
                return;
            }
            this.target = target;
            for (EntityPlayer entityPlayer : new ArrayList(this.mc.world.playerEntities)) {
                if (this.mc.player.getDistanceSq((Entity)entityPlayer) > MathUtils.square(this.targetRange.getFloatValue())) continue;
                for (BlockPos blockPos : this.possiblePlacePositions(this.placeRange.getFloatValue(), !this.crystalCheck.isEnabled(), this.thirteen.getValue())) {
                    double targetDamage = this.calculateDamage(blockPos, (Entity)entityPlayer, this.ignoreTerrain.isEnabled());
                    if (targetDamage == 0.0 || !(targetDamage > bestdmg) || !((double)this.calculateDamage(blockPos, (Entity)this.mc.player, this.ignoreTerrain.isEnabled()) <= this.maxLocal.getDoubleValue())) continue;
                    bestdmg = targetDamage;
                    finalPlace = blockPos;
                    target = entityPlayer;
                    this.placeDamage = bestdmg;
                    this.renderBlockPos = finalPlace;
                }
            }
            if (target == null || finalPlace == null) {
                this.placeDamage = 0.0;
                this.renderBlockPos = null;
                return;
            }
            if (target != null && finalPlace != null && bestdmg >= this.minDmg.getDoubleValue() && (hand = this.mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal ? EnumHand.MAIN_HAND : (this.mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal ? EnumHand.OFF_HAND : null)) != null) {
                this.placeCrystalOnBlock(finalPlace, (EnumHand)hand, this.placeSwing.isEnabled());
                this.placeDamage = bestdmg;
                this.renderBlockPos = finalPlace;
            }
        }
    }

    public void swingArm(String mode) {
        if (mode.equalsIgnoreCase("Both") && this.mc.player.getHeldItemOffhand() != null) {
            this.mc.player.swingArm(EnumHand.MAIN_HAND);
            this.mc.player.swingArm(EnumHand.OFF_HAND);
        } else if (mode.equalsIgnoreCase("Offhand") && this.mc.player.getHeldItemOffhand() != null) {
            this.mc.player.swingArm(EnumHand.OFF_HAND);
        } else if (mode.equalsIgnoreCase("Mainhand")) {
            this.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    public boolean rayTraceSolidCheck(Vec3d start, Vec3d end, boolean shouldIgnore) {
        if (!(Double.isNaN(start.x) || Double.isNaN(start.y) || Double.isNaN(start.z) || Double.isNaN(end.x) || Double.isNaN(end.y) || Double.isNaN(end.z))) {
            int currX = MathHelper.floor((double)start.x);
            int currY = MathHelper.floor((double)start.y);
            int currZ = MathHelper.floor((double)start.z);
            int endX = MathHelper.floor((double)end.x);
            int endY = MathHelper.floor((double)end.y);
            int endZ = MathHelper.floor((double)end.z);
            BlockPos blockPos = new BlockPos(currX, currY, currZ);
            IBlockState blockState = this.mc.world.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (blockState.getCollisionBoundingBox((IBlockAccess)this.mc.world, blockPos) != Block.NULL_AABB && block.canCollideCheck(blockState, false) && (this.getBlocks().contains((Object)block) || !shouldIgnore)) {
                return true;
            }
            double seDeltaX = end.x - start.x;
            double seDeltaY = end.y - start.y;
            double seDeltaZ = end.z - start.z;
            int steps = 200;
            while (steps-- >= 0) {
                EnumFacing facing;
                if (Double.isNaN(start.x) || Double.isNaN(start.y) || Double.isNaN(start.z)) {
                    return false;
                }
                if (currX == endX && currY == endY && currZ == endZ) {
                    return false;
                }
                boolean unboundedX = true;
                boolean unboundedY = true;
                boolean unboundedZ = true;
                double stepX = 999.0;
                double stepY = 999.0;
                double stepZ = 999.0;
                double deltaX = 999.0;
                double deltaY = 999.0;
                double deltaZ = 999.0;
                if (endX > currX) {
                    stepX = (double)currX + 1.0;
                } else if (endX < currX) {
                    stepX = currX;
                } else {
                    unboundedX = false;
                }
                if (endY > currY) {
                    stepY = (double)currY + 1.0;
                } else if (endY < currY) {
                    stepY = currY;
                } else {
                    unboundedY = false;
                }
                if (endZ > currZ) {
                    stepZ = (double)currZ + 1.0;
                } else if (endZ < currZ) {
                    stepZ = currZ;
                } else {
                    unboundedZ = false;
                }
                if (unboundedX) {
                    deltaX = (stepX - start.x) / seDeltaX;
                }
                if (unboundedY) {
                    deltaY = (stepY - start.y) / seDeltaY;
                }
                if (unboundedZ) {
                    deltaZ = (stepZ - start.z) / seDeltaZ;
                }
                if (deltaX == 0.0) {
                    deltaX = -1.0E-4;
                }
                if (deltaY == 0.0) {
                    deltaY = -1.0E-4;
                }
                if (deltaZ == 0.0) {
                    deltaZ = -1.0E-4;
                }
                if (deltaX < deltaY && deltaX < deltaZ) {
                    facing = endX > currX ? EnumFacing.WEST : EnumFacing.EAST;
                    start = new Vec3d(stepX, start.y + seDeltaY * deltaX, start.z + seDeltaZ * deltaX);
                } else if (deltaY < deltaZ) {
                    facing = endY > currY ? EnumFacing.DOWN : EnumFacing.UP;
                    start = new Vec3d(start.x + seDeltaX * deltaY, stepY, start.z + seDeltaZ * deltaY);
                } else {
                    facing = endZ > currZ ? EnumFacing.NORTH : EnumFacing.SOUTH;
                    start = new Vec3d(start.x + seDeltaX * deltaZ, start.y + seDeltaY * deltaZ, stepZ);
                }
                if (!(block = (blockState = this.mc.world.getBlockState(blockPos = new BlockPos(currX = MathHelper.floor((double)start.x) - (facing == EnumFacing.EAST ? 1 : 0), currY = MathHelper.floor((double)start.y) - (facing == EnumFacing.UP ? 1 : 0), currZ = MathHelper.floor((double)start.z) - (facing == EnumFacing.SOUTH ? 1 : 0)))).getBlock()).canCollideCheck(blockState, false) || !this.getBlocks().contains((Object)block) && shouldIgnore) continue;
                return true;
            }
        }
        return false;
    }

    public float getDamageFromDifficulty(float damage) {
        switch (this.mc.world.getDifficulty()) {
            case PEACEFUL: {
                return 0.0f;
            }
            case EASY: {
                return Math.min(damage / 2.0f + 1.0f, damage);
            }
            case HARD: {
                return damage * 3.0f / 2.0f;
            }
        }
        return damage;
    }

    public float calculateDamage(BlockPos pos, Entity target, boolean shouldIgnore) {
        return this.getExplosionDamage(target, new Vec3d((double)pos.getX() + 0.5, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5), 6.0f, shouldIgnore);
    }

    public float getExplosionDamage(Entity targetEntity, Vec3d explosionPosition, float explosionPower, boolean shouldIgnore) {
        Vec3d entityPosition = new Vec3d(targetEntity.posX, targetEntity.posY, targetEntity.posZ);
        if (targetEntity.isImmuneToExplosions()) {
            return 0.0f;
        }
        double distanceToSize = entityPosition.distanceTo(explosionPosition) / (double)(explosionPower *= 2.0f);
        double blockDensity = 0.0;
        AxisAlignedBB bbox = targetEntity.getEntityBoundingBox().offset(targetEntity.getPositionVector().subtract(entityPosition));
        Vec3d bboxDelta = new Vec3d(1.0 / ((bbox.maxX - bbox.minX) * 2.0 + 1.0), 1.0 / ((bbox.maxY - bbox.minY) * 2.0 + 1.0), 1.0 / ((bbox.maxZ - bbox.minZ) * 2.0 + 1.0));
        double xOff = (1.0 - Math.floor(1.0 / bboxDelta.x) * bboxDelta.x) / 2.0;
        double zOff = (1.0 - Math.floor(1.0 / bboxDelta.z) * bboxDelta.z) / 2.0;
        if (bboxDelta.x >= 0.0 && bboxDelta.y >= 0.0 && bboxDelta.z >= 0.0) {
            int nonSolid = 0;
            int total = 0;
            for (double x = 0.0; x <= 1.0; x += bboxDelta.x) {
                for (double y = 0.0; y <= 1.0; y += bboxDelta.y) {
                    for (double z = 0.0; z <= 1.0; z += bboxDelta.z) {
                        Vec3d startPos = new Vec3d(xOff + bbox.minX + (bbox.maxX - bbox.minX) * x, bbox.minY + (bbox.maxY - bbox.minY) * y, zOff + bbox.minZ + (bbox.maxZ - bbox.minZ) * z);
                        if (!this.rayTraceSolidCheck(startPos, explosionPosition, shouldIgnore)) {
                            ++nonSolid;
                        }
                        ++total;
                    }
                }
            }
            blockDensity = (double)nonSolid / (double)total;
        }
        double densityAdjust = (1.0 - distanceToSize) * blockDensity;
        float damage = (int)((densityAdjust * densityAdjust + densityAdjust) / 2.0 * 7.0 * (double)explosionPower + 1.0);
        if (targetEntity instanceof EntityLivingBase) {
            damage = this.getBlastReduction((EntityLivingBase)targetEntity, this.getDamageFromDifficulty(damage), new Explosion((World)this.mc.world, null, explosionPosition.x, explosionPosition.y, explosionPosition.z, explosionPower / 2.0f, false, true));
        }
        return damage;
    }

    public List<Block> getBlocks() {
        return Arrays.asList(new Block[]{Blocks.OBSIDIAN, Blocks.BEDROCK, Blocks.COMMAND_BLOCK, Blocks.BARRIER, Blocks.ENCHANTING_TABLE, Blocks.ENDER_CHEST, Blocks.END_PORTAL_FRAME, Blocks.BEACON, Blocks.ANVIL});
    }

    public float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        damage = CombatRules.getDamageAfterAbsorb((float)damage, (float)entity.getTotalArmorValue(), (float)((float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()));
        float enchantmentModifierDamage = 0.0f;
        try {
            enchantmentModifierDamage = EnchantmentHelper.getEnchantmentModifierDamage((Iterable)entity.getArmorInventoryList(), (DamageSource)DamageSource.causeExplosionDamage((Explosion)explosion));
        }
        catch (Exception exception) {
            // empty catch block
        }
        enchantmentModifierDamage = MathHelper.clamp((float)enchantmentModifierDamage, (float)0.0f, (float)20.0f);
        damage *= 1.0f - enchantmentModifierDamage / 25.0f;
        PotionEffect resistanceEffect = entity.getActivePotionEffect(MobEffects.RESISTANCE);
        if (entity.isPotionActive(MobEffects.RESISTANCE) && resistanceEffect != null) {
            damage = damage * (25.0f - (float)(resistanceEffect.getAmplifier() + 1) * 5.0f) / 25.0f;
        }
        damage = Math.max(damage, 0.0f);
        return damage;
    }

    public List<BlockPos> possiblePlacePositions(float placeRange, boolean specialEntityCheck, boolean oneDot15) {
        NonNullList positions = NonNullList.create();
        positions.addAll((Collection)this.getSphere(PlayerUtil.getPlayerPos(), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> this.mc.world.getBlockState(pos).getBlock() != Blocks.AIR).filter(pos -> this.canPlaceCrystal((BlockPos)pos, specialEntityCheck, oneDot15)).collect(Collectors.toList()));
        return positions;
    }

    public List<BlockPos> getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        int cx = pos.getX();
        int cy = pos.getY();
        int cz = pos.getZ();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                int y = sphere ? cy - (int)r : cy;
                while (true) {
                    float f2;
                    float f = y;
                    float f3 = f2 = sphere ? (float)cy + r : (float)(cy + h);
                    if (!(f < f2)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < (double)(r * r)) || hollow && dist < (double)((r - 1.0f) * (r - 1.0f)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    public boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck, boolean onepointThirteen) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        try {
            if (!onepointThirteen) {
                if (this.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && this.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                    return false;
                }
                if (this.mc.world.getBlockState(boost).getBlock() != Blocks.AIR || this.mc.world.getBlockState(boost2).getBlock() != Blocks.AIR) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return this.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && this.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
                }
                for (Entity entity : this.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
                for (Entity entity : this.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
            } else {
                if (this.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && this.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                    return false;
                }
                if (this.mc.world.getBlockState(boost).getBlock() != Blocks.AIR) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return this.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty();
                }
                for (Entity entity : this.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void placeCrystalOnBlock(BlockPos pos, EnumHand hand, boolean swing) {
        if (!(this.mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal) && !(this.mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal)) {
            return;
        }
        RayTraceResult result = this.mc.world.rayTraceBlocks(new Vec3d(this.mc.player.posX, this.mc.player.posY + (double)this.mc.player.getEyeHeight(), this.mc.player.posZ), new Vec3d((double)pos.getX() + 0.5, (double)pos.getY() - 0.5, (double)pos.getZ() + 0.5));
        EnumFacing facing = result == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
        this.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0f, 0.0f, 0.0f));
        if (swing) {
            this.mc.player.connection.sendPacket((Packet)new CPacketAnimation(hand));
        }
    }
}

