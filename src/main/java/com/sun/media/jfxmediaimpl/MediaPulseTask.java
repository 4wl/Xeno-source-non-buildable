/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import java.lang.ref.WeakReference;
import java.util.TimerTask;

class MediaPulseTask
extends TimerTask {
    WeakReference<NativeMediaPlayer> playerRef;

    MediaPulseTask(NativeMediaPlayer nativeMediaPlayer) {
        this.playerRef = new WeakReference<NativeMediaPlayer>(nativeMediaPlayer);
    }

    @Override
    public void run() {
        NativeMediaPlayer nativeMediaPlayer = (NativeMediaPlayer)this.playerRef.get();
        if (nativeMediaPlayer != null) {
            if (!nativeMediaPlayer.doMediaPulseTask()) {
                this.cancel();
            }
        } else {
            this.cancel();
        }
    }
}

