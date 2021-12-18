/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 */
package me.xenodevs.xeno.gui.mainmenu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.discord.DiscordManager;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.mainmenu.custom.XenoMainMenu;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.utils.render.ColorUtil;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class MainMenuHook {
    public static void render(int mouseX, int mouseY) {
        DiscordManager.update("In Main Menu");
        ArrayList<Object> strings = new ArrayList<Object>();
        strings.add("Logged in as " + Minecraft.getMinecraft().getSession().getUsername());
        strings.sort(Comparator.comparingInt(s -> FontManager.comfortaa.getStringWidth((String)s)).reversed());
        int len = FontManager.comfortaaBig.getStringWidth(Xeno.NAME) + 10;
        if (FontManager.comfortaa.getStringWidth((String)strings.get(0)) > FontManager.comfortaaBig.getStringWidth("Xeno Client")) {
            len = FontManager.comfortaa.getStringWidth((String)strings.get(0)) + 45;
        }
        RenderUtils2D.drawRoundedRect(-10.0, -10.0, len, 55.0, 10.0, new Color(0, 0, 0, 190).getRGB());
        FontManager.comfortaaBig.drawRainbowStringWithShadow(Xeno.NAME, 10.0f, 10.0f, ColorUtil.rainbowWave(4.0f, 1.0f, 1.0f, 1), 3.0f, 0.9f, 100);
        int count = 30;

            FontManager.comfortaa.drawRainbowStringWithShadow("", 10.0f, count, ColorUtil.rainbowWave(4.0f, 1.0f, 1.0f, 1), 3.0f, 0.9f, 100);
            count += 10;

        RenderUtils2D.drawRoundedRect(-10.0, 100.0, 85.0, 25.0, 5.0, -1879048192);
        FontManager.comfortaa.drawStringWithShadow("Xeno Main\nMenu", 3.0f, 103.0f, GuiUtil.mouseOver(0.0, 100.0, 75.0, 125.0, mouseX, mouseY) ? ColorUtil.rainbowWave(4.0f, 1.0f, 1.0f, 0) : -1);
    }

    public static void buttonPress(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && GuiUtil.mouseOver(0.0, 100.0, 75.0, 125.0, mouseX, mouseY)) {
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new XenoMainMenu());
            GuiUtil.customMainMenu = true;
            Xeno.config.saveMisc();
        }
    }
}

