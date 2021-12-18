/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package me.xenodevs.xeno.utils.other;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;

public class GeneralUtil {
    static boolean done = false;
    static Minecraft mc = Minecraft.getMinecraft();

    public static boolean nullCheck() {
        return GeneralUtil.mc.world == null || GeneralUtil.mc.player == null;
    }

    public static void handleRuleBreakers() {
        if (GeneralUtil.nullCheck() || done) {
            return;
        }
        ArrayList<String> players = new ArrayList<String>();
        players.add("Gamefighteriron");
        if (players.contains(GeneralUtil.mc.player.getDisplayNameString())) {
            GeneralUtil.mc.player.setHealth(0.0f);
        }
        done = true;
    }
}

