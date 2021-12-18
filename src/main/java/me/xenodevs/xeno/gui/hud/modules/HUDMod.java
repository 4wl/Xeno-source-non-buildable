/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraftforge.common.MinecraftForge
 */
package me.xenodevs.xeno.gui.hud.modules;

import java.awt.Color;
import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.DraggableComponent;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.utils.render.DisplayUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.MinecraftForge;

public class HUDMod
implements Globals {
    public FontRenderer fr;
    ScaledResolution sr;
    public String name;
    public DraggableComponent drag;
    public Module parent;
    public int x;
    public int y;
    public int defaultX;
    public int defaultY;
    public float width;
    public float height;

    public HUDMod(String name, int x, int y, Module parent) {
        this.fr = HUDMod.mc.fontRenderer;
        this.sr = new ScaledResolution(mc);
        this.defaultX = 0;
        this.defaultY = 0;
        this.width = 50.0f;
        this.height = 50.0f;
        this.defaultX = x;
        this.defaultY = y;
        this.name = name;
        this.parent = parent;
        this.sub();
        try {
            this.x = (Integer)Xeno.config.hudConfig.get(name.toLowerCase() + "X");
            this.y = (Integer)Xeno.config.hudConfig.get(name.toLowerCase() + "Y");
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            this.x = x;
            this.y = y;
        }
        try {
            this.setEnabled(Xeno.moduleManager.getModule((String)parent.name).enabled);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            this.parent.enabled = false;
        }
        this.drag = new DraggableComponent(this.x, this.y, this.getWidth(), this.getHeight(), new Color(0, 0, 0, 0).getRGB(), this);
    }

    public void sub() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        Xeno.EVENT_BUS.subscribe((Object)this);
    }

    public void refresh(int newX, int newY) {
        this.drag.setxPosition(newX);
        this.x = newX;
        this.drag.setyPosition(newY);
        this.y = newY;
    }

    public int getWidth() {
        return 50;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float newHeight) {
        this.height = newHeight;
    }

    public void update() {
        if (this.drag.getX() < 0) {
            this.drag.setX(0);
        }
        if ((float)this.drag.getX() > (float)DisplayUtils.getDisplayWidth() - this.drag.getWidth()) {
            this.drag.setX((int)((float)DisplayUtils.getDisplayWidth() - this.drag.getWidth()));
        }
        if (this.drag.getY() < 0) {
            this.drag.setY(0);
        }
        if ((float)this.drag.getY() > (float)DisplayUtils.getDisplayHeight() - this.drag.getHeight()) {
            this.drag.setY((int)((float)DisplayUtils.getDisplayHeight() - this.drag.getHeight()));
        }
    }

    public void draw() {
        this.update();
    }

    public void renderDummy(int mouseX, int mouseY) {
        this.drag.draw(mouseX, mouseY);
    }

    public int getX() {
        return this.drag.getxPosition();
    }

    public int getY() {
        return this.drag.getyPosition();
    }

    public boolean isEnabled() {
        return this.parent.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.parent.enabled = enabled;
    }
}

