/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

public interface LoadListenerClient {
    public static final int PAGE_STARTED = 0;
    public static final int PAGE_FINISHED = 1;
    public static final int PAGE_REDIRECTED = 2;
    public static final int LOAD_FAILED = 5;
    public static final int LOAD_STOPPED = 6;
    public static final int CONTENT_RECEIVED = 10;
    public static final int TITLE_RECEIVED = 11;
    public static final int ICON_RECEIVED = 12;
    public static final int CONTENTTYPE_RECEIVED = 13;
    public static final int DOCUMENT_AVAILABLE = 14;
    public static final int RESOURCE_STARTED = 20;
    public static final int RESOURCE_REDIRECTED = 21;
    public static final int RESOURCE_FINISHED = 22;
    public static final int RESOURCE_FAILED = 23;
    public static final int PROGRESS_CHANGED = 30;
    public static final int UNKNOWN_HOST = 1;
    public static final int MALFORMED_URL = 2;
    public static final int SSL_HANDSHAKE = 3;
    public static final int CONNECTION_REFUSED = 4;
    public static final int CONNECTION_RESET = 5;
    public static final int NO_ROUTE_TO_HOST = 6;
    public static final int CONNECTION_TIMED_OUT = 7;
    public static final int PERMISSION_DENIED = 8;
    public static final int INVALID_RESPONSE = 9;
    public static final int TOO_MANY_REDIRECTS = 10;
    public static final int FILE_NOT_FOUND = 11;
    public static final int UNKNOWN_ERROR = 99;

    public void dispatchLoadEvent(long var1, int var3, String var4, String var5, double var6, int var8);

    public void dispatchResourceLoadEvent(long var1, int var3, String var4, String var5, double var6, int var8);
}

