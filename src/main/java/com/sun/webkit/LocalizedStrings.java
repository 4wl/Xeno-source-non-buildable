/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

final class LocalizedStrings {
    private static final Logger log = Logger.getLogger(LocalizedStrings.class.getName());
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("com.sun.webkit.LocalizedStrings", Locale.getDefault(), new EncodingResourceBundleControl("utf-8"));

    private LocalizedStrings() {
    }

    private static String getLocalizedProperty(String string) {
        log.log(Level.FINE, "Get property: " + string);
        String string2 = BUNDLE.getString(string);
        if (string2 != null && string2.trim().length() > 0) {
            log.log(Level.FINE, "Property value: " + string2);
            return string2.trim();
        }
        log.log(Level.FINE, "Unknown property value");
        return null;
    }

    private static final class EncodingResourceBundleControl
    extends ResourceBundle.Control {
        private final String encoding;

        private EncodingResourceBundleControl(String string) {
            this.encoding = string;
        }

        @Override
        public ResourceBundle newBundle(String string, Locale locale, String string2, ClassLoader classLoader, boolean bl) throws IllegalAccessException, InstantiationException, IOException {
            String string3 = this.toBundleName(string, locale);
            String string4 = this.toResourceName(string3, "properties");
            URL uRL = classLoader.getResource(string4);
            if (uRL != null) {
                try {
                    return new PropertyResourceBundle(new InputStreamReader(uRL.openStream(), this.encoding));
                }
                catch (Exception exception) {
                    log.log(Level.FINE, "exception thrown during bundle initialization", exception);
                }
            }
            return super.newBundle(string, locale, string2, classLoader, bl);
        }
    }
}

