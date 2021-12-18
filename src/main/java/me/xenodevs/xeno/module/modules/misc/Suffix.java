/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraftforge.client.event.ClientChatEvent
 */
package me.xenodevs.xeno.module.modules.misc;

import java.util.function.Predicate;
import me.xenodevs.xeno.managers.CommandManager;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.StringSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.client.event.ClientChatEvent;

public class Suffix
extends Module {
    StringSetting suffixMessage = new StringSetting("MSG", "xeno on top");
    @EventHandler
    private final Listener<ClientChatEvent> chatListener = new Listener<ClientChatEvent>(event -> {
        if (!event.getMessage().startsWith(CommandManager.prefix) && !event.getMessage().startsWith("/")) {
            event.setCanceled(true);
            this.mc.getConnection().sendPacket((Packet)new CPacketChatMessage(event.getMessage() + " | " + this.suffixMessage.getText()));
        }
    }, new Predicate[0]);

    public Suffix() {
        super("Suffix", "adds text after every message", 0, Category.MISC);
        this.addSettings(this.suffixMessage);
    }
}

