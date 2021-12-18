/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.ConditionalFeature
 *  javafx.event.EventType
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.behavior.OptionalBoolean;
import java.util.ArrayList;
import java.util.List;
import javafx.application.ConditionalFeature;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TextInputControlBindings {
    protected static final List<KeyBinding> BINDINGS = new ArrayList<KeyBinding>();

    static {
        BINDINGS.add(new KeyBinding(KeyCode.RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Right"));
        BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Right"));
        BINDINGS.add(new KeyBinding(KeyCode.LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Left"));
        BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Left"));
        BINDINGS.add(new KeyBinding(KeyCode.UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Home"));
        BINDINGS.add(new KeyBinding(KeyCode.KP_UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Home"));
        BINDINGS.add(new KeyBinding(KeyCode.HOME, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Home"));
        BINDINGS.add(new KeyBinding(KeyCode.DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "End"));
        BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "End"));
        BINDINGS.add(new KeyBinding(KeyCode.END, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "End"));
        BINDINGS.add(new KeyBinding(KeyCode.ENTER, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Fire"));
        BINDINGS.add(new KeyBinding(KeyCode.BACK_SPACE, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "DeletePreviousChar"));
        BINDINGS.add(new KeyBinding(KeyCode.DELETE, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "DeleteNextChar"));
        BINDINGS.add(new KeyBinding(KeyCode.CUT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Cut"));
        BINDINGS.add(new KeyBinding(KeyCode.DELETE, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Cut").shift());
        BINDINGS.add(new KeyBinding(KeyCode.COPY, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Copy"));
        BINDINGS.add(new KeyBinding(KeyCode.PASTE, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Paste"));
        BINDINGS.add(new KeyBinding(KeyCode.INSERT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Paste").shift());
        BINDINGS.add(new KeyBinding(KeyCode.RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectRight").shift());
        BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectRight").shift());
        BINDINGS.add(new KeyBinding(KeyCode.LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectLeft").shift());
        BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectLeft").shift());
        BINDINGS.add(new KeyBinding(KeyCode.UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectHome").shift());
        BINDINGS.add(new KeyBinding(KeyCode.KP_UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectHome").shift());
        BINDINGS.add(new KeyBinding(KeyCode.DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectEnd").shift());
        BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectEnd").shift());
        BINDINGS.add(new KeyBinding(KeyCode.BACK_SPACE, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "DeletePreviousChar").shift());
        BINDINGS.add(new KeyBinding(KeyCode.DELETE, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "DeleteNextChar").shift());
        if (PlatformUtil.isMac()) {
            BINDINGS.add(new KeyBinding(KeyCode.HOME, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectHomeExtend").shift());
            BINDINGS.add(new KeyBinding(KeyCode.END, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectEndExtend").shift());
            BINDINGS.add(new KeyBinding(KeyCode.HOME, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Home").shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.END, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "End").shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Home").shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Home").shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "End").shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "End").shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "LeftWord").alt());
            BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "LeftWord").alt());
            BINDINGS.add(new KeyBinding(KeyCode.RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "RightWord").alt());
            BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "RightWord").alt());
            BINDINGS.add(new KeyBinding(KeyCode.DELETE, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "DeleteNextWord").alt());
            BINDINGS.add(new KeyBinding(KeyCode.BACK_SPACE, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "DeletePreviousWord").alt());
            BINDINGS.add(new KeyBinding(KeyCode.BACK_SPACE, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "DeleteFromLineStart").shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.X, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Cut").shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.C, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Copy").shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.INSERT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Copy").shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.V, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Paste").shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.HOME, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectHome").shift().shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.END, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectEnd").shift().shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectHomeExtend").shift().shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectHomeExtend").shift().shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectEndExtend").shift().shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectEndExtend").shift().shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.A, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectAll").shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectLeftWord").shift().alt());
            BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectLeftWord").shift().alt());
            BINDINGS.add(new KeyBinding(KeyCode.RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectRightWord").shift().alt());
            BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectRightWord").shift().alt());
            BINDINGS.add(new KeyBinding(KeyCode.Z, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Undo").shortcut());
            BINDINGS.add(new KeyBinding(KeyCode.Z, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Redo").shift().shortcut());
        } else {
            BINDINGS.add(new KeyBinding(KeyCode.HOME, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectHome").shift());
            BINDINGS.add(new KeyBinding(KeyCode.END, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectEnd").shift());
            BINDINGS.add(new KeyBinding(KeyCode.HOME, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Home").ctrl());
            BINDINGS.add(new KeyBinding(KeyCode.END, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "End").ctrl());
            BINDINGS.add(new KeyBinding(KeyCode.LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "LeftWord").ctrl());
            BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "LeftWord").ctrl());
            BINDINGS.add(new KeyBinding(KeyCode.RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "RightWord").ctrl());
            BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "RightWord").ctrl());
            BINDINGS.add(new KeyBinding(KeyCode.H, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "DeletePreviousChar").ctrl());
            BINDINGS.add(new KeyBinding(KeyCode.DELETE, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "DeleteNextWord").ctrl());
            BINDINGS.add(new KeyBinding(KeyCode.BACK_SPACE, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "DeletePreviousWord").ctrl());
            BINDINGS.add(new KeyBinding(KeyCode.X, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Cut").ctrl());
            BINDINGS.add(new KeyBinding(KeyCode.C, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Copy").ctrl());
            BINDINGS.add(new KeyBinding(KeyCode.INSERT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Copy").ctrl());
            BINDINGS.add(new KeyBinding(KeyCode.V, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Paste").ctrl());
            BINDINGS.add(new KeyBinding(KeyCode.HOME, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectHome").ctrl().shift());
            BINDINGS.add(new KeyBinding(KeyCode.END, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectEnd").ctrl().shift());
            BINDINGS.add(new KeyBinding(KeyCode.LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectLeftWord").ctrl().shift());
            BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectLeftWord").ctrl().shift());
            BINDINGS.add(new KeyBinding(KeyCode.RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectRightWord").ctrl().shift());
            BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectRightWord").ctrl().shift());
            BINDINGS.add(new KeyBinding(KeyCode.A, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectAll").ctrl());
            BINDINGS.add(new KeyBinding(KeyCode.BACK_SLASH, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Unselect").ctrl());
            if (PlatformUtil.isLinux()) {
                BINDINGS.add(new KeyBinding(KeyCode.Z, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Undo").ctrl());
                BINDINGS.add(new KeyBinding(KeyCode.Z, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Redo").ctrl().shift());
            } else {
                BINDINGS.add(new KeyBinding(KeyCode.Z, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Undo").ctrl());
                BINDINGS.add(new KeyBinding(KeyCode.Y, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Redo").ctrl());
            }
        }
        BINDINGS.add(new KeyBinding(null, (EventType<KeyEvent>)KeyEvent.KEY_TYPED, "InputCharacter").alt(OptionalBoolean.ANY).shift(OptionalBoolean.ANY).ctrl(OptionalBoolean.ANY).meta(OptionalBoolean.ANY));
        BINDINGS.add(new KeyBinding(KeyCode.TAB, "TraverseNext"));
        BINDINGS.add(new KeyBinding(KeyCode.TAB, "TraversePrevious").shift());
        BINDINGS.add(new KeyBinding(KeyCode.TAB, "TraverseNext").ctrl());
        BINDINGS.add(new KeyBinding(KeyCode.TAB, "TraversePrevious").shift().ctrl());
        BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, "Cancel"));
        BINDINGS.add(new KeyBinding(KeyCode.F10, "ToParent"));
        if (PlatformImpl.isSupported(ConditionalFeature.VIRTUAL_KEYBOARD)) {
            BINDINGS.add(new KeyBinding(KeyCode.DIGIT9, "UseVK").ctrl().shift());
        }
    }
}

