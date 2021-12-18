/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  org.lwjgl.input.Mouse
 */
package me.xenodevs.xeno.module.modules.misc;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.managers.FriendManager;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.utils.other.ChatUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Mouse;

public class MCF
extends Module {
    private boolean clicked = false;

    public MCF() {
        super("MCF", "middle click people to friend / unfriend them", 0, Category.MISC);
    }

    @Override
    public void onUpdate() {
        if (Mouse.isButtonDown((int)2)) {
            if (!this.clicked && this.mc.currentScreen == null) {
                this.onClick();
            }
            this.clicked = true;
        } else {
            this.clicked = false;
        }
    }

    private void onClick() {
        Entity entity;
        RayTraceResult result = this.mc.objectMouseOver;
        if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY && (entity = result.entityHit) instanceof EntityPlayer) {
            if (FriendManager.isFriend(entity.getName())) {
                FriendManager.removeFriend(entity.getName());
                ChatUtil.addChatMessage("\u00a7c" + entity.getName() + "\u00a7r unfriended.");
                Xeno.config.saveFriendConfig();
            } else {
                Xeno.friendManager.addFriend(entity.getName());
                ChatUtil.addChatMessage("\u00a7b" + entity.getName() + "\u00a7r friended.");
                Xeno.config.saveFriendConfig();
            }
        }
        this.clicked = true;
    }
}

