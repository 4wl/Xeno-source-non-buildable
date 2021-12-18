/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.WeakListener
 *  javafx.beans.property.adapter.ReadOnlyJavaBeanProperty
 */
package com.sun.javafx.property.adapter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import javafx.beans.WeakListener;
import javafx.beans.property.adapter.ReadOnlyJavaBeanProperty;
import sun.reflect.misc.ReflectUtil;

public class ReadOnlyPropertyDescriptor {
    private static final String ADD_LISTENER_METHOD_NAME = "addPropertyChangeListener";
    private static final String REMOVE_LISTENER_METHOD_NAME = "removePropertyChangeListener";
    private static final String ADD_PREFIX = "add";
    private static final String REMOVE_PREFIX = "remove";
    private static final String SUFFIX = "Listener";
    private static final int ADD_LISTENER_TAKES_NAME = 1;
    private static final int REMOVE_LISTENER_TAKES_NAME = 2;
    protected final String name;
    protected final Class<?> beanClass;
    private final Method getter;
    private final Class<?> type;
    private final Method addChangeListener;
    private final Method removeChangeListener;
    private final int flags;

    public String getName() {
        return this.name;
    }

    public Method getGetter() {
        return this.getter;
    }

    public Class<?> getType() {
        return this.type;
    }

    public ReadOnlyPropertyDescriptor(String string, Class<?> class_, Method method) {
        String string2;
        ReflectUtil.checkPackageAccess(class_);
        this.name = string;
        this.beanClass = class_;
        this.getter = method;
        this.type = method.getReturnType();
        Method method2 = null;
        Method method3 = null;
        int n = 0;
        try {
            string2 = ADD_PREFIX + ReadOnlyPropertyDescriptor.capitalizedName(this.name) + SUFFIX;
            method2 = class_.getMethod(string2, PropertyChangeListener.class);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            try {
                method2 = class_.getMethod(ADD_LISTENER_METHOD_NAME, String.class, PropertyChangeListener.class);
                n |= 1;
            }
            catch (NoSuchMethodException noSuchMethodException2) {
                try {
                    method2 = class_.getMethod(ADD_LISTENER_METHOD_NAME, PropertyChangeListener.class);
                }
                catch (NoSuchMethodException noSuchMethodException3) {
                    // empty catch block
                }
            }
        }
        try {
            string2 = REMOVE_PREFIX + ReadOnlyPropertyDescriptor.capitalizedName(this.name) + SUFFIX;
            method3 = class_.getMethod(string2, PropertyChangeListener.class);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            try {
                method3 = class_.getMethod(REMOVE_LISTENER_METHOD_NAME, String.class, PropertyChangeListener.class);
                n |= 2;
            }
            catch (NoSuchMethodException noSuchMethodException4) {
                try {
                    method3 = class_.getMethod(REMOVE_LISTENER_METHOD_NAME, PropertyChangeListener.class);
                }
                catch (NoSuchMethodException noSuchMethodException5) {
                    // empty catch block
                }
            }
        }
        this.addChangeListener = method2;
        this.removeChangeListener = method3;
        this.flags = n;
    }

    public static String capitalizedName(String string) {
        return string == null || string.length() == 0 ? string : string.substring(0, 1).toUpperCase(Locale.ENGLISH) + string.substring(1);
    }

    public void addListener(ReadOnlyListener readOnlyListener) {
        if (this.addChangeListener != null) {
            try {
                if ((this.flags & 1) > 0) {
                    this.addChangeListener.invoke(readOnlyListener.getBean(), this.name, readOnlyListener);
                } else {
                    this.addChangeListener.invoke(readOnlyListener.getBean(), readOnlyListener);
                }
            }
            catch (IllegalAccessException illegalAccessException) {
            }
            catch (InvocationTargetException invocationTargetException) {
                // empty catch block
            }
        }
    }

    public void removeListener(ReadOnlyListener readOnlyListener) {
        if (this.removeChangeListener != null) {
            try {
                if ((this.flags & 2) > 0) {
                    this.removeChangeListener.invoke(readOnlyListener.getBean(), this.name, readOnlyListener);
                } else {
                    this.removeChangeListener.invoke(readOnlyListener.getBean(), readOnlyListener);
                }
            }
            catch (IllegalAccessException illegalAccessException) {
            }
            catch (InvocationTargetException invocationTargetException) {
                // empty catch block
            }
        }
    }

    public class ReadOnlyListener<T>
    implements PropertyChangeListener,
    WeakListener {
        protected final Object bean;
        private final WeakReference<ReadOnlyJavaBeanProperty<T>> propertyRef;

        public Object getBean() {
            return this.bean;
        }

        public ReadOnlyListener(Object object, ReadOnlyJavaBeanProperty<T> readOnlyJavaBeanProperty) {
            this.bean = object;
            this.propertyRef = new WeakReference<ReadOnlyJavaBeanProperty<ReadOnlyJavaBeanProperty<T>>>(readOnlyJavaBeanProperty);
        }

        protected ReadOnlyJavaBeanProperty<T> checkRef() {
            ReadOnlyJavaBeanProperty readOnlyJavaBeanProperty = (ReadOnlyJavaBeanProperty)this.propertyRef.get();
            if (readOnlyJavaBeanProperty == null) {
                ReadOnlyPropertyDescriptor.this.removeListener(this);
            }
            return readOnlyJavaBeanProperty;
        }

        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            ReadOnlyJavaBeanProperty<T> readOnlyJavaBeanProperty;
            if (this.bean.equals(propertyChangeEvent.getSource()) && ReadOnlyPropertyDescriptor.this.name.equals(propertyChangeEvent.getPropertyName()) && (readOnlyJavaBeanProperty = this.checkRef()) != null) {
                readOnlyJavaBeanProperty.fireValueChangedEvent();
            }
        }

        public boolean wasGarbageCollected() {
            return this.checkRef() == null;
        }
    }
}

