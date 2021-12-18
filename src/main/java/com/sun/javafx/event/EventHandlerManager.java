/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.Event
 *  javafx.event.EventHandler
 *  javafx.event.EventType
 */
package com.sun.javafx.event;

import com.sun.javafx.event.BasicEventDispatcher;
import com.sun.javafx.event.CompositeEventHandler;
import java.util.HashMap;
import java.util.Map;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

public class EventHandlerManager
extends BasicEventDispatcher {
    private final Map<EventType<? extends Event>, CompositeEventHandler<? extends Event>> eventHandlerMap;
    private final Object eventSource;

    public EventHandlerManager(Object object) {
        this.eventSource = object;
        this.eventHandlerMap = new HashMap<EventType<? extends Event>, CompositeEventHandler<? extends Event>>();
    }

    public final <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        EventHandlerManager.validateEventType(eventType);
        EventHandlerManager.validateEventHandler(eventHandler);
        CompositeEventHandler<? super T> compositeEventHandler = this.createGetCompositeEventHandler(eventType);
        compositeEventHandler.addEventHandler(eventHandler);
    }

    public final <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        EventHandlerManager.validateEventType(eventType);
        EventHandlerManager.validateEventHandler(eventHandler);
        CompositeEventHandler<? extends Event> compositeEventHandler = this.eventHandlerMap.get(eventType);
        if (compositeEventHandler != null) {
            compositeEventHandler.removeEventHandler(eventHandler);
        }
    }

    public final <T extends Event> void addEventFilter(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        EventHandlerManager.validateEventType(eventType);
        EventHandlerManager.validateEventFilter(eventHandler);
        CompositeEventHandler<? super T> compositeEventHandler = this.createGetCompositeEventHandler(eventType);
        compositeEventHandler.addEventFilter(eventHandler);
    }

    public final <T extends Event> void removeEventFilter(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        EventHandlerManager.validateEventType(eventType);
        EventHandlerManager.validateEventFilter(eventHandler);
        CompositeEventHandler<? extends Event> compositeEventHandler = this.eventHandlerMap.get(eventType);
        if (compositeEventHandler != null) {
            compositeEventHandler.removeEventFilter(eventHandler);
        }
    }

    public final <T extends Event> void setEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        EventHandlerManager.validateEventType(eventType);
        CompositeEventHandler<Object> compositeEventHandler = this.eventHandlerMap.get(eventType);
        if (compositeEventHandler == null) {
            if (eventHandler == null) {
                return;
            }
            compositeEventHandler = new CompositeEventHandler();
            this.eventHandlerMap.put(eventType, compositeEventHandler);
        }
        compositeEventHandler.setEventHandler(eventHandler);
    }

    public final <T extends Event> EventHandler<? super T> getEventHandler(EventType<T> eventType) {
        CompositeEventHandler<? extends Event> compositeEventHandler = this.eventHandlerMap.get(eventType);
        return compositeEventHandler != null ? compositeEventHandler.getEventHandler() : null;
    }

    @Override
    public final Event dispatchCapturingEvent(Event event) {
        EventType eventType = event.getEventType();
        do {
            event = this.dispatchCapturingEvent((EventType<? extends Event>)eventType, event);
        } while ((eventType = eventType.getSuperType()) != null);
        return event;
    }

    @Override
    public final Event dispatchBubblingEvent(Event event) {
        EventType eventType = event.getEventType();
        do {
            event = this.dispatchBubblingEvent((EventType<? extends Event>)eventType, event);
        } while ((eventType = eventType.getSuperType()) != null);
        return event;
    }

    private <T extends Event> CompositeEventHandler<T> createGetCompositeEventHandler(EventType<T> eventType) {
        CompositeEventHandler<Object> compositeEventHandler = this.eventHandlerMap.get(eventType);
        if (compositeEventHandler == null) {
            compositeEventHandler = new CompositeEventHandler();
            this.eventHandlerMap.put(eventType, compositeEventHandler);
        }
        return compositeEventHandler;
    }

    protected Object getEventSource() {
        return this.eventSource;
    }

    private Event dispatchCapturingEvent(EventType<? extends Event> eventType, Event event) {
        CompositeEventHandler<? extends Event> compositeEventHandler = this.eventHandlerMap.get(eventType);
        if (compositeEventHandler != null) {
            event = EventHandlerManager.fixEventSource(event, this.eventSource);
            compositeEventHandler.dispatchCapturingEvent(event);
        }
        return event;
    }

    private Event dispatchBubblingEvent(EventType<? extends Event> eventType, Event event) {
        CompositeEventHandler<? extends Event> compositeEventHandler = this.eventHandlerMap.get(eventType);
        if (compositeEventHandler != null) {
            event = EventHandlerManager.fixEventSource(event, this.eventSource);
            compositeEventHandler.dispatchBubblingEvent(event);
        }
        return event;
    }

    private static Event fixEventSource(Event event, Object object) {
        return event.getSource() != object ? event.copyFor(object, event.getTarget()) : event;
    }

    private static void validateEventType(EventType<?> eventType) {
        if (eventType == null) {
            throw new NullPointerException("Event type must not be null");
        }
    }

    private static void validateEventHandler(EventHandler<?> eventHandler) {
        if (eventHandler == null) {
            throw new NullPointerException("Event handler must not be null");
        }
    }

    private static void validateEventFilter(EventHandler<?> eventHandler) {
        if (eventHandler == null) {
            throw new NullPointerException("Event filter must not be null");
        }
    }
}

