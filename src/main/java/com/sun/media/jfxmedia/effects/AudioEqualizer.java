/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.effects;

import com.sun.media.jfxmedia.effects.EqualizerBand;

public interface AudioEqualizer {
    public static final int MAX_NUM_BANDS = 64;

    public boolean getEnabled();

    public void setEnabled(boolean var1);

    public EqualizerBand addBand(double var1, double var3, double var5);

    public boolean removeBand(double var1);
}

