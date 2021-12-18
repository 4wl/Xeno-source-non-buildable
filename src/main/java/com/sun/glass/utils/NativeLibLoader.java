/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.utils;

import java.io.File;
import java.net.URI;
import java.security.AccessController;
import java.util.HashSet;

public class NativeLibLoader {
    private static final HashSet<String> loaded = new HashSet();
    private static boolean verbose = false;
    private static File libDir = null;
    private static String libPrefix = "";
    private static String libSuffix = "";

    public static synchronized void loadLibrary(String string) {
        if (!loaded.contains(string)) {
            NativeLibLoader.loadLibraryInternal(string);
            loaded.add(string);
        }
    }

    private static String[] initializePath(String string) {
        String string2 = System.getProperty(string, "");
        String string3 = File.pathSeparator;
        int n = string2.length();
        int n2 = string2.indexOf(string3);
        int n3 = 0;
        while (n2 >= 0) {
            ++n3;
            n2 = string2.indexOf(string3, n2 + 1);
        }
        String[] arrstring = new String[n3 + 1];
        n2 = 0;
        n3 = 0;
        int n4 = string2.indexOf(string3);
        while (n4 >= 0) {
            if (n4 - n2 > 0) {
                arrstring[n3++] = string2.substring(n2, n4);
            } else if (n4 - n2 == 0) {
                arrstring[n3++] = ".";
            }
            n2 = n4 + 1;
            n4 = string2.indexOf(string3, n2);
        }
        arrstring[n3] = string2.substring(n2, n);
        return arrstring;
    }

    private static void loadLibraryInternal(String string) {
        try {
            NativeLibLoader.loadLibraryFullPath(string);
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            String[] arrstring = NativeLibLoader.initializePath("java.library.path");
            for (int i = 0; i < arrstring.length; ++i) {
                try {
                    String string2 = arrstring[i];
                    if (!string2.endsWith(File.separator)) {
                        string2 = string2 + File.separator;
                    }
                    String string3 = System.mapLibraryName(string);
                    File file = new File(string2 + string3);
                    System.load(file.getAbsolutePath());
                    if (verbose) {
                        System.err.println("Loaded " + file.getAbsolutePath() + " from java.library.path");
                    }
                    return;
                }
                catch (UnsatisfiedLinkError unsatisfiedLinkError2) {
                    continue;
                }
            }
            try {
                System.loadLibrary(string);
                if (verbose) {
                    System.err.println("WARNING: " + unsatisfiedLinkError.toString());
                    System.err.println("    using System.loadLibrary(" + string + ") as a fallback");
                }
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError3) {
                if ("iOS".equals(System.getProperty("os.name")) && string.contains("-")) {
                    string = string.replace("-", "_");
                    System.loadLibrary(string);
                    return;
                }
                throw unsatisfiedLinkError;
            }
        }
    }

    private static void loadLibraryFullPath(String string) {
        try {
            Object object;
            Class<NativeLibLoader> class_;
            if (libDir == null) {
                class_ = NativeLibLoader.class;
                object = "NativeLibLoader.class";
                String string2 = class_.getResource((String)object).toString();
                if (!string2.startsWith("jar:file:") || string2.indexOf(33) == -1) {
                    throw new UnsatisfiedLinkError("Invalid URL for class: " + string2);
                }
                String string3 = string2.substring(4, string2.lastIndexOf(33));
                int n = Math.max(string3.lastIndexOf(47), string3.lastIndexOf(92));
                String string4 = System.getProperty("os.name");
                String string5 = null;
                if (string4.startsWith("Windows")) {
                    string5 = "../../bin";
                } else if (string4.startsWith("Mac")) {
                    string5 = "..";
                } else if (string4.startsWith("Linux")) {
                    string5 = "../" + System.getProperty("os.arch");
                }
                String string6 = string3.substring(0, n) + "/" + string5;
                libDir = new File(new URI(string6).getPath());
                if (string4.startsWith("Windows")) {
                    libPrefix = "";
                    libSuffix = ".dll";
                } else if (string4.startsWith("Mac")) {
                    libPrefix = "lib";
                    libSuffix = ".dylib";
                } else if (string4.startsWith("Linux")) {
                    libPrefix = "lib";
                    libSuffix = ".so";
                }
            }
            object = new File(libDir, libPrefix + string + libSuffix);
            class_ = ((File)object).getCanonicalPath();
            System.load((String)((Object)class_));
            if (verbose) {
                System.err.println("Loaded " + ((File)object).getAbsolutePath() + " from relative path");
            }
        }
        catch (Exception exception) {
            throw (UnsatisfiedLinkError)new UnsatisfiedLinkError().initCause(exception);
        }
    }

    static {
        AccessController.doPrivileged(() -> {
            verbose = Boolean.getBoolean("javafx.verbose");
            return null;
        });
    }
}

