/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.gtk;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.InvokeLaterDispatcher;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Robot;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.Size;
import com.sun.glass.ui.Timer;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.glass.ui.gtk.GtkChildWindow;
import com.sun.glass.ui.gtk.GtkCommonDialogs;
import com.sun.glass.ui.gtk.GtkCursor;
import com.sun.glass.ui.gtk.GtkPixels;
import com.sun.glass.ui.gtk.GtkRobot;
import com.sun.glass.ui.gtk.GtkTimer;
import com.sun.glass.ui.gtk.GtkView;
import com.sun.glass.ui.gtk.GtkWindow;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

final class GtkApplication
extends Application
implements InvokeLaterDispatcher.InvokeLaterSubmitter {
    public static int screen;
    public static long display;
    public static long visualID;
    private final InvokeLaterDispatcher invokeLaterDispatcher;
    private Object eventLoopExitEnterPassValue;

    GtkApplication() {
        if (!GtkApplication.isDisplayValid()) {
            throw new UnsupportedOperationException("Unable to open DISPLAY");
        }
        boolean bl = AccessController.doPrivileged(() -> Boolean.getBoolean("javafx.embed.isEventThread"));
        if (!bl) {
            this.invokeLaterDispatcher = new InvokeLaterDispatcher(this);
            this.invokeLaterDispatcher.start();
        } else {
            this.invokeLaterDispatcher = null;
        }
    }

    private static boolean isDisplayValid() {
        return GtkApplication._isDisplayValid();
    }

    private void initDisplay() {
        Map map = GtkApplication.getDeviceDetails();
        if (map != null) {
            Object v = map.get("XDisplay");
            if (v != null) {
                display = (Long)v;
            }
            if ((v = map.get("XVisualID")) != null) {
                visualID = (Long)v;
            }
            if ((v = map.get("XScreenID")) != null) {
                screen = (Integer)v;
            }
        }
    }

    private void init() {
        this.initDisplay();
        long l = 0L;
        Map map = GtkApplication.getDeviceDetails();
        if (map != null) {
            Long l2 = (Long)map.get("javafx.embed.eventProc");
            l = l2 == null ? 0L : l2;
        }
        boolean bl = AccessController.doPrivileged(() -> Boolean.getBoolean("sun.awt.disablegrab") || Boolean.getBoolean("glass.disableGrab"));
        this._init(l, bl);
    }

    @Override
    protected void runLoop(Runnable runnable) {
        boolean bl = AccessController.doPrivileged(() -> Boolean.getBoolean("javafx.embed.isEventThread"));
        if (bl) {
            this.init();
            GtkApplication.setEventThread(Thread.currentThread());
            runnable.run();
            return;
        }
        boolean bl2 = AccessController.doPrivileged(() -> Boolean.getBoolean("glass.noErrorTrap"));
        Thread thread = AccessController.doPrivileged(() -> new Thread(() -> {
            this.init();
            this._runLoop(runnable, bl2);
        }, "GtkNativeMainLoopThread"));
        GtkApplication.setEventThread(thread);
        thread.start();
    }

    @Override
    protected void finishTerminating() {
        Thread thread = GtkApplication.getEventThread();
        if (thread != null) {
            this._terminateLoop();
            GtkApplication.setEventThread(null);
        }
        super.finishTerminating();
    }

    @Override
    public boolean shouldUpdateWindow() {
        return true;
    }

    private static native boolean _isDisplayValid();

    private native void _terminateLoop();

    private native void _init(long var1, boolean var3);

    private native void _runLoop(Runnable var1, boolean var2);

    @Override
    protected void _invokeAndWait(Runnable runnable) {
        if (this.invokeLaterDispatcher != null) {
            this.invokeLaterDispatcher.invokeAndWait(runnable);
        } else {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            this.submitForLaterInvocation(() -> {
                if (runnable != null) {
                    runnable.run();
                }
                countDownLatch.countDown();
            });
            try {
                countDownLatch.await();
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
    }

    private native void _submitForLaterInvocation(Runnable var1);

    @Override
    public void submitForLaterInvocation(Runnable runnable) {
        this._submitForLaterInvocation(runnable);
    }

    @Override
    protected void _invokeLater(Runnable runnable) {
        if (this.invokeLaterDispatcher != null) {
            this.invokeLaterDispatcher.invokeLater(runnable);
        } else {
            this.submitForLaterInvocation(runnable);
        }
    }

    private native void enterNestedEventLoopImpl();

    private native void leaveNestedEventLoopImpl();

    @Override
    protected Object _enterNestedEventLoop() {
        if (this.invokeLaterDispatcher != null) {
            this.invokeLaterDispatcher.notifyEnteringNestedEventLoop();
        }
        try {
            this.enterNestedEventLoopImpl();
            Object object = this.eventLoopExitEnterPassValue;
            this.eventLoopExitEnterPassValue = null;
            Object object2 = object;
            return object2;
        }
        finally {
            if (this.invokeLaterDispatcher != null) {
                this.invokeLaterDispatcher.notifyLeftNestedEventLoop();
            }
        }
    }

    @Override
    protected void _leaveNestedEventLoop(Object object) {
        if (this.invokeLaterDispatcher != null) {
            this.invokeLaterDispatcher.notifyLeavingNestedEventLoop();
        }
        this.eventLoopExitEnterPassValue = object;
        this.leaveNestedEventLoopImpl();
    }

    @Override
    public Window createWindow(Window window, Screen screen, int n) {
        return new GtkWindow(window, screen, n);
    }

    @Override
    public Window createWindow(long l) {
        return new GtkChildWindow(l);
    }

    @Override
    public View createView() {
        return new GtkView();
    }

    @Override
    public Cursor createCursor(int n) {
        return new GtkCursor(n);
    }

    @Override
    public Cursor createCursor(int n, int n2, Pixels pixels) {
        return new GtkCursor(n, n2, pixels);
    }

    @Override
    protected void staticCursor_setVisible(boolean bl) {
    }

    @Override
    protected Size staticCursor_getBestSize(int n, int n2) {
        return GtkCursor._getBestSize(n, n2);
    }

    @Override
    public Pixels createPixels(int n, int n2, ByteBuffer byteBuffer) {
        return new GtkPixels(n, n2, byteBuffer);
    }

    @Override
    public Pixels createPixels(int n, int n2, IntBuffer intBuffer) {
        return new GtkPixels(n, n2, intBuffer);
    }

    @Override
    public Pixels createPixels(int n, int n2, IntBuffer intBuffer, float f) {
        return new GtkPixels(n, n2, intBuffer, f);
    }

    @Override
    protected int staticPixels_getNativeFormat() {
        return 1;
    }

    @Override
    public Robot createRobot() {
        return new GtkRobot();
    }

    @Override
    public Timer createTimer(Runnable runnable) {
        return new GtkTimer(runnable);
    }

    @Override
    protected native int staticTimer_getMinPeriod();

    @Override
    protected native int staticTimer_getMaxPeriod();

    @Override
    protected double staticScreen_getVideoRefreshPeriod() {
        return 0.0;
    }

    @Override
    protected native Screen[] staticScreen_getScreens();

    @Override
    protected CommonDialogs.FileChooserResult staticCommonDialogs_showFileChooser(Window window, String string, String string2, String string3, int n, boolean bl, CommonDialogs.ExtensionFilter[] arrextensionFilter, int n2) {
        return GtkCommonDialogs.showFileChooser(window, string, string2, string3, n, bl, arrextensionFilter, n2);
    }

    @Override
    protected File staticCommonDialogs_showFolderChooser(Window window, String string, String string2) {
        return GtkCommonDialogs.showFolderChooser(window, string, string2);
    }

    @Override
    protected native long staticView_getMultiClickTime();

    @Override
    protected native int staticView_getMultiClickMaxX();

    @Override
    protected native int staticView_getMultiClickMaxY();

    @Override
    protected boolean _supportsInputMethods() {
        return true;
    }

    @Override
    protected native boolean _supportsTransparentWindows();

    @Override
    protected boolean _supportsUnifiedWindows() {
        return false;
    }

    @Override
    protected native int _getKeyCodeForChar(char var1);

    static {
        AccessController.doPrivileged(() -> {
            Application.loadNativeLibrary();
            return null;
        });
        screen = -1;
        display = 0L;
        visualID = 0L;
    }
}

