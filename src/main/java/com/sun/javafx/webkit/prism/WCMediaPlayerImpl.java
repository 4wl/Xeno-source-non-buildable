/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.prism;

import com.sun.javafx.media.PrismMediaFrameHandler;
import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaManager;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.sun.media.jfxmedia.events.BufferListener;
import com.sun.media.jfxmedia.events.BufferProgressEvent;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.events.NewFrameEvent;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.events.PlayerStateListener;
import com.sun.media.jfxmedia.events.PlayerTimeListener;
import com.sun.media.jfxmedia.events.VideoRendererListener;
import com.sun.media.jfxmedia.events.VideoTrackSizeListener;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.track.AudioTrack;
import com.sun.media.jfxmedia.track.Track;
import com.sun.media.jfxmedia.track.VideoTrack;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCMediaPlayer;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;

final class WCMediaPlayerImpl
extends WCMediaPlayer
implements PlayerStateListener,
MediaErrorListener,
VideoTrackSizeListener,
BufferListener,
PlayerTimeListener {
    private final Object lock = new Object();
    private volatile MediaPlayer player;
    private volatile CreateThread createThread;
    private volatile PrismMediaFrameHandler frameHandler;
    private final MediaFrameListener frameListener = new MediaFrameListener();
    private boolean gotFirstFrame = false;
    private int finished = 0;
    private float bufferedStart = 0.0f;
    private float bufferedEnd = 0.0f;
    private boolean buffering = false;

    WCMediaPlayerImpl() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private MediaPlayer getPlayer() {
        Object object = this.lock;
        synchronized (object) {
            if (this.createThread != null) {
                return null;
            }
            return this.player;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void setPlayer(MediaPlayer mediaPlayer) {
        Object object = this.lock;
        synchronized (object) {
            this.player = mediaPlayer;
            this.installListeners();
            this.frameHandler = PrismMediaFrameHandler.getHandler(this.player);
        }
        this.finished = 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void load(String string, String string2) {
        Object object = this.lock;
        synchronized (object) {
            if (this.createThread != null) {
                this.createThread.cancel();
            }
            this.disposePlayer();
            this.createThread = new CreateThread(string, string2);
        }
        if (this.getPreload() != 0) {
            this.createThread.start();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void cancelLoad() {
        Object object = this.lock;
        synchronized (object) {
            if (this.createThread != null) {
                this.createThread.cancel();
            }
        }
        object = this.getPlayer();
        if (object != null) {
            object.stop();
        }
        this.notifyNetworkStateChanged(0);
        this.notifyReadyStateChanged(0);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void disposePlayer() {
        MediaPlayer mediaPlayer;
        Object object = this.lock;
        synchronized (object) {
            this.removeListeners();
            mediaPlayer = this.player;
            this.player = null;
            if (this.frameHandler != null) {
                this.frameHandler.releaseTextures();
                this.frameHandler = null;
            }
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
            if (this.frameListener != null) {
                this.frameListener.releaseVideoFrames();
            }
        }
    }

    private void installListeners() {
        if (null != this.player) {
            this.player.addMediaPlayerListener(this);
            this.player.addMediaErrorListener(this);
            this.player.addVideoTrackSizeListener(this);
            this.player.addBufferListener(this);
            this.player.getVideoRenderControl().addVideoRendererListener(this.frameListener);
        }
    }

    private void removeListeners() {
        if (null != this.player) {
            this.player.removeMediaPlayerListener(this);
            this.player.removeMediaErrorListener(this);
            this.player.removeVideoTrackSizeListener(this);
            this.player.removeBufferListener(this);
            this.player.getVideoRenderControl().removeVideoRendererListener(this.frameListener);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void prepareToPlay() {
        Object object = this.lock;
        synchronized (object) {
            CreateThread createThread;
            if (this.player == null && (createThread = this.createThread) != null && createThread.getState() == Thread.State.NEW) {
                createThread.start();
            }
        }
    }

    @Override
    protected void play() {
        MediaPlayer mediaPlayer = this.getPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.play();
            this.notifyPaused(false);
        }
    }

    @Override
    protected void pause() {
        MediaPlayer mediaPlayer = this.getPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            this.notifyPaused(true);
        }
    }

    @Override
    protected float getCurrentTime() {
        MediaPlayer mediaPlayer = this.getPlayer();
        if (mediaPlayer == null) {
            return 0.0f;
        }
        return this.finished == 0 ? (float)mediaPlayer.getPresentationTime() : (this.finished > 0 ? (float)mediaPlayer.getDuration() : 0.0f);
    }

    @Override
    protected void seek(float f) {
        MediaPlayer mediaPlayer = this.getPlayer();
        if (mediaPlayer != null) {
            this.finished = 0;
            if (this.getReadyState() >= 1) {
                this.notifySeeking(true, 1);
            } else {
                this.notifySeeking(true, 0);
            }
            mediaPlayer.seek(f);
            final float f2 = f;
            Thread thread = new Thread(new Runnable(){

                @Override
                public void run() {
                    MediaPlayer mediaPlayer;
                    while (WCMediaPlayerImpl.this.isSeeking() && (mediaPlayer = WCMediaPlayerImpl.this.getPlayer()) != null) {
                        double d = mediaPlayer.getPresentationTime();
                        if ((double)f2 < 0.01 || Math.abs(d) >= 0.01) {
                            WCMediaPlayerImpl.this.notifySeeking(false, 4);
                            break;
                        }
                        try {
                            Thread.sleep(10L);
                        }
                        catch (InterruptedException interruptedException) {}
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    @Override
    protected void setRate(float f) {
        MediaPlayer mediaPlayer = this.getPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.setRate(f);
        }
    }

    @Override
    protected void setVolume(float f) {
        MediaPlayer mediaPlayer = this.getPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(f);
        }
    }

    @Override
    protected void setMute(boolean bl) {
        MediaPlayer mediaPlayer = this.getPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.setMute(bl);
        }
    }

    @Override
    protected void setSize(int n, int n2) {
    }

    @Override
    protected void setPreservesPitch(boolean bl) {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void renderCurrentFrame(WCGraphicsContext wCGraphicsContext, int n, int n2, int n3, int n4) {
        Object object = this.lock;
        synchronized (object) {
            this.renderImpl(wCGraphicsContext, n, n2, n3, n4);
        }
    }

    private void renderImpl(WCGraphicsContext wCGraphicsContext, int n, int n2, int n3, int n4) {
        if (verbose) {
            log.log(Level.FINER, ">>(Prism)renderImpl");
        }
        Graphics graphics = (Graphics)wCGraphicsContext.getPlatformGraphics();
        Texture texture = null;
        VideoDataBuffer videoDataBuffer = this.frameListener.getLatestFrame();
        if (null != videoDataBuffer) {
            if (null != this.frameHandler) {
                texture = this.frameHandler.getTexture(graphics, videoDataBuffer);
            }
            videoDataBuffer.releaseFrame();
        }
        if (texture != null) {
            graphics.drawTexture(texture, n, n2, n + n3, n2 + n4, 0.0f, 0.0f, texture.getContentWidth(), texture.getContentHeight());
            texture.unlock();
        } else {
            if (verbose) {
                log.log(Level.FINEST, "  (Prism)renderImpl, texture is null, draw black rect");
            }
            wCGraphicsContext.fillRect(n, n2, n3, n4, -16777216);
        }
        if (verbose) {
            log.log(Level.FINER, "<<(Prism)renderImpl");
        }
    }

    @Override
    public void onReady(PlayerStateEvent playerStateEvent) {
        MediaPlayer mediaPlayer = this.getPlayer();
        if (verbose) {
            log.log(Level.FINE, "onReady");
        }
        Media media = mediaPlayer.getMedia();
        boolean bl = false;
        boolean bl2 = false;
        if (media != null) {
            List<Track> list = media.getTracks();
            if (list != null) {
                if (verbose) {
                    log.log(Level.INFO, "{0} track(s) detected:", list.size());
                }
                for (Track track : list) {
                    if (track instanceof VideoTrack) {
                        bl = true;
                    } else if (track instanceof AudioTrack) {
                        bl2 = true;
                    }
                    if (!verbose) continue;
                    log.log(Level.INFO, "track: {0}", track);
                }
            } else if (verbose) {
                log.log(Level.WARNING, "onReady, tracks IS NULL");
            }
        } else if (verbose) {
            log.log(Level.WARNING, "onReady, media IS NULL");
        }
        if (verbose) {
            log.log(Level.FINE, "onReady, hasVideo:{0}, hasAudio: {1}", new Object[]{bl, bl2});
        }
        this.notifyReady(bl, bl2, (float)mediaPlayer.getDuration());
        if (!bl) {
            this.notifyReadyStateChanged(4);
        } else if (this.getReadyState() < 1) {
            if (this.gotFirstFrame) {
                this.notifyReadyStateChanged(4);
            } else {
                this.notifyReadyStateChanged(1);
            }
        }
    }

    @Override
    public void onPlaying(PlayerStateEvent playerStateEvent) {
        if (verbose) {
            log.log(Level.FINE, "onPlaying");
        }
        this.notifyPaused(false);
    }

    @Override
    public void onPause(PlayerStateEvent playerStateEvent) {
        if (verbose) {
            log.log(Level.FINE, "onPause, time: {0}", playerStateEvent.getTime());
        }
        this.notifyPaused(true);
    }

    @Override
    public void onStop(PlayerStateEvent playerStateEvent) {
        if (verbose) {
            log.log(Level.FINE, "onStop");
        }
        this.notifyPaused(true);
    }

    @Override
    public void onStall(PlayerStateEvent playerStateEvent) {
        if (verbose) {
            log.log(Level.FINE, "onStall");
        }
    }

    @Override
    public void onFinish(PlayerStateEvent playerStateEvent) {
        MediaPlayer mediaPlayer = this.getPlayer();
        if (mediaPlayer != null) {
            int n = this.finished = mediaPlayer.getRate() > 0.0f ? 1 : -1;
            if (verbose) {
                log.log(Level.FINE, "onFinish, time: {0}", playerStateEvent.getTime());
            }
            this.notifyFinished();
        }
    }

    @Override
    public void onHalt(PlayerStateEvent playerStateEvent) {
        if (verbose) {
            log.log(Level.FINE, "onHalt");
        }
    }

    @Override
    public void onError(Object object, int n, String string) {
        if (verbose) {
            log.log(Level.WARNING, "onError, errCode={0}, msg={1}", new Object[]{n, string});
        }
        this.notifyNetworkStateChanged(5);
        this.notifyReadyStateChanged(0);
    }

    @Override
    public void onDurationChanged(double d) {
        if (verbose) {
            log.log(Level.FINE, "onDurationChanged, duration={0}", d);
        }
        this.notifyDurationChanged((float)d);
    }

    @Override
    public void onSizeChanged(int n, int n2) {
        if (verbose) {
            log.log(Level.FINE, "onSizeChanged, new size = {0} x {1}", new Object[]{n, n2});
        }
        this.notifySizeChanged(n, n2);
    }

    private void notifyFrameArrived() {
        if (!this.gotFirstFrame) {
            if (this.getReadyState() >= 1) {
                this.notifyReadyStateChanged(4);
            }
            this.gotFirstFrame = true;
        }
        if (verbose && this.finished != 0) {
            log.log(Level.FINE, "notifyFrameArrived (after finished) time: {0}", this.getPlayer().getPresentationTime());
        }
        this.notifyNewFrame();
    }

    private void updateBufferingStatus() {
        int n;
        int n2 = this.buffering ? 2 : (n = this.bufferedStart > 0.0f ? 1 : 3);
        if (verbose) {
            log.log(Level.FINE, "updateBufferingStatus, buffered: [{0} - {1}], buffering = {2}", new Object[]{Float.valueOf(this.bufferedStart), Float.valueOf(this.bufferedEnd), this.buffering});
        }
        this.notifyNetworkStateChanged(n);
    }

    @Override
    public void onBufferProgress(BufferProgressEvent bufferProgressEvent) {
        if (bufferProgressEvent.getDuration() < 0.0) {
            return;
        }
        double d = bufferProgressEvent.getDuration() / (double)bufferProgressEvent.getBufferStop();
        this.bufferedStart = (float)(d * (double)bufferProgressEvent.getBufferStart());
        this.bufferedEnd = (float)(d * (double)bufferProgressEvent.getBufferPosition());
        this.buffering = bufferProgressEvent.getBufferPosition() < bufferProgressEvent.getBufferStop();
        float[] arrf = new float[]{this.bufferedStart, this.bufferedEnd};
        int n = (int)(bufferProgressEvent.getBufferPosition() - bufferProgressEvent.getBufferStart());
        if (verbose) {
            log.log(Level.FINER, "onBufferProgress, bufferStart={0}, bufferStop={1}, bufferPos={2}, duration={3}; notify range [{4},[5]], bytesLoaded: {6}", new Object[]{bufferProgressEvent.getBufferStart(), bufferProgressEvent.getBufferStop(), bufferProgressEvent.getBufferPosition(), bufferProgressEvent.getDuration(), Float.valueOf(arrf[0]), Float.valueOf(arrf[1]), n});
        }
        this.notifyBufferChanged(arrf, n);
        this.updateBufferingStatus();
    }

    private final class MediaFrameListener
    implements VideoRendererListener {
        private final Object frameLock = new Object();
        private VideoDataBuffer currentFrame;
        private VideoDataBuffer nextFrame;

        private MediaFrameListener() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void videoFrameUpdated(NewFrameEvent newFrameEvent) {
            Object object = this.frameLock;
            synchronized (object) {
                if (null != this.nextFrame) {
                    this.nextFrame.releaseFrame();
                }
                this.nextFrame = newFrameEvent.getFrameData();
                if (null != this.nextFrame) {
                    this.nextFrame.holdFrame();
                }
            }
            WCMediaPlayerImpl.this.notifyFrameArrived();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void releaseVideoFrames() {
            Object object = this.frameLock;
            synchronized (object) {
                if (null != this.nextFrame) {
                    this.nextFrame.releaseFrame();
                    this.nextFrame = null;
                }
                if (null != this.currentFrame) {
                    this.currentFrame.releaseFrame();
                    this.currentFrame = null;
                }
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public VideoDataBuffer getLatestFrame() {
            Object object = this.frameLock;
            synchronized (object) {
                if (null != this.nextFrame) {
                    if (null != this.currentFrame) {
                        this.currentFrame.releaseFrame();
                    }
                    this.currentFrame = this.nextFrame;
                    this.nextFrame = null;
                }
                if (null != this.currentFrame) {
                    this.currentFrame.holdFrame();
                }
                return this.currentFrame;
            }
        }
    }

    private final class CreateThread
    extends Thread {
        private boolean cancelled = false;
        private final String url;
        private final String userAgent;

        private CreateThread(String string, String string2) {
            this.url = string;
            this.userAgent = string2;
            WCMediaPlayerImpl.this.gotFirstFrame = false;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            Object object;
            if (verbose) {
                log.log(Level.FINE, "CreateThread: started, url={0}", this.url);
            }
            WCMediaPlayerImpl.this.notifyNetworkStateChanged(2);
            WCMediaPlayerImpl.this.notifyReadyStateChanged(0);
            MediaPlayer mediaPlayer = null;
            try {
                object = new Locator(new URI(this.url));
                if (this.userAgent != null) {
                    ((Locator)object).setConnectionProperty("User-Agent", this.userAgent);
                }
                ((Locator)object).init();
                if (verbose) {
                    log.fine("CreateThread: locator created");
                }
                mediaPlayer = MediaManager.getPlayer((Locator)object);
            }
            catch (Exception exception) {
                if (verbose) {
                    log.log(Level.WARNING, "CreateThread ERROR: {0}", exception.toString());
                    exception.printStackTrace(System.out);
                }
                WCMediaPlayerImpl.this.onError(this, 0, exception.getMessage());
                return;
            }
            object = WCMediaPlayerImpl.this.lock;
            synchronized (object) {
                if (this.cancelled) {
                    if (verbose) {
                        log.log(Level.FINE, "CreateThread: cancelled");
                    }
                    mediaPlayer.dispose();
                    return;
                }
                WCMediaPlayerImpl.this.createThread = null;
                WCMediaPlayerImpl.this.setPlayer(mediaPlayer);
            }
            if (verbose) {
                log.log(Level.FINE, "CreateThread: completed");
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void cancel() {
            Object object = WCMediaPlayerImpl.this.lock;
            synchronized (object) {
                this.cancelled = true;
            }
        }
    }
}

