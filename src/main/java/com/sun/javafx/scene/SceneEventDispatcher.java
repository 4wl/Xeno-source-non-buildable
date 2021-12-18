/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene;

import com.sun.javafx.event.BasicEventDispatcher;
import com.sun.javafx.event.CompositeEventDispatcher;
import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.scene.EnteredExitedHandler;
import com.sun.javafx.scene.KeyboardShortcutsHandler;

public class SceneEventDispatcher
extends CompositeEventDispatcher {
    private final KeyboardShortcutsHandler keyboardShortcutsHandler;
    private final EnteredExitedHandler enteredExitedHandler;
    private final EventHandlerManager eventHandlerManager;

    public SceneEventDispatcher(Object object) {
        this(new KeyboardShortcutsHandler(), new EnteredExitedHandler(object), new EventHandlerManager(object));
    }

    public SceneEventDispatcher(KeyboardShortcutsHandler keyboardShortcutsHandler, EnteredExitedHandler enteredExitedHandler, EventHandlerManager eventHandlerManager) {
        this.keyboardShortcutsHandler = keyboardShortcutsHandler;
        this.enteredExitedHandler = enteredExitedHandler;
        this.eventHandlerManager = eventHandlerManager;
        keyboardShortcutsHandler.insertNextDispatcher(enteredExitedHandler);
        enteredExitedHandler.insertNextDispatcher(eventHandlerManager);
    }

    public final KeyboardShortcutsHandler getKeyboardShortcutsHandler() {
        return this.keyboardShortcutsHandler;
    }

    public final EnteredExitedHandler getEnteredExitedHandler() {
        return this.enteredExitedHandler;
    }

    public final EventHandlerManager getEventHandlerManager() {
        return this.eventHandlerManager;
    }

    @Override
    public BasicEventDispatcher getFirstDispatcher() {
        return this.keyboardShortcutsHandler;
    }

    @Override
    public BasicEventDispatcher getLastDispatcher() {
        return this.eventHandlerManager;
    }
}

