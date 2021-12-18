/*
 * Decompiled with CFR 0.150.
 */
package me.xenodevs.xeno.storage;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.friends.Friend;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.click.ClickGui;
import me.xenodevs.xeno.gui.click.component.Frame;
import me.xenodevs.xeno.gui.hud.HudConfig;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.managers.FriendManager;
import me.xenodevs.xeno.managers.ModuleManager;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.misc.FakePlayer;
import me.xenodevs.xeno.module.modules.movement.ElytraFly;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.KeybindSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.module.settings.Setting;
import me.xenodevs.xeno.module.settings.StringSetting;
import me.xenodevs.xeno.storage.Configuration;
import me.xenodevs.xeno.storage.ConfigurationAPI;
import me.xenodevs.xeno.utils.render.Colour;

public class Config {
    public File configFolder = new File("Xeno");
    public File hudFolder = new File("Xeno/HUD");
    public File moduleFolder = new File("Xeno/Modules");
    public File hudParentFolder = new File("Xeno/HUD/ParentModules");
    public File clickGuiFolder = new File("Xeno/ClickGUI");
    public File miscFolder = new File("Xeno/Misc");
    public Configuration hudConfig;
    public Configuration moduleConfig;
    public Configuration clickGUIConfig;
    public Configuration hudConfigToSave = ConfigurationAPI.newConfiguration(new File("Xeno/HUD/HudConfiguration.json"));
    public Configuration moduleConfigToSave;
    public Configuration clickGUIConfigToSave = ConfigurationAPI.newConfiguration(new File("Xeno/ClickGUI/ClickGUI.json"));

    public void saveHudConfig() {
        if (!this.configFolder.exists()) {
            this.configFolder.mkdirs();
        }
        if (!this.hudFolder.exists()) {
            this.hudFolder.mkdirs();
        }
        for (HUDMod m : Xeno.hudManager.hudMods) {
            this.hudConfigToSave.set(m.name.toLowerCase() + "X", m.getX());
            this.hudConfigToSave.set(m.name.toLowerCase() + "Y", m.getY());
            this.hudConfigToSave.set(m.name.toLowerCase() + "Enabled", m.parent.enabled);
        }
        for (Frame f : HudConfig.frames) {
            this.hudConfigToSave.set(f.category.name() + "X", f.getX());
            this.hudConfigToSave.set(f.category.name() + "Y", f.getY());
            this.hudConfigToSave.set(f.category.name() + "Open", f.isOpen());
        }
        try {
            this.hudConfigToSave.save();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public void loadHUDConfig() {
        if (!this.configFolder.exists()) {
            this.configFolder.mkdirs();
        }
        if (!this.hudFolder.exists()) {
            this.hudFolder.mkdirs();
        }
        try {
            this.hudConfig = ConfigurationAPI.loadExistingConfiguration(new File("Xeno/HUD/HudConfiguration.json"));
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public void saveModConfig(Module m) {
        if (!this.configFolder.exists()) {
            this.configFolder.mkdirs();
        }
        if (!this.moduleFolder.exists()) {
            this.moduleFolder.mkdirs();
        }
        if (!this.hudParentFolder.exists()) {
            this.hudParentFolder.mkdirs();
        }
        if (m instanceof FakePlayer) {
            return;
        }
        this.moduleConfigToSave = m.isHudParent ? ConfigurationAPI.newConfiguration(new File("Xeno/HUD/ParentModules" + m.name + ".json")) : ConfigurationAPI.newConfiguration(new File("Xeno/Modules/" + m.name + ".json"));
        this.moduleConfigToSave.setBool(m.name, m.isEnabled());
        for (Setting s : m.settings) {
            try {
                if (s instanceof BooleanSetting) {
                    this.moduleConfigToSave.set("Bool_" + s.name, ((BooleanSetting)s).enabled);
                }
                if (s instanceof NumberSetting) {
                    this.moduleConfigToSave.set("Num_" + s.name, ((NumberSetting)s).value);
                }
                if (s instanceof ModeSetting) {
                    this.moduleConfigToSave.set("Mode_" + s.name, ((ModeSetting)s).index);
                }
                if (s instanceof KeybindSetting) {
                    this.moduleConfigToSave.set("Key_" + s.getName(), ((KeybindSetting)s).getKeyCode());
                }
                if (s instanceof ColourPicker) {
                    this.moduleConfigToSave.set("Col_" + s.getName() + " R", ((ColourPicker)s).getColor().getRed());
                    this.moduleConfigToSave.set("Col_" + s.getName() + " G", ((ColourPicker)s).getColor().getGreen());
                    this.moduleConfigToSave.set("Col_" + s.getName() + " B", ((ColourPicker)s).getColor().getBlue());
                    this.moduleConfigToSave.set("Col_" + s.getName() + " A", ((ColourPicker)s).getColor().getAlpha());
                    this.moduleConfigToSave.set("Col_" + s.getName() + " Rainbow", ((ColourPicker)s).getRainbow());
                }
                if (!(s instanceof StringSetting)) continue;
                this.moduleConfigToSave.set("Str_" + s.getName(), ((StringSetting)s).getText());
            }
            catch (Exception exception) {}
        }
        try {
            this.moduleConfigToSave.save();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadModConfig() {
        if (!this.configFolder.exists()) {
            this.configFolder.mkdirs();
        }
        if (!this.moduleFolder.exists()) {
            this.moduleFolder.mkdirs();
        }
        if (!this.hudParentFolder.exists()) {
            this.hudParentFolder.mkdirs();
        }
        for (Module m : ModuleManager.modules) {
            try {
                if (m instanceof FakePlayer) continue;
                this.moduleConfig = !m.isHudParent ? ConfigurationAPI.loadExistingConfiguration(new File("Xeno/Modules/" + m.name + ".json")) : ConfigurationAPI.loadExistingConfiguration(new File("Xeno/HUD/ParentModules" + m.name + ".json"));
                m.enabled = (Boolean)this.moduleConfig.get(m.name);
                if (m.enabled) {
                    try {
                        m.enable();
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                for (Setting s : m.settings) {
                    try {
                        if (s instanceof BooleanSetting) {
                            ((BooleanSetting)s).setEnabled(this.moduleConfig.getBoolean("Bool_" + s.getName()));
                        }
                        if (s instanceof NumberSetting) {
                            ((NumberSetting)s).setValue(this.moduleConfig.getDouble("Num_" + s.getName(), ((NumberSetting)s).defaultValue));
                        }
                        if (s instanceof ModeSetting) {
                            ((ModeSetting)s).index = (Integer)this.moduleConfig.get("Mode_" + s.name);
                        }
                        if (s instanceof KeybindSetting) {
                            ((KeybindSetting)s).setKeyCode((int)this.moduleConfig.getDouble("Key_" + s.getName(), Double.valueOf(((KeybindSetting)s).defaultCode)));
                        }
                        if (s instanceof ColourPicker) {
                            Color c = new Color((Integer)this.moduleConfig.get("Col_" + s.getName() + " R"), (Integer)this.moduleConfig.get("Col_" + s.getName() + " G"), (Integer)this.moduleConfig.get("Col_" + s.getName() + " B"), (Integer)this.moduleConfig.get("Col_" + s.getName() + " A"));
                            ((ColourPicker)s).setValue(new Colour(c));
                            ((ColourPicker)s).setRainbow(this.moduleConfig.getBoolean("Col_" + s.getName() + " Rainbow"));
                        }
                        if (!(s instanceof StringSetting)) continue;
                        ((StringSetting)s).setText((String)this.moduleConfig.get("Str_" + s.getName()));
                    }
                    catch (Exception exception) {}
                }
            }
            catch (IOException iOException) {
            }
            catch (NumberFormatException numberFormatException) {
            }
            catch (NullPointerException nullPointerException) {
            }
        }
    }

    public void saveClickGUIConfig() {
        if (!this.configFolder.exists()) {
            this.configFolder.mkdirs();
        }
        if (!this.clickGuiFolder.exists()) {
            this.clickGuiFolder.mkdirs();
        }
        for (Frame f : ClickGui.frames) {
            this.clickGUIConfigToSave.set(f.category.name() + "X", f.getX());
            this.clickGUIConfigToSave.set(f.category.name() + "Y", f.getY());
            this.clickGUIConfigToSave.set(f.category.name() + "Open", f.isOpen());
        }
        try {
            this.clickGUIConfigToSave.save();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public void loadClickGUIConfig() {
        if (!this.configFolder.exists()) {
            this.configFolder.mkdirs();
        }
        if (!this.clickGuiFolder.exists()) {
            this.clickGuiFolder.mkdirs();
        }
        try {
            this.clickGUIConfig = ConfigurationAPI.loadExistingConfiguration(new File("Xeno/ClickGUI/ClickGUI.json"));
        }
        catch (IOException iOException) {
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void saveFriendConfig() {
        File file;
        if (!this.configFolder.exists()) {
            this.configFolder.mkdirs();
        }
        if (!this.miscFolder.exists()) {
            this.miscFolder.mkdirs();
        }
        if (!(file = new File(this.miscFolder + "/Friends.Xeno")).exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        try {
            PrintWriter pw = new PrintWriter(this.miscFolder + "/Friends.Xeno");
            for (Friend str : FriendManager.friends) {
                pw.println(str.getName());
            }
            pw.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadFriendConfig() {
        if (!this.configFolder.exists()) {
            this.configFolder.mkdirs();
        }
        if (!this.miscFolder.exists()) {
            this.miscFolder.mkdirs();
        }
        ArrayList<String> lines = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.miscFolder + "/Friends.Xeno"));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
        for (String str : lines) {
            Xeno.friendManager.addFriend(str);
            Xeno.logger.info("Added friend: " + str);
        }
    }

    public void saveMisc() {
        PrintWriter pw;
        if (!this.configFolder.exists()) {
            this.configFolder.mkdirs();
        }
        if (!this.miscFolder.exists()) {
            this.miscFolder.mkdirs();
        }
        try {
            pw = new PrintWriter(this.miscFolder + "/ElytraFlyActivate.Xeno");
            pw.println(ElytraFly.flightKey);
            pw.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            pw = new PrintWriter(this.miscFolder + "/CustomMainMenu.Xeno");
            pw.println(String.valueOf(GuiUtil.customMainMenu));
            pw.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadMisc() {
        String line;
        BufferedReader reader;
        if (!this.configFolder.exists()) {
            this.configFolder.mkdirs();
        }
        if (!this.miscFolder.exists()) {
            this.miscFolder.mkdirs();
        }
        try {
            reader = new BufferedReader(new FileReader(this.miscFolder + "/ElytraFlyActivate.Xeno"));
            line = reader.readLine();
            ElytraFly.flightKey = Integer.parseInt(line);
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            reader = new BufferedReader(new FileReader(this.miscFolder + "/CustomMainMenu.Xeno"));
            line = reader.readLine();
            GuiUtil.customMainMenu = Boolean.parseBoolean(line);
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePreset(ModuleManager mm) {
    }
}

