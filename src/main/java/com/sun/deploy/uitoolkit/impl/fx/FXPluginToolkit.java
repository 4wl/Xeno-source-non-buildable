/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.applet2.Applet2Context
 *  com.sun.applet2.preloader.Preloader
 *  com.sun.deploy.appcontext.AppContext
 *  com.sun.deploy.trace.Trace
 *  com.sun.deploy.uitoolkit.Applet2Adapter
 *  com.sun.deploy.uitoolkit.DragContext
 *  com.sun.deploy.uitoolkit.DragHelper
 *  com.sun.deploy.uitoolkit.DragListener
 *  com.sun.deploy.uitoolkit.PluginUIToolkit
 *  com.sun.deploy.uitoolkit.PluginWindowFactory
 *  com.sun.deploy.uitoolkit.UIToolkit
 *  com.sun.deploy.uitoolkit.Window
 *  com.sun.deploy.uitoolkit.ui.UIFactory
 *  com.sun.deploy.util.Waiter
 *  com.sun.deploy.util.Waiter$WaiterTask
 *  javafx.application.Platform
 *  sun.plugin2.applet.Plugin2Manager
 *  sun.plugin2.message.Pipe
 */
package com.sun.deploy.uitoolkit.impl.fx;

import com.sun.applet2.Applet2Context;
import com.sun.applet2.preloader.Preloader;
import com.sun.deploy.appcontext.AppContext;
import com.sun.deploy.trace.Trace;
import com.sun.deploy.uitoolkit.Applet2Adapter;
import com.sun.deploy.uitoolkit.DragContext;
import com.sun.deploy.uitoolkit.DragHelper;
import com.sun.deploy.uitoolkit.DragListener;
import com.sun.deploy.uitoolkit.PluginUIToolkit;
import com.sun.deploy.uitoolkit.PluginWindowFactory;
import com.sun.deploy.uitoolkit.UIToolkit;
import com.sun.deploy.uitoolkit.Window;
import com.sun.deploy.uitoolkit.impl.fx.FXApplet2Adapter;
import com.sun.deploy.uitoolkit.impl.fx.FXPreloader;
import com.sun.deploy.uitoolkit.impl.fx.FXWindowFactory;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXAppContext;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXUIFactory;
import com.sun.deploy.uitoolkit.ui.UIFactory;
import com.sun.deploy.util.Waiter;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.tk.Toolkit;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.Callable;
import javafx.application.Platform;
import sun.plugin2.applet.Plugin2Manager;
import sun.plugin2.message.Pipe;

public class FXPluginToolkit
extends PluginUIToolkit {
    static PluginWindowFactory windowFactory = null;
    private final Object initializedLock = new Object();
    private boolean initialized = false;
    private PlatformImpl.FinishListener finishListener = null;
    public static final DragHelper noOpDragHelper = new DragHelper(){

        public void register(DragContext dragContext, DragListener dragListener) {
        }

        public void makeDisconnected(DragContext dragContext, Window window) {
        }

        public void restore(DragContext dragContext) {
        }

        public void unregister(DragContext dragContext) {
        }
    };
    static FXUIFactory fxUIFactory = new FXUIFactory();

    public PluginWindowFactory getWindowFactory() {
        if (windowFactory == null) {
            this.doInitIfNeeded();
            windowFactory = new FXWindowFactory();
        }
        return windowFactory;
    }

    public Preloader getDefaultPreloader() {
        this.doInitIfNeeded();
        return FXPreloader.getDefaultPreloader();
    }

    public Applet2Adapter getApplet2Adapter(Applet2Context applet2Context) {
        this.doInitIfNeeded();
        FXApplet2Adapter fXApplet2Adapter = FXApplet2Adapter.getFXApplet2Adapter(applet2Context);
        this.uninstallFinishListener();
        return fXApplet2Adapter;
    }

    public void init() {
        Waiter.set((Waiter)new FxWaiter());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void doInitIfNeeded() {
        Object object = this.initializedLock;
        synchronized (object) {
            if (!this.initialized) {
                Boolean bl = (Boolean)AccessController.doPrivileged(new PrivilegedAction(){

                    public Object run() {
                        try {
                            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                            Class<?> class_ = Class.forName("com.sun.deploy.uitoolkit.ToolkitStore", false, classLoader);
                            Field field = class_.getDeclaredField("isPlugin");
                            field.setAccessible(true);
                            return field.getBoolean(class_);
                        }
                        catch (Throwable throwable) {
                            Trace.ignored((Throwable)throwable, (boolean)true);
                            return Boolean.FALSE;
                        }
                    }
                });
                boolean bl2 = bl;
                PlatformImpl.setTaskbarApplication(!bl2);
                this.installFinishListener();
                PlatformImpl.startup(new Runnable(){

                    @Override
                    public void run() {
                    }
                });
                this.initialized = true;
            }
        }
    }

    private void installFinishListener() {
        this.finishListener = new PlatformImpl.FinishListener(){

            @Override
            public void idle(boolean bl) {
            }

            @Override
            public void exitCalled() {
                FXPluginToolkit.this.uninstallFinishListener();
                PlatformImpl.tkExit();
            }
        };
        PlatformImpl.addListener(this.finishListener);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void uninstallFinishListener() {
        Object object = this.initializedLock;
        synchronized (object) {
            if (this.finishListener != null) {
                PlatformImpl.removeListener(this.finishListener);
                this.finishListener = null;
            }
        }
    }

    public boolean printApplet(Plugin2Manager plugin2Manager, int n, Pipe pipe, long l, boolean bl, int n2, int n3, int n4, int n5) {
        this.doInitIfNeeded();
        return false;
    }

    public DragHelper getDragHelper() {
        this.doInitIfNeeded();
        return noOpDragHelper;
    }

    public void dispose() throws Exception {
        PlatformImpl.tkExit();
    }

    public boolean isHeadless() {
        return false;
    }

    public void setContextClassLoader(final ClassLoader classLoader) {
        if (classLoader == null) {
            return;
        }
        Platform.runLater((Runnable)new Runnable(){

            @Override
            public void run() {
                Thread.currentThread().setContextClassLoader(classLoader);
            }
        });
    }

    public void warmup() {
        this.doInitIfNeeded();
    }

    public AppContext getAppContext() {
        return FXAppContext.getInstance();
    }

    public AppContext createAppContext() {
        return FXAppContext.getInstance();
    }

    public SecurityManager getSecurityManager() {
        SecurityManager securityManager = null;
        try {
            Class<?> class_ = Class.forName("sun.plugin2.applet.FXAppletSecurityManager", false, Thread.currentThread().getContextClassLoader());
            Constructor<?> constructor = class_.getDeclaredConstructor(new Class[0]);
            securityManager = (SecurityManager)constructor.newInstance(new Object[0]);
        }
        catch (Exception exception) {
            Trace.ignoredException((Exception)exception);
        }
        return securityManager;
    }

    public UIToolkit changeMode(int n) {
        return this;
    }

    public UIFactory getUIFactory() {
        this.doInitIfNeeded();
        return fxUIFactory;
    }

    public static <T> T callAndWait(Callable<T> callable) throws Exception {
        Caller<T> caller = new Caller<T>(callable);
        if (Platform.isFxApplicationThread()) {
            caller.run();
        } else {
            Platform.runLater(caller);
            if (FXApplet2Adapter.isPlatformDestroyed()) {
                return null;
            }
        }
        return caller.waitForReturn();
    }

    private static class Caller<T>
    implements Runnable {
        private T returnValue = null;
        Exception exception = null;
        private Callable<T> returnable;
        private Boolean returned = Boolean.FALSE;
        private final Object lock = new Object();

        Caller(Callable<T> callable) {
            this.returnable = callable;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            try {
                this.returnValue = this.returnable.call();
            }
            catch (Throwable throwable) {
                this.exception = throwable instanceof Exception ? (Exception)throwable : new RuntimeException("Problem in callAndWait()", throwable);
            }
            finally {
                Object object = this.lock;
                synchronized (object) {
                    this.returned = true;
                    this.lock.notifyAll();
                }
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        T waitForReturn() throws Exception {
            Object object = this.lock;
            synchronized (object) {
                while (!this.returned.booleanValue() && !FXApplet2Adapter.isPlatformDestroyed()) {
                    try {
                        this.lock.wait(500L);
                    }
                    catch (InterruptedException interruptedException) {
                        System.out.println("Interrupted wait" + interruptedException.getStackTrace());
                    }
                }
            }
            if (this.exception != null) {
                throw this.exception;
            }
            return this.returnValue;
        }
    }

    private static final class FxWaiter
    extends Waiter {
        private final Toolkit tk = Toolkit.getToolkit();

        FxWaiter() {
            Class<TaskThread> class_ = TaskThread.class;
        }

        public Object wait(Waiter.WaiterTask waiterTask) throws Exception {
            if (!this.tk.isFxUserThread()) {
                return waiterTask.run();
            }
            final Object object = new Object();
            Runnable runnable = new Runnable(){

                @Override
                public void run() {
                    FxWaiter.this.tk.exitNestedEventLoop(object, null);
                }
            };
            TaskThread taskThread = new TaskThread(waiterTask, runnable);
            taskThread.start();
            this.tk.enterNestedEventLoop(object);
            return taskThread.getResult();
        }
    }

    private static class TaskThread
    extends Thread {
        final Waiter.WaiterTask task;
        final Runnable cleanup;
        Object rv;

        TaskThread(Waiter.WaiterTask waiterTask, Runnable runnable) {
            this.task = waiterTask;
            this.cleanup = runnable;
        }

        public Object getResult() throws Exception {
            if (this.rv instanceof Exception) {
                throw (Exception)this.rv;
            }
            return this.rv;
        }

        @Override
        public void run() {
            try {
                this.rv = this.task.run();
            }
            catch (Exception exception) {
                this.rv = exception;
            }
            finally {
                Platform.runLater((Runnable)this.cleanup);
            }
        }
    }
}

