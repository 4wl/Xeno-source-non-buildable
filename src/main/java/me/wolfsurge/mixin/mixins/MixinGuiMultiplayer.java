/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiMultiplayer
 *  net.minecraft.client.gui.GuiScreen
 */
package me.wolfsurge.mixin.mixins;

import java.io.IOException;
import me.xenodevs.xeno.discord.DiscordManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiMultiplayer.class})
public abstract class MixinGuiMultiplayer
extends GuiScreen {
    @Inject(method={"drawScreen"}, at={@At(value="TAIL")})
    public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        DiscordManager.update("In Multiplayer Menu");
    }

    @Inject(method={"actionPerformed"}, at={@At(value="TAIL")})
    protected void actionPerformed(GuiButton s1, CallbackInfo ci) throws IOException {
    }
}

