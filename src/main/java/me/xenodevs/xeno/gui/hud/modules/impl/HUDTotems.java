/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.NonNullList
 */
package me.xenodevs.xeno.gui.hud.modules.impl;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.module.modules.client.Colors;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class HUDTotems
extends HUDMod {
    public HUDTotems() {
        super("Totems", 0, 185, Xeno.moduleManager.getModule("Totems"));
    }

    public int getTotems() {
        if (this.nullCheck()) {
            return 0;
        }
        ItemStack offhand = HUDTotems.mc.player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
        NonNullList inv = HUDTotems.mc.player.inventory.mainInventory;
        int totems = 0;
        for (int inventoryIndex = 0; inventoryIndex < inv.size(); ++inventoryIndex) {
            if (((ItemStack)inv.get(inventoryIndex)).getItem() != Items.TOTEM_OF_UNDYING) continue;
            ++totems;
        }
        if (offhand.getItem() == Items.TOTEM_OF_UNDYING) {
            ++totems;
        }
        return totems;
    }

    @Override
    public void draw() {
        TextUtil.drawStringWithShadow("Totems: " + this.getTotems(), this.getX() + 1, this.getY(), Colors.colourInt);
        super.draw();
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        super.renderDummy(mouseX, mouseY);
        this.drag.setHeight(this.getHeight());
        this.drag.setWidth(this.getWidth());
        TextUtil.drawStringWithShadow("Totems: " + this.getTotems(), this.getX() + 1, this.getY(), this.parent.enabled ? Colors.colourInt : -7340032);
    }

    @Override
    public int getWidth() {
        return TextUtil.getStringWidth("Totems: " + this.getTotems());
    }

    @Override
    public float getHeight() {
        return 11.0f;
    }
}

