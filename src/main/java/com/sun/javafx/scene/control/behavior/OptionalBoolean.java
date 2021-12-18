/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.behavior;

public enum OptionalBoolean {
    TRUE,
    FALSE,
    ANY;


    public boolean equals(boolean bl) {
        if (this == ANY) {
            return true;
        }
        if (bl && this == TRUE) {
            return true;
        }
        return !bl && this == FALSE;
    }
}

