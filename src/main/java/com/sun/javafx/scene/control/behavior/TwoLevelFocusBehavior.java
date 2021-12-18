/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ChangeListener
 *  javafx.css.PseudoClass
 *  javafx.event.Event
 *  javafx.event.EventDispatcher
 *  javafx.event.EventHandler
 *  javafx.event.EventTarget
 *  javafx.scene.Node
 *  javafx.scene.Scene
 *  javafx.scene.control.Control
 *  javafx.scene.control.PopupControl
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseEvent
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.traversal.Direction;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.PopupControl;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class TwoLevelFocusBehavior {
    Node tlNode = null;
    PopupControl tlPopup = null;
    EventDispatcher origEventDispatcher = null;
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
            this.setExternalFocus(true);
        }
    };
    private final EventHandler<MouseEvent> mouseEventListener = mouseEvent -> this.setExternalFocus(false);
    private boolean externalFocus = true;
    private static final PseudoClass INTERNAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass((String)"internal-focus");
    private static final PseudoClass EXTERNAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass((String)"external-focus");

    public TwoLevelFocusBehavior() {
    }

    public TwoLevelFocusBehavior(Node node) {
        this.tlNode = node;
        this.tlPopup = null;
        this.tlNode.addEventHandler(KeyEvent.ANY, this.keyEventListener);
        this.tlNode.addEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseEventListener);
        this.tlNode.focusedProperty().addListener(this.focusListener);
        this.origEventDispatcher = this.tlNode.getEventDispatcher();
        this.tlNode.setEventDispatcher(this.tlfEventDispatcher);
    }

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

    public boolean isExternalFocus() {
        return this.externalFocus;
    }

    public void setExternalFocus(boolean bl) {
        this.externalFocus = bl;
        if (this.tlNode != null && this.tlNode instanceof Control) {
            this.tlNode.pseudoClassStateChanged(INTERNAL_PSEUDOCLASS_STATE, !bl);
            this.tlNode.pseudoClassStateChanged(EXTERNAL_PSEUDOCLASS_STATE, bl);
        } else if (this.tlPopup != null) {
            this.tlPopup.pseudoClassStateChanged(INTERNAL_PSEUDOCLASS_STATE, !bl);
            this.tlPopup.pseudoClassStateChanged(EXTERNAL_PSEUDOCLASS_STATE, bl);
        }
    }
}

