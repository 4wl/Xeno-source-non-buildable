/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.EventType
 *  javafx.scene.control.Control
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.OptionalBoolean;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import javafx.event.EventType;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyBinding {
    private KeyCode code;
    private EventType<KeyEvent> eventType = KeyEvent.KEY_PRESSED;
    private String action;
    private OptionalBoolean shift = OptionalBoolean.FALSE;
    private OptionalBoolean ctrl = OptionalBoolean.FALSE;
    private OptionalBoolean alt = OptionalBoolean.FALSE;
    private OptionalBoolean meta = OptionalBoolean.FALSE;

    public KeyBinding(KeyCode keyCode, String string) {
        this.code = keyCode;
        this.action = string;
    }

    public KeyBinding(KeyCode keyCode, EventType<KeyEvent> eventType, String string) {
        this.code = keyCode;
        this.eventType = eventType;
        this.action = string;
    }

    public KeyBinding shift() {
        return this.shift(OptionalBoolean.TRUE);
    }

    public KeyBinding shift(OptionalBoolean optionalBoolean) {
        this.shift = optionalBoolean;
        return this;
    }

    public KeyBinding ctrl() {
        return this.ctrl(OptionalBoolean.TRUE);
    }

    public KeyBinding ctrl(OptionalBoolean optionalBoolean) {
        this.ctrl = optionalBoolean;
        return this;
    }

    public KeyBinding alt() {
        return this.alt(OptionalBoolean.TRUE);
    }

    public KeyBinding alt(OptionalBoolean optionalBoolean) {
        this.alt = optionalBoolean;
        return this;
    }

    public KeyBinding meta() {
        return this.meta(OptionalBoolean.TRUE);
    }

    public KeyBinding meta(OptionalBoolean optionalBoolean) {
        this.meta = optionalBoolean;
        return this;
    }

    public KeyBinding shortcut() {
        if (Toolkit.getToolkit().getClass().getName().endsWith("StubToolkit")) {
            if (Utils.isMac()) {
                return this.meta();
            }
            return this.ctrl();
        }
        switch (Toolkit.getToolkit().getPlatformShortcutKey()) {
            case SHIFT: {
                return this.shift();
            }
            case CONTROL: {
                return this.ctrl();
            }
            case ALT: {
                return this.alt();
            }
            case META: {
                return this.meta();
            }
        }
        return this;
    }

    public final KeyCode getCode() {
        return this.code;
    }

    public final EventType<KeyEvent> getType() {
        return this.eventType;
    }

    public final String getAction() {
        return this.action;
    }

    public final OptionalBoolean getShift() {
        return this.shift;
    }

    public final OptionalBoolean getCtrl() {
        return this.ctrl;
    }

    public final OptionalBoolean getAlt() {
        return this.alt;
    }

    public final OptionalBoolean getMeta() {
        return this.meta;
    }

    public int getSpecificity(Control control, KeyEvent keyEvent) {
        int n = 0;
        if (this.code != null && this.code != keyEvent.getCode()) {
            return 0;
        }
        n = 1;
        if (!this.shift.equals(keyEvent.isShiftDown())) {
            return 0;
        }
        if (this.shift != OptionalBoolean.ANY) {
            ++n;
        }
        if (!this.ctrl.equals(keyEvent.isControlDown())) {
            return 0;
        }
        if (this.ctrl != OptionalBoolean.ANY) {
            ++n;
        }
        if (!this.alt.equals(keyEvent.isAltDown())) {
            return 0;
        }
        if (this.alt != OptionalBoolean.ANY) {
            ++n;
        }
        if (!this.meta.equals(keyEvent.isMetaDown())) {
            return 0;
        }
        if (this.meta != OptionalBoolean.ANY) {
            ++n;
        }
        if (this.eventType != null && this.eventType != keyEvent.getEventType()) {
            return 0;
        }
        return ++n;
    }

    public String toString() {
        return "KeyBinding [code=" + (Object)this.code + ", shift=" + (Object)((Object)this.shift) + ", ctrl=" + (Object)((Object)this.ctrl) + ", alt=" + (Object)((Object)this.alt) + ", meta=" + (Object)((Object)this.meta) + ", type=" + this.eventType + ", action=" + this.action + "]";
    }
}

