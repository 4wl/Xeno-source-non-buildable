/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.es2;

class GLGPUInfo {
    final String vendor;
    final String model;

    GLGPUInfo(String string, String string2) {
        this.vendor = string;
        this.model = string2;
    }

    boolean matches(GLGPUInfo gLGPUInfo) {
        boolean bl = true;
        if (gLGPUInfo.vendor != null) {
            bl = this.vendor.startsWith(gLGPUInfo.vendor);
        }
        if (gLGPUInfo.model != null) {
            bl = this.model.contains(gLGPUInfo.model);
        }
        return bl;
    }

    public String toString() {
        return "GLGPUInfo [vendor = " + this.vendor + ", model = " + this.model + "]";
    }
}

