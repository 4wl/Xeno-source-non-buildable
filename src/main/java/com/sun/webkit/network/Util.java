/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

import com.sun.webkit.network.URLs;
import java.net.MalformedURLException;

public final class Util {
    private Util() {
        throw new AssertionError();
    }

    public static String adjustUrlForWebKit(String string) throws MalformedURLException {
        int n;
        if (URLs.newURL(string).getProtocol().equals("file") && (n = "file:".length()) < string.length() && string.charAt(n) != '/') {
            string = string.substring(0, n) + "///" + string.substring(n);
        }
        return string;
    }

    static String formatHeaders(String string) {
        return string.trim().replaceAll("(?m)^", "    ");
    }
}

