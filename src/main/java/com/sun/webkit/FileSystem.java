/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.logging.Level;
import java.util.logging.Logger;

final class FileSystem {
    private static final Logger logger = Logger.getLogger(FileSystem.class.getName());

    private FileSystem() {
        throw new AssertionError();
    }

    private static boolean fwkFileExists(String string) {
        return new File(string).exists();
    }

    private static long fwkGetFileSize(String string) {
        try {
            File file = new File(string);
            if (file.exists()) {
                return file.length();
            }
        }
        catch (SecurityException securityException) {
            logger.log(Level.FINE, String.format("Error determining size of file [%s]", string), securityException);
        }
        return -1L;
    }

    private static String fwkPathByAppendingComponent(String string, String string2) {
        return new File(string, string2).getPath();
    }

    private static boolean fwkMakeAllDirectories(String string) {
        try {
            Files.createDirectories(Paths.get(string, new String[0]), new FileAttribute[0]);
            return true;
        }
        catch (IOException | InvalidPathException exception) {
            logger.log(Level.FINE, String.format("Error creating directory [%s]", string), exception);
            return false;
        }
    }

    private static String fwkPathGetFileName(String string) {
        return new File(string).getName();
    }
}

