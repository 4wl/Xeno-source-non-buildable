/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network.about;

import com.sun.webkit.network.about.AboutURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public final class Handler
extends URLStreamHandler {
    @Override
    protected URLConnection openConnection(URL uRL) {
        return new AboutURLConnection(uRL);
    }
}

