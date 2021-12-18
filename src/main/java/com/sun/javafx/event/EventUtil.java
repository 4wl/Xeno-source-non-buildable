/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.Event
 *  javafx.event.EventDispatchChain
 *  javafx.event.EventTarget
 */
package com.sun.javafx.event;

import com.sun.javafx.event.CompositeEventTargetImpl;
import com.sun.javafx.event.EventDispatchChainImpl;
import com.sun.javafx.event.EventDispatchTreeImpl;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventTarget;

public final class EventUtil {
    private static final EventDispatchChainImpl eventDispatchChain = new EventDispatchChainImpl();
    private static final AtomicBoolean eventDispatchChainInUse = new AtomicBoolean();

    public static Event fireEvent(EventTarget eventTarget, Event event) {
        if (event.getTarget() != eventTarget) {
            event = event.copyFor(event.getSource(), eventTarget);
        }
        if (eventDispatchChainInUse.getAndSet(true)) {
            return EventUtil.fireEventImpl(new EventDispatchChainImpl(), eventTarget, event);
        }
        try {
            Event event2 = EventUtil.fireEventImpl(eventDispatchChain, eventTarget, event);
            return event2;
        }
        finally {
            eventDispatchChain.reset();
            eventDispatchChainInUse.set(false);
        }
    }

    public static Event fireEvent(Event event, EventTarget ... arreventTarget) {
        return EventUtil.fireEventImpl(new EventDispatchTreeImpl(), new CompositeEventTargetImpl(arreventTarget), event);
    }

    private static Event fireEventImpl(EventDispatchChain eventDispatchChain, EventTarget eventTarget, Event event) {
        EventDispatchChain eventDispatchChain2 = eventTarget.buildEventDispatchChain(eventDispatchChain);
        return eventDispatchChain2.dispatchEvent(event);
    }
}

