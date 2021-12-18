/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.Property
 *  javafx.beans.property.adapter.ReadOnlyJavaBeanProperty
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.ObservableValue
 */
package com.sun.javafx.property.adapter;

import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javafx.beans.property.Property;
import javafx.beans.property.adapter.ReadOnlyJavaBeanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import sun.reflect.misc.MethodUtil;

public class PropertyDescriptor
extends ReadOnlyPropertyDescriptor {
    private static final String ADD_VETOABLE_LISTENER_METHOD_NAME = "addVetoableChangeListener";
    private static final String REMOVE_VETOABLE_LISTENER_METHOD_NAME = "removeVetoableChangeListener";
    private static final String ADD_PREFIX = "add";
    private static final String REMOVE_PREFIX = "remove";
    private static final String SUFFIX = "Listener";
    private static final int ADD_VETOABLE_LISTENER_TAKES_NAME = 1;
    private static final int REMOVE_VETOABLE_LISTENER_TAKES_NAME = 2;
    private final Method setter;
    private final Method addVetoListener;
    private final Method removeVetoListener;
    private final int flags;

    public Method getSetter() {
        return this.setter;
    }

    public PropertyDescriptor(String string, Class<?> class_, Method method, Method method2) {
        super(string, class_, method);
        this.setter = method2;
        Method method3 = null;
        Method method4 = null;
        int n = 0;
        String string2 = ADD_PREFIX + PropertyDescriptor.capitalizedName(this.name) + SUFFIX;
        try {
            method3 = class_.getMethod(string2, VetoableChangeListener.class);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            try {
                method3 = class_.getMethod(ADD_VETOABLE_LISTENER_METHOD_NAME, String.class, VetoableChangeListener.class);
                n |= 1;
            }
            catch (NoSuchMethodException noSuchMethodException2) {
                try {
                    method3 = class_.getMethod(ADD_VETOABLE_LISTENER_METHOD_NAME, VetoableChangeListener.class);
                }
                catch (NoSuchMethodException noSuchMethodException3) {
                    // empty catch block
                }
            }
        }
        String string3 = REMOVE_PREFIX + PropertyDescriptor.capitalizedName(this.name) + SUFFIX;
        try {
            method4 = class_.getMethod(string3, VetoableChangeListener.class);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            try {
                method4 = class_.getMethod(REMOVE_VETOABLE_LISTENER_METHOD_NAME, String.class, VetoableChangeListener.class);
                n |= 2;
            }
            catch (NoSuchMethodException noSuchMethodException4) {
                try {
                    method4 = class_.getMethod(REMOVE_VETOABLE_LISTENER_METHOD_NAME, VetoableChangeListener.class);
                }
                catch (NoSuchMethodException noSuchMethodException5) {
                    // empty catch block
                }
            }
        }
        this.addVetoListener = method3;
        this.removeVetoListener = method4;
        this.flags = n;
    }

    @Override
    public void addListener(ReadOnlyPropertyDescriptor.ReadOnlyListener readOnlyListener) {
        super.addListener(readOnlyListener);
        if (this.addVetoListener != null) {
            try {
                if ((this.flags & 1) > 0) {
                    this.addVetoListener.invoke(readOnlyListener.getBean(), this.name, readOnlyListener);
                } else {
                    this.addVetoListener.invoke(readOnlyListener.getBean(), readOnlyListener);
                }
            }
            catch (IllegalAccessException illegalAccessException) {
            }
            catch (InvocationTargetException invocationTargetException) {
                // empty catch block
            }
        }
    }

    @Override
    public void removeListener(ReadOnlyPropertyDescriptor.ReadOnlyListener readOnlyListener) {
        super.removeListener(readOnlyListener);
        if (this.removeVetoListener != null) {
            try {
                if ((this.flags & 2) > 0) {
                    this.removeVetoListener.invoke(readOnlyListener.getBean(), this.name, readOnlyListener);
                } else {
                    this.removeVetoListener.invoke(readOnlyListener.getBean(), readOnlyListener);
                }
            }
            catch (IllegalAccessException illegalAccessException) {
            }
            catch (InvocationTargetException invocationTargetException) {
                // empty catch block
            }
        }
    }

    public class Listener<T>
    extends ReadOnlyPropertyDescriptor.ReadOnlyListener<T>
    implements ChangeListener<T>,
    VetoableChangeListener {
        private boolean updating;

        public Listener(Object object, ReadOnlyJavaBeanProperty<T> readOnlyJavaBeanProperty) {
            super(PropertyDescriptor.this, object, readOnlyJavaBeanProperty);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void changed(ObservableValue<? extends T> observableValue, T t, T t2) {
            ReadOnlyJavaBeanProperty readOnlyJavaBeanProperty = this.checkRef();
            if (readOnlyJavaBeanProperty == null) {
                observableValue.removeListener((ChangeListener)this);
            } else if (!this.updating) {
                this.updating = true;
                try {
                    MethodUtil.invoke(PropertyDescriptor.this.setter, this.bean, new Object[]{t2});
                    readOnlyJavaBeanProperty.fireValueChangedEvent();
                }
                catch (IllegalAccessException illegalAccessException) {
                }
                catch (InvocationTargetException invocationTargetException) {
                }
                finally {
                    this.updating = false;
                }
            }
        }

        @Override
        public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
            ReadOnlyJavaBeanProperty readOnlyJavaBeanProperty;
            if (this.bean.equals(propertyChangeEvent.getSource()) && PropertyDescriptor.this.name.equals(propertyChangeEvent.getPropertyName()) && (readOnlyJavaBeanProperty = this.checkRef()) instanceof Property && ((Property)readOnlyJavaBeanProperty).isBound() && !this.updating) {
                throw new PropertyVetoException("A bound value cannot be set.", propertyChangeEvent);
            }
        }
    }
}

