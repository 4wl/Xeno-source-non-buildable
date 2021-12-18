/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.applet2.Applet2Context
 *  com.sun.applet2.Applet2Host
 *  javafx.application.Application
 *  sun.plugin2.applet2.Plugin2Host
 */
package com.sun.javafx.applet;

import com.sun.applet2.Applet2Context;
import com.sun.applet2.Applet2Host;
import com.sun.javafx.application.HostServicesDelegate;
import java.net.URI;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javafx.application.Application;
import netscape.javascript.JSObject;
import sun.plugin2.applet2.Plugin2Host;

public class HostServicesImpl
extends HostServicesDelegate {
    private static HostServicesDelegate instance = null;
    private Applet2Context a2c = null;

    private HostServicesImpl(Applet2Context applet2Context) {
        this.a2c = applet2Context;
    }

    public static void init(Applet2Context applet2Context) {
        instance = new HostServicesImpl(applet2Context);
    }

    public static HostServicesDelegate getInstance(Application application) {
        return instance;
    }

    @Override
    public String getCodeBase() {
        return this.a2c.getCodeBase().toExternalForm();
    }

    @Override
    public String getDocumentBase() {
        return this.a2c.getHost().getDocumentBase().toExternalForm();
    }

    private URL toURL(String string) {
        try {
            return new URI(string).toURL();
        }
        catch (RuntimeException runtimeException) {
            throw runtimeException;
        }
        catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    @Override
    public void showDocument(String string) {
        this.a2c.getHost().showDocument(this.toURL(string), "_blank");
    }

    @Override
    public JSObject getWebContext() {
        try {
            return AccessController.doPrivileged(new WCGetter());
        }
        catch (PrivilegedActionException privilegedActionException) {
            privilegedActionException.printStackTrace();
            return null;
        }
    }

    class WCGetter
    implements PrivilegedExceptionAction<JSObject> {
        WCGetter() {
        }

        @Override
        public JSObject run() {
            Applet2Host applet2Host = HostServicesImpl.this.a2c.getHost();
            if (applet2Host instanceof Plugin2Host) {
                try {
                    return ((Plugin2Host)applet2Host).getJSObject();
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            return null;
        }
    }
}

