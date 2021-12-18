/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.MediaManager;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.events.PlayerStateListener;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.NativeMediaAudioClip;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

final class NativeMediaAudioClipPlayer
implements PlayerStateListener,
MediaErrorListener {
    private MediaPlayer mediaPlayer;
    private int playCount;
    private int loopCount;
    private boolean playing;
    private boolean ready;
    private NativeMediaAudioClip sourceClip;
    private double volume;
    private double balance;
    private double pan;
    private double rate;
    private int priority;
    private final ReentrantLock playerStateLock = new ReentrantLock();
    private static final int MAX_PLAYER_COUNT = 16;
    private static final List<NativeMediaAudioClipPlayer> activePlayers = new ArrayList<NativeMediaAudioClipPlayer>(16);
    private static final ReentrantLock playerListLock = new ReentrantLock();
    private static final LinkedBlockingQueue<SchedulerEntry> schedule = new LinkedBlockingQueue();

    public static int getPlayerLimit() {
        return 16;
    }

    public static int getPlayerCount() {
        return activePlayers.size();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void clipScheduler() {
        while (true) {
            Object object;
            SchedulerEntry schedulerEntry = null;
            try {
                schedulerEntry = schedule.take();
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
            if (null == schedulerEntry) continue;
            if (schedulerEntry.getCommand() == 0) {
                object = schedulerEntry.getPlayer();
                if (null != object) {
                    if (NativeMediaAudioClipPlayer.addPlayer((NativeMediaAudioClipPlayer)object)) {
                        ((NativeMediaAudioClipPlayer)object).play();
                    } else {
                        ((NativeMediaAudioClipPlayer)object).sourceClip.playFinished();
                    }
                }
            } else if (schedulerEntry.getCommand() == 1) {
                object = schedulerEntry.getClipURI();
                playerListLock.lock();
                try {
                    NativeMediaAudioClipPlayer[] arrnativeMediaAudioClipPlayer = new NativeMediaAudioClipPlayer[16];
                    arrnativeMediaAudioClipPlayer = activePlayers.toArray(arrnativeMediaAudioClipPlayer);
                    if (null != arrnativeMediaAudioClipPlayer) {
                        for (int i = 0; i < arrnativeMediaAudioClipPlayer.length; ++i) {
                            if (null == arrnativeMediaAudioClipPlayer[i] || null != object && !arrnativeMediaAudioClipPlayer[i].source().getURI().equals(object)) continue;
                            arrnativeMediaAudioClipPlayer[i].invalidate();
                        }
                    }
                }
                finally {
                    playerListLock.unlock();
                }
                boolean bl = null == object;
                for (SchedulerEntry schedulerEntry2 : schedule) {
                    NativeMediaAudioClipPlayer nativeMediaAudioClipPlayer = schedulerEntry2.getPlayer();
                    if (!bl && (null == nativeMediaAudioClipPlayer || !nativeMediaAudioClipPlayer.sourceClip.getLocator().getURI().equals(object))) continue;
                    schedule.remove(schedulerEntry2);
                    nativeMediaAudioClipPlayer.sourceClip.playFinished();
                }
            } else if (schedulerEntry.getCommand() == 2) {
                schedulerEntry.getMediaPlayer().dispose();
            }
            schedulerEntry.signal();
        }
    }

    public static void playClip(NativeMediaAudioClip nativeMediaAudioClip, double d, double d2, double d3, double d4, int n, int n2) {
        Enthreaderator.getSchedulerThread();
        NativeMediaAudioClipPlayer nativeMediaAudioClipPlayer = new NativeMediaAudioClipPlayer(nativeMediaAudioClip, d, d2, d3, d4, n, n2);
        SchedulerEntry schedulerEntry = new SchedulerEntry(nativeMediaAudioClipPlayer);
        boolean bl = schedule.contains(schedulerEntry);
        if (bl || !schedule.offer(schedulerEntry)) {
            if (Logger.canLog(1) && !bl) {
                Logger.logMsg(1, "AudioClip could not be scheduled for playback!");
            }
            nativeMediaAudioClip.playFinished();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static boolean addPlayer(NativeMediaAudioClipPlayer nativeMediaAudioClipPlayer) {
        playerListLock.lock();
        try {
            int n = nativeMediaAudioClipPlayer.priority();
            while (activePlayers.size() >= 16) {
                NativeMediaAudioClipPlayer nativeMediaAudioClipPlayer2 = null;
                for (NativeMediaAudioClipPlayer nativeMediaAudioClipPlayer3 : activePlayers) {
                    if (nativeMediaAudioClipPlayer3.priority() > n || nativeMediaAudioClipPlayer2 != null && (!nativeMediaAudioClipPlayer2.isReady() || nativeMediaAudioClipPlayer3.priority() >= nativeMediaAudioClipPlayer2.priority())) continue;
                    nativeMediaAudioClipPlayer2 = nativeMediaAudioClipPlayer3;
                }
                if (null == nativeMediaAudioClipPlayer2) {
                    boolean bl = false;
                    return bl;
                }
                nativeMediaAudioClipPlayer2.invalidate();
            }
            activePlayers.add(nativeMediaAudioClipPlayer);
        }
        finally {
            playerListLock.unlock();
        }
        return true;
    }

    public static void stopPlayers(Locator locator) {
        CountDownLatch countDownLatch;
        SchedulerEntry schedulerEntry;
        URI uRI;
        URI uRI2 = uRI = locator != null ? locator.getURI() : null;
        if (null != Enthreaderator.getSchedulerThread() && schedule.offer(schedulerEntry = new SchedulerEntry(uRI, countDownLatch = new CountDownLatch(1)))) {
            try {
                countDownLatch.await(5L, TimeUnit.SECONDS);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
    }

    private NativeMediaAudioClipPlayer(NativeMediaAudioClip nativeMediaAudioClip, double d, double d2, double d3, double d4, int n, int n2) {
        this.sourceClip = nativeMediaAudioClip;
        this.volume = d;
        this.balance = d2;
        this.pan = d4;
        this.rate = d3;
        this.loopCount = n;
        this.priority = n2;
        this.ready = false;
    }

    private Locator source() {
        return this.sourceClip.getLocator();
    }

    public double volume() {
        return this.volume;
    }

    public void setVolume(double d) {
        this.volume = d;
    }

    public double balance() {
        return this.balance;
    }

    public void setBalance(double d) {
        this.balance = d;
    }

    public double pan() {
        return this.pan;
    }

    public void setPan(double d) {
        this.pan = d;
    }

    public double playbackRate() {
        return this.rate;
    }

    public void setPlaybackRate(double d) {
        this.rate = d;
    }

    public int priority() {
        return this.priority;
    }

    public void setPriority(int n) {
        this.priority = n;
    }

    public int loopCount() {
        return this.loopCount;
    }

    public void setLoopCount(int n) {
        this.loopCount = n;
    }

    public boolean isPlaying() {
        return this.playing;
    }

    private boolean isReady() {
        return this.ready;
    }

    public synchronized void play() {
        this.playerStateLock.lock();
        try {
            this.playing = true;
            this.playCount = 0;
            if (null == this.mediaPlayer) {
                this.mediaPlayer = MediaManager.getPlayer(this.source());
                this.mediaPlayer.addMediaPlayerListener(this);
                this.mediaPlayer.addMediaErrorListener(this);
            } else {
                this.mediaPlayer.play();
            }
        }
        finally {
            this.playerStateLock.unlock();
        }
    }

    public void stop() {
        this.invalidate();
    }

    public synchronized void invalidate() {
        this.playerStateLock.lock();
        playerListLock.lock();
        try {
            this.playing = false;
            this.playCount = 0;
            this.ready = false;
            activePlayers.remove(this);
            this.sourceClip.playFinished();
            if (null != this.mediaPlayer) {
                this.mediaPlayer.removeMediaPlayerListener(this);
                this.mediaPlayer.setMute(true);
                SchedulerEntry schedulerEntry = new SchedulerEntry(this.mediaPlayer);
                if (!schedule.offer(schedulerEntry)) {
                    this.mediaPlayer.dispose();
                }
                this.mediaPlayer = null;
            }
        }
        catch (Throwable throwable) {
        }
        finally {
            playerListLock.unlock();
            this.playerStateLock.unlock();
        }
    }

    @Override
    public void onReady(PlayerStateEvent playerStateEvent) {
        this.playerStateLock.lock();
        try {
            this.ready = true;
            if (this.playing) {
                this.mediaPlayer.setVolume((float)this.volume);
                this.mediaPlayer.setBalance((float)this.balance);
                this.mediaPlayer.setRate((float)this.rate);
                this.mediaPlayer.play();
            }
        }
        finally {
            this.playerStateLock.unlock();
        }
    }

    @Override
    public void onPlaying(PlayerStateEvent playerStateEvent) {
    }

    @Override
    public void onPause(PlayerStateEvent playerStateEvent) {
    }

    @Override
    public void onStop(PlayerStateEvent playerStateEvent) {
        this.invalidate();
    }

    @Override
    public void onStall(PlayerStateEvent playerStateEvent) {
    }

    @Override
    public void onFinish(PlayerStateEvent playerStateEvent) {
        this.playerStateLock.lock();
        try {
            if (this.playing) {
                if (this.loopCount != -1) {
                    ++this.playCount;
                    if (this.playCount <= this.loopCount) {
                        this.mediaPlayer.seek(0.0);
                    } else {
                        this.invalidate();
                    }
                } else {
                    this.mediaPlayer.seek(0.0);
                }
            }
        }
        finally {
            this.playerStateLock.unlock();
        }
    }

    @Override
    public void onHalt(PlayerStateEvent playerStateEvent) {
        this.invalidate();
    }

    public void onWarning(Object object, String string) {
    }

    @Override
    public void onError(Object object, int n, String string) {
        if (Logger.canLog(4)) {
            Logger.logMsg(4, "Error with AudioClip player: code " + n + " : " + string);
        }
        this.invalidate();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof NativeMediaAudioClipPlayer) {
            URI uRI;
            NativeMediaAudioClipPlayer nativeMediaAudioClipPlayer = (NativeMediaAudioClipPlayer)object;
            URI uRI2 = this.sourceClip.getLocator().getURI();
            return uRI2.equals(uRI = nativeMediaAudioClipPlayer.sourceClip.getLocator().getURI()) && this.priority == nativeMediaAudioClipPlayer.priority && this.loopCount == nativeMediaAudioClipPlayer.loopCount && Double.compare(this.volume, nativeMediaAudioClipPlayer.volume) == 0 && Double.compare(this.balance, nativeMediaAudioClipPlayer.balance) == 0 && Double.compare(this.rate, nativeMediaAudioClipPlayer.rate) == 0 && Double.compare(this.pan, nativeMediaAudioClipPlayer.pan) == 0;
        }
        return false;
    }

    private static class SchedulerEntry {
        private final int command;
        private final NativeMediaAudioClipPlayer player;
        private final URI clipURI;
        private final CountDownLatch commandSignal;
        private final MediaPlayer mediaPlayer;

        public SchedulerEntry(NativeMediaAudioClipPlayer nativeMediaAudioClipPlayer) {
            this.command = 0;
            this.player = nativeMediaAudioClipPlayer;
            this.clipURI = null;
            this.commandSignal = null;
            this.mediaPlayer = null;
        }

        public SchedulerEntry(URI uRI, CountDownLatch countDownLatch) {
            this.command = 1;
            this.player = null;
            this.clipURI = uRI;
            this.commandSignal = countDownLatch;
            this.mediaPlayer = null;
        }

        public SchedulerEntry(MediaPlayer mediaPlayer) {
            this.command = 2;
            this.player = null;
            this.clipURI = null;
            this.commandSignal = null;
            this.mediaPlayer = mediaPlayer;
        }

        public int getCommand() {
            return this.command;
        }

        public NativeMediaAudioClipPlayer getPlayer() {
            return this.player;
        }

        public URI getClipURI() {
            return this.clipURI;
        }

        public MediaPlayer getMediaPlayer() {
            return this.mediaPlayer;
        }

        public void signal() {
            if (null != this.commandSignal) {
                this.commandSignal.countDown();
            }
        }

        public boolean equals(Object object) {
            if (object instanceof SchedulerEntry && null != this.player) {
                return this.player.equals(((SchedulerEntry)object).getPlayer());
            }
            return false;
        }
    }

    private static class Enthreaderator {
        private static final Thread schedulerThread = new Thread(() -> NativeMediaAudioClipPlayer.access$000());

        private Enthreaderator() {
        }

        public static Thread getSchedulerThread() {
            return schedulerThread;
        }

        static {
            schedulerThread.setDaemon(true);
            schedulerThread.start();
        }
    }
}

