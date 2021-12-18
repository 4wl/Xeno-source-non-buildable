/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.gui.click.component.components.subcomponents;

import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.click.ClickGui;
import me.xenodevs.xeno.gui.click.ClickGuiVariables;
import me.xenodevs.xeno.gui.click.component.Component;
import me.xenodevs.xeno.gui.click.component.components.Button;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.settings.ModeSetting;

public class ModeComponent
extends Component {
    private boolean hovered;
    private Button parent;
    public ModeSetting op;
    public double offset;
    private int x;
    private double y;

    public ModeComponent(Button button, ModeSetting op, double offset) {
        this.parent = button;
        this.op = op;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = (double)button.parent.getY() + button.offset;
        this.offset = offset;
    }

    @Override
    public void setOff(double newOff) {
        this.offset = newOff;
    }

    @Override
    public int getHeight() {
        return ClickGuiVariables.buttonBarHeight;
    }

    @Override
    public void renderComponent(int mouseX, int mouseY) {
        ClickGui.theme.drawMode(this.op, this.parent, this.offset, mouseX, mouseY, this.hovered);
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = this.isMouseOnButton(mouseX, mouseY);
        this.y = (double)this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.op.cycle();
            if (ClickGuiModule.clickSound.enabled) {
                GuiUtil.clickSound();
            }
            if (this.op == ClickGuiModule.theme) {
                ClickGui.themeSwitch();
            }
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + ClickGuiVariables.frameWidth && (double)y > this.y && (double)y < this.y + (double)ClickGuiVariables.buttonBarHeight;
    }
}

