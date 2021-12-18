/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.module.settings;

import me.xenodevs.xeno.module.settings.Setting;

public class StringSetting
extends Setting {
    public String text;
    public String defaultValue;

    public StringSetting(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.text = defaultValue;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String newText) {
        this.text = newText;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }
}

