/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia;

import com.sun.media.jfxmedia.MediaError;

public class MediaException
extends RuntimeException {
    private static final long serialVersionUID = 14L;
    private MediaError error = null;

    public MediaException(String string) {
        super(string);
    }

    public MediaException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public MediaException(String string, Throwable throwable, MediaError mediaError) {
        super(string, throwable);
        this.error = mediaError;
    }

    public MediaError getMediaError() {
        return this.error;
    }
}

