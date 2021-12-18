/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.gui.taskbar;

import java.util.ArrayList;
import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.taskbar.TaskbarElement;
import me.xenodevs.xeno.gui.taskbar.impl.GuiElement;
import me.xenodevs.xeno.gui.taskbar.impl.HudElement;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.utils.render.ColorUtil;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class Taskbar
implements Globals {
    public ArrayList<TaskbarElement> elements = new ArrayList();

    public Taskbar() {
        this.init();
    }

    public void init() {
        this.elements.add(new GuiElement());
        this.elements.add(new HudElement());
    }

    public void renderTaskbar(int mouseX, int mouseY) {
        GL11.glPushMatrix();
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtils2D.drawRoundedRect(-10.0, sr.getScaledHeight() - 70, this.elements.size() * 64 + 20, sr.getScaledHeight() + 10, 10.0, -1879048192);
        RenderUtils2D.drawRect(0.0, sr.getScaledHeight() - 100, FontManager.comfortaaBig.getStringWidth("Xeno Client") + 10, sr.getScaledHeight() - 70, -1879048192);
        RenderUtils2D.drawLeftGradientRect(0, sr.getScaledHeight() - 100, FontManager.comfortaaBig.getStringWidth("Xeno Client") + 10, sr.getScaledHeight() - 98, ColorUtil.rainbowWave(4.0f, 1.0f, 1.0f, 0), ColorUtil.rainbowWave(4.0f, 1.0f, 1.0f, 100));
        FontManager.comfortaaBig.drawStringWithShadow("Xeno Client", 5.0f, sr.getScaledHeight() - 90, -1);
        GL11.glPopMatrix();
        for (TaskbarElement element : this.elements) {
            GL11.glPushMatrix();
            element.update();
            element.render();
            GL11.glPopMatrix();
        }
    }

    public void click(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            for (TaskbarElement element : this.elements) {
                if (!GuiUtil.mouseOver(element.x, element.y, element.x + 64, element.y + 64, mouseX, mouseY)) continue;
                element.onClick();
            }
        }
    }
}

