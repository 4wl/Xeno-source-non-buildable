/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.player.EntityPlayer
 */
package me.xenodevs.xeno.module.modules.render;

import java.awt.Color;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils3D;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;

public class ESP
extends Module {
    public static ESP INSTANCE;
    public static BooleanSetting passive;
    public static ColourPicker passiveColour;
    public static BooleanSetting mobs;
    public static ColourPicker mobColour;
    public static BooleanSetting players;
    public static ColourPicker playerColour;
    public static BooleanSetting items;
    public static ColourPicker itemColour;
    public static NumberSetting lineWidth;
    public static ModeSetting mode;
    public static BooleanSetting invisibles;

    public ESP() {
        super("ESP", "highlights entities through walls", 0, Category.RENDER);
        this.addSettings(passive, mobs, players, items, passiveColour, mobColour, playerColour, lineWidth, mode);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        for (Entity e : this.mc.world.loadedEntityList) {
            if (e.isInvisible() && !ESP.invisibles.enabled) {
                return;
            }
            e.setGlowing(false);
        }
    }

    @Override
    public void onRenderWorld() {
        if (mode.is("Box")) {
            for (Entity e : this.mc.world.loadedEntityList) {
                if (e.isInvisible() && !ESP.invisibles.enabled) {
                    return;
                }
                if (e instanceof EntityLiving && !(e instanceof EntityMob) && passive.isEnabled()) {
                    RenderUtils3D.drawESPBox(e, (float)lineWidth.getDoubleValue(), passiveColour.getColor());
                }
                if (e instanceof EntityMob && mobs.isEnabled()) {
                    RenderUtils3D.drawESPBox(e, (float)lineWidth.getDoubleValue(), mobColour.getColor());
                }
                if (!(e instanceof EntityOtherPlayerMP) || !passive.isEnabled()) continue;
                RenderUtils3D.drawESPBox(e, (float)lineWidth.getDoubleValue(), playerColour.getColor());
            }
        }
        if (!mode.is("Glow")) {
            for (Entity e : this.mc.world.loadedEntityList) {
                if (e.isInvisible() && !ESP.invisibles.enabled) {
                    return;
                }
                e.setGlowing(false);
            }
        }
        if (mode.is("Glow")) {
            for (Entity e : this.mc.world.loadedEntityList) {
                if (e.isInvisible() && !ESP.invisibles.enabled) {
                    return;
                }
                if (e instanceof EntityLiving && !(e instanceof EntityMob) && passive.isEnabled()) {
                    e.setGlowing(true);
                    continue;
                }
                if (e instanceof EntityMob && mobs.isEnabled()) {
                    e.setGlowing(true);
                    continue;
                }
                if (!(e instanceof EntityPlayer) || !players.isEnabled() || e == this.mc.player) continue;
                e.setGlowing(true);
            }
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode();
    }

    static {
        passive = new BooleanSetting("Passive", true);
        passiveColour = new ColourPicker("Passive Colour", new Colour(Color.GREEN));
        mobs = new BooleanSetting("Mobs", true);
        mobColour = new ColourPicker("Mob Colour", new Colour(Color.RED));
        players = new BooleanSetting("Players", true);
        playerColour = new ColourPicker("Player Colour", new Colour(Color.CYAN));
        items = new BooleanSetting("Items", true);
        itemColour = new ColourPicker("Item Colour", new Colour(Color.WHITE));
        lineWidth = new NumberSetting("Line Width", 3.0, 1.0, 3.0, 0.5);
        mode = new ModeSetting("Mode", "Outline", "Box", "Glow");
        invisibles = new BooleanSetting("Invisibles", true);
    }
}

