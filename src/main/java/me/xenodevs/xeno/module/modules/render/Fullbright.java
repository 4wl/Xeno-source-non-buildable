/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.MobEffects
 *  net.minecraft.potion.PotionEffect
 */
package me.xenodevs.xeno.module.modules.render;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.ModeSetting;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class Fullbright
extends Module {
    public static Fullbright INSTANCE;
    public static ModeSetting mode;
    float oldBright;

    public Fullbright() {
        super("Fullbright", "makes everything bright", Category.RENDER);
        this.addSettings(mode);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        this.oldBright = this.mc.gameSettings.gammaSetting;
        if (mode.getValue() == 0) {
            this.mc.gameSettings.gammaSetting = 100.0f;
        }
    }

    @Override
    public void onDisable() {
        this.mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
        if (mode.getValue() == 0) {
            this.mc.gameSettings.gammaSetting = this.oldBright;
        }
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        if (mode.is("Effect")) {
            this.mc.player.addPotionEffect(new PotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 80950, 1, false, false)));
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode();
    }

    static {
        mode = new ModeSetting("Mode", "Gamma", "Effect");
    }
}

