/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.deploy.appcontext.AppContext
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.appcontext.AppContext;
import java.util.HashMap;

public class FXAppContext
implements AppContext {
    private HashMap storage = new HashMap();
    private static FXAppContext theInstance = new FXAppContext();

    private FXAppContext() {
    }

    public static synchronized FXAppContext getInstance() {
        return theInstance;
    }

    public Object get(Object object) {
        return this.storage.get(object);
    }

    public Object put(Object object, Object object2) {
        return this.storage.put(object, object2);
    }

    public Object remove(Object object) {
        return this.storage.remove(object);
    }

    public void invokeLater(Runnable runnable) {
        runnable.run();
    }

    public void invokeAndWait(Runnable runnable) {
        runnable.run();
    }

    public ThreadGroup getThreadGroup() {
        return Thread.currentThread().getThreadGroup();
    }

    public void dispose() {
        this.storage.clear();
    }

    public boolean destroy(long l) {
        return true;
    }
}

