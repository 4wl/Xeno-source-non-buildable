/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.EqualizerBand;

final class NativeAudioEqualizer
implements AudioEqualizer {
    private final long nativeRef;

    NativeAudioEqualizer(long l) {
        if (l == 0L) {
            throw new IllegalArgumentException("Invalid native media reference");
        }
        this.nativeRef = l;
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
    public EqualizerBand addBand(double d, double d2, double d3) {
        return this.nativeGetNumBands(this.nativeRef) >= 64 && d3 >= -24.0 && d3 <= 12.0 ? null : this.nativeAddBand(this.nativeRef, d, d2, d3);
    }

    @Override
    public boolean removeBand(double d) {
        return d > 0.0 ? this.nativeRemoveBand(this.nativeRef, d) : false;
    }

    private native boolean nativeGetEnabled(long var1);

    private native void nativeSetEnabled(long var1, boolean var3);

    private native int nativeGetNumBands(long var1);

    private native EqualizerBand nativeAddBand(long var1, double var3, double var5, double var7);

    private native boolean nativeRemoveBand(long var1, double var3);
}

