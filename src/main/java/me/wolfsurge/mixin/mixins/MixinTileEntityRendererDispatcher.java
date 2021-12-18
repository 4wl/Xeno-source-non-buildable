/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
 *  net.minecraft.tileentity.TileEntity
 */
package me.wolfsurge.mixin.mixins;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.event.impl.RenderTileEntityEvent;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={TileEntityRendererDispatcher.class})
public abstract class MixinTileEntityRendererDispatcher {
    @Shadow
    public static TileEntityRendererDispatcher instance;
    @Shadow
    public static double staticPlayerX;
    @Shadow
    public static double staticPlayerY;
    @Shadow
    public static double staticPlayerZ;
    @Shadow
    public double entityX;
    @Shadow
    public double entityY;
    @Shadow
    public double entityZ;

    @Inject(method={"render"}, at={@At(value="TAIL")}, cancellable=true)
    public void render(TileEntity tileentityIn, float partialTicks, int destroyStage, CallbackInfo ci) {
        RenderTileEntityEvent event = new RenderTileEntityEvent(instance, tileentityIn, partialTicks, destroyStage, this.entityX, this.entityX, this.entityX);
        event.setDestroyStage(destroyStage);
        event.setStaticPlayerX(staticPlayerX);
        event.setStaticPlayerY(staticPlayerY);
        event.setStaticPlayerZ(staticPlayerZ);
        event.setTileentityIn(tileentityIn);
        event.setTileEntityRendererDispatcher(instance);
        Xeno.EVENT_BUS.post(event);
    }
}

