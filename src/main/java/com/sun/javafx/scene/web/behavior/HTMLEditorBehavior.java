/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.input.KeyCode
 *  javafx.scene.web.HTMLEditor
 */
package com.sun.javafx.scene.web.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.web.skin.HTMLEditorSkin;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.KeyCode;
import javafx.scene.web.HTMLEditor;

public class HTMLEditorBehavior
extends BehaviorBase<HTMLEditor> {
    protected static final List<KeyBinding> HTML_EDITOR_BINDINGS = new ArrayList<KeyBinding>();

    public HTMLEditorBehavior(HTMLEditor hTMLEditor) {
        super(hTMLEditor, HTML_EDITOR_BINDINGS);
    }

    @Override
    protected void callAction(String string) {
        if ("bold".equals(string) || "italic".equals(string) || "underline".equals(string)) {
            HTMLEditor hTMLEditor = (HTMLEditor)this.getControl();
            HTMLEditorSkin hTMLEditorSkin = (HTMLEditorSkin)hTMLEditor.getSkin();
            hTMLEditorSkin.keyboardShortcuts(string);
        } else if ("F12".equals(string)) {
            ((HTMLEditor)this.getControl()).getImpl_traversalEngine().selectFirst().requestFocus();
        } else {
            super.callAction(string);
        }
    }

    static {
        HTML_EDITOR_BINDINGS.add(new KeyBinding(KeyCode.B, "bold").shortcut());
        HTML_EDITOR_BINDINGS.add(new KeyBinding(KeyCode.I, "italic").shortcut());
        HTML_EDITOR_BINDINGS.add(new KeyBinding(KeyCode.U, "underline").shortcut());
        HTML_EDITOR_BINDINGS.add(new KeyBinding(KeyCode.F12, "F12"));
        HTML_EDITOR_BINDINGS.add(new KeyBinding(KeyCode.TAB, "TraverseNext").ctrl());
        HTML_EDITOR_BINDINGS.add(new KeyBinding(KeyCode.TAB, "TraversePrevious").ctrl().shift());
    }
}

