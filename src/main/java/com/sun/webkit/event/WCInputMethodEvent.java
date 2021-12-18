/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.event;

import java.util.Arrays;

public final class WCInputMethodEvent {
    public static final int INPUT_METHOD_TEXT_CHANGED = 0;
    public static final int CARET_POSITION_CHANGED = 1;
    private final int id;
    private final String composed;
    private final String committed;
    private final int[] attributes;
    private final int caretPosition;

    public WCInputMethodEvent(String string, String string2, int[] arrn, int n) {
        this.id = 0;
        this.composed = string;
        this.committed = string2;
        this.attributes = Arrays.copyOf(arrn, arrn.length);
        this.caretPosition = n;
    }

    public WCInputMethodEvent(int n) {
        this.id = 1;
        this.composed = null;
        this.committed = null;
        this.attributes = null;
        this.caretPosition = n;
    }

    public int getID() {
        return this.id;
    }

    public String getComposed() {
        return this.composed;
    }

    public String getCommitted() {
        return this.committed;
    }

    public int[] getAttributes() {
        return Arrays.copyOf(this.attributes, this.attributes.length);
    }

    public int getCaretPosition() {
        return this.caretPosition;
    }
}

