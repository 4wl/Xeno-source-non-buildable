/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.command.impl;

import me.xenodevs.xeno.command.Command;
import me.xenodevs.xeno.gui.click.ClickGui;
import me.xenodevs.xeno.managers.CommandManager;
import me.xenodevs.xeno.utils.other.ChatUtil;

public class GuiCommand
extends Command {
    public GuiCommand() {
        super("Gui", "clickgui stuff", "gui reset", "gui");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reset")) {
                ClickGui.reset();
                ChatUtil.addChatMessage("reset clickgui");
            }
        } else {
            CommandManager.correctUsageMsg(this.getName(), this.getSyntax());
        }
    }
}

