/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.stylesheets.MediaList;

public class MediaListImpl
implements MediaList {
    private final long peer;

    MediaListImpl(long l) {
        this.peer = l;
        Disposer.addRecord(this, new SelfDisposer(l));
    }

    static MediaList create(long l) {
        if (l == 0L) {
            return null;
        }
        return new MediaListImpl(l);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object object) {
        return object instanceof MediaListImpl && this.peer == ((MediaListImpl)object).peer;
    }

    public int hashCode() {
        long l = this.peer;
        return (int)(l ^ l >> 17);
    }

    static long getPeer(MediaList mediaList) {
        return mediaList == null ? 0L : ((MediaListImpl)mediaList).getPeer();
    }

    private static native void dispose(long var0);

    static MediaList getImpl(long l) {
        return MediaListImpl.create(l);
    }

    @Override
    public String getMediaText() {
        return MediaListImpl.getMediaTextImpl(this.getPeer());
    }

    static native String getMediaTextImpl(long var0);

    @Override
    public void setMediaText(String string) throws DOMException {
        MediaListImpl.setMediaTextImpl(this.getPeer(), string);
    }

    static native void setMediaTextImpl(long var0, String var2);

    @Override
    public int getLength() {
        return MediaListImpl.getLengthImpl(this.getPeer());
    }

    static native int getLengthImpl(long var0);

    @Override
    public String item(int n) {
        return MediaListImpl.itemImpl(this.getPeer(), n);
    }

    static native String itemImpl(long var0, int var2);

    @Override
    public void deleteMedium(String string) throws DOMException {
        MediaListImpl.deleteMediumImpl(this.getPeer(), string);
    }

    static native void deleteMediumImpl(long var0, String var2);

    @Override
    public void appendMedium(String string) throws DOMException {
        MediaListImpl.appendMediumImpl(this.getPeer(), string);
    }

    static native void appendMediumImpl(long var0, String var2);

    private static class SelfDisposer
    implements DisposerRecord {
        private final long peer;

        SelfDisposer(long l) {
            this.peer = l;
        }

        @Override
        public void dispose() {
            MediaListImpl.dispose(this.peer);
        }
    }
}

