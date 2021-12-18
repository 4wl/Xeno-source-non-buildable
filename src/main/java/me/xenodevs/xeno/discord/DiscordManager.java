/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.Xeno;

public class DiscordManager
implements Globals {
    private static final DiscordRPC discordPresence = DiscordRPC.INSTANCE;
    private static final DiscordRichPresence richPresence = new DiscordRichPresence();
    private static final DiscordEventHandlers presenceHandlers = new DiscordEventHandlers();
    private static Thread presenceThread;

    public static void startPresence() {
        discordPresence.Discord_Initialize("916078755221499975", presenceHandlers, true, "");
        DiscordManager.richPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        discordPresence.Discord_UpdatePresence(richPresence);
        presenceThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    DiscordManager.richPresence.largeImageKey = "large";
                    DiscordManager.richPresence.largeImageText = Xeno.NAME + (Xeno.isDev ? " (Dev)" : "");
                    DiscordManager.richPresence.state = !Xeno.hasFinishedLoading ? "Loading Xeno Version 1.1" : "Xeno Client v1.1";
                    discordPresence.Discord_UpdatePresence(richPresence);
                    Thread.sleep(3000L);
                }
                catch (Exception exception) {}
            }
        });
        presenceThread.start();
    }

    public static void update(String arg) {
        DiscordManager.richPresence.details = arg;
    }

    public static void doInitPhase() {
        DiscordManager.richPresence.largeImageKey = "large";
        DiscordManager.richPresence.largeImageText = Xeno.NAME + (Xeno.isDev ? " (Dev)" : "");
        DiscordManager.richPresence.state = "Using Xeno Client version 1.1";
        discordPresence.Discord_UpdatePresence(richPresence);
    }

    public static void interruptPresence() {
        if (presenceThread != null && !presenceThread.isInterrupted()) {
            presenceThread.interrupt();
        }
        discordPresence.Discord_Shutdown();
        discordPresence.Discord_ClearPresence();
    }
}

