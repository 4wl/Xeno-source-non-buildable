/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.utils.render;

import java.awt.Dimension;
import java.awt.Toolkit;

public class DisplayUtils {
    public static int getDisplayWidth() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)size.getWidth() / 2;
        return width;
    }

    public static int getDisplayHeight() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int)size.getHeight() / 2;
        return height;
    }
}

