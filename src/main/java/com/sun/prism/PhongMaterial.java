/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism;

import com.sun.prism.Material;
import com.sun.prism.TextureMap;

public interface PhongMaterial
extends Material {
    public static final int DIFFUSE = MapType.DIFFUSE.ordinal();
    public static final int SPECULAR = MapType.SPECULAR.ordinal();
    public static final int BUMP = MapType.BUMP.ordinal();
    public static final int SELF_ILLUM = MapType.SELF_ILLUM.ordinal();
    public static final int MAX_MAP_TYPE = MapType.values().length;

    public void setDiffuseColor(float var1, float var2, float var3, float var4);

    public void setSpecularColor(boolean var1, float var2, float var3, float var4, float var5);

    public void setTextureMap(TextureMap var1);

    public void lockTextureMaps();

    public void unlockTextureMaps();

    public static enum MapType {
        DIFFUSE,
        SPECULAR,
        BUMP,
        SELF_ILLUM;

    }
}

