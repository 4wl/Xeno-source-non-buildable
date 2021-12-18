/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.runtime;

import java.io.InputStream;
import java.security.AccessController;
import java.util.Hashtable;

public class SystemProperties {
    private static final String[] sysprop_table = new String[]{"application.codebase", "jfx_specific", "debug", "javafx.debug"};
    private static final String[] jfxprop_table = new String[]{"application.codebase", ""};
    private static final Hashtable sysprop_list = new Hashtable();
    private static final Hashtable jfxprop_list = new Hashtable();
    private static final String versionResourceName = "/com/sun/javafx/runtime/resources/version.properties";
    private static boolean isDebug;
    private static String codebase_value;
    public static final String codebase = "javafx.application.codebase";

    private static void setVersions() {
        InputStream inputStream = SystemProperties.class.getResourceAsStream(versionResourceName);
        try {
            int n = inputStream.available();
            byte[] arrby = new byte[n];
            int n2 = inputStream.read(arrby);
            String string = new String(arrby, "utf-8");
            SystemProperties.setFXProperty("javafx.version", SystemProperties.getValue(string, "release="));
            SystemProperties.setFXProperty("javafx.runtime.version", SystemProperties.getValue(string, "full="));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static String getValue(String string, String string2) {
        String string3 = string;
        int n = string3.indexOf(string2);
        if (n != -1) {
            if ((n = (string3 = string3.substring(n)).indexOf(10)) != -1) {
                return string3.substring(string2.length(), n).trim();
            }
            return string3.substring(string2.length(), string3.length()).trim();
        }
        return "unknown";
    }

    public static void addProperties(String[] arrstring, boolean bl) {
        if (arrstring == null) {
            return;
        }
        Hashtable hashtable = bl ? jfxprop_list : sysprop_list;
        for (int i = 0; i < arrstring.length; i += 2) {
            hashtable.put(arrstring[i], arrstring[i + 1]);
        }
    }

    public static String getProperty(String string) {
        Hashtable hashtable = sysprop_list;
        if (string == null) {
            return null;
        }
        if (!string.startsWith("javafx.")) {
            return null;
        }
        string = string.substring("javafx.".length());
        String string2 = (String)hashtable.get(string);
        if (string2 == null || string2.equals("")) {
            return null;
        }
        if (string2.equals("jfx_specific")) {
            hashtable = jfxprop_list;
            return (String)hashtable.get(string);
        }
        return System.getProperty(string2);
    }

    public static void clearProperty(String string) {
        if (string == null) {
            return;
        }
        Hashtable hashtable = sysprop_list;
        if (!string.startsWith("javafx.".toString())) {
            return;
        }
        string = string.substring("javafx.".length());
        String string2 = (String)hashtable.get(string);
        if (string2 == null) {
            return;
        }
        hashtable.remove(string);
        if (string2.equals("jfx_specific")) {
            hashtable = jfxprop_list;
            hashtable.remove(string);
        }
    }

    public static void setFXProperty(String string, String string2) {
        Hashtable hashtable = sysprop_list;
        if (string.startsWith("javafx.")) {
            String string3 = (String)hashtable.get(string = string.substring("javafx.".length()));
            if (string3 == null) {
                hashtable.put(string, "jfx_specific");
                hashtable = jfxprop_list;
                hashtable.put(string, string2);
            } else if (string3.equals("jfx_specific")) {
                hashtable = jfxprop_list;
                hashtable.put(string, string2);
                if (codebase.equals("javafx." + string)) {
                    codebase_value = string2;
                }
            }
        }
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static String getCodebase() {
        return codebase_value;
    }

    public static void setCodebase(String string) {
        if (string == null) {
            string = "";
        }
        codebase_value = string;
        SystemProperties.setFXProperty(codebase, string);
    }

    static {
        AccessController.doPrivileged(() -> {
            SystemProperties.addProperties(sysprop_table, false);
            SystemProperties.addProperties(jfxprop_table, true);
            SystemProperties.setVersions();
            isDebug = "true".equalsIgnoreCase(SystemProperties.getProperty("javafx.debug"));
            return null;
        });
    }
}

