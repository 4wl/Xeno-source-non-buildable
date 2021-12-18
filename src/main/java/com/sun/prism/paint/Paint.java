/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.paint;

public abstract class Paint {
    private final Type type;
    private final boolean proportional;
    private final boolean isMutable;

    Paint(Type type2, boolean bl, boolean bl2) {
        this.type = type2;
        this.proportional = bl;
        this.isMutable = bl2;
    }

    public final Type getType() {
        return this.type;
    }

    public boolean isProportional() {
        return this.proportional;
    }

    public abstract boolean isOpaque();

    public boolean isMutable() {
        return this.isMutable;
    }

    public static enum Type {
        COLOR("Color", false, false),
        LINEAR_GRADIENT("LinearGradient", true, false),
        RADIAL_GRADIENT("RadialGradient", true, false),
        IMAGE_PATTERN("ImagePattern", false, true);

        private String name;
        private boolean isGradient;
        private boolean isImagePattern;

        private Type(String string2, boolean bl, boolean bl2) {
            this.name = string2;
            this.isGradient = bl;
            this.isImagePattern = bl2;
        }

        public String getName() {
            return this.name;
        }

        public boolean isGradient() {
            return this.isGradient;
        }

        public boolean isImagePattern() {
            return this.isImagePattern;
        }
    }
}

