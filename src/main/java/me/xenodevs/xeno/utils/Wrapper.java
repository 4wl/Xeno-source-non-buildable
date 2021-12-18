/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.network.NetHandlerPlayClient
 */
package me.xenodevs.xeno.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;

public class Wrapper {
    static Minecraft mc = Minecraft.getMinecraft();

    public static EntityPlayerSP getPlayer() {
        return Wrapper.mc.player;
    }

    public static WorldClient getWorld() {
        return Wrapper.mc.world;
    }

    public static NetHandlerPlayClient getConnection() {
        return mc.getConnection();
    }
}

