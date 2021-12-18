/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.gui.windowgui.component;

import java.util.ArrayList;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.windowgui.component.CategoryComponent;
import me.xenodevs.xeno.gui.windowgui.component.Component;
import me.xenodevs.xeno.gui.windowgui.component.ModuleComponent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class WindowComponent
extends Component {
    static Minecraft mc = Minecraft.getMinecraft();
    public ArrayList<Component> moduleComponents;
    public ArrayList<Component> categoryComponents;
    double x;
    double y;
    double width;
    double height;
    String headerText;
    boolean isDragging = false;
    public double dragX;
    public double dragY;
    public Category currentCategory = Category.HUD;

    public WindowComponent(String headerText, double x, double y, double width, double height) {
        this.headerText = headerText;
        ScaledResolution sr = new ScaledResolution(mc);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.categoryComponents = new ArrayList();
        double cWidth = this.width / (double)Category.values().length - 3.0;
        double cX = 4.0;
        for (Category c : Category.values()) {
            this.categoryComponents.add(new CategoryComponent(c, x + cX, y + 26.0, cWidth, this));
            cX += cWidth + 2.0;
        }
        this.moduleComponents = new ArrayList();
        double xOff = x + 4.0;
        double yOff = y + 44.0;
        int counter = 0;
        for (Module m : Xeno.moduleManager.getModules()) {
            this.moduleComponents.add(new ModuleComponent(m, xOff, yOff));
            if (++counter == 3) {
                counter = 0;
                xOff = x + 4.0;
                yOff += 28.0;
                continue;
            }
            xOff += 142.0;
        }
        this.refreshModules();
    }

    public void refreshModules() {
        this.moduleComponents = new ArrayList();
        double xOff = this.x + 4.0;
        double yOff = this.y + 44.0;
        int counter = 0;
        for (Module m : Xeno.moduleManager.getModulesInCategory(this.currentCategory)) {
            this.moduleComponents.add(new ModuleComponent(m, xOff, yOff));
            if (++counter == 3) {
                counter = 0;
                xOff = this.x + 4.0;
                yOff += 28.0;
                continue;
            }
            xOff += 142.0;
        }
    }

    @Override
    public void render(int mouseX, int mouseY) {
        this.update(mouseX, mouseY);
        GuiUtil.drawBorderedRect(this.x, this.y, this.width, this.height, 1.0, -1, -1879048192);
        GL11.glPushMatrix();
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        WindowComponent.mc.fontRenderer.drawStringWithShadow(this.headerText, (float)(this.x + 5.0) / 2.0f, (float)(this.y + 5.0) / 2.0f, Colors.colourInt);
        GL11.glPopMatrix();
        RenderUtils2D.drawRect(this.x, this.y + 23.0, this.x + this.width, this.y + 24.0, -1);
        RenderUtils2D.drawRect(this.x + this.width - 21.0, this.y + 2.0, this.x + this.width - 2.0, this.y + 22.0, GuiUtil.mouseOver(this.x + this.width - 21.0, this.y + 2.0, this.x + this.width - 2.0, this.y + 22.0, mouseX, mouseY) ? 0x50000000 : -1879048192);
        GL11.glPushMatrix();
        GL11.glScaled((double)2.0, (double)2.0, (double)2.0);
        WindowComponent.mc.fontRenderer.drawString("x", (float)(this.x + this.width - 16.5) / 2.0f, (float)(this.y + 3.0) / 2.0f, -1, false);
        GL11.glPopMatrix();
        for (Component c : this.categoryComponents) {
            c.render(mouseX, mouseY);
        }
        RenderUtils2D.drawRect(this.x, this.y + 41.0, this.x + this.width, this.y + 42.0, -1);
        GL11.glPushMatrix();
        GL11.glPushAttrib((int)524288);
        RenderUtils2D.scissor(this.x, this.y + 44.0, this.x + this.width, this.height - 44.0);
        GL11.glEnable((int)3089);
        for (Component c : this.moduleComponents) {
            c.render(mouseX, mouseY);
        }
        GL11.glDisable((int)3089);
        GL11.glPopAttrib();
        GL11.glPopMatrix();
        this.handleMouseWheel(mouseX, mouseY);
    }

    public void update(int mouseX, int mouseY) {
        if (this.isDragging) {
            this.x = (double)mouseX - this.dragX;
            this.y = (double)mouseY - this.dragY;
            double xOff = this.x + 4.0;
            double yOff = this.y + 44.0;
            int counter = 0;
            for (Component c : this.moduleComponents) {
                if (!(c instanceof ModuleComponent)) continue;
                ((ModuleComponent)c).x = xOff;
                ((ModuleComponent)c).y = yOff;
                if (++counter == 3) {
                    counter = 0;
                    xOff = this.x + 4.0;
                    yOff += 28.0;
                    continue;
                }
                xOff += 142.0;
            }
            double cWidth = this.width / (double)Category.values().length - 3.0;
            double cX = this.x + 4.0;
            for (Component c : this.categoryComponents) {
                if (!(c instanceof CategoryComponent)) continue;
                ((CategoryComponent)c).x = cX;
                ((CategoryComponent)c).y = this.y + 26.0;
                cX += cWidth + 2.0;
            }
        }
    }

    public void setDrag(boolean drag) {
        this.isDragging = drag;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (GuiUtil.mouseOver(this.x, this.y + 2.0, this.x + this.width - 22.0, this.y + 22.0, mouseX, mouseY)) {
            this.setDrag(true);
            this.dragX = (double)mouseX - this.x;
            this.dragY = (double)mouseY - this.y;
        }
        if (GuiUtil.mouseOver(this.x + this.width - 21.0, this.y + 2.0, this.x + this.width - 2.0, this.y + 22.0, mouseX, mouseY) && button == 0) {
            mc.displayGuiScreen(null);
        }
        for (Component c : this.moduleComponents) {
            if (this.currentCategory != ((ModuleComponent)c).module.category) continue;
            c.mouseClicked(mouseX, mouseY, button);
        }
        for (Component c : this.categoryComponents) {
            c.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        this.setDrag(false);
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    public void handleMouseWheel(int mouseX, int mouseY) {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            for (Component c : this.moduleComponents) {
                ModuleComponent last = (ModuleComponent)this.moduleComponents.get(this.moduleComponents.size() - 1);
                if (!GuiUtil.mouseOver(this.x, this.y + 44.0, this.x + this.width, this.y + this.height, mouseX, mouseY) || !(last.y > this.y + 44.0) || !(c instanceof ModuleComponent)) continue;
                ((ModuleComponent)c).y -= 13.0;
            }
        } else if (dWheel > 0) {
            for (Component c : this.moduleComponents) {
                if (!GuiUtil.mouseOver(this.x, this.y + 44.0, this.x + this.width, this.y + this.height, mouseX, mouseY) || !(((ModuleComponent)this.moduleComponents.get((int)0)).y < this.y + this.height - 10.0)) continue;
                ((ModuleComponent)c).y += 13.0;
            }
        }
        try {
            if (((ModuleComponent)this.moduleComponents.get((int)0)).y > ((ModuleComponent)this.moduleComponents.get((int)1)).y) {
                ((ModuleComponent)this.moduleComponents.get((int)0)).y = ((ModuleComponent)this.moduleComponents.get((int)1)).y;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

