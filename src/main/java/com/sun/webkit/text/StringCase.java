/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.text;

import java.util.Locale;

final class StringCase {
    StringCase() {
    }

    private static String toLowerCase(String string) {
        return string.toLowerCase(Locale.ROOT);
    }

    private static String toUpperCase(String string) {
        return string.toUpperCase(Locale.ROOT);
    }

    private static String foldCase(String string) {
        return string.toUpperCase(Locale.ROOT).toLowerCase(Locale.ROOT);
    }
}

