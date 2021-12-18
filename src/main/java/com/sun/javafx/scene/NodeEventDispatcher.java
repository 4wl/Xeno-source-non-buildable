/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene;

import com.sun.javafx.event.BasicEventDispatcher;
import com.sun.javafx.event.CompositeEventDispatcher;
import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.scene.EnteredExitedHandler;

public class NodeEventDispatcher
extends CompositeEventDispatcher {
    private final EnteredExitedHandler enteredExitedHandler;
    private final EventHandlerManager eventHandlerManager;

    public NodeEventDispatcher(Object object) {
        this(new EnteredExitedHandler(object), new EventHandlerManager(object));
    }

    public NodeEventDispatcher(EnteredExitedHandler enteredExitedHandler, EventHandlerManager eventHandlerManager) {
        this.enteredExitedHandler = enteredExitedHandler;
        this.eventHandlerManager = eventHandlerManager;
        enteredExitedHandler.insertNextDispatcher(eventHandlerManager);
    }

    public final EnteredExitedHandler getEnteredExitedHandler() {
        return this.enteredExitedHandler;
    }

    public final EventHandlerManager getEventHandlerManager() {
        return this.eventHandlerManager;
    }

    @Override
    public BasicEventDispatcher getFirstDispatcher() {
        return this.enteredExitedHandler;
    }

    @Override
    public BasicEventDispatcher getLastDispatcher() {
        return this.eventHandlerManager;
    }
}

