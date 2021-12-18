/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.gui.click.theme;

import java.util.ArrayList;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.click.component.Component;
import me.xenodevs.xeno.gui.click.component.Frame;
import me.xenodevs.xeno.gui.click.component.components.Button;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.ColourComponent;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.StringComponent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.KeybindSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.module.settings.StringSetting;
import me.xenodevs.xeno.utils.other.Timer;

public abstract class Theme {
    public String name;
    public boolean isScrollable;

    public Theme(String name, boolean isScrollable) {
        this.name = name;
        this.isScrollable = isScrollable;
    }

    public abstract void drawBoolean(BooleanSetting var1, Button var2, double var3, int var5, int var6, boolean var7);

    public abstract void drawMode(ModeSetting var1, Button var2, double var3, int var5, int var6, boolean var7);

    public abstract void drawSlider(NumberSetting var1, Button var2, double var3, int var5, boolean var6, int var7, int var8);

    public abstract void drawColourPicker(ColourComponent var1, ColourPicker var2, double var3, boolean var5, Button var6, int var7, int var8);

    public abstract void drawStringComponent(StringSetting var1, StringComponent.CurrentString var2, Button var3, boolean var4, boolean var5, double var6, int var8, double var9, int var11, int var12);

    public abstract void drawKeybind(KeybindSetting var1, double var2, Button var4, boolean var5, int var6, int var7);

    public abstract void drawFrame(ArrayList<Component> var1, Category var2, boolean var3, int var4, int var5, int var6, int var7, int var8);

    public abstract void drawButton(Frame var1, Module var2, ArrayList<Component> var3, boolean var4, double var5, int var7, int var8, boolean var9);

    public abstract void drawDescription(String var1, Timer var2, int var3, int var4);

    public void drawScreen() {
        if (ClickGuiModule.blurBG.isEnabled()) {
            Xeno.blurManager.blur((int)ClickGuiModule.BGBlurIntensity.getDoubleValue());
        }
    }
}

