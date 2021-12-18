/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.client.event.RenderGameOverlayEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ServerTickEvent
 *  org.lwjgl.input.Keyboard
 */
package me.xenodevs.xeno.managers;

import java.util.ArrayList;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.module.modules.client.CustomFont;
import me.xenodevs.xeno.module.modules.combat.Aura;
import me.xenodevs.xeno.module.modules.combat.AutoArmour;
import me.xenodevs.xeno.module.modules.combat.AutoCrystal;
import me.xenodevs.xeno.module.modules.combat.AutoXP;
import me.xenodevs.xeno.module.modules.combat.FastXP;
import me.xenodevs.xeno.module.modules.combat.HoleFill;
import me.xenodevs.xeno.module.modules.combat.Offhand;
import me.xenodevs.xeno.module.modules.combat.Surround;
import me.xenodevs.xeno.module.modules.hud.Armour;
import me.xenodevs.xeno.module.modules.hud.ArrayListModule;
import me.xenodevs.xeno.module.modules.hud.ClientName;
import me.xenodevs.xeno.module.modules.hud.Coordinates;
import me.xenodevs.xeno.module.modules.hud.FPS;
import me.xenodevs.xeno.module.modules.hud.HUD;
import me.xenodevs.xeno.module.modules.hud.Inventory;
import me.xenodevs.xeno.module.modules.hud.Ping;
import me.xenodevs.xeno.module.modules.hud.TPS;
import me.xenodevs.xeno.module.modules.hud.Totems;
import me.xenodevs.xeno.module.modules.hud.Welcomer;
import me.xenodevs.xeno.module.modules.misc.AutoEZ;
import me.xenodevs.xeno.module.modules.misc.FakePlayer;
import me.xenodevs.xeno.module.modules.misc.MCF;
import me.xenodevs.xeno.module.modules.misc.Suffix;
import me.xenodevs.xeno.module.modules.movement.ElytraFly;
import me.xenodevs.xeno.module.modules.movement.Fly;
import me.xenodevs.xeno.module.modules.movement.IceSpeed;
import me.xenodevs.xeno.module.modules.movement.Jetpack;
import me.xenodevs.xeno.module.modules.movement.NoFall;
import me.xenodevs.xeno.module.modules.movement.NoPush;
import me.xenodevs.xeno.module.modules.movement.ReverseStep;
import me.xenodevs.xeno.module.modules.movement.Sprint;
import me.xenodevs.xeno.module.modules.movement.Step;
import me.xenodevs.xeno.module.modules.movement.Velocity;
import me.xenodevs.xeno.module.modules.player.AntiAFK;
import me.xenodevs.xeno.module.modules.player.FastBreak;
import me.xenodevs.xeno.module.modules.player.FastPlace;
import me.xenodevs.xeno.module.modules.render.CameraClip;
import me.xenodevs.xeno.module.modules.render.Chams;
import me.xenodevs.xeno.module.modules.render.ESP;
import me.xenodevs.xeno.module.modules.render.Fullbright;
import me.xenodevs.xeno.module.modules.render.HoleESP;
import me.xenodevs.xeno.module.modules.render.ItemPhysics;
import me.xenodevs.xeno.module.modules.render.MobOwner;
import me.xenodevs.xeno.module.modules.render.Nametags;
import me.xenodevs.xeno.module.modules.render.NoRender;
import me.xenodevs.xeno.module.modules.render.ShulkerPreview;
import me.xenodevs.xeno.module.modules.render.StorageESP;
import me.xenodevs.xeno.module.modules.render.Tracers;
import me.xenodevs.xeno.utils.other.GeneralUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class ModuleManager {
    public static ArrayList<Module> modules = new ArrayList();

    public ModuleManager() {
        modules = new ArrayList();
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.init();
        Xeno.logger.info("Initialized Modules");
    }

    public void init() {
        modules.add(new Aura());
        modules.add(new Offhand());
        modules.add(new Surround());
        modules.add(new AutoArmour());
        modules.add(new AutoCrystal());
        modules.add(new FastXP());
        modules.add(new AutoXP());
        modules.add(new Fly());
        modules.add(new Sprint());
        modules.add(new NoFall());
        modules.add(new ElytraFly());
        modules.add(new Velocity());
        modules.add(new Step());
        modules.add(new ReverseStep());
        modules.add(new Jetpack());
        modules.add(new IceSpeed());
        modules.add(new NoPush());
        modules.add(new ESP());
        modules.add(new Chams());
        modules.add(new StorageESP());
        modules.add(new Tracers());
        modules.add(new ItemPhysics());
        modules.add(new HoleESP());
        modules.add(new Fullbright());
        modules.add(new NoRender());
        modules.add(new Nametags());
        modules.add(new MobOwner());
        modules.add(new CameraClip());
        modules.add(new HoleFill());
        modules.add(new ShulkerPreview());
        modules.add(new FastBreak());
        modules.add(new FastPlace());
        modules.add(new AntiAFK());
        modules.add(new ClickGuiModule());
        modules.add(new Colors());
        modules.add(new CustomFont());
        modules.add(new AutoEZ());
        modules.add(new MCF());
        modules.add(new Suffix());
        modules.add(new FakePlayer());
        modules.add(new HUD());
        modules.add(new Armour());
        modules.add(new ArrayListModule());
        modules.add(new ClientName());
        modules.add(new Coordinates());
        modules.add(new FPS());
        modules.add(new Inventory());
        modules.add(new Ping());
        modules.add(new Totems());
        modules.add(new TPS());
        modules.add(new Welcomer());
        System.out.println(modules.size());
    }

    public Module getModule(String name) {
        for (Module m : modules) {
            if (!m.getName().equalsIgnoreCase(name)) continue;
            return m;
        }
        return null;
    }

    public static Module getModuleByName(String name) {
        for (Module m : modules) {
            if (!m.getName().equalsIgnoreCase(name)) continue;
            return m;
        }
        return null;
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            for (Module m : modules) {
                if (m.getKey() != Keyboard.getEventKey() || m.getKey() <= 1) continue;
                m.toggle();
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            GeneralUtil.handleRuleBreakers();
            for (Module m : modules) {
                if (m.enabled) {
                    m.onUpdate();
                }
                m.onNonToggledUpdate();
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            for (Module m : modules) {
                if (!m.enabled) continue;
                m.onRenderWorld();
            }
        }
    }

    @SubscribeEvent
    public void onRenderGUI(RenderGameOverlayEvent event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            for (Module m : modules) {
                if (!m.enabled) continue;
                m.onRenderGUI();
            }
        }
    }

    @SubscribeEvent
    public void onFastTick(TickEvent event) {
        this.onFastUpdate();
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        this.onServerUpdate();
    }

    public void onFastUpdate() {
        try {
            for (Module m : modules) {
                if (!m.isEnabled()) continue;
                m.onFastUpdate();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void onServerUpdate() {
        try {
            for (Module m : modules) {
                if (!m.isEnabled()) continue;
                m.onServerUpdate();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public ArrayList<Module> getModulesInCategory(Category cat) {
        ArrayList<Module> mods = new ArrayList<Module>();
        for (Module m : modules) {
            if (m.category != cat) continue;
            mods.add(m);
        }
        return mods;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public boolean isModuleEnabled(String name) {
        for (Module m : modules) {
            if (!m.getName().equalsIgnoreCase(name)) continue;
            return m.isEnabled();
        }
        return false;
    }
}

