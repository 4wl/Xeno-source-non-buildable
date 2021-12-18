/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.module.settings;

import me.xenodevs.xeno.module.settings.Setting;

public class KeybindSetting
extends Setting {
    public int code;
    public int defaultCode;

    public KeybindSetting(int code) {
        this.name = "Key";
        this.code = code;
        this.defaultCode = code;
    }

    public KeybindSetting(String name, int code) {
        this.name = name;
        this.code = code;
        this.defaultCode = code;
    }

    public int getKeyCode() {
        return this.code;
    }

    public void setKeyCode(int code) {
        this.code = code;
    }
}

