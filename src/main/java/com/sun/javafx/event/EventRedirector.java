/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.Event
 *  javafx.event.EventDispatchChain
 *  javafx.event.EventDispatcher
 *  javafx.event.EventType
 */
package com.sun.javafx.event;

import com.sun.javafx.event.BasicEventDispatcher;
import com.sun.javafx.event.DirectEvent;
import com.sun.javafx.event.EventDispatchChainImpl;
import com.sun.javafx.event.RedirectedEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.event.EventType;

public class EventRedirector
extends BasicEventDispatcher {
    private final EventDispatchChainImpl eventDispatchChain;
    private final List<EventDispatcher> eventDispatchers = new CopyOnWriteArrayList<EventDispatcher>();
    private final Object eventSource;

    public EventRedirector(Object object) {
        this.eventDispatchChain = new EventDispatchChainImpl();
        this.eventSource = object;
    }

    protected void handleRedirectedEvent(Object object, Event event) {
    }

    public final void addEventDispatcher(EventDispatcher eventDispatcher) {
        this.eventDispatchers.add(eventDispatcher);
    }

    public final void removeEventDispatcher(EventDispatcher eventDispatcher) {
        this.eventDispatchers.remove((Object)eventDispatcher);
    }

    @Override
    public final Event dispatchCapturingEvent(Event event) {
        EventType eventType = event.getEventType();
        if (eventType == DirectEvent.DIRECT) {
            event = ((DirectEvent)event).getOriginalEvent();
        } else {
            this.redirectEvent(event);
            if (eventType == RedirectedEvent.REDIRECTED) {
                this.handleRedirectedEvent(event.getSource(), ((RedirectedEvent)event).getOriginalEvent());
            }
        }
        return event;
    }

    private void redirectEvent(Event event) {
        if (!this.eventDispatchers.isEmpty()) {
            RedirectedEvent redirectedEvent = event.getEventType() == RedirectedEvent.REDIRECTED ? (RedirectedEvent)event : new RedirectedEvent(event, this.eventSource, null);
            for (EventDispatcher eventDispatcher : this.eventDispatchers) {
                this.eventDispatchChain.reset();
                eventDispatcher.dispatchEvent((Event)redirectedEvent, (EventDispatchChain)this.eventDispatchChain);
            }
        }
    }
}

