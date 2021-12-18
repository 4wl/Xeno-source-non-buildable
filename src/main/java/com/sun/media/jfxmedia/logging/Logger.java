/*
 * Decompiled with CFR 0.150.
 */
package com.sun.media.jfxmedia.logging;

public class Logger {
    public static final int OFF = Integer.MAX_VALUE;
    public static final int ERROR = 4;
    public static final int WARNING = 3;
    public static final int INFO = 2;
    public static final int DEBUG = 1;
    private static int currentLevel = Integer.MAX_VALUE;
    private static long startTime = 0L;
    private static final Object lock = new Object();

    private static void startLogger() {
        try {
            String string = System.getProperty("jfxmedia.loglevel", "off").toLowerCase();
            Integer n = string.equals("debug") ? Integer.valueOf(1) : (string.equals("warning") ? Integer.valueOf(3) : (string.equals("error") ? Integer.valueOf(4) : (string.equals("info") ? Integer.valueOf(2) : Integer.valueOf(Integer.MAX_VALUE))));
            Logger.setLevel(n);
            startTime = System.currentTimeMillis();
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (Logger.canLog(1)) {
            Logger.logMsg(1, "Logger initialized");
        }
    }

    private Logger() {
    }

    public static boolean initNative() {
        if (Logger.nativeInit()) {
            Logger.nativeSetNativeLevel(currentLevel);
            return true;
        }
        return false;
    }

    private static native boolean nativeInit();

    public static void setLevel(int n) {
        currentLevel = n;
        try {
            Logger.nativeSetNativeLevel(n);
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            // empty catch block
        }
    }

    private static native void nativeSetNativeLevel(int var0);

    public static boolean canLog(int n) {
        return n >= currentLevel;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void logMsg(int n, String string) {
        Object object = lock;
        synchronized (object) {
            if (n < currentLevel) {
                return;
            }
            if (n == 4) {
                System.err.println("Error (" + Logger.getTimestamp() + "): " + string);
            } else if (n == 3) {
                System.err.println("Warning (" + Logger.getTimestamp() + "): " + string);
            } else if (n == 2) {
                System.out.println("Info (" + Logger.getTimestamp() + "): " + string);
            } else if (n == 1) {
                System.out.println("Debug (" + Logger.getTimestamp() + "): " + string);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void logMsg(int n, String string, String string2, String string3) {
        Object object = lock;
        synchronized (object) {
            if (n < currentLevel) {
                return;
            }
            Logger.logMsg(n, string + ":" + string2 + "() " + string3);
        }
    }

    private static String getTimestamp() {
        long l = System.currentTimeMillis() - startTime;
        long l2 = l / 3600000L;
        long l3 = (l - l2 * 60L * 60L * 1000L) / 60000L;
        long l4 = (l - l2 * 60L * 60L * 1000L - l3 * 60L * 1000L) / 1000L;
        long l5 = l - l2 * 60L * 60L * 1000L - l3 * 60L * 1000L - l4 * 1000L;
        return String.format("%d:%02d:%02d:%03d", l2, l3, l4, l5);
    }

    static {
        Logger.startLogger();
    }
}

