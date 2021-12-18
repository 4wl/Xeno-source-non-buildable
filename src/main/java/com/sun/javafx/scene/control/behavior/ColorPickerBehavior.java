/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.EventType
 *  javafx.scene.control.ColorPicker
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.paint.Color
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.skin.ColorPickerSkin;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventType;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class ColorPickerBehavior
extends ComboBoxBaseBehavior<Color> {
    protected static final String OPEN_ACTION = "Open";
    protected static final String CLOSE_ACTION = "Close";
    protected static final List<KeyBinding> COLOR_PICKER_BINDINGS = new ArrayList<KeyBinding>();

    public ColorPickerBehavior(ColorPicker colorPicker) {
        super(colorPicker, COLOR_PICKER_BINDINGS);
    }

    @Override
    protected void callAction(String string) {
        if (OPEN_ACTION.equals(string)) {
            this.show();
        } else if (CLOSE_ACTION.equals(string)) {
            this.hide();
        } else {
            super.callAction(string);
        }
    }

    @Override
    public void onAutoHide() {
        ColorPicker colorPicker = (ColorPicker)this.getControl();
        ColorPickerSkin colorPickerSkin = (ColorPickerSkin)colorPicker.getSkin();
        colorPickerSkin.syncWithAutoUpdate();
        if (!colorPicker.isShowing()) {
            super.onAutoHide();
        }
    }

    static {
        COLOR_PICKER_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, CLOSE_ACTION));
        COLOR_PICKER_BINDINGS.add(new KeyBinding(KeyCode.SPACE, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, OPEN_ACTION));
        COLOR_PICKER_BINDINGS.add(new KeyBinding(KeyCode.ENTER, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, OPEN_ACTION));
    }
}

