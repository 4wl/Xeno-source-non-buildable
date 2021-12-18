/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.projectile.EntityFishHook
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraft.world.World
 */
package me.xenodevs.xeno.module.modules.movement;

import java.util.function.Predicate;
import me.xenodevs.xeno.event.impl.PacketEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.world.World;

public class Velocity
extends Module {
    public static Velocity INSTANCE;
    BooleanSetting velocityPacket = new BooleanSetting("Velocity PKT", true);
    NumberSetting horizontal = new NumberSetting("Horizontal", 0.0, 0.0, 1.0, 0.01);
    NumberSetting vertical = new NumberSetting("Vertical", 0.0, 0.0, 1.0, 0.01);
    BooleanSetting explosion = new BooleanSetting("Explosion", true);
    BooleanSetting fishhook = new BooleanSetting("Fishhook", true);
    @EventHandler
    public final Listener<PacketEvent.Receive> velocityListener = new Listener<PacketEvent.Receive>(event -> {
        Entity entity;
        SPacketEntityStatus packet;
        if (event.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)event.getPacket()).getEntityID() == this.mc.player.getEntityId() && this.velocityPacket.isEnabled()) {
            if (this.horizontal.getFloatValue() == 0.0f && this.vertical.getFloatValue() == 0.0f) {
                event.cancel();
                return;
            }
            SPacketEntityVelocity pkt = (SPacketEntityVelocity)event.getPacket();
            pkt.motionX = (int)((float)pkt.motionX * this.horizontal.getFloatValue());
            pkt.motionY = (int)((float)pkt.motionY * this.vertical.getFloatValue());
            pkt.motionZ = (int)((float)pkt.motionZ * this.horizontal.getFloatValue());
        }
        if (event.getPacket() instanceof SPacketExplosion && this.explosion.isEnabled()) {
            event.cancel();
        }
        if (event.getPacket() instanceof SPacketEntityStatus && this.fishhook.isEnabled() && (packet = (SPacketEntityStatus)event.getPacket()).getOpCode() == 31 && (entity = packet.getEntity((World)this.mc.world)) instanceof EntityFishHook) {
            EntityFishHook fishHook = (EntityFishHook)entity;
            if (fishHook.caughtEntity == this.mc.player) {
                event.cancel();
            }
        }
    }, new Predicate[0]);

    public Velocity() {
        super("Velocity", "stops u being knocked back", 0, Category.MOVEMENT);
        this.addSettings(this.velocityPacket, this.horizontal, this.vertical, this.explosion, this.fishhook);
        INSTANCE = this;
    }
}

