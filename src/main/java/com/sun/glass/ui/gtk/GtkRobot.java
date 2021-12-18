/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.gtk;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Robot;
import java.nio.IntBuffer;

final class GtkRobot
extends Robot {
    GtkRobot() {
    }

    @Override
    protected void _create() {
    }

    @Override
    protected void _destroy() {
    }

    @Override
    protected native void _keyPress(int var1);

    @Override
    protected native void _keyRelease(int var1);

    @Override
    protected native void _mouseMove(int var1, int var2);

    @Override
    protected native void _mousePress(int var1);

    @Override
    protected native void _mouseRelease(int var1);

    @Override
    protected native void _mouseWheel(int var1);

    @Override
    protected native int _getMouseX();

    @Override
    protected native int _getMouseY();

    @Override
    protected int _getPixelColor(int n, int n2) {
        int[] arrn = new int[1];
        this._getScreenCapture(n, n2, 1, 1, arrn);
        return arrn[0];
    }

    private native void _getScreenCapture(int var1, int var2, int var3, int var4, int[] var5);

    @Override
    protected Pixels _getScreenCapture(int n, int n2, int n3, int n4, boolean bl) {
        int[] arrn = new int[n3 * n4];
        this._getScreenCapture(n, n2, n3, n4, arrn);
        return Application.GetApplication().createPixels(n3, n4, IntBuffer.wrap(arrn));
    }
}

