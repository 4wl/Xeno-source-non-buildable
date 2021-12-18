/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.track;

import java.util.Locale;

public abstract class Track {
    private boolean trackEnabled;
    private long trackID;
    private String name;
    private Locale locale;
    private Encoding encoding;

    protected Track(boolean bl, long l, String string, Locale locale, Encoding encoding) {
        if (string == null) {
            throw new IllegalArgumentException("name == null!");
        }
        if (encoding == null) {
            throw new IllegalArgumentException("encoding == null!");
        }
        this.trackEnabled = bl;
        this.trackID = l;
        this.locale = locale;
        this.encoding = encoding;
        this.name = string;
    }

    public Encoding getEncodingType() {
        return this.encoding;
    }

    public String getName() {
        return this.name;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public long getTrackID() {
        return this.trackID;
    }

    public boolean isEnabled() {
        return this.trackEnabled;
    }

    public static enum Encoding {
        NONE,
        PCM,
        MPEG1AUDIO,
        MPEG1LAYER3,
        AAC,
        H264,
        VP6,
        CUSTOM;


        public static Encoding toEncoding(int n) {
            for (Encoding encoding : Encoding.values()) {
                if (encoding.ordinal() != n) continue;
                return encoding;
            }
            return NONE;
        }
    }
}

