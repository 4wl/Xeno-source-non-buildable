/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.common.MinecraftForge
 */
package me.xenodevs.xeno.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.KeybindSetting;
import me.xenodevs.xeno.module.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class Module
implements Globals {
    public String name;
    public String description;
    public KeybindSetting keyCode = new KeybindSetting(0);
    public BooleanSetting visible = new BooleanSetting("Visible", true);
    public Category category;
    public boolean enabled;
    public boolean isHudParent;
    public Minecraft mc = Minecraft.getMinecraft();
    public List<Setting> settings = new ArrayList<Setting>();

    public Module(String name, String description, Category category, boolean isHudParent) {
        this.name = name;
        this.description = description;
        this.keyCode.code = 0;
        this.category = category;
        if (isHudParent) {
            this.visible = new BooleanSetting("Visible", false);
            isHudParent = true;
        }
        this.addSettings(this.visible);
        this.addSettings(this.keyCode);
        this.setup();
    }

    public Module(String name, String description, Category category) {
        this.isHudParent = false;
        this.name = name;
        this.description = description;
        this.keyCode.code = 0;
        this.category = category;
        this.addSettings(this.visible);
        this.addSettings(this.keyCode);
        this.setup();
    }

    public Module(String name, String description, int key, Category category) {
        this.isHudParent = false;
        this.name = name;
        this.description = description;
        this.keyCode.code = key;
        this.category = category;
        this.addSettings(this.visible);
        this.addSettings(this.keyCode);
        this.setup();
    }

    public void setup() {
    }

    public void addSetting(Setting setting) {
        this.settings.add(setting);
        this.settings.sort(Comparator.comparingInt(s -> s == this.visible ? 1 : 0));
        this.settings.sort(Comparator.comparingInt(s -> s == this.keyCode ? 1 : 0));
    }

    public void addSettings(Setting ... settings) {
        this.settings.addAll(Arrays.asList(settings));
        this.settings.sort(Comparator.comparingInt(s -> s == this.visible ? 1 : 0));
        this.settings.sort(Comparator.comparingInt(s -> s == this.keyCode ? 1 : 0));
    }

    public void onUpdate() {
    }

    public void onNonToggledUpdate() {
    }

    public void onRenderWorld() {
    }

    public void onRenderGUI() {
    }

    public void onFastUpdate() {
    }

    public void onServerUpdate() {
    }

    public void enable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        Xeno.EVENT_BUS.subscribe((Object)this);
        this.onEnable();
    }

    public void disable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        Xeno.EVENT_BUS.unsubscribe((Object)this);
        this.onDisable();
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void toggle() {
        boolean bl = this.enabled = !this.enabled;
        if (this.enabled) {
            this.enable();
        } else {
            this.disable();
        }
    }

    public String getName() {
        return this.name;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public int getKey() {
        return this.keyCode.code;
    }

    @Override
    public boolean nullCheck() {
        return this.mc.player == null || this.mc.world == null;
    }

    public String getHUDData() {
        return "";
    }
}

