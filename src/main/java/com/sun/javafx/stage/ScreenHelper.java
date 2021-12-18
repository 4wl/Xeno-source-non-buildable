/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.stage.Screen
 */
package com.sun.javafx.stage;

import javafx.stage.Screen;

public final class ScreenHelper {
    private static ScreenAccessor screenAccessor;

    public static void setScreenAccessor(ScreenAccessor screenAccessor) {
        if (ScreenHelper.screenAccessor != null) {
            throw new IllegalStateException();
        }
        ScreenHelper.screenAccessor = screenAccessor;
    }

    public static ScreenAccessor getScreenAccessor() {
        return screenAccessor;
    }

    public static interface ScreenAccessor {
        public float getRenderScale(Screen var1);
    }
}

