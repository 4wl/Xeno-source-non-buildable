/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.xenodevs.xeno.utils.other;

import me.wolfsurge.api.util.Globals;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class RaytraceUtil
implements Globals {
    public static boolean raytraceBlock(BlockPos blockPos, double offset) {
        return RaytraceUtil.mc.world.rayTraceBlocks(new Vec3d(RaytraceUtil.mc.player.posX, RaytraceUtil.mc.player.posY + (double)RaytraceUtil.mc.player.getEyeHeight(), RaytraceUtil.mc.player.posZ), new Vec3d((double)blockPos.getX(), (double)blockPos.getY() + offset, (double)blockPos.getZ()), false, true, false) == null;
    }

    public static boolean raytraceQuill(BlockPos blockPos, double offset) {
        return RaytraceUtil.mc.world.rayTraceBlocks(new Vec3d(RaytraceUtil.mc.player.posX, RaytraceUtil.mc.player.posY + (double)RaytraceUtil.mc.player.getEyeHeight(), RaytraceUtil.mc.player.posZ), new Vec3d((double)blockPos.getX(), (double)blockPos.getY() + offset + 1.5, (double)blockPos.getZ()), false, true, false) == null;
    }

    public static boolean raytraceEntity(Entity entity) {
        return RaytraceUtil.mc.world.rayTraceBlocks(new Vec3d(RaytraceUtil.mc.player.posX, RaytraceUtil.mc.player.posY + (double)RaytraceUtil.mc.player.getEyeHeight(), RaytraceUtil.mc.player.posZ), new Vec3d(entity.posX, entity.posY, entity.posZ), false, true, false) == null;
    }

    public static boolean raytraceFeet(Entity entity) {
        return RaytraceUtil.mc.world.rayTraceBlocks(new Vec3d(RaytraceUtil.mc.player.posX, RaytraceUtil.mc.player.posY + (double)RaytraceUtil.mc.player.getEyeHeight(), RaytraceUtil.mc.player.posZ), new Vec3d(entity.posX, entity.posY, entity.posZ), false, true, false) == null;
    }
}

