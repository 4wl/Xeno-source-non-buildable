/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.control.DatePicker
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.DatePicker;

public class DatePickerBehavior
extends ComboBoxBaseBehavior<LocalDate> {
    protected static final List<KeyBinding> DATE_PICKER_BINDINGS = new ArrayList<KeyBinding>();

    public DatePickerBehavior(DatePicker datePicker) {
        super(datePicker, DATE_PICKER_BINDINGS);
    }

    @Override
    public void onAutoHide() {
        DatePicker datePicker = (DatePicker)this.getControl();
        DatePickerSkin datePickerSkin = (DatePickerSkin)datePicker.getSkin();
        datePickerSkin.syncWithAutoUpdate();
        if (!datePicker.isShowing()) {
            super.onAutoHide();
        }
    }

    static {
        DATE_PICKER_BINDINGS.addAll(COMBO_BOX_BASE_BINDINGS);
    }
}

