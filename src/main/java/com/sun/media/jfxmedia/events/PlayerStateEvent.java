/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.events;

import com.sun.media.jfxmedia.events.PlayerEvent;

public class PlayerStateEvent
extends PlayerEvent {
    private PlayerState playerState;
    private double playerTime;
    private String message;

    public PlayerStateEvent(PlayerState playerState, double d) {
        if (playerState == null) {
            throw new IllegalArgumentException("state == null!");
        }
        if (d < 0.0) {
            throw new IllegalArgumentException("time < 0.0!");
        }
        this.playerState = playerState;
        this.playerTime = d;
    }

    public PlayerStateEvent(PlayerState playerState, double d, String string) {
        this(playerState, d);
        this.message = string;
    }

    public PlayerState getState() {
        return this.playerState;
    }

    public double getTime() {
        return this.playerTime;
    }

    public String getMessage() {
        return this.message;
    }

    public static enum PlayerState {
        UNKNOWN,
        READY,
        PLAYING,
        PAUSED,
        STOPPED,
        STALLED,
        FINISHED,
        HALTED;

    }
}

