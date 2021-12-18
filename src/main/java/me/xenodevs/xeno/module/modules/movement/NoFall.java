/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 */
package me.xenodevs.xeno.module.modules.movement;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.ModeSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoFall
extends Module {
    public static NoFall INSTANCE;
    public static ModeSetting mode;

    public NoFall() {
        super("NoFall", "dont break ur legs when you fall", 0, Category.MOVEMENT);
        this.addSetting(mode);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.nullCheck()) {
            return;
        }
        if (this.mc.player.fallDistance > 4.0f) {
            switch (mode.getValue()) {
                case 0: {
                    this.mc.player.fallDistance = 0.0f;
                    this.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(this.mc.player.posX + 420420.0, this.mc.player.posY, this.mc.player.posZ, false));
                    this.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + 1.0, this.mc.player.posZ, true));
                    break;
                }
                case 1: {
                    this.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + 1.0, this.mc.player.posZ, true));
                    break;
                }
                case 2: {
                    this.mc.getConnection().sendPacket((Packet)new CPacketPlayer(true));
                }
            }
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode();
    }

    static {
        mode = new ModeSetting("Mode", "NCP", "AAC", "Vanilla");
    }
}

