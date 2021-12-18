/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.notifications;

import me.xenodevs.xeno.notifications.NotificationType;

public class Notification {
    public String name;
    public String message;
    public NotificationType priority;

    public Notification(String name, String message, NotificationType priority) {
        this.name = name;
        this.message = message;
        this.priority = priority;
    }
}

