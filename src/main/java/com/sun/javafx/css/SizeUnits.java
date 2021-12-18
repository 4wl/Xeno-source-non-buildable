/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.text.Font
 */
package com.sun.javafx.css;

import javafx.scene.text.Font;

public enum SizeUnits {
    PERCENT(false){

        public String toString() {
            return "%";
        }

        @Override
        public double points(double d, double d2, Font font) {
            return d / 100.0 * d2;
        }

        @Override
        public double pixels(double d, double d2, Font font) {
            return d / 100.0 * d2;
        }
    }
    ,
    IN(true){

        public String toString() {
            return "in";
        }

        @Override
        public double points(double d, double d2, Font font) {
            return d * 72.0;
        }

        @Override
        public double pixels(double d, double d2, Font font) {
            return d * 96.0;
        }
    }
    ,
    CM(true){

        public String toString() {
            return "cm";
        }

        @Override
        public double points(double d, double d2, Font font) {
            return d / 2.54 * 72.0;
        }

        @Override
        public double pixels(double d, double d2, Font font) {
            return d / 2.54 * 96.0;
        }
    }
    ,
    MM(true){

        public String toString() {
            return "mm";
        }

        @Override
        public double points(double d, double d2, Font font) {
            return d / 25.4 * 72.0;
        }

        @Override
        public double pixels(double d, double d2, Font font) {
            return d / 25.4 * 96.0;
        }
    }
    ,
    EM(false){

        public String toString() {
            return "em";
        }

        @Override
        public double points(double d, double d2, Font font) {
            return SizeUnits.round(d * SizeUnits.pointSize(font));
        }

        @Override
        public double pixels(double d, double d2, Font font) {
            return SizeUnits.round(d * SizeUnits.pixelSize(font));
        }
    }
    ,
    EX(false){

        public String toString() {
            return "ex";
        }

        @Override
        public double points(double d, double d2, Font font) {
            return SizeUnits.round(d / 2.0 * SizeUnits.pointSize(font));
        }

        @Override
        public double pixels(double d, double d2, Font font) {
            return SizeUnits.round(d / 2.0 * SizeUnits.pixelSize(font));
        }
    }
    ,
    PT(true){

        public String toString() {
            return "pt";
        }

        @Override
        public double points(double d, double d2, Font font) {
            return d;
        }

        @Override
        public double pixels(double d, double d2, Font font) {
            return d * 1.3333333333333333;
        }
    }
    ,
    PC(true){

        public String toString() {
            return "pc";
        }

        @Override
        public double points(double d, double d2, Font font) {
            return d * 12.0;
        }

        @Override
        public double pixels(double d, double d2, Font font) {
            return d * 12.0 * 1.3333333333333333;
        }
    }
    ,
    PX(true){

        public String toString() {
            return "px";
        }

        @Override
        public double points(double d, double d2, Font font) {
            return d * 0.75;
        }

        @Override
        public double pixels(double d, double d2, Font font) {
            return d;
        }
    }
    ,
    DEG(true){

        public String toString() {
            return "deg";
        }

        @Override
        public double points(double d, double d2, Font font) {
            return SizeUnits.round(d);
        }

        @Override
        public double pixels(double d, double d2, Font font) {
            return SizeUnits.round(d);
        }
    }
    ,
    GRAD(true){

        public String toString() {
            return "grad";
        }

        @Override
        public double points(double d, double d2, Font font) {
            return SizeUnits.round(d * 9.0 / 10.0);
        }

        @Override
        public double pixels(double d, double d2, Font font) {
            return SizeUnits.round(d * 9.0 / 10.0);
        }
    }
    ,
    RAD(true){

        public String toString() {
            return "rad";
        }

        @Override
        public double points(double d, double d2, Font font) {
            return SizeUnits.round(d * 180.0 / Math.PI);
        }

        @Override
        public double pixels(double d, double d2, Font font) {
            return SizeUnits.round(d * 180.0 / Math.PI);
        }
    }
    ,
    TURN(true){

        public String toString() {
            return "turn";
        }

        @Override
        public double points(double d, double d2, Font font) {
            return SizeUnits.round(d * 360.0);
        }

        @Override
        public double pixels(double d, double d2, Font font) {
            return SizeUnits.round(d * 360.0);
        }
    }
    ,
    S(true){

        public String toString() {
            return "s";
        }

        @Override
        public double points(double d, double d2, Font font) {
            return d;
        }

        @Override
        public double pixels(double d, double d2, Font font) {
            return d;
        }
    }
    ,
    MS(true){

        public String toString() {
            return "ms";
        }

        @Override
        public double points(double d, double d2, Font font) {
            return d;
        }

        @Override
        public double pixels(double d, double d2, Font font) {
            return d;
        }
    };

    private final boolean absolute;
    private static final double DOTS_PER_INCH = 96.0;
    private static final double POINTS_PER_INCH = 72.0;
    private static final double CM_PER_INCH = 2.54;
    private static final double MM_PER_INCH = 25.4;
    private static final double POINTS_PER_PICA = 12.0;

    abstract double points(double var1, double var3, Font var5);

    abstract double pixels(double var1, double var3, Font var5);

    private SizeUnits(boolean bl) {
        this.absolute = bl;
    }

    public boolean isAbsolute() {
        return this.absolute;
    }

    private static double pointSize(Font font) {
        return SizeUnits.pixelSize(font) * 0.75;
    }

    private static double pixelSize(Font font) {
        return font != null ? font.getSize() : Font.getDefault().getSize();
    }

    private static double round(double d) {
        if (d == 0.0) {
            return d;
        }
        double d2 = d < 0.0 ? -0.05 : 0.05;
        return (double)((long)((d + d2) * 10.0)) / 10.0;
    }
}

