/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Scene
 *  javafx.scene.control.Control
 */
package com.sun.javafx.webkit.prism.theme;

import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.webkit.theme.Renderer;
import com.sun.prism.Graphics;
import com.sun.webkit.graphics.WCGraphicsContext;
import javafx.scene.Scene;
import javafx.scene.control.Control;

public final class PrismRenderer
extends Renderer {
    @Override
    protected void render(Control control, WCGraphicsContext wCGraphicsContext) {
        Scene.impl_setAllowPGAccess((boolean)true);
        NGNode nGNode = control.impl_getPeer();
        Scene.impl_setAllowPGAccess((boolean)false);
        nGNode.render((Graphics)wCGraphicsContext.getPlatformGraphics());
    }
}

