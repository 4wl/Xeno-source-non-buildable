/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.Event
 *  javafx.event.EventTarget
 */
package com.sun.javafx.event;

import java.util.ArrayDeque;
import java.util.Queue;
import javafx.event.Event;
import javafx.event.EventTarget;

public final class EventQueue {
    private Queue<Event> queue = new ArrayDeque<Event>();
    private boolean inLoop;

    public void postEvent(Event event) {
        this.queue.add(event);
    }

    public void fire() {
        if (this.inLoop) {
            return;
        }
        this.inLoop = true;
        try {
            while (!this.queue.isEmpty()) {
                Event event = this.queue.remove();
                Event.fireEvent((EventTarget)event.getTarget(), (Event)event);
            }
        }
        finally {
            this.inLoop = false;
        }
    }
}

