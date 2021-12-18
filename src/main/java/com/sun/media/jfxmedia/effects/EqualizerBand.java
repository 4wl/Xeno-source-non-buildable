/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.effects;

public interface EqualizerBand {
    public static final double MIN_GAIN = -24.0;
    public static final double MAX_GAIN = 12.0;

    public double getCenterFrequency();

    public void setCenterFrequency(double var1);

    public double getBandwidth();

    public void setBandwidth(double var1);

    public double getGain();

    public void setGain(double var1);
}

