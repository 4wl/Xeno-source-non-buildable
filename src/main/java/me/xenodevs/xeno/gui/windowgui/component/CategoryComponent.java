/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.gui.windowgui.component;

import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.windowgui.component.Component;
import me.xenodevs.xeno.gui.windowgui.component.WindowComponent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.modules.client.Colors;

public class CategoryComponent
extends Component {
    public Category c;
    public double x;
    public double y;
    public double width;
    public double height;
    public WindowComponent parent;

    public CategoryComponent(Category c, double x, double y, double width, WindowComponent parent) {
        this.c = c;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = 13.0;
        this.parent = parent;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        boolean isMouseOver = GuiUtil.mouseOver(this.x, this.y, this.x + this.width, this.y + 13.0, mouseX, mouseY);
        GuiUtil.drawBorderedRect(this.x, this.y, this.width, this.height, 1.0, -1, isMouseOver ? 0x70000000 : -1879048192);
        this.mc.fontRenderer.drawStringWithShadow(this.c.name(), (float)(this.x + 4.0), (float)(this.y + 3.0), this.parent.currentCategory == this.c ? Colors.colourInt : -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (GuiUtil.mouseOver(this.x, this.y, this.x + this.width, this.y + this.height, mouseX, mouseY) && button == 0) {
            this.parent.currentCategory = this.c;
            this.parent.refreshModules();
        }
        super.mouseClicked(mouseX, mouseY, button);
    }
}

