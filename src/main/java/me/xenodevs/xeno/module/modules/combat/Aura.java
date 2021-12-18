/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.EnumHand
 *  net.minecraftforge.event.entity.player.AttackEntityEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.xenodevs.xeno.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.xenodevs.xeno.managers.FriendManager;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.entity.EntityFakePlayer;
import me.xenodevs.xeno.utils.entity.EntityUtil;
import me.xenodevs.xeno.utils.other.RaytraceUtil;
import me.xenodevs.xeno.utils.other.Timer;
import me.xenodevs.xeno.utils.player.InventoryUtil;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils3D;
import me.xenodevs.xeno.utils.render.builder.RenderBuilder;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Aura
extends Module {
    public Timer timer = new Timer();
    public ModeSetting priority = new ModeSetting("Priority", "Closest", "Health");
    public ModeSetting rotationMode = new ModeSetting("Rotation", "Legit", "Packet");
    public NumberSetting range = new NumberSetting("Range", 4.0, 1.0, 5.0, 0.1);
    public NumberSetting speed = new NumberSetting("Speed", 10.0, 1.0, 20.0, 1.0);
    public BooleanSetting noSwing = new BooleanSetting("No Swing", false);
    public BooleanSetting rotate = new BooleanSetting("Rotate", false);
    public BooleanSetting rayTrace = new BooleanSetting("Raytrace", false);
    public ModeSetting rayTraceMode = new ModeSetting("RT Mode", "Body", "Feet");
    public BooleanSetting cooldown = new BooleanSetting("UseCooldown", true);
    public BooleanSetting autoWeapon = new BooleanSetting("AutoWeapon", false);
    public ModeSetting autoWeaponMode = new ModeSetting("AW Mode", "Target", "All");
    public BooleanSetting mobs = new BooleanSetting("Mobs", true);
    public BooleanSetting passive = new BooleanSetting("Passive", true);
    public BooleanSetting players = new BooleanSetting("Players", true);
    public BooleanSetting attackFriends = new BooleanSetting("AttackFriends", false);
    public BooleanSetting render = new BooleanSetting("Render", true);
    public ModeSetting renderMode = new ModeSetting("R Mode", "Circle", "2D");
    public ColourPicker renderColour = new ColourPicker("Render Colour", new Colour(Color.CYAN.darker()));
    public static Entity target;
    public static String entityName;
    public ArrayList<Item> weapons;

    public Aura() {
        super("Aura", "attacks things for u", Category.COMBAT);
        this.addSetting(this.priority);
        this.addSettings(this.passive, this.mobs, this.players);
        this.addSettings(this.autoWeapon, this.autoWeaponMode, this.noSwing, this.cooldown, this.attackFriends);
        this.addSettings(this.rotate, this.rotationMode);
        this.addSettings(this.rayTrace, this.rayTraceMode);
        this.addSettings(this.range, this.speed);
        this.addSettings(this.render, this.renderMode, this.renderColour);
    }

    @Override
    public void setup() {
        this.weapons = new ArrayList();
        this.weapons.add(Items.DIAMOND_SWORD);
        this.weapons.add(Items.DIAMOND_AXE);
        this.weapons.add(Items.IRON_SWORD);
        this.weapons.add(Items.IRON_AXE);
        this.weapons.add(Items.GOLDEN_SWORD);
        this.weapons.add(Items.STONE_AXE);
        this.weapons.add(Items.STONE_SWORD);
        this.weapons.add(Items.STONE_AXE);
        this.weapons.add(Items.WOODEN_SWORD);
        this.weapons.add(Items.WOODEN_AXE);
    }

    @SubscribeEvent
    public void attackEntity(AttackEntityEvent event) {
        if (this.autoWeapon.isEnabled() && (!this.autoWeaponMode.is("Target") || entityName == event.getTarget().getName())) {
            for (Item i : this.weapons) {
                if (!InventoryUtil.hasItem(i)) continue;
                InventoryUtil.switchToSlot(i);
                break;
            }
        }
    }

    public void attack(Entity e) {
        if (FriendManager.isFriend(this.name) || e instanceof EntityEnderCrystal) {
            return;
        }
        entityName = e.getName();
        if (e instanceof EntityPlayer) {
            if (this.attackFriends.isEnabled()) {
                this.rotate(e);
                this.rayTrace(e);
                if (this.timer.hasTimeElapsed((long)((double)(this.cooldown.enabled ? 5000 : 1000) / this.speed.getDoubleValue()), true)) {
                    this.mc.getConnection().sendPacket((Packet)new CPacketUseEntity(e));
                    if (!this.noSwing.isEnabled()) {
                        this.mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                }
            } else if (!FriendManager.isFriend(this.name) && !this.attackFriends.isEnabled()) {
                this.rotate(e);
                this.rayTrace(e);
                if (this.timer.hasTimeElapsed((long)((double)(this.cooldown.enabled ? 5000 : 1000) / this.speed.getDoubleValue()), true)) {
                    this.mc.getConnection().sendPacket((Packet)new CPacketUseEntity(e));
                    if (!this.noSwing.isEnabled()) {
                        this.mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                }
            }
        } else {
            this.rotate(e);
            this.rayTrace(e);
            if (this.timer.hasTimeElapsed((long)((double)(this.cooldown.enabled ? 5000 : 1000) / this.speed.getDoubleValue()), true)) {
                this.mc.getConnection().sendPacket((Packet)new CPacketUseEntity(e));
                if (!this.noSwing.isEnabled()) {
                    this.mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
        target = e;
    }

    public void rayTrace(Entity e) {
        if (this.rayTrace.isEnabled()) {
            if (this.rayTraceMode.is("Body")) {
                RaytraceUtil.raytraceEntity(e);
            } else if (this.rayTraceMode.is("Feet")) {
                RaytraceUtil.raytraceFeet(e);
            }
        }
    }

    public void rotate(Entity e) {
        if (this.rotate.isEnabled()) {
            if (this.rotationMode.is("Legit")) {
                this.mc.player.rotationYaw = this.getRotations(e)[0];
                this.mc.player.rotationPitch = this.getRotations(e)[1];
            } else if (this.rotationMode.is("Packet")) {
                this.mc.getConnection().sendPacket((Packet)new CPacketPlayer.PositionRotation(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ, this.getRotations(e)[0], this.getRotations(e)[1], this.mc.player.onGround));
            }
        }
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        if (target != null && Aura.target.isDead || target != null && (double)this.mc.player.getDistance(target) > this.range.getDoubleValue() || target == this.mc.player) {
            target = null;
        }
        if (this.priority.is("Closest")) {
            if (this.players.enabled) {
                List targetsPlayers = this.mc.world.loadedEntityList.stream().filter(EntityOtherPlayerMP.class::isInstance).collect(Collectors.toList());
                targetsPlayers = targetsPlayers.stream().filter(entity -> (double)entity.getDistance((Entity)this.mc.player) < this.range.getDoubleValue() && entity != this.mc.player && !entity.isDead && entity instanceof EntityOtherPlayerMP && !(entity instanceof EntityFakePlayer) && !((EntityOtherPlayerMP)entity).isDead).collect(Collectors.toList());
                targetsPlayers.sort(Comparator.comparingDouble(entity -> ((EntityOtherPlayerMP)entity).getDistance((Entity)this.mc.player)));
                if (!targetsPlayers.isEmpty() && targetsPlayers.get(0) != this.mc.player) {
                    target = (Entity)targetsPlayers.get(0);
                    String name = target.getName();
                    if (!this.attackFriends.isEnabled()) {
                        if (FriendManager.isFriend(name)) {
                            return;
                        }
                    }
                    this.attack(target);
                }
            }
            if (this.passive.enabled) {
                List targetsMobs = this.mc.world.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
                targetsMobs = targetsMobs.stream().filter(entity -> (double)entity.getDistance((Entity)this.mc.player) < this.range.getDoubleValue() && entity != this.mc.player && !entity.isDead && entity instanceof EntityLivingBase && !(entity instanceof EntityOtherPlayerMP) && !(entity instanceof EntityFakePlayer) && ((EntityLivingBase)entity).getHealth() > 0.0f).collect(Collectors.toList());
                targetsMobs.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getDistance((Entity)this.mc.player)));
                if (!targetsMobs.isEmpty()) {
                    target = (Entity)targetsMobs.get(0);
                    this.rotate(target);
                    this.rayTrace(target);
                    entityName = target.getName().toString();
                    this.attack(target);
                }
            }
        } else if (this.priority.is("Health")) {
            if (this.players.enabled) {
                List players = this.mc.world.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
                if ((players = players.stream().filter(entity -> (double)entity.getDistance((Entity)this.mc.player) < this.range.getDoubleValue() && entity != this.mc.player && !entity.isDead && entity instanceof EntityOtherPlayerMP && !(entity instanceof EntityFakePlayer) && !entity.isDead).collect(Collectors.toList())).size() == 1) {
                    entityName = ((Entity)players.get(0)).getName().toString();
                    if (!this.attackFriends.isEnabled()) {
                        if (FriendManager.isFriend(entityName)) {
                            return;
                        }
                    }
                    this.attack((Entity)players.get(0));
                    return;
                }
                players.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getHealth()));
                if (!players.isEmpty()) {
                    target = (Entity)players.get(0);
                    entityName = target.getName().toString();
                    if (!this.attackFriends.isEnabled()) {
                        if (FriendManager.isFriend(entityName)) {
                            return;
                        }
                    }
                    this.attack(target);
                }
            }
            if (this.mobs.enabled) {
                List mobs = this.mc.world.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
                if ((mobs = mobs.stream().filter(entity -> (double)entity.getDistance((Entity)this.mc.player) < this.range.getDoubleValue() && entity != this.mc.player && !entity.isDead && entity instanceof EntityMob).collect(Collectors.toList())).size() == 1) {
                    this.rotate((Entity)mobs.get(0));
                    this.rayTrace((Entity)mobs.get(0));
                    entityName = ((Entity)mobs.get(0)).getName().toString();
                    this.attack((Entity)mobs.get(0));
                    return;
                }
                mobs.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getHealth()));
                if (!mobs.isEmpty()) {
                    target = (Entity)mobs.get(0);
                    this.rotate(target);
                    this.rayTrace(target);
                    entityName = target.getName().toString();
                    this.attack(target);
                }
            }
            if (this.passive.enabled) {
                List passives = this.mc.world.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
                if ((passives = passives.stream().filter(entity -> (double)entity.getDistance((Entity)this.mc.player) < this.range.getDoubleValue() && entity != this.mc.player && !entity.isDead && entity instanceof EntityLivingBase && !(entity instanceof EntityOtherPlayerMP) && ((EntityLivingBase)entity).getHealth() > 0.0f).collect(Collectors.toList())).size() == 1) {
                    this.rotate((Entity)passives.get(0));
                    this.rayTrace((Entity)passives.get(0));
                    entityName = ((Entity)passives.get(0)).getName().toString();
                    this.attack((Entity)passives.get(0));
                    return;
                }
                passives.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getHealth()));
                if (!passives.isEmpty()) {
                    target = (Entity)passives.get(0);
                    this.rotate(target);
                    this.rayTrace(target);
                    entityName = target.getName().toString();
                    this.attack(target);
                }
            }
        }
    }

    @Override
    public void onRenderWorld() {
        if (target != null && this.render.isEnabled() && target != this.mc.player) {
            if (this.renderMode.is("Circle")) {
                RenderUtils3D.drawCircle(new RenderBuilder().setup().line(1.5f).depth(true).blend().texture(), EntityUtil.getInterpolatedPos(target, 1.0f), Aura.target.width, (double)Aura.target.height * (0.5 * (Math.sin((double)this.mc.player.ticksExisted * 3.5 * (Math.PI / 180)) + 1.0)), this.renderColour.getColor());
            } else if (this.renderMode.is("2D")) {
                RenderUtils3D.draw2D(target, this.renderColour.getColor());
            }
        }
    }

    public float[] getRotations(Entity e) {
        double deltaX = e.posX + (e.posX - e.lastTickPosX) - this.mc.player.posX;
        double deltaY = e.posY - 3.5 + (double)e.getEyeHeight() - this.mc.player.posY + (double)this.mc.player.getEyeHeight();
        double deltaZ = e.posZ + (e.posZ - e.lastTickPosZ) - this.mc.player.posZ;
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

