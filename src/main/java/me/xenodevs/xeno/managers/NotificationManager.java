/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.text.TextFormatting
 */
package me.xenodevs.xeno.managers;

import me.xenodevs.xeno.notifications.Notification;
import me.xenodevs.xeno.notifications.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

public class NotificationManager {
    Minecraft mc = Minecraft.getMinecraft();

    public void call(Notification notification) {
        this.mc.player.sendChatMessage(this.addPrefix(notification.name, notification.priority) + notification.message);
    }

    public String addPrefix(String prefix, NotificationType type2) {
        TextFormatting col = TextFormatting.GRAY;
        if (type2 == NotificationType.INFO) {
            col = TextFormatting.GREEN;
        } else if (type2 == NotificationType.WARN) {
            col = TextFormatting.GOLD;
        } else if (type2 == NotificationType.ALERT) {
            col = TextFormatting.DARK_RED;
        }
        return (Object)TextFormatting.GRAY + "[" + (Object)col + prefix + (Object)TextFormatting.GRAY + "] " + (Object)TextFormatting.WHITE;
    }
}

