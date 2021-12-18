/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiIngame
 *  net.minecraft.client.gui.ScaledResolution
 */
package me.wolfsurge.mixin.mixins;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.module.modules.render.NoRender;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiIngame.class})
public abstract class MixinGuiIngame {
    @Inject(method={"renderPotionEffects"}, at={@At(value="HEAD")}, cancellable=true)
    public void hookPotions(ScaledResolution sr, CallbackInfo info) {
        if (Xeno.moduleManager.isModuleEnabled("NoRender") && NoRender.potions.isEnabled()) {
            info.cancel();
        }
    }

    @Inject(method={"renderPortal"}, at={@At(value="HEAD")}, cancellable=true)
    public void hookPortal(CallbackInfo info) {
        if (Xeno.moduleManager.isModuleEnabled("NoRender") && NoRender.portal.isEnabled()) {
            info.cancel();
        }
    }
}

