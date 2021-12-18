/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemExpBottle
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.EnumHand
 */
package me.xenodevs.xeno.module.modules.combat;

import java.util.function.Predicate;
import me.xenodevs.xeno.event.impl.PacketEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.utils.player.InventoryUtil;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

public class AutoXP
extends Module {
    BooleanSetting autoRepair = new BooleanSetting("AutoRepair", true);
    BooleanSetting switchh = new BooleanSetting("Switch", false);
    BooleanSetting switchBack = new BooleanSetting("Switch Back", true);
    ModeSetting mode = new ModeSetting("Mode", "Half", "Quarter");
    @EventHandler
    public Listener<PacketEvent.Send> listener = new Listener<PacketEvent.Send>(event -> {
        if (event.getPacket() instanceof CPacketPlayerTryUseItem && this.mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle) {
            this.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(this.mc.player.rotationYaw, 90.0f, this.mc.player.onGround));
        }
    }, new Predicate[0]);

    public AutoXP() {
        super("AutoXP", "repairs armour automatically (works well with fastxp)", Category.COMBAT);
        this.addSettings(this.autoRepair, this.switchh, this.switchBack, this.mode);
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        if (this.autoRepair.isEnabled()) {
            boolean doRepair = false;
            for (ItemStack stack : this.mc.player.inventory.armorInventory) {
                boolean hasMending = false;
                NBTTagList enchants = stack.getEnchantmentTagList();
                for (int index = 0; index < enchants.tagCount(); ++index) {
                    short id = enchants.getCompoundTagAt(index).getShort("id");
                    Enchantment enc = Enchantment.getEnchantmentByID((int)id);
                    if (enc == null || !enc.getName().equalsIgnoreCase("enchantment.mending")) continue;
                    hasMending = true;
                }
                int divBy = 2;
                if (this.mode.is("Quarter")) {
                    divBy = 4;
                }
                if (stack == null || !hasMending || !((float)stack.getMaxDamage() - (float)stack.getItemDamage() <= (float)(stack.getMaxDamage() / divBy))) continue;
                doRepair = true;
            }
            if (doRepair) {
                if (this.switchh.isEnabled()) {
                    InventoryUtil.switchToSlot(Items.EXPERIENCE_BOTTLE);
                }
                int slot = -1;
                if (this.switchh.isEnabled() && this.switchBack.isEnabled()) {
                    slot = this.mc.player.inventory.currentItem;
                }
                if (InventoryUtil.isHolding(Items.EXPERIENCE_BOTTLE)) {
                    EnumHand hand = EnumHand.MAIN_HAND;
                    if (this.mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE) {
                        hand = EnumHand.OFF_HAND;
                    }
                    this.mc.rightClickMouse();
                }
                if (this.switchh.isEnabled() && this.switchBack.isEnabled()) {
                    this.mc.player.inventory.currentItem = slot;
                }
            }
        }
    }
}

