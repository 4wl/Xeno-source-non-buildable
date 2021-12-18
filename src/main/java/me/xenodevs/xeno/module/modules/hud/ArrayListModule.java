/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.text.TextFormatting
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.module.modules.hud;

import java.util.ArrayList;
import java.util.Comparator;
import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.render.ColorUtil;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

public class ArrayListModule
extends Module {
    public static ModeSetting anchor = new ModeSetting("Anchor", "B Right", "B Left", "T Left", "T Right");
    public static BooleanSetting rainbowWave = new BooleanSetting("RainbowWave", true);
    public static BooleanSetting outline = new BooleanSetting("Outline", true);
    public static BooleanSetting background = new BooleanSetting("Background", true);
    public static NumberSetting rainbowSpeed = new NumberSetting("Speed", 4.0, 0.1, 10.0, 0.1);

    public ArrayListModule() {
        super("ArrayList", "Displays the enabled modules on screen.", Category.HUD, true);
        this.addSettings(anchor, rainbowWave, outline, background, rainbowSpeed);
    }

    public static void drawArrayList() {
        block21: {
            ArrayList<Object> enabledModules;
            int length;
            block24: {
                ScaledResolution sr;
                block23: {
                    block22: {
                        if (!Xeno.moduleManager.getModule("ArrayList").isEnabled()) break block21;
                        length = 11;
                        Minecraft mc = Minecraft.getMinecraft();
                        sr = new ScaledResolution(Minecraft.getMinecraft());
                        enabledModules = new ArrayList<Object>();
                        for (Module m : Xeno.moduleManager.getModules()) {
                            if (!m.isEnabled() || !m.visible.isEnabled()) continue;
                            enabledModules.add(m);
                        }
                        if (!anchor.is("T Right")) break block22;
                        float xOffset = sr.getScaledWidth() - 2;
                        float yOffset = 0.0f;
                        enabledModules.sort(Comparator.comparingInt(mod -> TextUtil.getStringWidth(((Module)mod).name + (Object)TextFormatting.GRAY + ((Module)mod).getHUDData())).reversed());
                        int index = 0;
                        int count = 0;
                        for (Module module : enabledModules) {
                            int outLineCol2;
                            int col = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1.0f, 1.0f, index) : Colors.colourInt;
                            int outLineCol1 = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1.0f, 1.0f, index) : Colors.colourInt;
                            int n = outLineCol2 = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1.0f, 1.0f, index + 150) : Colors.colourInt;
                            if (background.isEnabled()) {
                                RenderUtils2D.drawRect(xOffset - 2.0f - (float)TextUtil.getStringWidth(module.name + module.getHUDData()), yOffset, xOffset + 2.0f, yOffset + (float)length, -1879048192);
                            }
                            if (outline.isEnabled()) {
                                RenderUtils2D.drawGradientRect(xOffset - 3.0f - (float)TextUtil.getStringWidth(module.name + module.getHUDData()), yOffset, xOffset - 2.0f - (float)TextUtil.getStringWidth(module.name + module.getHUDData()), yOffset + (float)length, outLineCol1, outLineCol2, false);
                                String text = module.name + (Object)TextFormatting.GRAY + module.getHUDData();
                                int diff = 0;
                                int thing = 2;
                                String text2 = "";
                                try {
                                    text2 = ((Module)enabledModules.get((int)(count + 1))).name + (Object)TextFormatting.GRAY + ((Module)enabledModules.get(count + 1)).getHUDData();
                                    diff = Math.min(TextUtil.getStringWidth(text), TextUtil.getStringWidth(text2));
                                }
                                catch (Exception e) {
                                    thing = -2;
                                }
                                RenderUtils2D.drawGradientRect(xOffset - 3.0f - (float)TextUtil.getStringWidth(text), yOffset + (float)length - 1.0f, xOffset - (float)diff - (float)thing, yOffset + (float)length, outLineCol1, outLineCol2, false);
                            }
                            TextUtil.drawStringWithShadow(module.name + (Object)TextFormatting.GRAY + module.getHUDData(), xOffset - (float)TextUtil.getStringWidth(module.name + (Object)TextFormatting.GRAY + module.getHUDData()), yOffset - 1.0f, col);
                            yOffset += (float)length;
                            index += 150;
                            ++count;
                        }
                        break block21;
                    }
                    if (!anchor.is("B Right")) break block23;
                    float xOffset = sr.getScaledWidth() - 2;
                    float yOffset = sr.getScaledHeight() - length;
                    enabledModules.sort(Comparator.comparingInt(mod -> TextUtil.getStringWidth(((Module)mod).name + (Object)TextFormatting.GRAY + ((Module)mod).getHUDData())).reversed());
                    int index = 0;
                    int count = 0;
                    for (Module module : enabledModules) {
                        int outlineCol2;
                        int col = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1.0f, 1.0f, index) : Colors.colourInt;
                        int outlineCol1 = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1.0f, 1.0f, index) : Colors.colourInt;
                        int n = outlineCol2 = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1.0f, 1.0f, index + 150) : Colors.colourInt;
                        if (background.isEnabled()) {
                            RenderUtils2D.drawRect(xOffset - 2.0f - (float)TextUtil.getStringWidth(module.name + module.getHUDData()), yOffset, xOffset + 2.0f, yOffset + (float)length, -1879048192);
                        }
                        if (outline.isEnabled()) {
                            RenderUtils2D.drawGradientRect(xOffset - 3.0f - (float)TextUtil.getStringWidth(module.name + module.getHUDData()), yOffset, xOffset - 2.0f - (float)TextUtil.getStringWidth(module.name + module.getHUDData()), yOffset + (float)length, outlineCol1, outlineCol2, false);
                            String text = module.name + (Object)TextFormatting.GRAY + module.getHUDData();
                            int diff = 0;
                            int thing = 5;
                            String text2 = "";
                            try {
                                text2 = ((Module)enabledModules.get((int)(count + 1))).name + (Object)TextFormatting.GRAY + ((Module)enabledModules.get(count + 1)).getHUDData();
                                diff = Math.min(TextUtil.getStringWidth(text), TextUtil.getStringWidth(text2));
                            }
                            catch (Exception e) {
                                thing = 0;
                            }
                            RenderUtils2D.drawGradientRect(xOffset - 3.0f - (float)TextUtil.getStringWidth(text), yOffset, xOffset - (float)diff - (float)thing + 3.0f, yOffset + 1.0f, outlineCol1, outlineCol2, false);
                        }
                        TextUtil.drawStringWithShadow(module.name + (Object)TextFormatting.GRAY + module.getHUDData(), xOffset - (float)TextUtil.getStringWidth(module.name + (Object)TextFormatting.GRAY + module.getHUDData()), yOffset + 0.5f, col);
                        yOffset -= (float)length;
                        index += 150;
                        ++count;
                    }
                    break block21;
                }
                if (!anchor.is("B Left")) break block24;
                float xOffset = 2.0f;
                float yOffset = sr.getScaledHeight() - length;
                enabledModules.sort(Comparator.comparingInt(mod -> TextUtil.getStringWidth(((Module)mod).name + (Object)TextFormatting.GRAY + ((Module)mod).getHUDData())).reversed());
                int index = 0;
                int count = 0;
                for (Module module : enabledModules) {
                    int outlineCol2;
                    int col = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1.0f, 1.0f, index) : Colors.colourInt;
                    int outlineCol1 = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1.0f, 1.0f, index) : Colors.colourInt;
                    int n = outlineCol2 = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1.0f, 1.0f, index + 150) : Colors.colourInt;
                    if (background.isEnabled()) {
                        RenderUtils2D.drawRect(xOffset - 2.0f, yOffset, xOffset + 2.0f + (float)TextUtil.getStringWidth(module.name + module.getHUDData()), yOffset + (float)length, -1879048192);
                    }
                    if (outline.isEnabled()) {
                        RenderUtils2D.drawGradientRect(xOffset + 2.0f + (float)TextUtil.getStringWidth(module.name + module.getHUDData()), yOffset, xOffset + (float)TextUtil.getStringWidth(module.name + module.getHUDData()) + 3.0f, yOffset + (float)length, outlineCol1, outlineCol2, false);
                        String text = module.name + (Object)TextFormatting.GRAY + module.getHUDData();
                        int diff = 0;
                        String text2 = "";
                        try {
                            text2 = ((Module)enabledModules.get((int)(count + 1))).name + (Object)TextFormatting.WHITE + ((Module)enabledModules.get(count + 1)).getHUDData();
                            diff = TextUtil.getStringWidth(text) - TextUtil.getStringWidth(text2);
                        }
                        catch (Exception e) {
                            diff = TextUtil.getStringWidth(text) + 5;
                        }
                        GL11.glPushMatrix();
                        GL11.glTranslated((double)2.0, (double)0.0, (double)0.0);
                        RenderUtils2D.drawRect(xOffset + (float)TextUtil.getStringWidth(text) - (float)diff, yOffset, xOffset + (float)TextUtil.getStringWidth(text), yOffset + 1.0f, outlineCol1);
                        GL11.glPopMatrix();
                    }
                    TextUtil.drawStringWithShadow(module.name + (Object)TextFormatting.GRAY + module.getHUDData(), xOffset, yOffset + 0.5f, col);
                    yOffset -= (float)length;
                    index += 150;
                    ++count;
                }
                break block21;
            }
            if (!anchor.is("T Left")) break block21;
            float xOffset = 0.0f;
            float yOffset = 0.0f;
            enabledModules.sort(Comparator.comparingInt(mod -> TextUtil.getStringWidth(((Module)mod).name + (Object)TextFormatting.GRAY + ((Module)mod).getHUDData())).reversed());
            int index = 0;
            int count = 0;
            for (Module module : enabledModules) {
                int outLineCol2;
                int col = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1.0f, 1.0f, index) : Colors.colourInt;
                int outLineCol1 = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1.0f, 1.0f, index) : Colors.colourInt;
                int n = outLineCol2 = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1.0f, 1.0f, index + 150) : Colors.colourInt;
                if (background.isEnabled()) {
                    RenderUtils2D.drawRect(xOffset, yOffset, xOffset + (float)TextUtil.getStringWidth(module.name + module.getHUDData()) + 2.0f, yOffset + (float)length, -1879048192);
                }
                if (outline.isEnabled()) {
                    RenderUtils2D.drawGradientRect(xOffset - 3.0f - (float)TextUtil.getStringWidth(module.name + module.getHUDData()), yOffset, xOffset - (float)TextUtil.getStringWidth(module.name + module.getHUDData()), yOffset + (float)length, outLineCol1, outLineCol2, false);
                    String text = module.name + (Object)TextFormatting.GRAY + module.getHUDData();
                    int diff = 0;
                    String text2 = "";
                    try {
                        text2 = ((Module)enabledModules.get((int)(count + 1))).name + (Object)TextFormatting.WHITE + ((Module)enabledModules.get(count + 1)).getHUDData();
                        diff = TextUtil.getStringWidth(text) - TextUtil.getStringWidth(text2);
                    }
                    catch (Exception e) {
                        diff = TextUtil.getStringWidth(text) + 5;
                    }
                    RenderUtils2D.drawRect(xOffset + (float)TextUtil.getStringWidth(text) + 2.0f, yOffset, xOffset + (float)TextUtil.getStringWidth(text) + 3.0f, yOffset + (float)length + 1.0f, outLineCol1);
                    RenderUtils2D.drawRect(xOffset + (float)TextUtil.getStringWidth(text) + 2.0f, yOffset + (float)length, xOffset + (float)TextUtil.getStringWidth(text) + 4.0f - (float)diff - 2.0f, yOffset + (float)length + 1.0f, outLineCol1);
                }
                TextUtil.drawStringWithShadow(module.name + (Object)TextFormatting.GRAY + module.getHUDData(), xOffset, yOffset, col);
                yOffset += (float)length;
                index += 150;
                ++count;
            }
        }
    }
}

