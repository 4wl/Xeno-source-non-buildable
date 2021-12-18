/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.ConditionalFeature
 *  javafx.application.Platform
 *  javafx.beans.value.ObservableValue
 *  javafx.event.EventHandler
 *  javafx.event.EventType
 *  javafx.scene.control.Control
 *  javafx.scene.control.SkinBase
 *  javafx.scene.input.ContextMenuEvent
 *  javafx.scene.input.MouseEvent
 *  javafx.util.Callback
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public abstract class BehaviorSkinBase<C extends Control, BB extends BehaviorBase<C>>
extends SkinBase<C> {
    protected static final boolean IS_TOUCH_SUPPORTED = Platform.isSupported((ConditionalFeature)ConditionalFeature.INPUT_TOUCH);
    private BB behavior;
    private MultiplePropertyChangeListenerHandler changeListenerHandler;
    private final EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>(){

        public void handle(MouseEvent mouseEvent) {
            EventType eventType = mouseEvent.getEventType();
            if (eventType == MouseEvent.MOUSE_ENTERED) {
                BehaviorSkinBase.this.behavior.mouseEntered(mouseEvent);
            } else if (eventType == MouseEvent.MOUSE_EXITED) {
                BehaviorSkinBase.this.behavior.mouseExited(mouseEvent);
            } else if (eventType == MouseEvent.MOUSE_PRESSED) {
                BehaviorSkinBase.this.behavior.mousePressed(mouseEvent);
            } else if (eventType == MouseEvent.MOUSE_RELEASED) {
                BehaviorSkinBase.this.behavior.mouseReleased(mouseEvent);
            } else if (eventType == MouseEvent.MOUSE_DRAGGED) {
                BehaviorSkinBase.this.behavior.mouseDragged(mouseEvent);
            } else {
                throw new AssertionError((Object)"Unsupported event type received");
            }
        }
    };
    private final EventHandler<ContextMenuEvent> contextMenuHandler = new EventHandler<ContextMenuEvent>(){

        public void handle(ContextMenuEvent contextMenuEvent) {
            BehaviorSkinBase.this.behavior.contextMenuRequested(contextMenuEvent);
        }
    };

    protected BehaviorSkinBase(C c, BB BB) {
        super(c);
        if (BB == null) {
            throw new IllegalArgumentException("Cannot pass null for behavior");
        }
        this.behavior = BB;
        c.addEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseHandler);
        c.addEventHandler(MouseEvent.MOUSE_EXITED, this.mouseHandler);
        c.addEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseHandler);
        c.addEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseHandler);
        c.addEventHandler(MouseEvent.MOUSE_DRAGGED, this.mouseHandler);
        c.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, this.contextMenuHandler);
    }

    public final BB getBehavior() {
        return this.behavior;
    }

    public void dispose() {
        Control control;
        if (this.changeListenerHandler != null) {
            this.changeListenerHandler.dispose();
        }
        if ((control = this.getSkinnable()) != null) {
            control.removeEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseHandler);
            control.removeEventHandler(MouseEvent.MOUSE_EXITED, this.mouseHandler);
            control.removeEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseHandler);
            control.removeEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseHandler);
            control.removeEventHandler(MouseEvent.MOUSE_DRAGGED, this.mouseHandler);
        }
        if (this.behavior != null) {
            ((BehaviorBase)this.behavior).dispose();
            this.behavior = null;
        }
        super.dispose();
    }

    protected final void registerChangeListener(ObservableValue<?> observableValue, String string2) {
        if (this.changeListenerHandler == null) {
            this.changeListenerHandler = new MultiplePropertyChangeListenerHandler((Callback<String, Void>)((Callback)string -> {
                this.handleControlPropertyChanged((String)string);
                return null;
            }));
        }
        this.changeListenerHandler.registerChangeListener(observableValue, string2);
    }

    protected void handleControlPropertyChanged(String string) {
    }
}

