/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.effects;

public interface AudioSpectrum {
    public boolean getEnabled();

    public void setEnabled(boolean var1);

    public int getBandCount();

    public void setBandCount(int var1);

    public double getInterval();

    public void setInterval(double var1);

    public int getSensitivityThreshold();

    public void setSensitivityThreshold(int var1);

    public float[] getMagnitudes(float[] var1);

    public float[] getPhases(float[] var1);
}

