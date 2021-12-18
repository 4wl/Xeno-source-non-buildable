/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 */
package me.wolfsurge.mixin.mixins;

import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.event.impl.MotionEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={EntityPlayerSP.class})
public class MixinEntityPlayerSP
implements Globals {
    @Inject(method={"onUpdateWalkingPlayer"}, at={@At(value="TAIL")}, cancellable=true)
    public void hookUWPPost(CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        MotionEvent e = new MotionEvent(mc.player.posX, mc.player.getEntityBoundingBox().minY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, mc.player.onGround);
        Xeno.EVENT_BUS.post(e);
    }
}

