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

import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.behavior.OptionalBoolean;
import javafx.event.EventType;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public abstract class OrientedKeyBinding
extends KeyBinding {
    private OptionalBoolean vertical = OptionalBoolean.FALSE;

    public OrientedKeyBinding(KeyCode keyCode, String string) {
        super(keyCode, string);
    }

    public OrientedKeyBinding(KeyCode keyCode, EventType<KeyEvent> eventType, String string) {
        super(keyCode, eventType, string);
    }

    public OrientedKeyBinding vertical() {
        this.vertical = OptionalBoolean.TRUE;
        return this;
    }

    protected abstract boolean getVertical(Control var1);

    @Override
    public int getSpecificity(Control control, KeyEvent keyEvent) {
        boolean bl = this.getVertical(control);
        if (!this.vertical.equals(bl)) {
            return 0;
        }
        int n = super.getSpecificity(control, keyEvent);
        if (n == 0) {
            return 0;
        }
        return this.vertical != OptionalBoolean.ANY ? n + 1 : n;
    }

    @Override
    public String toString() {
        return "OrientedKeyBinding [code=" + (Object)this.getCode() + ", shift=" + (Object)((Object)this.getShift()) + ", ctrl=" + (Object)((Object)this.getCtrl()) + ", alt=" + (Object)((Object)this.getAlt()) + ", meta=" + (Object)((Object)this.getMeta()) + ", type=" + this.getType() + ", vertical=" + (Object)((Object)this.vertical) + ", action=" + this.getAction() + "]";
    }
}

