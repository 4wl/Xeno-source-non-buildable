/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia;

import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.track.Track;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class Media {
    private Locator locator;
    private final List<Track> tracks = new ArrayList<Track>();

    protected Media(Locator locator) {
        if (locator == null) {
            throw new IllegalArgumentException("locator == null!");
        }
        this.locator = locator;
    }

    public abstract void addMarker(String var1, double var2);

    public abstract double removeMarker(String var1);

    public abstract void removeAllMarkers();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<Track> getTracks() {
        List<Track> list;
        List<Track> list2 = this.tracks;
        synchronized (list2) {
            list = this.tracks.isEmpty() ? null : Collections.unmodifiableList(new ArrayList<Track>(this.tracks));
        }
        return list;
    }

    public abstract Map<String, Double> getMarkers();

    public Locator getLocator() {
        return this.locator;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void addTrack(Track track) {
        if (track == null) {
            throw new IllegalArgumentException("track == null!");
        }
        List<Track> list = this.tracks;
        synchronized (list) {
            this.tracks.add(track);
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.tracks != null && !this.tracks.isEmpty()) {
            for (Track track : this.tracks) {
                stringBuffer.append(track);
                stringBuffer.append("\n");
            }
        }
        return stringBuffer.toString();
    }
}

