/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.module.settings;

import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.Setting;

public class NumberSetting
extends Setting {
    public double value;
    public double minimum;
    public double maximum;
    public double increment;
    public double defaultValue;

    public NumberSetting(String name, double value, double minimum, double maximum, double increment) {
        this.name = name;
        this.value = value;
        this.defaultValue = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
    }

    public NumberSetting(Object obj, String name, double value, double minimum, double maximum, double increment) {
        this.name = name;
        this.value = value;
        this.defaultValue = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
    }

    public NumberSetting(String name, Module m, double value, double minimum, double maximum, double increment) {
        this.name = name;
        this.value = value;
        this.defaultValue = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
    }

    public float getFloatValue() {
        return (float)this.value;
    }

    public int getIntValue() {
        return (int)this.value;
    }

    public double getDoubleValue() {
        return this.value;
    }

    public void setValue(double value) {
        double precision = 1.0 / this.increment;
        this.value = (double)Math.round(Math.max(this.minimum, Math.min(this.maximum, value)) * precision) / precision;
    }

    public void setValue(int value) {
        double precision = 1.0 / this.increment;
        this.value = (double)Math.round(Math.max(this.minimum, Math.min(this.maximum, (double)value)) * precision) / precision;
    }

    public void setValue(float value) {
        double precision = 1.0 / this.increment;
        this.value = (double)Math.round(Math.max(this.minimum, Math.min(this.maximum, (double)value)) * precision) / precision;
    }

    public double getMinimum() {
        return this.minimum;
    }

    public double getMaximum() {
        return this.maximum;
    }

    public double getVal() {
        return this.value;
    }

    public void setVal(double val) {
        this.value = val;
    }
}

