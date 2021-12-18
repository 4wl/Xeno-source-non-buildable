/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.jmx;

import com.sun.javafx.util.Logging;

public abstract class MXExtension {
    private static final String EXTENSION_CLASS_NAME = System.getProperty("javafx.debug.jmx.class", "com.oracle.javafx.jmx.MXExtensionImpl");

    public abstract void intialize() throws Exception;

    public static void initializeIfAvailable() {
        try {
            ClassLoader classLoader = MXExtension.class.getClassLoader();
            Class<?> class_ = Class.forName(EXTENSION_CLASS_NAME, false, classLoader);
            if (!MXExtension.class.isAssignableFrom(class_)) {
                throw new IllegalArgumentException("Unrecognized MXExtension class: " + EXTENSION_CLASS_NAME);
            }
            MXExtension mXExtension = (MXExtension)class_.newInstance();
            mXExtension.intialize();
        }
        catch (Exception exception) {
            Logging.getJavaFXLogger().info("Failed to initialize management extension", exception);
        }
    }
}

