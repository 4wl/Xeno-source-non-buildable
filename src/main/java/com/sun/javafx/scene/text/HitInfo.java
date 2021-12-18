/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.text;

public class HitInfo {
    private int charIndex;
    private boolean leading;

    public int getCharIndex() {
        return this.charIndex;
    }

    public void setCharIndex(int n) {
        this.charIndex = n;
    }

    public boolean isLeading() {
        return this.leading;
    }

    public void setLeading(boolean bl) {
        this.leading = bl;
    }

    public int getInsertionIndex() {
        return this.leading ? this.charIndex : this.charIndex + 1;
    }

    public String toString() {
        return "charIndex: " + this.charIndex + ", isLeading: " + this.leading;
    }
}

