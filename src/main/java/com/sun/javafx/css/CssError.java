/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.CssMetaData
 *  javafx.css.Styleable
 *  javafx.scene.Scene
 */
package com.sun.javafx.css;

import com.sun.javafx.css.StyleManager;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.scene.Scene;

public class CssError {
    private static Reference<Scene> SCENE_REF;
    private final Reference<Scene> sceneRef;
    protected final String message;

    public static void setCurrentScene(Scene scene) {
        if (StyleManager.getErrors() == null) {
            return;
        }
        if (scene != null) {
            Scene scene2;
            Scene scene3 = scene2 = SCENE_REF != null ? SCENE_REF.get() : null;
            if (scene2 != scene) {
                SCENE_REF = new WeakReference<Scene>(scene);
            }
        } else {
            SCENE_REF = null;
        }
    }

    public final String getMessage() {
        return this.message;
    }

    public CssError(String string) {
        this.message = string;
        this.sceneRef = SCENE_REF;
    }

    public Scene getScene() {
        return this.sceneRef != null ? this.sceneRef.get() : null;
    }

    public String toString() {
        return "CSS Error: " + this.message;
    }

    public static final class PropertySetError
    extends CssError {
        private final CssMetaData styleableProperty;
        private final Styleable styleable;

        public PropertySetError(CssMetaData cssMetaData, Styleable styleable, String string) {
            super(string);
            this.styleableProperty = cssMetaData;
            this.styleable = styleable;
        }

        public Styleable getStyleable() {
            return this.styleable;
        }

        public CssMetaData getProperty() {
            return this.styleableProperty;
        }
    }

    public static final class StringParsingError
    extends CssError {
        private final String style;

        public StringParsingError(String string, String string2) {
            super(string2);
            this.style = string;
        }

        public String getStyle() {
            return this.style;
        }

        @Override
        public String toString() {
            return "CSS Error parsing '" + this.style + ": " + this.message;
        }
    }

    public static final class InlineStyleParsingError
    extends CssError {
        private final Styleable styleable;

        public InlineStyleParsingError(Styleable styleable, String string) {
            super(string);
            this.styleable = styleable;
        }

        public Styleable getStyleable() {
            return this.styleable;
        }

        @Override
        public String toString() {
            String string = this.styleable.getStyle();
            String string2 = this.styleable.toString();
            return "CSS Error parsing in-line style '" + string + "' from " + string2 + ": " + this.message;
        }
    }

    public static final class StylesheetParsingError
    extends CssError {
        private final String url;

        public StylesheetParsingError(String string, String string2) {
            super(string2);
            this.url = string;
        }

        public String getURL() {
            return this.url;
        }

        @Override
        public String toString() {
            String string = this.url != null ? this.url : "?";
            return "CSS Error parsing " + string + ": " + this.message;
        }
    }
}

