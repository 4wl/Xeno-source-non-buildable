/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.text.Font
 */
package com.sun.javafx.css;

import com.sun.javafx.css.SizeUnits;
import javafx.scene.text.Font;

public final class Size {
    private final double value;
    private final SizeUnits units;

    public Size(double d, SizeUnits sizeUnits) {
        this.value = d;
        this.units = sizeUnits != null ? sizeUnits : SizeUnits.PX;
    }

    public double getValue() {
        return this.value;
    }

    public SizeUnits getUnits() {
        return this.units;
    }

    public boolean isAbsolute() {
        return this.units.isAbsolute();
    }

    public double points(Font font) {
        return this.points(1.0, font);
    }

    public double points(double d, Font font) {
        return this.units.points(this.value, d, font);
    }

    public double pixels(double d, Font font) {
        return this.units.pixels(this.value, d, font);
    }

    public double pixels(Font font) {
        return this.pixels(1.0, font);
    }

    public double pixels(double d) {
        return this.pixels(d, null);
    }

    public double pixels() {
        return this.pixels(1.0, null);
    }

    public String toString() {
        return Double.toString(this.value) + this.units.toString();
    }

    public int hashCode() {
        long l = 17L;
        l = 37L * l + Double.doubleToLongBits(this.value);
        l = 37L * l + (long)this.units.hashCode();
        return (int)(l ^ l >> 32);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }
        Size size = (Size)object;
        if (this.units != size.units) {
            return false;
        }
        if (this.value == size.value) {
            return true;
        }
        if (this.value > 0.0 ? size.value > 0.0 : size.value < 0.0) {
            double d = this.value > 0.0 ? this.value : -this.value;
            double d2 = size.value > 0.0 ? size.value : -size.value;
            double d3 = this.value - size.value;
            return !(d3 < -1.0E-6) && !(1.0E-6 < d3);
        }
        return false;
    }
}

