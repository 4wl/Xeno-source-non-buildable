/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ChangeListener
 *  javafx.event.Event
 *  javafx.event.EventDispatcher
 *  javafx.event.EventHandler
 *  javafx.event.EventTarget
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseEvent
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.TwoLevelFocusBehavior;
import com.sun.javafx.scene.traversal.Direction;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class TwoLevelFocusListBehavior
extends TwoLevelFocusBehavior {
    final EventDispatcher preemptiveEventDispatcher = (event, eventDispatchChain) -> {
        if (event instanceof KeyEvent && event.getEventType() == KeyEvent.KEY_PRESSED && !((KeyEvent)event).isMetaDown() && !((KeyEvent)event).isControlDown() && !((KeyEvent)event).isAltDown() && this.isExternalFocus()) {
            EventTarget eventTarget = event.getTarget();
            switch (((KeyEvent)event).getCode()) {
                case TAB: {
                    if (((KeyEvent)event).isShiftDown()) {
                        ((Node)eventTarget).impl_traverse(Direction.PREVIOUS);
                    } else {
                        ((Node)eventTarget).impl_traverse(Direction.NEXT);
                    }
                    event.consume();
                    break;
                }
                case UP: {
                    ((Node)eventTarget).impl_traverse(Direction.UP);
                    event.consume();
                    break;
                }
                case DOWN: {
                    ((Node)eventTarget).impl_traverse(Direction.DOWN);
                    event.consume();
                    break;
                }
                case LEFT: {
                    ((Node)eventTarget).impl_traverse(Direction.LEFT);
                    event.consume();
                    break;
                }
                case RIGHT: {
                    ((Node)eventTarget).impl_traverse(Direction.RIGHT);
                    event.consume();
                    break;
                }
                case ENTER: {
                    this.setExternalFocus(false);
                    event.consume();
                    break;
                }
                default: {
                    Scene scene = this.tlNode.getScene();
                    Event.fireEvent((EventTarget)scene, (Event)event);
                    event.consume();
                }
            }
        }
        return event;
    };
    final EventDispatcher tlfEventDispatcher = (event, eventDispatchChain) -> {
        if (event instanceof KeyEvent && this.isExternalFocus()) {
            eventDispatchChain = eventDispatchChain.prepend(this.preemptiveEventDispatcher);
            return eventDispatchChain.dispatchEvent(event);
        }
        return this.origEventDispatcher.dispatchEvent(event, eventDispatchChain);
    };
    private final EventHandler<KeyEvent> keyEventListener = keyEvent -> this.postDispatchTidyup(keyEvent);
    final ChangeListener<Boolean> focusListener = (observableValue, bl, bl2) -> {
        if (bl2.booleanValue() && this.tlPopup != null) {
            this.setExternalFocus(false);
        } else {
            Parent parent;
            boolean bl3 = true;
            if (this.tlNode != null && (parent = this.tlNode.getParent()) != null && "combo-box-popup".equals(parent.getStyleClass().toString())) {
                bl3 = false;
            }
            this.setExternalFocus(bl3);
        }
    };
    private final EventHandler<MouseEvent> mouseEventListener = mouseEvent -> this.setExternalFocus(false);

    public TwoLevelFocusListBehavior(Node node) {
        this.tlNode = node;
        this.tlNode.addEventHandler(KeyEvent.ANY, this.keyEventListener);
        this.tlNode.addEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseEventListener);
        this.tlNode.focusedProperty().addListener(this.focusListener);
        this.origEventDispatcher = this.tlNode.getEventDispatcher();
        this.tlNode.setEventDispatcher(this.tlfEventDispatcher);
    }

    @Override
    public void dispose() {
        this.tlNode.removeEventHandler(KeyEvent.ANY, this.keyEventListener);
        this.tlNode.removeEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseEventListener);
        this.tlNode.focusedProperty().removeListener(this.focusListener);
        this.tlNode.setEventDispatcher(this.origEventDispatcher);
    }

    private Event postDispatchTidyup(Event event) {
        if (!(!(event instanceof KeyEvent) || event.getEventType() != KeyEvent.KEY_PRESSED || this.isExternalFocus() || ((KeyEvent)event).isMetaDown() || ((KeyEvent)event).isControlDown() || ((KeyEvent)event).isAltDown())) {
            switch (((KeyEvent)event).getCode()) {
                case TAB: 
                case UP: 
                case DOWN: 
                case LEFT: 
                case RIGHT: {
                    event.consume();
                    break;
                }
                case ENTER: {
                    this.setExternalFocus(true);
                    event.consume();
                    break;
                }
            }
        }
        return event;
    }
}

