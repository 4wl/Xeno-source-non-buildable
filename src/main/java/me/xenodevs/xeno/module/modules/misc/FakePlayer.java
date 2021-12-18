/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package me.xenodevs.xeno.module.modules.misc;

import java.awt.Color;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.module.settings.StringSetting;
import me.xenodevs.xeno.utils.entity.EntityFakePlayer;
import me.xenodevs.xeno.utils.render.RenderUtils3D;
import net.minecraft.entity.Entity;

public class FakePlayer
extends Module {
    StringSetting plrName = new StringSetting("Name", "FakePlayer");
    BooleanSetting trace = new BooleanSetting("Tracer", true);
    NumberSetting lineWidth = new NumberSetting("LineWidth", 0.5, 0.1, 2.0, 0.1);
    ColourPicker traceCol = new ColourPicker("TracerColour", Color.CYAN);
    EntityFakePlayer fake = null;

    public FakePlayer() {
        super("FakePlayer", "spawns you... but it ain't you", Category.MISC);
        this.addSettings(this.trace, this.lineWidth, this.traceCol);
    }

    @Override
    public void onRenderWorld() {
        if (this.trace.isEnabled() && this.fake != null) {
            RenderUtils3D.drawTracer((Entity)this.fake, this.lineWidth.getFloatValue(), this.traceCol.getColor());
        }
    }

    @Override
    public void onEnable() {
        this.fake = new EntityFakePlayer(this.plrName.getText());
        this.mc.world.playerEntities.add(this.fake);
    }

    @Override
    public void onDisable() {
        this.fake.despawn();
        this.fake = null;
    }
}

