/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketTimeUpdate
 *  net.minecraftforge.common.MinecraftForge
 */
package me.xenodevs.xeno.managers;

import java.util.function.Predicate;
import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.event.impl.PacketEvent;
import me.xenodevs.xeno.utils.other.MathUtils;
import me.xenodevs.xeno.utils.other.MathsHelper;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraftforge.common.MinecraftForge;

public class TickManager
implements Globals {
    private long prevTime = -1L;
    private final float[] TPS = new float[20];
    private int currentTick;
    @EventHandler
    private final Listener<PacketEvent.Receive> recieveListener = new Listener<PacketEvent.Receive>(event -> {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            if (this.prevTime != -1L) {
                this.TPS[this.currentTick % this.TPS.length] = MathsHelper.clamp(20.0f / ((float)(System.currentTimeMillis() - this.prevTime) / 1000.0f), 0.0f, 20.0f);
                ++this.currentTick;
            }
            this.prevTime = System.currentTimeMillis();
        }
    }, new Predicate[0]);

    public TickManager() {
        int len = this.TPS.length;
        for (int i = 0; i < len; ++i) {
            this.TPS[i] = 0.0f;
        }
        MinecraftForge.EVENT_BUS.register((Object)this);
        Xeno.EVENT_BUS.subscribe((Object)this);
    }

    public float getTPS(TPS tps) {
        switch (tps) {
            case CURRENT: {
                return mc.isSingleplayer() ? 20.0f : (float)MathUtils.roundDouble(MathsHelper.clamp(this.TPS[0], 0.0f, 20.0f), 2);
            }
            case AVERAGE: {
                int tickCount = 0;
                float tickRate = 0.0f;
                for (float tick : this.TPS) {
                    if (!(tick > 0.0f)) continue;
                    tickRate += tick;
                    ++tickCount;
                }
                return mc.isSingleplayer() ? 20.0f : (float)MathUtils.roundDouble(MathsHelper.clamp(tickRate / (float)tickCount, 0.0f, 20.0f), 2);
            }
        }
        return 0.0f;
    }

    public void setClientTicks(float ticks) {
        mc.world.tick();
    }

    public void shiftServerTicks(int tickShift) {
        for (int ticks = 0; ticks < tickShift; ++ticks) {
        }
    }

    public static enum TPS {
        CURRENT,
        AVERAGE,
        NONE;

    }
}

