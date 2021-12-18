/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Application
 */
package com.sun.javafx.application;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import javafx.application.Application;
import netscape.javascript.JSObject;

public abstract class HostServicesDelegate {
    private static Method getInstanceMeth = null;

    public static HostServicesDelegate getInstance(Application application) {
        HostServicesDelegate hostServicesDelegate = null;
        try {
            hostServicesDelegate = AccessController.doPrivileged(() -> {
                if (getInstanceMeth == null) {
                    try {
                        String string = "com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory";
                        Class<?> class_ = Class.forName("com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory", true, HostServicesDelegate.class.getClassLoader());
                        getInstanceMeth = class_.getMethod("getInstance", Application.class);
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                        return null;
                    }
                }
                return (HostServicesDelegate)getInstanceMeth.invoke(null, new Object[]{application});
            });
        }
        catch (PrivilegedActionException privilegedActionException) {
            System.err.println(privilegedActionException.getException().toString());
            return null;
        }
        return hostServicesDelegate;
    }

    protected HostServicesDelegate() {
    }

    public abstract String getCodeBase();

    public abstract String getDocumentBase();

    public abstract void showDocument(String var1);

    public abstract JSObject getWebContext();
}

