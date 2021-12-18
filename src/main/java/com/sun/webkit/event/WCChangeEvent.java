/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.event;

public final class WCChangeEvent {
    private final Object source;

    public WCChangeEvent(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("null source");
        }
        this.source = object;
    }

    public Object getSource() {
        return this.source;
    }

    public String toString() {
        return this.getClass().getName() + "[source=" + this.source + "]";
    }
}

