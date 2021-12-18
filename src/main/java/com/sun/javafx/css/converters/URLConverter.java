/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Application
 *  javafx.css.ParsedValue
 *  javafx.css.StyleConverter
 *  javafx.scene.text.Font
 */
package com.sun.javafx.css.converters;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.util.Logging;
import com.sun.javafx.util.Utils;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedActionException;
import java.security.ProtectionDomain;
import javafx.application.Application;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;
import sun.util.logging.PlatformLogger;

public final class URLConverter
extends StyleConverterImpl<ParsedValue[], String> {
    public static StyleConverter<ParsedValue[], String> getInstance() {
        return Holder.INSTANCE;
    }

    private URLConverter() {
    }

    public String convert(ParsedValue<ParsedValue[], String> parsedValue, Font font) {
        String string;
        URL uRL;
        String string2;
        String string3 = null;
        ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
        String string4 = string2 = arrparsedValue.length > 0 ? (String)StringConverter.getInstance().convert(arrparsedValue[0], font) : null;
        if (string2 != null && !string2.trim().isEmpty() && (uRL = this.resolve(string = arrparsedValue.length > 1 && arrparsedValue[1] != null ? (String)arrparsedValue[1].getValue() : null, string2 = string2.startsWith("url(") ? Utils.stripQuotes(string2.substring(4, string2.length() - 1)) : Utils.stripQuotes(string2))) != null) {
            string3 = uRL.toExternalForm();
        }
        return string3;
    }

    URL resolve(String string, String string2) {
        String string3;
        String string4 = string3 = string2 != null ? string2.trim() : null;
        if (string3 == null || string3.isEmpty()) {
            return null;
        }
        try {
            String string5;
            URI uRI = new URI(string3);
            if (uRI.isAbsolute()) {
                return uRI.toURL();
            }
            URL uRL = this.resolveRuntimeImport(uRI);
            if (uRL != null) {
                return uRL;
            }
            String string6 = uRI.getPath();
            if (string6.startsWith("/")) {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                return classLoader.getResource(string6.substring(1));
            }
            String string7 = string5 = string != null ? string.trim() : null;
            if (string5 != null && !string5.isEmpty()) {
                URI uRI2 = new URI(string5);
                if (!uRI2.isOpaque()) {
                    URI uRI3 = uRI2.resolve(uRI);
                    return uRI3.toURL();
                }
                URL uRL2 = uRI2.toURL();
                return new URL(uRL2, uRI.getPath());
            }
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            return classLoader.getResource(string6);
        }
        catch (MalformedURLException | URISyntaxException exception) {
            PlatformLogger platformLogger = Logging.getCSSLogger();
            if (platformLogger.isLoggable(PlatformLogger.Level.WARNING)) {
                platformLogger.warning(exception.getLocalizedMessage());
            }
            return null;
        }
    }

    private URL resolveRuntimeImport(URI uRI) {
        String string;
        String string2 = uRI.getPath();
        String string3 = string = string2.startsWith("/") ? string2.substring(1) : string2;
        if ((string.startsWith("com/sun/javafx/scene/control/skin/modena/") || string.startsWith("com/sun/javafx/scene/control/skin/caspian/")) && (string.endsWith(".css") || string.endsWith(".bss"))) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager == null) {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                URL uRL = classLoader.getResource(string);
                return uRL;
            }
            try {
                URL uRL = AccessController.doPrivileged(() -> {
                    ProtectionDomain protectionDomain = Application.class.getProtectionDomain();
                    CodeSource codeSource = protectionDomain.getCodeSource();
                    return codeSource.getLocation();
                });
                URI uRI2 = uRL.toURI();
                String string4 = uRI2.getScheme();
                String string5 = uRI2.getPath();
                if ("file".equals(string4) && string5.endsWith(".jar") && "file".equals(string4)) {
                    string4 = "jar:file";
                    string5 = string5.concat("!/");
                }
                string5 = string5.concat(string);
                String string6 = uRI2.getUserInfo();
                String string7 = uRI2.getHost();
                int n = uRI2.getPort();
                URI uRI3 = new URI(string4, string6, string7, n, string5, null, null);
                return uRI3.toURL();
            }
            catch (MalformedURLException | URISyntaxException | PrivilegedActionException exception) {
                // empty catch block
            }
        }
        return null;
    }

    public String toString() {
        return "URLType";
    }

    public static final class SequenceConverter
    extends StyleConverterImpl<ParsedValue<ParsedValue[], String>[], String[]> {
        public static SequenceConverter getInstance() {
            return Holder.SEQUENCE_INSTANCE;
        }

        private SequenceConverter() {
        }

        public String[] convert(ParsedValue<ParsedValue<ParsedValue[], String>[], String[]> parsedValue, Font font) {
            ParsedValue[] arrparsedValue = (ParsedValue[])parsedValue.getValue();
            String[] arrstring = new String[arrparsedValue.length];
            for (int i = 0; i < arrparsedValue.length; ++i) {
                arrstring[i] = (String)URLConverter.getInstance().convert(arrparsedValue[i], font);
            }
            return arrstring;
        }

        public String toString() {
            return "URLSeqType";
        }
    }

    private static class Holder {
        static final URLConverter INSTANCE = new URLConverter();
        static final SequenceConverter SEQUENCE_INSTANCE = new SequenceConverter();

        private Holder() {
        }
    }
}

