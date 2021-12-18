/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.NonNullList
 */
package me.xenodevs.xeno.gui.hud.modules.impl;

import java.awt.Color;
import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class HUDInventory
extends HUDMod {
    private final Minecraft mc = Minecraft.getMinecraft();
    private static final RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

    public HUDInventory() {
        super("Inventory", 0, 130, Xeno.moduleManager.getModule("Inventory"));
    }

    @Override
    public void draw() {
        super.draw();
        this.drag.setHeight(this.getHeight());
        this.drag.setWidth(this.getWidth());
        RenderUtils2D.drawRoundedRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 3.0, -1879048192);
        NonNullList items = Minecraft.getMinecraft().player.inventory.mainInventory;
        int size = items.size();
        for (int item = 9; item < size; ++item) {
            int slotX = this.getX() + 1 + item % 9 * 18;
            int slotY = this.getY() + 1 + (item / 9 - 1) * 18;
            RenderUtils2D.drawRoundedRect(slotX, slotY, 16.0, 16.0, 2.0, new Colour(new Colour(Color.GRAY), 50).getRGB());
            String text = ((ItemStack)items.get(item)).getCount() > 1 ? String.valueOf(((ItemStack)items.get(item)).getCount()) : "";
            GlStateManager.enableDepth();
            HUDInventory.itemRender.zLevel = 200.0f;
            itemRender.renderItemAndEffectIntoGUI((ItemStack)items.get(item), slotX, slotY);
            itemRender.renderItemOverlayIntoGUI(this.mc.fontRenderer, (ItemStack)items.get(item), slotX, slotY, "");
            HUDInventory.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            TextUtil.drawStringWithShadow(text, slotX + 16 - TextUtil.getStringWidth(text) - 1, slotY + 7, -1);
        }
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        super.renderDummy(mouseX, mouseY);
        this.drag.setHeight(this.getHeight());
        this.drag.setWidth(this.getWidth());
        RenderUtils2D.drawRoundedRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 3.0, -1879048192);
        NonNullList items = Minecraft.getMinecraft().player.inventory.mainInventory;
        int size = items.size();
        for (int item = 9; item < size; ++item) {
            int slotX = this.getX() + 1 + item % 9 * 18;
            int slotY = this.getY() + 1 + (item / 9 - 1) * 18;
            RenderUtils2D.drawRoundedRect(slotX, slotY, 16.0, 16.0, 2.0, new Colour(new Colour(Color.GRAY), 50).getRGB());
            String text = ((ItemStack)items.get(item)).getCount() > 1 ? String.valueOf(((ItemStack)items.get(item)).getCount()) : "";
            GlStateManager.enableDepth();
            HUDInventory.itemRender.zLevel = 200.0f;
            itemRender.renderItemAndEffectIntoGUI((ItemStack)items.get(item), slotX, slotY);
            itemRender.renderItemOverlayIntoGUI(this.mc.fontRenderer, (ItemStack)items.get(item), slotX, slotY, "");
            HUDInventory.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            TextUtil.drawStringWithShadow(text, slotX + 16 - TextUtil.getStringWidth(text) - 1, slotY + 7, -1);
        }
    }

    @Override
    public int getWidth() {
        return 162;
    }

    @Override
    public float getHeight() {
        return 54.0f;
    }
}

