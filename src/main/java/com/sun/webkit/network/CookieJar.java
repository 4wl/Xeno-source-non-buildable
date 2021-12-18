/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class CookieJar {
    private CookieJar() {
    }

    private static void fwkPut(String string, String string2) {
        CookieHandler cookieHandler = CookieHandler.getDefault();
        if (cookieHandler != null) {
            URI uRI = null;
            try {
                uRI = new URI(string);
                uRI = CookieJar.rewriteToFilterOutHttpOnlyCookies(uRI);
            }
            catch (URISyntaxException uRISyntaxException) {
                return;
            }
            HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add(string2);
            hashMap.put("Set-Cookie", arrayList);
            try {
                cookieHandler.put(uRI, hashMap);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    private static String fwkGet(String string, boolean bl) {
        CookieHandler cookieHandler = CookieHandler.getDefault();
        if (cookieHandler != null) {
            URI uRI = null;
            try {
                uRI = new URI(string);
                if (!bl) {
                    uRI = CookieJar.rewriteToFilterOutHttpOnlyCookies(uRI);
                }
            }
            catch (URISyntaxException uRISyntaxException) {
                return null;
            }
            HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();
            Map<String, List<String>> map = null;
            try {
                map = cookieHandler.get(uRI, hashMap);
            }
            catch (IOException iOException) {
                return null;
            }
            if (map != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    String string2 = entry.getKey();
                    if (!"Cookie".equalsIgnoreCase(string2)) continue;
                    for (String string3 : entry.getValue()) {
                        if (stringBuilder.length() > 0) {
                            stringBuilder.append("; ");
                        }
                        stringBuilder.append(string3);
                    }
                }
                return stringBuilder.toString();
            }
        }
        return null;
    }

    private static URI rewriteToFilterOutHttpOnlyCookies(URI uRI) throws URISyntaxException {
        return new URI(uRI.getScheme().equalsIgnoreCase("https") ? "javascripts" : "javascript", uRI.getRawSchemeSpecificPart(), uRI.getRawFragment());
    }
}

