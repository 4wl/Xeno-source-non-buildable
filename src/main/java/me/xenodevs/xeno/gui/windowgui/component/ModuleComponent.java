/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.gui.windowgui.component;

import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.windowgui.component.Component;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.Colors;
import org.lwjgl.opengl.GL11;

public class ModuleComponent
extends Component {
    Module module;
    double x;
    double y;

    public ModuleComponent(Module module, double x, double y) {
        this.module = module;
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        boolean isMouseOver = GuiUtil.mouseOver(this.x, this.y, this.x + 140.0, this.y + 26.0, mouseX, mouseY);
        GuiUtil.drawBorderedRect(this.x, this.y, 140.0, 26.0, 1.0, -1, isMouseOver ? 0x70000000 : -1879048192);
        GL11.glPushMatrix();
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        this.mc.fontRenderer.drawStringWithShadow(this.module.name, (float)(this.x + 4.0) / 2.0f, (float)(this.y + 5.0) / 2.0f, this.module.isEnabled() ? Colors.colourInt : -1);
        GL11.glPopMatrix();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (GuiUtil.mouseOver(this.x, this.y, this.x + 140.0, this.y + 26.0, mouseX, mouseY) && button == 0) {
            this.module.toggle();
        }
        super.mouseClicked(mouseX, mouseY, button);
    }
}

