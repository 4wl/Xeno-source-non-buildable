/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.track;

import com.sun.media.jfxmedia.track.Track;
import com.sun.media.jfxmedia.track.VideoResolution;
import java.util.Locale;

public class VideoTrack
extends Track {
    private VideoResolution frameSize;
    private float encodedFrameRate;
    private boolean hasAlphaChannel;

    public VideoTrack(boolean bl, long l, String string, Locale locale, Track.Encoding encoding, VideoResolution videoResolution, float f, boolean bl2) {
        super(bl, l, string, locale, encoding);
        if (videoResolution == null) {
            throw new IllegalArgumentException("frameSize == null!");
        }
        if (videoResolution.width <= 0) {
            throw new IllegalArgumentException("frameSize.width <= 0!");
        }
        if (videoResolution.height <= 0) {
            throw new IllegalArgumentException("frameSize.height <= 0!");
        }
        if (f < 0.0f) {
            throw new IllegalArgumentException("encodedFrameRate < 0.0!");
        }
        this.frameSize = videoResolution;
        this.encodedFrameRate = f;
        this.hasAlphaChannel = bl2;
    }

    public boolean hasAlphaChannel() {
        return this.hasAlphaChannel;
    }

    public float getEncodedFrameRate() {
        return this.encodedFrameRate;
    }

    public VideoResolution getFrameSize() {
        return this.frameSize;
    }

    public final String toString() {
        return "VideoTrack {\n    name: " + this.getName() + "\n" + "    encoding: " + (Object)((Object)this.getEncodingType()) + "\n" + "    frameSize: " + this.frameSize + "\n" + "    encodedFrameRate: " + this.encodedFrameRate + "\n" + "    hasAlphaChannel: " + this.hasAlphaChannel + "\n" + "}";
    }
}

