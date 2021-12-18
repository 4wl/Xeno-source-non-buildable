/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.xenodevs.xeno.gui.hud;

import me.xenodevs.xeno.gui.hud.HudConfig;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import org.lwjgl.input.Mouse;

public class DraggableComponent {
    private int x;
    private int y;
    public float width;
    public float height;
    private int color;
    private int lastX;
    private int lastY;
    public int mouseX;
    public int mouseY;
    private boolean dragging;
    private boolean mouseButton1 = false;
    public boolean isMouseOver;
    private HUDMod parent;

    public DraggableComponent(int x, int y, float width, float height, int color, HUDMod parent) {
        this.width = width;
        this.height = height;
        this.setX(x);
        this.setY(y);
        this.color = color;
        this.parent = parent;
    }

    public int getxPosition() {
        return this.getX();
    }

    public int getyPosition() {
        return this.getY();
    }

    public void setxPosition(int x) {
        this.setX(x);
    }

    public void setyPosition(int y) {
        this.setY(y);
    }

    public float getHeight() {
        return this.height;
    }

    public float getWidth() {
        return this.width;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void draw(int mouseX, int mouseY) {
        this.draggingFix(mouseX, mouseY);
        boolean mouseOverX = mouseX >= this.getxPosition() && (float)mouseX <= (float)this.getxPosition() + this.getWidth();
        boolean mouseOverY = mouseY >= this.getyPosition() && (float)mouseY <= (float)this.getyPosition() + this.getHeight();
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        boolean bl = this.isMouseOver = mouseOverX && mouseOverY;
        if (mouseOverX && mouseOverY) {
            if (Mouse.isButtonDown((int)0) && !HudConfig.isAlreadyDragging && !this.dragging) {
                this.lastX = this.getX() - mouseX;
                this.lastY = this.getY() - mouseY;
                this.dragging = true;
                HudConfig.isAlreadyDragging = true;
            }
            if (Mouse.isButtonDown((int)1) && !this.mouseButton1) {
                this.parent.parent.enabled = !this.parent.parent.enabled;
            }
        }
        this.mouseButton1 = Mouse.isButtonDown((int)1);
    }

    private void draggingFix(int mouseX, int mouseY) {
        if (this.dragging) {
            this.setX(mouseX + this.lastX);
            this.setY(mouseY + this.lastY);
            if (!Mouse.isButtonDown((int)0)) {
                this.dragging = false;
            }
            this.parent.update();
        }
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

