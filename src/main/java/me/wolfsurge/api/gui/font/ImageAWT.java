/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.texture.TextureUtil
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package me.wolfsurge.api.gui.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import me.wolfsurge.api.gui.font.FontCache;
import me.wolfsurge.api.util.Globals;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class ImageAWT
implements Globals {
    private static final ArrayList<ImageAWT> activeFontRenderers = new ArrayList();
    private static int gcTicks = 0;
    private final Font font;
    private int fontHeight = -1;
    private final CharLocation[] charLocations;
    private final HashMap<String, FontCache> cachedStrings = new HashMap();
    private int textureID = 0;
    private int textureWidth = 0;
    private int textureHeight = 0;

    public static void garbageCollectionTick() {
        if (gcTicks++ > 600) {
            activeFontRenderers.forEach(ImageAWT::collectGarbage);
            gcTicks = 0;
        }
    }

    public ImageAWT(Font font, int startChar, int stopChar) {
        this.font = font;
        this.charLocations = new CharLocation[stopChar];
        this.renderBitmap(startChar, stopChar);
        activeFontRenderers.add(this);
    }

    public ImageAWT(Font font) {
        this(font, 0, 255);
    }

    private void collectGarbage() {
        long currentTime = System.currentTimeMillis();
        this.cachedStrings.entrySet().stream().filter(entry -> currentTime - ((FontCache)entry.getValue()).getLastUsage() > 30000L).forEach(entry -> {
            GL11.glDeleteLists((int)((FontCache)entry.getValue()).getDisplayList(), (int)1);
            ((FontCache)entry.getValue()).setDeleted(true);
            this.cachedStrings.remove(entry.getKey());
        });
    }

    public int getHeight() {
        return (this.fontHeight - 8) / 2;
    }

    public void drawString(String text, double x, double y, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.scale((double)0.25, (double)0.25, (double)0.25);
        GL11.glTranslated((double)(x * 2.0), (double)(y * 2.0 - 2.0), (double)0.0);
        GlStateManager.bindTexture((int)this.textureID);
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        GlStateManager.color((float)red, (float)green, (float)blue, (float)alpha);
        double currX = 0.0;
        FontCache cached = this.cachedStrings.get(text);
        if (cached != null) {
            GL11.glCallList((int)cached.getDisplayList());
            cached.setLastUsage(System.currentTimeMillis());
            GlStateManager.popMatrix();
            return;
        }
        int list = -1;
        boolean assumeNonVolatile = false;
        if (assumeNonVolatile) {
            list = GL11.glGenLists((int)1);
            GL11.glNewList((int)list, (int)4865);
        }
        GL11.glBegin((int)7);
        for (char ch : text.toCharArray()) {
            CharLocation fontChar;
            if (Character.getNumericValue(ch) >= this.charLocations.length) {
                GL11.glEnd();
                GlStateManager.scale((double)4.0, (double)4.0, (double)4.0);
                ImageAWT.mc.fontRenderer.drawString(String.valueOf(ch), (float)currX * 0.25f + 1.0f, 2.0f, color, false);
                currX += (double)ImageAWT.mc.fontRenderer.getStringWidth(String.valueOf(ch)) * 4.0;
                GlStateManager.scale((double)0.25, (double)0.25, (double)0.25);
                GlStateManager.bindTexture((int)this.textureID);
                GlStateManager.color((float)red, (float)green, (float)blue, (float)alpha);
                GL11.glBegin((int)7);
                continue;
            }
            if (this.charLocations.length <= ch || (fontChar = this.charLocations[ch]) == null) continue;
            this.drawChar(fontChar, (float)currX, 0.0f);
            currX += (double)fontChar.width - 8.0;
        }
        GL11.glEnd();
        if (assumeNonVolatile) {
            this.cachedStrings.put(text, new FontCache(list, System.currentTimeMillis()));
            GL11.glEndList();
        }
        GlStateManager.popMatrix();
    }

    private void drawChar(CharLocation ch, float x, float y) {
        float width = ch.width;
        float height = ch.height;
        float srcX = ch.x;
        float srcY = ch.y;
        float renderX = srcX / (float)this.textureWidth;
        float renderY = srcY / (float)this.textureHeight;
        float renderWidth = width / (float)this.textureWidth;
        float renderHeight = height / (float)this.textureHeight;
        GL11.glTexCoord2f((float)renderX, (float)renderY);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glTexCoord2f((float)renderX, (float)(renderY + renderHeight));
        GL11.glVertex2f((float)x, (float)(y + height));
        GL11.glTexCoord2f((float)(renderX + renderWidth), (float)(renderY + renderHeight));
        GL11.glVertex2f((float)(x + width), (float)(y + height));
        GL11.glTexCoord2f((float)(renderX + renderWidth), (float)renderY);
        GL11.glVertex2f((float)(x + width), (float)y);
    }

    private void renderBitmap(int startChar, int stopChar) {
        BufferedImage[] fontImages = new BufferedImage[stopChar];
        int rowHeight = 0;
        int charX = 0;
        int charY = 0;
        for (int targetChar = startChar; targetChar < stopChar; ++targetChar) {
            BufferedImage fontImage = this.drawCharToImage((char)targetChar);
            CharLocation fontChar = new CharLocation(charX, charY, fontImage.getWidth(), fontImage.getHeight());
            if (fontChar.height > this.fontHeight) {
                this.fontHeight = fontChar.height;
            }
            if (fontChar.height > rowHeight) {
                rowHeight = fontChar.height;
            }
            if (this.charLocations.length <= targetChar) continue;
            this.charLocations[targetChar] = fontChar;
            fontImages[targetChar] = fontImage;
            if ((charX += fontChar.width) <= 2048) continue;
            if (charX > this.textureWidth) {
                this.textureWidth = charX;
            }
            charX = 0;
            charY += rowHeight;
            rowHeight = 0;
        }
        this.textureHeight = charY + rowHeight;
        BufferedImage bufferedImage = new BufferedImage(this.textureWidth, this.textureHeight, 2);
        Graphics2D graphics2D = (Graphics2D)bufferedImage.getGraphics();
        graphics2D.setFont(this.font);
        graphics2D.setColor(new Color(255, 255, 255, 0));
        graphics2D.fillRect(0, 0, this.textureWidth, this.textureHeight);
        graphics2D.setColor(Color.WHITE);
        for (int targetChar = startChar; targetChar < stopChar; ++targetChar) {
            if (fontImages[targetChar] == null || this.charLocations[targetChar] == null) continue;
            graphics2D.drawImage((Image)fontImages[targetChar], this.charLocations[targetChar].x, this.charLocations[targetChar].y, null);
        }
        this.textureID = TextureUtil.uploadTextureImageAllocate((int)TextureUtil.glGenTextures(), (BufferedImage)bufferedImage, (boolean)true, (boolean)true);
    }

    private BufferedImage drawCharToImage(char ch) {
        int charHeight;
        Graphics2D graphics2D = (Graphics2D)new BufferedImage(1, 1, 2).getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setFont(this.font);
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int charWidth = fontMetrics.charWidth(ch) + 8;
        if (charWidth <= 8) {
            charWidth = 7;
        }
        if ((charHeight = fontMetrics.getHeight() + 3) <= 0) {
            charHeight = this.font.getSize();
        }
        BufferedImage fontImage = new BufferedImage(charWidth, charHeight, 2);
        Graphics2D graphics = (Graphics2D)fontImage.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setFont(this.font);
        graphics.setColor(Color.WHITE);
        graphics.drawString(String.valueOf(ch), 3, 1 + fontMetrics.getAscent());
        return fontImage;
    }

    public int getStringWidth(String text) {
        int width = 0;
        for (int n : text.toCharArray()) {
            CharLocation fontChar;
            int index;
            int n2 = index = n < this.charLocations.length ? n : 3;
            if (this.charLocations.length <= index || (fontChar = this.charLocations[index]) == null) continue;
            width += fontChar.width - 8;
        }
        return width / 2;
    }

    public Font getFont() {
        return this.font;
    }

    private static class CharLocation {
        private final int x;
        private final int y;
        private final int width;
        private final int height;

        CharLocation(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
}

