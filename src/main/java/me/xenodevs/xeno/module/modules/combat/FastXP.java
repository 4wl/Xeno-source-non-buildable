/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Items
 */
package me.xenodevs.xeno.module.modules.combat;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.module.settings.Setting;
import net.minecraft.init.Items;

public class FastXP
extends Module {
    NumberSetting delay = new NumberSetting("Delay", 0.0, 0.0, 3.0, 1.0);

    public FastXP() {
        super("FastXP", "throw xp bottles very fast", Category.COMBAT);
        this.addSettings(new Setting[0]);
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        if (this.mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE || this.mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE) {
            this.mc.rightClickDelayTimer = 0;
        }
    }
}

