/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.module.modules.movement;

import java.util.function.Predicate;
import me.xenodevs.xeno.event.impl.PushEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class NoPush
extends Module {
    @EventHandler
    private final Listener<PushEvent> pushEventListener = new Listener<PushEvent>(event -> {
        if (event.entity == this.mc.player) {
            event.cancel();
        }
    }, new Predicate[0]);

    public NoPush() {
        super("NoPush", "stops entities from pushing you around", Category.MOVEMENT);
    }
}

