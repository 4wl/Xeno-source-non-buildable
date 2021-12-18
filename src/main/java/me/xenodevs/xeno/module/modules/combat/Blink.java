/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.world.World
 */
package me.xenodevs.xeno.module.modules.combat;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;
import me.xenodevs.xeno.event.impl.PacketEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.world.World;

public class Blink
extends Module {
    private final Queue<CPacketPlayer> packetQueue = new LinkedList<CPacketPlayer>();
    private EntityOtherPlayerMP player;
    @EventHandler
    private final Listener<PacketEvent.Send> pkt = new Listener<PacketEvent.Send>(event -> {
        if (this.mc.player == null || this.mc.world == null) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayer) {
            event.cancel();
            this.packetQueue.add((CPacketPlayer)event.getPacket());
        }
    }, new Predicate[0]);

    public Blink() {
        super("Blink", "fake lag", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        this.player = new EntityOtherPlayerMP((World)this.mc.world, this.mc.getSession().getProfile());
        this.player.copyLocationAndAnglesFrom((Entity)this.mc.player);
        this.player.rotationYawHead = this.mc.player.rotationYawHead;
        this.mc.world.addEntityToWorld(-100, (Entity)this.player);
    }

    @Override
    public void onDisable() {
        while (!this.packetQueue.isEmpty()) {
            this.mc.player.connection.sendPacket((Packet)this.packetQueue.poll());
        }
        if (this.mc.player != null) {
            this.mc.world.removeEntityFromWorld(-100);
            this.player = null;
        }
    }
}

