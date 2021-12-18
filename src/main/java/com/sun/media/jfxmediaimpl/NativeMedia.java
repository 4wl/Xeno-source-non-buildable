/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.track.Track;
import com.sun.media.jfxmediaimpl.MarkerStateListener;
import com.sun.media.jfxmediaimpl.platform.Platform;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class NativeMedia
extends Media {
    protected final Lock markerLock = new ReentrantLock();
    protected final Lock listenerLock = new ReentrantLock();
    protected Map<String, Double> markersByName;
    protected NavigableMap<Double, String> markersByTime;
    protected WeakHashMap<MarkerStateListener, Boolean> markerListeners;

    protected NativeMedia(Locator locator) {
        super(locator);
    }

    public abstract Platform getPlatform();

    @Override
    public void addTrack(Track track) {
        super.addTrack(track);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void addMarker(String string, double d) {
        if (string == null) {
            throw new IllegalArgumentException("markerName == null!");
        }
        if (d < 0.0) {
            throw new IllegalArgumentException("presentationTime < 0");
        }
        this.markerLock.lock();
        try {
            if (this.markersByName == null) {
                this.markersByName = new HashMap<String, Double>();
                this.markersByTime = new TreeMap<Double, String>();
            }
            this.markersByName.put(string, d);
            this.markersByTime.put(d, string);
        }
        finally {
            this.markerLock.unlock();
        }
        this.fireMarkerStateEvent(true);
    }

    @Override
    public Map<String, Double> getMarkers() {
        Map<String, Double> map = null;
        this.markerLock.lock();
        try {
            if (this.markersByName != null && !this.markersByName.isEmpty()) {
                map = Collections.unmodifiableMap(this.markersByName);
            }
        }
        finally {
            this.markerLock.unlock();
        }
        return map;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public double removeMarker(String string) {
        if (string == null) {
            throw new IllegalArgumentException("markerName == null!");
        }
        double d = -1.0;
        boolean bl = false;
        this.markerLock.lock();
        try {
            if (this.markersByName.containsKey(string)) {
                d = this.markersByName.get(string);
                this.markersByName.remove(string);
                this.markersByTime.remove(d);
                bl = this.markersByName.size() > 0;
            }
        }
        finally {
            this.markerLock.unlock();
        }
        this.fireMarkerStateEvent(bl);
        return d;
    }

    @Override
    public void removeAllMarkers() {
        this.markerLock.lock();
        try {
            this.markersByName.clear();
            this.markersByTime.clear();
        }
        finally {
            this.markerLock.unlock();
        }
        this.fireMarkerStateEvent(false);
    }

    public abstract void dispose();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    Map.Entry<Double, String> getNextMarker(double d, boolean bl) {
        Map.Entry<Double, String> entry = null;
        this.markerLock.lock();
        try {
            if (this.markersByTime != null) {
                entry = bl ? this.markersByTime.ceilingEntry(d) : this.markersByTime.higherEntry(d);
            }
        }
        finally {
            this.markerLock.unlock();
        }
        return entry;
    }

    void addMarkerStateListener(MarkerStateListener markerStateListener) {
        if (markerStateListener != null) {
            this.listenerLock.lock();
            try {
                if (this.markerListeners == null) {
                    this.markerListeners = new WeakHashMap();
                }
                this.markerListeners.put(markerStateListener, Boolean.TRUE);
            }
            finally {
                this.listenerLock.unlock();
            }
        }
    }

    void removeMarkerStateListener(MarkerStateListener markerStateListener) {
        if (markerStateListener != null) {
            this.listenerLock.lock();
            try {
                if (this.markerListeners != null) {
                    this.markerListeners.remove(markerStateListener);
                }
            }
            finally {
                this.listenerLock.unlock();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void fireMarkerStateEvent(boolean bl) {
        this.listenerLock.lock();
        try {
            if (this.markerListeners != null && !this.markerListeners.isEmpty()) {
                for (MarkerStateListener markerStateListener : this.markerListeners.keySet()) {
                    if (markerStateListener == null) continue;
                    markerStateListener.markerStateChanged(bl);
                }
            }
        }
        finally {
            this.listenerLock.unlock();
        }
    }
}

