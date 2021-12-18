/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.module.settings;

import java.util.Arrays;
import java.util.List;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.Setting;

public class ModeSetting
extends Setting {
    public int index;
    public List<String> modes;

    public ModeSetting(String name, String ... modes) {
        this.name = name;
        this.modes = Arrays.asList(modes);
        this.index = 0;
    }

    public ModeSetting(Object obj, String name, String ... modes) {
        this.name = name;
        this.modes = Arrays.asList(modes);
        this.index = 0;
    }

    public ModeSetting(String name, Module m, String ... modes) {
        this.name = name;
        this.modes = Arrays.asList(modes);
        this.index = 0;
    }

    public String getMode() {
        return this.modes.get(this.index);
    }

    public boolean is(String mode) {
        return this.index == this.modes.indexOf(mode);
    }

    public void cycle() {
        this.index = this.index < this.modes.size() - 1 ? ++this.index : 0;
    }

    public int getValue() {
        return this.index;
    }
}

