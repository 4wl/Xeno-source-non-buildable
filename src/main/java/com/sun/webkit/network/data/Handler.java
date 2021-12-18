/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network.data;

import com.sun.webkit.network.data.DataURLConnection;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public final class Handler
extends URLStreamHandler {
    @Override
    protected URLConnection openConnection(URL uRL) throws IOException {
        return new DataURLConnection(uRL);
    }
}

