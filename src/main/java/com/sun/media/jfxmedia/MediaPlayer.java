/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.control.VideoRenderControl;
import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.AudioSpectrum;
import com.sun.media.jfxmedia.events.AudioSpectrumListener;
import com.sun.media.jfxmedia.events.BufferListener;
import com.sun.media.jfxmedia.events.MarkerListener;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.events.PlayerStateListener;
import com.sun.media.jfxmedia.events.PlayerTimeListener;
import com.sun.media.jfxmedia.events.VideoTrackSizeListener;

public interface MediaPlayer {
    public void addMediaErrorListener(MediaErrorListener var1);

    public void removeMediaErrorListener(MediaErrorListener var1);

    public void addMediaPlayerListener(PlayerStateListener var1);

    public void removeMediaPlayerListener(PlayerStateListener var1);

    public void addMediaTimeListener(PlayerTimeListener var1);

    public void removeMediaTimeListener(PlayerTimeListener var1);

    public void addVideoTrackSizeListener(VideoTrackSizeListener var1);

    public void removeVideoTrackSizeListener(VideoTrackSizeListener var1);

    public void addMarkerListener(MarkerListener var1);

    public void removeMarkerListener(MarkerListener var1);

    public void addBufferListener(BufferListener var1);

    public void removeBufferListener(BufferListener var1);

    public void addAudioSpectrumListener(AudioSpectrumListener var1);

    public void removeAudioSpectrumListener(AudioSpectrumListener var1);

    public VideoRenderControl getVideoRenderControl();

    public Media getMedia();

    public void setAudioSyncDelay(long var1);

    public long getAudioSyncDelay();

    public void play();

    public void stop();

    public void pause();

    public float getRate();

    public void setRate(float var1);

    public double getPresentationTime();

    public float getVolume();

    public void setVolume(float var1);

    public boolean getMute();

    public void setMute(boolean var1);

    public float getBalance();

    public void setBalance(float var1);

    public AudioEqualizer getEqualizer();

    public AudioSpectrum getAudioSpectrum();

    public double getDuration();

    public double getStartTime();

    public void setStartTime(double var1);

    public double getStopTime();

    public void setStopTime(double var1);

    public void seek(double var1);

    public PlayerStateEvent.PlayerState getState();

    public void dispose();
}

