/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.NodeOrientation
 *  javafx.scene.control.ScrollPane
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseEvent
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.skin.ScrollPaneSkin;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ScrollPaneBehavior
extends BehaviorBase<ScrollPane> {
    static final String TRAVERSE_DEBUG = "TraverseDebug";
    static final String HORIZONTAL_UNITDECREMENT = "HorizontalUnitDecrement";
    static final String HORIZONTAL_UNITINCREMENT = "HorizontalUnitIncrement";
    static final String VERTICAL_UNITDECREMENT = "VerticalUnitDecrement";
    static final String VERTICAL_UNITINCREMENT = "VerticalUnitIncrement";
    static final String VERTICAL_PAGEDECREMENT = "VerticalPageDecrement";
    static final String VERTICAL_PAGEINCREMENT = "VerticalPageIncrement";
    static final String VERTICAL_HOME = "VerticalHome";
    static final String VERTICAL_END = "VerticalEnd";
    protected static final List<KeyBinding> SCROLL_PANE_BINDINGS = new ArrayList<KeyBinding>();

    public ScrollPaneBehavior(ScrollPane scrollPane) {
        super(scrollPane, SCROLL_PANE_BINDINGS);
    }

    public void horizontalUnitIncrement() {
        ((ScrollPaneSkin)((ScrollPane)this.getControl()).getSkin()).hsbIncrement();
    }

    public void horizontalUnitDecrement() {
        ((ScrollPaneSkin)((ScrollPane)this.getControl()).getSkin()).hsbDecrement();
    }

    public void verticalUnitIncrement() {
        ((ScrollPaneSkin)((ScrollPane)this.getControl()).getSkin()).vsbIncrement();
    }

    void verticalUnitDecrement() {
        ((ScrollPaneSkin)((ScrollPane)this.getControl()).getSkin()).vsbDecrement();
    }

    void horizontalPageIncrement() {
        ((ScrollPaneSkin)((ScrollPane)this.getControl()).getSkin()).hsbPageIncrement();
    }

    void horizontalPageDecrement() {
        ((ScrollPaneSkin)((ScrollPane)this.getControl()).getSkin()).hsbPageDecrement();
    }

    void verticalPageIncrement() {
        ((ScrollPaneSkin)((ScrollPane)this.getControl()).getSkin()).vsbPageIncrement();
    }

    void verticalPageDecrement() {
        ((ScrollPaneSkin)((ScrollPane)this.getControl()).getSkin()).vsbPageDecrement();
    }

    void verticalHome() {
        ScrollPane scrollPane = (ScrollPane)this.getControl();
        scrollPane.setHvalue(scrollPane.getHmin());
        scrollPane.setVvalue(scrollPane.getVmin());
    }

    void verticalEnd() {
        ScrollPane scrollPane = (ScrollPane)this.getControl();
        scrollPane.setHvalue(scrollPane.getHmax());
        scrollPane.setVvalue(scrollPane.getVmax());
    }

    public void contentDragged(double d, double d2) {
        ScrollPane scrollPane = (ScrollPane)this.getControl();
        if (!scrollPane.isPannable()) {
            return;
        }
        if (d < 0.0 && scrollPane.getHvalue() != 0.0 || d > 0.0 && scrollPane.getHvalue() != scrollPane.getHmax()) {
            scrollPane.setHvalue(scrollPane.getHvalue() + d);
        }
        if (d2 < 0.0 && scrollPane.getVvalue() != 0.0 || d2 > 0.0 && scrollPane.getVvalue() != scrollPane.getVmax()) {
            scrollPane.setVvalue(scrollPane.getVvalue() + d2);
        }
    }

    @Override
    protected String matchActionForEvent(KeyEvent keyEvent) {
        String string = super.matchActionForEvent(keyEvent);
        if (string != null) {
            if (keyEvent.getCode() == KeyCode.LEFT) {
                if (((ScrollPane)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                    string = HORIZONTAL_UNITINCREMENT;
                }
            } else if (keyEvent.getCode() == KeyCode.RIGHT && ((ScrollPane)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                string = HORIZONTAL_UNITDECREMENT;
            }
        }
        return string;
    }

    @Override
    protected void callAction(String string) {
        switch (string) {
            case "HorizontalUnitDecrement": {
                this.horizontalUnitDecrement();
                break;
            }
            case "HorizontalUnitIncrement": {
                this.horizontalUnitIncrement();
                break;
            }
            case "VerticalUnitDecrement": {
                this.verticalUnitDecrement();
                break;
            }
            case "VerticalUnitIncrement": {
                this.verticalUnitIncrement();
                break;
            }
            case "VerticalPageDecrement": {
                this.verticalPageDecrement();
                break;
            }
            case "VerticalPageIncrement": {
                this.verticalPageIncrement();
                break;
            }
            case "VerticalHome": {
                this.verticalHome();
                break;
            }
            case "VerticalEnd": {
                this.verticalEnd();
                break;
            }
            default: {
                super.callAction(string);
            }
        }
    }

    public void mouseClicked() {
        ((ScrollPane)this.getControl()).requestFocus();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        super.mousePressed(mouseEvent);
        ((ScrollPane)this.getControl()).requestFocus();
    }

    static {
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.F4, TRAVERSE_DEBUG).alt().ctrl().shift());
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.LEFT, HORIZONTAL_UNITDECREMENT));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, HORIZONTAL_UNITINCREMENT));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.UP, VERTICAL_UNITDECREMENT));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.DOWN, VERTICAL_UNITINCREMENT));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, VERTICAL_PAGEDECREMENT));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, VERTICAL_PAGEINCREMENT));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.SPACE, VERTICAL_PAGEINCREMENT));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.HOME, VERTICAL_HOME));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.END, VERTICAL_END));
    }
}

