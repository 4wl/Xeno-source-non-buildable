/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.effects.AudioSpectrum;

final class NativeAudioSpectrum
implements AudioSpectrum {
    private static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    public static final int DEFAULT_THRESHOLD = -60;
    public static final int DEFAULT_BANDS = 128;
    public static final double DEFAULT_INTERVAL = 0.1;
    private final long nativeRef;
    private float[] magnitudes = EMPTY_FLOAT_ARRAY;
    private float[] phases = EMPTY_FLOAT_ARRAY;

    NativeAudioSpectrum(long l) {
        if (l == 0L) {
            throw new IllegalArgumentException("Invalid native media reference");
        }
        this.nativeRef = l;
        this.setBandCount(128);
    }

    @Override
    public boolean getEnabled() {
        return this.nativeGetEnabled(this.nativeRef);
    }

    @Override
    public void setEnabled(boolean bl) {
        this.nativeSetEnabled(this.nativeRef, bl);
    }

    @Override
    public int getBandCount() {
        return this.phases.length;
    }

    @Override
    public void setBandCount(int n) {
        if (n > 1) {
            this.magnitudes = new float[n];
            for (int i = 0; i < this.magnitudes.length; ++i) {
                this.magnitudes[i] = -60.0f;
            }
        } else {
            this.magnitudes = EMPTY_FLOAT_ARRAY;
            this.phases = EMPTY_FLOAT_ARRAY;
            throw new IllegalArgumentException("Number of bands must at least be 2");
        }
        this.phases = new float[n];
        this.nativeSetBands(this.nativeRef, n, this.magnitudes, this.phases);
    }

    @Override
    public double getInterval() {
        return this.nativeGetInterval(this.nativeRef);
    }

    @Override
    public void setInterval(double d) {
        if (!(d * 1.0E9 >= 1.0)) {
            throw new IllegalArgumentException("Interval can't be less that 1 nanosecond");
        }
        this.nativeSetInterval(this.nativeRef, d);
    }

    @Override
    public int getSensitivityThreshold() {
        return this.nativeGetThreshold(this.nativeRef);
    }

    @Override
    public void setSensitivityThreshold(int n) {
        if (n > 0) {
            throw new IllegalArgumentException(String.format("Sensitivity threshold must be less than 0: %d", n));
        }
        this.nativeSetThreshold(this.nativeRef, n);
    }

    @Override
    public float[] getMagnitudes(float[] arrf) {
        int n = this.magnitudes.length;
        if (arrf == null || arrf.length < n) {
            arrf = new float[n];
        }
        System.arraycopy(this.magnitudes, 0, arrf, 0, n);
        return arrf;
    }

    @Override
    public float[] getPhases(float[] arrf) {
        int n = this.phases.length;
        if (arrf == null || arrf.length < n) {
            arrf = new float[n];
        }
        System.arraycopy(this.phases, 0, arrf, 0, n);
        return arrf;
    }

    private native boolean nativeGetEnabled(long var1);

    private native void nativeSetEnabled(long var1, boolean var3);

    private native void nativeSetBands(long var1, int var3, float[] var4, float[] var5);

    private native double nativeGetInterval(long var1);

    private native void nativeSetInterval(long var1, double var3);

    private native int nativeGetThreshold(long var1);

    private native void nativeSetThreshold(long var1, int var3);
}

