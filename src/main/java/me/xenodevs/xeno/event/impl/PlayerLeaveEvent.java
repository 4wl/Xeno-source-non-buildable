/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.event.impl;

import me.xenodevs.xeno.event.Event;

public class PlayerLeaveEvent
extends Event {
    private final String name;

    public PlayerLeaveEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

