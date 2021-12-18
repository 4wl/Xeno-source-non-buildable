/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiMainMenu
 *  net.minecraft.client.gui.GuiMultiplayer
 *  net.minecraft.client.gui.GuiOptions
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiWorldSelection
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.gui.mainmenu.custom;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.discord.DiscordManager;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.utils.render.ColorUtil;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class XenoMainMenu
extends GuiScreen {
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        DiscordManager.update("In Main Menu");
        ScaledResolution sr = new ScaledResolution(this.mc);
        int minX = sr.getScaledWidth() / 2 - FontManager.comfortaaBig.getStringWidth("Xeno Client") - 5;
        int minY = sr.getScaledHeight() / 2 - 55;
        int maxX = minX + FontManager.comfortaaBig.getStringWidth("Xeno Client") * 2 + 5;
        int maxY = minY + 40;
        ResourceLocation path = new ResourceLocation("xeno", "textures/background1.png");
        this.mc.getTextureManager().bindTexture(path);
        RenderUtils2D.drawModalRectWithCustomSizedTexture(0.0, 0.0, 0.0f, 0.0f, this.width, this.height, this.width, this.height);
        Xeno.blurManager.blur(5);
        RenderUtils2D.drawRoundedRect(0.0, 0.0, 30.0, 15.0, 1, 1, 5, 1.0, -1879048192);
        FontManager.comfortaa.drawStringWithShadow("MC", 3.0f, 3.0f, GuiUtil.mouseOver(0.0, 0.0, 30.0, 15.0, mouseX, mouseY) ? ColorUtil.rainbowWave(4.0f, 1.0f, 1.0f, 0) : -1);
        GL11.glPushMatrix();
        GlStateManager.scale((float)2.0f, (float)2.0f, (float)0.0f);
        this.drawCenteredString(FontManager.aquireBig, ChatFormatting.UNDERLINE + "Xeno Client", sr.getScaledWidth() / 4, sr.getScaledHeight() / 4 - 25, GuiUtil.mouseOver(minX, minY, maxX, maxY, mouseX, mouseY) ? ColorUtil.rainbowWave(6.0f, 0.5f, 1.0f, 0) : -1);
        GL11.glPopMatrix();
        ArrayList<String> buttons = new ArrayList<String>();
        buttons.add("Singleplayer");
        buttons.add("Multiplayer");
        buttons.add("Settings");
        buttons.add("Quit");
        int totalStringLength = 0;
        for (String s : buttons) {
            totalStringLength += FontManager.comfortaaBig.getStringWidth(s) + 20;
        }
        int x = sr.getScaledWidth() / 2 - totalStringLength / 2;
        for (String str : buttons) {
            FontManager.comfortaaBig.drawStringWithShadow(str, x, sr.getScaledHeight() / 2, GuiUtil.mouseOver(x, sr.getScaledHeight() / 2, x + FontManager.comfortaaBig.getStringWidth(str) + 19, sr.getScaledHeight() / 2 + FontManager.comfortaaBig.getHeight(), mouseX, mouseY) ? ColorUtil.rainbowWave(4.0f, 1.0f, 1.0f, 0) : -1);
            x += FontManager.comfortaaBig.getStringWidth(str) + 20;
        }
        if (GuiUtil.mouseOver(minX, minY, maxX, maxY, mouseX, mouseY)) {
            RenderUtils2D.drawRoundedRect(sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() / 2 - 105, 200.0, 45.0, 5.0, -1879048192);
            FontManager.comfortaa.drawRainbowStringWithShadow("Authors", sr.getScaledWidth() / 2 - 95, sr.getScaledHeight() / 2 - 100, -1, 4.0f, 1.0f, 1);
            FontManager.comfortaa.drawStringWithShadow("Wolfsurge, Mathew101Q, q3m", sr.getScaledWidth() / 2 - 95, sr.getScaledHeight() / 2 - 86, -1);
            FontManager.comfortaa.drawStringWithShadow("SoldierMC, GameFighterIron", sr.getScaledWidth() / 2 - 95, sr.getScaledHeight() / 2 - 72, -1);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void onGuiClosed() {
        super.onGuiClosed();
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution sr = new ScaledResolution(this.mc);
        ArrayList<String> buttons = new ArrayList<String>();
        buttons.add("Singleplayer");
        buttons.add("Multiplayer");
        buttons.add("Settings");
        buttons.add("Quit");
        int totalStringLength = 0;
        for (String string : buttons) {
            totalStringLength += FontManager.comfortaaBig.getStringWidth(string) + 20;
        }
        if (mouseButton == 0) {
            int maxY;
            int maxX;
            int minY;
            int x = sr.getScaledWidth() / 2 - totalStringLength / 2;
            for (String str : buttons) {
                if (GuiUtil.mouseOver(x, sr.getScaledHeight() / 2, x + FontManager.comfortaaBig.getStringWidth(str) + 20, sr.getScaledHeight() / 2 + FontManager.comfortaaBig.getHeight(), mouseX, mouseY)) {
                    if (str == "Singleplayer") {
                        this.mc.displayGuiScreen((GuiScreen)new GuiWorldSelection((GuiScreen)this));
                    } else if (str == "Multiplayer") {
                        this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
                    } else if (str == "Settings") {
                        this.mc.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.mc.gameSettings));
                    } else if (str == "Quit") {
                        this.mc.shutdown();
                    }
                }
                x += FontManager.comfortaaBig.getStringWidth(str) + 19;
            }
            int n = sr.getScaledWidth() / 2 - FontManager.comfortaaBig.getStringWidth("Xeno Client") - 5;
            if (GuiUtil.mouseOver(n, minY = sr.getScaledHeight() / 2 - 55, maxX = n + FontManager.comfortaaBig.getStringWidth("Xeno Client") * 2 + 5, maxY = minY + 40, mouseX, mouseY) && mouseButton == 0) {
                Desktop d = Desktop.getDesktop();
                try {
                    d.browse(new URI("https://discord.gg/YPeVBdZMQA"));
                }
                catch (URISyntaxException uRISyntaxException) {
                    // empty catch block
                }
            }
            if (GuiUtil.mouseOver(0.0, 0.0, 30.0, 15.0, mouseX, mouseY)) {
                this.mc.displayGuiScreen((GuiScreen)new GuiMainMenu());
                GuiUtil.customMainMenu = false;
                Xeno.config.saveMisc();
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }
}

