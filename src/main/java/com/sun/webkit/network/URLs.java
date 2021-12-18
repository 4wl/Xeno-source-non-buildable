/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

import com.sun.webkit.network.about.Handler;
import java.net.MalformedURLException;
import java.net.NetPermission;
import java.net.URL;
import java.net.URLStreamHandler;
import java.security.AccessController;
import java.security.Permission;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class URLs {
    private static final Map<String, URLStreamHandler> handlerMap;
    private static final Permission streamHandlerPermission;

    private URLs() {
        throw new AssertionError();
    }

    public static URL newURL(String string) throws MalformedURLException {
        return URLs.newURL(null, string);
    }

    public static URL newURL(URL uRL, String string) throws MalformedURLException {
        try {
            return new URL(uRL, string);
        }
        catch (MalformedURLException malformedURLException) {
            URLStreamHandler uRLStreamHandler;
            int n = string.indexOf(58);
            URLStreamHandler uRLStreamHandler2 = uRLStreamHandler = n != -1 ? handlerMap.get(string.substring(0, n).toLowerCase()) : null;
            if (uRLStreamHandler == null) {
                throw malformedURLException;
            }
            try {
                return AccessController.doPrivileged(() -> {
                    try {
                        return new URL(uRL, string, uRLStreamHandler);
                    }
                    catch (MalformedURLException malformedURLException) {
                        throw new RuntimeException(malformedURLException);
                    }
                }, null, streamHandlerPermission);
            }
            catch (RuntimeException runtimeException) {
                if (runtimeException.getCause() instanceof MalformedURLException) {
                    throw (MalformedURLException)runtimeException.getCause();
                }
                throw runtimeException;
            }
        }
    }

    static {
        HashMap<String, URLStreamHandler> hashMap = new HashMap<String, URLStreamHandler>(2);
        hashMap.put("about", new Handler());
        hashMap.put("data", new com.sun.webkit.network.data.Handler());
        handlerMap = Collections.unmodifiableMap(hashMap);
        streamHandlerPermission = new NetPermission("specifyStreamHandler");
    }
}

