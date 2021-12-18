/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.runtime;

public class VersionInfo {
    private static final String BUILD_TIMESTAMP = "Thu Sep 22 18:43:43 UTC 2016";
    private static final String HUDSON_JOB_NAME = "8u111";
    private static final String HUDSON_BUILD_NUMBER = "28";
    private static final String PROMOTED_BUILD_NUMBER = "14";
    private static final String PRODUCT_NAME = "Java(TM)";
    private static final String RAW_VERSION = "8.0.111";
    private static final String RELEASE_MILESTONE = "fcs";
    private static final String RELEASE_NAME = "8u111";
    private static final String VERSION;
    private static final String RUNTIME_VERSION;

    public static synchronized void setupSystemProperties() {
        if (System.getProperty("javafx.version") == null) {
            System.setProperty("javafx.version", VersionInfo.getVersion());
            System.setProperty("javafx.runtime.version", VersionInfo.getRuntimeVersion());
        }
    }

    public static String getBuildTimestamp() {
        return BUILD_TIMESTAMP;
    }

    public static String getHudsonJobName() {
        if ("8u111".equals("not_hudson")) {
            return "";
        }
        return "8u111";
    }

    public static String getHudsonBuildNumber() {
        return HUDSON_BUILD_NUMBER;
    }

    public static String getReleaseMilestone() {
        if (RELEASE_MILESTONE.equals(RELEASE_MILESTONE)) {
            return "";
        }
        return RELEASE_MILESTONE;
    }

    public static String getVersion() {
        return VERSION;
    }

    public static String getRuntimeVersion() {
        return RUNTIME_VERSION;
    }

    static {
        String string = RAW_VERSION;
        if (VersionInfo.getReleaseMilestone().length() > 0) {
            string = string + "-fcs";
        }
        VERSION = string;
        string = VersionInfo.getHudsonJobName().length() > 0 ? string + "-b14" : string + " (Thu Sep 22 18:43:43 UTC 2016)";
        RUNTIME_VERSION = string;
    }
}

