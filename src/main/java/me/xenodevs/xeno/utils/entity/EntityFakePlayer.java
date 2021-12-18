/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.world.World
 */
package me.xenodevs.xeno.utils.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityFakePlayer
extends EntityOtherPlayerMP {
    static Minecraft mc = Minecraft.getMinecraft();

    public EntityFakePlayer() {
        super((World)EntityFakePlayer.mc.world, EntityFakePlayer.mc.player.getGameProfile());
        this.copyLocationAndAnglesFrom((Entity)EntityFakePlayer.mc.player);
        this.inventory.copyInventory(EntityFakePlayer.mc.player.inventory);
        this.rotationYawHead = EntityFakePlayer.mc.player.rotationYawHead;
        this.renderYawOffset = EntityFakePlayer.mc.player.renderYawOffset;
        this.chasingPosX = this.posX;
        this.chasingPosY = this.posY;
        this.chasingPosZ = this.posZ;
        EntityFakePlayer.mc.world.addEntityToWorld(this.getEntityId(), (Entity)this);
    }

    public EntityFakePlayer(String name) {
        super((World)EntityFakePlayer.mc.world, EntityFakePlayer.mc.player.getGameProfile());
        this.setCustomNameTag(name);
        this.copyLocationAndAnglesFrom((Entity)EntityFakePlayer.mc.player);
        this.inventory.copyInventory(EntityFakePlayer.mc.player.inventory);
        this.rotationYawHead = EntityFakePlayer.mc.player.rotationYawHead;
        this.renderYawOffset = EntityFakePlayer.mc.player.renderYawOffset;
        this.chasingPosX = this.posX;
        this.chasingPosY = this.posY;
        this.chasingPosZ = this.posZ;
        EntityFakePlayer.mc.world.addEntityToWorld(this.getEntityId(), (Entity)this);
    }

    public void resetPlayerPosition() {
        EntityFakePlayer.mc.player.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
    }

    public void despawn() {
        EntityFakePlayer.mc.world.removeEntityFromWorld(this.getEntityId());
    }
}

