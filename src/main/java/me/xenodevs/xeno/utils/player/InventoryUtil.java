/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Enchantments
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 */
package me.xenodevs.xeno.utils.player;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryUtil {
    private static final EntityPlayerSP player = Minecraft.getMinecraft().player;
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean getHeldItem(Item item) {
        return InventoryUtil.mc.player.getHeldItemMainhand().getItem().equals((Object)item) || InventoryUtil.mc.player.getHeldItemOffhand().getItem().equals((Object)item);
    }

    public static void switchToSlotGhost(int slot) {
        if (slot != -1 && InventoryUtil.mc.player.inventory.currentItem != slot) {
            InventoryUtil.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(slot));
        }
    }

    public static void switchToSlotGhost(Block block) {
        if (InventoryUtil.getBlockInHotbar(block) != -1 && InventoryUtil.mc.player.inventory.currentItem != InventoryUtil.getBlockInHotbar(block)) {
            InventoryUtil.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(InventoryUtil.getBlockInHotbar(block)));
        }
    }

    public static void switchToSlotGhost(Item item) {
        if (InventoryUtil.getHotbarItemSlot(item) != -1 && InventoryUtil.mc.player.inventory.currentItem != InventoryUtil.getHotbarItemSlot(item)) {
            InventoryUtil.switchToSlotGhost(InventoryUtil.getHotbarItemSlot(item));
        }
    }

    public static void moveItemToOffhand(int slot) {
        int returnSlot = -1;
        if (slot == -1) {
            return;
        }
        InventoryUtil.mc.playerController.windowClick(0, slot < 9 ? slot + 36 : slot, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtil.mc.player);
        InventoryUtil.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtil.mc.player);
        for (int i = 0; i < 45; ++i) {
            if (!InventoryUtil.mc.player.inventory.getStackInSlot(i).isEmpty()) continue;
            returnSlot = i;
            break;
        }
        if (returnSlot != -1) {
            InventoryUtil.mc.playerController.windowClick(0, returnSlot < 9 ? returnSlot + 36 : returnSlot, 0, ClickType.PICKUP, (EntityPlayer)InventoryUtil.mc.player);
        }
    }

    public static int getInventoryItemSlot(Item item, boolean hotbar) {
        int i;
        int n = i = hotbar ? 9 : 0;
        while (i < 45) {
            if (InventoryUtil.mc.player.inventory.getStackInSlot(i).getItem() == item) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    public static int getAnyBlockInHotbar() {
        for (int i = 0; i < 9; ++i) {
            Item item = InventoryUtil.mc.player.inventory.getStackInSlot(i).getItem();
            if (!(item instanceof ItemBlock)) continue;
            return i;
        }
        return -1;
    }

    public static int getItemCount(Item item) {
        return InventoryUtil.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == item).mapToInt(ItemStack::getMaxStackSize).sum();
    }

    public static int getItemCount(EntityPlayer entityPlayer, Item item) {
        return entityPlayer.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == item).mapToInt(ItemStack::getMaxStackSize).sum();
    }

    public static boolean Is32k(ItemStack stack) {
        stack.getEnchantmentTagList();
        for (int i = 0; i < stack.getEnchantmentTagList().tagCount(); ++i) {
            stack.getEnchantmentTagList().getCompoundTagAt(i);
            if (Enchantment.getEnchantmentByID((int)stack.getEnchantmentTagList().getCompoundTagAt(i).getByte("id")) == null || Enchantment.getEnchantmentByID((int)stack.getEnchantmentTagList().getCompoundTagAt(i).getShort("id")) == null || Enchantment.getEnchantmentByID((int)stack.getEnchantmentTagList().getCompoundTagAt(i).getShort("id")).isCurse() || stack.getEnchantmentTagList().getCompoundTagAt(i).getShort("lvl") < 1000) continue;
            return true;
        }
        return false;
    }

    public static boolean isHolding(Item item) {
        return InventoryUtil.mc.player.getHeldItemMainhand().getItem().equals((Object)item) || InventoryUtil.mc.player.getHeldItemOffhand().getItem().equals((Object)item);
    }

    public static boolean hasItem(Item input) {
        for (int i = 0; i < 36; ++i) {
            Item item = InventoryUtil.mc.player.inventory.getStackInSlot(i).getItem();
            if (item != input) continue;
            return true;
        }
        return false;
    }

    public static boolean isInventoryFull() {
        for (int i = 0; i < 36; ++i) {
            if (!InventoryUtil.mc.player.inventory.getStackInSlot(i).isEmpty()) continue;
            return false;
        }
        return true;
    }

    public static int getBestHotbarSword() {
        float best = -1.0f;
        int index = -1;
        for (int i = 0; i < 9; ++i) {
            ItemSword sword;
            float damage;
            ItemStack itemStack = InventoryUtil.player.inventory.getStackInSlot(i);
            if (itemStack == null || !(itemStack.getItem() instanceof ItemSword) || !((damage = (sword = (ItemSword)itemStack.getItem()).getAttackDamage() + (float)EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.SHARPNESS, (ItemStack)itemStack) * 1.25f) > best)) continue;
            best = damage;
            index = i;
        }
        return index;
    }

    public static int getBestArmour() {
        float best = -1.0f;
        int index = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = InventoryUtil.player.inventory.getStackInSlot(i);
            if (itemStack == null || !(itemStack.getItem() instanceof ItemSword)) continue;
            ItemArmor armour = (ItemArmor)itemStack.getItem();
            float damage = armour.toughness + (float)EnchantmentHelper.getEnchantmentLevel((Enchantment)Enchantments.SHARPNESS, (ItemStack)itemStack) * 1.25f;
            if (!(damage > best)) continue;
            best = damage;
            index = i;
        }
        return index;
    }

    public static void switchToSlot(int slot) {
        if (slot != -1 && InventoryUtil.mc.player.inventory.currentItem != slot) {
            InventoryUtil.mc.player.inventory.currentItem = slot;
        }
    }

    public static void switchToSlot(Block block) {
        if (InventoryUtil.getBlockInHotbar(block) != -1 && InventoryUtil.mc.player.inventory.currentItem != InventoryUtil.getBlockInHotbar(block)) {
            InventoryUtil.mc.player.inventory.currentItem = InventoryUtil.getBlockInHotbar(block);
        }
    }

    public static void switchToSlot(Item item) {
        if (InventoryUtil.getHotbarItemSlot(item) != -1 && InventoryUtil.mc.player.inventory.currentItem != InventoryUtil.getHotbarItemSlot(item)) {
            InventoryUtil.mc.player.inventory.currentItem = InventoryUtil.getHotbarItemSlot(item);
        }
    }

    public static int getBlockInHotbar(Block block) {
        for (int i = 0; i < 9; ++i) {
            Item item = InventoryUtil.mc.player.inventory.getStackInSlot(i).getItem();
            if (!(item instanceof ItemBlock) || !((ItemBlock)item).getBlock().equals((Object)block)) continue;
            return i;
        }
        return -1;
    }

    public static int getHotbarItemSlot(Item item) {
        for (int i = 0; i < 9; ++i) {
            if (InventoryUtil.mc.player.inventory.getStackInSlot(i).getItem() != item) continue;
            return i;
        }
        return -1;
    }
}

