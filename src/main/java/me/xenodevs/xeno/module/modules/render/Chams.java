/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.monster.EntityMob
 */
package me.xenodevs.xeno.module.modules.render;

import java.awt.Color;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.utils.render.Colour;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;

public class Chams
extends Module {
    public static Chams INSTANCE;
    public static BooleanSetting passive;
    public static BooleanSetting mobs;
    public static BooleanSetting players;
    public static ColourPicker passiveColour;
    public static ColourPicker mobColour;
    public static ColourPicker playerColour;
    public static ModeSetting mode;

    public Chams() {
        super("Chams", "shows entities through walls", 0, Category.RENDER);
        this.addSettings(mode, passive, mobs, players, passiveColour, mobColour, playerColour);
        INSTANCE = this;
    }

    @Override
    public void onRenderWorld() {
        if (this.nullCheck()) {
            return;
        }
        if (mode.is("Wallhack")) {
            if (Chams.passive.enabled) {
                for (Object e : this.mc.world.loadedEntityList) {
                    if (!(e instanceof EntityLiving) || e instanceof EntityMob || e instanceof EntityItem) continue;
                    GlStateManager.clear((int)256);
                    RenderHelper.enableStandardItemLighting();
                    GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                    if (e == this.mc.getRenderViewEntity() && this.mc.gameSettings.thirdPersonView == 0) continue;
                    this.mc.entityRenderer.disableLightmap();
                    this.mc.getRenderManager().renderEntityStatic((Entity)e, this.mc.timer.renderPartialTicks, false);
                    this.mc.entityRenderer.enableLightmap();
                    this.mc.entityRenderer.resetData();
                }
            }
            if (Chams.mobs.enabled) {
                for (Object e : this.mc.world.loadedEntityList) {
                    if (!(e instanceof EntityMob) || !(e instanceof EntityLiving) || e instanceof EntityItem) continue;
                    GlStateManager.clear((int)256);
                    RenderHelper.enableStandardItemLighting();
                    GlStateManager.color((float)1.0f, (float)0.0f, (float)1.0f, (float)1.0f);
                    if (e == this.mc.getRenderViewEntity() && this.mc.gameSettings.thirdPersonView == 0) continue;
                    this.mc.entityRenderer.disableLightmap();
                    this.mc.getRenderManager().renderEntityStatic((Entity)e, this.mc.timer.renderPartialTicks, false);
                    this.mc.entityRenderer.enableLightmap();
                }
            }
            if (Chams.players.enabled) {
                for (Object e : this.mc.world.loadedEntityList) {
                    if (!(e instanceof EntityOtherPlayerMP) || e instanceof EntityMob || e instanceof EntityItem) continue;
                    if (e == this.mc.player) {
                        return;
                    }
                    GlStateManager.clear((int)256);
                    RenderHelper.enableStandardItemLighting();
                    GlStateManager.color((float)1.0f, (float)0.0f, (float)1.0f, (float)1.0f);
                    if (e == this.mc.getRenderViewEntity() && this.mc.gameSettings.thirdPersonView == 0) continue;
                    this.mc.entityRenderer.disableLightmap();
                    this.mc.getRenderManager().renderEntityStatic((Entity)e, this.mc.timer.renderPartialTicks, false);
                    this.mc.entityRenderer.enableLightmap();
                }
            }
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode();
    }

    static {
        passive = new BooleanSetting("Passive", true);
        mobs = new BooleanSetting("Mobs", true);
        players = new BooleanSetting("Players", true);
        passiveColour = new ColourPicker("Passive Colour", new Colour(Color.GREEN));
        mobColour = new ColourPicker("Mob Colour", new Colour(Color.RED));
        playerColour = new ColourPicker("Player Colour", new Colour(Color.CYAN));
        mode = new ModeSetting("Mode", "Fill", "Wallhack");
    }
}

