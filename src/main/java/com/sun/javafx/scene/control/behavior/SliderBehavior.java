/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.EventType
 *  javafx.geometry.NodeOrientation
 *  javafx.geometry.Orientation
 *  javafx.scene.Node
 *  javafx.scene.control.Control
 *  javafx.scene.control.Slider
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseEvent
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.behavior.OrientedKeyBinding;
import com.sun.javafx.scene.control.behavior.TwoLevelFocusBehavior;
import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventType;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class SliderBehavior
extends BehaviorBase<Slider> {
    protected static final List<KeyBinding> SLIDER_BINDINGS = new ArrayList<KeyBinding>();
    private TwoLevelFocusBehavior tlFocus;

    @Override
    protected String matchActionForEvent(KeyEvent keyEvent) {
        String string = super.matchActionForEvent(keyEvent);
        if (string != null) {
            if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.KP_LEFT) {
                if (((Slider)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                    string = ((Slider)this.getControl()).getOrientation() == Orientation.HORIZONTAL ? "IncrementValue" : "DecrementValue";
                }
            } else if ((keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.KP_RIGHT) && ((Slider)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                string = ((Slider)this.getControl()).getOrientation() == Orientation.HORIZONTAL ? "DecrementValue" : "IncrementValue";
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
    }

    public SliderBehavior(Slider slider) {
        super(slider, SLIDER_BINDINGS);
        if (Utils.isTwoLevelFocus()) {
            this.tlFocus = new TwoLevelFocusBehavior((Node)slider);
        }
    }

    @Override
    public void dispose() {
        if (this.tlFocus != null) {
            this.tlFocus.dispose();
        }
        super.dispose();
    }

    public void trackPress(MouseEvent mouseEvent, double d) {
        Slider slider = (Slider)this.getControl();
        if (!slider.isFocused()) {
            slider.requestFocus();
        }
        if (slider.getOrientation().equals((Object)Orientation.HORIZONTAL)) {
            slider.adjustValue(d * (slider.getMax() - slider.getMin()) + slider.getMin());
        } else {
            slider.adjustValue((1.0 - d) * (slider.getMax() - slider.getMin()) + slider.getMin());
        }
    }

    public void thumbPressed(MouseEvent mouseEvent, double d) {
        Slider slider = (Slider)this.getControl();
        if (!slider.isFocused()) {
            slider.requestFocus();
        }
        slider.setValueChanging(true);
    }

    public void thumbDragged(MouseEvent mouseEvent, double d) {
        Slider slider = (Slider)this.getControl();
        slider.setValue(com.sun.javafx.util.Utils.clamp(slider.getMin(), d * (slider.getMax() - slider.getMin()) + slider.getMin(), slider.getMax()));
    }

    public void thumbReleased(MouseEvent mouseEvent) {
        Slider slider = (Slider)this.getControl();
        slider.setValueChanging(false);
        slider.adjustValue(slider.getValue());
    }

    void home() {
        Slider slider = (Slider)this.getControl();
        slider.adjustValue(slider.getMin());
    }

    void decrementValue() {
        Slider slider = (Slider)this.getControl();
        if (slider.isSnapToTicks()) {
            slider.adjustValue(slider.getValue() - this.computeIncrement());
        } else {
            slider.decrement();
        }
    }

    void end() {
        Slider slider = (Slider)this.getControl();
        slider.adjustValue(slider.getMax());
    }

    void incrementValue() {
        Slider slider = (Slider)this.getControl();
        if (slider.isSnapToTicks()) {
            slider.adjustValue(slider.getValue() + this.computeIncrement());
        } else {
            slider.increment();
        }
    }

    double computeIncrement() {
        Slider slider = (Slider)this.getControl();
        double d = 0.0;
        d = slider.getMinorTickCount() != 0 ? slider.getMajorTickUnit() / (double)(Math.max(slider.getMinorTickCount(), 0) + 1) : slider.getMajorTickUnit();
        if (slider.getBlockIncrement() > 0.0 && slider.getBlockIncrement() < d) {
            return d;
        }
        return slider.getBlockIncrement();
    }

    static {
        SLIDER_BINDINGS.add(new KeyBinding(KeyCode.F4, "TraverseDebug").alt().ctrl().shift());
        SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.LEFT, "DecrementValue"));
        SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.KP_LEFT, "DecrementValue"));
        SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.UP, "IncrementValue").vertical());
        SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.KP_UP, "IncrementValue").vertical());
        SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.RIGHT, "IncrementValue"));
        SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.KP_RIGHT, "IncrementValue"));
        SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.DOWN, "DecrementValue").vertical());
        SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.KP_DOWN, "DecrementValue").vertical());
        SLIDER_BINDINGS.add(new KeyBinding(KeyCode.HOME, (EventType<KeyEvent>)KeyEvent.KEY_RELEASED, "Home"));
        SLIDER_BINDINGS.add(new KeyBinding(KeyCode.END, (EventType<KeyEvent>)KeyEvent.KEY_RELEASED, "End"));
    }

    public static class SliderKeyBinding
    extends OrientedKeyBinding {
        public SliderKeyBinding(KeyCode keyCode, String string) {
            super(keyCode, string);
        }

        public SliderKeyBinding(KeyCode keyCode, EventType<KeyEvent> eventType, String string) {
            super(keyCode, eventType, string);
        }

        @Override
        public boolean getVertical(Control control) {
            return ((Slider)control).getOrientation() == Orientation.VERTICAL;
        }
    }
}

