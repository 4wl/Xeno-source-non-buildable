/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 */
package me.xenodevs.xeno.module.modules.client;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.click.ClickGui;
import me.xenodevs.xeno.gui.click.ClickGuiVariables;
import me.xenodevs.xeno.gui.windowgui.WindowGui;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import net.minecraft.client.gui.GuiScreen;

public class ClickGuiModule
extends Module {
    public static ClickGuiModule INSTANCE;
    public static ModeSetting textPos;
    public static ModeSetting theme;
    public static BooleanSetting underline;
    public static BooleanSetting darken;
    public static BooleanSetting blurFrame;
    public static NumberSetting frameBlurIntensity;
    public static BooleanSetting blurBG;
    public static NumberSetting BGBlurIntensity;
    public static BooleanSetting closedButtonOutline;
    public static BooleanSetting clickSound;
    public static BooleanSetting reset;
    public static BooleanSetting desc;
    public static BooleanSetting limit;
    public static NumberSetting radius;
    public static NumberSetting length;
    public static ModeSetting mode;

    public ClickGuiModule() {
        super("ClickGUI", "idk clickgui open for settings etc", 54, Category.CLIENT);
        this.addSettings(reset, desc, darken, closedButtonOutline, blurFrame, blurBG, BGBlurIntensity, clickSound, limit, radius, length, textPos, theme);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (mode.is("Frames")) {
            this.mc.displayGuiScreen((GuiScreen)Xeno.clickGui);
        } else if (mode.is("Window")) {
            this.mc.displayGuiScreen((GuiScreen)new WindowGui());
        }
        this.toggle();
    }

    @Override
    public void onNonToggledUpdate() {
        if (reset.isEnabled()) {
            ClickGui.reset();
            ClickGuiModule.reset.enabled = false;
        }
        ClickGuiVariables.frameRoundedRadius = (int)radius.getDoubleValue();
        ClickGuiVariables.limitFrameXandYToDisplaySides = limit.getValue();
        ClickGuiVariables.frameTextUnderline = underline.getValue();
        ClickGui.maxLength = length.getIntValue();
    }

    static {
        textPos = new ModeSetting("TextPos", "Center", "Left", "Right");
        theme = new ModeSetting("Theme", "Xeno", "Plain", "Future");
        underline = new BooleanSetting("Underline", false);
        darken = new BooleanSetting("Darken", false);
        blurFrame = new BooleanSetting("Blur Frame", false);
        frameBlurIntensity = new NumberSetting("FrBlur Intensity", 3.0, 1.0, 10.0, 1.0);
        blurBG = new BooleanSetting("Blur BG", true);
        BGBlurIntensity = new NumberSetting("BG Intensity", 1.0, 1.0, 10.0, 1.0);
        closedButtonOutline = new BooleanSetting("Button Outline", false);
        clickSound = new BooleanSetting("Click Sound", true);
        reset = new BooleanSetting("Reset", false);
        desc = new BooleanSetting("Tooltips", true);
        limit = new BooleanSetting("LimitPositions", true);
        radius = new NumberSetting("Radius", 1.0, 1.0, 6.0, 1.0);
        length = new NumberSetting("Length", 130.0, 70.0, 495.0, 5.0);
        mode = new ModeSetting("Mode", "Frames", "Window");
    }
}

