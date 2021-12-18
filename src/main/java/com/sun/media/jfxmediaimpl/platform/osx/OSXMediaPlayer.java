/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl.platform.osx;

import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.AudioSpectrum;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.NativeMedia;
import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import com.sun.media.jfxmediaimpl.platform.osx.OSXMedia;

final class OSXMediaPlayer
extends NativeMediaPlayer {
    private final AudioEqualizer audioEq;
    private final AudioSpectrum audioSpectrum;
    private final Locator mediaLocator;

    OSXMediaPlayer(NativeMedia nativeMedia) {
        super(nativeMedia);
        this.init();
        this.mediaLocator = nativeMedia.getLocator();
        this.osxCreatePlayer(this.mediaLocator.getStringLocation());
        this.audioEq = this.createNativeAudioEqualizer(this.osxGetAudioEqualizerRef());
        this.audioSpectrum = this.createNativeAudioSpectrum(this.osxGetAudioSpectrumRef());
    }

    OSXMediaPlayer(Locator locator) {
        this(new OSXMedia(locator));
    }

    @Override
    public AudioEqualizer getEqualizer() {
        return this.audioEq;
    }

    @Override
    public AudioSpectrum getAudioSpectrum() {
        return this.audioSpectrum;
    }

    @Override
    protected long playerGetAudioSyncDelay() throws MediaException {
        return this.osxGetAudioSyncDelay();
    }

    @Override
    protected void playerSetAudioSyncDelay(long l) throws MediaException {
        this.osxSetAudioSyncDelay(l);
    }

    @Override
    protected void playerPlay() throws MediaException {
        this.osxPlay();
    }

    @Override
    protected void playerStop() throws MediaException {
        this.osxStop();
    }

    @Override
    protected void playerPause() throws MediaException {
        this.osxPause();
    }

    @Override
    protected void playerFinish() throws MediaException {
        this.osxFinish();
    }

    @Override
    protected float playerGetRate() throws MediaException {
        return this.osxGetRate();
    }

    @Override
    protected void playerSetRate(float f) throws MediaException {
        this.osxSetRate(f);
    }

    @Override
    protected double playerGetPresentationTime() throws MediaException {
        return this.osxGetPresentationTime();
    }

    @Override
    protected boolean playerGetMute() throws MediaException {
        return this.osxGetMute();
    }

    @Override
    protected void playerSetMute(boolean bl) throws MediaException {
        this.osxSetMute(bl);
    }

    @Override
    protected float playerGetVolume() throws MediaException {
        return this.osxGetVolume();
    }

    @Override
    protected void playerSetVolume(float f) throws MediaException {
        this.osxSetVolume(f);
    }

    @Override
    protected float playerGetBalance() throws MediaException {
        return this.osxGetBalance();
    }

    @Override
    protected void playerSetBalance(float f) throws MediaException {
        this.osxSetBalance(f);
    }

    @Override
    protected double playerGetDuration() throws MediaException {
        double d = this.osxGetDuration();
        if (d == -1.0) {
            return Double.POSITIVE_INFINITY;
        }
        return d;
    }

    @Override
    protected void playerSeek(double d) throws MediaException {
        this.osxSeek(d);
    }

    @Override
    protected void playerDispose() {
        this.osxDispose();
    }

    @Override
    public void playerInit() throws MediaException {
    }

    private native void osxCreatePlayer(String var1) throws MediaException;

    private native long osxGetAudioEqualizerRef();

    private native long osxGetAudioSpectrumRef();

    private native long osxGetAudioSyncDelay() throws MediaException;

    private native void osxSetAudioSyncDelay(long var1) throws MediaException;

    private native void osxPlay() throws MediaException;

    private native void osxStop() throws MediaException;

    private native void osxPause() throws MediaException;

    private native void osxFinish() throws MediaException;

    private native float osxGetRate() throws MediaException;

    private native void osxSetRate(float var1) throws MediaException;

    private native double osxGetPresentationTime() throws MediaException;

    private native boolean osxGetMute() throws MediaException;

    private native void osxSetMute(boolean var1) throws MediaException;

    private native float osxGetVolume() throws MediaException;

    private native void osxSetVolume(float var1) throws MediaException;

    private native float osxGetBalance() throws MediaException;

    private native void osxSetBalance(float var1) throws MediaException;

    private native double osxGetDuration() throws MediaException;

    private native void osxSeek(double var1) throws MediaException;

    private native void osxDispose();
}

