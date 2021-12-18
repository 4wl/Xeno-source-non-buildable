/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.AudioClip;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.NativeMediaAudioClipPlayer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

final class NativeMediaAudioClip
extends AudioClip {
    private URI sourceURI;
    private Locator mediaLocator;
    private AtomicInteger playCount;

    private NativeMediaAudioClip(URI uRI) throws URISyntaxException, FileNotFoundException, IOException {
        this.sourceURI = uRI;
        this.playCount = new AtomicInteger(0);
        if (Logger.canLog(1)) {
            Logger.logMsg(1, "Creating AudioClip for URI " + uRI);
        }
        this.mediaLocator = new Locator(this.sourceURI);
        this.mediaLocator.init();
        this.mediaLocator.cacheMedia();
    }

    Locator getLocator() {
        return this.mediaLocator;
    }

    public static AudioClip load(URI uRI) throws URISyntaxException, FileNotFoundException, IOException {
        return new NativeMediaAudioClip(uRI);
    }

    public static AudioClip create(byte[] arrby, int n, int n2, int n3, int n4, int n5) {
        throw new UnsupportedOperationException("NativeMediaAudioClip does not support creating clips from raw sample data");
    }

    @Override
    public AudioClip createSegment(double d, double d2) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AudioClip createSegment(int n, int n2) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AudioClip resample(int n, int n2, int n3) throws IllegalArgumentException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AudioClip append(AudioClip audioClip) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AudioClip flatten() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isPlaying() {
        return this.playCount.get() > 0;
    }

    @Override
    public void play() {
        this.play(this.clipVolume, this.clipBalance, this.clipRate, this.clipPan, this.loopCount, this.clipPriority);
    }

    @Override
    public void play(double d) {
        this.play(d, this.clipBalance, this.clipRate, this.clipPan, this.loopCount, this.clipPriority);
    }

    @Override
    public void play(double d, double d2, double d3, double d4, int n, int n2) {
        this.playCount.getAndIncrement();
        NativeMediaAudioClipPlayer.playClip(this, d, d2, d3, d4, n, n2);
    }

    @Override
    public void stop() {
        NativeMediaAudioClipPlayer.stopPlayers(this.mediaLocator);
    }

    public static void stopAllClips() {
        NativeMediaAudioClipPlayer.stopPlayers(null);
    }

    void playFinished() {
        this.playCount.decrementAndGet();
    }
}

