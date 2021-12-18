/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene;

public enum DirtyBits {
    NODE_CACHE,
    NODE_CLIP,
    NODE_EFFECT,
    NODE_OPACITY,
    NODE_TRANSFORM,
    NODE_BOUNDS,
    NODE_TRANSFORMED_BOUNDS,
    NODE_VISIBLE,
    NODE_DEPTH_TEST,
    NODE_BLENDMODE,
    NODE_CSS,
    NODE_FORCE_SYNC,
    NODE_GEOMETRY,
    NODE_CULLFACE,
    NODE_DRAWMODE,
    NODE_SMOOTH,
    NODE_VIEWPORT,
    NODE_CONTENTS,
    PARENT_CHILDREN,
    SHAPE_FILL,
    SHAPE_FILLRULE,
    SHAPE_MODE,
    SHAPE_STROKE,
    SHAPE_STROKEATTRS,
    REGION_SHAPE,
    TEXT_ATTRS,
    TEXT_FONT,
    TEXT_SELECTION,
    TEXT_HELPER,
    MEDIAVIEW_MEDIA,
    WEBVIEW_VIEW,
    EFFECT_EFFECT,
    NODE_CAMERA,
    NODE_CAMERA_TRANSFORM,
    NODE_LIGHT,
    NODE_LIGHT_SCOPE,
    NODE_LIGHT_TRANSFORM,
    MATERIAL,
    MESH,
    MESH_GEOM,
    DEBUG,
    MAX_DIRTY;

    private long mask = 1 << this.ordinal();

    public final long getMask() {
        return this.mask;
    }
}

