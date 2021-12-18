/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.events;

import com.sun.media.jfxmedia.events.PlayerStateEvent;

public interface PlayerStateListener {
    public void onReady(PlayerStateEvent var1);

    public void onPlaying(PlayerStateEvent var1);

    public void onPause(PlayerStateEvent var1);

    public void onStop(PlayerStateEvent var1);

    public void onStall(PlayerStateEvent var1);

    public void onFinish(PlayerStateEvent var1);

    public void onHalt(PlayerStateEvent var1);
}

