/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.Event
 *  javafx.event.EventDispatchChain
 *  javafx.event.EventDispatcher
 */
package com.sun.javafx.event;

import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;

public class EventDispatchChainImpl
implements EventDispatchChain {
    private static final int CAPACITY_GROWTH_FACTOR = 8;
    private EventDispatcher[] dispatchers;
    private int[] nextLinks;
    private int reservedCount;
    private int activeCount;
    private int headIndex;
    private int tailIndex;

    public void reset() {
        for (int i = 0; i < this.reservedCount; ++i) {
            this.dispatchers[i] = null;
        }
        this.reservedCount = 0;
        this.activeCount = 0;
        this.headIndex = 0;
        this.tailIndex = 0;
    }

    public EventDispatchChain append(EventDispatcher eventDispatcher) {
        this.ensureCapacity(this.reservedCount + 1);
        if (this.activeCount == 0) {
            this.insertFirst(eventDispatcher);
            return this;
        }
        this.dispatchers[this.reservedCount] = eventDispatcher;
        this.nextLinks[this.tailIndex] = this.reservedCount;
        this.tailIndex = this.reservedCount++;
        ++this.activeCount;
        return this;
    }

    public EventDispatchChain prepend(EventDispatcher eventDispatcher) {
        this.ensureCapacity(this.reservedCount + 1);
        if (this.activeCount == 0) {
            this.insertFirst(eventDispatcher);
            return this;
        }
        this.dispatchers[this.reservedCount] = eventDispatcher;
        this.nextLinks[this.reservedCount] = this.headIndex;
        this.headIndex = this.reservedCount++;
        ++this.activeCount;
        return this;
    }

    public Event dispatchEvent(Event event) {
        if (this.activeCount == 0) {
            return event;
        }
        int n = this.headIndex;
        int n2 = this.tailIndex;
        int n3 = this.activeCount--;
        int n4 = this.reservedCount;
        EventDispatcher eventDispatcher = this.dispatchers[this.headIndex];
        this.headIndex = this.nextLinks[this.headIndex];
        Event event2 = eventDispatcher.dispatchEvent(event, (EventDispatchChain)this);
        this.headIndex = n;
        this.tailIndex = n2;
        this.activeCount = n3;
        this.reservedCount = n4;
        return event2;
    }

    private void insertFirst(EventDispatcher eventDispatcher) {
        this.dispatchers[this.reservedCount] = eventDispatcher;
        this.headIndex = this.reservedCount;
        this.tailIndex = this.reservedCount++;
        this.activeCount = 1;
    }

    private void ensureCapacity(int n) {
        int n2 = n + 8 - 1 & 0xFFFFFFF8;
        if (n2 == 0) {
            return;
        }
        if (this.dispatchers == null || this.dispatchers.length < n2) {
            EventDispatcher[] arreventDispatcher = new EventDispatcher[n2];
            int[] arrn = new int[n2];
            if (this.reservedCount > 0) {
                System.arraycopy(this.dispatchers, 0, arreventDispatcher, 0, this.reservedCount);
                System.arraycopy(this.nextLinks, 0, arrn, 0, this.reservedCount);
            }
            this.dispatchers = arreventDispatcher;
            this.nextLinks = arrn;
        }
    }
}

