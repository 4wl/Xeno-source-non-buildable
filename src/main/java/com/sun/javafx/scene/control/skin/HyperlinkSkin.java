/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.control.Hyperlink
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ButtonBehavior;
import com.sun.javafx.scene.control.skin.LabeledSkinBase;
import javafx.scene.control.Hyperlink;

public class HyperlinkSkin
extends LabeledSkinBase<Hyperlink, ButtonBehavior<Hyperlink>> {
    public HyperlinkSkin(Hyperlink hyperlink) {
        super(hyperlink, new ButtonBehavior<Hyperlink>(hyperlink));
    }
}

