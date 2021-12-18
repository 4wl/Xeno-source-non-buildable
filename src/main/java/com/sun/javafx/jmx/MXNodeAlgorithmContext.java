/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.jmx;

public class MXNodeAlgorithmContext {
    private int counter;

    public MXNodeAlgorithmContext() {
        this(0);
    }

    public MXNodeAlgorithmContext(int n) {
        this.counter = n;
    }

    public int getNextInt() {
        return ++this.counter;
    }
}

