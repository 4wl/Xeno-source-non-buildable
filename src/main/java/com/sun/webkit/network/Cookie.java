/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

import com.sun.webkit.network.DateParser;
import com.sun.webkit.network.ExtendedTime;
import java.net.URI;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class Cookie {
    private static final Logger logger = Logger.getLogger(Cookie.class.getName());
    private static final Pattern IP_ADDRESS_PATTERN = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})");
    private final String name;
    private final String value;
    private final long expiryTime;
    private String domain;
    private String path;
    private ExtendedTime creationTime;
    private long lastAccessTime;
    private final boolean persistent;
    private boolean hostOnly;
    private final boolean secureOnly;
    private final boolean httpOnly;

    private Cookie(String string, String string2, long l, String string3, String string4, ExtendedTime extendedTime, long l2, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        this.name = string;
        this.value = string2;
        this.expiryTime = l;
        this.domain = string3;
        this.path = string4;
        this.creationTime = extendedTime;
        this.lastAccessTime = l2;
        this.persistent = bl;
        this.hostOnly = bl2;
        this.secureOnly = bl3;
        this.httpOnly = bl4;
    }

    static Cookie parse(String string, ExtendedTime extendedTime) {
        long l;
        boolean bl;
        Object object;
        logger.log(Level.FINEST, "setCookieString: [{0}]", string);
        String[] arrstring = string.split(";", -1);
        String[] arrstring2 = arrstring[0].split("=", 2);
        if (arrstring2.length != 2) {
            logger.log(Level.FINEST, "Name-value pair string lacks '=', ignoring cookie");
            return null;
        }
        String string2 = arrstring2[0].trim();
        String string3 = arrstring2[1].trim();
        if (string2.length() == 0) {
            logger.log(Level.FINEST, "Name string is empty, ignoring cookie");
            return null;
        }
        Long l2 = null;
        Long l3 = null;
        String string4 = null;
        String string5 = null;
        boolean bl2 = false;
        boolean bl3 = false;
        for (int i = 1; i < arrstring.length; ++i) {
            String[] arrstring3 = arrstring[i].split("=", 2);
            String string6 = arrstring3[0].trim();
            object = (arrstring3.length > 1 ? arrstring3[1] : "").trim();
            try {
                if ("Expires".equalsIgnoreCase(string6)) {
                    l2 = Cookie.parseExpires((String)object);
                    continue;
                }
                if ("Max-Age".equalsIgnoreCase(string6)) {
                    l3 = Cookie.parseMaxAge((String)object, extendedTime.baseTime());
                    continue;
                }
                if ("Domain".equalsIgnoreCase(string6)) {
                    string4 = Cookie.parseDomain((String)object);
                    continue;
                }
                if ("Path".equalsIgnoreCase(string6)) {
                    string5 = Cookie.parsePath((String)object);
                    continue;
                }
                if ("Secure".equalsIgnoreCase(string6)) {
                    bl2 = true;
                    continue;
                }
                if ("HttpOnly".equalsIgnoreCase(string6)) {
                    bl3 = true;
                    continue;
                }
                logger.log(Level.FINEST, "Unknown attribute: [{0}], ignoring", string6);
                continue;
            }
            catch (ParseException parseException) {
                logger.log(Level.FINEST, "{0}, ignoring", parseException.getMessage());
            }
        }
        if (l3 != null) {
            bl = true;
            l = l3;
        } else if (l2 != null) {
            bl = true;
            l = l2;
        } else {
            bl = false;
            l = Long.MAX_VALUE;
        }
        if (string4 == null) {
            string4 = "";
        }
        object = new Cookie(string2, string3, l, string4, string5, extendedTime, extendedTime.baseTime(), bl, false, bl2, bl3);
        logger.log(Level.FINEST, "result: {0}", object);
        return object;
    }

    private static long parseExpires(String string) throws ParseException {
        try {
            return Math.max(DateParser.parse(string), 0L);
        }
        catch (ParseException parseException) {
            throw new ParseException("Error parsing Expires attribute", 0);
        }
    }

    private static long parseMaxAge(String string, long l) throws ParseException {
        try {
            long l2 = Long.parseLong(string);
            if (l2 <= 0L) {
                return 0L;
            }
            return l2 > (Long.MAX_VALUE - l) / 1000L ? Long.MAX_VALUE : l + l2 * 1000L;
        }
        catch (NumberFormatException numberFormatException) {
            throw new ParseException("Error parsing Max-Age attribute", 0);
        }
    }

    private static String parseDomain(String string) throws ParseException {
        if (string.length() == 0) {
            throw new ParseException("Domain attribute is empty", 0);
        }
        if (string.startsWith(".")) {
            string = string.substring(1);
        }
        return string.toLowerCase();
    }

    private static String parsePath(String string) {
        return string.startsWith("/") ? string : null;
    }

    String getName() {
        return this.name;
    }

    String getValue() {
        return this.value;
    }

    long getExpiryTime() {
        return this.expiryTime;
    }

    String getDomain() {
        return this.domain;
    }

    void setDomain(String string) {
        this.domain = string;
    }

    String getPath() {
        return this.path;
    }

    void setPath(String string) {
        this.path = string;
    }

    ExtendedTime getCreationTime() {
        return this.creationTime;
    }

    void setCreationTime(ExtendedTime extendedTime) {
        this.creationTime = extendedTime;
    }

    long getLastAccessTime() {
        return this.lastAccessTime;
    }

    void setLastAccessTime(long l) {
        this.lastAccessTime = l;
    }

    boolean getPersistent() {
        return this.persistent;
    }

    boolean getHostOnly() {
        return this.hostOnly;
    }

    void setHostOnly(boolean bl) {
        this.hostOnly = bl;
    }

    boolean getSecureOnly() {
        return this.secureOnly;
    }

    boolean getHttpOnly() {
        return this.httpOnly;
    }

    boolean hasExpired() {
        return System.currentTimeMillis() > this.expiryTime;
    }

    public boolean equals(Object object) {
        if (object instanceof Cookie) {
            Cookie cookie = (Cookie)object;
            return Cookie.equal(this.name, cookie.name) && Cookie.equal(this.domain, cookie.domain) && Cookie.equal(this.path, cookie.path);
        }
        return false;
    }

    private static boolean equal(Object object, Object object2) {
        return object == null && object2 == null || object != null && object.equals(object2);
    }

    public int hashCode() {
        int n = 7;
        n = 53 * n + Cookie.hashCode(this.name);
        n = 53 * n + Cookie.hashCode(this.domain);
        n = 53 * n + Cookie.hashCode(this.path);
        return n;
    }

    private static int hashCode(Object object) {
        return object != null ? object.hashCode() : 0;
    }

    public String toString() {
        return "[name=" + this.name + ", value=" + this.value + ", " + "expiryTime=" + this.expiryTime + ", domain=" + this.domain + ", " + "path=" + this.path + ", creationTime=" + this.creationTime + ", " + "lastAccessTime=" + this.lastAccessTime + ", " + "persistent=" + this.persistent + ", hostOnly=" + this.hostOnly + ", " + "secureOnly=" + this.secureOnly + ", httpOnly=" + this.httpOnly + "]";
    }

    static boolean domainMatches(String string, String string2) {
        return string.endsWith(string2) && (string.length() == string2.length() || string.charAt(string.length() - string2.length() - 1) == '.' && !Cookie.isIpAddress(string));
    }

    private static boolean isIpAddress(String string) {
        Matcher matcher = IP_ADDRESS_PATTERN.matcher(string);
        if (!matcher.matches()) {
            return false;
        }
        for (int i = 1; i <= matcher.groupCount(); ++i) {
            if (Integer.parseInt(matcher.group(i)) <= 255) continue;
            return false;
        }
        return true;
    }

    static String defaultPath(URI uRI) {
        String string = uRI.getPath();
        if (string == null || !string.startsWith("/")) {
            return "/";
        }
        if ((string = string.substring(0, string.lastIndexOf("/"))).length() == 0) {
            return "/";
        }
        return string;
    }

    static boolean pathMatches(String string, String string2) {
        return string != null && string.startsWith(string2) && (string.length() == string2.length() || string2.endsWith("/") || string.charAt(string2.length()) == '/');
    }
}

