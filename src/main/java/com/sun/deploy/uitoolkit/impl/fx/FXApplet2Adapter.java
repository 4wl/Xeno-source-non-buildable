/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.applet2.Applet2
 *  com.sun.applet2.Applet2Context
 *  com.sun.applet2.preloader.Preloader
 *  com.sun.deploy.uitoolkit.Applet2Adapter
 *  com.sun.deploy.uitoolkit.Window
 *  javafx.application.Platform
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.stage.Stage
 */
package com.sun.deploy.uitoolkit.impl.fx;

import com.sun.applet2.Applet2;
import com.sun.applet2.Applet2Context;
import com.sun.applet2.preloader.Preloader;
import com.sun.deploy.uitoolkit.Applet2Adapter;
import com.sun.deploy.uitoolkit.Window;
import com.sun.deploy.uitoolkit.impl.fx.FXPreloader;
import com.sun.deploy.uitoolkit.impl.fx.FXWindow;
import com.sun.deploy.uitoolkit.impl.fx.ui.ErrorPane;
import com.sun.javafx.applet.ExperimentalExtensions;
import com.sun.javafx.applet.FXApplet2;
import com.sun.javafx.applet.HostServicesImpl;
import com.sun.javafx.application.PlatformImpl;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXApplet2Adapter
extends Applet2Adapter {
    FXApplet2 applet2 = null;
    FXPreloader preLoader = null;
    private PlatformImpl.FinishListener finishListener;
    private boolean exitOnIdle = false;
    private static boolean platformDestroyed = false;
    private static FXApplet2Adapter adapter = null;
    private FXWindow window = null;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    FXApplet2Adapter(Applet2Context applet2Context) {
        super(applet2Context);
        Class<FXApplet2Adapter> class_ = FXApplet2Adapter.class;
        synchronized (FXApplet2Adapter.class) {
            adapter = this;
            // ** MonitorExit[var2_2] (shouldn't be in output)
            ExperimentalExtensions.init(applet2Context);
            HostServicesImpl.init(applet2Context);
            this.installFinishListener();
            return;
        }
    }

    void abortApplet() {
        if (this.applet2 != null) {
            this.applet2.abortLaunch();
        }
    }

    synchronized void setExitOnIdle(boolean bl) {
        this.exitOnIdle = bl;
    }

    synchronized boolean isExitOnIdle() {
        return this.exitOnIdle;
    }

    static synchronized boolean isPlatformDestroyed() {
        return platformDestroyed;
    }

    private static synchronized void platformExit() {
        platformDestroyed = true;
        PlatformImpl.tkExit();
    }

    private void installFinishListener() {
        this.finishListener = new PlatformImpl.FinishListener(){

            private void destroyIfNeeded() {
                if (FXApplet2Adapter.this.window == null) {
                    try {
                        if (FXApplet2Adapter.this.applet2 != null) {
                            FXApplet2Adapter.this.applet2.stop();
                        }
                        if (FXApplet2Adapter.this.preLoader != null) {
                            FXApplet2Adapter.this.preLoader.stop();
                        }
                        if (FXApplet2Adapter.this.applet2 != null) {
                            FXApplet2Adapter.this.applet2.destroy();
                        }
                        FXApplet2Adapter.this.applet2 = null;
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    finally {
                        PlatformImpl.removeListener(FXApplet2Adapter.this.finishListener);
                        FXApplet2Adapter.platformExit();
                    }
                }
            }

            @Override
            public void idle(boolean bl) {
                if (bl && FXApplet2Adapter.this.isExitOnIdle()) {
                    this.destroyIfNeeded();
                }
            }

            @Override
            public void exitCalled() {
                this.destroyIfNeeded();
            }
        };
        PlatformImpl.addListener(this.finishListener);
    }

    public static FXApplet2Adapter getFXApplet2Adapter(Applet2Context applet2Context) {
        return new FXApplet2Adapter(applet2Context);
    }

    static synchronized Applet2Adapter get() {
        return adapter;
    }

    public void setParentContainer(Window window) {
        if (window instanceof FXWindow) {
            this.window = (FXWindow)window;
        }
    }

    public void instantiateApplet(Class class_) throws InstantiationException, IllegalAccessException {
        this.applet2 = new FXApplet2(class_, this.window);
    }

    public void instantiateSerialApplet(ClassLoader classLoader, String string) {
        throw new RuntimeException("Serial applets are not supported with FX toolkit");
    }

    public Object getLiveConnectObject() {
        return this.applet2 == null ? null : this.applet2.getApplication();
    }

    public Applet2 getApplet2() {
        return this.applet2;
    }

    public Preloader instantiatePreloader(Class class_) {
        try {
            this.preLoader = class_ != null ? new FXPreloader(class_, this.getApplet2Context(), this.window) : new FXPreloader(this.getApplet2Context(), this.window);
            this.preLoader.start();
        }
        catch (Exception exception) {
            this.preLoader = null;
            throw new RuntimeException(exception);
        }
        return this.preLoader;
    }

    public Preloader getPreloader() {
        return this.preLoader;
    }

    public void init() {
        if (this.applet2 != null) {
            this.applet2.init(this.getApplet2Context());
        }
    }

    public void start() {
        if (this.applet2 != null) {
            this.applet2.start();
        }
        this.setExitOnIdle(true);
    }

    public void stop() {
        if (this.applet2 != null) {
            this.applet2.stop();
        }
    }

    public void destroy() {
        if (this.applet2 != null) {
            this.applet2.destroy();
        }
    }

    public void abort() {
    }

    public void resize(int n, int n2) {
    }

    public void doShowApplet() {
    }

    public void doShowPreloader() {
    }

    public void doShowError(String string, final Throwable throwable, final boolean bl) {
        FXPreloader.hideSplash();
        Platform.runLater((Runnable)new Runnable(){

            @Override
            public void run() {
                Stage stage = FXApplet2Adapter.this.window != null ? FXApplet2Adapter.this.window.getErrorStage() : new Stage();
                ErrorPane errorPane = new ErrorPane(FXApplet2Adapter.this.getApplet2Context(), throwable, bl);
                stage.setScene(new Scene((Parent)errorPane));
                stage.show();
                stage.toFront();
            }
        });
    }

    public void doClearAppletArea() {
    }

    public boolean isInstantiated() {
        return this.applet2 != null;
    }
}

