/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.gui.click.component;

import java.util.ArrayList;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.click.ClickGui;
import me.xenodevs.xeno.gui.click.ClickGuiVariables;
import me.xenodevs.xeno.gui.click.component.Component;
import me.xenodevs.xeno.gui.click.component.components.Button;
import me.xenodevs.xeno.gui.click.theme.themes.PlainTheme;
import me.xenodevs.xeno.gui.click.theme.themes.XenoTheme;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.utils.other.Timer;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

public class Frame {
    public ArrayList<Component> components;
    public Category category;
    private boolean open;
    private int width;
    private int y;
    private int x;
    private int barHeight;
    private boolean isDragging;
    public int dragX;
    public int dragY;
    Timer timer = new Timer();

    public Frame(Category cat) {
        this.components = new ArrayList();
        this.category = cat;
        this.width = ClickGuiVariables.frameWidth;
        this.x = 5;
        this.y = 5;
        this.barHeight = ClickGuiVariables.frameBarHeight;
        this.dragX = 0;
        this.open = false;
        this.isDragging = false;
        double tY = (double)this.barHeight + (ClickGui.theme.isScrollable ? 2.5 : 0.0);
        for (Module mod : Xeno.moduleManager.getModulesInCategory(this.category)) {
            Button modButton = new Button(mod, this, tY);
            this.components.add(modButton);
            if (ClickGui.theme instanceof XenoTheme) {
                tY += (double)ClickGuiVariables.buttonBarHeight - 0.5;
                continue;
            }
            tY += (double)ClickGuiVariables.buttonBarHeight;
        }
        try {
            this.x = (Integer)Xeno.config.clickGUIConfig.get(this.category.name() + "X");
            this.y = (Integer)Xeno.config.clickGUIConfig.get(this.category.name() + "Y");
            this.open = (Boolean)Xeno.config.clickGUIConfig.get(this.category.name() + "Open");
        }
        catch (NullPointerException e) {
            this.setY(10);
            this.setX(ClickGui.frameX);
            ClickGui.frameX += this.getWidth() + 6 + (ClickGui.theme instanceof PlainTheme ? 4 : 0);
        }
    }

    public void open() {
    }

    public void close() {
    }

    public ArrayList<Component> getComponents() {
        return this.components;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public void setDrag(boolean drag) {
        this.isDragging = drag;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
        if (open) {
            this.open();
        } else {
            this.close();
        }
    }

    public void renderFrame(FontRenderer fontRenderer, int mouseX, int mouseY) {
        GL11.glPushMatrix();
        ClickGui.theme.drawFrame(this.components, this.category, this.open, this.getX(), this.getY(), this.getWidth(), mouseX, mouseY);
        GL11.glPopMatrix();
    }

    public void refresh() {
        Button first = (Button)this.components.get(0);
        double off = ClickGui.theme.isScrollable ? first.offset : (double)this.barHeight;
        for (Component comp : this.components) {
            comp.setOff(off);
            off += (double)comp.getHeight();
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getBarHeight() {
        return this.barHeight;
    }

    public void updatePosition(int mouseX, int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
    }

    public boolean isWithinHeader(int x, int y) {
        return x >= this.x - 2 && x <= this.x + this.width + 2 && y >= this.y && y <= this.y + this.barHeight;
    }
}

