/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.gui.click.component.components;

import java.util.ArrayList;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.click.ClickGui;
import me.xenodevs.xeno.gui.click.ClickGuiVariables;
import me.xenodevs.xeno.gui.click.component.Component;
import me.xenodevs.xeno.gui.click.component.Frame;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.BooleanComponent;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.ColourComponent;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.KeybindComponent;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.ModeComponent;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.SliderComponent;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.StringComponent;
import me.xenodevs.xeno.gui.click.theme.themes.FutureTheme;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.KeybindSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.module.settings.Setting;
import me.xenodevs.xeno.module.settings.StringSetting;

public class Button
extends Component {
    public Module mod;
    public Frame parent;
    public double offset;
    private boolean isHovered;
    public ArrayList<Component> subcomponents;
    public boolean open;
    private int height;

    public Button(Module mod, Frame parent, double offset) {
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.subcomponents = new ArrayList();
        this.open = false;
        this.height = ClickGuiVariables.buttonBarHeight;
        double opY = offset + (double)ClickGuiVariables.buttonBarHeight;
        for (Setting s : mod.settings) {
            if (s instanceof BooleanSetting) {
                BooleanComponent check = new BooleanComponent((BooleanSetting)s, this, opY);
                this.subcomponents.add(check);
                opY += (double)ClickGuiVariables.buttonBarHeight;
            }
            if (s instanceof NumberSetting) {
                SliderComponent slider = new SliderComponent((NumberSetting)s, this, opY);
                this.subcomponents.add(slider);
                opY += (double)ClickGuiVariables.buttonBarHeight;
            }
            if (s instanceof ModeSetting) {
                ModeComponent modeButton = new ModeComponent(this, (ModeSetting)s, opY);
                this.subcomponents.add(modeButton);
                opY += (double)ClickGuiVariables.buttonBarHeight;
            }
            if (s instanceof ColourPicker) {
                ColourComponent colourButton = new ColourComponent((ColourPicker)s, this, opY);
                this.subcomponents.add(colourButton);
                opY += (double)ClickGuiVariables.buttonBarHeight;
            }
            if (s instanceof StringSetting) {
                StringComponent sb = new StringComponent((StringSetting)s, this, opY);
                this.subcomponents.add(sb);
                opY += (double)ClickGuiVariables.buttonBarHeight;
            }
            if (!(s instanceof KeybindSetting)) continue;
            KeybindComponent keybind = new KeybindComponent(this, opY, (KeybindSetting)s);
            this.subcomponents.add(keybind);
            opY += (double)ClickGuiVariables.buttonBarHeight;
        }
    }

    public ArrayList<Component> getSubcomponents() {
        return this.subcomponents;
    }

    public void setSubcomponents(ArrayList<Component> subcomponents) {
        this.subcomponents = subcomponents;
    }

    @Override
    public void setOff(double newOff) {
        this.offset = newOff;
        double opY = this.offset + (double)ClickGuiVariables.buttonBarHeight;
        for (Component comp : this.subcomponents) {
            comp.setOff(opY);
            opY += (double)comp.getHeight();
        }
    }

    @Override
    public void renderComponent(int mouseX, int mouseY) {
        ClickGui.theme.drawButton(this.parent, this.mod, this.subcomponents, this.open, this.offset, mouseX, mouseY, this.isHovered);
    }

    @Override
    public int getHeight() {
        if (this.open) {
            int h = ClickGui.theme instanceof FutureTheme ? 1 : 0;
            for (Component c : this.subcomponents) {
                if (!(c instanceof ColourComponent) || !((ColourComponent)c).open) continue;
                h += ClickGuiVariables.buttonBarHeight * ClickGuiVariables.colourMultiplier;
            }
            return ClickGuiVariables.buttonBarHeight * (this.subcomponents.size() + 1) + h;
        }
        return ClickGuiVariables.buttonBarHeight;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.isHovered = this.isMouseOnButton(mouseX, mouseY);
        if (!this.subcomponents.isEmpty()) {
            for (Component comp : this.subcomponents) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0) {
            this.mod.toggle();
            if (ClickGuiModule.clickSound.enabled) {
                GuiUtil.clickSound();
            }
        }
        if (this.isMouseOnButton(mouseX, mouseY) && button == 1) {
            this.open = !this.open;
            this.parent.refresh();
            if (ClickGuiModule.clickSound.enabled) {
                GuiUtil.clickSound();
            }
        }
        for (Component comp : this.subcomponents) {
            comp.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (Component comp : this.subcomponents) {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        for (Component comp : this.subcomponents) {
            comp.keyTyped(typedChar, key);
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() && (double)y > (double)this.parent.getY() + this.offset && (double)y < (double)(this.parent.getY() + ClickGuiVariables.buttonBarHeight) + this.offset;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}

