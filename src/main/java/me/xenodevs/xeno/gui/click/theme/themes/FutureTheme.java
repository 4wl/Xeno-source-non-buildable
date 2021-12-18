/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.text.TextFormatting
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.gui.click.theme.themes;

import java.awt.Color;
import java.util.ArrayList;
import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.gui.click.ClickGuiVariables;
import me.xenodevs.xeno.gui.click.component.Component;
import me.xenodevs.xeno.gui.click.component.Frame;
import me.xenodevs.xeno.gui.click.component.components.Button;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.BooleanComponent;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.ColourComponent;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.KeybindComponent;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.ModeComponent;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.SliderComponent;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.StringComponent;
import me.xenodevs.xeno.gui.click.theme.Theme;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.KeybindSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.module.settings.StringSetting;
import me.xenodevs.xeno.utils.other.Timer;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class FutureTheme
extends Theme {
    Timer timer = new Timer();

    public FutureTheme() {
        super("Future", false);
    }

    @Override
    public void drawBoolean(BooleanSetting op, Button parent, double offset, int mouseX, int mouseY, boolean hovered) {
        RenderUtils2D.drawRect(parent.parent.getX() - 2, (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, 0x70000000);
        if (op.isEnabled()) {
            RenderUtils2D.drawRect(parent.parent.getX(), (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, new Colour(new Colour(Colors.colourInt), hovered ? 100 : 150).getRGB());
        }
        if (hovered && !op.isEnabled()) {
            RenderUtils2D.drawRect(parent.parent.getX(), (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, new Colour(new Colour(Color.GRAY), 100).getRGB());
        }
        TextUtil.drawStringWithShadow(op.getName(), parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 2.0), -1);
    }

    @Override
    public void drawMode(ModeSetting op, Button parent, double offset, int mouseX, int mouseY, boolean hovered) {
        RenderUtils2D.drawRect(parent.parent.getX() - 2, (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, 0x70000000);
        RenderUtils2D.drawRect(parent.parent.getX(), (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, new Colour(new Colour(Colors.colourInt), hovered ? 100 : 150).getRGB());
        TextUtil.drawStringWithShadow(op.getName(), parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 2.0), -1);
        TextUtil.drawStringWithShadow(op.getMode(), parent.parent.getX() + 6 + TextUtil.getStringWidth(op.getName() + " "), (float)((double)parent.parent.getY() + offset + 2.0), Color.GRAY.brighter().getRGB());
    }

    @Override
    public void drawSlider(NumberSetting val, Button parent, double offset, int renderWidth, boolean hovered, int mouseX, int mouseY) {
        RenderUtils2D.drawRect(parent.parent.getX() - 2, (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, 0x70000000);
        if (hovered) {
            RenderUtils2D.drawRect(parent.parent.getX() + renderWidth, (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, new Colour(new Colour(Color.GRAY), 100).getRGB());
        }
        RenderUtils2D.drawRectWH((double)parent.parent.getX(), (double)parent.parent.getY() + offset, (double)renderWidth, (double)ClickGuiVariables.buttonBarHeight, new Colour(new Colour(Colors.colourInt), hovered ? 100 : 150).getRGB());
        TextUtil.drawStringWithShadow(val.getName() + ": " + val.getDoubleValue(), parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 2.0), -1);
    }

    @Override
    public void drawColourPicker(ColourComponent cb, ColourPicker op, double offset, boolean open, Button parent, int mouseX, int mouseY) {
        RenderUtils2D.drawRect(parent.parent.getX() - 2, (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, 0x70000000);
        RenderUtils2D.drawRect(parent.parent.getX(), (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, new Color(230, 230, 230, 30).getRGB());
        TextUtil.drawStringWithShadow(op.getName(), parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 2.0), -1);
        if (open) {
            RenderUtils2D.drawRect(parent.parent.getX() - 2, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, parent.parent.getX() + parent.parent.getWidth() + 2, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight + (double)(ClickGuiVariables.buttonBarHeight * ClickGuiVariables.colourMultiplier), 0x70000000);
            cb.drawPicker(op, parent.parent.getX() + 4, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight + 5.0, parent.parent.getX() + parent.parent.getWidth() - 14, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight + 5.0, parent.parent.getX() + 4, (double)parent.parent.getY() + offset + (double)(ClickGuiVariables.buttonBarHeight * 5), mouseX, mouseY);
            TextUtil.drawStringWithShadow("Rainbow", parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 90.0), op.getRainbow() ? new Color(op.getColor().getRed(), op.getColor().getGreen(), op.getColor().getBlue(), 255).getRGB() : -1);
        }
        int size = 13;
        if (!open) {
            GL11.glPushMatrix();
            GL11.glColor3f((float)1.0f, (float)1.0f, (float)1.0f);
            ResourceLocation down = new ResourceLocation("xeno", "textures/future_gear.png");
            Minecraft.getMinecraft().getTextureManager().bindTexture(down);
            RenderUtils2D.drawModalRectWithCustomSizedTexture(parent.parent.getX() + parent.parent.getWidth() - 12, (double)parent.parent.getY() + offset + 2.0, 0.0f, 0.0f, 10.0, 10.0, 10.0f, 10.0f);
            GL11.glPopMatrix();
        } else {
            GL11.glPushMatrix();
            GL11.glColor3f((float)1.0f, (float)1.0f, (float)1.0f);
            ResourceLocation down = new ResourceLocation("xeno", "textures/future_gear_rotated.png");
            Minecraft.getMinecraft().getTextureManager().bindTexture(down);
            RenderUtils2D.drawModalRectWithCustomSizedTexture(parent.parent.getX() + parent.parent.getWidth() - 12, (double)parent.parent.getY() + offset + 2.0, 0.0f, 0.0f, 10.0, 10.0, 10.0f, 10.0f);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void drawStringComponent(StringSetting ss, StringComponent.CurrentString cs, Button parent, boolean isListening, boolean hovered, double offset, int x, double y, int mouseX, int mouseY) {
        RenderUtils2D.drawRect(parent.parent.getX() - 2, (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, 0x70000000);
        if (hovered && !isListening) {
            RenderUtils2D.drawRect(parent.parent.getX(), (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, new Colour(new Colour(Color.GRAY), 100).getRGB());
        }
        if (isListening) {
            RenderUtils2D.drawRect(parent.parent.getX(), (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, new Colour(new Colour(Colors.colourInt), hovered ? 100 : 150).getRGB());
            TextUtil.drawStringWithShadow(ss.getName() + " \u00a77" + cs.getString() + "_", parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 2.0), -1);
        } else {
            TextUtil.drawStringWithShadow(ss.getName() + " \u00a77" + ss.getText(), parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 2.0), -1);
        }
    }

    @Override
    public void drawKeybind(KeybindSetting op, double offset, Button parent, boolean binding, int mouseX, int mouseY) {
        RenderUtils2D.drawRect(parent.parent.getX() - 2, (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, 0x70000000);
        if (binding) {
            RenderUtils2D.drawRect(parent.parent.getX(), (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, new Colour(new Colour(Colors.colourInt), 150).getRGB());
        }
        TextUtil.drawStringWithShadow(binding ? "Press a key" : op.name + (Object)TextFormatting.GRAY + " " + Keyboard.getKeyName((int)op.getKeyCode()), parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 2.0), -1);
    }

    @Override
    public void drawFrame(ArrayList<Component> components, Category category, boolean open, int x, int y, int width, int mouseX, int mouseY) {
        ClickGuiVariables.frameBarHeight = 13;
        ClickGuiVariables.buttonBarHeight = 15;
        int length = ClickGuiVariables.frameBarHeight + 3;
        boolean thing = false;
        int count = 0;
        if (open && !components.isEmpty()) {
            for (Component component : components) {
                length += ClickGuiVariables.buttonBarHeight;
                if (!(component instanceof Button)) continue;
                Button button = (Button)component;
                if (!button.open) continue;
                for (Component c : button.subcomponents) {
                    int extra = 0;
                    if (c instanceof ColourComponent && ((ColourComponent)c).open) {
                        extra = ClickGuiVariables.buttonBarHeight * ClickGuiVariables.colourMultiplier;
                    }
                    length += ClickGuiVariables.buttonBarHeight + extra;
                    if (!thing) {
                        --length;
                        thing = true;
                    }
                    ++count;
                }
            }
        }
        RenderUtils2D.drawRectMC(x - 2, y, x + width + 2, y + ClickGuiVariables.frameBarHeight, new Colour(new Colour(Colors.colourInt), 170).getRGB());
        int size = 13;
        if (open) {
            ResourceLocation up = new ResourceLocation("xeno", "textures/future_arrow.png");
            Minecraft.getMinecraft().getTextureManager().bindTexture(up);
            Gui.drawModalRectWithCustomSizedTexture((int)(x + width - 15), (int)y, (float)0.0f, (float)0.0f, (int)size, (int)size, (float)size, (float)size);
        } else {
            ResourceLocation down = new ResourceLocation("xeno", "textures/future_arrow_down.png");
            Minecraft.getMinecraft().getTextureManager().bindTexture(down);
            Gui.drawModalRectWithCustomSizedTexture((int)(x + width - 15), (int)y, (float)0.0f, (float)0.0f, (int)size, (int)size, (float)size, (float)size);
        }
        if (open && !components.isEmpty()) {
            for (Component component : components) {
                component.renderComponent(mouseX, mouseY);
            }
        }
        FontManager.drawStringWithShadow(category.name, x + 2, y + 3, -1);
        int ex = 0;
        boolean a = false;
        for (Component c : components) {
            if (!(c instanceof Button) || !((Button)c).open) continue;
            if (!a) {
                ++ex;
            }
            for (Component co : ((Button)c).subcomponents) {
                ex += co.getHeight();
            }
        }
        if (open) {
            Gui.drawRect((int)(x - 2), (int)(y + ClickGuiVariables.frameBarHeight + components.size() * ClickGuiVariables.buttonBarHeight + ex), (int)(x + width + 2), (int)(y + ClickGuiVariables.frameBarHeight + components.size() * ClickGuiVariables.buttonBarHeight + ex + 1), (int)0x70000000);
        } else {
            Gui.drawRect((int)(x - 2), (int)(y + ClickGuiVariables.frameBarHeight), (int)(x + width + 2), (int)(y + ClickGuiVariables.frameBarHeight + 3), (int)0x70000000);
        }
    }

    @Override
    public void drawButton(Frame parent, Module mod, ArrayList<Component> subcomponents, boolean open, double offset, int mouseX, int mouseY, boolean hovered) {
        ClickGuiVariables.buttonBarHeight = 15;
        RenderUtils2D.drawRect(parent.getX() - 2, (double)parent.getY() + offset, parent.getX() + parent.getWidth() + 2, (double)parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, 0x70000000);
        RenderUtils2D.drawRect(parent.getX() - 1, (double)parent.getY() + offset + 1.0, parent.getX() + parent.getWidth() + 1, (double)parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, mod.enabled ? new Colour(new Colour(Colors.colourInt), hovered ? 100 : 150).getRGB() : new Color(230, 230, 230, hovered ? 26 : 30).getRGB());
        TextUtil.drawStringWithShadow(mod.getName(), parent.getX() + 2, (float)((double)parent.getY() + offset + 2.5), -1);
        if (subcomponents.size() > 2) {
            ResourceLocation down;
            if (!open) {
                GL11.glPushMatrix();
                GL11.glColor3f((float)1.0f, (float)1.0f, (float)1.0f);
                down = new ResourceLocation("xeno", "textures/future_gear.png");
                Minecraft.getMinecraft().getTextureManager().bindTexture(down);
                RenderUtils2D.drawModalRectWithCustomSizedTexture(parent.getX() + parent.getWidth() - 10, (double)parent.getY() + offset + 3.0, 0.0f, 0.0f, 10.0, 10.0, 10.0f, 10.0f);
                GL11.glPopMatrix();
            } else {
                GL11.glPushMatrix();
                GL11.glColor3f((float)1.0f, (float)1.0f, (float)1.0f);
                down = new ResourceLocation("xeno", "textures/future_gear_rotated.png");
                Minecraft.getMinecraft().getTextureManager().bindTexture(down);
                RenderUtils2D.drawModalRectWithCustomSizedTexture(parent.getX() + parent.getWidth() - 10, (double)parent.getY() + offset + 3.0, 0.0f, 0.0f, 10.0, 10.0, 10.0f, 10.0f);
                GL11.glPopMatrix();
            }
        }
        if (open && !subcomponents.isEmpty()) {
            double off = offset + (double)ClickGuiVariables.buttonBarHeight + 1.0;
            if (open) {
                RenderUtils2D.drawRect(parent.getX() - 2, (double)parent.getY() + off - 1.0, parent.getX() + parent.getWidth() + 2, (double)parent.getY() + off, 0x70000000);
            }
            for (Component comp : subcomponents) {
                if (comp instanceof BooleanComponent) {
                    comp.setOff(off);
                    off += (double)ClickGuiVariables.buttonBarHeight;
                }
                if (comp instanceof SliderComponent) {
                    comp.setOff(off);
                    off += (double)ClickGuiVariables.buttonBarHeight;
                }
                if (comp instanceof ModeComponent) {
                    comp.setOff(off);
                    off += (double)ClickGuiVariables.buttonBarHeight;
                }
                if (comp instanceof ColourComponent) {
                    comp.setOff(off);
                    off += (double)(ClickGuiVariables.buttonBarHeight + (((ColourComponent)comp).open ? ClickGuiVariables.buttonBarHeight * ClickGuiVariables.colourMultiplier : 0));
                }
                if (comp instanceof StringComponent) {
                    comp.setOff(off);
                    off += (double)ClickGuiVariables.buttonBarHeight;
                }
                if (comp instanceof KeybindComponent) {
                    comp.setOff(off);
                    off += (double)ClickGuiVariables.buttonBarHeight;
                }
                comp.renderComponent(mouseX, mouseY);
            }
        }
    }

    @Override
    public void drawDescription(String text, Timer timer, int mouseX, int mouseY) {
        RenderUtils2D.drawRoundedRect(mouseX + 5, mouseY - 14, TextUtil.getStringWidth(text) + 5, 12.0, ClickGuiVariables.frameRoundedRadius, -16777216);
        RenderUtils2D.drawRoundedOutline(mouseX + 5, mouseY - 14, TextUtil.getStringWidth(text) + 5, 12.0, ClickGuiVariables.frameRoundedRadius, 2.0, Colors.colourInt);
        TextUtil.drawStringWithShadow(text, mouseX + 7, mouseY - 14, -1);
    }
}

