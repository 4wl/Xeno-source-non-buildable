/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraftforge.client.event.ClientChatEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  org.lwjgl.input.Keyboard
 */
package me.xenodevs.xeno.managers;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.command.Command;
import me.xenodevs.xeno.command.impl.BindCommand;
import me.xenodevs.xeno.command.impl.ElytraFlyCommand;
import me.xenodevs.xeno.command.impl.FriendCommand;
import me.xenodevs.xeno.command.impl.GuiCommand;
import me.xenodevs.xeno.command.impl.HelpCommand;
import me.xenodevs.xeno.utils.other.ChatUtil;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class CommandManager {
    public static List<Command> commands = new ArrayList<Command>();
    public static String prefix = "+";
    public boolean commandFound = false;
    @EventHandler
    public Listener<ClientChatEvent> listener = new Listener<ClientChatEvent>(event -> {
        String message = event.getMessage();
        if (!message.startsWith(prefix)) {
            return;
        }
        event.setCanceled(true);
        message = message.substring(prefix.length());
        if (message.split(" ").length > 0) {
            boolean commandFound = false;
            String commandName = message.split(" ")[0];
            for (Command c : commands) {
                if (!c.aliases.contains(commandName) && !c.name.equalsIgnoreCase(commandName)) continue;
                c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
                commandFound = true;
                break;
            }
            if (!commandFound) {
                ChatUtil.addChatMessage((Object)ChatFormatting.DARK_RED + "Command not found! Use " + (Object)ChatFormatting.RESET + (Object)ChatFormatting.ITALIC + prefix + "help " + (Object)ChatFormatting.DARK_RED + "for help.");
            }
        }
    }, new Predicate[0]);

    public CommandManager() {
        this.register();
        Xeno.logger.info("Initialized Command Manager");
    }

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
        Xeno.EVENT_BUS.subscribe(this);
        commands.add(new FriendCommand());
        commands.add(new GuiCommand());
        commands.add(new ElytraFlyCommand());
        commands.add(new HelpCommand());
        commands.add(new BindCommand());
    }

    @SubscribeEvent
    public void key(InputEvent.KeyInputEvent e) {
        if (prefix.length() == 1) {
            char key = Keyboard.getEventCharacter();
            if (prefix.charAt(0) == key) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiChat());
                //Minecraft.getMinecraft().currentScreen.inputField.setText(prefix);
                // what is inputField?
            }
        }
    }

    public static void setCommandPrefix(String pre) {
        prefix = pre;
    }

    public static String getCommandPrefix(String name) {
        return prefix;
    }

    public static void correctUsageMsg(String name, String syntax) {
        String usage = "Correct usage of " + name + " command > " + prefix + syntax;
        ChatUtil.addChatMessage(usage);
    }
}

