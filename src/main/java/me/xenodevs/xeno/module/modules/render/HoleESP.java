/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.BlockPos
 */
package me.xenodevs.xeno.module.modules.render;

import java.util.Objects;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.block.BlockUtil;
import me.xenodevs.xeno.utils.block.hole.HoleUtil;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils3D;
import me.xenodevs.xeno.utils.render.builder.RenderBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class HoleESP
extends Module {
    public static ModeSetting main = new ModeSetting("Main", "Glow", "Fill", "Flat", "None");
    public static NumberSetting mainHeight = new NumberSetting("Main Height", 1.0, -1.0, 3.0, 1.0);
    public static ModeSetting outline = new ModeSetting("Outline", "Frame", "Claw", "None");
    public static NumberSetting outlineHeight = new NumberSetting("Outline Height", 0.0, -1.0, 3.0, 1.0);
    public static ModeSetting highlight = new ModeSetting("Highlight", "None", "Hide", "Glow");
    public static ColourPicker obsidianPicker = new ColourPicker("Obsidian Colour", new Colour(144, 0, 255, 255));
    public static ColourPicker bedrockPicker = new ColourPicker("Bedrock Colour", new Colour(93, 235, 240, 255));
    public static BooleanSetting doubles = new BooleanSetting("Doubles", true);
    public static BooleanSetting viewFrustrum = new BooleanSetting("View Frustrum", false);
    public static NumberSetting updates = new NumberSetting("Updates", 10.0, 0.0, 20.0, 1.0);
    public static NumberSetting lineWidth = new NumberSetting("Width", 0.0, 0.0, 3.0, 1.0);
    public static NumberSetting range = new NumberSetting("Range", 5.0, 0.0, 10.0, 1.0);
    int currentTick;
    boolean clearRender = false;

    public HoleESP() {
        super("HoleESP", "highlights safe holes to stand in while crystalling", Category.RENDER);
    }

    @Override
    public void setup() {
        this.addSetting(main);
        this.addSetting(outline);
        this.addSetting(obsidianPicker);
        this.addSetting(bedrockPicker);
        this.addSetting(doubles);
        this.addSetting(viewFrustrum);
        this.addSetting(updates);
        this.addSetting(range);
    }

    @Override
    public void onRenderWorld() {
        if (this.nullCheck()) {
            return;
        }
        ++this.currentTick;
        if ((double)this.currentTick < updates.getDoubleValue()) {
            return;
        }
        if (this.clearRender) {
            return;
        }
        RenderUtils3D.camera.setPosition(Objects.requireNonNull(this.mc.getRenderViewEntity()).posX, this.mc.getRenderViewEntity().posY, this.mc.getRenderViewEntity().posZ);
        for (BlockPos potentialHole : BlockUtil.getNearbyBlocks((EntityPlayer)this.mc.player, range.getDoubleValue(), false)) {
            if (HoleUtil.isBedRockHole(potentialHole)) {
                this.renderHole(potentialHole, Type.Bedrock, 0.0, 0.0);
            } else if (HoleUtil.isObsidianHole(potentialHole)) {
                this.renderHole(potentialHole, Type.Obsidian, 0.0, 0.0);
            }
            if (!doubles.getValue()) continue;
            if (HoleUtil.isDoubleObsidianHoleX(potentialHole.west())) {
                this.renderHole(potentialHole.west(), Type.Obsidian, 1.0, 0.0);
                continue;
            }
            if (HoleUtil.isDoubleObsidianHoleZ(potentialHole.north())) {
                this.renderHole(potentialHole.north(), Type.Obsidian, 0.0, 1.0);
                continue;
            }
            if (HoleUtil.isDoubleBedrockHoleX(potentialHole.west())) {
                this.renderHole(potentialHole.west(), Type.Bedrock, 1.0, 0.0);
                continue;
            }
            if (!HoleUtil.isDoubleBedrockHoleZ(potentialHole.north())) continue;
            this.renderHole(potentialHole.north(), Type.Bedrock, 0.0, 1.0);
        }
    }

    public void renderHole(BlockPos hole, Type type2, double length, double width) {
        this.renderMain(hole, type2, length, width);
        this.renderOutline(hole, type2, length, width);
    }

    public void renderMain(BlockPos hole, Type type2, double length, double width) {
        switch (HoleESP.main.index) {
            case 0: {
                RenderUtils3D.drawBoxBlockPos(hole, mainHeight.getDoubleValue() - 1.0, length, width, type2.equals((Object)Type.Obsidian) ? obsidianPicker.getColor() : bedrockPicker.getColor(), RenderBuilder.RenderMode.Glow);
                break;
            }
            case 1: {
                RenderUtils3D.drawBoxBlockPos(hole, mainHeight.getDoubleValue() - 1.0, length, width, type2.equals((Object)Type.Obsidian) ? obsidianPicker.getColor() : bedrockPicker.getColor(), RenderBuilder.RenderMode.Fill);
                break;
            }
            case 2: {
                RenderUtils3D.drawBoxBlockPos(hole, mainHeight.getDoubleValue() - 2.0, length, width, type2.equals((Object)Type.Obsidian) ? obsidianPicker.getColor().darker() : bedrockPicker.getColor(), RenderBuilder.RenderMode.Fill);
            }
        }
        if (this.mc.player.getDistanceSq(hole) < 1.5) {
            switch (HoleESP.highlight.index) {
                case 0: 
                case 1: {
                    break;
                }
                case 2: {
                    RenderUtils3D.drawBoxBlockPos(hole, mainHeight.getDoubleValue() - 1.0, length, width, type2.equals((Object)Type.Obsidian) ? obsidianPicker.getColor() : bedrockPicker.getColor(), RenderBuilder.RenderMode.Glow);
                }
            }
        }
    }

    public void renderOutline(BlockPos hole, Type type2, double length, double width) {
        switch (HoleESP.outline.index) {
            case 0: {
                RenderUtils3D.drawBoxBlockPos(hole, outlineHeight.getDoubleValue() - 1.0, length, width, type2.equals((Object)Type.Obsidian) ? obsidianPicker.getColor() : bedrockPicker.getColor(), RenderBuilder.RenderMode.Outline);
                break;
            }
            case 1: {
                RenderUtils3D.drawBoxBlockPos(hole, outlineHeight.getDoubleValue() - 1.0, length, width, type2.equals((Object)Type.Obsidian) ? obsidianPicker.getColor() : bedrockPicker.getColor(), RenderBuilder.RenderMode.Claw);
            }
        }
    }

    @Override
    public String getHUDData() {
        return " " + main.getMode() + ", " + outline.getMode();
    }

    public static enum Type {
        Obsidian,
        Bedrock,
        Mixed;

    }
}

