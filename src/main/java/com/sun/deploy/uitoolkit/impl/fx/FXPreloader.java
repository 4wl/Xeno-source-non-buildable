/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.applet2.Applet2
 *  com.sun.applet2.Applet2Context
 *  com.sun.applet2.preloader.CancelException
 *  com.sun.applet2.preloader.Preloader
 *  com.sun.applet2.preloader.event.AppletInitEvent
 *  com.sun.applet2.preloader.event.ApplicationExitEvent
 *  com.sun.applet2.preloader.event.DownloadEvent
 *  com.sun.applet2.preloader.event.ErrorEvent
 *  com.sun.applet2.preloader.event.PreloaderEvent
 *  com.sun.applet2.preloader.event.UserDeclinedEvent
 *  com.sun.deploy.trace.Trace
 *  com.sun.deploy.uitoolkit.Applet2Adapter
 *  javafx.application.Application
 *  javafx.application.Preloader
 *  javafx.application.Preloader$ErrorNotification
 *  javafx.application.Preloader$PreloaderNotification
 *  javafx.application.Preloader$ProgressNotification
 *  javafx.application.Preloader$StateChangeNotification
 *  javafx.application.Preloader$StateChangeNotification$Type
 *  javafx.stage.Stage
 */
package com.sun.deploy.uitoolkit.impl.fx;

import com.sun.applet2.Applet2;
import com.sun.applet2.Applet2Context;
import com.sun.applet2.preloader.CancelException;
import com.sun.applet2.preloader.Preloader;
import com.sun.applet2.preloader.event.AppletInitEvent;
import com.sun.applet2.preloader.event.ApplicationExitEvent;
import com.sun.applet2.preloader.event.DownloadEvent;
import com.sun.applet2.preloader.event.ErrorEvent;
import com.sun.applet2.preloader.event.PreloaderEvent;
import com.sun.applet2.preloader.event.UserDeclinedEvent;
import com.sun.deploy.trace.Trace;
import com.sun.deploy.uitoolkit.Applet2Adapter;
import com.sun.deploy.uitoolkit.impl.fx.FXApplet2Adapter;
import com.sun.deploy.uitoolkit.impl.fx.FXPluginToolkit;
import com.sun.deploy.uitoolkit.impl.fx.FXWindow;
import com.sun.deploy.uitoolkit.impl.fx.Utils;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXDefaultPreloader;
import com.sun.javafx.applet.ExperimentalExtensions;
import com.sun.javafx.applet.FXApplet2;
import com.sun.javafx.application.ParametersImpl;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Map;
import java.util.concurrent.Callable;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.stage.Stage;

public class FXPreloader
extends Preloader {
    private static final Object lock = new Object();
    private static FXPreloader defaultPreloader = null;
    private javafx.application.Preloader fxPreview = null;
    private FXWindow window = null;
    private boolean seenFatalError = false;

    FXPreloader() {
        this.fxPreview = new FXDefaultPreloader();
    }

    FXPreloader(Applet2Context applet2Context, FXWindow fXWindow) {
        this.window = fXWindow;
        try {
            FXPluginToolkit.callAndWait(new Callable<Object>(){

                @Override
                public Object call() {
                    FXPreloader.this.fxPreview = new FXDefaultPreloader();
                    return null;
                }
            });
        }
        catch (RuntimeException runtimeException) {
            throw runtimeException;
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        String[] arrstring = Utils.getUnnamed(applet2Context);
        Map<String, String> map = Utils.getNamedParameters(applet2Context);
        ParametersImpl.registerParameters((Application)this.fxPreview, new ParametersImpl(map, arrstring));
    }

    FXPreloader(final Class<javafx.application.Preloader> class_, Applet2Context applet2Context, FXWindow fXWindow) throws InstantiationException, IllegalAccessException {
        this.window = fXWindow;
        if (!javafx.application.Preloader.class.isAssignableFrom(class_)) {
            throw new IllegalArgumentException("Unrecognized FX Preloader class");
        }
        try {
            FXPluginToolkit.callAndWait(new Callable<Object>(){

                @Override
                public Object call() throws InstantiationException, IllegalAccessException {
                    FXPreloader.this.fxPreview = (javafx.application.Preloader)class_.newInstance();
                    return null;
                }
            });
        }
        catch (InstantiationException instantiationException) {
            throw instantiationException;
        }
        catch (IllegalAccessException illegalAccessException) {
            throw illegalAccessException;
        }
        catch (RuntimeException runtimeException) {
            throw runtimeException;
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        String[] arrstring = Utils.getUnnamed(applet2Context);
        Map<String, String> map = Utils.getNamedParameters(applet2Context);
        ParametersImpl.registerParameters((Application)this.fxPreview, new ParametersImpl(map, arrstring));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static FXPreloader getDefaultPreloader() {
        Object object = lock;
        synchronized (object) {
            if (defaultPreloader != null) {
                defaultPreloader = new FXPreloader();
            }
        }
        return defaultPreloader;
    }

    public static void notfiyCurrentPreloaderOnExit() {
        Notifier.send((PreloaderEvent)new ApplicationExitEvent());
    }

    public static void notifyCurrentPreloaderOnError(ErrorEvent errorEvent) {
        Notifier.send((PreloaderEvent)errorEvent);
    }

    public static void notifyCurrentPreloader(Preloader.PreloaderNotification preloaderNotification) {
        Notifier.send(new UserEvent(preloaderNotification));
    }

    public Object getOwner() {
        return null;
    }

    public boolean handleEvent(PreloaderEvent preloaderEvent) throws CancelException {
        Object object;
        Boolean bl = false;
        if (preloaderEvent instanceof ErrorEvent) {
            object = (FXApplet2Adapter)FXApplet2Adapter.get();
            object.abortApplet();
        }
        object = new FXDispatcher(preloaderEvent);
        try {
            bl = (Boolean)FXPluginToolkit.callAndWait(object);
        }
        catch (CancelException cancelException) {
            throw cancelException;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return bl;
    }

    void start() throws Exception {
        this.fxPreview.init();
        FXPluginToolkit.callAndWait(new Callable<Object>(){

            @Override
            public Object call() throws Exception {
                Stage stage;
                if (FXPreloader.this.window == null) {
                    stage = new Stage();
                    stage.impl_setPrimary(true);
                } else {
                    stage = FXPreloader.this.window.getPreloaderStage();
                }
                FXPreloader.this.fxPreview.start(stage);
                if (!(FXPreloader.this.fxPreview instanceof FXDefaultPreloader)) {
                    FXPreloader.hideSplash();
                }
                return null;
            }
        });
    }

    public static void hideSplash() {
        ExperimentalExtensions experimentalExtensions = ExperimentalExtensions.get();
        if (experimentalExtensions != null) {
            experimentalExtensions.getSplash().hide();
        }
    }

    void stop() throws Exception {
        if (this.fxPreview != null) {
            this.fxPreview.stop();
        }
    }

    static {
        Class<Notifier> class_ = Notifier.class;
        Class<UserEvent> class_2 = UserEvent.class;
    }

    class FXDispatcher
    implements Callable<Boolean> {
        PreloaderEvent pe;

        FXDispatcher(PreloaderEvent preloaderEvent) {
            this.pe = preloaderEvent;
        }

        private void gotFatalError() {
            FXPreloader.this.seenFatalError = true;
            FXApplet2Adapter fXApplet2Adapter = (FXApplet2Adapter)FXApplet2Adapter.get();
            fXApplet2Adapter.setExitOnIdle(true);
        }

        @Override
        public Boolean call() throws Exception {
            if (FXPreloader.this.seenFatalError) {
                throw new CancelException("Cancel launch after fatal error");
            }
            switch (this.pe.getType()) {
                case 5: {
                    AppletInitEvent appletInitEvent = (AppletInitEvent)this.pe;
                    Application application = null;
                    Applet2 applet2 = appletInitEvent.getApplet();
                    if (applet2 != null && applet2 instanceof FXApplet2) {
                        application = ((FXApplet2)applet2).getApplication();
                    }
                    switch (appletInitEvent.getSubtype()) {
                        case 2: {
                            FXPreloader.this.fxPreview.handleStateChangeNotification(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_LOAD));
                            break;
                        }
                        case 3: {
                            FXPreloader.this.fxPreview.handleStateChangeNotification(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_INIT, application));
                            break;
                        }
                        case 4: {
                            FXPreloader.this.fxPreview.handleStateChangeNotification(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START, application));
                            break;
                        }
                    }
                    return true;
                }
                case 1: {
                    return true;
                }
                case 1000: {
                    Preloader.PreloaderNotification preloaderNotification = ((UserEvent)this.pe).get();
                    FXPreloader.this.fxPreview.handleApplicationNotification(preloaderNotification);
                    return true;
                }
                case 3: {
                    DownloadEvent downloadEvent = (DownloadEvent)this.pe;
                    double d = (double)downloadEvent.getOverallPercentage() / 100.0;
                    FXPreloader.this.fxPreview.handleProgressNotification(new Preloader.ProgressNotification(d));
                    return true;
                }
                case 6: {
                    ErrorEvent errorEvent = (ErrorEvent)this.pe;
                    String string = errorEvent.getLocation() != null ? errorEvent.getLocation().toString() : null;
                    Throwable throwable = errorEvent.getException();
                    String string2 = errorEvent.getValue();
                    if (string2 == null) {
                        string2 = throwable != null ? throwable.getMessage() : "unknown error";
                    }
                    this.gotFatalError();
                    return FXPreloader.this.fxPreview.handleErrorNotification(new Preloader.ErrorNotification(string, string2, throwable));
                }
                case 7: {
                    UserDeclinedEvent userDeclinedEvent = (UserDeclinedEvent)this.pe;
                    String string = userDeclinedEvent.getLocation();
                    return FXPreloader.this.fxPreview.handleErrorNotification((Preloader.ErrorNotification)new UserDeclinedNotification(string));
                }
            }
            return false;
        }
    }

    class UserDeclinedNotification
    extends Preloader.ErrorNotification {
        public UserDeclinedNotification(String string) {
            super(string, "User declined to grant permissions to the application.", null);
        }
    }

    static class Notifier
    implements PrivilegedExceptionAction<Void> {
        PreloaderEvent pe;

        Notifier(PreloaderEvent preloaderEvent) {
            this.pe = preloaderEvent;
        }

        static void send(PreloaderEvent preloaderEvent) {
            try {
                AccessController.doPrivileged(new Notifier(preloaderEvent));
            }
            catch (Exception exception) {
                Trace.ignoredException((Exception)exception);
            }
        }

        @Override
        public Void run() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, CancelException {
            Applet2Adapter applet2Adapter = FXApplet2Adapter.get();
            if (applet2Adapter != null) {
                Class<?> class_ = Class.forName("com.sun.javaws.progress.Progress");
                Method method = class_.getMethod("get", Applet2Adapter.class);
                Preloader preloader = (Preloader)method.invoke(null, new Object[]{applet2Adapter});
                preloader.handleEvent(this.pe);
            }
            return null;
        }
    }

    static class UserEvent
    extends PreloaderEvent {
        public static final int CUSTOM_USER_EVENT = 1000;
        Preloader.PreloaderNotification pe;

        UserEvent(Preloader.PreloaderNotification preloaderNotification) {
            super(1000);
            this.pe = preloaderNotification;
        }

        Preloader.PreloaderNotification get() {
            return this.pe;
        }
    }
}

