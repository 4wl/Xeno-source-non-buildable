/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.EventTarget
 */
package com.sun.javafx.event;

import java.util.Set;
import javafx.event.EventTarget;

public interface CompositeEventTarget
extends EventTarget {
    public Set<EventTarget> getTargets();

    public boolean containsTarget(EventTarget var1);
}

