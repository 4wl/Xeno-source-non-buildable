/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

import com.sun.webkit.network.Cookie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

final class CookieStore {
    private static final Logger logger = Logger.getLogger(CookieStore.class.getName());
    private static final int MAX_BUCKET_SIZE = 50;
    private static final int TOTAL_COUNT_LOWER_THRESHOLD = 3000;
    private static final int TOTAL_COUNT_UPPER_THRESHOLD = 4000;
    private final Map<String, Map<Cookie, Cookie>> buckets = new HashMap<String, Map<Cookie, Cookie>>();
    private int totalCount = 0;

    CookieStore() {
    }

    Cookie get(Cookie cookie) {
        Map<Cookie, Cookie> map = this.buckets.get(cookie.getDomain());
        if (map == null) {
            return null;
        }
        Cookie cookie2 = map.get(cookie);
        if (cookie2 == null) {
            return null;
        }
        if (cookie2.hasExpired()) {
            map.remove(cookie2);
            --this.totalCount;
            this.log("Expired cookie removed by get", cookie2, map);
            return null;
        }
        return cookie2;
    }

    List<Cookie> get(String string, String string2, boolean bl, boolean bl2) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "hostname: [{0}], path: [{1}], secureProtocol: [{2}], httpApi: [{3}]", new Object[]{string, string2, bl, bl2});
        }
        ArrayList<Cookie> arrayList = new ArrayList<Cookie>();
        String string3 = string;
        while (string3.length() > 0) {
            int n;
            Map<Cookie, Cookie> map = this.buckets.get(string3);
            if (map != null) {
                this.find(arrayList, map, string, string2, bl, bl2);
            }
            if ((n = string3.indexOf(46)) == -1) break;
            string3 = string3.substring(n + 1);
        }
        Collections.sort(arrayList, new GetComparator());
        long l = System.currentTimeMillis();
        for (Cookie cookie : arrayList) {
            cookie.setLastAccessTime(l);
        }
        logger.log(Level.FINEST, "result: {0}", arrayList);
        return arrayList;
    }

    private void find(List<Cookie> list, Map<Cookie, Cookie> map, String string, String string2, boolean bl, boolean bl2) {
        Iterator<Cookie> iterator = map.values().iterator();
        while (iterator.hasNext()) {
            Cookie cookie = iterator.next();
            if (cookie.hasExpired()) {
                iterator.remove();
                --this.totalCount;
                this.log("Expired cookie removed by find", cookie, map);
                continue;
            }
            if (!cookie.getHostOnly() ? !Cookie.domainMatches(string, cookie.getDomain()) : !string.equalsIgnoreCase(cookie.getDomain())) continue;
            if (!Cookie.pathMatches(string2, cookie.getPath()) || cookie.getSecureOnly() && !bl || cookie.getHttpOnly() && !bl2) continue;
            list.add(cookie);
        }
    }

    void put(Cookie cookie) {
        Map<Cookie, Cookie> map = this.buckets.get(cookie.getDomain());
        if (map == null) {
            map = new LinkedHashMap<Cookie, Cookie>(20);
            this.buckets.put(cookie.getDomain(), map);
        }
        if (cookie.hasExpired()) {
            this.log("Cookie expired", cookie, map);
            if (map.remove(cookie) != null) {
                --this.totalCount;
                this.log("Expired cookie removed by put", cookie, map);
            }
        } else if (map.put(cookie, cookie) == null) {
            ++this.totalCount;
            this.log("Cookie added", cookie, map);
            if (map.size() > 50) {
                this.purge(map);
            }
            if (this.totalCount > 4000) {
                this.purge();
            }
        } else {
            this.log("Cookie updated", cookie, map);
        }
    }

    private void purge(Map<Cookie, Cookie> map) {
        logger.log(Level.FINEST, "Purging bucket: {0}", map.values());
        Cookie cookie = null;
        Iterator<Cookie> iterator = map.values().iterator();
        while (iterator.hasNext()) {
            Cookie cookie2 = iterator.next();
            if (cookie2.hasExpired()) {
                iterator.remove();
                --this.totalCount;
                this.log("Expired cookie removed", cookie2, map);
                continue;
            }
            if (cookie != null && cookie2.getLastAccessTime() >= cookie.getLastAccessTime()) continue;
            cookie = cookie2;
        }
        if (map.size() > 50) {
            map.remove(cookie);
            --this.totalCount;
            this.log("Excess cookie removed", cookie, map);
        }
    }

    private void purge() {
        logger.log(Level.FINEST, "Purging store");
        PriorityQueue<Cookie> priorityQueue = new PriorityQueue<Cookie>(this.totalCount / 2, new RemovalComparator());
        for (Map.Entry<String, Map<Cookie, Cookie>> map : this.buckets.entrySet()) {
            Map<Cookie, Cookie> map2 = map.getValue();
            Iterator<Cookie> iterator = map2.values().iterator();
            while (iterator.hasNext()) {
                Cookie cookie = iterator.next();
                if (cookie.hasExpired()) {
                    iterator.remove();
                    --this.totalCount;
                    this.log("Expired cookie removed", cookie, map2);
                    continue;
                }
                priorityQueue.add(cookie);
            }
        }
        while (this.totalCount > 3000) {
            Cookie cookie = (Cookie)priorityQueue.remove();
            Map<Cookie, Cookie> map = this.buckets.get(cookie.getDomain());
            if (map == null) continue;
            map.remove(cookie);
            --this.totalCount;
            this.log("Excess cookie removed", cookie, map);
        }
    }

    private void log(String string, Cookie cookie, Map<Cookie, Cookie> map) {
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "{0}: {1}, bucket size: {2}, total count: {3}", new Object[]{string, cookie, map.size(), this.totalCount});
        }
    }

    private static final class RemovalComparator
    implements Comparator<Cookie> {
        private RemovalComparator() {
        }

        @Override
        public int compare(Cookie cookie, Cookie cookie2) {
            return (int)(cookie.getLastAccessTime() - cookie2.getLastAccessTime());
        }
    }

    private static final class GetComparator
    implements Comparator<Cookie> {
        private GetComparator() {
        }

        @Override
        public int compare(Cookie cookie, Cookie cookie2) {
            int n = cookie2.getPath().length() - cookie.getPath().length();
            if (n != 0) {
                return n;
            }
            return cookie.getCreationTime().compareTo(cookie2.getCreationTime());
        }
    }
}

