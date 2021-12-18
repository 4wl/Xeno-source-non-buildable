/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 */
package me.xenodevs.xeno.utils.player;

import me.wolfsurge.api.util.Globals;
import net.minecraft.client.entity.EntityPlayerSP;

public class MotionUtil
implements Globals {
    public static void setMoveSpeed(double speed, float stepHeight) {
        EntityPlayerSP currentMover;
        Object object = currentMover = MotionUtil.mc.player.isRiding() ? (EntityPlayerSP) mc.player.getRidingEntity() : MotionUtil.mc.player;
        if (currentMover != null) {
            float forward = MotionUtil.mc.player.movementInput.moveForward;
            float strafe = MotionUtil.mc.player.movementInput.moveStrafe;
            float yaw = MotionUtil.mc.player.rotationYaw;
            if (!MotionUtil.isMoving()) {
                currentMover.motionX = 0.0;
                currentMover.motionZ = 0.0;
            } else if (forward != 0.0f) {
                if (strafe >= 1.0f) {
                    yaw += (float)(forward > 0.0f ? -45 : 45);
                    strafe = 0.0f;
                } else if (strafe <= -1.0f) {
                    yaw += (float)(forward > 0.0f ? 45 : -45);
                    strafe = 0.0f;
                }
                if (forward > 0.0f) {
                    forward = 1.0f;
                } else if (forward < 0.0f) {
                    forward = -1.0f;
                }
            }
            double sin = Math.sin(Math.toRadians(yaw + 90.0f));
            double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            currentMover.motionX = (double)forward * speed * cos + (double)strafe * speed * sin;
            currentMover.motionZ = (double)forward * speed * sin - (double)strafe * speed * cos;
            currentMover.stepHeight = stepHeight;
            if (!MotionUtil.isMoving()) {
                currentMover.motionX = 0.0;
                currentMover.motionZ = 0.0;
            }
        }
    }

    public static double[] getMoveSpeed(double speed) {
        float forward = MotionUtil.mc.player.movementInput.moveForward;
        float strafe = MotionUtil.mc.player.movementInput.moveStrafe;
        float yaw = MotionUtil.mc.player.rotationYaw;
        if (!MotionUtil.isMoving()) {
            return new double[]{0.0, 0.0};
        }
        if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
                strafe = 0.0f;
            } else if (strafe <= -1.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
                strafe = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double motionX = (double)forward * speed * cos + (double)strafe * speed * sin;
        double motionZ = (double)forward * speed * sin - (double)strafe * speed * cos;
        return new double[]{motionX, motionZ};
    }

    public static void stopMotion(double fall) {
        EntityPlayerSP currentMover;
        Object object = currentMover = MotionUtil.mc.player.isRiding() ? (EntityPlayerSP) mc.player.getRidingEntity() : MotionUtil.mc.player;
        if (currentMover != null) {
            currentMover.setVelocity(0.0, fall, 0.0);
        }
    }

    public static boolean isMoving() {
        return MotionUtil.mc.player.moveForward != 0.0f || MotionUtil.mc.player.moveStrafing != 0.0f;
    }

    public static boolean hasMoved() {
        return Math.pow(MotionUtil.mc.player.motionX, 2.0) + Math.pow(MotionUtil.mc.player.motionY, 2.0) + Math.pow(MotionUtil.mc.player.motionZ, 2.0) >= 9.0E-4;
    }
}

