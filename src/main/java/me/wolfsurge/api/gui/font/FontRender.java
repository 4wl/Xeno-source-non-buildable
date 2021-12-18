/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.util.ChatAllowedCharacters
 *  net.minecraft.util.ResourceLocation
 */
package me.wolfsurge.api.gui.font;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;
import me.wolfsurge.api.gui.font.ImageAWT;
import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.module.modules.client.CustomFont;
import me.xenodevs.xeno.utils.render.ColorUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;

public class FontRender
extends FontRenderer
implements Globals {
    private final ImageAWT defaultFont;

    public FontRender(Font font) {
        super(FontRender.mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), null, false);
        this.defaultFont = new ImageAWT(font);
        this.FONT_HEIGHT = this.getHeight();
    }

    public int getHeight() {
        return this.defaultFont.getHeight() / 2;
    }

    public int getSize() {
        return this.defaultFont.getFont().getSize();
    }

    public int drawStringWithShadow(String text, float x, float y, int color) {
        return this.drawString(text, x, y, color, true);
    }

    public int drawString(String text, float x, float y, int color, boolean dropShadow) {
        float currY = y - 3.0f;
        if (text.contains("\n")) {
            String[] parts = text.split("\n");
            float newY = 0.0f;
            for (String s : parts) {
                this.drawText(s, x, currY + newY, color, dropShadow);
                newY += (float)this.getHeight();
            }
            return 0;
        }
        if (dropShadow) {
            this.drawText(text, x + CustomFont.SHADOW_SPACING.getFloatValue(), currY + CustomFont.SHADOW_SPACING.getFloatValue(), new Color(0, 0, 0, 150).getRGB(), true);
        }
        return this.drawText(text, x, currY, color, false);
    }

    public void drawRainbowStringWithShadow(String text, float x2, float y2, int color, float speed, float saturation, int seperation) {
        int count = text.length() * seperation;
        int off = 0;
        String chars = "";
        for (char c : text.toCharArray()) {
            this.drawStringWithShadow(String.valueOf(c), x2 + (float)off, y2, ColorUtil.rainbowWave(speed, saturation, 1.0f, count));
            off = off + this.getStringWidth(String.valueOf(c)) + 1;
            count -= seperation;
        }
    }

    private int drawText(String text, float x, float y, int color, boolean ignoreColor) {
        if (text == null) {
            return 0;
        }
        if (text.isEmpty()) {
            return (int)x;
        }
        GlStateManager.translate((double)((double)x - 1.5), (double)((double)y + 0.5), (double)0.0);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GlStateManager.enableTexture2D();
        int currentColor = color;
        if ((currentColor & 0xFC000000) == 0) {
            currentColor |= 0xFF000000;
        }
        int alpha = currentColor >> 24 & 0xFF;
        if (text.contains("\u00a7")) {
            String[] parts = text.split("\u00a7");
            ImageAWT currentFont = this.defaultFont;
            double width = 0.0;
            boolean randomCase = false;
            for (int index = 0; index < parts.length; ++index) {
                String part = parts[index];
                if (part.isEmpty()) continue;
                if (index == 0) {
                    currentFont.drawString(part, width, 0.0, currentColor);
                    width += (double)currentFont.getStringWidth(part);
                    continue;
                }
                String words = part.substring(1);
                char type2 = part.charAt(0);
                int colorIndex = "0123456789abcdefklmnor".indexOf(type2);
                switch (colorIndex) {
                    case 0: 
                    case 1: 
                    case 2: 
                    case 3: 
                    case 4: 
                    case 5: 
                    case 6: 
                    case 7: 
                    case 8: 
                    case 9: 
                    case 10: 
                    case 11: 
                    case 12: 
                    case 13: 
                    case 14: 
                    case 15: {
                        if (!ignoreColor) {
                            currentColor = ColorUtils.hexColors[colorIndex] | alpha << 24;
                        }
                        randomCase = false;
                        break;
                    }
                    case 16: {
                        randomCase = true;
                        break;
                    }
                    case 18: {
                        break;
                    }
                    case 21: {
                        currentColor = color;
                        if ((currentColor & 0xFC000000) == 0) {
                            currentColor |= 0xFF000000;
                        }
                        randomCase = false;
                    }
                }
                currentFont = this.defaultFont;
                if (randomCase) {
                    currentFont.drawString(ColorUtils.randomMagicText(words), width, 0.0, currentColor);
                } else {
                    currentFont.drawString(words, width, 0.0, currentColor);
                }
                width += (double)currentFont.getStringWidth(words);
            }
        } else {
            this.defaultFont.drawString(text, 0.0, 0.0, currentColor);
        }
        GlStateManager.disableBlend();
        GlStateManager.translate((double)(-((double)x - 1.5)), (double)(-((double)y + 0.5)), (double)0.0);
        return (int)(x + (float)this.getStringWidth(text));
    }

    public int getColorCode(char charCode) {
        return ColorUtils.hexColors[FontRender.getColorIndex(charCode)];
    }

    public int getStringWidth(String text) {
        if (text.contains("\u00a7")) {
            String[] parts = text.split("\u00a7");
            ImageAWT currentFont = this.defaultFont;
            int width = 0;
            for (int index = 0; index < parts.length; ++index) {
                String part = parts[index];
                if (part.isEmpty()) continue;
                if (index == 0) {
                    width += currentFont.getStringWidth(part);
                    continue;
                }
                String words = part.substring(1);
                currentFont = this.defaultFont;
                width += currentFont.getStringWidth(words);
            }
            return width / 2;
        }
        return this.defaultFont.getStringWidth(text) / 2;
    }

    public int getCharWidth(char character) {
        return this.getStringWidth(String.valueOf(character));
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
    }

    protected void bindTexture(ResourceLocation location) {
    }

    public static int getColorIndex(char type2) {
        switch (type2) {
            case '0': 
            case '1': 
            case '2': 
            case '3': 
            case '4': 
            case '5': 
            case '6': 
            case '7': 
            case '8': 
            case '9': {
                return type2 - 48;
            }
            case 'a': 
            case 'b': 
            case 'c': 
            case 'd': 
            case 'e': 
            case 'f': {
                return type2 - 97 + 10;
            }
            case 'k': 
            case 'l': 
            case 'm': 
            case 'n': 
            case 'o': {
                return type2 - 107 + 16;
            }
            case 'r': {
                return 21;
            }
        }
        return -1;
    }

    private static class ColorUtils {
        public static int[] hexColors = new int[16];
        private static final Random random;
        private static final String magicAllowedCharacters = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0";

        private ColorUtils() {
        }

        public static String randomMagicText(String text) {
            StringBuilder stringBuilder = new StringBuilder();
            for (char ch : text.toCharArray()) {
                if (!ChatAllowedCharacters.isAllowedCharacter((char)ch)) continue;
                int index = random.nextInt(magicAllowedCharacters.length());
                stringBuilder.append(magicAllowedCharacters.charAt(index));
            }
            return stringBuilder.toString();
        }

        static {
            ColorUtils.hexColors[0] = 0;
            ColorUtils.hexColors[1] = 170;
            ColorUtils.hexColors[2] = 43520;
            ColorUtils.hexColors[3] = 43690;
            ColorUtils.hexColors[4] = 0xAA0000;
            ColorUtils.hexColors[5] = 0xAA00AA;
            ColorUtils.hexColors[6] = 0xFFAA00;
            ColorUtils.hexColors[7] = 0xAAAAAA;
            ColorUtils.hexColors[8] = 0x555555;
            ColorUtils.hexColors[9] = 0x5555FF;
            ColorUtils.hexColors[10] = 0x55FF55;
            ColorUtils.hexColors[11] = 0x55FFFF;
            ColorUtils.hexColors[12] = 0xFF5555;
            ColorUtils.hexColors[13] = 0xFF55FF;
            ColorUtils.hexColors[14] = 0xFFFF55;
            ColorUtils.hexColors[15] = 0xFFFFFF;
            random = new Random();
        }
    }
}

