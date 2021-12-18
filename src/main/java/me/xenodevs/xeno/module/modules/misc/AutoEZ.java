/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.world.World
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 */
package me.xenodevs.xeno.module.modules.misc;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import me.xenodevs.xeno.event.impl.PacketEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.StringSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class AutoEZ
extends Module {
    public StringSetting msg = new StringSetting("MSG", "ez");
    public BooleanSetting greenText = new BooleanSetting("Green Text", true);
    int delay = 0;
    private static final ConcurrentHashMap<Object, Integer> targetedPlayers = new ConcurrentHashMap();
    public static String customMsgArg = "";
    @EventHandler
    private Listener<PacketEvent.Send> sendListener = new Listener<PacketEvent.Send>(event -> {
        Entity targetEntity;
        CPacketUseEntity cPacketUseEntity;
        if (this.mc.player == null) {
            return;
        }
        if (event.getPacket() instanceof CPacketUseEntity && (cPacketUseEntity = (CPacketUseEntity)event.getPacket()).getAction().equals((Object)CPacketUseEntity.Action.ATTACK) && (targetEntity = cPacketUseEntity.getEntityFromWorld((World)this.mc.world)) instanceof EntityPlayer) {
            AutoEZ.addTarget(targetEntity.getName());
        }
    }, new Predicate[0]);
    @EventHandler
    private Listener<LivingDeathEvent> livingDeathListener = new Listener<LivingDeathEvent>(event -> {
        EntityPlayer player;
        if (this.mc.player == null) {
            return;
        }
        EntityLivingBase e = event.getEntityLiving();
        if (e == null) {
            return;
        }
        if (e instanceof EntityPlayer && (player = (EntityPlayer)e).getHealth() <= 0.0f && targetedPlayers.containsKey(player.getName())) {
            this.announce(player.getName());
        }
    }, new Predicate[0]);

    public AutoEZ() {
        super("AutoEZ", "lets people know you're clouted", 0, Category.MISC);
        this.addSettings(this.msg);
    }

    public static void setMessage(String msg) {
        customMsgArg = msg;
    }

    @Override
    public void onUpdate() {
        for (Entity entity : this.mc.world.getLoadedEntityList()) {
            EntityPlayer player;
            if (!(entity instanceof EntityPlayer) || !((player = (EntityPlayer)entity).getHealth() <= 0.0f) || !targetedPlayers.containsKey(player.getName())) continue;
            this.announce(player.getName());
        }
        targetedPlayers.forEach((name, timeout) -> {
            if (timeout <= 0) {
                targetedPlayers.remove(name);
            } else {
                targetedPlayers.put(name, timeout - 1);
            }
        });
        ++this.delay;
    }

    public void announce(String name) {
        if (this.delay < 150) {
            return;
        }
        this.delay = 0;
        targetedPlayers.remove(name);
        String starter = "";
        if (this.greenText.isEnabled()) {
            starter = "> ";
        }
        String message = "";
        message = starter + this.msg.getText();
        this.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(message));
    }

    public static void addTarget(String name) {
        if (!Objects.equals(name, Minecraft.getMinecraft().player.getName())) {
            targetedPlayers.put(name, 20);
        }
    }

    @Override
    public String getHUDData() {
        return "";
    }
}

