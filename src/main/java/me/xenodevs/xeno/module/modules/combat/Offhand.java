/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.ContainerPlayer
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.NonNullList
 */
package me.xenodevs.xeno.module.modules.combat;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.entity.EntityUtil;
import me.xenodevs.xeno.utils.player.InventoryUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class Offhand
extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Totem", "Gapple", "Crystal");
    public BooleanSetting fallback = new BooleanSetting("Fallback", true);
    public ModeSetting fallbackMode = new ModeSetting("Fallback", "Totem", "Gapple", "Crystal");
    public BooleanSetting healthSwap = new BooleanSetting("TotemSwap", true);
    public NumberSetting health = new NumberSetting("TotemHealth", 16.0, 0.0, 36.0, 1.0);
    public NumberSetting cooldown = new NumberSetting("Cooldown", 0.0, 0.0, 40.0, 1.0);
    private int timer = 0;

    public Offhand() {
        super("Offhand", "moves stuff to ur offhand", 0, Category.COMBAT);
        this.addSettings(this.mode, this.healthSwap, this.health, this.cooldown, this.fallback, this.fallbackMode);
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        ++this.timer;
        float hp = EntityUtil.getHealth((Entity)this.mc.player);
        if (hp <= this.health.getFloatValue()) {
            this.swapItems(this.getItemSlot(Items.TOTEM_OF_UNDYING));
            return;
        }
        this.doOffhand();
    }

    public void doOffhand() {
        boolean doFallback = this.fallback.isEnabled();
        if (InventoryUtil.hasItem(this.getItem(this.mode.getMode()))) {
            if (!this.mode.is("Gapple")) {
                this.swapItems(this.getItemSlot(this.getItem(this.mode.getMode())));
            } else {
                EntityPlayerSP player = this.mc.player;
                ItemStack offhand = player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
                int inventoryIndex = 0;
                NonNullList inv = player.inventory.mainInventory;
                if (offhand == null || offhand.getItem() != Items.GOLDEN_APPLE) {
                    for (inventoryIndex = 0; inventoryIndex < inv.size(); ++inventoryIndex) {
                        if (inv.get(inventoryIndex) == ItemStack.EMPTY || ((ItemStack)inv.get(inventoryIndex)).getItem() != Items.GOLDEN_APPLE) continue;
                        if (!(this.mc.player.openContainer instanceof ContainerPlayer)) break;
                        this.mc.playerController.windowClick(0, inventoryIndex < 9 ? inventoryIndex + 36 : inventoryIndex, 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
                        this.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
                        this.mc.playerController.windowClick(0, inventoryIndex < 9 ? inventoryIndex + 36 : inventoryIndex, 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
                        break;
                    }
                }
            }
            doFallback = false;
            return;
        }
        if (doFallback && !InventoryUtil.hasItem(this.getItem(this.mode.getMode())) && this.mc.player.getHeldItemOffhand().getItem() != this.getItem(this.mode.getMode())) {
            this.swapItems(this.getItemSlot(this.getItem(this.fallbackMode.getMode())));
        }
    }

    public Item getItem(String item) {
        if (item.equalsIgnoreCase("Totem")) {
            return Items.TOTEM_OF_UNDYING;
        }
        if (item.equalsIgnoreCase("Gapple")) {
            return Items.GOLDEN_APPLE;
        }
        if (item.equalsIgnoreCase("Crystal")) {
            return Items.END_CRYSTAL;
        }
        return Items.TOTEM_OF_UNDYING;
    }

    public void swapItems(int slot) {
        if (slot == -1 || (double)this.timer <= this.cooldown.getDoubleValue() && this.mc.player.inventory.getStackInSlot(slot).getItem() != this.getItem(this.mode.getMode())) {
            return;
        }
        this.timer = 0;
        this.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
        this.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
        this.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)this.mc.player);
        this.mc.playerController.updateController();
    }

    private int getItemSlot(Item input) {
        if (input == this.mc.player.getHeldItemOffhand().getItem()) {
            return -1;
        }
        for (int i = 36; i >= 0; --i) {
            Item item = this.mc.player.inventory.getStackInSlot(i).getItem();
            if (item != input) continue;
            if (i < 9) {
                if (input == Items.GOLDEN_APPLE) {
                    return -1;
                }
                i += 36;
            }
            return i;
        }
        return -1;
    }

    @Override
    public String getHUDData() {
        return " " + this.mode.getMode();
    }
}

