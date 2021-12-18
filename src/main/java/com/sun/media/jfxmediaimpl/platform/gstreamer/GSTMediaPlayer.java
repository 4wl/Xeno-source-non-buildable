/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl.platform.gstreamer;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.AudioSpectrum;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import com.sun.media.jfxmediaimpl.platform.gstreamer.GSTMedia;

final class GSTMediaPlayer
extends NativeMediaPlayer {
    private GSTMedia gstMedia = null;
    private float mutedVolume = 1.0f;
    private boolean muteEnabled = false;
    private AudioEqualizer audioEqualizer;
    private AudioSpectrum audioSpectrum;

    private GSTMediaPlayer(GSTMedia gSTMedia) {
        super(gSTMedia);
        this.init();
        this.gstMedia = gSTMedia;
        int n = this.gstInitPlayer(this.gstMedia.getNativeMediaRef());
        if (0 != n) {
            this.dispose();
            this.throwMediaErrorException(n, null);
        }
        long l = this.gstMedia.getNativeMediaRef();
        this.audioSpectrum = this.createNativeAudioSpectrum(this.gstGetAudioSpectrum(l));
        this.audioEqualizer = this.createNativeAudioEqualizer(this.gstGetAudioEqualizer(l));
    }

    GSTMediaPlayer(Locator locator) {
        this(new GSTMedia(locator));
    }

    @Override
    public AudioEqualizer getEqualizer() {
        return this.audioEqualizer;
    }

    @Override
    public AudioSpectrum getAudioSpectrum() {
        return this.audioSpectrum;
    }

    private void throwMediaErrorException(int n, String string) throws MediaException {
        MediaError mediaError = MediaError.getFromCode(n);
        throw new MediaException(string, null, mediaError);
    }

    @Override
    protected long playerGetAudioSyncDelay() throws MediaException {
        long[] arrl = new long[1];
        int n = this.gstGetAudioSyncDelay(this.gstMedia.getNativeMediaRef(), arrl);
        if (0 != n) {
            this.throwMediaErrorException(n, null);
        }
        return arrl[0];
    }

    @Override
    protected void playerSetAudioSyncDelay(long l) throws MediaException {
        int n = this.gstSetAudioSyncDelay(this.gstMedia.getNativeMediaRef(), l);
        if (0 != n) {
            this.throwMediaErrorException(n, null);
        }
    }

    @Override
    protected void playerPlay() throws MediaException {
        int n = this.gstPlay(this.gstMedia.getNativeMediaRef());
        if (0 != n) {
            this.throwMediaErrorException(n, null);
        }
    }

    @Override
    protected void playerStop() throws MediaException {
        int n = this.gstStop(this.gstMedia.getNativeMediaRef());
        if (0 != n) {
            this.throwMediaErrorException(n, null);
        }
    }

    @Override
    protected void playerPause() throws MediaException {
        int n = this.gstPause(this.gstMedia.getNativeMediaRef());
        if (0 != n) {
            this.throwMediaErrorException(n, null);
        }
    }

    @Override
    protected void playerFinish() throws MediaException {
        int n = this.gstFinish(this.gstMedia.getNativeMediaRef());
        if (0 != n) {
            this.throwMediaErrorException(n, null);
        }
    }

    @Override
    protected float playerGetRate() throws MediaException {
        float[] arrf = new float[1];
        int n = this.gstGetRate(this.gstMedia.getNativeMediaRef(), arrf);
        if (0 != n) {
            this.throwMediaErrorException(n, null);
        }
        return arrf[0];
    }

    @Override
    protected void playerSetRate(float f) throws MediaException {
        int n = this.gstSetRate(this.gstMedia.getNativeMediaRef(), f);
        if (0 != n) {
            this.throwMediaErrorException(n, null);
        }
    }

    @Override
    protected double playerGetPresentationTime() throws MediaException {
        double[] arrd = new double[1];
        int n = this.gstGetPresentationTime(this.gstMedia.getNativeMediaRef(), arrd);
        if (0 != n) {
            this.throwMediaErrorException(n, null);
        }
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
        int n = this.gstGetVolume(this.gstMedia.getNativeMediaRef(), (float[])object);
        if (0 != n) {
            this.throwMediaErrorException(n, null);
        }
        return (float)object[0];
    }

    @Override
    protected synchronized void playerSetVolume(float f) throws MediaException {
        if (!this.muteEnabled) {
            int n = this.gstSetVolume(this.gstMedia.getNativeMediaRef(), f);
            if (0 != n) {
                this.throwMediaErrorException(n, null);
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
        int n = this.gstGetBalance(this.gstMedia.getNativeMediaRef(), arrf);
        if (0 != n) {
            this.throwMediaErrorException(n, null);
        }
        return arrf[0];
    }

    @Override
    protected void playerSetBalance(float f) throws MediaException {
        int n = this.gstSetBalance(this.gstMedia.getNativeMediaRef(), f);
        if (0 != n) {
            this.throwMediaErrorException(n, null);
        }
    }

    @Override
    protected double playerGetDuration() throws MediaException {
        double[] arrd = new double[1];
        int n = this.gstGetDuration(this.gstMedia.getNativeMediaRef(), arrd);
        if (0 != n) {
            this.throwMediaErrorException(n, null);
        }
        if (arrd[0] == -1.0) {
            return Double.POSITIVE_INFINITY;
        }
        return arrd[0];
    }

    @Override
    protected void playerSeek(double d) throws MediaException {
        int n = this.gstSeek(this.gstMedia.getNativeMediaRef(), d);
        if (0 != n) {
            this.throwMediaErrorException(n, null);
        }
    }

    @Override
    protected void playerInit() throws MediaException {
    }

    @Override
    protected void playerDispose() {
        this.audioEqualizer = null;
        this.audioSpectrum = null;
        this.gstMedia = null;
    }

    private native int gstInitPlayer(long var1);

    private native long gstGetAudioEqualizer(long var1);

    private native long gstGetAudioSpectrum(long var1);

    private native int gstGetAudioSyncDelay(long var1, long[] var3);

    private native int gstSetAudioSyncDelay(long var1, long var3);

    private native int gstPlay(long var1);

    private native int gstPause(long var1);

    private native int gstStop(long var1);

    private native int gstFinish(long var1);

    private native int gstGetRate(long var1, float[] var3);

    private native int gstSetRate(long var1, float var3);

    private native int gstGetPresentationTime(long var1, double[] var3);

    private native int gstGetVolume(long var1, float[] var3);

    private native int gstSetVolume(long var1, float var3);

    private native int gstGetBalance(long var1, float[] var3);

    private native int gstSetBalance(long var1, float var3);

    private native int gstGetDuration(long var1, double[] var3);

    private native int gstSeek(long var1, double var3);
}

