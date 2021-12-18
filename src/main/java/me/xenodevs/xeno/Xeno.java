/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventHandler
 *  net.minecraftforge.fml.common.event.FMLPostInitializationEvent
 *  net.minecraftforge.fml.common.event.FMLPreInitializationEvent
 *  org.apache.logging.log4j.Logger
 */
package me.xenodevs.xeno;

import java.util.ArrayList;
import me.wolfsurge.api.gui.font.FontUtil;
import me.xenodevs.xeno.discord.DiscordManager;
import me.xenodevs.xeno.gui.click.ClickGui;
import me.xenodevs.xeno.gui.click.component.Frame;
import me.xenodevs.xeno.gui.windowgui.WindowGui;
import me.xenodevs.xeno.managers.CommandManager;
import me.xenodevs.xeno.managers.EventManager;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.managers.FriendManager;
import me.xenodevs.xeno.managers.HudManager;
import me.xenodevs.xeno.managers.ModuleManager;
import me.xenodevs.xeno.managers.NotificationManager;
import me.xenodevs.xeno.managers.TickManager;
import me.xenodevs.xeno.storage.Config;
import me.xenodevs.xeno.utils.render.BlurUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid="xeno", name="Xeno", version="1.1")
public class Xeno {
    public static final String MODID = "xeno";
    public static final String MODNAME = "Xeno";
    public static final String VERSION = "1.1";
    public static String NAME = "Xeno Client";
    public static boolean isDev = false;
    public static Logger logger;
    public static me.zero.alpine.EventManager EVENT_BUS;
    public static boolean hasFinishedLoading;
    public static BlurUtil blurManager;
    public static ModuleManager moduleManager;
    public static ClickGui clickGui;
    public static WindowGui windowGui;
    public static EventManager eventManager;
    public static HudManager hudManager;
    public static Config config;
    public static FriendManager friendManager;
    public static CommandManager commandManager;
    public static TickManager tickManager;
    public static NotificationManager notificationManager;
    public static DiscordManager discordManager;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        this.handleDev();
        discordManager = new DiscordManager();
        DiscordManager.startPresence();
    }

    @Mod.EventHandler
    public void init(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register((Object)this);
        FontUtil.bootstrap();
        config = new Config();
        config.loadHUDConfig();
        config.loadClickGUIConfig();
        blurManager = new BlurUtil();
        moduleManager = new ModuleManager();
        config.loadModConfig();
        clickGui = new ClickGui();
        for (Frame f : ClickGui.frames) {
            f.refresh();
        }
        windowGui = new WindowGui();
        eventManager = new EventManager();
        hudManager = new HudManager();
        friendManager = new FriendManager();
        config.loadFriendConfig();
        commandManager = new CommandManager();
        config.loadMisc();
        notificationManager = new NotificationManager();
        tickManager = new TickManager();
        FontManager.load();
        hasFinishedLoading = true;
        logger.info("\n\n\nXeno on top! - Xeno Initialized!\n\n");
    }

    public void handleDev() {
        ArrayList<String> devNames = new ArrayList<String>();
        devNames.add("Wolfsurge");
        devNames.add("Eaxu");
        devNames.add("SoldierMC");
        devNames.add("HAV0X_");
        devNames.add("HAV0X");
        devNames.add("Rollbad");
        devNames.add("kf_greywolf");
        devNames.add("IdentifyDelay");
        devNames.add("AlwaysMineing");
        devNames.add("y_wiener");
        devNames.add("Sensei_foxy");
        devNames.add("Sensei_ice");
        devNames.add("Gamefighteriron");
        devNames.add("Smoakes");
        devNames.add("USP_Match");
        for (int i = 0; i < 1000; ++i) {
            devNames.add("Player" + i);
        }
        if (devNames.contains(Minecraft.getMinecraft().getSession().getUsername())) {
            isDev = true;
            NAME = "Xeno Client";
            logger.info("Recognised Dev Username");
        }
    }

    static {
        EVENT_BUS = new me.zero.alpine.EventManager();
        hasFinishedLoading = false;
    }
}

