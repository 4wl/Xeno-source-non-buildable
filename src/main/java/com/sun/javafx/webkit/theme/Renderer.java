/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.control.Control
 */
package com.sun.javafx.webkit.theme;

import com.sun.webkit.graphics.WCGraphicsContext;
import javafx.scene.control.Control;

public abstract class Renderer {
    private static Renderer instance;

    public static void setRenderer(Renderer renderer) {
        instance = renderer;
    }

    public static Renderer getRenderer() {
        return instance;
    }

    protected abstract void render(Control var1, WCGraphicsContext var2);
}

