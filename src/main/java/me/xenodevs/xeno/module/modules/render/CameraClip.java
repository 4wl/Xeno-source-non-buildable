/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.module.modules.render;

import java.util.function.Predicate;
import me.xenodevs.xeno.event.impl.CameraClipEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class CameraClip
extends Module {
    @EventHandler
    private final Listener<CameraClipEvent> listener = new Listener<CameraClipEvent>(event -> event.cancel(), new Predicate[0]);

    public CameraClip() {
        super("CameraClip", "lets the 3rd person camera clip through blocks", Category.RENDER);
    }
}

