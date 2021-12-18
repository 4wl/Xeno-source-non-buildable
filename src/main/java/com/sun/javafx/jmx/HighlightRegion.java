/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Rectangle2D
 */
package com.sun.javafx.jmx;

import com.sun.javafx.tk.TKScene;
import javafx.geometry.Rectangle2D;

public class HighlightRegion
extends Rectangle2D {
    private TKScene tkScene;
    private int hash = 0;

    public HighlightRegion(TKScene tKScene, double d, double d2, double d3, double d4) {
        super(d, d2, d3, d4);
        this.tkScene = tKScene;
    }

    public TKScene getTKScene() {
        return this.tkScene;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof HighlightRegion) {
            HighlightRegion highlightRegion = (HighlightRegion)((Object)object);
            return this.tkScene.equals(highlightRegion.tkScene) && super.equals((Object)highlightRegion);
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long l = 7L;
            l = 31L * l + (long)super.hashCode();
            l = 31L * l + (long)this.tkScene.hashCode();
            this.hash = (int)(l ^ l >> 32);
        }
        return this.hash;
    }

    public String toString() {
        return "HighlighRegion [tkScene = " + this.tkScene + ", rectangle = " + super.toString() + "]";
    }
}

