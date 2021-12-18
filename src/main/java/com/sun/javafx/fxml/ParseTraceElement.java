/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.fxml;

import java.net.URL;

public class ParseTraceElement {
    private URL location;
    private int lineNumber;

    public ParseTraceElement(URL uRL, int n) {
        this.location = uRL;
        this.lineNumber = n;
    }

    public URL getLocation() {
        return this.location;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public String toString() {
        return (this.location == null ? "?" : this.location.getPath()) + ": " + this.lineNumber;
    }
}

