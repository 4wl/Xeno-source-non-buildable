/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package me.xenodevs.xeno.event.impl;

import me.xenodevs.xeno.event.Event;
import net.minecraft.entity.Entity;

public class RenderEntityNameEvent
extends Event {
    public Entity entityIn;
    public double x;
    public double y;
    public double z;
    public double distanceSq;
    public String name;

    public RenderEntityNameEvent(Entity entityIn, double x, double y, double z, String name, double distanceSq) {
        this.entityIn = entityIn;
        this.x = x;
        this.y = y;
        this.z = z;
        this.distanceSq = distanceSq;
        this.name = name;
    }
}

