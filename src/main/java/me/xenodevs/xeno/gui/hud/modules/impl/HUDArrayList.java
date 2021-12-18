/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.text.TextFormatting
 */
package me.xenodevs.xeno.gui.hud.modules.impl;

import java.util.ArrayList;
import java.util.Comparator;
import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.managers.ModuleManager;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.module.modules.hud.ArrayListModule;
import me.xenodevs.xeno.utils.render.ColorUtil;
import me.xenodevs.xeno.utils.render.DisplayUtils;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;

public class HUDArrayList
extends HUDMod {
    public int longestModName = 0;
    public int length = 0;
    ArrayList<Module> enabledModules = new ArrayList();

    public HUDArrayList() {
        super("ArrayList", DisplayUtils.getDisplayWidth() - TextUtil.getStringWidth("ArrayList"), 0, Xeno.moduleManager.getModule("ArrayList"));
    }

    @Override
    public void draw() {
        int count = 0;
        this.update();
        this.enabledModules = new ArrayList();
        String mode = "FromRight";
        String vertMode = "Top";
        for (Module m : Xeno.moduleManager.getModules()) {
            if (!m.isEnabled() || !m.visible.enabled) continue;
            this.enabledModules.add(m);
        }
        ScaledResolution sr = new ScaledResolution(mc);
        if (this.getX() + this.getWidth() / 2 < sr.getScaledWidth() / 2) {
            mode = "FromLeft";
        }
        int textSeperation = ArrayListModule.background.isEnabled() ? 12 : 10;
        float sec = (float)ArrayListModule.rainbowSpeed.getDoubleValue();
        int seperation = 150;
        float yOffset = 0.0f;
        float xOffset = 0.0f;
        for (Module m : this.enabledModules) {
            int rainbowTwo;
            int rainbowThing;
            String text = m.name + (Object)TextFormatting.GRAY + m.getHUDData();
            int n = this.parent.enabled ? (ArrayListModule.rainbowWave.enabled ? ColorUtil.rainbowWave(sec, 1.0f, 1.0f, count * seperation) : Colors.colourInt) : (rainbowThing = -7340032);
            int n2 = this.parent.enabled ? (ArrayListModule.rainbowWave.enabled ? ColorUtil.rainbowWave(sec, 1.0f, 1.0f, (count + 1) * seperation) : Colors.colourInt) : (rainbowTwo = -7340032);
            if (vertMode == "Top") {
                int formula;
                Xeno.moduleManager.getModules().sort(Comparator.comparingInt(mod -> TextUtil.getStringWidth(((Module)mod).name + (Object)TextFormatting.GRAY + ((Module)mod).getHUDData())).reversed());
                if (ArrayListModule.background.enabled) {
                    if (mode == "FromRight") {
                        Gui.drawRect((int)((int)((float)this.getX() + this.drag.getWidth() - 5.0f - (float)TextUtil.getStringWidth(text))), (int)(this.getY() + count * textSeperation), (int)(this.getX() + this.getWidth()), (int)(this.getY() + count * textSeperation + textSeperation), (int)0x70000000);
                    } else if (mode == "FromLeft") {
                        Gui.drawRect((int)this.getX(), (int)(this.getY() + count * textSeperation), (int)(this.getX() + TextUtil.getStringWidth(text) + 5), (int)(this.getY() + count * textSeperation + textSeperation), (int)0x70000000);
                    }
                }
                if (ArrayListModule.outline.enabled && mode == "FromRight" && ArrayListModule.background.enabled) {
                    RenderUtils2D.drawGradientRect((float)this.getX() + this.drag.getWidth() - 6.0f - (float)TextUtil.getStringWidth(text), this.getY() + count * textSeperation, this.getX() + this.getWidth() - 5 - TextUtil.getStringWidth(text), this.getY() + count * textSeperation + textSeperation, rainbowThing, rainbowTwo, false);
                }
                if (ArrayListModule.outline.enabled && mode == "FromLeft" && ArrayListModule.background.enabled) {
                    Gui.drawRect((int)(this.getX() + TextUtil.getStringWidth(text) + 5), (int)(this.getY() + count * textSeperation), (int)(this.getX() + TextUtil.getStringWidth(text) + 6), (int)(this.getY() + count * textSeperation + textSeperation), (int)rainbowThing);
                }
                if (mode == "FromRight") {
                    formula = this.getY() + count * textSeperation;
                    TextUtil.drawStringWithShadow(text, (int)((float)this.getX() + this.drag.getWidth() - 3.0f - (float)TextUtil.getStringWidth(text)), formula, rainbowThing);
                } else if (mode == "FromLeft") {
                    formula = this.getY() + count * textSeperation;
                    TextUtil.drawStringWithShadow(text, this.getX() + 2, formula, rainbowThing);
                }
                if (ArrayListModule.outline.enabled && ArrayListModule.background.enabled) {
                    int diff;
                    if (mode == "FromRight") {
                        diff = 0;
                        int diff2 = 0;
                        int thing = 5;
                        String text2 = "";
                        if (!this.enabledModules.isEmpty() && this.enabledModules.get(0) == m) {
                            RenderUtils2D.drawGradientRect((float)this.getX() + this.drag.getWidth() - 6.0f - (float)TextUtil.getStringWidth(text), this.getY() + count * textSeperation, this.getX() + this.getWidth(), this.getY() + count * textSeperation + 1, rainbowThing, rainbowTwo, false);
                        }
                        try {
                            text2 = this.enabledModules.get((int)(count + 1)).name + (Object)TextFormatting.GRAY + this.enabledModules.get(count + 1).getHUDData();
                            diff = Math.min(TextUtil.getStringWidth(text), TextUtil.getStringWidth(text2));
                        }
                        catch (Exception e) {
                            thing = 0;
                        }
                        RenderUtils2D.drawGradientRect((float)this.getX() + this.drag.getWidth() - 6.0f - (float)TextUtil.getStringWidth(text), this.getY() + count * textSeperation + 11, this.getX() + this.getWidth() - diff - thing + diff2, this.getY() + count * textSeperation + textSeperation, rainbowThing, rainbowTwo, false);
                        RenderUtils2D.drawGradientRect((float)this.getX() + this.drag.getWidth() - 1.0f, this.getY() + count * textSeperation, (float)this.getX() + this.drag.getWidth(), this.getY() + count * textSeperation + 12, rainbowThing, rainbowTwo, false);
                    } else if (mode == "FromLeft") {
                        if (!this.enabledModules.isEmpty() && this.enabledModules.get(0) == m) {
                            RenderUtils2D.drawGradientRect(this.getX(), this.getY() + count * textSeperation, this.getX() + TextUtil.getStringWidth(text) + 5, this.getY() + count * textSeperation + 1, rainbowThing, rainbowTwo, false);
                        }
                        diff = 0;
                        String text2 = "";
                        try {
                            text2 = this.enabledModules.get((int)(count + 1)).name + (Object)TextFormatting.GRAY + this.enabledModules.get(count + 1).getHUDData();
                            diff = TextUtil.getStringWidth(text) - TextUtil.getStringWidth(text2);
                        }
                        catch (Exception e) {
                            diff = TextUtil.getStringWidth(text) + 5;
                        }
                        Gui.drawRect((int)(this.getX() + TextUtil.getStringWidth(text) + 5), (int)(this.getY() + count * textSeperation + 11), (int)(this.getX() + TextUtil.getStringWidth(text) + 6 - diff - 1), (int)(this.getY() + count * textSeperation + 12), (int)rainbowThing);
                        RenderUtils2D.drawGradientRect(this.getX(), this.getY() + count * textSeperation, this.getX() + 1, this.getY() + count * textSeperation + textSeperation, rainbowThing, rainbowTwo, false);
                    }
                }
                if (TextUtil.getStringWidth(text) > this.longestModName) {
                    this.longestModName = TextUtil.getStringWidth(text);
                }
            }
            ++count;
        }
        if (!this.enabledModules.isEmpty()) {
            this.drag.setHeight(count * textSeperation);
        }
        this.drag.setWidth(this.getWidth());
        super.draw();
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        int textSeperation;
        super.renderDummy(mouseX, mouseY);
        if (!this.drag.isMouseOver) {
            this.update();
        }
        int count = 0;
        this.enabledModules = new ArrayList();
        String mode = "FromRight";
        String vertMode = "Top";
        for (Module m : ModuleManager.modules) {
            if (!m.isEnabled() || !m.visible.enabled) continue;
            this.enabledModules.add(m);
        }
        ScaledResolution sr = new ScaledResolution(mc);
        if (this.getX() + this.getWidth() / 2 < sr.getScaledWidth() / 2) {
            mode = "FromLeft";
        }
        int n = textSeperation = ArrayListModule.background.isEnabled() ? 12 : 10;
        if (this.enabledModules.isEmpty()) {
            this.drag.setHeight(11.0f);
            this.drag.setWidth(TextUtil.getStringWidth(this.name));
            TextUtil.drawStringWithShadow(this.name, this.drag.getX(), this.drag.getY() + 2, this.isEnabled() ? Colors.colourInt : -1869611008);
            super.renderDummy(mouseX, mouseY);
            return;
        }
        float sec = (float)ArrayListModule.rainbowSpeed.getDoubleValue();
        int seperation = 150;
        for (Module m : this.enabledModules) {
            int rainbowTwo;
            int rainbowThing;
            String text = m.name + (Object)TextFormatting.GRAY + m.getHUDData();
            int n2 = this.parent.enabled ? (ArrayListModule.rainbowWave.enabled ? ColorUtil.rainbowWave(sec, 1.0f, 1.0f, count * seperation) : Colors.colourInt) : (rainbowThing = -7340032);
            int n3 = this.parent.enabled ? (ArrayListModule.rainbowWave.enabled ? ColorUtil.rainbowWave(sec, 1.0f, 1.0f, (count + 1) * seperation) : Colors.colourInt) : (rainbowTwo = -7340032);
            if (vertMode == "Top") {
                int formula;
                ModuleManager.modules.sort(Comparator.comparingInt(mod -> TextUtil.getStringWidth(((Module)mod).name + (Object)TextFormatting.GRAY + ((Module)mod).getHUDData())).reversed());
                if (ArrayListModule.background.enabled) {
                    if (mode == "FromRight") {
                        Gui.drawRect((int)((int)((float)this.getX() + this.drag.getWidth() - 5.0f - (float)TextUtil.getStringWidth(text))), (int)(this.getY() + count * textSeperation), (int)(this.getX() + this.getWidth()), (int)(this.getY() + count * textSeperation + textSeperation), (int)0x70000000);
                    } else if (mode == "FromLeft") {
                        Gui.drawRect((int)this.getX(), (int)(this.getY() + count * textSeperation), (int)(this.getX() + TextUtil.getStringWidth(text) + 5), (int)(this.getY() + count * textSeperation + textSeperation), (int)0x70000000);
                    }
                }
                if (ArrayListModule.outline.enabled && mode == "FromRight" && ArrayListModule.background.enabled) {
                    RenderUtils2D.drawGradientRect((float)this.getX() + this.drag.getWidth() - 6.0f - (float)TextUtil.getStringWidth(text), this.getY() + count * textSeperation, this.getX() + this.getWidth() - 5 - TextUtil.getStringWidth(text), this.getY() + count * textSeperation + textSeperation, rainbowThing, rainbowTwo, false);
                }
                if (ArrayListModule.outline.enabled && mode == "FromLeft" && ArrayListModule.background.enabled) {
                    Gui.drawRect((int)(this.getX() + TextUtil.getStringWidth(text) + 5), (int)(this.getY() + count * textSeperation), (int)(this.getX() + TextUtil.getStringWidth(text) + 6), (int)(this.getY() + count * textSeperation + textSeperation), (int)rainbowThing);
                }
                if (mode == "FromRight") {
                    formula = this.getY() + count * textSeperation;
                    TextUtil.drawStringWithShadow(text, (int)((float)this.getX() + this.drag.getWidth() - 3.0f - (float)TextUtil.getStringWidth(text)), formula, rainbowThing);
                } else if (mode == "FromLeft") {
                    formula = this.getY() + count * textSeperation;
                    TextUtil.drawStringWithShadow(text, this.getX() + 2, formula, rainbowThing);
                }
                if (ArrayListModule.outline.enabled && ArrayListModule.background.enabled) {
                    int diff;
                    if (mode == "FromRight") {
                        diff = 0;
                        int diff2 = 0;
                        int thing = 5;
                        String text2 = "";
                        if (!this.enabledModules.isEmpty() && this.enabledModules.get(0) == m) {
                            RenderUtils2D.drawGradientRect((float)this.getX() + this.drag.getWidth() - 6.0f - (float)TextUtil.getStringWidth(text), this.getY() + count * textSeperation, this.getX() + this.getWidth(), this.getY() + count * textSeperation + 1, rainbowThing, rainbowTwo, false);
                        }
                        try {
                            text2 = this.enabledModules.get((int)(count + 1)).name + (Object)TextFormatting.GRAY + this.enabledModules.get(count + 1).getHUDData();
                            diff = Math.min(TextUtil.getStringWidth(text), TextUtil.getStringWidth(text2));
                        }
                        catch (Exception e) {
                            thing = 0;
                        }
                        RenderUtils2D.drawGradientRect((float)this.getX() + this.drag.getWidth() - 6.0f - (float)TextUtil.getStringWidth(text), this.getY() + count * textSeperation + 11, this.getX() + this.getWidth() - diff - thing + diff2, this.getY() + count * textSeperation + textSeperation, rainbowThing, rainbowTwo, false);
                        RenderUtils2D.drawGradientRect((float)this.getX() + this.drag.getWidth() - 1.0f, this.getY() + count * textSeperation, (float)this.getX() + this.drag.getWidth(), this.getY() + count * textSeperation + 12, rainbowThing, rainbowTwo, false);
                    } else if (mode == "FromLeft") {
                        if (!this.enabledModules.isEmpty() && this.enabledModules.get(0) == m) {
                            RenderUtils2D.drawGradientRect(this.getX(), this.getY() + count * textSeperation, this.getX() + TextUtil.getStringWidth(text) + 5, this.getY() + count * textSeperation + 1, rainbowThing, rainbowTwo, false);
                        }
                        diff = 0;
                        String text2 = "";
                        try {
                            text2 = this.enabledModules.get((int)(count + 1)).name + (Object)TextFormatting.WHITE + this.enabledModules.get(count + 1).getHUDData();
                            diff = TextUtil.getStringWidth(text) - TextUtil.getStringWidth(text2);
                        }
                        catch (Exception e) {
                            diff = TextUtil.getStringWidth(text) + 5;
                        }
                        Gui.drawRect((int)(this.getX() + TextUtil.getStringWidth(text) + 5), (int)(this.getY() + count * textSeperation + 11), (int)(this.getX() + TextUtil.getStringWidth(text) + 6 - diff - 1), (int)(this.getY() + count * textSeperation + 12), (int)rainbowThing);
                        RenderUtils2D.drawGradientRect(this.getX(), this.getY() + count * textSeperation, this.getX() + 1, this.getY() + count * textSeperation + textSeperation, rainbowThing, rainbowTwo, false);
                    }
                }
                if (TextUtil.getStringWidth(text) > this.longestModName) {
                    this.longestModName = TextUtil.getStringWidth(text);
                }
            }
            if (ArrayListModule.outline.enabled && ArrayListModule.background.enabled && vertMode == "Bottom") {
                if (mode == "FromRight") {
                    RenderUtils2D.drawGradientRect(this.getX() + this.getWidth() - 1, this.getY(), this.getX() + this.getWidth(), this.getY() + count * textSeperation + textSeperation, this.parent.enabled ? (ArrayListModule.rainbowWave.enabled ? ColorUtil.rainbowWave(sec, 1.0f, 1.0f, 0) : Colors.colourInt) : -7340032, rainbowThing, false);
                } else if (mode == "FromLeft") {
                    RenderUtils2D.drawGradientRect(this.getX(), this.getY(), this.getX() + 1, this.getY() + count * textSeperation + textSeperation, this.parent.enabled ? (ArrayListModule.rainbowWave.enabled ? ColorUtil.rainbowWave(sec, 1.0f, 1.0f, 0) : Colors.colourInt) : -7340032, rainbowThing, false);
                }
            }
            ++count;
        }
        if (!this.enabledModules.isEmpty()) {
            this.drag.setHeight(count * textSeperation);
        }
        this.drag.setWidth(this.getWidth());
    }

    @Override
    public int getWidth() {
        return this.longestModName + 4;
    }

    @Override
    public float getHeight() {
        return 10.0f;
    }
}

