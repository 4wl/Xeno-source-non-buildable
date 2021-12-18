/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.webkit.ContextMenu;
import com.sun.webkit.Pasteboard;
import com.sun.webkit.PopupMenu;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.HashMap;
import java.util.Map;
import sun.reflect.misc.MethodUtil;

public abstract class Utilities {
    private static Utilities instance;

    public static synchronized void setUtilities(Utilities utilities) {
        instance = utilities;
    }

    public static synchronized Utilities getUtilities() {
        return instance;
    }

    protected abstract Pasteboard createPasteboard();

    protected abstract PopupMenu createPopupMenu();

    protected abstract ContextMenu createContextMenu();

    private static String fwkGetMIMETypeForExtension(String string) {
        return (String)MimeTypeMapHolder.MIME_TYPE_MAP.get(string);
    }

    private static Object fwkInvokeWithContext(Method method, Object object, Object[] arrobject, AccessControlContext accessControlContext) throws Throwable {
        try {
            return AccessController.doPrivileged(() -> MethodUtil.invoke(method, object, arrobject), accessControlContext);
        }
        catch (PrivilegedActionException privilegedActionException) {
            Throwable throwable = privilegedActionException.getCause();
            if (throwable == null) {
                throwable = privilegedActionException;
            } else if (throwable instanceof InvocationTargetException && throwable.getCause() != null) {
                throwable = throwable.getCause();
            }
            throw throwable;
        }
    }

    private static final class MimeTypeMapHolder {
        private static final Map<String, String> MIME_TYPE_MAP = MimeTypeMapHolder.createMimeTypeMap();

        private MimeTypeMapHolder() {
        }

        private static Map<String, String> createMimeTypeMap() {
            HashMap<String, String> hashMap = new HashMap<String, String>(21);
            hashMap.put("txt", "text/plain");
            hashMap.put("html", "text/html");
            hashMap.put("htm", "text/html");
            hashMap.put("css", "text/css");
            hashMap.put("xml", "text/xml");
            hashMap.put("xsl", "text/xsl");
            hashMap.put("js", "application/x-javascript");
            hashMap.put("xhtml", "application/xhtml+xml");
            hashMap.put("svg", "image/svg+xml");
            hashMap.put("svgz", "image/svg+xml");
            hashMap.put("gif", "image/gif");
            hashMap.put("jpg", "image/jpeg");
            hashMap.put("jpeg", "image/jpeg");
            hashMap.put("png", "image/png");
            hashMap.put("tif", "image/tiff");
            hashMap.put("tiff", "image/tiff");
            hashMap.put("ico", "image/ico");
            hashMap.put("cur", "image/ico");
            hashMap.put("bmp", "image/bmp");
            hashMap.put("mp3", "audio/mpeg");
            return hashMap;
        }
    }
}

