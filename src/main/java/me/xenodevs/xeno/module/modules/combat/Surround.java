/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.xenodevs.xeno.module.modules.combat;

import java.util.ArrayList;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.utils.entity.EntityHelper;
import me.xenodevs.xeno.utils.entity.EntityUtil;
import me.xenodevs.xeno.utils.other.Timer;
import me.xenodevs.xeno.utils.player.InventoryUtil;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Surround
extends Module {
    public static Surround INSTANCE;
    BooleanSetting disable = new BooleanSetting("Disable", false);
    BooleanSetting rotate = new BooleanSetting("Rotate", false);
    Timer timer = new Timer();

    public Surround() {
        super("Surround", "places obsidian around u", 0, Category.COMBAT);
        this.addSettings(this.disable);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        this.timer = new Timer();
        if (this.timer.hasTimeElapsed(100L, false)) {
            this.place();
        }
        if (this.disable.enabled) {
            this.toggle();
        }
    }

    public void place() {
        Vec3d vec3d = EntityUtil.getInterpolatedPos((Entity)this.mc.player, 0.0f);
        BlockPos northBlockPos = new BlockPos(vec3d).north();
        BlockPos southBlockPos = new BlockPos(vec3d).south();
        BlockPos eastBlockPos = new BlockPos(vec3d).east();
        BlockPos westBlockPos = new BlockPos(vec3d).west();
        BlockPos downBlockPos = new BlockPos(vec3d).down();
        ArrayList<BlockPos> pos = new ArrayList<BlockPos>();
        pos.add(northBlockPos);
        pos.add(southBlockPos);
        pos.add(eastBlockPos);
        pos.add(westBlockPos);
        if (this.mc.world.getBlockState(downBlockPos).getBlock() == Blocks.AIR) {
            pos.add(downBlockPos);
        }
        InventoryUtil.switchToSlot(Blocks.OBSIDIAN);
        int count = 0;
        float oldYaw = this.mc.player.rotationYaw;
        float oldPitch = this.mc.player.rotationPitch;
        this.mc.rightClickDelayTimer = 4;
        if (this.timer.hasTimeElapsed(100 / (count == 0 ? 1 : count), true)) {
            for (BlockPos bp : pos) {
                if (this.mc.world.getBlockState(bp).getBlock() == Blocks.OBSIDIAN) continue;
                this.mc.player.rotationYaw = count * 90;
                this.mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(bp, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                ++count;
            }
        }
        this.mc.player.rotationYaw = oldYaw;
        this.mc.player.rotationPitch = oldPitch;
    }

    public void faceVectorPacketInstant(Vec3d vec) {
        float[] rotations = this.getRotations(vec);
        this.mc.getConnection().sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], this.mc.player.onGround));
    }

    public Vec3d getEyePos() {
        double[] pos = EntityHelper.interpolate((Entity)this.mc.player);
        double x = pos[0] - this.mc.getRenderManager().viewerPosX;
        double y = pos[1] - this.mc.getRenderManager().viewerPosY;
        double z = pos[2] - this.mc.getRenderManager().viewerPosZ;
        return new Vec3d(0.0, 0.0, 1.0).rotatePitch(-((float)Math.toRadians(this.mc.player.rotationPitch))).rotateYaw(-((float)Math.toRadians(this.mc.player.rotationYaw)));
    }

    public float[] getRotations(Vec3d vec) {
        double deltaX = vec.x + vec.x - this.mc.player.posX;
        double deltaY = vec.y - 3.5 - this.mc.player.posY + (double)this.mc.player.getEyeHeight();
        double deltaZ = vec.z + vec.y - this.mc.player.posZ;
        double distance = Math.sqrt(Math.pow(deltaX, 2.0) + Math.pow(deltaZ, 2.0));
        float yaw = (float)Math.toDegrees(-Math.atan(deltaX / deltaZ));
        float pitch = (float)(-Math.toDegrees(Math.atan(deltaY / distance)));
        if (deltaX < 0.0 && deltaZ < 0.0) {
            yaw = (float)(90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        } else if (deltaX > 0.0 && deltaZ < 0.0) {
            yaw = (float)(-90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        }
        return new float[]{yaw, pitch};
    }
}

