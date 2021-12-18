/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.module.modules.client;

import java.awt.Color;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.utils.render.Colour;

public class Colors
extends Module {
    public ColourPicker colour = new ColourPicker("Colour", new Colour(0, 128, 255, 255));
    public static int colourInt = -1;
    public static Color col = Color.WHITE;

    public Colors() {
        super("Colors", "change the clients colour", 0, Category.CLIENT);
        this.addSettings(this.colour);
    }

    @Override
    public void onNonToggledUpdate() {
        this.enabled = false;
        colourInt = this.colour.getColor().getRGB();
        col = this.colour.getColor();
    }
}

