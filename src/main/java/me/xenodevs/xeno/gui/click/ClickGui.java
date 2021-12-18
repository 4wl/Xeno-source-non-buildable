/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  org.lwjgl.input.Mouse
 */
package me.xenodevs.xeno.gui.click;

import java.io.IOException;
import java.util.ArrayList;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.click.ClickGuiVariables;
import me.xenodevs.xeno.gui.click.component.Component;
import me.xenodevs.xeno.gui.click.component.Frame;
import me.xenodevs.xeno.gui.click.component.components.Button;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.KeybindComponent;
import me.xenodevs.xeno.gui.click.theme.Theme;
import me.xenodevs.xeno.gui.click.theme.themes.FutureTheme;
import me.xenodevs.xeno.gui.click.theme.themes.PlainTheme;
import me.xenodevs.xeno.gui.click.theme.themes.XenoTheme;
import me.xenodevs.xeno.managers.ModuleManager;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.utils.other.Timer;
import me.xenodevs.xeno.utils.render.DisplayUtils;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

public class ClickGui
extends GuiScreen {
    public static ArrayList<Frame> frames;
    public static int color;
    public static int frameX;
    public static Theme theme;
    Timer timer = new Timer();
    public boolean isDraggingFrame = false;
    public static int maxLength;

    public ClickGui() {
        ClickGui.themeSwitch();
        frames = new ArrayList();
        int x = DisplayUtils.getDisplayWidth() / 2 - Category.values().length * (ClickGuiVariables.frameWidth + 6) / 2;
        frames.clear();
        for (Category category : Category.values()) {
            Frame frame = new Frame(category);
            frames.add(frame);
        }
        for (Frame f : frames) {
            f.setX(x);
            f.setY(20);
            x += ClickGuiVariables.frameWidth + 6;
            f.setOpen(true);
            f.refresh();
        }
        Xeno.logger.info("Created ClickGUI");
    }

    public static void reset() {
        int x = DisplayUtils.getDisplayWidth() / 2 - Category.values().length * (ClickGuiVariables.frameWidth + 6) / 2;
        frames.clear();
        for (Category category : Category.values()) {
            Frame frame = new Frame(category);
            frames.add(frame);
        }
        for (Frame f : frames) {
            f.setX(x);
            f.setY(20);
            x += ClickGuiVariables.frameWidth + 6;
            f.setOpen(true);
        }
    }

    public void initGui() {
        for (Frame f : frames) {
            f.refresh();
        }
    }

    public static void themeSwitch() {
        if (ClickGuiModule.theme.is("Xeno")) {
            ClickGuiVariables.buttonBarHeight = 14;
            theme = new XenoTheme();
        } else if (ClickGuiModule.theme.is("Plain")) {
            ClickGuiVariables.buttonBarHeight = 13;
            theme = new PlainTheme();
        } else if (ClickGuiModule.theme.is("Future")) {
            ClickGuiVariables.buttonBarHeight = 15;
            theme = new FutureTheme();
        }
        ClickGuiModule.reset.toggle();
    }

    public void onGuiClosed() {
        for (Module m : ModuleManager.modules) {
            Xeno.config.saveModConfig(m);
        }
        Xeno.config.saveClickGUIConfig();
        super.onGuiClosed();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (ClickGuiModule.darken.isEnabled()) {
            this.drawDefaultBackground();
        }
        for (Frame f : frames) {
            f.refresh();
        }
        color = Colors.colourInt;
        theme.drawScreen();
        String descText = "";
        boolean renderDesc = false;
        for (Frame frame : frames) {
            frame.renderFrame(this.fontRenderer, mouseX, mouseY);
            frame.updatePosition(mouseX, mouseY);
            for (Component comp : frame.getComponents()) {
                comp.updateComponent(mouseX, mouseY);
                if (!(comp instanceof Button) || ClickGui.theme.isScrollable && !this.mouseOver(frame.getX() - 2, frame.getY() + 3 + ClickGuiVariables.frameBarHeight, frame.getX() + ClickGuiVariables.frameWidth + 2, frame.getY() + 3 + ClickGuiVariables.frameBarHeight + maxLength, mouseX, mouseY) || !((Button)comp).isMouseOnButton(mouseX, mouseY)) continue;
                descText = ((Button)comp).mod.description;
                renderDesc = ClickGuiModule.desc.isEnabled() && frame.isOpen();
            }
            if (!ClickGuiVariables.limitFrameXandYToDisplaySides) continue;
            if (frame.getX() - 2 < 0) {
                frame.setX(2);
            }
            if (frame.getX() > DisplayUtils.getDisplayWidth() - frame.getWidth()) {
                frame.setX(DisplayUtils.getDisplayWidth() - frame.getWidth());
            }
            if (frame.getY() <= DisplayUtils.getDisplayHeight() - frame.getBarHeight()) continue;
            frame.setY(DisplayUtils.getDisplayHeight() - frame.getBarHeight());
        }
        if (renderDesc && this.timer.hasTimeElapsed(1000L, false)) {
            theme.drawDescription(descText, this.timer, mouseX, mouseY);
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
            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0 && !this.isDraggingFrame) {
                frame.setDrag(true);
                frame.dragX = mouseX - frame.getX();
                frame.dragY = mouseY - frame.getY();
                this.isDraggingFrame = true;
            }
            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
                if (ClickGuiModule.clickSound.enabled) {
                    GuiUtil.clickSound();
                }
                frame.setOpen(!frame.isOpen());
            }
            if (!frame.isOpen() || ClickGui.theme.isScrollable && !this.mouseOver(frame.getX() - 2, frame.getY() + 3 + ClickGuiVariables.frameBarHeight, frame.getX() + ClickGuiVariables.frameWidth + 2, frame.getY() + 3 + ClickGuiVariables.frameBarHeight + maxLength, mouseX, mouseY) || frame.getComponents().isEmpty()) continue;
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
                if (!(first.offset > (double)maxLength)) continue;
                first.offset = maxLength;
                f.refresh();
                return;
            }
            int dWheel = Mouse.getDWheel();
            if (dWheel < 0) {
                for (Frame f : frames) {
                    if (!f.isOpen() || !this.mouseOver(f.getX() - 2, f.getY() + ClickGuiVariables.frameBarHeight + 3, f.getX() + ClickGuiVariables.frameWidth + 2, f.getY() + ClickGuiVariables.frameBarHeight + 3 + maxLength, mouseX, mouseY)) continue;
                    for (Component c : f.components) {
                        if (!(c instanceof Button)) continue;
                        button = (Button)c;
                        if (((Button)f.getComponents().get((int)0)).offset + 0.5 > (double)ClickGuiVariables.frameBarHeight + 2.5) continue;
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
                    if (!f.isOpen() || !this.mouseOver(f.getX() - 2, f.getY() + ClickGuiVariables.frameBarHeight + 3, f.getX() + ClickGuiVariables.frameWidth + 2, f.getY() + ClickGuiVariables.frameBarHeight + 3 + maxLength, mouseX, mouseY)) continue;
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
        maxLength = ClickGuiVariables.buttonBarHeight * 15;
    }
}

