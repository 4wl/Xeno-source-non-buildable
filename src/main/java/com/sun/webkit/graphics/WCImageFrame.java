/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.WCImage;

public abstract class WCImageFrame
extends Ref {
    public abstract WCImage getFrame();

    protected void destroyDecodedData() {
    }
}

