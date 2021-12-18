/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.tileentity.TileEntityChest
 *  net.minecraft.tileentity.TileEntityEnderChest
 *  net.minecraft.tileentity.TileEntityShulkerBox
 *  net.minecraft.util.math.BlockPos
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.module.modules.render;

import java.awt.Color;
import java.util.function.Predicate;
import me.xenodevs.xeno.event.impl.RenderTileEntityEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.OutlineUtils;
import me.xenodevs.xeno.utils.render.RenderUtils3D;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public class StorageESP
extends Module {
    public static BooleanSetting Chests = new BooleanSetting("Chests", true);
    public static ColourPicker chestColour = new ColourPicker("Chest Colour", new Colour(Color.YELLOW));
    public BooleanSetting Shulkers = new BooleanSetting("Shulkers", true);
    public static ColourPicker shulkerColour = new ColourPicker("Shulker Colour", new Colour(Color.MAGENTA));
    public BooleanSetting EChests = new BooleanSetting("EnderChests", true);
    public static ColourPicker enderChestColour = new ColourPicker("EChest Colour", new Colour(Color.PINK));
    public static NumberSetting lineThickness = new NumberSetting("LineWidth", 3.0, 0.5, 3.0, 0.5);
    public static StorageESP instance = new StorageESP();
    public ModeSetting mode = new ModeSetting("Mode", "Outline", "Box", "FilledBox");
    @EventHandler
    private final Listener<RenderTileEntityEvent> renderListener = new Listener<RenderTileEntityEvent>(event -> {
        if (this.mode.is("Outline")) {
            BlockPos blockpos;
            int destroyStage;
            float partialTicks;
            TileEntity tileentityIn;
            GL11.glPushMatrix();
            GlStateManager.depthMask((boolean)true);
            boolean multiplier = true;
            if (Minecraft.getMinecraft().world != null && event.getTileentityIn() instanceof TileEntityChest && StorageESP.Chests.enabled) {
                tileentityIn = event.getTileentityIn();
                partialTicks = event.getPartialTicks();
                destroyStage = event.getDestroyStage();
                blockpos = tileentityIn.getPos();
                event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
                OutlineUtils.renderOne((float)StorageESP.lineThickness.value * (float)multiplier);
                event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
                OutlineUtils.renderTwo();
                event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
                OutlineUtils.renderThree();
                OutlineUtils.renderFour(chestColour.getColor());
                event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
                OutlineUtils.renderFive();
            }
            if (Minecraft.getMinecraft().world != null && event.getTileentityIn() instanceof TileEntityShulkerBox && this.Shulkers.enabled) {
                tileentityIn = event.getTileentityIn();
                partialTicks = event.getPartialTicks();
                destroyStage = event.getDestroyStage();
                blockpos = tileentityIn.getPos();
                event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
                OutlineUtils.renderOne((float)StorageESP.lineThickness.value * (float)multiplier);
                event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
                OutlineUtils.renderTwo();
                event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
                OutlineUtils.renderThree();
                OutlineUtils.renderFour(shulkerColour.getColor());
                event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
                OutlineUtils.renderFive();
            }
            if (Minecraft.getMinecraft().world != null && event.getTileentityIn() instanceof TileEntityEnderChest && this.EChests.enabled) {
                tileentityIn = event.getTileentityIn();
                partialTicks = event.getPartialTicks();
                destroyStage = event.getDestroyStage();
                blockpos = tileentityIn.getPos();
                event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
                OutlineUtils.renderOne((float)StorageESP.lineThickness.value * (float)multiplier);
                event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
                OutlineUtils.renderTwo();
                event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
                OutlineUtils.renderThree();
                OutlineUtils.renderFour(enderChestColour.getColor());
                event.getTileEntityRendererDispatcher().render(tileentityIn, (double)blockpos.getX() - event.getStaticPlayerX(), (double)blockpos.getY() - event.getStaticPlayerY(), (double)blockpos.getZ() - event.getStaticPlayerZ(), partialTicks);
                OutlineUtils.renderFive();
            }
            GL11.glPopMatrix();
        }
    }, new Predicate[0]);

    public StorageESP() {
        super("StorageESP", "highlights storage blocks eg chests", 0, Category.RENDER);
        this.addSettings(Chests, this.Shulkers, this.EChests, chestColour, shulkerColour, enderChestColour, lineThickness, this.mode);
    }

    @Override
    public void onRenderWorld() {
        for (Object e : this.mc.world.loadedTileEntityList) {
            if (this.mode.is("Box")) {
                if (e instanceof TileEntityChest && StorageESP.Chests.enabled) {
                    RenderUtils3D.blockESPBox(((TileEntity)e).getPos(), (float)lineThickness.getDoubleValue(), chestColour.getColor(), 1);
                }
                if (e instanceof TileEntityShulkerBox && this.Shulkers.enabled) {
                    RenderUtils3D.blockESPBox(((TileEntity)e).getPos(), (float)lineThickness.getDoubleValue(), shulkerColour.getColor(), 1);
                }
                if (e instanceof TileEntityEnderChest && this.EChests.enabled) {
                    RenderUtils3D.blockESPBox(((TileEntity)e).getPos(), (float)lineThickness.getDoubleValue(), enderChestColour.getColor(), 1);
                }
            }
            if (!this.mode.is("FilledBox")) continue;
            if (e instanceof TileEntityChest && StorageESP.Chests.enabled) {
                RenderUtils3D.solidBlockESPBox(((TileEntity)e).getPos(), (float)lineThickness.getDoubleValue(), chestColour.getColor(), 1.0f);
            }
            if (e instanceof TileEntityShulkerBox && this.Shulkers.enabled) {
                RenderUtils3D.solidBlockESPBox(((TileEntity)e).getPos(), (float)lineThickness.getDoubleValue(), shulkerColour.getColor(), 1.0f);
            }
            if (!(e instanceof TileEntityEnderChest) || !this.EChests.enabled) continue;
            RenderUtils3D.solidBlockESPBox(((TileEntity)e).getPos(), (float)lineThickness.getDoubleValue(), enderChestColour.getColor(), 1.0f);
        }
    }

    @Override
    public String getHUDData() {
        return " " + this.mode.getMode();
    }
}

