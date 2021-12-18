/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.adapter.ReadOnlyJavaBeanObjectProperty
 *  javafx.beans.property.adapter.ReadOnlyJavaBeanObjectPropertyBuilder
 */
package com.sun.javafx.property.adapter;

import javafx.beans.property.adapter.ReadOnlyJavaBeanObjectProperty;
import javafx.beans.property.adapter.ReadOnlyJavaBeanObjectPropertyBuilder;

public final class JavaBeanQuickAccessor {
    private JavaBeanQuickAccessor() {
    }

    public static <T> ReadOnlyJavaBeanObjectProperty<T> createReadOnlyJavaBeanObjectProperty(Object object, String string) throws NoSuchMethodException {
        return ReadOnlyJavaBeanObjectPropertyBuilder.create().bean(object).name(string).build();
    }
}

