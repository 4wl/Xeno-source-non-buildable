/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.managers;

import java.util.ArrayList;
import java.util.Collections;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.gui.hud.modules.impl.HUDArmour;
import me.xenodevs.xeno.gui.hud.modules.impl.HUDClientName;
import me.xenodevs.xeno.gui.hud.modules.impl.HUDCoords;
import me.xenodevs.xeno.gui.hud.modules.impl.HUDFPS;
import me.xenodevs.xeno.gui.hud.modules.impl.HUDInventory;
import me.xenodevs.xeno.gui.hud.modules.impl.HUDPing;
import me.xenodevs.xeno.gui.hud.modules.impl.HUDTPS;
import me.xenodevs.xeno.gui.hud.modules.impl.HUDTotems;
import me.xenodevs.xeno.gui.hud.modules.impl.HUDWelcomer;
import me.xenodevs.xeno.module.modules.hud.ArrayListModule;

public class HudManager {
    public ArrayList<HUDMod> hudMods = new ArrayList();

    public HudManager() {
        this.hudMods.add(new HUDClientName());
        this.hudMods.add(new HUDWelcomer());
        this.hudMods.add(new HUDFPS());
        this.hudMods.add(new HUDPing());
        this.hudMods.add(new HUDCoords());
        this.hudMods.add(new HUDTPS());
        this.hudMods.add(new HUDArmour());
        this.hudMods.add(new HUDInventory());
        this.hudMods.add(new HUDTotems());
        for (HUDMod m : this.hudMods) {
            m.sub();
        }
        Collections.reverse(this.hudMods);
        Xeno.logger.info("Initialized HUD Manager");
    }

    public void reset() {
        for (HUDMod m : this.hudMods) {
            m.x = m.defaultX;
            m.y = m.defaultY;
            m.setEnabled(false);
            Xeno.config.saveHudConfig();
        }
        Collections.reverse(this.hudMods);
    }

    public void renderMods() {
        for (HUDMod m : this.hudMods) {
            if (!m.parent.enabled) continue;
            m.draw();
        }
        ArrayListModule.drawArrayList();
    }

    public HUDMod getModule(String name) {
        for (HUDMod m : this.hudMods) {
            if (m.name != name) continue;
            return m;
        }
        return null;
    }
}

