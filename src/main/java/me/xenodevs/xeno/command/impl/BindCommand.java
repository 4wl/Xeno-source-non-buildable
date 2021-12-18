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
import me.xenodevs.xeno.managers.ModuleManager;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.utils.other.ChatUtil;
import org.lwjgl.input.Keyboard;

public class BindCommand
extends Command {
    public BindCommand() {
        super("Bind", "bind module keybinds to keys", "bind <set|clear> <module> <key>", "bind");
    }

    @Override
    public void onCommand(String[] args, String command) {
        block9: {
            if (args != null) {
                if (args.length > 0) {
                    try {
                        if (args[0].equalsIgnoreCase("set") && args.length == 3) {
                            for (Module m : ModuleManager.modules) {
                                if (!args[1].equalsIgnoreCase(m.name)) continue;
                                m.keyCode.code = Keyboard.getKeyIndex((String)args[2].toUpperCase());
                                ChatUtil.addChatMessage("set " + m.name + "'s bind to " + args[2].toUpperCase());
                                Xeno.config.saveModConfig(m);
                                break block9;
                            }
                            break block9;
                        }
                        if (args[0].equalsIgnoreCase("clear") && args.length == 2) {
                            for (Module m : ModuleManager.modules) {
                                if (!args[1].equalsIgnoreCase(m.name)) continue;
                                m.keyCode.code = 0;
                                ChatUtil.addChatMessage("cleared " + m.name + "'s bind");
                                Xeno.config.saveModConfig(m);
                                break block9;
                            }
                            break block9;
                        }
                        CommandManager.correctUsageMsg(this.getName(), this.getSyntax());
                    }
                    catch (Exception e) {
                        CommandManager.correctUsageMsg(this.getName(), this.getSyntax());
                    }
                } else {
                    CommandManager.correctUsageMsg(this.getName(), this.getSyntax());
                }
            }
        }
    }
}

