/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.play.server.SPacketPlayerListItem
 *  net.minecraft.network.play.server.SPacketPlayerListItem$Action
 *  net.minecraft.network.play.server.SPacketPlayerListItem$AddPlayerData
 *  net.minecraftforge.client.event.ClientChatEvent
 *  net.minecraftforge.client.event.ClientChatReceivedEvent
 *  net.minecraftforge.client.event.DrawBlockHighlightEvent
 *  net.minecraftforge.client.event.InputUpdateEvent
 *  net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent
 *  net.minecraftforge.client.event.RenderBlockOverlayEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Post
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Text
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.entity.living.LivingDamageEvent
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 *  net.minecraftforge.event.entity.living.LivingEntityUseItemEvent$Finish
 *  net.minecraftforge.event.entity.player.AttackEntityEvent
 *  net.minecraftforge.event.world.WorldEvent$Load
 *  net.minecraftforge.event.world.WorldEvent$Unload
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$MouseInputEvent
 *  net.minecraftforge.fml.common.gameevent.PlayerEvent$PlayerRespawnEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  org.apache.commons.io.IOUtils
 *  org.lwjgl.input.Mouse
 */
package me.xenodevs.xeno.managers;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.function.Predicate;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.event.impl.PacketEvent;
import me.xenodevs.xeno.event.impl.PlayerJoinEvent;
import me.xenodevs.xeno.event.impl.PlayerLeaveEvent;
import me.xenodevs.xeno.gui.hud.HudConfig;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.lwjgl.input.Mouse;

public class EventManager {
    public static EventManager instance;
    Minecraft mc = Minecraft.getMinecraft();
    @EventHandler
    private final Listener<PacketEvent.Receive> receiveListener = new Listener<PacketEvent.Receive>(event -> {
        if (event.getPacket() instanceof SPacketPlayerListItem) {
            SPacketPlayerListItem packet = (SPacketPlayerListItem)event.getPacket();
            if (packet.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER) {
                for (SPacketPlayerListItem.AddPlayerData playerData : packet.getEntries()) {
                    if (playerData.getProfile().getId() == this.mc.getSession().getProfile().getId()) continue;
                    new Thread(() -> {
                        String name = this.resolveName(playerData.getProfile().getId().toString());
                        if (name != null && this.mc.player != null && this.mc.player.ticksExisted >= 1000) {
                            Xeno.EVENT_BUS.post(new PlayerJoinEvent(name));
                        }
                    }).start();
                }
            }
            if (packet.getAction() == SPacketPlayerListItem.Action.REMOVE_PLAYER) {
                for (SPacketPlayerListItem.AddPlayerData playerData : packet.getEntries()) {
                    if (playerData.getProfile().getId() == this.mc.getSession().getProfile().getId()) continue;
                    new Thread(() -> {
                        String name = this.resolveName(playerData.getProfile().getId().toString());
                        if (name != null && this.mc.player != null && this.mc.player.ticksExisted >= 1000) {
                            Xeno.EVENT_BUS.post(new PlayerLeaveEvent(name));
                        }
                    }).start();
                }
            }
        }
    }, new Predicate[0]);
    private final Map<String, String> uuidNameCache = Maps.newConcurrentMap();

    public EventManager() {
        instance = this;
        Xeno.EVENT_BUS.subscribe((Object)this);
        MinecraftForge.EVENT_BUS.register((Object)this);
        Xeno.logger.info("Initialized Event Processor");
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        Xeno.EVENT_BUS.post((Object)event);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Xeno.EVENT_BUS.post((Object)event);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        Xeno.EVENT_BUS.post((Object)event);
        if (!(Minecraft.getMinecraft().currentScreen instanceof HudConfig) && event.getType() == RenderGameOverlayEvent.ElementType.TEXT && Xeno.moduleManager.isModuleEnabled("HUD")) {
            Xeno.hudManager.renderMods();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        Xeno.EVENT_BUS.post((Object)event);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        Xeno.EVENT_BUS.post((Object)event);
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButtonState()) {
            Xeno.EVENT_BUS.post((Object)event);
        }
    }

    @SubscribeEvent
    public void onRenderScreen(RenderGameOverlayEvent.Text event) {
        Xeno.EVENT_BUS.post((Object)event);
    }

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        Xeno.EVENT_BUS.post((Object)event);
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        Xeno.EVENT_BUS.post((Object)event);
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        Xeno.EVENT_BUS.post((Object)event);
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Xeno.EVENT_BUS.post((Object)event);
    }

    @SubscribeEvent
    public void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
        Xeno.EVENT_BUS.post((Object)event);
    }

    @SubscribeEvent
    public void onRenderBlockOverlay(RenderBlockOverlayEvent event) {
        Xeno.EVENT_BUS.post((Object)event);
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        Xeno.EVENT_BUS.post((Object)event);
    }

    @SubscribeEvent
    public void onLivingEntityUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        Xeno.EVENT_BUS.post((Object)event);
    }

    @SubscribeEvent
    public void onInputUpdate(InputUpdateEvent event) {
        Xeno.EVENT_BUS.post((Object)event);
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        Xeno.EVENT_BUS.post((Object)event);
    }

    @SubscribeEvent
    public void onPlayerPush(PlayerSPPushOutOfBlocksEvent event) {
        Xeno.EVENT_BUS.post((Object)event);
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        Xeno.EVENT_BUS.post((Object)event);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        Xeno.EVENT_BUS.post((Object)event);
    }

    public String resolveName(String uuid) {
        if (this.uuidNameCache.containsKey(uuid = uuid.replace("-", ""))) {
            return this.uuidNameCache.get(uuid);
        }
        String url = "https://api.mojang.com/user/profiles/" + uuid + "/names";
        try {
            JSONObject latestName;
            JSONArray jsonArray;
            String nameJson = IOUtils.toString((URL)new URL(url));
            if (nameJson != null && nameJson.length() > 0 && (jsonArray = (JSONArray)JSONValue.parseWithException(nameJson)) != null && (latestName = (JSONObject)jsonArray.get(jsonArray.size() - 1)) != null) {
                return latestName.get("name").toString();
            }
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}

