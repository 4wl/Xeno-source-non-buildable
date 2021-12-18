/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.ConditionalFeature
 *  javafx.application.Platform
 *  javafx.beans.InvalidationListener
 *  javafx.event.EventHandler
 *  javafx.scene.Node
 *  javafx.scene.control.Control
 *  javafx.scene.input.ContextMenuEvent
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseEvent
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.traversal.Direction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class BehaviorBase<C extends Control> {
    protected static final boolean IS_TOUCH_SUPPORTED = Platform.isSupported((ConditionalFeature)ConditionalFeature.INPUT_TOUCH);
    protected static final List<KeyBinding> TRAVERSAL_BINDINGS = new ArrayList<KeyBinding>();
    static final String TRAVERSE_UP = "TraverseUp";
    static final String TRAVERSE_DOWN = "TraverseDown";
    static final String TRAVERSE_LEFT = "TraverseLeft";
    static final String TRAVERSE_RIGHT = "TraverseRight";
    static final String TRAVERSE_NEXT = "TraverseNext";
    static final String TRAVERSE_PREVIOUS = "TraversePrevious";
    private final C control;
    private final List<KeyBinding> keyBindings;
    private final EventHandler<KeyEvent> keyEventListener = keyEvent -> {
        if (!keyEvent.isConsumed()) {
            this.callActionForEvent((KeyEvent)keyEvent);
        }
    };
    private final InvalidationListener focusListener = observable -> this.focusChanged();

    public BehaviorBase(C c, List<KeyBinding> list) {
        this.control = c;
        this.keyBindings = list == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<KeyBinding>(list));
        c.addEventHandler(KeyEvent.ANY, this.keyEventListener);
        c.focusedProperty().addListener(this.focusListener);
    }

    public void dispose() {
        this.control.removeEventHandler(KeyEvent.ANY, this.keyEventListener);
        this.control.focusedProperty().removeListener(this.focusListener);
    }

    public final C getControl() {
        return this.control;
    }

    protected void callActionForEvent(KeyEvent keyEvent) {
        String string = this.matchActionForEvent(keyEvent);
        if (string != null) {
            this.callAction(string);
            keyEvent.consume();
        }
    }

    protected String matchActionForEvent(KeyEvent keyEvent) {
        if (keyEvent == null) {
            throw new NullPointerException("KeyEvent must not be null");
        }
        KeyBinding keyBinding = null;
        int n = 0;
        int n2 = this.keyBindings.size();
        for (int i = 0; i < n2; ++i) {
            KeyBinding keyBinding2 = this.keyBindings.get(i);
            int n3 = keyBinding2.getSpecificity((Control)this.control, keyEvent);
            if (n3 <= n) continue;
            n = n3;
            keyBinding = keyBinding2;
        }
        String string = null;
        if (keyBinding != null) {
            string = keyBinding.getAction();
        }
        return string;
    }

    protected void callAction(String string) {
        switch (string) {
            case "TraverseUp": {
                this.traverseUp();
                break;
            }
            case "TraverseDown": {
                this.traverseDown();
                break;
            }
            case "TraverseLeft": {
                this.traverseLeft();
                break;
            }
            case "TraverseRight": {
                this.traverseRight();
                break;
            }
            case "TraverseNext": {
                this.traverseNext();
                break;
            }
            case "TraversePrevious": {
                this.traversePrevious();
            }
        }
    }

    protected void traverse(Node node, Direction direction) {
        node.impl_traverse(direction);
    }

    public final void traverseUp() {
        this.traverse((Node)this.control, Direction.UP);
    }

    public final void traverseDown() {
        this.traverse((Node)this.control, Direction.DOWN);
    }

    public final void traverseLeft() {
        this.traverse((Node)this.control, Direction.LEFT);
    }

    public final void traverseRight() {
        this.traverse((Node)this.control, Direction.RIGHT);
    }

    public final void traverseNext() {
        this.traverse((Node)this.control, Direction.NEXT);
    }

    public final void traversePrevious() {
        this.traverse((Node)this.control, Direction.PREVIOUS);
    }

    protected void focusChanged() {
    }

    public void mousePressed(MouseEvent mouseEvent) {
    }

    public void mouseDragged(MouseEvent mouseEvent) {
    }

    public void mouseReleased(MouseEvent mouseEvent) {
    }

    public void mouseEntered(MouseEvent mouseEvent) {
    }

    public void mouseExited(MouseEvent mouseEvent) {
    }

    public void contextMenuRequested(ContextMenuEvent contextMenuEvent) {
    }

    static {
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.UP, TRAVERSE_UP));
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.DOWN, TRAVERSE_DOWN));
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.LEFT, TRAVERSE_LEFT));
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, TRAVERSE_RIGHT));
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.TAB, TRAVERSE_NEXT));
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.TAB, TRAVERSE_PREVIOUS).shift());
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.UP, TRAVERSE_UP).shift().alt().ctrl());
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.DOWN, TRAVERSE_DOWN).shift().alt().ctrl());
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.LEFT, TRAVERSE_LEFT).shift().alt().ctrl());
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, TRAVERSE_RIGHT).shift().alt().ctrl());
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.TAB, TRAVERSE_NEXT).shift().alt().ctrl());
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.TAB, TRAVERSE_PREVIOUS).alt().ctrl());
    }
}

