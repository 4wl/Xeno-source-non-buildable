/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.KeyFrame
 *  javafx.animation.KeyValue
 *  javafx.animation.Timeline
 *  javafx.event.EventHandler
 *  javafx.event.EventType
 *  javafx.geometry.NodeOrientation
 *  javafx.geometry.Orientation
 *  javafx.scene.control.Control
 *  javafx.scene.control.ScrollBar
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.util.Duration
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.behavior.OrientedKeyBinding;
import com.sun.javafx.util.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class ScrollBarBehavior
extends BehaviorBase<ScrollBar> {
    protected static final List<KeyBinding> SCROLL_BAR_BINDINGS = new ArrayList<KeyBinding>();
    Timeline timeline;

    public ScrollBarBehavior(ScrollBar scrollBar) {
        super(scrollBar, SCROLL_BAR_BINDINGS);
    }

    void home() {
        ((ScrollBar)this.getControl()).setValue(((ScrollBar)this.getControl()).getMin());
    }

    void decrementValue() {
        ((ScrollBar)this.getControl()).adjustValue(0.0);
    }

    void end() {
        ((ScrollBar)this.getControl()).setValue(((ScrollBar)this.getControl()).getMax());
    }

    void incrementValue() {
        ((ScrollBar)this.getControl()).adjustValue(1.0);
    }

    @Override
    protected String matchActionForEvent(KeyEvent keyEvent) {
        String string = super.matchActionForEvent(keyEvent);
        if (string != null) {
            if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.KP_LEFT) {
                if (((ScrollBar)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                    string = ((ScrollBar)this.getControl()).getOrientation() == Orientation.HORIZONTAL ? "IncrementValue" : "DecrementValue";
                }
            } else if ((keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.KP_RIGHT) && ((ScrollBar)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                string = ((ScrollBar)this.getControl()).getOrientation() == Orientation.HORIZONTAL ? "DecrementValue" : "IncrementValue";
            }
        }
        return string;
    }

    @Override
    protected void callAction(String string) {
        if ("Home".equals(string)) {
            this.home();
        } else if ("End".equals(string)) {
            this.end();
        } else if ("IncrementValue".equals(string)) {
            this.incrementValue();
        } else if ("DecrementValue".equals(string)) {
            this.decrementValue();
        } else {
            super.callAction(string);
        }
        super.callAction(string);
    }

    public void trackPress(double d) {
        double d2;
        if (this.timeline != null) {
            return;
        }
        ScrollBar scrollBar = (ScrollBar)this.getControl();
        if (!scrollBar.isFocused() && scrollBar.isFocusTraversable()) {
            scrollBar.requestFocus();
        }
        boolean bl = (d2 = d) > (scrollBar.getValue() - scrollBar.getMin()) / (scrollBar.getMax() - scrollBar.getMin());
        this.timeline = new Timeline();
        this.timeline.setCycleCount(-1);
        EventHandler eventHandler = actionEvent -> {
            boolean bl2;
            boolean bl3 = bl2 = d2 > (scrollBar.getValue() - scrollBar.getMin()) / (scrollBar.getMax() - scrollBar.getMin());
            if (bl == bl2) {
                scrollBar.adjustValue(d2);
            } else {
                this.stopTimeline();
            }
        };
        KeyFrame keyFrame = new KeyFrame(Duration.millis((double)200.0), eventHandler, new KeyValue[0]);
        this.timeline.getKeyFrames().add((Object)keyFrame);
        this.timeline.play();
        eventHandler.handle(null);
    }

    public void trackRelease() {
        this.stopTimeline();
    }

    public void decButtonPressed() {
        ScrollBar scrollBar = (ScrollBar)this.getControl();
        if (!scrollBar.isFocused() && scrollBar.isFocusTraversable()) {
            scrollBar.requestFocus();
        }
        this.stopTimeline();
        this.timeline = new Timeline();
        this.timeline.setCycleCount(-1);
        EventHandler eventHandler = actionEvent -> {
            if (scrollBar.getValue() > scrollBar.getMin()) {
                scrollBar.decrement();
            } else {
                this.stopTimeline();
            }
        };
        KeyFrame keyFrame = new KeyFrame(Duration.millis((double)200.0), eventHandler, new KeyValue[0]);
        this.timeline.getKeyFrames().add((Object)keyFrame);
        this.timeline.play();
        eventHandler.handle(null);
    }

    public void decButtonReleased() {
        this.stopTimeline();
    }

    public void incButtonPressed() {
        ScrollBar scrollBar = (ScrollBar)this.getControl();
        if (!scrollBar.isFocused() && scrollBar.isFocusTraversable()) {
            scrollBar.requestFocus();
        }
        this.stopTimeline();
        this.timeline = new Timeline();
        this.timeline.setCycleCount(-1);
        EventHandler eventHandler = actionEvent -> {
            if (scrollBar.getValue() < scrollBar.getMax()) {
                scrollBar.increment();
            } else {
                this.stopTimeline();
            }
        };
        KeyFrame keyFrame = new KeyFrame(Duration.millis((double)200.0), eventHandler, new KeyValue[0]);
        this.timeline.getKeyFrames().add((Object)keyFrame);
        this.timeline.play();
        eventHandler.handle(null);
    }

    public void incButtonReleased() {
        this.stopTimeline();
    }

    public void thumbDragged(double d) {
        double d2;
        ScrollBar scrollBar = (ScrollBar)this.getControl();
        this.stopTimeline();
        if (!scrollBar.isFocused() && scrollBar.isFocusTraversable()) {
            scrollBar.requestFocus();
        }
        if (!Double.isNaN(d2 = d * (scrollBar.getMax() - scrollBar.getMin()) + scrollBar.getMin())) {
            scrollBar.setValue(Utils.clamp(scrollBar.getMin(), d2, scrollBar.getMax()));
        }
    }

    private void stopTimeline() {
        if (this.timeline != null) {
            this.timeline.stop();
            this.timeline = null;
        }
    }

    static {
        SCROLL_BAR_BINDINGS.add(new KeyBinding(KeyCode.F4, "TraverseDebug").alt().ctrl().shift());
        SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.LEFT, "DecrementValue"));
        SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.KP_LEFT, "DecrementValue"));
        SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.UP, "DecrementValue").vertical());
        SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.KP_UP, "DecrementValue").vertical());
        SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.RIGHT, "IncrementValue"));
        SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.KP_RIGHT, "IncrementValue"));
        SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.DOWN, "IncrementValue").vertical());
        SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.KP_DOWN, "IncrementValue").vertical());
        SCROLL_BAR_BINDINGS.add(new KeyBinding(KeyCode.HOME, (EventType<KeyEvent>)KeyEvent.KEY_RELEASED, "Home"));
        SCROLL_BAR_BINDINGS.add(new KeyBinding(KeyCode.END, (EventType<KeyEvent>)KeyEvent.KEY_RELEASED, "End"));
    }

    public static class ScrollBarKeyBinding
    extends OrientedKeyBinding {
        public ScrollBarKeyBinding(KeyCode keyCode, String string) {
            super(keyCode, string);
        }

        public ScrollBarKeyBinding(KeyCode keyCode, EventType<KeyEvent> eventType, String string) {
            super(keyCode, eventType, string);
        }

        @Override
        public boolean getVertical(Control control) {
            return ((ScrollBar)control).getOrientation() == Orientation.VERTICAL;
        }
    }
}

