/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.EnumAction
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.input.Mouse
 */
package me.xenodevs.xeno.utils.player;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.managers.TickManager;
import me.xenodevs.xeno.utils.player.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Mouse;

public class PlayerUtil {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static int findObiInHotbar() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = PlayerUtil.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) continue;
            Block block = ((ItemBlock)stack.getItem()).getBlock();
            if (block instanceof BlockEnderChest) {
                return i;
            }
            if (!(block instanceof BlockObsidian)) continue;
            return i;
        }
        return -1;
    }

    public static double getHealth() {
        return PlayerUtil.mc.player.getHealth() + PlayerUtil.mc.player.getAbsorptionAmount();
    }

    public static void attackEntity(Entity entity, boolean packet, boolean cooldown, boolean sync, boolean swing) {
        block7: {
            block6: {
                if (!cooldown) break block6;
                float f = sync ? 20.0f - Xeno.tickManager.getTPS(TickManager.TPS.CURRENT) : 0.0f;
                if (!(PlayerUtil.mc.player.getCooledAttackStrength(f) >= 1.0f)) break block7;
            }
            if (packet) {
                PlayerUtil.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity));
            } else {
                PlayerUtil.mc.playerController.attackEntity((EntityPlayer)PlayerUtil.mc.player, entity);
            }
            if (swing) {
                PlayerUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
            PlayerUtil.mc.player.resetCooldown();
        }
    }

    public static boolean isTrapped() {
        BlockPos[] trapPos;
        BlockPos playerPos = new BlockPos(Math.floor(PlayerUtil.mc.player.posX), Math.floor(PlayerUtil.mc.player.posY), Math.floor(PlayerUtil.mc.player.posZ));
        for (BlockPos pos : trapPos = new BlockPos[]{playerPos.down(), playerPos.up().up(), playerPos.north(), playerPos.south(), playerPos.east(), playerPos.west(), playerPos.north().up(), playerPos.south().up(), playerPos.east().up(), playerPos.west().up()}) {
            IBlockState state = PlayerUtil.mc.world.getBlockState(pos);
            if (state.getBlock() == Blocks.OBSIDIAN || PlayerUtil.mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK) continue;
            return false;
        }
        return true;
    }

    public static Vec3d getCenter(double posX, double posY, double posZ) {
        return new Vec3d(Math.floor(posX) + 0.5, Math.floor(posY), Math.floor(posZ) + 0.5);
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(PlayerUtil.mc.player.posX), Math.floor(PlayerUtil.mc.player.posY), Math.floor(PlayerUtil.mc.player.posZ));
    }

    public static void lockLimbs() {
        PlayerUtil.mc.player.prevLimbSwingAmount = 0.0f;
        PlayerUtil.mc.player.limbSwingAmount = 0.0f;
        PlayerUtil.mc.player.limbSwing = 0.0f;
    }

    public static boolean isEating() {
        return PlayerUtil.mc.player.getHeldItemMainhand().getItemUseAction().equals((Object)EnumAction.EAT) || PlayerUtil.mc.player.getHeldItemMainhand().getItemUseAction().equals((Object)EnumAction.DRINK);
    }

    public static boolean isMending() {
        return InventoryUtil.isHolding(Items.EXPERIENCE_BOTTLE) && Mouse.isButtonDown((int)1);
    }

    public static boolean isMining() {
        return InventoryUtil.isHolding(Items.DIAMOND_PICKAXE) && PlayerUtil.mc.playerController.getIsHittingBlock();
    }

    public static boolean isInLiquid() {
        return PlayerUtil.mc.player.isInLava() || PlayerUtil.mc.player.isInWater();
    }

    public static boolean isCollided() {
        return PlayerUtil.mc.player.collidedHorizontally || PlayerUtil.mc.player.collidedVertically;
    }
}

