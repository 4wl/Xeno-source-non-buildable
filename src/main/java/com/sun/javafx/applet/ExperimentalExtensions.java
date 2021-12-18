/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.applet2.Applet2Context
 *  com.sun.applet2.Applet2Host
 *  sun.plugin2.applet2.Plugin2Host
 */
package com.sun.javafx.applet;

import com.sun.applet2.Applet2Context;
import com.sun.applet2.Applet2Host;
import com.sun.javafx.applet.Splash;
import netscape.javascript.JSObject;
import sun.plugin2.applet2.Plugin2Host;

public class ExperimentalExtensions {
    Applet2Context a2c;
    private static ExperimentalExtensions instance = null;

    public static synchronized ExperimentalExtensions get() {
        return instance;
    }

    public static synchronized void init(Applet2Context applet2Context) {
        instance = new ExperimentalExtensions(applet2Context);
    }

    private ExperimentalExtensions(Applet2Context applet2Context) {
        this.a2c = applet2Context;
    }

    public JSObject getOneWayJSObject() {
        Applet2Host applet2Host = this.a2c.getHost();
        if (applet2Host instanceof Plugin2Host) {
            try {
                return ((Plugin2Host)applet2Host).getOneWayJSObject();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return null;
    }

    public Splash getSplash() {
        return new Splash(this.a2c);
    }
}

