/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.world.WorldSettings
 */
package me.wolfsurge.mixin.mixins;

import me.xenodevs.xeno.discord.DiscordManager;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.mainmenu.custom.XenoMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.world.WorldSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Minecraft.class})
public class MixinMinecraft {
    @Inject(method={"init"}, at={@At(value="TAIL")})
    private void displayMainMenu(CallbackInfo ci) {
        if (GuiUtil.customMainMenu) {
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new XenoMainMenu());
        }
    }

    @Inject(method={"launchIntegratedServer"}, at={@At(value="TAIL")})
    public void singlePlayerDiscordRP(String minecraftsessionservice, String gameprofilerepository, WorldSettings playerprofilecache, CallbackInfo ci) {
        DiscordManager.update("Playing Singleplayer");
    }
}

