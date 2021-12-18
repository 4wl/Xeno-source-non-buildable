/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.gui.click.component.components.subcomponents;

import java.math.BigDecimal;
import java.math.RoundingMode;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.click.ClickGui;
import me.xenodevs.xeno.gui.click.ClickGuiVariables;
import me.xenodevs.xeno.gui.click.component.Component;
import me.xenodevs.xeno.gui.click.component.components.Button;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.settings.NumberSetting;

public class SliderComponent
extends Component {
    private boolean hovered;
    private NumberSetting val;
    private Button parent;
    public double offset;
    public int x;
    private double y;
    private boolean dragging = false;
    private double renderWidth;

    public SliderComponent(NumberSetting value, Button button, double offset) {
        this.val = value;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = (double)button.parent.getY() + button.offset;
        this.offset = offset;
    }

    @Override
    public int getHeight() {
        return ClickGuiVariables.buttonBarHeight;
    }

    @Override
    public void renderComponent(int mouseX, int mouseY) {
        ClickGui.theme.drawSlider(this.val, this.parent, this.offset, (int)this.renderWidth, this.hovered, mouseX, mouseY);
    }

    @Override
    public void setOff(double newOff) {
        this.offset = newOff;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = this.isMouseOnButtonD(mouseX, mouseY) || this.isMouseOnButtonI(mouseX, mouseY);
        this.y = (double)this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
        double diff = Math.min(ClickGuiVariables.frameWidth, Math.max(0, mouseX - this.x));
        double min = this.val.getMinimum();
        double max = this.val.getMaximum();
        this.renderWidth = (double)ClickGuiVariables.frameWidth * (this.val.getDoubleValue() - min) / (max - min);
        if (this.dragging) {
            if (diff == 0.0) {
                this.val.setValue(this.val.getMinimum());
            } else {
                double newValue = SliderComponent.roundToPlace(diff / (double)ClickGuiVariables.frameWidth * (max - min) + min, 2);
                this.val.setValue(newValue);
            }
        }
    }

    private static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.dragging = true;
            if (ClickGuiModule.clickSound.enabled) {
                GuiUtil.clickSound();
            }
        }
        if (this.isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.dragging = true;
            if (ClickGuiModule.clickSound.enabled) {
                GuiUtil.clickSound();
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        this.dragging = false;
    }

    public boolean isMouseOnButtonD(int x, int y) {
        return x > this.x && x < this.x + this.parent.parent.getWidth() / 2 && (double)y > this.y && (double)y < this.y + (double)ClickGuiVariables.buttonBarHeight;
    }

    public boolean isMouseOnButtonI(int x, int y) {
        return x > this.x + this.parent.parent.getWidth() / 2 && x < this.x + this.parent.parent.getWidth() && (double)y > this.y && (double)y < this.y + (double)ClickGuiVariables.buttonBarHeight;
    }
}

