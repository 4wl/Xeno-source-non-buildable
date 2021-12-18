/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 */
package me.xenodevs.xeno.utils.block.hole;

import java.util.ArrayList;
import java.util.List;
import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.utils.block.BlockUtil;
import me.xenodevs.xeno.utils.block.hole.Hole;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class HoleUtil
implements Globals {
    public static boolean isInHole(EntityPlayer entityPlayer) {
        return HoleUtil.isHole(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ));
    }

    public static boolean isVoidHole(BlockPos blockPos) {
        return HoleUtil.mc.player.dimension == -1 ? (blockPos.getY() == 0 || blockPos.getY() == 127) && BlockUtil.getBlockResistance(blockPos) == BlockUtil.BlockResistance.Blank : blockPos.getY() == 0 && BlockUtil.getBlockResistance(blockPos) == BlockUtil.BlockResistance.Blank;
    }

    public static List<Hole> getHoles(double range) {
        ArrayList<Hole> holes = new ArrayList<Hole>();
        for (BlockPos pos : BlockUtil.getNearbyBlocks((EntityPlayer)HoleUtil.mc.player, range, false)) {
            if (HoleUtil.isObsidianHole(pos)) {
                holes.add(new Hole(Hole.Type.Obsidian, Hole.Facing.None, pos));
            }
            if (!HoleUtil.isBedRockHole(pos)) continue;
            holes.add(new Hole(Hole.Type.Bedrock, Hole.Facing.None, pos));
        }
        return holes;
    }

    public static List<Hole> getHoles(double range, EntityPlayer plr) {
        ArrayList<Hole> holes = new ArrayList<Hole>();
        for (BlockPos pos : BlockUtil.getNearbyBlocks(plr, range, false)) {
            if (HoleUtil.isObsidianHole(pos)) {
                holes.add(new Hole(Hole.Type.Obsidian, Hole.Facing.None, pos));
            }
            if (!HoleUtil.isBedRockHole(pos)) continue;
            holes.add(new Hole(Hole.Type.Bedrock, Hole.Facing.None, pos));
        }
        return holes;
    }

    public static boolean isDoubleBedrockHoleX(BlockPos blockPos) {
        if (!HoleUtil.mc.world.getBlockState(blockPos).getBlock().equals((Object)Blocks.AIR) || !HoleUtil.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock().equals((Object)Blocks.AIR) || !HoleUtil.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals((Object)Blocks.AIR) && !HoleUtil.mc.world.getBlockState(blockPos.add(1, 1, 0)).getBlock().equals((Object)Blocks.AIR) || !HoleUtil.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals((Object)Blocks.AIR) && !HoleUtil.mc.world.getBlockState(blockPos.add(1, 2, 0)).getBlock().equals((Object)Blocks.AIR)) {
            return false;
        }
        for (BlockPos blockPos2 : new BlockPos[]{blockPos.add(2, 0, 0), blockPos.add(1, 0, 1), blockPos.add(1, 0, -1), blockPos.add(-1, 0, 0), blockPos.add(0, 0, 1), blockPos.add(0, 0, -1), blockPos.add(0, -1, 0), blockPos.add(1, -1, 0)}) {
            IBlockState iBlockState = HoleUtil.mc.world.getBlockState(blockPos2);
            if (iBlockState.getBlock() != Blocks.AIR && iBlockState.getBlock() == Blocks.BEDROCK) continue;
            return false;
        }
        return true;
    }

    public static boolean isDoubleBedrockHoleZ(BlockPos blockPos) {
        if (!HoleUtil.mc.world.getBlockState(blockPos).getBlock().equals((Object)Blocks.AIR) || !HoleUtil.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock().equals((Object)Blocks.AIR) || !HoleUtil.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals((Object)Blocks.AIR) && !HoleUtil.mc.world.getBlockState(blockPos.add(0, 1, 1)).getBlock().equals((Object)Blocks.AIR) || !HoleUtil.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals((Object)Blocks.AIR) && !HoleUtil.mc.world.getBlockState(blockPos.add(0, 2, 1)).getBlock().equals((Object)Blocks.AIR)) {
            return false;
        }
        for (BlockPos blockPos2 : new BlockPos[]{blockPos.add(0, 0, 2), blockPos.add(1, 0, 1), blockPos.add(-1, 0, 1), blockPos.add(0, 0, -1), blockPos.add(1, 0, 0), blockPos.add(-1, 0, 0), blockPos.add(0, -1, 0), blockPos.add(0, -1, 1)}) {
            IBlockState iBlockState = HoleUtil.mc.world.getBlockState(blockPos2);
            if (iBlockState.getBlock() != Blocks.AIR && iBlockState.getBlock() == Blocks.BEDROCK) continue;
            return false;
        }
        return true;
    }

    public static boolean isDoubleObsidianHoleX(BlockPos blockPos) {
        if (!HoleUtil.mc.world.getBlockState(blockPos).getBlock().equals((Object)Blocks.AIR) || !HoleUtil.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock().equals((Object)Blocks.AIR) || !HoleUtil.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals((Object)Blocks.AIR) && !HoleUtil.mc.world.getBlockState(blockPos.add(1, 1, 0)).getBlock().equals((Object)Blocks.AIR) || !HoleUtil.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals((Object)Blocks.AIR) && !HoleUtil.mc.world.getBlockState(blockPos.add(1, 2, 0)).getBlock().equals((Object)Blocks.AIR)) {
            return false;
        }
        for (BlockPos blockPos2 : new BlockPos[]{blockPos.add(2, 0, 0), blockPos.add(1, 0, 1), blockPos.add(1, 0, -1), blockPos.add(-1, 0, 0), blockPos.add(0, 0, 1), blockPos.add(0, 0, -1), blockPos.add(0, -1, 0), blockPos.add(1, -1, 0)}) {
            IBlockState iBlockState = HoleUtil.mc.world.getBlockState(blockPos2);
            if (iBlockState.getBlock() != Blocks.AIR && iBlockState.getBlock() == Blocks.OBSIDIAN) continue;
            return false;
        }
        return true;
    }

    public static boolean isDoubleObsidianHoleZ(BlockPos blockPos) {
        if (!HoleUtil.mc.world.getBlockState(blockPos).getBlock().equals((Object)Blocks.AIR) || !HoleUtil.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock().equals((Object)Blocks.AIR) || !HoleUtil.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals((Object)Blocks.AIR) && !HoleUtil.mc.world.getBlockState(blockPos.add(0, 1, 1)).getBlock().equals((Object)Blocks.AIR) || !HoleUtil.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals((Object)Blocks.AIR) && !HoleUtil.mc.world.getBlockState(blockPos.add(0, 2, 1)).getBlock().equals((Object)Blocks.AIR)) {
            return false;
        }
        for (BlockPos blockPos2 : new BlockPos[]{blockPos.add(0, 0, 2), blockPos.add(1, 0, 1), blockPos.add(-1, 0, 1), blockPos.add(0, 0, -1), blockPos.add(1, 0, 0), blockPos.add(-1, 0, 0), blockPos.add(0, -1, 0), blockPos.add(0, -1, 1)}) {
            IBlockState iBlockState = HoleUtil.mc.world.getBlockState(blockPos2);
            if (iBlockState.getBlock() != Blocks.AIR && iBlockState.getBlock() == Blocks.OBSIDIAN) continue;
            return false;
        }
        return true;
    }

    public static boolean isObsidianHole(BlockPos blockPos) {
        return !(BlockUtil.getBlockResistance(blockPos.add(0, 1, 0)) != BlockUtil.BlockResistance.Blank || HoleUtil.isBedRockHole(blockPos) || BlockUtil.getBlockResistance(blockPos.add(0, 0, 0)) != BlockUtil.BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.add(0, 2, 0)) != BlockUtil.BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.add(0, 0, -1)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.add(0, 0, -1)) != BlockUtil.BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.add(1, 0, 0)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.add(1, 0, 0)) != BlockUtil.BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.add(-1, 0, 0)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.add(-1, 0, 0)) != BlockUtil.BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.add(0, 0, 1)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.add(0, 0, 1)) != BlockUtil.BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.add(0.5, 0.5, 0.5)) != BlockUtil.BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.add(0, -1, 0)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.add(0, -1, 0)) != BlockUtil.BlockResistance.Unbreakable);
    }

    public static boolean isBedRockHole(BlockPos blockPos) {
        return BlockUtil.getBlockResistance(blockPos.add(0, 1, 0)) == BlockUtil.BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 0, 0)) == BlockUtil.BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 2, 0)) == BlockUtil.BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 0, -1)) == BlockUtil.BlockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.add(1, 0, 0)) == BlockUtil.BlockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.add(-1, 0, 0)) == BlockUtil.BlockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.add(0, 0, 1)) == BlockUtil.BlockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.add(0.5, 0.5, 0.5)) == BlockUtil.BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, -1, 0)) == BlockUtil.BlockResistance.Unbreakable;
    }

    public static boolean isHole(BlockPos blockPos) {
        return !(BlockUtil.getBlockResistance(blockPos.add(0, 1, 0)) != BlockUtil.BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.add(0, 0, 0)) != BlockUtil.BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.add(0, 2, 0)) != BlockUtil.BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.add(0, 0, -1)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.add(0, 0, -1)) != BlockUtil.BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.add(1, 0, 0)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.add(1, 0, 0)) != BlockUtil.BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.add(-1, 0, 0)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.add(-1, 0, 0)) != BlockUtil.BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.add(0, 0, 1)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.add(0, 0, 1)) != BlockUtil.BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.add(0.5, 0.5, 0.5)) != BlockUtil.BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.add(0, -1, 0)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.add(0, -1, 0)) != BlockUtil.BlockResistance.Unbreakable);
    }
}

