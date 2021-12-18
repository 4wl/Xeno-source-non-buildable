/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network.about;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

final class AboutURLConnection
extends URLConnection {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String DEFAULT_MIMETYPE = "text/html";
    private final AboutRecord record = new AboutRecord("");

    AboutURLConnection(URL uRL) {
        super(uRL);
    }

    @Override
    public void connect() throws IOException {
        if (this.connected) {
            return;
        }
        boolean bl = this.connected = this.record != null;
        if (this.connected) {
            this.record.content.reset();
            return;
        }
        throw new ProtocolException("The URL is not valid and cannot be loaded.");
    }

    @Override
    public InputStream getInputStream() throws IOException {
        this.connect();
        return this.record.content;
    }

    @Override
    public String getContentType() {
        try {
            this.connect();
            if (this.record.contentType != null) {
                return this.record.contentType;
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return DEFAULT_MIMETYPE;
    }

    @Override
    public String getContentEncoding() {
        try {
            this.connect();
            if (this.record.contentEncoding != null) {
                return this.record.contentEncoding;
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return DEFAULT_CHARSET;
    }

    @Override
    public int getContentLength() {
        try {
            this.connect();
            return this.record.contentLength;
        }
        catch (IOException iOException) {
            return -1;
        }
    }

    private static final class AboutRecord {
        private final InputStream content;
        private final int contentLength;
        private final String contentEncoding;
        private final String contentType;

        private AboutRecord(String string) {
            byte[] arrby = new byte[]{};
            try {
                arrby = string.getBytes(AboutURLConnection.DEFAULT_CHARSET);
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                // empty catch block
            }
            this.content = new ByteArrayInputStream(arrby);
            this.contentLength = arrby.length;
            this.contentEncoding = AboutURLConnection.DEFAULT_CHARSET;
            this.contentType = AboutURLConnection.DEFAULT_MIMETYPE;
        }
    }
}

