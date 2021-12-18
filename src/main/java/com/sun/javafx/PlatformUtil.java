/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.util.Properties;

public class PlatformUtil {
    private static final String os = System.getProperty("os.name");
    private static final String version = System.getProperty("os.version");
    private static final boolean embedded;
    private static final String embeddedType;
    private static final boolean useEGL;
    private static final boolean doEGLCompositing;
    private static String javafxPlatform;
    private static final boolean ANDROID;
    private static final boolean WINDOWS;
    private static final boolean WINDOWS_VISTA_OR_LATER;
    private static final boolean WINDOWS_7_OR_LATER;
    private static final boolean MAC;
    private static final boolean LINUX;
    private static final boolean SOLARIS;
    private static final boolean IOS;

    private static boolean versionNumberGreaterThanOrEqualTo(float f) {
        try {
            return Float.parseFloat(version) >= f;
        }
        catch (Exception exception) {
            return false;
        }
    }

    public static boolean isWindows() {
        return WINDOWS;
    }

    public static boolean isWinVistaOrLater() {
        return WINDOWS_VISTA_OR_LATER;
    }

    public static boolean isWin7OrLater() {
        return WINDOWS_7_OR_LATER;
    }

    public static boolean isMac() {
        return MAC;
    }

    public static boolean isLinux() {
        return LINUX;
    }

    public static boolean useEGL() {
        return useEGL;
    }

    public static boolean useEGLWindowComposition() {
        return doEGLCompositing;
    }

    public static boolean useGLES2() {
        String string = "false";
        string = AccessController.doPrivileged(() -> System.getProperty("use.gles2"));
        return "true".equals(string);
    }

    public static boolean isSolaris() {
        return SOLARIS;
    }

    public static boolean isUnix() {
        return LINUX || SOLARIS;
    }

    public static boolean isEmbedded() {
        return embedded;
    }

    public static String getEmbeddedType() {
        return embeddedType;
    }

    public static boolean isIOS() {
        return IOS;
    }

    private static void loadPropertiesFromFile(File file) {
        Object object;
        Properties properties = new Properties();
        try {
            object = new FileInputStream(file);
            properties.load((InputStream)object);
            ((InputStream)object).close();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        if (javafxPlatform == null) {
            javafxPlatform = properties.getProperty("javafx.platform");
        }
        object = javafxPlatform + ".";
        int n = ((String)object).length();
        boolean bl = false;
        for (Object object2 : properties.keySet()) {
            String string = (String)object2;
            if (!string.startsWith((String)object)) continue;
            bl = true;
            String string2 = string.substring(n);
            if (System.getProperty(string2) != null) continue;
            String string3 = properties.getProperty(string);
            System.setProperty(string2, string3);
        }
        if (!bl) {
            System.err.println("Warning: No settings found for javafx.platform='" + javafxPlatform + "'");
        }
    }

    private static File getRTDir() {
        try {
            String string = "PlatformUtil.class";
            Class<PlatformUtil> class_ = PlatformUtil.class;
            URL uRL = class_.getResource(string);
            if (uRL == null) {
                return null;
            }
            String string2 = uRL.toString();
            if (!string2.startsWith("jar:file:") || string2.indexOf(33) == -1) {
                return null;
            }
            String string3 = string2.substring(4, string2.lastIndexOf(33));
            int n = Math.max(string3.lastIndexOf(47), string3.lastIndexOf(92));
            return new File(new URL(string3.substring(0, n + 1)).getPath()).getParentFile();
        }
        catch (MalformedURLException malformedURLException) {
            return null;
        }
    }

    private static void loadProperties() {
        String string = System.getProperty("java.vm.name");
        String string2 = System.getProperty("os.arch");
        if (!(javafxPlatform != null || string2 != null && string2.equals("arm") || string != null && string.indexOf("Embedded") > 0)) {
            return;
        }
        AccessController.doPrivileged(() -> {
            File file = PlatformUtil.getRTDir();
            String string = "javafx.platform.properties";
            File file2 = new File(file, "javafx.platform.properties");
            if (file2.exists()) {
                PlatformUtil.loadPropertiesFromFile(file2);
                return null;
            }
            String string2 = System.getProperty("java.home");
            File file3 = new File(string2, "lib" + File.separator + "javafx.platform.properties");
            if (file3.exists()) {
                PlatformUtil.loadPropertiesFromFile(file3);
                return null;
            }
            String string3 = System.getProperty("javafx.runtime.path");
            File file4 = new File(string3, File.separator + "javafx.platform.properties");
            if (file4.exists()) {
                PlatformUtil.loadPropertiesFromFile(file4);
                return null;
            }
            return null;
        });
    }

    public static boolean isAndroid() {
        return ANDROID;
    }

    static {
        javafxPlatform = AccessController.doPrivileged(() -> System.getProperty("javafx.platform"));
        PlatformUtil.loadProperties();
        embedded = AccessController.doPrivileged(() -> Boolean.getBoolean("com.sun.javafx.isEmbedded"));
        embeddedType = AccessController.doPrivileged(() -> System.getProperty("embedded"));
        useEGL = AccessController.doPrivileged(() -> Boolean.getBoolean("use.egl"));
        doEGLCompositing = useEGL ? AccessController.doPrivileged(() -> Boolean.getBoolean("doNativeComposite")) : false;
        ANDROID = "android".equals(javafxPlatform) || "Dalvik".equals(System.getProperty("java.vm.name"));
        WINDOWS = os.startsWith("Windows");
        WINDOWS_VISTA_OR_LATER = WINDOWS && PlatformUtil.versionNumberGreaterThanOrEqualTo(6.0f);
        WINDOWS_7_OR_LATER = WINDOWS && PlatformUtil.versionNumberGreaterThanOrEqualTo(6.1f);
        MAC = os.startsWith("Mac");
        LINUX = os.startsWith("Linux") && !ANDROID;
        SOLARIS = os.startsWith("SunOS");
        IOS = os.startsWith("iOS");
    }
}

