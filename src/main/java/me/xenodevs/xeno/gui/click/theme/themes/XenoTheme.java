/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.TextFormatting
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.gui.click.theme.themes;

import java.awt.Color;
import java.util.ArrayList;
import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.click.ClickGui;
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
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.KeybindSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.module.settings.StringSetting;
import me.xenodevs.xeno.utils.other.Timer;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class XenoTheme
extends Theme {
    public XenoTheme() {
        super("Xeno", true);
    }

    @Override
    public void drawBoolean(BooleanSetting op, Button parent, double offset, int mouseX, int mouseY, boolean hovered) {
        TextUtil.drawClickGuiString(op.getName(), parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 2.0), parent.parent.getX() + parent.parent.getWidth() / 2, parent.parent.getX() + parent.parent.getWidth(), op.enabled ? ClickGui.color : -1);
    }

    @Override
    public void drawMode(ModeSetting op, Button parent, double offset, int mouseX, int mouseY, boolean hovered) {
        TextUtil.drawClickGuiString(op.getName() + ": " + op.getMode(), parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 2.0), parent.parent.getX() + parent.parent.getWidth() / 2, parent.parent.getX() + parent.parent.getWidth(), -1);
    }

    @Override
    public void drawSlider(NumberSetting val, Button parent, double offset, int renderWidth, boolean hovered, int mouseX, int mouseY) {
        RenderUtils2D.drawRect(parent.parent.getX() + 2, (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, 0);
        RenderUtils2D.drawRect(parent.parent.getX(), (double)parent.parent.getY() + offset + 1.0, parent.parent.getX() + renderWidth, (double)(parent.parent.getY() + 1) + offset + (double)ClickGuiVariables.buttonBarHeight, hovered ? Colors.col.brighter().getRGB() : Colors.col.getRGB());
        GL11.glPushMatrix();
        TextUtil.drawClickGuiString(val.getName() + ": " + val.getDoubleValue(), parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 2.0), parent.parent.getX() + parent.parent.getWidth() / 2, parent.parent.getX() + parent.parent.getWidth(), -1);
        GL11.glPopMatrix();
    }

    @Override
    public void drawColourPicker(ColourComponent cb, ColourPicker op, double offset, boolean open, Button parent, int mouseX, int mouseY) {
        if (open) {
            RenderUtils2D.drawRoundedOutline(parent.parent.getX() + 2, (double)parent.parent.getY() + offset + 2.0, ClickGuiVariables.frameWidth - 4, 89.0, ClickGuiVariables.frameRoundedRadius, 2.0, ClickGui.color);
            cb.drawPicker(op, parent.parent.getX() + 4, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, parent.parent.getX() + parent.parent.getWidth() - 14, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, parent.parent.getX() + 5, (double)parent.parent.getY() + offset + (double)(ClickGuiVariables.buttonBarHeight * 5) - 2.0, mouseX, mouseY);
            TextUtil.drawCenteredString("Rainbow", parent.parent.getX() + parent.parent.getWidth() / 2, (float)((double)parent.parent.getY() + offset + (double)(ClickGuiVariables.buttonBarHeight * 6) - 1.0), op.getRainbow() ? new Color(op.getColor().getRed(), op.getColor().getGreen(), op.getColor().getBlue(), 255).getRGB() : -1);
        }
        TextUtil.drawClickGuiString(op.getName(), parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 2.0), parent.parent.getX() + parent.parent.getWidth() / 2, parent.parent.getX() + parent.parent.getWidth(), new Color(op.getColor().getRed(), op.getColor().getGreen(), op.getColor().getBlue(), 255).getRGB());
    }

    @Override
    public void drawStringComponent(StringSetting ss, StringComponent.CurrentString cs, Button parent, boolean isListening, boolean hovered, double offset, int x, double y, int mouseX, int mouseY) {
        if (isListening) {
            TextUtil.drawStringWithShadow(ss.getName() + " \u00a77" + cs.getString() + "_", parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 2.0), -1);
        } else {
            TextUtil.drawStringWithShadow(ss.getName() + " \u00a77" + ss.getText(), parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 2.0), -1);
        }
    }

    @Override
    public void drawKeybind(KeybindSetting op, double offset, Button parent, boolean binding, int mouseX, int mouseY) {
        TextUtil.drawClickGuiString(binding ? "Press a key..." : op.name + ": " + Keyboard.getKeyName((int)op.getKeyCode()), parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 1.0), parent.parent.getX() + parent.parent.getWidth() / 2, parent.parent.getX() + parent.parent.getWidth(), -1);
    }

    @Override
    public void drawFrame(ArrayList<Component> components, Category category, boolean open, int x, int y, int width, int mouseX, int mouseY) {
        ClickGuiVariables.buttonBarHeight = 13;
        int length = ClickGuiVariables.frameBarHeight + 3 + (open ? ClickGui.maxLength + ClickGuiVariables.buttonBarHeight / 2 : 0);
        RenderUtils2D.drawRoundedRect(x - 2, y, ClickGuiVariables.frameWidth + 4, length, ClickGuiVariables.frameRoundedRadius, open ? ClickGuiVariables.frameRoundedRadius : 1, open ? ClickGuiVariables.frameRoundedRadius : 1, ClickGuiVariables.frameRoundedRadius, new Color(0, 0, 0, 100).getRGB());
        if (ClickGuiModule.blurFrame.isEnabled()) {
            Xeno.blurManager.blur(x - 2, y - 1, width + 4, length + 1, (int)ClickGuiModule.frameBlurIntensity.getDoubleValue());
        }
        RenderUtils2D.drawRoundedOutline(x - 2, y, ClickGuiVariables.frameWidth + 4, length, ClickGuiVariables.frameRoundedRadius, open ? ClickGuiVariables.frameRoundedRadius : 1, open ? ClickGuiVariables.frameRoundedRadius : 1, ClickGuiVariables.frameRoundedRadius, 2.0, ClickGui.color);
        RenderUtils2D.drawRectMC(x - 2, y + ClickGuiVariables.frameBarHeight + ClickGui.maxLength + 2, x + ClickGuiVariables.frameWidth + 2, y + ClickGuiVariables.frameBarHeight + ClickGui.maxLength + 3, 0);
        if (open) {
            RenderUtils2D.drawRect(x - 2, (double)(y + ClickGuiVariables.frameBarHeight) + 2.5, x + ClickGuiVariables.frameWidth + 2, (double)(y + ClickGuiVariables.frameBarHeight) + 3.5, Colors.colourInt);
        }
        if (open) {
            boolean doGrad = false;
            boolean doBottomGrad = false;
            if (!components.isEmpty()) {
                GL11.glPushMatrix();
                RenderUtils2D.drawRectMC(x - 2, y + ClickGuiVariables.frameBarHeight + ClickGui.maxLength + 2, x + ClickGuiVariables.frameWidth + 2, y + ClickGuiVariables.frameBarHeight + ClickGui.maxLength + 3, 0);
                RenderUtils2D.drawRectMC(x - 2, y + ClickGuiVariables.frameBarHeight + ClickGui.maxLength + 2, x + ClickGuiVariables.frameWidth + 2, y + ClickGuiVariables.frameBarHeight + ClickGui.maxLength + 3, Colors.colourInt);
                GL11.glPopMatrix();
                GL11.glPushAttrib((int)524288);
                RenderUtils2D.scissor(x - 2, y + ClickGuiVariables.frameBarHeight + 4, x + ClickGuiVariables.frameWidth + 2, y + ClickGuiVariables.frameBarHeight + ClickGui.maxLength + 2);
                GL11.glEnable((int)3089);
                for (Component component : components) {
                    component.renderComponent(mouseX, mouseY);
                    if (!(component instanceof Button)) continue;
                    int modLength = ClickGuiVariables.buttonBarHeight - 2;
                    Button button = (Button)component;
                    if (button.open) {
                        for (Component c : button.subcomponents) {
                            int extra = 0;
                            if (c instanceof ColourComponent && ((ColourComponent)c).open) {
                                extra = ClickGuiVariables.buttonBarHeight * ClickGuiVariables.colourMultiplier;
                            }
                            modLength += ClickGuiVariables.buttonBarHeight + extra;
                        }
                    }
                    if (ClickGuiModule.closedButtonOutline.enabled) {
                        RenderUtils2D.drawRoundedOutline(x, (double)y + button.offset + 2.5, ClickGuiVariables.frameWidth, button.open ? (double)modLength + 0.5 : (double)ClickGuiVariables.buttonBarHeight - 1.5, ClickGuiVariables.frameRoundedRadius, 2.0, button.open ? ClickGui.color : new Color(ClickGui.color).darker().getRGB());
                    } else if (button.open) {
                        RenderUtils2D.drawRoundedOutline(x, (double)y + button.offset + 2.5, ClickGuiVariables.frameWidth, (double)modLength + 0.5, ClickGuiVariables.frameRoundedRadius, 2.0, ClickGui.color);
                    }
                    if (button.offset < 10.0 && !doGrad) {
                        doGrad = true;
                    }
                    KeybindComponent finalSetting = (KeybindComponent)button.getSubcomponents().get(button.getSubcomponents().size() - 1);
                    if (!(button.offset > (double)ClickGui.maxLength && !doBottomGrad) && (!button.open || !(finalSetting.offset > (double)ClickGui.maxLength) || doBottomGrad)) continue;
                    doBottomGrad = true;
                }
                if (this.isScrollable) {
                    GL11.glDisable((int)3089);
                    GL11.glPopAttrib();
                }
            }
            if (doGrad) {
                RenderUtils2D.drawGradientRect(x - 1, y + ClickGuiVariables.frameBarHeight + 4, x + width + 1, y + ClickGuiVariables.frameBarHeight + 4 + 10, -1879048192, 0, false);
            }
            if (doBottomGrad) {
                RenderUtils2D.drawGradientRect(x - 1, y + ClickGuiVariables.frameBarHeight + 2 + ClickGui.maxLength - ClickGuiVariables.buttonBarHeight, x + width + 1, y + ClickGuiVariables.frameBarHeight + 2 + ClickGui.maxLength, 0, -1879048192, false);
            }
        }
        if (Xeno.moduleManager.getModule("CustomFont").isEnabled()) {
            if (ClickGuiModule.textPos.is("Center")) {
                FontManager.comfortaa.drawStringWithShadow((ClickGuiModule.underline.enabled ? TextFormatting.UNDERLINE : "") + category.name, x + width / 2 - FontManager.comfortaa.getStringWidth((ClickGuiModule.underline.enabled ? TextFormatting.UNDERLINE : "") + category.name) / 2, y + 4, -1);
            }
            if (ClickGuiModule.textPos.is("Left")) {
                FontManager.comfortaa.drawStringWithShadow((ClickGuiModule.underline.enabled ? TextFormatting.UNDERLINE : "") + category.name, x + 2, y + 4, -1);
            }
            if (ClickGuiModule.textPos.is("Right")) {
                FontManager.comfortaa.drawStringWithShadow((ClickGuiModule.underline.enabled ? TextFormatting.UNDERLINE : "") + category.name, x + width - FontManager.comfortaa.getStringWidth((ClickGuiModule.underline.enabled ? TextFormatting.UNDERLINE : "") + category.name) - 2, y + 4, -1);
            }
        } else {
            TextUtil.drawClickGuiString((ClickGuiModule.underline.enabled ? TextFormatting.UNDERLINE : "") + category.name, x + 2, y + 3, x + width / 2, x + width, -1);
        }
    }

    @Override
    public void drawButton(Frame parent, Module mod, ArrayList<Component> subcomponents, boolean open, double offset, int mouseX, int mouseY, boolean hovered) {
        TextUtil.drawClickGuiString(mod.getName(), parent.getX() + 2, (float)((double)parent.getY() + offset + (double)(Xeno.moduleManager.isModuleEnabled("CustomFont") ? 2.5f : 3.0f)), parent.getX() + parent.getWidth() / 2, parent.getX() + parent.getWidth(), mod.isEnabled() ? ClickGui.color : -1);
        if (open && !subcomponents.isEmpty()) {
            double off = offset + (double)ClickGuiVariables.buttonBarHeight;
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
        Xeno.blurManager.blur(mouseX + 5, mouseY - 15, TextUtil.getStringWidth(text) + 5, 15.0, 3);
        TextUtil.drawStringWithShadow(text, mouseX + 7, mouseY - 13, -1);
    }
}

