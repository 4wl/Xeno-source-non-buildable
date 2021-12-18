/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.events;

import com.sun.media.jfxmedia.events.PlayerEvent;

public class MarkerEvent
extends PlayerEvent {
    private String markerName;
    private double presentationTime;

    public MarkerEvent(String string, double d) {
        if (string == null) {
            throw new IllegalArgumentException("name == null!");
        }
        if (d < 0.0) {
            throw new IllegalArgumentException("time < 0.0!");
        }
        this.markerName = string;
        this.presentationTime = d;
    }

    public String getMarkerName() {
        return this.markerName;
    }

    public double getPresentationTime() {
        return this.presentationTime;
    }
}

