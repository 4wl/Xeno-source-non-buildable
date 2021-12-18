/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import java.security.AllPermission;
import java.security.Permission;

public abstract class Robot {
    private static final Permission allPermission = new AllPermission();
    public static final int MOUSE_LEFT_BTN = 1;
    public static final int MOUSE_RIGHT_BTN = 2;
    public static final int MOUSE_MIDDLE_BTN = 4;

    protected abstract void _create();

    protected Robot() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(allPermission);
        }
        Application.checkEventThread();
        this._create();
    }

    protected abstract void _destroy();

    public void destroy() {
        Application.checkEventThread();
        this._destroy();
    }

    protected abstract void _keyPress(int var1);

    public void keyPress(int n) {
        Application.checkEventThread();
        this._keyPress(n);
    }

    protected abstract void _keyRelease(int var1);

    public void keyRelease(int n) {
        Application.checkEventThread();
        this._keyRelease(n);
    }

    protected abstract void _mouseMove(int var1, int var2);

    public void mouseMove(int n, int n2) {
        Application.checkEventThread();
        this._mouseMove(n, n2);
    }

    protected abstract void _mousePress(int var1);

    public void mousePress(int n) {
        Application.checkEventThread();
        this._mousePress(n);
    }

    protected abstract void _mouseRelease(int var1);

    public void mouseRelease(int n) {
        Application.checkEventThread();
        this._mouseRelease(n);
    }

    protected abstract void _mouseWheel(int var1);

    public void mouseWheel(int n) {
        Application.checkEventThread();
        this._mouseWheel(n);
    }

    protected abstract int _getMouseX();

    public int getMouseX() {
        Application.checkEventThread();
        return this._getMouseX();
    }

    protected abstract int _getMouseY();

    public int getMouseY() {
        Application.checkEventThread();
        return this._getMouseY();
    }

    protected abstract int _getPixelColor(int var1, int var2);

    public int getPixelColor(int n, int n2) {
        Application.checkEventThread();
        return this._getPixelColor(n, n2);
    }

    protected abstract Pixels _getScreenCapture(int var1, int var2, int var3, int var4, boolean var5);

    public Pixels getScreenCapture(int n, int n2, int n3, int n4, boolean bl) {
        Application.checkEventThread();
        return this._getScreenCapture(n, n2, n3, n4, bl);
    }

    public Pixels getScreenCapture(int n, int n2, int n3, int n4) {
        return this.getScreenCapture(n, n2, n3, n4, false);
    }
}

