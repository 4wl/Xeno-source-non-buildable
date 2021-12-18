/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ObservableValue
 *  javafx.scene.control.Label
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.LabeledSkinBase;
import java.util.Collections;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;

public class LabelSkin
extends LabeledSkinBase<Label, BehaviorBase<Label>> {
    public LabelSkin(Label label) {
        super(label, new BehaviorBase<Label>(label, Collections.emptyList()));
        this.consumeMouseEvents(false);
        this.registerChangeListener((ObservableValue<?>)label.labelForProperty(), "LABEL_FOR");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("LABEL_FOR".equals(string)) {
            this.mnemonicTargetChanged();
        }
    }
}

