/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.module.settings;

import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.Setting;

public class BooleanSetting
extends Setting {
    public boolean enabled;
    public boolean clickGuiFancy;

    public BooleanSetting(String name, boolean defaultEnabled) {
        this.name = name;
        this.enabled = defaultEnabled;
        this.clickGuiFancy = false;
    }

    public BooleanSetting(Object obj, String name, boolean defaultEnabled) {
        this.name = name;
        this.enabled = defaultEnabled;
        this.clickGuiFancy = false;
    }

    public BooleanSetting(String name, Module m, boolean defaultEnabled) {
        this.name = name;
        this.enabled = defaultEnabled;
        this.clickGuiFancy = false;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }

    public boolean getValue() {
        return this.enabled;
    }
}

