/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.applet2.Applet2Context
 *  com.sun.applet2.AppletParameters
 *  com.sun.deploy.trace.Trace
 *  com.sun.deploy.trace.TraceLevel
 */
package com.sun.javafx.applet;

import com.sun.applet2.Applet2Context;
import com.sun.applet2.AppletParameters;
import com.sun.deploy.trace.Trace;
import com.sun.deploy.trace.TraceLevel;
import com.sun.javafx.applet.ExperimentalExtensions;
import netscape.javascript.JSObject;

public class Splash {
    Applet2Context a2c = null;

    public Splash(Applet2Context applet2Context) {
        this.a2c = applet2Context;
    }

    public void hide() {
        Runnable runnable = new Runnable(){

            @Override
            public void run() {
                try {
                    JSObject jSObject = ExperimentalExtensions.get().getOneWayJSObject();
                    if (jSObject == null) {
                        Trace.println((String)"Can not hide splash as Javascript handle is not avaialble", (TraceLevel)TraceLevel.UI);
                        return;
                    }
                    AppletParameters appletParameters = null;
                    if (Splash.this.a2c != null) {
                        appletParameters = Splash.this.a2c.getParameters();
                    }
                    if (appletParameters != null && !"false".equals(appletParameters.get("javafx_splash"))) {
                        String string = (String)appletParameters.get("javafx_applet_id");
                        if (string != null) {
                            jSObject.eval("dtjava.hideSplash('" + string + "');");
                        } else {
                            Trace.println((String)"Missing applet id parameter!");
                        }
                    }
                }
                catch (Exception exception) {
                    Trace.ignored((Throwable)exception);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
    }
}

