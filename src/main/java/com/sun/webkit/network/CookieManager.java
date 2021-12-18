/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

import com.sun.webkit.network.Cookie;
import com.sun.webkit.network.CookieStore;
import com.sun.webkit.network.ExtendedTime;
import com.sun.webkit.network.PublicSuffixes;
import java.net.CookieHandler;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class CookieManager
extends CookieHandler {
    private static final Logger logger = Logger.getLogger(CookieManager.class.getName());
    private final CookieStore store = new CookieStore();

    @Override
    public Map<String, List<String>> get(URI uRI, Map<String, List<String>> map) {
        Map<String, List<String>> map2;
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "uri: [{0}], requestHeaders: {1}", new Object[]{uRI, CookieManager.toLogString(map)});
        }
        if (uRI == null) {
            throw new IllegalArgumentException("uri is null");
        }
        if (map == null) {
            throw new IllegalArgumentException("requestHeaders is null");
        }
        String string = this.get(uRI);
        if (string != null) {
            map2 = new HashMap();
            map2.put("Cookie", Arrays.asList(string));
        } else {
            map2 = Collections.emptyMap();
        }
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "result: {0}", CookieManager.toLogString(map2));
        }
        return map2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private String get(URI uRI) {
        List<Cookie> list;
        String string = uRI.getHost();
        if (string == null || string.length() == 0) {
            logger.log(Level.FINEST, "Null or empty URI host, returning null");
            return null;
        }
        string = CookieManager.canonicalize(string);
        String string2 = uRI.getScheme();
        boolean bl = "https".equalsIgnoreCase(string2) || "javascripts".equalsIgnoreCase(string2);
        boolean bl2 = "http".equalsIgnoreCase(string2) || "https".equalsIgnoreCase(string2);
        Object object = this.store;
        synchronized (object) {
            list = this.store.get(string, uRI.getPath(), bl, bl2);
        }
        object = new StringBuilder();
        for (Cookie cookie : list) {
            if (((StringBuilder)object).length() > 0) {
                ((StringBuilder)object).append("; ");
            }
            ((StringBuilder)object).append(cookie.getName());
            ((StringBuilder)object).append('=');
            ((StringBuilder)object).append(cookie.getValue());
        }
        return ((StringBuilder)object).length() > 0 ? ((StringBuilder)object).toString() : null;
    }

    @Override
    public void put(URI uRI, Map<String, List<String>> map) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "uri: [{0}], responseHeaders: {1}", new Object[]{uRI, CookieManager.toLogString(map)});
        }
        if (uRI == null) {
            throw new IllegalArgumentException("uri is null");
        }
        if (map == null) {
            throw new IllegalArgumentException("responseHeaders is null");
        }
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String string = entry.getKey();
            if (!"Set-Cookie".equalsIgnoreCase(string)) continue;
            ExtendedTime extendedTime = ExtendedTime.currentTime();
            ListIterator<String> listIterator = entry.getValue().listIterator(entry.getValue().size());
            while (listIterator.hasPrevious()) {
                Cookie cookie = Cookie.parse(listIterator.previous(), extendedTime);
                if (cookie == null) continue;
                this.put(uRI, cookie);
                extendedTime = extendedTime.incrementSubtime();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void put(URI uRI, Cookie cookie) {
        boolean bl;
        logger.log(Level.FINEST, "cookie: {0}", cookie);
        String string = uRI.getHost();
        if (string == null || string.length() == 0) {
            logger.log(Level.FINEST, "Null or empty URI host, ignoring cookie");
            return;
        }
        string = CookieManager.canonicalize(string);
        if (PublicSuffixes.isPublicSuffix(cookie.getDomain())) {
            if (cookie.getDomain().equals(string)) {
                cookie.setDomain("");
            } else {
                logger.log(Level.FINEST, "Domain is public suffix, ignoring cookie");
                return;
            }
        }
        if (cookie.getDomain().length() > 0) {
            if (!Cookie.domainMatches(string, cookie.getDomain())) {
                logger.log(Level.FINEST, "Hostname does not match domain, ignoring cookie");
                return;
            }
            cookie.setHostOnly(false);
        } else {
            cookie.setHostOnly(true);
            cookie.setDomain(string);
        }
        if (cookie.getPath() == null) {
            cookie.setPath(Cookie.defaultPath(uRI));
        }
        boolean bl2 = bl = "http".equalsIgnoreCase(uRI.getScheme()) || "https".equalsIgnoreCase(uRI.getScheme());
        if (cookie.getHttpOnly() && !bl) {
            logger.log(Level.FINEST, "HttpOnly cookie received from non-HTTP API, ignoring cookie");
            return;
        }
        CookieStore cookieStore = this.store;
        synchronized (cookieStore) {
            Cookie cookie2 = this.store.get(cookie);
            if (cookie2 != null) {
                if (cookie2.getHttpOnly() && !bl) {
                    logger.log(Level.FINEST, "Non-HTTP API attempts to overwrite HttpOnly cookie, blocked");
                    return;
                }
                cookie.setCreationTime(cookie2.getCreationTime());
            }
            this.store.put(cookie);
        }
        logger.log(Level.FINEST, "Stored: {0}", cookie);
    }

    private static String toLogString(Map<String, List<String>> map) {
        if (map == null) {
            return null;
        }
        if (map.isEmpty()) {
            return "{}";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String string = entry.getKey();
            for (String string2 : entry.getValue()) {
                stringBuilder.append(String.format("%n    ", new Object[0]));
                stringBuilder.append(string);
                stringBuilder.append(": ");
                stringBuilder.append(string2);
            }
        }
        return stringBuilder.toString();
    }

    private static String canonicalize(String string) {
        return string.toLowerCase();
    }
}

