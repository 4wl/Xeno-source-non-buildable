/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.math.BlockPos
 */
package me.xenodevs.xeno.utils.combat;

import java.util.ArrayList;
import java.util.List;
import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.utils.block.BlockUtil;
import me.xenodevs.xeno.utils.entity.EntityUtil;
import me.xenodevs.xeno.utils.player.InventoryUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class EnemyUtil
implements Globals {
    public static float getHealth(EntityPlayer entityPlayer) {
        return entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount();
    }

    public static float getArmor(EntityPlayer target) {
        float armorDurability = 0.0f;
        for (ItemStack stack : target.getArmorInventoryList()) {
            if (stack == null || stack.getItem() == Items.AIR) continue;
            armorDurability += (float)(stack.getMaxDamage() - stack.getItemDamage()) / (float)stack.getMaxDamage() * 100.0f;
        }
        return armorDurability;
    }

    public static boolean getArmor(EntityPlayer target, boolean melt, double durability) {
        for (ItemStack stack : target.getArmorInventoryList()) {
            if (stack == null || stack.getItem() == Items.AIR) {
                return true;
            }
            if (!melt || !(durability >= (double)((float)(stack.getMaxDamage() - stack.getItemDamage()) / (float)stack.getMaxDamage() * 100.0f))) continue;
            return true;
        }
        return false;
    }

    public static boolean getGearPlay(EntityPlayer target, double stacks) {
        return InventoryUtil.getItemCount(target, (Item)Items.DIAMOND_HELMET) < 1 && (double)(InventoryUtil.getItemCount(target, Items.EXPERIENCE_BOTTLE) / 64) < stacks;
    }

    public static List<BlockPos> getCityBlocks(EntityPlayer player, boolean crystal) {
        BlockPos playerPos = new BlockPos(player.posX, player.posY, player.posZ);
        ArrayList<BlockPos> cityBlocks = new ArrayList<BlockPos>();
        if (BlockUtil.getBlockResistance(playerPos.north()) == BlockUtil.BlockResistance.Resistant) {
            if (crystal) {
                cityBlocks.add(playerPos.north());
            } else if (BlockUtil.getBlockResistance(playerPos.north().north()) == BlockUtil.BlockResistance.Blank) {
                cityBlocks.add(playerPos.north());
            }
        }
        if (BlockUtil.getBlockResistance(playerPos.east()) == BlockUtil.BlockResistance.Resistant) {
            if (crystal) {
                cityBlocks.add(playerPos.east());
            } else if (BlockUtil.getBlockResistance(playerPos.east().east()) == BlockUtil.BlockResistance.Blank) {
                cityBlocks.add(playerPos.east());
            }
        }
        if (BlockUtil.getBlockResistance(playerPos.south()) == BlockUtil.BlockResistance.Resistant) {
            if (crystal) {
                cityBlocks.add(playerPos.south());
            } else if (BlockUtil.getBlockResistance(playerPos.south().south()) == BlockUtil.BlockResistance.Blank) {
                cityBlocks.add(playerPos.south());
            }
        }
        if (BlockUtil.getBlockResistance(playerPos.west()) == BlockUtil.BlockResistance.Resistant) {
            if (crystal) {
                cityBlocks.add(playerPos.west());
            } else if (BlockUtil.getBlockResistance(playerPos.west().west()) == BlockUtil.BlockResistance.Blank) {
                cityBlocks.add(playerPos.west());
            }
        }
        return cityBlocks;
    }

    public static boolean attackCheck(Entity entity, boolean players, boolean animals, boolean mobs) {
        if (players && entity instanceof EntityPlayer && ((EntityPlayer)entity).getHealth() > 0.0f) {
            return true;
        }
        if (animals && (EntityUtil.isPassive(entity) || EntityUtil.isNeutralMob(entity))) {
            return true;
        }
        return mobs && EntityUtil.isHostileMob(entity);
    }
}

