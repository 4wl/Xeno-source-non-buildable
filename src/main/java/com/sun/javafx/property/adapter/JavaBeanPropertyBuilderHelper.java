/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.property.adapter;

import com.sun.javafx.property.adapter.PropertyDescriptor;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.lang.reflect.Method;
import sun.reflect.misc.ReflectUtil;

public class JavaBeanPropertyBuilderHelper {
    private static final String IS_PREFIX = "is";
    private static final String GET_PREFIX = "get";
    private static final String SET_PREFIX = "set";
    private String propertyName;
    private Class<?> beanClass;
    private Object bean;
    private String getterName;
    private String setterName;
    private Method getter;
    private Method setter;
    private PropertyDescriptor descriptor;

    public void name(String string) {
        if (string == null ? this.propertyName != null : !string.equals(this.propertyName)) {
            this.propertyName = string;
            this.descriptor = null;
        }
    }

    public void beanClass(Class<?> class_) {
        if (class_ == null ? this.beanClass != null : !class_.equals(this.beanClass)) {
            ReflectUtil.checkPackageAccess(class_);
            this.beanClass = class_;
            this.descriptor = null;
        }
    }

    public void bean(Object object) {
        this.bean = object;
        if (object != null) {
            Class<?> class_ = object.getClass();
            if (this.beanClass == null || !this.beanClass.isAssignableFrom(class_)) {
                ReflectUtil.checkPackageAccess(class_);
                this.beanClass = class_;
                this.descriptor = null;
            }
        }
    }

    public Object getBean() {
        return this.bean;
    }

    public void getterName(String string) {
        if (string == null ? this.getterName != null : !string.equals(this.getterName)) {
            this.getterName = string;
            this.descriptor = null;
        }
    }

    public void setterName(String string) {
        if (string == null ? this.setterName != null : !string.equals(this.setterName)) {
            this.setterName = string;
            this.descriptor = null;
        }
    }

    public void getter(Method method) {
        if (method == null ? this.getter != null : !method.equals(this.getter)) {
            this.getter = method;
            this.descriptor = null;
        }
    }

    public void setter(Method method) {
        if (method == null ? this.setter != null : !method.equals(this.setter)) {
            this.setter = method;
            this.descriptor = null;
        }
    }

    public PropertyDescriptor getDescriptor() throws NoSuchMethodException {
        if (this.descriptor == null) {
            Method method;
            if (this.propertyName == null) {
                throw new NullPointerException("Property name has to be specified");
            }
            if (this.propertyName.isEmpty()) {
                throw new IllegalArgumentException("Property name cannot be empty");
            }
            String string = ReadOnlyPropertyDescriptor.capitalizedName(this.propertyName);
            Method method2 = this.getter;
            if (method2 == null) {
                if (this.getterName != null && !this.getterName.isEmpty()) {
                    method2 = this.beanClass.getMethod(this.getterName, new Class[0]);
                } else {
                    try {
                        method2 = this.beanClass.getMethod(IS_PREFIX + string, new Class[0]);
                    }
                    catch (NoSuchMethodException noSuchMethodException) {
                        method2 = this.beanClass.getMethod(GET_PREFIX + string, new Class[0]);
                    }
                }
            }
            if ((method = this.setter) == null) {
                Class<?> class_ = method2.getReturnType();
                method = this.setterName != null && !this.setterName.isEmpty() ? this.beanClass.getMethod(this.setterName, class_) : this.beanClass.getMethod(SET_PREFIX + string, class_);
            }
            this.descriptor = new PropertyDescriptor(this.propertyName, this.beanClass, method2, method);
        }
        return this.descriptor;
    }
}

