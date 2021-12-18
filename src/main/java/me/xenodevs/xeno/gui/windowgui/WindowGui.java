/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.util.text.TextFormatting
 *  org.lwjgl.input.Mouse
 */
package me.xenodevs.xeno.gui.windowgui;

import java.io.IOException;
import java.util.ArrayList;
import me.xenodevs.xeno.gui.windowgui.component.Component;
import me.xenodevs.xeno.gui.windowgui.component.WindowComponent;
import me.xenodevs.xeno.utils.render.DisplayUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;

public class WindowGui
extends GuiScreen {
    public ArrayList<Component> components = new ArrayList();
    public static TextFormatting textCol = TextFormatting.RED;

    public WindowGui() {
        this.components.add(new WindowComponent("Xeno Client 1.1", DisplayUtils.getDisplayWidth() / 2 - 216, DisplayUtils.getDisplayHeight() / 2 - 175, 432.0, 336.0));
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (Component c : this.components) {
            if (c instanceof WindowComponent) {
                c.updateComponent(mouseX, mouseY);
            }
            c.render(mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (Component c : this.components) {
            c.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (!Mouse.isButtonDown((int)0)) {
            for (Component comp : this.components) {
                if (!(comp instanceof WindowComponent)) continue;
                ((WindowComponent)comp).setDrag(false);
            }
        }
        for (Component comp : this.components) {
            if (!(comp instanceof WindowComponent)) continue;
            for (Component component : ((WindowComponent)comp).moduleComponents) {
                component.mouseReleased(mouseX, mouseY, state);
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }
}

