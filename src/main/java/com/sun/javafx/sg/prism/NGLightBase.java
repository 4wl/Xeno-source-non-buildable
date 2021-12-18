/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGGroup;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape3D;
import com.sun.prism.Graphics;
import com.sun.prism.paint.Color;

public class NGLightBase
extends NGNode {
    private Color color = Color.WHITE;
    private boolean lightOn = true;
    private Affine3D worldTransform;
    Object[] scopedNodes = null;

    protected NGLightBase() {
    }

    @Override
    public void setTransformMatrix(BaseTransform baseTransform) {
        super.setTransformMatrix(baseTransform);
    }

    @Override
    protected void doRender(Graphics graphics) {
    }

    @Override
    protected void renderContent(Graphics graphics) {
    }

    @Override
    protected boolean hasOverlappingContents() {
        return false;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Object object) {
        if (!this.color.equals(object)) {
            this.color = (Color)object;
            this.visualsChanged();
        }
    }

    public boolean isLightOn() {
        return this.lightOn;
    }

    public void setLightOn(boolean bl) {
        if (this.lightOn != bl) {
            this.visualsChanged();
            this.lightOn = bl;
        }
    }

    public Affine3D getWorldTransform() {
        return this.worldTransform;
    }

    public void setWorldTransform(Affine3D affine3D) {
        this.worldTransform = affine3D;
    }

    public void setScope(Object[] arrobject) {
        if (this.scopedNodes != arrobject) {
            this.scopedNodes = arrobject;
            this.visualsChanged();
        }
    }

    final boolean affects(NGShape3D nGShape3D) {
        if (!this.lightOn) {
            return false;
        }
        if (this.scopedNodes == null) {
            return true;
        }
        for (int i = 0; i < this.scopedNodes.length; ++i) {
            Object object = this.scopedNodes[i];
            if (object instanceof NGGroup) {
                for (NGNode nGNode = nGShape3D.getParent(); nGNode != null; nGNode = nGNode.getParent()) {
                    if (object != nGNode) continue;
                    return true;
                }
                continue;
            }
            if (object != nGShape3D) continue;
            return true;
        }
        return false;
    }

    @Override
    public void release() {
    }
}

