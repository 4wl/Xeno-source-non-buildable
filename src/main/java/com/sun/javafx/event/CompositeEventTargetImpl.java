/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.EventDispatchChain
 *  javafx.event.EventTarget
 */
package com.sun.javafx.event;

import com.sun.javafx.event.CompositeEventTarget;
import com.sun.javafx.event.EventDispatchTree;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javafx.event.EventDispatchChain;
import javafx.event.EventTarget;

public class CompositeEventTargetImpl
implements CompositeEventTarget {
    private final Set<EventTarget> eventTargets;

    public CompositeEventTargetImpl(EventTarget ... arreventTarget) {
        HashSet<EventTarget> hashSet = new HashSet<EventTarget>(arreventTarget.length);
        hashSet.addAll(Arrays.asList(arreventTarget));
        this.eventTargets = Collections.unmodifiableSet(hashSet);
    }

    @Override
    public Set<EventTarget> getTargets() {
        return this.eventTargets;
    }

    @Override
    public boolean containsTarget(EventTarget eventTarget) {
        return this.eventTargets.contains((Object)eventTarget);
    }

    public EventDispatchChain buildEventDispatchChain(EventDispatchChain eventDispatchChain) {
        EventDispatchTree eventDispatchTree = (EventDispatchTree)eventDispatchChain;
        for (EventTarget eventTarget : this.eventTargets) {
            EventDispatchTree eventDispatchTree2 = eventDispatchTree.createTree();
            eventDispatchTree = eventDispatchTree.mergeTree((EventDispatchTree)eventTarget.buildEventDispatchChain((EventDispatchChain)eventDispatchTree2));
        }
        return eventDispatchTree;
    }
}

