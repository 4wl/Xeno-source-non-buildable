/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  org.lwjgl.input.Keyboard
 */
package me.xenodevs.xeno.module.modules.movement;

import java.util.function.Predicate;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.event.impl.TravelEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.KeybindSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.other.Timer;
import me.xenodevs.xeno.utils.player.MotionUtil;
import me.xenodevs.xeno.utils.player.PlayerUtil;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import org.lwjgl.input.Keyboard;

public class ElytraFly
extends Module {
    public static ElytraFly INSTANCE;
    public static ModeSetting mode;
    public static NumberSetting speed;
    public static NumberSetting up;
    public static NumberSetting down;
    public static NumberSetting fall;
    public static BooleanSetting lockRotation;
    public static BooleanSetting pauseLiquid;
    public static BooleanSetting pauseCollision;
    KeybindSetting activateKey = new KeybindSetting("Activate Key", 0);
    public static int flightKey;
    Timer timer = new Timer();
    @EventHandler
    private final Listener<TravelEvent> travelListener = new Listener<TravelEvent>(event -> {
        if (!this.nullCheck() && this.mc.player.isElytraFlying()) {
            if (PlayerUtil.isInLiquid() && pauseLiquid.getValue()) {
                return;
            }
            if (PlayerUtil.isCollided() && pauseCollision.getValue()) {
                return;
            }
            this.elytraFlight((TravelEvent)event);
        }
    }, new Predicate[0]);

    public ElytraFly() {
        super("ElytraFly", "ez fly with elytra", 0, Category.MOVEMENT);
        this.addSettings(mode, speed, up, down, fall, lockRotation, pauseLiquid, pauseCollision, this.activateKey);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        this.handleKey();
    }

    public void handleKey() {
        boolean var = false;
        if (Keyboard.getEventKeyState() && Keyboard.isKeyDown((int)this.activateKey.code) && !var) {
            this.mc.getConnection().sendPacket((Packet)new CPacketEntityAction((Entity)this.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            var = true;
        }
    }

    public void elytraFlight(TravelEvent event) {
        event.cancel();
        Xeno.tickManager.setClientTicks(1.0f);
        MotionUtil.stopMotion(-fall.getDoubleValue());
        MotionUtil.setMoveSpeed(speed.getDoubleValue(), 0.6f);
        switch (mode.getMode()) {
            case "Control": {
                this.handleControl();
                break;
            }
        }
        PlayerUtil.lockLimbs();
    }

    public void handleControl() {
        if (this.mc.gameSettings.keyBindJump.isKeyDown()) {
            this.mc.player.motionY = up.getDoubleValue();
        } else if (this.mc.gameSettings.keyBindSneak.isKeyDown()) {
            this.mc.player.motionY = -down.getDoubleValue();
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode();
    }

    static {
        mode = new ModeSetting("Mode", "Control", "Packet");
        speed = new NumberSetting("Speed", 2.5, 0.0, 5.0, 0.1);
        up = new NumberSetting("Up", 1.0, 0.0, 5.0, 0.1);
        down = new NumberSetting("Down", 1.0, 0.0, 5.0, 0.1);
        fall = new NumberSetting("Fall", 0.0, 0.0, 0.1, 0.1);
        lockRotation = new BooleanSetting("LockRotation", false);
        pauseLiquid = new BooleanSetting("StopInLiquid", true);
        pauseCollision = new BooleanSetting("StopWhenColliding", false);
    }
}

