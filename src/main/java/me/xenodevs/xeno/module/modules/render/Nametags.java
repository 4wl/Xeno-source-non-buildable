/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.culling.ICamera
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.util.math.MathHelper
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.module.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.managers.FriendManager;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.GLUtil;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class Nametags
extends Module {
    public BooleanSetting customFont = new BooleanSetting("Custom Font", true);
    public BooleanSetting gameMode = new BooleanSetting("Gamemode", false);
    public BooleanSetting armour = new BooleanSetting("Armour", true);
    public BooleanSetting invisibles = new BooleanSetting("Invisibles", false);
    public NumberSetting distance = new NumberSetting("Distance", 250.0, 0.0, 500.0, 1.0);
    public NumberSetting arrowPos = new NumberSetting("Arrow Pos", 28.0, 0.0, 50.0, 1.0);
    public NumberSetting scale = new NumberSetting("Scale", 0.05, 0.01, 0.1, 0.01);
    public NumberSetting height = new NumberSetting("Height", 2.5, 0.5, 5.0, 0.1);
    public NumberSetting textOffset = new NumberSetting("TextOffset", 0.0, -5.0, 5.0, 1.0);
    public BooleanSetting outline = new BooleanSetting("Outline", true);
    public NumberSetting outlineWidth = new NumberSetting("Width", 1.5, 0.1, 3.0, 0.1);
    public ColourPicker fontColour = new ColourPicker("Font Colour", new Colour(255, 120, 0, 200));
    public ColourPicker outlineColourFriend = new ColourPicker("Friend Colour", new Colour(0, 128, 255, 255));
    public ColourPicker outlineColourEnemy = new ColourPicker("Enemy Colour", new Colour(255, 0, 0, 255));
    private final ICamera camera = new Frustum();
    private boolean shownItem;

    public Nametags() {
        super("Nametags", "adds stuff above players nametags", Category.RENDER);
        this.addSettings(this.customFont, this.gameMode, this.armour, this.invisibles, this.distance, this.arrowPos, this.scale, this.height, this.textOffset, this.outline, this.outlineWidth, this.outlineColourFriend, this.outlineColourEnemy);
    }

    @Override
    public void onRenderWorld() {
        if (this.nullCheck()) {
            return;
        }
        double cx = this.mc.player.lastTickPosX + (this.mc.player.posX - this.mc.player.lastTickPosX) * (double)this.mc.getRenderPartialTicks();
        double cy = this.mc.player.lastTickPosY + (this.mc.player.posY - this.mc.player.lastTickPosY) * (double)this.mc.getRenderPartialTicks();
        double cz = this.mc.player.lastTickPosZ + (this.mc.player.posZ - this.mc.player.lastTickPosZ) * (double)this.mc.getRenderPartialTicks();
        this.camera.setPosition(cx, cy, cz);
        try {
            for (EntityPlayer player : this.mc.world.playerEntities) {
                if (!player.isEntityAlive() || player == this.mc.getRenderViewEntity() || (double)player.getDistance((Entity)this.mc.player) > this.distance.getDoubleValue() || !this.camera.isBoundingBoxInFrustum(player.getEntityBoundingBox())) continue;
                NetworkPlayerInfo npi = this.mc.player.connection.getPlayerInfo(player.getGameProfile().getId());
                double pX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)this.mc.timer.renderPartialTicks - this.mc.renderManager.renderPosX;
                double pY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)this.mc.timer.renderPartialTicks - this.mc.renderManager.renderPosY;
                double pZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)this.mc.timer.renderPartialTicks - this.mc.renderManager.renderPosZ;
                if (this.getShortGamemode(npi.getGameType().getName()).equalsIgnoreCase("SP") && !this.invisibles.getValue() || player.getName().startsWith("Body #")) continue;
                this.renderNametag(player, pX, pY, pZ);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
    }

    public void renderNametag(EntityPlayer player, double x, double y, double z) {
        this.shownItem = false;
        GlStateManager.pushMatrix();
        FontRenderer var13 = this.mc.fontRenderer;
        NetworkPlayerInfo npi = this.mc.player.connection.getPlayerInfo(player.getGameProfile().getId());
        boolean isFriend = FriendManager.isFriend(player.getName());
        boolean isEnemy = !FriendManager.isFriend(player.getName());
        String name = ((isFriend || isEnemy) && player.isSneaking() ? "\u00a79" : "\u00a7r") + (isFriend ? ChatFormatting.AQUA : (isEnemy ? ChatFormatting.RED : "")) + player.getName() + (Object)ChatFormatting.RESET + (this.gameMode.getValue() ? " [" + this.getShortGamemode(npi.getGameType().getName()) + "]" : "") + " " + '\u00a7' + this.getPing(npi.getResponseTime()) + npi.getResponseTime() + "ms " + '\u00a7' + this.getHealth(player.getHealth() + player.getAbsorptionAmount()) + MathHelper.ceil((float)(player.getHealth() + player.getAbsorptionAmount()));
        name = name.replace(".0", "");
        float distance = this.mc.player.getDistance((Entity)player);
        float var15 = (float)((distance / 5.0f <= 2.0f ? 2.0 : (double)(distance / 5.0f) * (this.scale.getDoubleValue() * 10.0 + 1.0)) * 2.5 * (this.scale.getDoubleValue() / 10.0));
        boolean far = distance / 5.0f > 2.0f;
        GL11.glTranslated((double)((float)x), (double)((double)((float)y) + this.height.getDoubleValue() - (player.isSneaking() ? 0.4 : 0.0) + (far ? (double)(distance / 12.0f) - 0.7 : 0.25)), (double)((float)z));
        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)(-this.mc.renderManager.playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)this.mc.renderManager.playerViewX, (float)(this.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GL11.glScalef((float)(-var15), (float)(-var15), (float)var15);
        GLUtil.disableGlCap(2896, 2929);
        GLUtil.enableGlCap(3042);
        GL11.glBlendFunc((int)770, (int)771);
        int width = this.customFont.getValue() ? FontManager.comfortaa.getStringWidth(name) / 2 + 1 : var13.getStringWidth(name) / 2 + 1;
        int color = Color.BLACK.getRGB();
        int outlineColor = -1;
        if (isFriend) {
            outlineColor = this.outlineColourFriend.getColor().getRGB();
        } else if (isEnemy) {
            outlineColor = this.outlineColourEnemy.getColor().getRGB();
        }
        int height = (this.customFont.isEnabled() ? FontManager.comfortaa.getHeight() : this.mc.fontRenderer.FONT_HEIGHT) + 2;
        RenderUtils2D.drawRoundedRect(-width - 2, 0.0, (width + 1) * 2, height + 1, 5.0, -1879048192);
        if (this.outline.isEnabled()) {
            RenderUtils2D.drawRoundedOutline(-width - 2, 0.0, (width + 1) * 2, height + 1, 5.0, this.outlineWidth.getDoubleValue(), outlineColor);
        }
        if (this.customFont.getValue()) {
            FontManager.comfortaa.drawStringWithShadow(name, -width, 3.0f + (float)this.textOffset.getDoubleValue(), this.fontColour.getColor().getRGB());
        } else {
            this.mc.fontRenderer.drawStringWithShadow(name, (float)(-width), 2.0f + (float)this.textOffset.getDoubleValue(), this.fontColour.getColor().getRGB());
        }
        if (this.armour.getValue()) {
            int xOffset = -6;
            int count = 0;
            for (ItemStack armourStack : player.inventory.armorInventory) {
                if (armourStack == null) continue;
                xOffset -= 8;
                if (armourStack.getItem() == Items.AIR) continue;
                ++count;
            }
            if (player.getHeldItemOffhand().getItem() != Items.AIR) {
                ++count;
            }
            int cacheX = xOffset - 8;
            xOffset += 8 * (5 - count) - (count == 0 ? 4 : 0);
            if (player.getHeldItemMainhand().getItem() != Items.AIR) {
                ItemStack renderStack = player.getHeldItemMainhand().copy();
                this.renderItem(player, renderStack, xOffset -= 8, -7, cacheX, true);
                xOffset += 16;
            } else {
                this.shownItem = true;
            }
            for (int index = 3; index >= 0; --index) {
                ItemStack armourStack = (ItemStack)player.inventory.armorInventory.get(index);
                if (armourStack.getItem() == Items.AIR) continue;
                ItemStack renderStack1 = armourStack.copy();
                this.renderItem(player, renderStack1, xOffset, -7, cacheX, false);
                xOffset += 16;
            }
            if (player.getHeldItemOffhand().getItem() != Items.AIR) {
                ItemStack renderOffhand = player.getHeldItemOffhand().copy();
                this.renderItem(player, renderOffhand, xOffset, -7, cacheX, false);
            }
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
        } else if (this.armour.getValue()) {
            int xOffset = -6;
            int count = 0;
            for (ItemStack armourStack : player.inventory.armorInventory) {
                if (armourStack == null) continue;
                xOffset -= 8;
                if (armourStack.getItem() == Items.AIR) continue;
                ++count;
            }
            if (player.getHeldItemOffhand().getItem() != Items.AIR) {
                ++count;
            }
            xOffset += 8 * (5 - count) - (count == 0 ? 4 : 0);
            for (int index = 3; index >= 0; --index) {
                ItemStack armourStack;
                armourStack = (ItemStack)player.inventory.armorInventory.get(index);
                if (armourStack.getItem() == Items.AIR) continue;
                ItemStack renderStack1 = armourStack.copy();
                this.renderDurabilityText(renderStack1, xOffset);
                xOffset += 16;
            }
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
        }
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GLUtil.reset();
        GlStateManager.resetColor();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
    }

    private void renderDurabilityText(ItemStack stack, int x) {
        if (stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool) {
            float green = ((float)stack.getMaxDamage() - (float)stack.getItemDamage()) / (float)stack.getMaxDamage();
            float red = 1.0f - green;
            int dmg = 100 - (int)(red * 100.0f);
            if (this.customFont.getValue()) {
                FontManager.comfortaa.drawStringWithShadow(dmg + "%", x * 2 + 4, -17.0f, new Color(red, green, 0.0f).getRGB());
            } else {
                this.mc.fontRenderer.drawStringWithShadow(dmg + "%", (float)(x * 2 + 4), -17.0f, new Color(red, green, 0.0f).getRGB());
            }
        }
    }

    public void renderItem(EntityPlayer player, ItemStack stack, int x, int y, int nameX, boolean showHeldItemText) {
        GL11.glPushMatrix();
        GL11.glDepthMask((boolean)true);
        GlStateManager.clear((int)256);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        this.mc.getRenderItem().zLevel = -100.0f;
        GlStateManager.scale((float)1.0f, (float)1.0f, (float)0.01f);
        this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y / 2 - 15);
        if (this.armour.getValue()) {
            this.mc.getRenderItem().renderItemOverlays(this.mc.fontRenderer, stack, x, y / 2 - 15);
        }
        this.mc.getRenderItem().zLevel = 0.0f;
        GlStateManager.scale((float)1.0f, (float)1.0f, (float)1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale((double)0.5, (double)0.5, (double)0.5);
        GlStateManager.disableDepth();
        this.renderEnchantText(player, stack, x, y - 22);
        if (!this.shownItem && showHeldItemText) {
            if (this.customFont.getValue()) {
                // empty if block
            }
            this.shownItem = true;
        }
        GlStateManager.enableDepth();
        GlStateManager.scale((float)2.0f, (float)2.0f, (float)2.0f);
        GL11.glPopMatrix();
    }

    public void renderEnchantText(EntityPlayer player, ItemStack stack, int x, int y) {
        int yCount = y;
        if ((stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool) && this.armour.getValue()) {
            float green = ((float)stack.getMaxDamage() - (float)stack.getItemDamage()) / (float)stack.getMaxDamage();
            float red = 1.0f - green;
            int dmg = 100 - (int)(red * 100.0f);
            if (this.customFont.getValue()) {
                FontManager.comfortaa.drawStringWithShadow(dmg + "%", x * 2 + 4, y - 10, new Color(red, green, 0.0f).getRGB());
            } else {
                this.mc.fontRenderer.drawStringWithShadow(dmg + "%", (float)(x * 2 + 4), (float)(y - 10), new Color(red, green, 0.0f).getRGB());
            }
        }
        NBTTagList enchants = stack.getEnchantmentTagList();
        for (int index = 0; index < enchants.tagCount(); ++index) {
            short id = enchants.getCompoundTagAt(index).getShort("id");
            short level = enchants.getCompoundTagAt(index).getShort("lvl");
            Enchantment enc = Enchantment.getEnchantmentByID((int)id);
            if (enc == null || enc.isCurse()) continue;
            String encName = level == 1 ? enc.getTranslatedName((int)level).substring(0, 3).toLowerCase() : enc.getTranslatedName((int)level).substring(0, 2).toLowerCase() + level;
            encName = encName.substring(0, 1).toUpperCase() + encName.substring(1);
            GL11.glPushMatrix();
            GL11.glScalef((float)1.0f, (float)1.0f, (float)0.0f);
            if (this.customFont.getValue()) {
                FontManager.comfortaa.drawStringWithShadow(encName, x * 2 + 3, yCount, -1);
            } else {
                this.mc.fontRenderer.drawStringWithShadow(encName, (float)(x * 2 + 3), (float)yCount, -1);
            }
            GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glPopMatrix();
            yCount += 10;
        }
    }

    public boolean isMaxEnchants(ItemStack stack) {
        return stack.getEnchantmentTagList().tagCount() > 2;
    }

    public String getHealth(float health) {
        if (health > 18.0f) {
            return "a";
        }
        if (health > 16.0f) {
            return "2";
        }
        if (health > 12.0f) {
            return "e";
        }
        if (health > 8.0f) {
            return "6";
        }
        if (health > 5.0f) {
            return "c";
        }
        return "4";
    }

    public String getPing(float ping) {
        if (ping > 200.0f) {
            return "c";
        }
        if (ping > 100.0f) {
            return "e";
        }
        return "a";
    }

    public String getPop(int pops) {
        return "";
    }

    public static int changeAlpha(int origColor, int userInputedAlpha) {
        return userInputedAlpha << 24 | (origColor &= 0xFFFFFF);
    }

    public String getShortGamemode(String gameType) {
        if (gameType.equalsIgnoreCase("survival")) {
            return "S";
        }
        if (gameType.equalsIgnoreCase("creative")) {
            return "C";
        }
        if (gameType.equalsIgnoreCase("adventure")) {
            return "A";
        }
        if (gameType.equalsIgnoreCase("spectator")) {
            return "SP";
        }
        return "NONE";
    }
}

