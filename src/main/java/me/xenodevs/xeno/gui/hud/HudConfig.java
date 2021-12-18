/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.util.text.TextFormatting
 *  org.lwjgl.input.Mouse
 */
package me.xenodevs.xeno.gui.hud;

import java.io.IOException;
import java.util.ArrayList;
import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.click.ClickGui;
import me.xenodevs.xeno.gui.click.ClickGuiVariables;
import me.xenodevs.xeno.gui.click.component.Component;
import me.xenodevs.xeno.gui.click.component.Frame;
import me.xenodevs.xeno.gui.click.component.components.Button;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.KeybindComponent;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.module.modules.hud.ArrayListModule;
import me.xenodevs.xeno.module.modules.hud.HUD;
import me.xenodevs.xeno.utils.other.Timer;
import me.xenodevs.xeno.utils.render.DisplayUtils;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;

public class HudConfig
extends GuiScreen {
    public static boolean isAlreadyDragging = false;
    public static ArrayList<Frame> frames;
    public static int color;
    public static int frameX;
    public boolean isDraggingFrame = false;

    public HudConfig() {
        frames = new ArrayList();
        frameX = 200;
        Frame f = new Frame(Category.HUD);
        try {
            f.setX((Integer)Xeno.config.clickGUIConfig.get(f.category.name() + "X"));
            f.setY((Integer)Xeno.config.clickGUIConfig.get(f.category.name() + "Y"));
            f.setOpen((Boolean)Xeno.config.clickGUIConfig.get(f.category.name() + "Open"));
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            f.setX(frameX);
            f.setY(20);
            f.setOpen(true);
        }
        frames.add(f);
    }

    public void initGui() {
    }

    public void onGuiClosed() {
        Xeno.config.saveHudConfig();
        super.onGuiClosed();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        color = Colors.colourInt;
        String descText = "";
        boolean renderDesc = false;
        this.drawDefaultBackground();
        if (HUD.blur.isEnabled()) {
            Xeno.blurManager.blur(3);
        }
        for (HUDMod m : Xeno.hudManager.hudMods) {
            if (!m.parent.isEnabled()) continue;
            m.renderDummy(mouseX, mouseY);
            FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
            if (m.drag == null || !m.drag.isMouseOver) continue;
            String enabledText = " " + (m.parent.enabled ? "[" + (Object)TextFormatting.GREEN + "Enabled" + (Object)TextFormatting.RESET + "]" : "[" + (Object)TextFormatting.RED + "Disabled" + (Object)TextFormatting.RESET + "]");
            RenderUtils2D.drawRoundedRect(mouseX + 14, mouseY - 6, TextUtil.getStringWidth(m.name + enabledText) + 7, 13.0, 5.0, -1879048192);
            RenderUtils2D.drawRoundedOutline(mouseX + 14, mouseY - 6, TextUtil.getStringWidth(m.name + enabledText) + 7, 13.0, 5.0, 2.0, Colors.colourInt);
            TextUtil.drawStringWithShadow(m.name + enabledText, mouseX + 17, mouseY - 5, -1);
        }
        ArrayListModule.drawArrayList();
        for (Frame frame : frames) {
            frame.renderFrame(this.fontRenderer, mouseX, mouseY);
            frame.updatePosition(mouseX, mouseY);
            for (Component comp : frame.getComponents()) {
                comp.updateComponent(mouseX, mouseY);
                if (!(comp instanceof Button) || !((Button)comp).isMouseOnButton(mouseX, mouseY)) continue;
                descText = ((Button)comp).mod.description;
                renderDesc = ClickGuiModule.desc.isEnabled() && frame.isOpen();
            }
            if (frame.getX() - 2 < 0) {
                frame.setX(2);
            }
            if (frame.getX() > DisplayUtils.getDisplayWidth() - frame.getWidth()) {
                frame.setX(DisplayUtils.getDisplayWidth() - frame.getWidth());
            }
            if (frame.getY() <= DisplayUtils.getDisplayHeight() - frame.getBarHeight()) continue;
            frame.setY(DisplayUtils.getDisplayHeight() - frame.getBarHeight());
        }
        if (renderDesc) {
            Timer timer = new Timer();
            ClickGui.theme.drawDescription(descText, timer, mouseX, mouseY);
        }
        this.checkMouseWheel(mouseX, mouseY);
        this.overlay();
        GuiUtil.renderButtons(mouseX, mouseY);
    }

    public void overlay() {
    }

    public boolean mouseOver(int minX, int minY, int maxX, int maxY, int mX, int mY) {
        return mX >= minX && mY >= minY && mX <= maxX && mY <= maxY;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (Frame frame : frames) {
            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0 && !this.isDraggingFrame && !isAlreadyDragging) {
                frame.setDrag(true);
                frame.dragX = mouseX - frame.getX();
                frame.dragY = mouseY - frame.getY();
                this.isDraggingFrame = true;
                isAlreadyDragging = true;
            }
            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
                if (ClickGuiModule.clickSound.enabled) {
                    GuiUtil.clickSound();
                }
                frame.setOpen(!frame.isOpen());
            }
            if (!frame.isOpen() || frame.getComponents().isEmpty()) continue;
            for (Component component : frame.getComponents()) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
        if (mouseButton == 0 || mouseButton == 1) {
            GuiUtil.handleButtons(mouseX, mouseY);
        }
    }

    protected void keyTyped(char typedChar, int keyCode) {
        for (Frame frame : frames) {
            if (!frame.isOpen() || keyCode == 1) continue;
            for (Component component : frame.getComponents()) {
                component.keyTyped(typedChar, keyCode);
            }
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (!Mouse.isButtonDown((int)0)) {
            for (Frame frame : frames) {
                frame.setDrag(false);
            }
            this.isDraggingFrame = false;
            isAlreadyDragging = false;
        }
        for (Frame frame : frames) {
            if (!frame.isOpen() || frame.getComponents().isEmpty()) continue;
            for (Component component : frame.getComponents()) {
                component.mouseReleased(mouseX, mouseY, state);
            }
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public void checkMouseWheel(int mouseX, int mouseY) {
        block13: {
            Button button;
            block11: {
                int dWheel;
                block12: {
                    if (ClickGui.theme.isScrollable) break block11;
                    dWheel = Mouse.getDWheel();
                    if (dWheel >= 0) break block12;
                    for (Frame f : frames) {
                        f.setY(f.getY() + f.getBarHeight() / 2);
                    }
                    break block13;
                }
                if (dWheel <= 0) break block13;
                for (Frame f : frames) {
                    f.setY(f.getY() - f.getBarHeight() / 2);
                }
                break block13;
            }
            for (Frame f : frames) {
                Button first = (Button)f.getComponents().get(0);
                if (!(first.offset > (double)ClickGui.maxLength)) continue;
                first.offset = ClickGui.maxLength;
                f.refresh();
                return;
            }
            int dWheel = Mouse.getDWheel();
            if (dWheel < 0) {
                for (Frame f : frames) {
                    if (!f.isOpen() || !this.mouseOver(f.getX() - 2, f.getY() + ClickGuiVariables.frameBarHeight + 3, f.getX() + ClickGuiVariables.frameWidth + 2, f.getY() + ClickGuiVariables.frameBarHeight + 3 + ClickGui.maxLength, mouseX, mouseY)) continue;
                    for (Component c : f.components) {
                        if (!(c instanceof Button)) continue;
                        button = (Button)c;
                        button.offset += (double)(ClickGuiVariables.buttonBarHeight / 2);
                    }
                }
            }
            for (Frame f : frames) {
                Button last = (Button)f.getComponents().get(f.components.size() - 1);
                if (!last.open) {
                    if (!(last.offset < (double)(ClickGuiVariables.frameBarHeight + 3))) continue;
                    last.offset = ClickGuiVariables.frameBarHeight + 3 + ClickGuiVariables.buttonBarHeight;
                    f.refresh();
                    return;
                }
                KeybindComponent lastSetting = (KeybindComponent)last.getSubcomponents().get(last.getSubcomponents().size() - 1);
                if (!(lastSetting.offset < (double)(ClickGuiVariables.frameBarHeight + 3))) continue;
                lastSetting.offset = ClickGuiVariables.frameBarHeight + 3 + ClickGuiVariables.buttonBarHeight;
                f.refresh();
                return;
            }
            if (dWheel > 0) {
                for (Frame f : frames) {
                    if (!f.isOpen() || !this.mouseOver(f.getX() - 2, f.getY() + ClickGuiVariables.frameBarHeight + 3, f.getX() + ClickGuiVariables.frameWidth + 2, f.getY() + ClickGuiVariables.frameBarHeight + 3 + ClickGui.maxLength, mouseX, mouseY)) continue;
                    for (Component c : f.components) {
                        if (!(c instanceof Button)) continue;
                        button = (Button)c;
                        button.offset -= (double)(ClickGuiVariables.buttonBarHeight / 2);
                    }
                }
            }
        }
    }

    static {
        color = ClickGuiVariables.clickGuiColor;
    }
}

