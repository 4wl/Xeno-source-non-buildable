/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.control.VideoRenderControl;
import com.sun.media.jfxmedia.effects.AudioEqualizer;
import com.sun.media.jfxmedia.effects.AudioSpectrum;
import com.sun.media.jfxmedia.events.AudioSpectrumEvent;
import com.sun.media.jfxmedia.events.AudioSpectrumListener;
import com.sun.media.jfxmedia.events.BufferListener;
import com.sun.media.jfxmedia.events.BufferProgressEvent;
import com.sun.media.jfxmedia.events.MarkerEvent;
import com.sun.media.jfxmedia.events.MarkerListener;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.events.NewFrameEvent;
import com.sun.media.jfxmedia.events.PlayerEvent;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.events.PlayerStateListener;
import com.sun.media.jfxmedia.events.PlayerTimeListener;
import com.sun.media.jfxmedia.events.VideoFrameRateListener;
import com.sun.media.jfxmedia.events.VideoRendererListener;
import com.sun.media.jfxmedia.events.VideoTrackSizeListener;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmedia.track.AudioTrack;
import com.sun.media.jfxmedia.track.SubtitleTrack;
import com.sun.media.jfxmedia.track.Track;
import com.sun.media.jfxmedia.track.VideoResolution;
import com.sun.media.jfxmedia.track.VideoTrack;
import com.sun.media.jfxmediaimpl.MarkerStateListener;
import com.sun.media.jfxmediaimpl.MediaPulseTask;
import com.sun.media.jfxmediaimpl.MediaUtils;
import com.sun.media.jfxmediaimpl.NativeAudioEqualizer;
import com.sun.media.jfxmediaimpl.NativeAudioSpectrum;
import com.sun.media.jfxmediaimpl.NativeMedia;
import com.sun.media.jfxmediaimpl.NativeVideoBuffer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class NativeMediaPlayer
implements MediaPlayer,
MarkerStateListener {
    public static final int eventPlayerUnknown = 100;
    public static final int eventPlayerReady = 101;
    public static final int eventPlayerPlaying = 102;
    public static final int eventPlayerPaused = 103;
    public static final int eventPlayerStopped = 104;
    public static final int eventPlayerStalled = 105;
    public static final int eventPlayerFinished = 106;
    public static final int eventPlayerError = 107;
    private static final int NOMINAL_VIDEO_FPS = 30;
    public static final long ONE_SECOND = 1000000000L;
    private NativeMedia media;
    private VideoRenderControl videoRenderControl;
    private final List<WeakReference<MediaErrorListener>> errorListeners = new ArrayList<WeakReference<MediaErrorListener>>();
    private final List<WeakReference<PlayerStateListener>> playerStateListeners = new ArrayList<WeakReference<PlayerStateListener>>();
    private final List<WeakReference<PlayerTimeListener>> playerTimeListeners = new ArrayList<WeakReference<PlayerTimeListener>>();
    private final List<WeakReference<VideoTrackSizeListener>> videoTrackSizeListeners = new ArrayList<WeakReference<VideoTrackSizeListener>>();
    private final List<WeakReference<VideoRendererListener>> videoUpdateListeners = new ArrayList<WeakReference<VideoRendererListener>>();
    private final List<WeakReference<VideoFrameRateListener>> videoFrameRateListeners = new ArrayList<WeakReference<VideoFrameRateListener>>();
    private final List<WeakReference<MarkerListener>> markerListeners = new ArrayList<WeakReference<MarkerListener>>();
    private final List<WeakReference<BufferListener>> bufferListeners = new ArrayList<WeakReference<BufferListener>>();
    private final List<WeakReference<AudioSpectrumListener>> audioSpectrumListeners = new ArrayList<WeakReference<AudioSpectrumListener>>();
    private final List<PlayerStateEvent> cachedStateEvents = new ArrayList<PlayerStateEvent>();
    private final List<PlayerTimeEvent> cachedTimeEvents = new ArrayList<PlayerTimeEvent>();
    private final List<BufferProgressEvent> cachedBufferEvents = new ArrayList<BufferProgressEvent>();
    private final List<MediaErrorEvent> cachedErrorEvents = new ArrayList<MediaErrorEvent>();
    private boolean isFirstFrame = true;
    private NewFrameEvent firstFrameEvent = null;
    private double firstFrameTime;
    private final Object firstFrameLock = new Object();
    private EventQueueThread eventLoop = new EventQueueThread();
    private int frameWidth = -1;
    private int frameHeight = -1;
    private final AtomicBoolean isMediaPulseEnabled = new AtomicBoolean(false);
    private final Lock mediaPulseLock = new ReentrantLock();
    private Timer mediaPulseTimer;
    private final Lock markerLock = new ReentrantLock();
    private boolean checkSeek = false;
    private double timeBeforeSeek = 0.0;
    private double timeAfterSeek = 0.0;
    private double previousTime = 0.0;
    private double firedMarkerTime = -1.0;
    private double startTime = 0.0;
    private double stopTime = Double.POSITIVE_INFINITY;
    private boolean isStartTimeUpdated = false;
    private boolean isStopTimeSet = false;
    private double encodedFrameRate = 0.0;
    private boolean recomputeFrameRate = true;
    private double previousFrameTime;
    private long numFramesSincePlaying;
    private double meanFrameDuration;
    private double decodedFrameRate;
    private PlayerStateEvent.PlayerState playerState = PlayerStateEvent.PlayerState.UNKNOWN;
    private final Lock disposeLock = new ReentrantLock();
    private boolean isDisposed = false;
    private Runnable onDispose;

    protected NativeMediaPlayer(NativeMedia nativeMedia) {
        if (nativeMedia == null) {
            throw new IllegalArgumentException("clip == null!");
        }
        this.media = nativeMedia;
        this.videoRenderControl = new VideoRenderer();
    }

    protected void init() {
        this.media.addMarkerStateListener(this);
        this.eventLoop.start();
    }

    void setOnDispose(Runnable runnable) {
        this.disposeLock.lock();
        try {
            if (!this.isDisposed) {
                this.onDispose = runnable;
            }
        }
        finally {
            this.disposeLock.unlock();
        }
    }

    private synchronized void onNativeInit() {
        try {
            this.playerInit();
        }
        catch (MediaException mediaException) {
            this.sendPlayerMediaErrorEvent(mediaException.getMediaError().code());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void addMediaErrorListener(MediaErrorListener mediaErrorListener) {
        if (mediaErrorListener != null) {
            this.errorListeners.add(new WeakReference<MediaErrorListener>(mediaErrorListener));
            List<MediaErrorEvent> list = this.cachedErrorEvents;
            synchronized (list) {
                if (!this.cachedErrorEvents.isEmpty() && !this.errorListeners.isEmpty()) {
                    this.cachedErrorEvents.stream().forEach(mediaErrorEvent -> this.sendPlayerEvent((PlayerEvent)mediaErrorEvent));
                    this.cachedErrorEvents.clear();
                }
            }
        }
    }

    @Override
    public void removeMediaErrorListener(MediaErrorListener mediaErrorListener) {
        if (mediaErrorListener != null) {
            ListIterator<WeakReference<MediaErrorListener>> listIterator = this.errorListeners.listIterator();
            while (listIterator.hasNext()) {
                MediaErrorListener mediaErrorListener2 = (MediaErrorListener)listIterator.next().get();
                if (mediaErrorListener2 != null && mediaErrorListener2 != mediaErrorListener) continue;
                listIterator.remove();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void addMediaPlayerListener(PlayerStateListener playerStateListener) {
        if (playerStateListener != null) {
            List<PlayerStateEvent> list = this.cachedStateEvents;
            synchronized (list) {
                if (!this.cachedStateEvents.isEmpty() && this.playerStateListeners.isEmpty()) {
                    for (PlayerStateEvent playerStateEvent : this.cachedStateEvents) {
                        switch (playerStateEvent.getState()) {
                            case READY: {
                                playerStateListener.onReady(playerStateEvent);
                                break;
                            }
                            case PLAYING: {
                                playerStateListener.onPlaying(playerStateEvent);
                                break;
                            }
                            case PAUSED: {
                                playerStateListener.onPause(playerStateEvent);
                                break;
                            }
                            case STOPPED: {
                                playerStateListener.onStop(playerStateEvent);
                                break;
                            }
                            case STALLED: {
                                playerStateListener.onStall(playerStateEvent);
                                break;
                            }
                            case FINISHED: {
                                playerStateListener.onFinish(playerStateEvent);
                                break;
                            }
                        }
                    }
                    this.cachedStateEvents.clear();
                }
                this.playerStateListeners.add(new WeakReference<PlayerStateListener>(playerStateListener));
            }
        }
    }

    @Override
    public void removeMediaPlayerListener(PlayerStateListener playerStateListener) {
        if (playerStateListener != null) {
            ListIterator<WeakReference<PlayerStateListener>> listIterator = this.playerStateListeners.listIterator();
            while (listIterator.hasNext()) {
                PlayerStateListener playerStateListener2 = (PlayerStateListener)listIterator.next().get();
                if (playerStateListener2 != null && playerStateListener2 != playerStateListener) continue;
                listIterator.remove();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void addMediaTimeListener(PlayerTimeListener playerTimeListener) {
        if (playerTimeListener != null) {
            List<PlayerTimeEvent> list = this.cachedTimeEvents;
            synchronized (list) {
                if (!this.cachedTimeEvents.isEmpty() && this.playerTimeListeners.isEmpty()) {
                    for (PlayerTimeEvent playerTimeEvent : this.cachedTimeEvents) {
                        playerTimeListener.onDurationChanged(playerTimeEvent.getTime());
                    }
                    this.cachedTimeEvents.clear();
                } else {
                    double d = this.getDuration();
                    if (d != Double.POSITIVE_INFINITY) {
                        playerTimeListener.onDurationChanged(d);
                    }
                }
                this.playerTimeListeners.add(new WeakReference<PlayerTimeListener>(playerTimeListener));
            }
        }
    }

    @Override
    public void removeMediaTimeListener(PlayerTimeListener playerTimeListener) {
        if (playerTimeListener != null) {
            ListIterator<WeakReference<PlayerTimeListener>> listIterator = this.playerTimeListeners.listIterator();
            while (listIterator.hasNext()) {
                PlayerTimeListener playerTimeListener2 = (PlayerTimeListener)listIterator.next().get();
                if (playerTimeListener2 != null && playerTimeListener2 != playerTimeListener) continue;
                listIterator.remove();
            }
        }
    }

    @Override
    public void addVideoTrackSizeListener(VideoTrackSizeListener videoTrackSizeListener) {
        if (videoTrackSizeListener != null) {
            if (this.frameWidth != -1 && this.frameHeight != -1) {
                videoTrackSizeListener.onSizeChanged(this.frameWidth, this.frameHeight);
            }
            this.videoTrackSizeListeners.add(new WeakReference<VideoTrackSizeListener>(videoTrackSizeListener));
        }
    }

    @Override
    public void removeVideoTrackSizeListener(VideoTrackSizeListener videoTrackSizeListener) {
        if (videoTrackSizeListener != null) {
            ListIterator<WeakReference<VideoTrackSizeListener>> listIterator = this.videoTrackSizeListeners.listIterator();
            while (listIterator.hasNext()) {
                VideoTrackSizeListener videoTrackSizeListener2 = (VideoTrackSizeListener)listIterator.next().get();
                if (videoTrackSizeListener2 != null && videoTrackSizeListener2 != videoTrackSizeListener) continue;
                listIterator.remove();
            }
        }
    }

    @Override
    public void addMarkerListener(MarkerListener markerListener) {
        if (markerListener != null) {
            this.markerListeners.add(new WeakReference<MarkerListener>(markerListener));
        }
    }

    @Override
    public void removeMarkerListener(MarkerListener markerListener) {
        if (markerListener != null) {
            ListIterator<WeakReference<MarkerListener>> listIterator = this.markerListeners.listIterator();
            while (listIterator.hasNext()) {
                MarkerListener markerListener2 = (MarkerListener)listIterator.next().get();
                if (markerListener2 != null && markerListener2 != markerListener) continue;
                listIterator.remove();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void addBufferListener(BufferListener bufferListener) {
        if (bufferListener != null) {
            List<BufferProgressEvent> list = this.cachedBufferEvents;
            synchronized (list) {
                if (!this.cachedBufferEvents.isEmpty() && this.bufferListeners.isEmpty()) {
                    this.cachedBufferEvents.stream().forEach(bufferProgressEvent -> bufferListener.onBufferProgress((BufferProgressEvent)bufferProgressEvent));
                    this.cachedBufferEvents.clear();
                }
                this.bufferListeners.add(new WeakReference<BufferListener>(bufferListener));
            }
        }
    }

    @Override
    public void removeBufferListener(BufferListener bufferListener) {
        if (bufferListener != null) {
            ListIterator<WeakReference<BufferListener>> listIterator = this.bufferListeners.listIterator();
            while (listIterator.hasNext()) {
                BufferListener bufferListener2 = (BufferListener)listIterator.next().get();
                if (bufferListener2 != null && bufferListener2 != bufferListener) continue;
                listIterator.remove();
            }
        }
    }

    @Override
    public void addAudioSpectrumListener(AudioSpectrumListener audioSpectrumListener) {
        if (audioSpectrumListener != null) {
            this.audioSpectrumListeners.add(new WeakReference<AudioSpectrumListener>(audioSpectrumListener));
        }
    }

    @Override
    public void removeAudioSpectrumListener(AudioSpectrumListener audioSpectrumListener) {
        if (audioSpectrumListener != null) {
            ListIterator<WeakReference<AudioSpectrumListener>> listIterator = this.audioSpectrumListeners.listIterator();
            while (listIterator.hasNext()) {
                AudioSpectrumListener audioSpectrumListener2 = (AudioSpectrumListener)listIterator.next().get();
                if (audioSpectrumListener2 != null && audioSpectrumListener2 != audioSpectrumListener) continue;
                listIterator.remove();
            }
        }
    }

    @Override
    public VideoRenderControl getVideoRenderControl() {
        return this.videoRenderControl;
    }

    @Override
    public Media getMedia() {
        return this.media;
    }

    @Override
    public void setAudioSyncDelay(long l) {
        try {
            this.playerSetAudioSyncDelay(l);
        }
        catch (MediaException mediaException) {
            this.sendPlayerEvent(new MediaErrorEvent(this, mediaException.getMediaError()));
        }
    }

    @Override
    public long getAudioSyncDelay() {
        try {
            return this.playerGetAudioSyncDelay();
        }
        catch (MediaException mediaException) {
            this.sendPlayerEvent(new MediaErrorEvent(this, mediaException.getMediaError()));
            return 0L;
        }
    }

    @Override
    public void play() {
        try {
            if (this.isStartTimeUpdated) {
                this.playerSeek(this.startTime);
            }
            this.isMediaPulseEnabled.set(true);
            this.playerPlay();
        }
        catch (MediaException mediaException) {
            this.sendPlayerEvent(new MediaErrorEvent(this, mediaException.getMediaError()));
        }
    }

    @Override
    public void stop() {
        try {
            this.playerStop();
            this.playerSeek(this.startTime);
        }
        catch (MediaException mediaException) {
            MediaUtils.warning(this, "stop() failed!");
        }
    }

    @Override
    public void pause() {
        try {
            this.playerPause();
        }
        catch (MediaException mediaException) {
            this.sendPlayerEvent(new MediaErrorEvent(this, mediaException.getMediaError()));
        }
    }

    @Override
    public float getRate() {
        try {
            return this.playerGetRate();
        }
        catch (MediaException mediaException) {
            this.sendPlayerEvent(new MediaErrorEvent(this, mediaException.getMediaError()));
            return 0.0f;
        }
    }

    @Override
    public void setRate(float f) {
        try {
            this.playerSetRate(f);
        }
        catch (MediaException mediaException) {
            MediaUtils.warning(this, "setRate(" + f + ") failed!");
        }
    }

    @Override
    public double getPresentationTime() {
        try {
            return this.playerGetPresentationTime();
        }
        catch (MediaException mediaException) {
            return -1.0;
        }
    }

    @Override
    public float getVolume() {
        try {
            return this.playerGetVolume();
        }
        catch (MediaException mediaException) {
            this.sendPlayerEvent(new MediaErrorEvent(this, mediaException.getMediaError()));
            return 0.0f;
        }
    }

    @Override
    public void setVolume(float f) {
        if (f < 0.0f) {
            f = 0.0f;
        } else if (f > 1.0f) {
            f = 1.0f;
        }
        try {
            this.playerSetVolume(f);
        }
        catch (MediaException mediaException) {
            this.sendPlayerEvent(new MediaErrorEvent(this, mediaException.getMediaError()));
        }
    }

    @Override
    public boolean getMute() {
        try {
            return this.playerGetMute();
        }
        catch (MediaException mediaException) {
            this.sendPlayerEvent(new MediaErrorEvent(this, mediaException.getMediaError()));
            return false;
        }
    }

    @Override
    public void setMute(boolean bl) {
        try {
            this.playerSetMute(bl);
        }
        catch (MediaException mediaException) {
            this.sendPlayerEvent(new MediaErrorEvent(this, mediaException.getMediaError()));
        }
    }

    @Override
    public float getBalance() {
        try {
            return this.playerGetBalance();
        }
        catch (MediaException mediaException) {
            this.sendPlayerEvent(new MediaErrorEvent(this, mediaException.getMediaError()));
            return 0.0f;
        }
    }

    @Override
    public void setBalance(float f) {
        if (f < -1.0f) {
            f = -1.0f;
        } else if (f > 1.0f) {
            f = 1.0f;
        }
        try {
            this.playerSetBalance(f);
        }
        catch (MediaException mediaException) {
            this.sendPlayerEvent(new MediaErrorEvent(this, mediaException.getMediaError()));
        }
    }

    @Override
    public abstract AudioEqualizer getEqualizer();

    @Override
    public abstract AudioSpectrum getAudioSpectrum();

    @Override
    public double getDuration() {
        try {
            return this.playerGetDuration();
        }
        catch (MediaException mediaException) {
            return Double.POSITIVE_INFINITY;
        }
    }

    @Override
    public double getStartTime() {
        return this.startTime;
    }

    @Override
    public void setStartTime(double d) {
        try {
            this.markerLock.lock();
            this.startTime = d;
            if (this.playerState != PlayerStateEvent.PlayerState.PLAYING && this.playerState != PlayerStateEvent.PlayerState.FINISHED && this.playerState != PlayerStateEvent.PlayerState.STOPPED) {
                this.playerSeek(d);
            } else if (this.playerState == PlayerStateEvent.PlayerState.STOPPED) {
                this.isStartTimeUpdated = true;
            }
        }
        finally {
            this.markerLock.unlock();
        }
    }

    @Override
    public double getStopTime() {
        return this.stopTime;
    }

    @Override
    public void setStopTime(double d) {
        try {
            this.markerLock.lock();
            this.stopTime = d;
            this.isStopTimeSet = true;
            this.createMediaPulse();
        }
        finally {
            this.markerLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void seek(double d) {
        if (this.playerState == PlayerStateEvent.PlayerState.STOPPED) {
            return;
        }
        if (d < 0.0) {
            d = 0.0;
        } else {
            double d2 = this.getDuration();
            if (d2 >= 0.0 && d > d2) {
                d = d2;
            }
        }
        if (!this.isMediaPulseEnabled.get() && (this.playerState == PlayerStateEvent.PlayerState.PLAYING || this.playerState == PlayerStateEvent.PlayerState.PAUSED || this.playerState == PlayerStateEvent.PlayerState.FINISHED) && this.getStartTime() <= d && d <= this.getStopTime()) {
            this.isMediaPulseEnabled.set(true);
        }
        this.markerLock.lock();
        try {
            this.timeBeforeSeek = this.getPresentationTime();
            this.timeAfterSeek = d;
            this.checkSeek = this.timeBeforeSeek != this.timeAfterSeek;
            this.previousTime = d;
            this.firedMarkerTime = -1.0;
            try {
                this.playerSeek(d);
            }
            catch (MediaException mediaException) {
                MediaUtils.warning(this, "seek(" + d + ") failed!");
            }
        }
        finally {
            this.markerLock.unlock();
        }
    }

    protected abstract long playerGetAudioSyncDelay() throws MediaException;

    protected abstract void playerSetAudioSyncDelay(long var1) throws MediaException;

    protected abstract void playerPlay() throws MediaException;

    protected abstract void playerStop() throws MediaException;

    protected abstract void playerPause() throws MediaException;

    protected abstract void playerFinish() throws MediaException;

    protected abstract float playerGetRate() throws MediaException;

    protected abstract void playerSetRate(float var1) throws MediaException;

    protected abstract double playerGetPresentationTime() throws MediaException;

    protected abstract boolean playerGetMute() throws MediaException;

    protected abstract void playerSetMute(boolean var1) throws MediaException;

    protected abstract float playerGetVolume() throws MediaException;

    protected abstract void playerSetVolume(float var1) throws MediaException;

    protected abstract float playerGetBalance() throws MediaException;

    protected abstract void playerSetBalance(float var1) throws MediaException;

    protected abstract double playerGetDuration() throws MediaException;

    protected abstract void playerSeek(double var1) throws MediaException;

    protected abstract void playerInit() throws MediaException;

    protected abstract void playerDispose();

    @Override
    public PlayerStateEvent.PlayerState getState() {
        return this.playerState;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public final void dispose() {
        block25: {
            this.disposeLock.lock();
            try {
                if (this.isDisposed) break block25;
                this.destroyMediaPulse();
                if (this.eventLoop != null) {
                    this.eventLoop.terminateLoop();
                    this.eventLoop = null;
                }
                ListIterator<WeakReference<VideoRendererListener>> listIterator = this.firstFrameLock;
                synchronized (listIterator) {
                    if (this.firstFrameEvent != null) {
                        this.firstFrameEvent.getFrameData().releaseFrame();
                        this.firstFrameEvent = null;
                    }
                }
                this.playerDispose();
                if (this.media != null) {
                    this.media.dispose();
                    this.media = null;
                }
                if (this.videoUpdateListeners != null) {
                    listIterator = this.videoUpdateListeners.listIterator();
                    while (listIterator.hasNext()) {
                        VideoRendererListener videoRendererListener = (VideoRendererListener)listIterator.next().get();
                        if (videoRendererListener != null) {
                            videoRendererListener.releaseVideoFrames();
                            continue;
                        }
                        listIterator.remove();
                    }
                    this.videoUpdateListeners.clear();
                }
                if (this.playerStateListeners != null) {
                    this.playerStateListeners.clear();
                }
                if (this.videoTrackSizeListeners != null) {
                    this.videoTrackSizeListeners.clear();
                }
                if (this.videoFrameRateListeners != null) {
                    this.videoFrameRateListeners.clear();
                }
                if (this.cachedStateEvents != null) {
                    this.cachedStateEvents.clear();
                }
                if (this.cachedTimeEvents != null) {
                    this.cachedTimeEvents.clear();
                }
                if (this.cachedBufferEvents != null) {
                    this.cachedBufferEvents.clear();
                }
                if (this.errorListeners != null) {
                    this.errorListeners.clear();
                }
                if (this.playerTimeListeners != null) {
                    this.playerTimeListeners.clear();
                }
                if (this.markerListeners != null) {
                    this.markerListeners.clear();
                }
                if (this.bufferListeners != null) {
                    this.bufferListeners.clear();
                }
                if (this.audioSpectrumListeners != null) {
                    this.audioSpectrumListeners.clear();
                }
                if (this.videoRenderControl != null) {
                    this.videoRenderControl = null;
                }
                if (this.onDispose != null) {
                    this.onDispose.run();
                }
                this.isDisposed = true;
            }
            finally {
                this.disposeLock.unlock();
            }
        }
    }

    protected void sendWarning(int n, String string) {
        if (this.eventLoop != null) {
            String string2 = String.format("Internal media warning: %d", n);
            if (string != null) {
                string2 = string2 + ": " + string;
            }
            this.eventLoop.postEvent(new WarningEvent(this, string2));
        }
    }

    protected void sendPlayerEvent(PlayerEvent playerEvent) {
        if (this.eventLoop != null) {
            this.eventLoop.postEvent(playerEvent);
        }
    }

    protected void sendPlayerHaltEvent(String string, double d) {
        Logger.logMsg(4, string);
        if (this.eventLoop != null) {
            this.eventLoop.postEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.HALTED, d, string));
        }
    }

    protected void sendPlayerMediaErrorEvent(int n) {
        this.sendPlayerEvent(new MediaErrorEvent(this, MediaError.getFromCode(n)));
    }

    protected void sendPlayerStateEvent(int n, double d) {
        switch (n) {
            case 101: {
                this.sendPlayerEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.READY, d));
                break;
            }
            case 102: {
                this.sendPlayerEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.PLAYING, d));
                break;
            }
            case 103: {
                this.sendPlayerEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.PAUSED, d));
                break;
            }
            case 104: {
                this.sendPlayerEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.STOPPED, d));
                break;
            }
            case 105: {
                this.sendPlayerEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.STALLED, d));
                break;
            }
            case 106: {
                this.sendPlayerEvent(new PlayerStateEvent(PlayerStateEvent.PlayerState.FINISHED, d));
                break;
            }
        }
    }

    protected void sendNewFrameEvent(long l) {
        NativeVideoBuffer nativeVideoBuffer = NativeVideoBuffer.createVideoBuffer(l);
        this.sendPlayerEvent(new NewFrameEvent(nativeVideoBuffer));
    }

    protected void sendFrameSizeChangedEvent(int n, int n2) {
        this.sendPlayerEvent(new FrameSizeChangedEvent(n, n2));
    }

    protected void sendAudioTrack(boolean bl, long l, String string, int n, String string2, int n2, int n3, float f) {
        Locale locale = null;
        if (!string2.equals("und")) {
            locale = new Locale(string2);
        }
        AudioTrack audioTrack = new AudioTrack(bl, l, string, locale, Track.Encoding.toEncoding(n), n2, n3, f);
        TrackEvent trackEvent = new TrackEvent(audioTrack);
        this.sendPlayerEvent(trackEvent);
    }

    protected void sendVideoTrack(boolean bl, long l, String string, int n, int n2, int n3, float f, boolean bl2) {
        VideoTrack videoTrack = new VideoTrack(bl, l, string, null, Track.Encoding.toEncoding(n), new VideoResolution(n2, n3), f, bl2);
        TrackEvent trackEvent = new TrackEvent(videoTrack);
        this.sendPlayerEvent(trackEvent);
    }

    protected void sendSubtitleTrack(boolean bl, long l, String string, int n, String string2) {
        Locale locale = null;
        if (null != string2) {
            locale = new Locale(string2);
        }
        SubtitleTrack subtitleTrack = new SubtitleTrack(bl, l, string, locale, Track.Encoding.toEncoding(n));
        this.sendPlayerEvent(new TrackEvent(subtitleTrack));
    }

    protected void sendMarkerEvent(String string, double d) {
        this.sendPlayerEvent(new MarkerEvent(string, d));
    }

    protected void sendDurationUpdateEvent(double d) {
        this.sendPlayerEvent(new PlayerTimeEvent(d));
    }

    protected void sendBufferProgressEvent(double d, long l, long l2, long l3) {
        this.sendPlayerEvent(new BufferProgressEvent(d, l, l2, l3));
    }

    protected void sendAudioSpectrumEvent(double d, double d2) {
        this.sendPlayerEvent(new AudioSpectrumEvent(this.getAudioSpectrum(), d, d2));
    }

    @Override
    public void markerStateChanged(boolean bl) {
        if (bl) {
            this.markerLock.lock();
            try {
                this.previousTime = this.getPresentationTime();
            }
            finally {
                this.markerLock.unlock();
            }
            this.createMediaPulse();
        } else if (!this.isStopTimeSet) {
            this.destroyMediaPulse();
        }
    }

    private void createMediaPulse() {
        this.mediaPulseLock.lock();
        try {
            if (this.mediaPulseTimer == null) {
                this.mediaPulseTimer = new Timer(true);
                this.mediaPulseTimer.scheduleAtFixedRate((TimerTask)new MediaPulseTask(this), 0L, 40L);
            }
        }
        finally {
            this.mediaPulseLock.unlock();
        }
    }

    private void destroyMediaPulse() {
        this.mediaPulseLock.lock();
        try {
            if (this.mediaPulseTimer != null) {
                this.mediaPulseTimer.cancel();
                this.mediaPulseTimer = null;
            }
        }
        finally {
            this.mediaPulseLock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    boolean doMediaPulseTask() {
        block16: {
            block17: {
                if (this.isMediaPulseEnabled.get() == false) return true;
                this.disposeLock.lock();
                if (this.isDisposed) {
                    this.disposeLock.unlock();
                    return false;
                }
                var1_1 = this.getPresentationTime();
                this.markerLock.lock();
                if (this.checkSeek) {
                    if (this.timeAfterSeek > this.timeBeforeSeek) {
                        if (!(var1_1 >= this.timeAfterSeek)) {
                            var3_2 = true;
                            return var3_2;
                        }
                        this.checkSeek = false;
                    } else if (this.timeAfterSeek < this.timeBeforeSeek) {
                        if (var1_1 >= this.timeBeforeSeek) {
                            var3_3 = true;
                            return var3_3;
                        }
                        this.checkSeek = false;
                    }
                }
                var3_4 = this.media.getNextMarker(this.previousTime, true);
lbl22:
                // 2 sources

                while (true) {
                    if (var3_4 != null && !((var4_5 = var3_4.getKey().doubleValue()) > var1_1)) {
                        if (var4_5 == this.firedMarkerTime || !(var4_5 >= this.previousTime) || !(var4_5 >= this.getStartTime()) || !(var4_5 <= this.getStopTime())) break block16;
                        var6_6 = new MarkerEvent(var3_4.getValue(), var4_5);
                        var7_7 = this.markerListeners.listIterator();
                        break block17;
                    }
                    this.previousTime = var1_1;
                    if (this.isStopTimeSet == false) return true;
                    if (!(var1_1 >= this.stopTime)) return true;
                    this.playerFinish();
                    return true;
                }
                finally {
                    this.disposeLock.unlock();
                    this.markerLock.unlock();
                }
            }
            while (var7_7.hasNext()) {
                var8_8 = (MarkerListener)var7_7.next().get();
                if (var8_8 != null) {
                    var8_8.onMarker(var6_6);
                    continue;
                }
                var7_7.remove();
            }
            this.firedMarkerTime = var4_5;
        }
        var3_4 = this.media.getNextMarker(var4_5, false);
        ** continue;
    }

    protected AudioEqualizer createNativeAudioEqualizer(long l) {
        return new NativeAudioEqualizer(l);
    }

    protected AudioSpectrum createNativeAudioSpectrum(long l) {
        return new NativeAudioSpectrum(l);
    }

    private class EventQueueThread
    extends Thread {
        private final BlockingQueue<PlayerEvent> eventQueue = new LinkedBlockingQueue<PlayerEvent>();
        private volatile boolean stopped = false;

        EventQueueThread() {
            this.setName("JFXMedia Player EventQueueThread");
            this.setDaemon(true);
        }

        @Override
        public void run() {
            while (!this.stopped) {
                try {
                    PlayerEvent playerEvent = this.eventQueue.take();
                    if (this.stopped) continue;
                    if (playerEvent instanceof NewFrameEvent) {
                        try {
                            this.HandleRendererEvents((NewFrameEvent)playerEvent);
                        }
                        catch (Throwable throwable) {
                            if (!Logger.canLog(4)) continue;
                            Logger.logMsg(4, "Caught exception in HandleRendererEvents: " + throwable.toString());
                        }
                        continue;
                    }
                    if (playerEvent instanceof PlayerStateEvent) {
                        this.HandleStateEvents((PlayerStateEvent)playerEvent);
                        continue;
                    }
                    if (playerEvent instanceof FrameSizeChangedEvent) {
                        this.HandleFrameSizeChangedEvents((FrameSizeChangedEvent)playerEvent);
                        continue;
                    }
                    if (playerEvent instanceof TrackEvent) {
                        this.HandleTrackEvents((TrackEvent)playerEvent);
                        continue;
                    }
                    if (playerEvent instanceof MarkerEvent) {
                        this.HandleMarkerEvents((MarkerEvent)playerEvent);
                        continue;
                    }
                    if (playerEvent instanceof WarningEvent) {
                        this.HandleWarningEvents((WarningEvent)playerEvent);
                        continue;
                    }
                    if (playerEvent instanceof PlayerTimeEvent) {
                        this.HandlePlayerTimeEvents((PlayerTimeEvent)playerEvent);
                        continue;
                    }
                    if (playerEvent instanceof BufferProgressEvent) {
                        this.HandleBufferEvents((BufferProgressEvent)playerEvent);
                        continue;
                    }
                    if (playerEvent instanceof AudioSpectrumEvent) {
                        this.HandleAudioSpectrumEvents((AudioSpectrumEvent)playerEvent);
                        continue;
                    }
                    if (!(playerEvent instanceof MediaErrorEvent)) continue;
                    this.HandleErrorEvents((MediaErrorEvent)playerEvent);
                }
                catch (Exception exception) {}
            }
            this.eventQueue.clear();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void HandleRendererEvents(NewFrameEvent newFrameEvent) {
            Object object;
            if (NativeMediaPlayer.this.isFirstFrame) {
                NativeMediaPlayer.this.isFirstFrame = false;
                object = NativeMediaPlayer.this.firstFrameLock;
                synchronized (object) {
                    NativeMediaPlayer.this.firstFrameEvent = newFrameEvent;
                    NativeMediaPlayer.this.firstFrameTime = NativeMediaPlayer.this.firstFrameEvent.getFrameData().getTimestamp();
                    NativeMediaPlayer.this.firstFrameEvent.getFrameData().holdFrame();
                }
            }
            if (NativeMediaPlayer.this.firstFrameEvent != null && NativeMediaPlayer.this.firstFrameTime != newFrameEvent.getFrameData().getTimestamp()) {
                object = NativeMediaPlayer.this.firstFrameLock;
                synchronized (object) {
                    NativeMediaPlayer.this.firstFrameEvent.getFrameData().releaseFrame();
                    NativeMediaPlayer.this.firstFrameEvent = null;
                }
            }
            object = NativeMediaPlayer.this.videoUpdateListeners.listIterator();
            while (object.hasNext()) {
                VideoRendererListener videoRendererListener = (VideoRendererListener)((WeakReference)object.next()).get();
                if (videoRendererListener != null) {
                    videoRendererListener.videoFrameUpdated(newFrameEvent);
                    continue;
                }
                object.remove();
            }
            newFrameEvent.getFrameData().releaseFrame();
            if (!NativeMediaPlayer.this.videoFrameRateListeners.isEmpty()) {
                double d = (double)System.nanoTime() / 1.0E9;
                if (NativeMediaPlayer.this.recomputeFrameRate) {
                    NativeMediaPlayer.this.recomputeFrameRate = false;
                    NativeMediaPlayer.this.previousFrameTime = d;
                    NativeMediaPlayer.this.numFramesSincePlaying = 1L;
                } else {
                    boolean bl = false;
                    if (NativeMediaPlayer.this.numFramesSincePlaying == 1L) {
                        NativeMediaPlayer.this.meanFrameDuration = d - NativeMediaPlayer.this.previousFrameTime;
                        if (NativeMediaPlayer.this.meanFrameDuration > 0.0) {
                            NativeMediaPlayer.this.decodedFrameRate = 1.0 / NativeMediaPlayer.this.meanFrameDuration;
                            bl = true;
                        }
                    } else {
                        double d2 = NativeMediaPlayer.this.meanFrameDuration;
                        int n = NativeMediaPlayer.this.encodedFrameRate != 0.0 ? (int)(NativeMediaPlayer.this.encodedFrameRate + 0.5) : 30;
                        long l = NativeMediaPlayer.this.numFramesSincePlaying < (long)n ? NativeMediaPlayer.this.numFramesSincePlaying : (long)n;
                        NativeMediaPlayer.this.meanFrameDuration = ((double)(l - 1L) * d2 + d - NativeMediaPlayer.this.previousFrameTime) / (double)l;
                        if (NativeMediaPlayer.this.meanFrameDuration > 0.0 && Math.abs(NativeMediaPlayer.this.decodedFrameRate - 1.0 / NativeMediaPlayer.this.meanFrameDuration) > 0.5) {
                            NativeMediaPlayer.this.decodedFrameRate = 1.0 / NativeMediaPlayer.this.meanFrameDuration;
                            bl = true;
                        }
                    }
                    if (bl) {
                        ListIterator listIterator = NativeMediaPlayer.this.videoFrameRateListeners.listIterator();
                        while (listIterator.hasNext()) {
                            VideoFrameRateListener videoFrameRateListener = (VideoFrameRateListener)((WeakReference)listIterator.next()).get();
                            if (videoFrameRateListener != null) {
                                videoFrameRateListener.onFrameRateChanged(NativeMediaPlayer.this.decodedFrameRate);
                                continue;
                            }
                            listIterator.remove();
                        }
                    }
                    NativeMediaPlayer.this.previousFrameTime = d;
                    NativeMediaPlayer.this.numFramesSincePlaying++;
                }
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void HandleStateEvents(PlayerStateEvent playerStateEvent) {
            NativeMediaPlayer.this.playerState = playerStateEvent.getState();
            NativeMediaPlayer.this.recomputeFrameRate = PlayerStateEvent.PlayerState.PLAYING == playerStateEvent.getState();
            switch (NativeMediaPlayer.this.playerState) {
                case READY: {
                    NativeMediaPlayer.this.onNativeInit();
                    this.sendFakeBufferProgressEvent();
                    break;
                }
                case PLAYING: {
                    NativeMediaPlayer.this.isMediaPulseEnabled.set(true);
                    break;
                }
                case STOPPED: 
                case FINISHED: {
                    NativeMediaPlayer.this.doMediaPulseTask();
                }
                case PAUSED: 
                case STALLED: 
                case HALTED: {
                    NativeMediaPlayer.this.isMediaPulseEnabled.set(false);
                    break;
                }
            }
            Object object = NativeMediaPlayer.this.cachedStateEvents;
            synchronized (object) {
                if (NativeMediaPlayer.this.playerStateListeners.isEmpty()) {
                    NativeMediaPlayer.this.cachedStateEvents.add(playerStateEvent);
                    return;
                }
            }
            object = NativeMediaPlayer.this.playerStateListeners.listIterator();
            block18: while (object.hasNext()) {
                PlayerStateListener playerStateListener = (PlayerStateListener)((WeakReference)object.next()).get();
                if (playerStateListener != null) {
                    switch (NativeMediaPlayer.this.playerState) {
                        case READY: {
                            NativeMediaPlayer.this.onNativeInit();
                            this.sendFakeBufferProgressEvent();
                            playerStateListener.onReady(playerStateEvent);
                            continue block18;
                        }
                        case PLAYING: {
                            playerStateListener.onPlaying(playerStateEvent);
                            continue block18;
                        }
                        case PAUSED: {
                            playerStateListener.onPause(playerStateEvent);
                            continue block18;
                        }
                        case STOPPED: {
                            playerStateListener.onStop(playerStateEvent);
                            continue block18;
                        }
                        case STALLED: {
                            playerStateListener.onStall(playerStateEvent);
                            continue block18;
                        }
                        case FINISHED: {
                            playerStateListener.onFinish(playerStateEvent);
                            continue block18;
                        }
                        case HALTED: {
                            playerStateListener.onHalt(playerStateEvent);
                            continue block18;
                        }
                    }
                    continue;
                }
                object.remove();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void HandlePlayerTimeEvents(PlayerTimeEvent playerTimeEvent) {
            Object object = NativeMediaPlayer.this.cachedTimeEvents;
            synchronized (object) {
                if (NativeMediaPlayer.this.playerTimeListeners.isEmpty()) {
                    NativeMediaPlayer.this.cachedTimeEvents.add(playerTimeEvent);
                    return;
                }
            }
            object = NativeMediaPlayer.this.playerTimeListeners.listIterator();
            while (object.hasNext()) {
                PlayerTimeListener playerTimeListener = (PlayerTimeListener)((WeakReference)object.next()).get();
                if (playerTimeListener != null) {
                    playerTimeListener.onDurationChanged(playerTimeEvent.getTime());
                    continue;
                }
                object.remove();
            }
        }

        private void HandleFrameSizeChangedEvents(FrameSizeChangedEvent frameSizeChangedEvent) {
            NativeMediaPlayer.this.frameWidth = frameSizeChangedEvent.getWidth();
            NativeMediaPlayer.this.frameHeight = frameSizeChangedEvent.getHeight();
            Logger.logMsg(1, "** Frame size changed (" + NativeMediaPlayer.this.frameWidth + ", " + NativeMediaPlayer.this.frameHeight + ")");
            ListIterator listIterator = NativeMediaPlayer.this.videoTrackSizeListeners.listIterator();
            while (listIterator.hasNext()) {
                VideoTrackSizeListener videoTrackSizeListener = (VideoTrackSizeListener)((WeakReference)listIterator.next()).get();
                if (videoTrackSizeListener != null) {
                    videoTrackSizeListener.onSizeChanged(NativeMediaPlayer.this.frameWidth, NativeMediaPlayer.this.frameHeight);
                    continue;
                }
                listIterator.remove();
            }
        }

        private void HandleTrackEvents(TrackEvent trackEvent) {
            NativeMediaPlayer.this.media.addTrack(trackEvent.getTrack());
            if (trackEvent.getTrack() instanceof VideoTrack) {
                NativeMediaPlayer.this.encodedFrameRate = ((VideoTrack)trackEvent.getTrack()).getEncodedFrameRate();
            }
        }

        private void HandleMarkerEvents(MarkerEvent markerEvent) {
            ListIterator listIterator = NativeMediaPlayer.this.markerListeners.listIterator();
            while (listIterator.hasNext()) {
                MarkerListener markerListener = (MarkerListener)((WeakReference)listIterator.next()).get();
                if (markerListener != null) {
                    markerListener.onMarker(markerEvent);
                    continue;
                }
                listIterator.remove();
            }
        }

        private void HandleWarningEvents(WarningEvent warningEvent) {
            Logger.logMsg(3, warningEvent.getSource() + warningEvent.getMessage());
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void HandleErrorEvents(MediaErrorEvent mediaErrorEvent) {
            Logger.logMsg(4, mediaErrorEvent.getMessage());
            Object object = NativeMediaPlayer.this.cachedErrorEvents;
            synchronized (object) {
                if (NativeMediaPlayer.this.errorListeners.isEmpty()) {
                    NativeMediaPlayer.this.cachedErrorEvents.add(mediaErrorEvent);
                    return;
                }
            }
            object = NativeMediaPlayer.this.errorListeners.listIterator();
            while (object.hasNext()) {
                MediaErrorListener mediaErrorListener = (MediaErrorListener)((WeakReference)object.next()).get();
                if (mediaErrorListener != null) {
                    mediaErrorListener.onError(mediaErrorEvent.getSource(), mediaErrorEvent.getErrorCode(), mediaErrorEvent.getMessage());
                    continue;
                }
                object.remove();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void HandleBufferEvents(BufferProgressEvent bufferProgressEvent) {
            Object object = NativeMediaPlayer.this.cachedBufferEvents;
            synchronized (object) {
                if (NativeMediaPlayer.this.bufferListeners.isEmpty()) {
                    NativeMediaPlayer.this.cachedBufferEvents.add(bufferProgressEvent);
                    return;
                }
            }
            object = NativeMediaPlayer.this.bufferListeners.listIterator();
            while (object.hasNext()) {
                BufferListener bufferListener = (BufferListener)((WeakReference)object.next()).get();
                if (bufferListener != null) {
                    bufferListener.onBufferProgress(bufferProgressEvent);
                    continue;
                }
                object.remove();
            }
        }

        private void HandleAudioSpectrumEvents(AudioSpectrumEvent audioSpectrumEvent) {
            ListIterator listIterator = NativeMediaPlayer.this.audioSpectrumListeners.listIterator();
            while (listIterator.hasNext()) {
                AudioSpectrumListener audioSpectrumListener = (AudioSpectrumListener)((WeakReference)listIterator.next()).get();
                if (audioSpectrumListener != null) {
                    audioSpectrumListener.onAudioSpectrumEvent(audioSpectrumEvent);
                    continue;
                }
                listIterator.remove();
            }
        }

        public void postEvent(PlayerEvent playerEvent) {
            if (this.eventQueue != null) {
                this.eventQueue.offer(playerEvent);
            }
        }

        public void terminateLoop() {
            this.stopped = true;
            try {
                this.eventQueue.put(new PlayerEvent());
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }

        private void sendFakeBufferProgressEvent() {
            String string = NativeMediaPlayer.this.media.getLocator().getContentType();
            String string2 = NativeMediaPlayer.this.media.getLocator().getProtocol();
            if (string != null && (string.equals("audio/mpegurl") || string.equals("application/vnd.apple.mpegurl")) || string2 != null && !string2.equals("http") && !string2.equals("https")) {
                this.HandleBufferEvents(new BufferProgressEvent(NativeMediaPlayer.this.getDuration(), 0L, 1L, 1L));
            }
        }
    }

    private class VideoRenderer
    implements VideoRenderControl {
        private VideoRenderer() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void addVideoRendererListener(VideoRendererListener videoRendererListener) {
            if (videoRendererListener != null) {
                Object object = NativeMediaPlayer.this.firstFrameLock;
                synchronized (object) {
                    if (NativeMediaPlayer.this.firstFrameEvent != null) {
                        videoRendererListener.videoFrameUpdated(NativeMediaPlayer.this.firstFrameEvent);
                    }
                }
                NativeMediaPlayer.this.videoUpdateListeners.add(new WeakReference<VideoRendererListener>(videoRendererListener));
            }
        }

        @Override
        public void removeVideoRendererListener(VideoRendererListener videoRendererListener) {
            if (videoRendererListener != null) {
                ListIterator listIterator = NativeMediaPlayer.this.videoUpdateListeners.listIterator();
                while (listIterator.hasNext()) {
                    VideoRendererListener videoRendererListener2 = (VideoRendererListener)((WeakReference)listIterator.next()).get();
                    if (videoRendererListener2 != null && videoRendererListener2 != videoRendererListener) continue;
                    listIterator.remove();
                }
            }
        }

        @Override
        public void addVideoFrameRateListener(VideoFrameRateListener videoFrameRateListener) {
            if (videoFrameRateListener != null) {
                NativeMediaPlayer.this.videoFrameRateListeners.add(new WeakReference<VideoFrameRateListener>(videoFrameRateListener));
            }
        }

        @Override
        public void removeVideoFrameRateListener(VideoFrameRateListener videoFrameRateListener) {
            if (videoFrameRateListener != null) {
                ListIterator listIterator = NativeMediaPlayer.this.videoFrameRateListeners.listIterator();
                while (listIterator.hasNext()) {
                    VideoFrameRateListener videoFrameRateListener2 = (VideoFrameRateListener)((WeakReference)listIterator.next()).get();
                    if (videoFrameRateListener2 != null && videoFrameRateListener2 != videoFrameRateListener) continue;
                    listIterator.remove();
                }
            }
        }

        @Override
        public int getFrameWidth() {
            return NativeMediaPlayer.this.frameWidth;
        }

        @Override
        public int getFrameHeight() {
            return NativeMediaPlayer.this.frameHeight;
        }
    }

    private static class FrameSizeChangedEvent
    extends PlayerEvent {
        private final int width;
        private final int height;

        public FrameSizeChangedEvent(int n, int n2) {
            this.width = n > 0 ? n : 0;
            this.height = n2 > 0 ? n2 : 0;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }
    }

    private static class TrackEvent
    extends PlayerEvent {
        private final Track track;

        TrackEvent(Track track) {
            this.track = track;
        }

        public Track getTrack() {
            return this.track;
        }
    }

    private static class PlayerTimeEvent
    extends PlayerEvent {
        private final double time;

        public PlayerTimeEvent(double d) {
            this.time = d;
        }

        public double getTime() {
            return this.time;
        }
    }

    public static class MediaErrorEvent
    extends PlayerEvent {
        private final Object source;
        private final MediaError error;

        public MediaErrorEvent(Object object, MediaError mediaError) {
            this.source = object;
            this.error = mediaError;
        }

        public Object getSource() {
            return this.source;
        }

        public String getMessage() {
            return this.error.description();
        }

        public int getErrorCode() {
            return this.error.code();
        }
    }

    private static class WarningEvent
    extends PlayerEvent {
        private final Object source;
        private final String message;

        WarningEvent(Object object, String string) {
            this.source = object;
            this.message = string;
        }

        public Object getSource() {
            return this.source;
        }

        public String getMessage() {
            return this.message;
        }
    }
}

