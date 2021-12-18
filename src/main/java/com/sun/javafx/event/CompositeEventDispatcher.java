/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.Event
 */
package com.sun.javafx.event;

import com.sun.javafx.event.BasicEventDispatcher;
import javafx.event.Event;

public abstract class CompositeEventDispatcher
extends BasicEventDispatcher {
    public abstract BasicEventDispatcher getFirstDispatcher();

    public abstract BasicEventDispatcher getLastDispatcher();

    @Override
    public final Event dispatchCapturingEvent(Event event) {
        for (BasicEventDispatcher basicEventDispatcher = this.getFirstDispatcher(); basicEventDispatcher != null && !(event = basicEventDispatcher.dispatchCapturingEvent(event)).isConsumed(); basicEventDispatcher = basicEventDispatcher.getNextDispatcher()) {
        }
        return event;
    }

    @Override
    public final Event dispatchBubblingEvent(Event event) {
        for (BasicEventDispatcher basicEventDispatcher = this.getLastDispatcher(); basicEventDispatcher != null && !(event = basicEventDispatcher.dispatchBubblingEvent(event)).isConsumed(); basicEventDispatcher = basicEventDispatcher.getPreviousDispatcher()) {
        }
        return event;
    }
}

