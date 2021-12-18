/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.util.Builder
 */
package com.sun.javafx.fxml.builder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import javafx.util.Builder;

public class URLBuilder
extends AbstractMap<String, Object>
implements Builder<URL> {
    private ClassLoader classLoader;
    private Object value = null;
    public static final String VALUE_KEY = "value";

    public URLBuilder(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public Object put(String string, Object object) {
        if (string == null) {
            throw new NullPointerException();
        }
        if (!string.equals(VALUE_KEY)) {
            throw new IllegalArgumentException(string + " is not a valid property.");
        }
        this.value = object;
        return null;
    }

    public URL build() {
        URL uRL;
        if (this.value == null) {
            throw new IllegalStateException();
        }
        if (this.value instanceof URL) {
            uRL = (URL)this.value;
        } else {
            String string = this.value.toString();
            if (string.startsWith("/")) {
                uRL = this.classLoader.getResource(string);
            } else {
                try {
                    uRL = new URL(string);
                }
                catch (MalformedURLException malformedURLException) {
                    throw new RuntimeException(malformedURLException);
                }
            }
        }
        return uRL;
    }

    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }
}

