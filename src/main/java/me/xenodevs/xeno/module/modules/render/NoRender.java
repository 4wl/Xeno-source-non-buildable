/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketTimeUpdate
 *  net.minecraft.network.play.server.SPacketUpdateBossInfo
 *  net.minecraftforge.client.event.RenderBlockOverlayEvent
 *  net.minecraftforge.client.event.RenderBlockOverlayEvent$OverlayType
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.xenodevs.xeno.module.modules.render;

import java.util.function.Predicate;
import me.xenodevs.xeno.event.impl.PacketEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoRender
extends Module {
    public static NoRender INSTANCE;
    BooleanSetting fire = new BooleanSetting("Fire", true);
    BooleanSetting water = new BooleanSetting("Water", true);
    BooleanSetting block = new BooleanSetting("Block", false);
    public static BooleanSetting portal;
    public static BooleanSetting potions;
    BooleanSetting bossBars = new BooleanSetting("Bossbars", true);
    BooleanSetting time = new BooleanSetting("Change Time", false);
    NumberSetting timeSet = new NumberSetting("Time", 0.0, 0.0, 23000.0, 100.0);
    @EventHandler
    private final Listener<PacketEvent.Receive> pktRec = new Listener<PacketEvent.Receive>(event -> {
        if (event.getPacket() instanceof SPacketTimeUpdate && this.time.isEnabled()) {
            event.cancel();
        }
        if (event.getPacket() instanceof SPacketUpdateBossInfo && this.bossBars.isEnabled()) {
            event.cancel();
        }
    }, new Predicate[0]);

    public NoRender() {
        super("NoRender", "stops things from being rendered", 0, Category.RENDER);
        this.addSettings(this.fire, this.water, this.block, portal, this.bossBars, potions, this.time, this.timeSet);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.time.isEnabled()) {
            this.mc.world.setWorldTime((long)this.timeSet.getDoubleValue());
        }
    }

    @SubscribeEvent
    public void onRenderBlockOverlay(RenderBlockOverlayEvent event) {
        if (this.nullCheck()) {
            return;
        }
        if (event.getOverlayType().equals((Object)RenderBlockOverlayEvent.OverlayType.FIRE) && this.fire.getValue()) {
            event.setCanceled(true);
        }
        if (event.getOverlayType().equals((Object)RenderBlockOverlayEvent.OverlayType.WATER) && this.water.getValue()) {
            event.setCanceled(true);
        }
        if (event.getOverlayType().equals((Object)RenderBlockOverlayEvent.OverlayType.BLOCK) && this.block.getValue()) {
            event.setCanceled(true);
        }
    }

    static {
        portal = new BooleanSetting("Portal", true);
        potions = new BooleanSetting("Potions", true);
    }
}

