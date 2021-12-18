/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.text.ITextComponent
 *  net.minecraft.util.text.TextComponentString
 */
package me.xenodevs.xeno.utils.other;

import me.xenodevs.xeno.Xeno;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class ChatUtil {
    static Minecraft mc = Minecraft.getMinecraft();

    public static void addChatMessage(String message) {
        message = "\u00a79" + Xeno.NAME + "\u00a7  > " + message;
        Minecraft.getMinecraft().player.sendMessage((ITextComponent)new TextComponentString(message));
    }
}

