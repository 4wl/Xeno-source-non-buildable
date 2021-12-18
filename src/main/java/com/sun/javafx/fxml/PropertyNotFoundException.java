/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.fxml;

public class PropertyNotFoundException
extends RuntimeException {
    private static final long serialVersionUID = 0L;

    public PropertyNotFoundException() {
    }

    public PropertyNotFoundException(String string) {
        super(string);
    }

    public PropertyNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public PropertyNotFoundException(String string, Throwable throwable) {
        super(string, throwable);
    }
}

