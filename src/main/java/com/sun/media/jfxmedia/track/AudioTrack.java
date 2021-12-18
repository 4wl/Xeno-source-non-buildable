/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.track;

import com.sun.media.jfxmedia.track.Track;
import java.util.Locale;

public class AudioTrack
extends Track {
    public static final int UNKNOWN = 0;
    public static final int FRONT_LEFT = 1;
    public static final int FRONT_RIGHT = 2;
    public static final int FRONT_CENTER = 4;
    public static final int REAR_LEFT = 8;
    public static final int REAR_RIGHT = 16;
    public static final int REAR_CENTER = 32;
    private int numChannels;
    private int channelMask;
    private float encodedSampleRate;

    public AudioTrack(boolean bl, long l, String string, Locale locale, Track.Encoding encoding, int n, int n2, float f) {
        super(bl, l, string, locale, encoding);
        if (n < 1) {
            throw new IllegalArgumentException("numChannels < 1!");
        }
        if (f <= 0.0f) {
            throw new IllegalArgumentException("encodedSampleRate <= 0.0");
        }
        this.numChannels = n;
        this.channelMask = n2;
        this.encodedSampleRate = f;
    }

    public int getNumChannels() {
        return this.numChannels;
    }

    public int getChannelMask() {
        return this.channelMask;
    }

    public float getEncodedSampleRate() {
        return this.encodedSampleRate;
    }

    public final String toString() {
        return "AudioTrack {\n    name: " + this.getName() + "\n" + "    encoding: " + (Object)((Object)this.getEncodingType()) + "\n" + "    language: " + this.getLocale() + "\n" + "    numChannels: " + this.numChannels + "\n" + "    channelMask: " + this.channelMask + "\n" + "    encodedSampleRate: " + this.encodedSampleRate + "\n" + "}";
    }
}

