/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.ReadOnlyObjectProperty
 *  javafx.stage.Screen
 *  javafx.stage.Window
 */
package com.sun.javafx.stage;

import java.security.AccessControlContext;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.stage.Screen;
import javafx.stage.Window;

public final class WindowHelper {
    private static WindowAccessor windowAccessor;

    private WindowHelper() {
    }

    public static void notifyLocationChanged(Window window, double d, double d2) {
        windowAccessor.notifyLocationChanged(window, d, d2);
    }

    public static void notifySizeChanged(Window window, double d, double d2) {
        windowAccessor.notifySizeChanged(window, d, d2);
    }

    static AccessControlContext getAccessControlContext(Window window) {
        return windowAccessor.getAccessControlContext(window);
    }

    public static void setWindowAccessor(WindowAccessor windowAccessor) {
        if (WindowHelper.windowAccessor != null) {
            throw new IllegalStateException();
        }
        WindowHelper.windowAccessor = windowAccessor;
    }

    public static WindowAccessor getWindowAccessor() {
        return windowAccessor;
    }

    private static void forceInit(Class<?> class_) {
        try {
            Class.forName(class_.getName(), true, class_.getClassLoader());
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new AssertionError((Object)classNotFoundException);
        }
    }

    static {
        WindowHelper.forceInit(Window.class);
    }

    public static interface WindowAccessor {
        public void notifyLocationChanged(Window var1, double var2, double var4);

        public void notifySizeChanged(Window var1, double var2, double var4);

        public void notifyScreenChanged(Window var1, Object var2, Object var3);

        public float getUIScale(Window var1);

        public float getRenderScale(Window var1);

        public ReadOnlyObjectProperty<Screen> screenProperty(Window var1);

        public AccessControlContext getAccessControlContext(Window var1);
    }
}

