/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  net.minecraft.inventory.ItemStackHelper
 *  net.minecraft.item.ItemShulkerBox
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.math.MathHelper
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.module.modules.render;

import javax.annotation.Nullable;
import me.wolfsurge.api.TextUtil;
import me.wolfsurge.api.gui.font.FontUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ShulkerPreview
extends Module {
    static RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

    public ShulkerPreview() {
        super("ShulkerPreview", "shows whats in shulkers", Category.RENDER);
    }

    public static void previewHook(ItemStack stack, int x, int y, CallbackInfo info) {
        NBTTagCompound blockEntityTag;
        NBTTagCompound tagCompound;
        if (stack.getItem() instanceof ItemShulkerBox && (tagCompound = stack.getTagCompound()) != null && tagCompound.hasKey("BlockEntityTag", 10) && (blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag")).hasKey("Items", 9)) {
            info.cancel();
            NonNullList nonnulllist = NonNullList.withSize((int)27, (Object)ItemStack.EMPTY);
            ItemStackHelper.loadAllItems((NBTTagCompound)blockEntityTag, (NonNullList)nonnulllist);
            GlStateManager.enableBlend();
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int x1 = x + 4;
            int y1 = y - 30;
            ShulkerPreview.itemRender.zLevel = 300.0f;
            GL11.glPushMatrix();
            Xeno.blurManager.blur(x1, y1 - 1, 162.0, 67.0, 3);
            GL11.glPopMatrix();
            Gui.drawRect((int)x1, (int)y1, (int)(x1 + 162), (int)(y1 + 13), (int)-1879048192);
            RenderUtils2D.drawRoundedOutline(x1, y1, 162.0, 66.0, 1.0, 2.0, Colors.colourInt);
            RenderUtils2D.drawRect(x1, y1 + 12, x1 + 162, y1 + 13, Colors.colourInt);
            TextUtil.drawStringWithShadow(stack.getDisplayName(), x1 + 2, (float)y1 + 0.5f, -1);
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableGUIStandardItemLighting();
            for (int i = 0; i < nonnulllist.size(); ++i) {
                int iX = x + 5 + i % 9 * 18;
                int iY = y + 1 + (i / 9 - 1) * 18;
                ItemStack itemStack = (ItemStack)nonnulllist.get(i);
                itemRender.renderItemAndEffectIntoGUI(itemStack, iX, iY);
                ShulkerPreview.renderItemOverlayIntoGUI(FontManager.comfortaa, itemStack, iX, iY, null);
            }
            RenderHelper.disableStandardItemLighting();
            ShulkerPreview.itemRender.zLevel = 0.0f;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }

    private static void draw(BufferBuilder renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
        renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        renderer.pos((double)(x + 0), (double)(y + 0), 0.0).color(red, green, blue, alpha).endVertex();
        renderer.pos((double)(x + 0), (double)(y + height), 0.0).color(red, green, blue, alpha).endVertex();
        renderer.pos((double)(x + width), (double)(y + height), 0.0).color(red, green, blue, alpha).endVertex();
        renderer.pos((double)(x + width), (double)(y + 0), 0.0).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }

    public static void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, @Nullable String text) {
        if (!stack.isEmpty()) {
            EntityPlayerSP entityplayersp;
            float f3;
            if (stack.getCount() != 1 || text != null) {
                String s = text == null ? String.valueOf(stack.getCount()) : text;
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableBlend();
                FontUtil.comfortaa.drawStringWithShadow(s, xPosition + 16 - FontUtil.comfortaa.getStringWidth(s), yPosition + 16 - FontUtil.comfortaa.getHeight(), 0xFFFFFF);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                GlStateManager.enableBlend();
            }
            if (stack.getItem().showDurabilityBar(stack)) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                double health = stack.getItem().getDurabilityForDisplay(stack);
                int rgbfordisplay = stack.getItem().getRGBDurabilityForDisplay(stack);
                int i = Math.round(13.0f - (float)health * 13.0f);
                ShulkerPreview.draw(bufferbuilder, xPosition + 2, yPosition + 13, 13, 2, 0, 0, 0, 255);
                ShulkerPreview.draw(bufferbuilder, xPosition + 2, yPosition + 13, i, 1, rgbfordisplay >> 16 & 0xFF, rgbfordisplay >> 8 & 0xFF, rgbfordisplay & 0xFF, 255);
                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
            float f = f3 = (entityplayersp = Minecraft.getMinecraft().player) == null ? 0.0f : entityplayersp.getCooldownTracker().getCooldown(stack.getItem(), Minecraft.getMinecraft().getRenderPartialTicks());
            if (f3 > 0.0f) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                Tessellator tessellator1 = Tessellator.getInstance();
                BufferBuilder bufferbuilder1 = tessellator1.getBuffer();
                ShulkerPreview.draw(bufferbuilder1, xPosition, yPosition + MathHelper.floor((float)(16.0f * (1.0f - f3))), 16, MathHelper.ceil((float)(16.0f * f3)), 255, 255, 255, 127);
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }
    }
}

