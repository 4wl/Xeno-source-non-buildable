/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.xenodevs.xeno.command.impl;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.command.Command;
import me.xenodevs.xeno.managers.CommandManager;
import me.xenodevs.xeno.module.modules.movement.ElytraFly;
import me.xenodevs.xeno.utils.other.ChatUtil;
import org.lwjgl.input.Keyboard;

public class ElytraFlyCommand
extends Command {
    public ElytraFlyCommand() {
        super("ElytraFly", "elytrafly settings", "elytra key <key>", "elytra");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("key")) {
                ElytraFly.flightKey = Keyboard.getKeyIndex((String)args[1].toUpperCase());
                ChatUtil.addChatMessage("set elytrafly activate keybind to: " + Keyboard.getKeyName((int)ElytraFly.flightKey));
                Xeno.config.saveMisc();
            }
        } else {
            CommandManager.correctUsageMsg(this.getName(), this.getSyntax());
        }
    }
}

