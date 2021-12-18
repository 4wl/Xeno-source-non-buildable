/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.item.ItemShulkerBox
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ResourceLocation
 */
package me.wolfsurge.mixin.mixins;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.module.modules.render.ShulkerPreview;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiScreen.class})
public class MixinGuiScreen {
    ResourceLocation resource;

    @Inject(method={"renderToolTip"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderToolTip(ItemStack stack, int x, int y, CallbackInfo info) {
        this.resource = new ResourceLocation("textures/gui/container/shulker_box.png");
        if (Xeno.moduleManager.isModuleEnabled("ShulkerPreview") && stack.getItem() instanceof ItemShulkerBox) {
            ShulkerPreview.previewHook(stack, x, y, info);
        }
    }
}

