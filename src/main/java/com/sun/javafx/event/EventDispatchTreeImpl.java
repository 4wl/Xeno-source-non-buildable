/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.Event
 *  javafx.event.EventDispatchChain
 *  javafx.event.EventDispatcher
 */
package com.sun.javafx.event;

import com.sun.javafx.event.EventDispatchTree;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;

public final class EventDispatchTreeImpl
implements EventDispatchTree {
    private static final int CAPACITY_GROWTH_FACTOR = 8;
    private static final int NULL_INDEX = -1;
    private EventDispatcher[] dispatchers;
    private int[] nextChildren;
    private int[] nextSiblings;
    private int reservedCount;
    private int rootIndex = -1;
    private int tailFirstIndex = -1;
    private int tailLastIndex = -1;
    private boolean expandTailFirstPath;

    public void reset() {
        for (int i = 0; i < this.reservedCount; ++i) {
            this.dispatchers[i] = null;
        }
        this.reservedCount = 0;
        this.rootIndex = -1;
        this.tailFirstIndex = -1;
        this.tailLastIndex = -1;
    }

    @Override
    public EventDispatchTree createTree() {
        return new EventDispatchTreeImpl();
    }

    @Override
    public EventDispatchTree mergeTree(EventDispatchTree eventDispatchTree) {
        int n;
        if (this.tailFirstIndex != -1) {
            if (this.rootIndex != -1) {
                this.expandTailFirstPath = true;
                this.expandTail(this.rootIndex);
            } else {
                this.rootIndex = this.tailFirstIndex;
            }
            this.tailFirstIndex = -1;
            this.tailLastIndex = -1;
        }
        EventDispatchTreeImpl eventDispatchTreeImpl = (EventDispatchTreeImpl)eventDispatchTree;
        int n2 = n = eventDispatchTreeImpl.rootIndex != -1 ? eventDispatchTreeImpl.rootIndex : eventDispatchTreeImpl.tailFirstIndex;
        if (this.rootIndex == -1) {
            this.rootIndex = this.copyTreeLevel(eventDispatchTreeImpl, n);
        } else {
            this.mergeTreeLevel(eventDispatchTreeImpl, this.rootIndex, n);
        }
        return this;
    }

    @Override
    public EventDispatchTree append(EventDispatcher eventDispatcher) {
        this.ensureCapacity(this.reservedCount + 1);
        this.dispatchers[this.reservedCount] = eventDispatcher;
        this.nextSiblings[this.reservedCount] = -1;
        this.nextChildren[this.reservedCount] = -1;
        if (this.tailFirstIndex == -1) {
            this.tailFirstIndex = this.reservedCount;
        } else {
            this.nextChildren[this.tailLastIndex] = this.reservedCount;
        }
        this.tailLastIndex = this.reservedCount++;
        return this;
    }

    @Override
    public EventDispatchTree prepend(EventDispatcher eventDispatcher) {
        this.ensureCapacity(this.reservedCount + 1);
        this.dispatchers[this.reservedCount] = eventDispatcher;
        this.nextSiblings[this.reservedCount] = -1;
        this.nextChildren[this.reservedCount] = this.rootIndex;
        this.rootIndex = this.reservedCount++;
        return this;
    }

    public Event dispatchEvent(Event event) {
        if (this.rootIndex == -1) {
            if (this.tailFirstIndex == -1) {
                return event;
            }
            this.rootIndex = this.tailFirstIndex;
            this.tailFirstIndex = -1;
            this.tailLastIndex = -1;
        }
        int n = this.reservedCount;
        int n2 = this.rootIndex;
        int n3 = this.tailFirstIndex;
        int n4 = this.tailLastIndex;
        Event event2 = null;
        int n5 = this.rootIndex;
        do {
            this.rootIndex = this.nextChildren[n5];
            Event event3 = this.dispatchers[n5].dispatchEvent(event, (EventDispatchChain)this);
            if (event3 == null) continue;
            Event event4 = event2 = event2 != null ? event : event3;
        } while ((n5 = this.nextSiblings[n5]) != -1);
        this.reservedCount = n;
        this.rootIndex = n2;
        this.tailFirstIndex = n3;
        this.tailLastIndex = n4;
        return event2;
    }

    public String toString() {
        int n;
        int n2 = n = this.rootIndex != -1 ? this.rootIndex : this.tailFirstIndex;
        if (n == -1) {
            return "()";
        }
        StringBuilder stringBuilder = new StringBuilder();
        this.appendTreeLevel(stringBuilder, n);
        return stringBuilder.toString();
    }

    private void ensureCapacity(int n) {
        int n2 = n + 8 - 1 & 0xFFFFFFF8;
        if (n2 == 0) {
            return;
        }
        if (this.dispatchers == null || this.dispatchers.length < n2) {
            EventDispatcher[] arreventDispatcher = new EventDispatcher[n2];
            int[] arrn = new int[n2];
            int[] arrn2 = new int[n2];
            if (this.reservedCount > 0) {
                System.arraycopy(this.dispatchers, 0, arreventDispatcher, 0, this.reservedCount);
                System.arraycopy(this.nextChildren, 0, arrn, 0, this.reservedCount);
                System.arraycopy(this.nextSiblings, 0, arrn2, 0, this.reservedCount);
            }
            this.dispatchers = arreventDispatcher;
            this.nextChildren = arrn;
            this.nextSiblings = arrn2;
        }
    }

    private void expandTail(int n) {
        int n2 = n;
        while (n2 != -1) {
            if (this.nextChildren[n2] != -1) {
                this.expandTail(this.nextChildren[n2]);
            } else if (this.expandTailFirstPath) {
                this.nextChildren[n2] = this.tailFirstIndex;
                this.expandTailFirstPath = false;
            } else {
                int n3;
                this.nextChildren[n2] = n3 = this.copyTreeLevel(this, this.tailFirstIndex);
            }
            n2 = this.nextSiblings[n2];
        }
    }

    private void mergeTreeLevel(EventDispatchTreeImpl eventDispatchTreeImpl, int n, int n2) {
        int n3 = n2;
        while (n3 != -1) {
            int n4;
            EventDispatcher eventDispatcher = eventDispatchTreeImpl.dispatchers[n3];
            int n5 = n;
            int n6 = n;
            while (n5 != -1 && eventDispatcher != this.dispatchers[n5]) {
                n6 = n5;
                n5 = this.nextSiblings[n5];
            }
            if (n5 == -1) {
                this.nextSiblings[n6] = n4 = this.copySubtree(eventDispatchTreeImpl, n3);
                this.nextSiblings[n4] = -1;
            } else {
                n4 = this.nextChildren[n5];
                int n7 = EventDispatchTreeImpl.getChildIndex(eventDispatchTreeImpl, n3);
                if (n4 != -1) {
                    this.mergeTreeLevel(eventDispatchTreeImpl, n4, n7);
                } else {
                    this.nextChildren[n5] = n4 = this.copyTreeLevel(eventDispatchTreeImpl, n7);
                }
            }
            n3 = eventDispatchTreeImpl.nextSiblings[n3];
        }
    }

    private int copyTreeLevel(EventDispatchTreeImpl eventDispatchTreeImpl, int n) {
        int n2;
        if (n == -1) {
            return -1;
        }
        int n3 = n;
        int n4 = n2 = this.copySubtree(eventDispatchTreeImpl, n3);
        n3 = eventDispatchTreeImpl.nextSiblings[n3];
        while (n3 != -1) {
            int n5;
            this.nextSiblings[n4] = n5 = this.copySubtree(eventDispatchTreeImpl, n3);
            n4 = n5;
            n3 = eventDispatchTreeImpl.nextSiblings[n3];
        }
        this.nextSiblings[n4] = -1;
        return n2;
    }

    private int copySubtree(EventDispatchTreeImpl eventDispatchTreeImpl, int n) {
        this.ensureCapacity(this.reservedCount + 1);
        int n2 = this.reservedCount++;
        int n3 = this.copyTreeLevel(eventDispatchTreeImpl, EventDispatchTreeImpl.getChildIndex(eventDispatchTreeImpl, n));
        this.dispatchers[n2] = eventDispatchTreeImpl.dispatchers[n];
        this.nextChildren[n2] = n3;
        return n2;
    }

    private void appendTreeLevel(StringBuilder stringBuilder, int n) {
        stringBuilder.append('(');
        int n2 = n;
        this.appendSubtree(stringBuilder, n2);
        n2 = this.nextSiblings[n2];
        while (n2 != -1) {
            stringBuilder.append(",");
            this.appendSubtree(stringBuilder, n2);
            n2 = this.nextSiblings[n2];
        }
        stringBuilder.append(')');
    }

    private void appendSubtree(StringBuilder stringBuilder, int n) {
        stringBuilder.append((Object)this.dispatchers[n]);
        int n2 = EventDispatchTreeImpl.getChildIndex(this, n);
        if (n2 != -1) {
            stringBuilder.append("->");
            this.appendTreeLevel(stringBuilder, n2);
        }
    }

    private static int getChildIndex(EventDispatchTreeImpl eventDispatchTreeImpl, int n) {
        int n2 = eventDispatchTreeImpl.nextChildren[n];
        if (n2 == -1 && n != eventDispatchTreeImpl.tailLastIndex) {
            n2 = eventDispatchTreeImpl.tailFirstIndex;
        }
        return n2;
    }
}

