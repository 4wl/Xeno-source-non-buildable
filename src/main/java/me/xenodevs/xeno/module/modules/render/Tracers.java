/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.monster.EntityMob
 */
package me.xenodevs.xeno.module.modules.render;

import java.awt.Color;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils3D;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;

public class Tracers
extends Module {
    public static BooleanSetting passive = new BooleanSetting("Passives", true);
    public static ColourPicker passiveColour = new ColourPicker("Passive Colour", new Colour(Color.GREEN));
    public static BooleanSetting mobs = new BooleanSetting("Mobs", true);
    public static ColourPicker mobColour = new ColourPicker("Mob Colour", new Colour(Color.RED));
    public static BooleanSetting players = new BooleanSetting("Players", true);
    public static ColourPicker playerColour = new ColourPicker("Player Colour", new Colour(Color.WHITE));
    NumberSetting lineWidth = new NumberSetting("Width", 1.0, 0.1, 2.0, 0.1);

    public Tracers() {
        super("Tracers", "draws lines to entities", 0, Category.RENDER);
        this.addSettings(passive, passiveColour, mobs, mobColour, players, playerColour, this.lineWidth);
    }

    @Override
    public void onRenderWorld() {
        this.mc.gameSettings.viewBobbing = false;
        for (Entity e : this.mc.world.loadedEntityList) {
            if (e instanceof EntityLiving && !(e instanceof EntityMob) && Tracers.passive.enabled) {
                RenderUtils3D.drawTracer(e, this.lineWidth.getFloatValue(), passiveColour.getColor());
            }
            if (e instanceof EntityLiving && e instanceof EntityMob && Tracers.mobs.enabled) {
                RenderUtils3D.drawTracer(e, this.lineWidth.getFloatValue(), mobColour.getColor());
            }
            if (!(e instanceof EntityOtherPlayerMP) || !Tracers.players.enabled) continue;
            RenderUtils3D.drawTracer(e, this.lineWidth.getFloatValue(), playerColour.getColor());
        }
    }
}

