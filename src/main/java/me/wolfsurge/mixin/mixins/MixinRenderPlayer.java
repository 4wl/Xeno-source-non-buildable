/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.renderer.entity.RenderPlayer
 *  net.minecraft.entity.Entity
 */
package me.wolfsurge.mixin.mixins;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.event.impl.RenderEntityNameEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={RenderPlayer.class})
public class MixinRenderPlayer {
    @Inject(method={"renderEntityName"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderLivingLabel(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq, CallbackInfo info) {
        if (Xeno.moduleManager.isModuleEnabled("Nametags")) {
            info.cancel();
        }
        RenderEntityNameEvent event = new RenderEntityNameEvent((Entity)entityIn, x, y, z, name, distanceSq);
        Xeno.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }
}

