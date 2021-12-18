/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.ContainerChest
 */
package me.xenodevs.xeno.module.modules.misc;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.other.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;

public class ChestStealer
extends Module {
    ModeSetting mode = new ModeSetting("Mode", "Steal", "Drop");
    NumberSetting delay = new NumberSetting("DelayMS", 100.0, 0.0, 200.0, 5.0);
    Timer timer = new Timer();

    public ChestStealer() {
        super("ChestStealer", "takes everything from chests", Category.MISC);
        this.addSettings(this.mode, this.delay);
    }

    @Override
    public void onUpdate() {
        if (this.mc.player.openContainer != null && this.mc.player.openContainer instanceof ContainerChest) {
            ContainerChest chest = (ContainerChest)this.mc.player.openContainer;
            for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
                if (chest.getLowerChestInventory().getStackInSlot(i) == null) continue;
                this.mc.playerController.windowClick(chest.windowId, i, 0, ClickType.QUICK_MOVE, (EntityPlayer)this.mc.player);
            }
        }
    }
}

