/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.effects.EqualizerBand;

final class NativeEqualizerBand
implements EqualizerBand {
    private final long bandRef;

    private NativeEqualizerBand(long l) {
        if (l == 0L) {
            throw new IllegalArgumentException("bandRef == 0");
        }
        this.bandRef = l;
    }

    @Override
    public double getCenterFrequency() {
        return this.nativeGetCenterFrequency(this.bandRef);
    }

    @Override
    public void setCenterFrequency(double d) {
        this.nativeSetCenterFrequency(this.bandRef, d);
    }

    @Override
    public double getBandwidth() {
        return this.nativeGetBandwidth(this.bandRef);
    }

    @Override
    public void setBandwidth(double d) {
        this.nativeSetBandwidth(this.bandRef, d);
    }

    @Override
    public double getGain() {
        return this.nativeGetGain(this.bandRef);
    }

    @Override
    public void setGain(double d) {
        if (d >= -24.0 && d <= 12.0) {
            this.nativeSetGain(this.bandRef, d);
        }
    }

    private native double nativeGetCenterFrequency(long var1);

    private native void nativeSetCenterFrequency(long var1, double var3);

    private native double nativeGetBandwidth(long var1);

    private native void nativeSetBandwidth(long var1, double var3);

    private native double nativeGetGain(long var1);

    private native void nativeSetGain(long var1, double var3);
}

