/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.module.settings;

import java.awt.Color;
import me.xenodevs.xeno.module.settings.Setting;
import me.xenodevs.xeno.utils.render.Colour;

public class ColourPicker
extends Setting {
    private boolean rainbow;
    public Colour value;

    public ColourPicker(String name, Colour colour) {
        this.name = name;
        this.value = colour;
    }

    public ColourPicker(String name, Color colour) {
        this.name = name;
        this.value = new Colour(colour);
    }

    public void doRainBow() {
        if (this.rainbow) {
            Colour c = Colour.fromHSB((float)(System.currentTimeMillis() % 11520L) / 11520.0f, this.value.getSaturation(), this.value.getBrightness());
            this.setValue(new Colour(c.getRed(), c.getGreen(), c.getBlue(), this.value.getAlpha()));
        }
    }

    public void setValue(Color value) {
        this.value = new Colour(value);
    }

    public void setValue(int red, int green, int blue, int alpha) {
        this.value = new Colour(red, green, blue, alpha);
    }

    public Color getColor() {
        this.doRainBow();
        return this.value;
    }

    public boolean getRainbow() {
        return this.rainbow;
    }

    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }
}

