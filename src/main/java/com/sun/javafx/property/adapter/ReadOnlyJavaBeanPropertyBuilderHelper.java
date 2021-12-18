/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.property.adapter;

import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.lang.reflect.Method;
import sun.reflect.misc.ReflectUtil;

public class ReadOnlyJavaBeanPropertyBuilderHelper {
    private static final String IS_PREFIX = "is";
    private static final String GET_PREFIX = "get";
    private String propertyName;
    private Class<?> beanClass;
    private Object bean;
    private String getterName;
    private Method getter;
    private ReadOnlyPropertyDescriptor descriptor;

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
                this.beanClass = object.getClass();
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

    public void getter(Method method) {
        if (method == null ? this.getter != null : !method.equals(this.getter)) {
            this.getter = method;
            this.descriptor = null;
        }
    }

    public ReadOnlyPropertyDescriptor getDescriptor() throws NoSuchMethodException {
        if (this.descriptor == null) {
            if (this.propertyName == null || this.bean == null) {
                throw new NullPointerException("Bean and property name have to be specified");
            }
            if (this.propertyName.isEmpty()) {
                throw new IllegalArgumentException("Property name cannot be empty");
            }
            String string = ReadOnlyPropertyDescriptor.capitalizedName(this.propertyName);
            if (this.getter == null) {
                if (this.getterName != null && !this.getterName.isEmpty()) {
                    this.getter = this.beanClass.getMethod(this.getterName, new Class[0]);
                } else {
                    try {
                        this.getter = this.beanClass.getMethod(IS_PREFIX + string, new Class[0]);
                    }
                    catch (NoSuchMethodException noSuchMethodException) {
                        this.getter = this.beanClass.getMethod(GET_PREFIX + string, new Class[0]);
                    }
                }
            }
            this.descriptor = new ReadOnlyPropertyDescriptor(this.propertyName, this.beanClass, this.getter);
        }
        return this.descriptor;
    }
}

