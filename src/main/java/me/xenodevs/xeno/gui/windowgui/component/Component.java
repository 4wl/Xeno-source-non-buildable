/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package me.xenodevs.xeno.gui.windowgui.component;

import net.minecraft.client.Minecraft;

public abstract class Component {
    Minecraft mc = Minecraft.getMinecraft();

    public abstract void render(int var1, int var2);

    public void updateComponent(int mouseX, int mouseY) {
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    public void keyTyped(char typedChar, int key) {
    }

    public void setOff(double newOff) {
    }

    public int getHeight() {
        return 13;
    }
}

