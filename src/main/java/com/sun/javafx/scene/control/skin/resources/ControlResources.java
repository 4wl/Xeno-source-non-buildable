/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin.resources;

import java.util.ResourceBundle;

public final class ControlResources {
    private static final String BASE_NAME = "com/sun/javafx/scene/control/skin/resources/controls";
    private static final String NT_BASE_NAME = "com/sun/javafx/scene/control/skin/resources/controls-nt";

    private ControlResources() {
    }

    public static String getString(String string) {
        return ResourceBundle.getBundle(BASE_NAME).getString(string);
    }

    public static String getNonTranslatableString(String string) {
        return ResourceBundle.getBundle(NT_BASE_NAME).getString(string);
    }
}

