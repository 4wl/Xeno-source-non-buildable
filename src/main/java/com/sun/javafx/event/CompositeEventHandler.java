/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.Event
 *  javafx.event.EventHandler
 *  javafx.event.WeakEventHandler
 */
package com.sun.javafx.event;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;

public final class CompositeEventHandler<T extends Event> {
    private EventProcessorRecord<T> firstRecord;
    private EventProcessorRecord<T> lastRecord;
    private EventHandler<? super T> eventHandler;

    public void setEventHandler(EventHandler<? super T> eventHandler) {
        this.eventHandler = eventHandler;
    }

    public EventHandler<? super T> getEventHandler() {
        return this.eventHandler;
    }

    public void addEventHandler(EventHandler<? super T> eventHandler) {
        if (this.find(eventHandler, false) == null) {
            this.append(this.lastRecord, this.createEventHandlerRecord(eventHandler));
        }
    }

    public void removeEventHandler(EventHandler<? super T> eventHandler) {
        EventProcessorRecord<? super T> eventProcessorRecord = this.find(eventHandler, false);
        if (eventProcessorRecord != null) {
            this.remove(eventProcessorRecord);
        }
    }

    public void addEventFilter(EventHandler<? super T> eventHandler) {
        if (this.find(eventHandler, true) == null) {
            this.append(this.lastRecord, this.createEventFilterRecord(eventHandler));
        }
    }

    public void removeEventFilter(EventHandler<? super T> eventHandler) {
        EventProcessorRecord<? super T> eventProcessorRecord = this.find(eventHandler, true);
        if (eventProcessorRecord != null) {
            this.remove(eventProcessorRecord);
        }
    }

    public void dispatchBubblingEvent(Event event) {
        Event event2 = event;
        EventProcessorRecord eventProcessorRecord = this.firstRecord;
        while (eventProcessorRecord != null) {
            if (eventProcessorRecord.isDisconnected()) {
                this.remove(eventProcessorRecord);
            } else {
                eventProcessorRecord.handleBubblingEvent(event2);
            }
            eventProcessorRecord = eventProcessorRecord.nextRecord;
        }
        if (this.eventHandler != null) {
            this.eventHandler.handle(event2);
        }
    }

    public void dispatchCapturingEvent(Event event) {
        Event event2 = event;
        EventProcessorRecord eventProcessorRecord = this.firstRecord;
        while (eventProcessorRecord != null) {
            if (eventProcessorRecord.isDisconnected()) {
                this.remove(eventProcessorRecord);
            } else {
                eventProcessorRecord.handleCapturingEvent(event2);
            }
            eventProcessorRecord = eventProcessorRecord.nextRecord;
        }
    }

    boolean containsHandler(EventHandler<? super T> eventHandler) {
        return this.find(eventHandler, false) != null;
    }

    boolean containsFilter(EventHandler<? super T> eventHandler) {
        return this.find(eventHandler, true) != null;
    }

    private EventProcessorRecord<T> createEventHandlerRecord(EventHandler<? super T> eventHandler) {
        return eventHandler instanceof WeakEventHandler ? new WeakEventHandlerRecord((WeakEventHandler)eventHandler) : new NormalEventHandlerRecord<T>(eventHandler);
    }

    private EventProcessorRecord<T> createEventFilterRecord(EventHandler<? super T> eventHandler) {
        return eventHandler instanceof WeakEventHandler ? new WeakEventFilterRecord((WeakEventHandler)eventHandler) : new NormalEventFilterRecord<T>(eventHandler);
    }

    private void remove(EventProcessorRecord<T> eventProcessorRecord) {
        EventProcessorRecord eventProcessorRecord2 = ((EventProcessorRecord)eventProcessorRecord).prevRecord;
        EventProcessorRecord eventProcessorRecord3 = ((EventProcessorRecord)eventProcessorRecord).nextRecord;
        if (eventProcessorRecord2 != null) {
            eventProcessorRecord2.nextRecord = eventProcessorRecord3;
        } else {
            this.firstRecord = eventProcessorRecord3;
        }
        if (eventProcessorRecord3 != null) {
            eventProcessorRecord3.prevRecord = eventProcessorRecord2;
        } else {
            this.lastRecord = eventProcessorRecord2;
        }
    }

    private void append(EventProcessorRecord<T> eventProcessorRecord, EventProcessorRecord<T> eventProcessorRecord2) {
        EventProcessorRecord<T> eventProcessorRecord3;
        if (eventProcessorRecord != null) {
            eventProcessorRecord3 = ((EventProcessorRecord)eventProcessorRecord).nextRecord;
            ((EventProcessorRecord)eventProcessorRecord).nextRecord = (EventProcessorRecord)eventProcessorRecord2;
        } else {
            eventProcessorRecord3 = this.firstRecord;
            this.firstRecord = eventProcessorRecord2;
        }
        if (eventProcessorRecord3 != null) {
            ((EventProcessorRecord)eventProcessorRecord3).prevRecord = (EventProcessorRecord)eventProcessorRecord2;
        } else {
            this.lastRecord = eventProcessorRecord2;
        }
        ((EventProcessorRecord)eventProcessorRecord2).prevRecord = (EventProcessorRecord)eventProcessorRecord;
        ((EventProcessorRecord)eventProcessorRecord2).nextRecord = (EventProcessorRecord)eventProcessorRecord3;
    }

    private EventProcessorRecord<T> find(EventHandler<? super T> eventHandler, boolean bl) {
        EventProcessorRecord eventProcessorRecord = this.firstRecord;
        while (eventProcessorRecord != null) {
            if (eventProcessorRecord.isDisconnected()) {
                this.remove(eventProcessorRecord);
            } else if (eventProcessorRecord.stores(eventHandler, bl)) {
                return eventProcessorRecord;
            }
            eventProcessorRecord = eventProcessorRecord.nextRecord;
        }
        return null;
    }

    private static final class WeakEventFilterRecord<T extends Event>
    extends EventProcessorRecord<T> {
        private final WeakEventHandler<? super T> weakEventFilter;

        public WeakEventFilterRecord(WeakEventHandler<? super T> weakEventHandler) {
            this.weakEventFilter = weakEventHandler;
        }

        @Override
        public boolean stores(EventHandler<? super T> eventHandler, boolean bl) {
            return bl && this.weakEventFilter == eventHandler;
        }

        @Override
        public void handleBubblingEvent(T t) {
        }

        @Override
        public void handleCapturingEvent(T t) {
            this.weakEventFilter.handle(t);
        }

        @Override
        public boolean isDisconnected() {
            return this.weakEventFilter.wasGarbageCollected();
        }
    }

    private static final class NormalEventFilterRecord<T extends Event>
    extends EventProcessorRecord<T> {
        private final EventHandler<? super T> eventFilter;

        public NormalEventFilterRecord(EventHandler<? super T> eventHandler) {
            this.eventFilter = eventHandler;
        }

        @Override
        public boolean stores(EventHandler<? super T> eventHandler, boolean bl) {
            return bl && this.eventFilter == eventHandler;
        }

        @Override
        public void handleBubblingEvent(T t) {
        }

        @Override
        public void handleCapturingEvent(T t) {
            this.eventFilter.handle(t);
        }

        @Override
        public boolean isDisconnected() {
            return false;
        }
    }

    private static final class WeakEventHandlerRecord<T extends Event>
    extends EventProcessorRecord<T> {
        private final WeakEventHandler<? super T> weakEventHandler;

        public WeakEventHandlerRecord(WeakEventHandler<? super T> weakEventHandler) {
            this.weakEventHandler = weakEventHandler;
        }

        @Override
        public boolean stores(EventHandler<? super T> eventHandler, boolean bl) {
            return !bl && this.weakEventHandler == eventHandler;
        }

        @Override
        public void handleBubblingEvent(T t) {
            this.weakEventHandler.handle(t);
        }

        @Override
        public void handleCapturingEvent(T t) {
        }

        @Override
        public boolean isDisconnected() {
            return this.weakEventHandler.wasGarbageCollected();
        }
    }

    private static final class NormalEventHandlerRecord<T extends Event>
    extends EventProcessorRecord<T> {
        private final EventHandler<? super T> eventHandler;

        public NormalEventHandlerRecord(EventHandler<? super T> eventHandler) {
            this.eventHandler = eventHandler;
        }

        @Override
        public boolean stores(EventHandler<? super T> eventHandler, boolean bl) {
            return !bl && this.eventHandler == eventHandler;
        }

        @Override
        public void handleBubblingEvent(T t) {
            this.eventHandler.handle(t);
        }

        @Override
        public void handleCapturingEvent(T t) {
        }

        @Override
        public boolean isDisconnected() {
            return false;
        }
    }

    private static abstract class EventProcessorRecord<T extends Event> {
        private EventProcessorRecord<T> nextRecord;
        private EventProcessorRecord<T> prevRecord;

        private EventProcessorRecord() {
        }

        public abstract boolean stores(EventHandler<? super T> var1, boolean var2);

        public abstract void handleBubblingEvent(T var1);

        public abstract void handleCapturingEvent(T var1);

        public abstract boolean isDisconnected();
    }
}

