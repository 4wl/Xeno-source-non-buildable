/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.Event
 *  javafx.event.EventTarget
 *  javafx.event.EventType
 */
package com.sun.javafx.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class RedirectedEvent
extends Event {
    private static final long serialVersionUID = 20121107L;
    public static final EventType<RedirectedEvent> REDIRECTED = new EventType(Event.ANY, "REDIRECTED");
    private final Event originalEvent;

    public RedirectedEvent(Event event) {
        this(event, null, null);
    }

    public RedirectedEvent(Event event, Object object, EventTarget eventTarget) {
        super(object, eventTarget, REDIRECTED);
        this.originalEvent = event;
    }

    public Event getOriginalEvent() {
        return this.originalEvent;
    }
}

