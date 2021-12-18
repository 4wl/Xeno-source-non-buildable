/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.events;

import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.sun.media.jfxmedia.events.PlayerEvent;

public class NewFrameEvent
extends PlayerEvent {
    private VideoDataBuffer frameData;

    public NewFrameEvent(VideoDataBuffer videoDataBuffer) {
        if (videoDataBuffer == null) {
            throw new IllegalArgumentException("buffer == null!");
        }
        this.frameData = videoDataBuffer;
    }

    public VideoDataBuffer getFrameData() {
        return this.frameData;
    }
}

