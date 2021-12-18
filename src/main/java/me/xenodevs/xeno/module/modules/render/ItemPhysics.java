/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.module.modules.render;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.NumberSetting;

public class ItemPhysics
extends Module {
    public static ItemPhysics INSTANCE;
    public static NumberSetting scale;

    public ItemPhysics() {
        super("ItemPhysics", "gives physics to dropped items", 0, Category.RENDER);
        this.addSettings(scale);
        INSTANCE = this;
    }

    static {
        scale = new NumberSetting("Scale", 0.5, 0.0, 5.0, 0.1);
    }
}

