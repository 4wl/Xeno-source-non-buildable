/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.renderer.InventoryEffectRenderer
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.init.Enchantments
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.DamageSource
 */
package me.xenodevs.xeno.module.modules.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.other.EnchantmentUtil;
import me.xenodevs.xeno.utils.other.ItemUtil;
import me.xenodevs.xeno.utils.other.Timer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class AutoArmour
extends Module {
    NumberSetting delay = new NumberSetting("Delay", 300.0, 0.0, 1000.0, 50.0);
    ModeSetting prefer = new ModeSetting("Prefer", "Chestplate", "Elytra");
    Timer timerr = new Timer();

    public AutoArmour() {
        super("AutoArmour", "Automatically equips your best armour.", Category.COMBAT);
        this.addSettings(this.delay);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        if (this.mc.currentScreen instanceof GuiContainer && !(this.mc.currentScreen instanceof InventoryEffectRenderer)) {
            return;
        }
        if (this.timerr.hasTimeElapsed((long)this.delay.getDoubleValue(), true)) {
            ItemStack stack;
            EntityPlayerSP player = this.mc.player;
            InventoryPlayer inventory = player.inventory;
            if (player.moveForward != 0.0f || player.movementInput.moveStrafe != 0.0f) {
                return;
            }
            int[] bestArmorSlots = new int[4];
            int[] bestArmorValues = new int[4];
            for (int type2 = 0; type2 < 4; ++type2) {
                bestArmorSlots[type2] = -1;
                stack = inventory.armorItemInSlot(type2);
                ItemStack chest = this.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                if (ItemUtil.isNullOrEmpty(stack) || !(stack.getItem() instanceof ItemArmor)) continue;
                ItemArmor item = (ItemArmor)stack.getItem();
                bestArmorValues[type2] = this.getArmorValue(item, stack);
            }
            for (int slot = 0; slot < 36; ++slot) {
                stack = inventory.getStackInSlot(slot);
                if (ItemUtil.isNullOrEmpty(stack) || !(stack.getItem() instanceof ItemArmor)) continue;
                ItemArmor item = (ItemArmor)stack.getItem();
                int armorType = ItemUtil.getArmorType(item);
                int armorValue = this.getArmorValue(item, stack);
                if (armorValue <= bestArmorValues[armorType]) continue;
                bestArmorSlots[armorType] = slot;
                bestArmorValues[armorType] = armorValue;
            }
            ArrayList<Integer> types = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));
            Collections.shuffle(types);
            for (int i : types) {
                ItemStack oldArmor;
                int j = bestArmorSlots[i];
                if (j == -1 || !ItemUtil.isNullOrEmpty(oldArmor = inventory.armorItemInSlot(i)) && inventory.getFirstEmptyStack() == -1) continue;
                if (j < 9) {
                    j += 36;
                }
                if (!ItemUtil.isNullOrEmpty(oldArmor)) {
                    this.mc.playerController.windowClick(0, 8 - i, 0, ClickType.QUICK_MOVE, (EntityPlayer)this.mc.player);
                }
                this.mc.playerController.windowClick(0, j, 0, ClickType.QUICK_MOVE, (EntityPlayer)this.mc.player);
                break;
            }
        }
    }

    private int getArmorValue(ItemArmor item, ItemStack stack) {
        int armorPoints = item.damageReduceAmount;
        int prtPoints = 0;
        int armorToughness = (int)ItemUtil.getArmorToughness(item);
        int armorType = item.getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.LEGS);
        Enchantment protection = Enchantments.PROTECTION;
        int prtLvl = EnchantmentUtil.getEnchantmentLevel(protection, stack);
        EntityPlayerSP player = this.mc.player;
        DamageSource dmgSource = DamageSource.causePlayerDamage((EntityPlayer)player);
        prtPoints = protection.calcModifierDamage(prtLvl, dmgSource);
        return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
    }
}

