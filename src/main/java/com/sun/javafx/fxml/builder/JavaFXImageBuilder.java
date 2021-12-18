/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.image.Image
 *  javafx.util.Builder
 */
package com.sun.javafx.fxml.builder;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import javafx.scene.image.Image;
import javafx.util.Builder;

public class JavaFXImageBuilder
extends AbstractMap<String, Object>
implements Builder<Image> {
    private String url = "";
    private double requestedWidth = 0.0;
    private double requestedHeight = 0.0;
    private boolean preserveRatio = false;
    private boolean smooth = false;
    private boolean backgroundLoading = false;

    public Image build() {
        return new Image(this.url, this.requestedWidth, this.requestedHeight, this.preserveRatio, this.smooth, this.backgroundLoading);
    }

    @Override
    public Object put(String string, Object object) {
        if (object != null) {
            String string2 = object.toString();
            if ("url".equals(string)) {
                this.url = string2;
            } else if ("requestedWidth".equals(string)) {
                this.requestedWidth = Double.parseDouble(string2);
            } else if ("requestedHeight".equals(string)) {
                this.requestedHeight = Double.parseDouble(string2);
            } else if ("preserveRatio".equals(string)) {
                this.preserveRatio = Boolean.parseBoolean(string2);
            } else if ("smooth".equals(string)) {
                this.smooth = Boolean.parseBoolean(string2);
            } else if ("backgroundLoading".equals(string)) {
                this.backgroundLoading = Boolean.parseBoolean(string2);
            } else {
                throw new IllegalArgumentException("Unknown Image property: " + string);
            }
        }
        return null;
    }

    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }
}

