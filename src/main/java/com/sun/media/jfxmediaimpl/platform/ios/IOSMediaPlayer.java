/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl.platform.ios;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.AudioSpectrum;
import com.sun.media.jfxmedia.effects.EqualizerBand;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import com.sun.media.jfxmediaimpl.platform.ios.IOSMedia;
import java.util.HashMap;
import java.util.Map;

public final class IOSMediaPlayer
extends NativeMediaPlayer {
    private IOSMedia iosMedia;
    private final NullAudioEQ audioEqualizer;
    private final NullAudioSpectrum audioSpectrum;
    private float mutedVolume = 1.0f;
    private boolean muteEnabled;

    private IOSMediaPlayer(IOSMedia iOSMedia) {
        super(iOSMedia);
        this.iosMedia = iOSMedia;
        this.init();
        this.handleError(this.iosInitPlayer(this.iosMedia.getNativeMediaRef()));
        this.audioEqualizer = new NullAudioEQ();
        this.audioSpectrum = new NullAudioSpectrum();
    }

    IOSMediaPlayer(Locator locator) {
        this(new IOSMedia(locator));
    }

    @Override
    public AudioEqualizer getEqualizer() {
        return this.audioEqualizer;
    }

    @Override
    public AudioSpectrum getAudioSpectrum() {
        return this.audioSpectrum;
    }

    private void handleError(int n) throws MediaException {
        if (0 != n) {
            MediaError mediaError = MediaError.getFromCode(n);
            throw new MediaException("Media error occurred", null, mediaError);
        }
    }

    @Override
    protected long playerGetAudioSyncDelay() throws MediaException {
        long[] arrl = new long[1];
        this.handleError(this.iosGetAudioSyncDelay(this.iosMedia.getNativeMediaRef(), arrl));
        return arrl[0];
    }

    @Override
    protected void playerSetAudioSyncDelay(long l) throws MediaException {
        this.handleError(this.iosSetAudioSyncDelay(this.iosMedia.getNativeMediaRef(), l));
    }

    @Override
    protected void playerPlay() throws MediaException {
        this.handleError(this.iosPlay(this.iosMedia.getNativeMediaRef()));
    }

    @Override
    protected void playerStop() throws MediaException {
        this.handleError(this.iosStop(this.iosMedia.getNativeMediaRef()));
    }

    @Override
    protected void playerPause() throws MediaException {
        this.handleError(this.iosPause(this.iosMedia.getNativeMediaRef()));
    }

    @Override
    protected float playerGetRate() throws MediaException {
        float[] arrf = new float[1];
        this.handleError(this.iosGetRate(this.iosMedia.getNativeMediaRef(), arrf));
        return arrf[0];
    }

    @Override
    protected void playerSetRate(float f) throws MediaException {
        this.handleError(this.iosSetRate(this.iosMedia.getNativeMediaRef(), f));
    }

    @Override
    protected double playerGetPresentationTime() throws MediaException {
        double[] arrd = new double[1];
        this.handleError(this.iosGetPresentationTime(this.iosMedia.getNativeMediaRef(), arrd));
        return arrd[0];
    }

    @Override
    protected boolean playerGetMute() throws MediaException {
        return this.muteEnabled;
    }

    @Override
    protected synchronized void playerSetMute(boolean bl) throws MediaException {
        if (bl != this.muteEnabled) {
            if (bl) {
                float f = this.getVolume();
                this.playerSetVolume(0.0f);
                this.muteEnabled = true;
                this.mutedVolume = f;
            } else {
                this.muteEnabled = false;
                this.playerSetVolume(this.mutedVolume);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected float playerGetVolume() throws MediaException {
        Object object = this;
        synchronized (object) {
            if (this.muteEnabled) {
                return this.mutedVolume;
            }
        }
        object = new float[1];
        this.handleError(this.iosGetVolume(this.iosMedia.getNativeMediaRef(), (float[])object));
        return (float)object[0];
    }

    @Override
    protected synchronized void playerSetVolume(float f) throws MediaException {
        if (!this.muteEnabled) {
            int n = this.iosSetVolume(this.iosMedia.getNativeMediaRef(), f);
            if (0 != n) {
                this.handleError(n);
            } else {
                this.mutedVolume = f;
            }
        } else {
            this.mutedVolume = f;
        }
    }

    @Override
    protected float playerGetBalance() throws MediaException {
        float[] arrf = new float[1];
        this.handleError(this.iosGetBalance(this.iosMedia.getNativeMediaRef(), arrf));
        return arrf[0];
    }

    @Override
    protected void playerSetBalance(float f) throws MediaException {
        this.handleError(this.iosSetBalance(this.iosMedia.getNativeMediaRef(), f));
    }

    @Override
    protected double playerGetDuration() throws MediaException {
        double[] arrd = new double[1];
        this.handleError(this.iosGetDuration(this.iosMedia.getNativeMediaRef(), arrd));
        double d = arrd[0] == -1.0 ? Double.POSITIVE_INFINITY : arrd[0];
        return d;
    }

    @Override
    protected void playerSeek(double d) throws MediaException {
        this.handleError(this.iosSeek(this.iosMedia.getNativeMediaRef(), d));
    }

    @Override
    protected void playerInit() throws MediaException {
    }

    @Override
    protected void playerFinish() throws MediaException {
        this.handleError(this.iosFinish(this.iosMedia.getNativeMediaRef()));
    }

    @Override
    protected void playerDispose() {
        this.iosDispose(this.iosMedia.getNativeMediaRef());
        this.iosMedia = null;
    }

    public void setOverlayX(double d) {
        this.handleError(this.iosSetOverlayX(this.iosMedia.getNativeMediaRef(), d));
    }

    public void setOverlayY(double d) {
        this.handleError(this.iosSetOverlayY(this.iosMedia.getNativeMediaRef(), d));
    }

    public void setOverlayVisible(boolean bl) {
        this.handleError(this.iosSetOverlayVisible(this.iosMedia.getNativeMediaRef(), bl));
    }

    public void setOverlayWidth(double d) {
        this.handleError(this.iosSetOverlayWidth(this.iosMedia.getNativeMediaRef(), d));
    }

    public void setOverlayHeight(double d) {
        this.handleError(this.iosSetOverlayHeight(this.iosMedia.getNativeMediaRef(), d));
    }

    public void setOverlayPreserveRatio(boolean bl) {
        this.handleError(this.iosSetOverlayPreserveRatio(this.iosMedia.getNativeMediaRef(), bl));
    }

    public void setOverlayOpacity(double d) {
        this.handleError(this.iosSetOverlayOpacity(this.iosMedia.getNativeMediaRef(), d));
    }

    public void setOverlayTransform(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12) {
        this.handleError(this.iosSetOverlayTransform(this.iosMedia.getNativeMediaRef(), d, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12));
    }

    private native int iosInitPlayer(long var1);

    private native int iosGetAudioSyncDelay(long var1, long[] var3);

    private native int iosSetAudioSyncDelay(long var1, long var3);

    private native int iosPlay(long var1);

    private native int iosPause(long var1);

    private native int iosStop(long var1);

    private native int iosGetRate(long var1, float[] var3);

    private native int iosSetRate(long var1, float var3);

    private native int iosGetPresentationTime(long var1, double[] var3);

    private native int iosGetVolume(long var1, float[] var3);

    private native int iosSetVolume(long var1, float var3);

    private native int iosGetBalance(long var1, float[] var3);

    private native int iosSetBalance(long var1, float var3);

    private native int iosGetDuration(long var1, double[] var3);

    private native int iosSeek(long var1, double var3);

    private native void iosDispose(long var1);

    private native int iosFinish(long var1);

    private native int iosSetOverlayX(long var1, double var3);

    private native int iosSetOverlayY(long var1, double var3);

    private native int iosSetOverlayVisible(long var1, boolean var3);

    private native int iosSetOverlayWidth(long var1, double var3);

    private native int iosSetOverlayHeight(long var1, double var3);

    private native int iosSetOverlayPreserveRatio(long var1, boolean var3);

    private native int iosSetOverlayOpacity(long var1, double var3);

    private native int iosSetOverlayTransform(long var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23, double var25);

    private static final class NullEQBand
    implements EqualizerBand {
        private double center;
        private double bandwidth;
        private double gain;

        NullEQBand(double d, double d2, double d3) {
            this.center = d;
            this.bandwidth = d2;
            this.gain = d3;
        }

        @Override
        public double getCenterFrequency() {
            return this.center;
        }

        @Override
        public void setCenterFrequency(double d) {
            this.center = d;
        }

        @Override
        public double getBandwidth() {
            return this.bandwidth;
        }

        @Override
        public void setBandwidth(double d) {
            this.bandwidth = d;
        }

        @Override
        public double getGain() {
            return this.gain;
        }

        @Override
        public void setGain(double d) {
            this.gain = d;
        }
    }

    private static final class NullAudioSpectrum
    implements AudioSpectrum {
        private boolean enabled = false;
        private int bandCount = 128;
        private double interval = 0.1;
        private int threshold = 60;
        private float[] fakeData;

        private NullAudioSpectrum() {
        }

        @Override
        public boolean getEnabled() {
            return this.enabled;
        }

        @Override
        public void setEnabled(boolean bl) {
            this.enabled = bl;
        }

        @Override
        public int getBandCount() {
            return this.bandCount;
        }

        @Override
        public void setBandCount(int n) {
            this.bandCount = n;
            this.fakeData = new float[this.bandCount];
        }

        @Override
        public double getInterval() {
            return this.interval;
        }

        @Override
        public void setInterval(double d) {
            this.interval = d;
        }

        @Override
        public int getSensitivityThreshold() {
            return this.threshold;
        }

        @Override
        public void setSensitivityThreshold(int n) {
            this.threshold = n;
        }

        @Override
        public float[] getMagnitudes(float[] arrf) {
            int n = this.fakeData.length;
            if (arrf == null || arrf.length < n) {
                arrf = new float[n];
            }
            System.arraycopy(this.fakeData, 0, arrf, 0, n);
            return arrf;
        }

        @Override
        public float[] getPhases(float[] arrf) {
            int n = this.fakeData.length;
            if (arrf == null || arrf.length < n) {
                arrf = new float[n];
            }
            System.arraycopy(this.fakeData, 0, arrf, 0, n);
            return arrf;
        }
    }

    private static final class NullAudioEQ
    implements AudioEqualizer {
        private boolean enabled = false;
        private Map<Double, EqualizerBand> bands = new HashMap<Double, EqualizerBand>();

        private NullAudioEQ() {
        }

        @Override
        public boolean getEnabled() {
            return this.enabled;
        }

        @Override
        public void setEnabled(boolean bl) {
            this.enabled = bl;
        }

        @Override
        public EqualizerBand addBand(double d, double d2, double d3) {
            Double d4 = new Double(d);
            if (this.bands.containsKey(d4)) {
                this.removeBand(d);
            }
            NullEQBand nullEQBand = new NullEQBand(d, d2, d3);
            this.bands.put(d4, nullEQBand);
            return nullEQBand;
        }

        @Override
        public boolean removeBand(double d) {
            Double d2 = new Double(d);
            if (this.bands.containsKey(d2)) {
                this.bands.remove(d2);
                return true;
            }
            return false;
        }
    }
}

