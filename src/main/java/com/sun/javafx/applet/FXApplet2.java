/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.applet2.Applet2
 *  com.sun.applet2.Applet2Context
 *  com.sun.applet2.preloader.event.ErrorEvent
 *  javafx.application.Application
 *  javafx.application.Platform
 *  javafx.stage.Stage
 *  sun.misc.PerformanceLogger
 */
package com.sun.javafx.applet;

import com.sun.applet2.Applet2;
import com.sun.applet2.Applet2Context;
import com.sun.applet2.preloader.event.ErrorEvent;
import com.sun.deploy.uitoolkit.impl.fx.FXPluginToolkit;
import com.sun.deploy.uitoolkit.impl.fx.FXPreloader;
import com.sun.deploy.uitoolkit.impl.fx.FXWindow;
import com.sun.deploy.uitoolkit.impl.fx.Utils;
import com.sun.javafx.application.ParametersImpl;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.perf.PerformanceTracker;
import java.security.AllPermission;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import sun.misc.PerformanceLogger;

public class FXApplet2
implements Applet2 {
    static final int SANDBOXAPP_DESTROY_TIMEOUT = 200;
    static final String JAVFX_APPLICATION_PARAM = "javafx.application";
    private Application application;
    private Applet2Context a2c;
    private FXWindow window;
    private Class<? extends Application> applicationClass = null;
    private boolean isAborted = false;

    public FXApplet2(Class<? extends Application> class_, FXWindow fXWindow) {
        this.applicationClass = class_;
        this.window = fXWindow;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void init(Applet2Context applet2Context) {
        String[] arrstring = this;
        synchronized (this) {
            if (this.isAborted) {
                // ** MonitorExit[var2_2] (shouldn't be in output)
                return;
            }
            // ** MonitorExit[var2_2] (shouldn't be in output)
            try {
                FXPluginToolkit.callAndWait(new Callable<Object>(){

                    @Override
                    public Object call() throws Exception {
                        FXApplet2.this.application = (Application)FXApplet2.this.applicationClass.newInstance();
                        return null;
                    }
                });
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
                FXPreloader.notifyCurrentPreloaderOnError(new ErrorEvent(null, "Failed to instantiate application.", throwable));
                if (throwable instanceof ClassCastException) {
                    throw new UnsupportedOperationException("In the JavaFX mode we only support applications extending JavaFX Application class.", throwable);
                }
                throw new RuntimeException(throwable);
            }
            this.a2c = applet2Context;
            arrstring = Utils.getUnnamed(this.a2c);
            Map<String, String> map = Utils.getNamedParameters(this.a2c);
            ParametersImpl.registerParameters(this.application, new ParametersImpl(map, arrstring));
            PlatformImpl.setApplicationName(this.applicationClass);
            try {
                this.application.init();
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
                FXPreloader.notifyCurrentPreloaderOnError(new ErrorEvent(null, "Failed to init application.", throwable));
                throw new RuntimeException(throwable);
            }
            return;
        }
    }

    public Application getApplication() {
        return this.application;
    }

    public synchronized void abortLaunch() {
        this.isAborted = true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void start() {
        FXApplet2 fXApplet2 = this;
        synchronized (fXApplet2) {
            if (this.isAborted) {
                return;
            }
        }
        Platform.runLater((Runnable)new Runnable(){

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            @Override
            public void run() {
                if (FXApplet2.this.application != null) {
                    Stage stage = FXApplet2.this.window == null ? new Stage() : FXApplet2.this.window.getAppletStage();
                    try {
                        String string;
                        FXApplet2.this.application.start(stage);
                        if (!PerformanceTracker.isLoggingEnabled() || (string = (String)FXApplet2.this.application.getParameters().getNamed().get("sun_perflog_fx_launchtime")) == null || string.equals("")) return;
                        long l = Long.parseLong(string);
                        PerformanceLogger.setStartTime((String)"LaunchTime", (long)l);
                        return;
                    }
                    catch (Throwable throwable) {
                        throwable.printStackTrace();
                        FXPreloader.notifyCurrentPreloaderOnError(new ErrorEvent(null, "Failed to start application.", throwable));
                        throw new RuntimeException(throwable);
                    }
                } else {
                    System.err.println("application is null?");
                }
            }
        });
    }

    public void stop() {
    }

    private boolean isSandboxApplication() {
        try {
            ProtectionDomain protectionDomain = this.applicationClass.getProtectionDomain();
            if (protectionDomain.getPermissions().implies(new AllPermission())) {
                return false;
            }
        }
        catch (SecurityException securityException) {
            securityException.printStackTrace();
        }
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void destroy() {
        FXApplet2 fXApplet2 = this;
        synchronized (fXApplet2) {
            if (this.isAborted) {
                return;
            }
        }
        PlatformImpl.runAndWait(new Runnable(){

            @Override
            public void run() {
                if (FXApplet2.this.isSandboxApplication()) {
                    Timer timer = new Timer("Exit timer", true);
                    TimerTask timerTask = new TimerTask(){

                        @Override
                        public void run() {
                            System.exit(0);
                        }
                    };
                    timer.schedule(timerTask, 200L);
                }
                try {
                    FXApplet2.this.application.stop();
                    FXPreloader.notfiyCurrentPreloaderOnExit();
                }
                catch (Throwable throwable) {
                    throwable.printStackTrace();
                    FXPreloader.notifyCurrentPreloaderOnError(new ErrorEvent(null, "Failed to stop application.", throwable));
                }
            }
        });
    }
}

