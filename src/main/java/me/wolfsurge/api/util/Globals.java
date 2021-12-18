/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package me.wolfsurge.api.util;

import net.minecraft.client.Minecraft;

public interface Globals {
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final char SECTIONSIGN = '\u00a7';

    default public boolean nullCheck() {
        return Globals.mc.world == null || Globals.mc.player == null;
    }
}

