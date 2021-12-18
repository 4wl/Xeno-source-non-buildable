/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.ReadOnlyObjectProperty
 */
package com.sun.javafx.property;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javafx.beans.property.ReadOnlyObjectProperty;

public final class JavaBeanAccessHelper {
    private static Method JAVA_BEAN_QUICK_ACCESSOR_CREATE_RO;
    private static boolean initialized;

    private JavaBeanAccessHelper() {
    }

    public static <T> ReadOnlyObjectProperty<T> createReadOnlyJavaBeanProperty(Object object, String string) throws NoSuchMethodException {
        JavaBeanAccessHelper.init();
        if (JAVA_BEAN_QUICK_ACCESSOR_CREATE_RO == null) {
            throw new UnsupportedOperationException("Java beans are not supported.");
        }
        try {
            return (ReadOnlyObjectProperty)JAVA_BEAN_QUICK_ACCESSOR_CREATE_RO.invoke(null, object, string);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new UnsupportedOperationException("Java beans are not supported.");
        }
        catch (InvocationTargetException invocationTargetException) {
            if (invocationTargetException.getCause() instanceof NoSuchMethodException) {
                throw (NoSuchMethodException)invocationTargetException.getCause();
            }
            throw new UnsupportedOperationException("Java beans are not supported.");
        }
    }

    private static void init() {
        if (!initialized) {
            try {
                Class<?> class_ = Class.forName("com.sun.javafx.property.adapter.JavaBeanQuickAccessor", true, JavaBeanAccessHelper.class.getClassLoader());
                JAVA_BEAN_QUICK_ACCESSOR_CREATE_RO = class_.getDeclaredMethod("createReadOnlyJavaBeanObjectProperty", Object.class, String.class);
            }
            catch (ClassNotFoundException classNotFoundException) {
            }
            catch (NoSuchMethodException noSuchMethodException) {
                // empty catch block
            }
            initialized = true;
        }
    }
}

