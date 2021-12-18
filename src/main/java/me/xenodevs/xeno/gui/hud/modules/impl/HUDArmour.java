/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderItem
 *  net.minecraft.item.ItemStack
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.gui.hud.modules.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.module.modules.hud.Armour;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class HUDArmour
extends HUDMod {
    private final Minecraft mc = Minecraft.getMinecraft();
    private static final RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

    public HUDArmour() {
        super("Armour", 0, 60, Xeno.moduleManager.getModule("Armour"));
    }

    @Override
    public void draw() {
        if (Armour.orientation.is("Down")) {
            int yOffset = 69;
            GL11.glPushMatrix();
            RenderUtils2D.drawRoundedRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 3.0, -1879048192);
            for (ItemStack i : this.mc.player.inventory.armorInventory) {
                yOffset -= 17;
                if (i.isEmpty()) continue;
                RenderUtils2D.drawRoundedRect(this.getX() + 1, this.getY() + yOffset, this.getWidth() - 2, 16.0, 3.0, new Colour(new Colour(Color.GRAY), 100).getRGB());
                GlStateManager.enableDepth();
                HUDArmour.itemRender.zLevel = 200.0f;
                itemRender.renderItemAndEffectIntoGUI(i, this.getX() + 2, this.getY() + yOffset);
                itemRender.renderItemOverlayIntoGUI(this.mc.fontRenderer, i, this.getX() + 2, this.getY() + yOffset, "");
                HUDArmour.itemRender.zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                float green = ((float)i.getMaxDamage() - (float)i.getItemDamage()) / (float)i.getMaxDamage();
                float red = 1.0f - green;
                int dmg = 100 - (int)(red * 100.0f);
                TextUtil.drawStringWithShadow(dmg + "%", this.getX() + 19, this.getY() + yOffset + 3, this.parent.enabled ? Colors.colourInt : -7340032);
            }
            GL11.glPopMatrix();
        } else if (Armour.orientation.is("Across")) {
            GlStateManager.enableTexture2D();
            RenderUtils2D.drawRoundedRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 3.0, -1879048192);
            int iteration = 0;
            int y = this.getY() + 1;
            ArrayList<ItemStack> armourList = new ArrayList<ItemStack>();
            for (ItemStack is : this.mc.player.inventory.armorInventory) {
                armourList.add(is);
            }
            Collections.reverse(armourList);
            for (ItemStack is : armourList) {
                if (is.isEmpty()) continue;
                int x = this.getX() + 1 + iteration * 27;
                RenderUtils2D.drawRoundedRect(x, y, 26.0, 24.0, 3.0, new Colour(new Colour(Color.GRAY), 100).getRGB());
                GlStateManager.enableDepth();
                HUDArmour.itemRender.zLevel = 200.0f;
                itemRender.renderItemAndEffectIntoGUI(is, x + 4, y + 8);
                itemRender.renderItemOverlayIntoGUI(this.mc.fontRenderer, is, x + 4, y + 8, "");
                HUDArmour.itemRender.zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                float green = ((float)is.getMaxDamage() - (float)is.getItemDamage()) / (float)is.getMaxDamage();
                float red = 1.0f - green;
                int dmg = 100 - (int)(red * 100.0f);
                TextUtil.drawCenteredString(dmg + "%", x + 13, y, Colors.colourInt);
                ++iteration;
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
        this.drag.setHeight(this.getHeight());
        this.drag.setWidth(this.getWidth());
        super.draw();
    }

    @Override
    public void renderDummy(int mouseX, int mouseY) {
        if (Armour.orientation.is("Down")) {
            int yOffset = 69;
            GL11.glPushMatrix();
            RenderUtils2D.drawRoundedRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 3.0, -1879048192);
            for (ItemStack i : this.mc.player.inventory.armorInventory) {
                yOffset -= 17;
                if (i.isEmpty()) continue;
                RenderUtils2D.drawRoundedRect(this.getX() + 1, this.getY() + yOffset, this.getWidth() - 2, 16.0, 3.0, new Colour(new Colour(Color.GRAY), 100).getRGB());
                GlStateManager.enableDepth();
                HUDArmour.itemRender.zLevel = 200.0f;
                itemRender.renderItemAndEffectIntoGUI(i, this.getX() + 2, this.getY() + yOffset);
                itemRender.renderItemOverlayIntoGUI(this.mc.fontRenderer, i, this.getX() + 2, this.getY() + yOffset, "");
                HUDArmour.itemRender.zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                float green = ((float)i.getMaxDamage() - (float)i.getItemDamage()) / (float)i.getMaxDamage();
                float red = 1.0f - green;
                int dmg = 100 - (int)(red * 100.0f);
                TextUtil.drawStringWithShadow(dmg + "%", this.getX() + 19, this.getY() + yOffset + 3, this.parent.enabled ? Colors.colourInt : -7340032);
            }
            GL11.glPopMatrix();
        } else if (Armour.orientation.is("Across")) {
            GlStateManager.enableTexture2D();
            RenderUtils2D.drawRoundedRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 3.0, -1879048192);
            int iteration = 0;
            int y = this.getY() + 1;
            ArrayList<ItemStack> armourList = new ArrayList<ItemStack>();
            for (ItemStack is : this.mc.player.inventory.armorInventory) {
                armourList.add(is);
            }
            Collections.reverse(armourList);
            for (ItemStack is : armourList) {
                if (is.isEmpty()) continue;
                int x = this.getX() + 1 + iteration * 27;
                RenderUtils2D.drawRoundedRect(x, y, 26.0, 24.0, 3.0, new Colour(new Colour(Color.GRAY), 100).getRGB());
                GlStateManager.enableDepth();
                HUDArmour.itemRender.zLevel = 200.0f;
                itemRender.renderItemAndEffectIntoGUI(is, x + 4, y + 8);
                itemRender.renderItemOverlayIntoGUI(this.mc.fontRenderer, is, x + 4, y + 8, "");
                HUDArmour.itemRender.zLevel = 0.0f;
                GlStateManager.enableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                float green = ((float)is.getMaxDamage() - (float)is.getItemDamage()) / (float)is.getMaxDamage();
                float red = 1.0f - green;
                int dmg = 100 - (int)(red * 100.0f);
                TextUtil.drawCenteredString(dmg + "%", x + 13, y, Colors.colourInt);
                ++iteration;
            }
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
        this.drag.setHeight(this.getHeight());
        this.drag.setWidth(this.getWidth());
        super.renderDummy(mouseX, mouseY);
    }

    @Override
    public int getWidth() {
        return Armour.orientation.is("Down") ? (Xeno.moduleManager.isModuleEnabled("CustomFont") ? 44 : 46) : 109;
    }

    @Override
    public float getHeight() {
        return Armour.orientation.is("Down") ? 69 : 26;
    }
}

