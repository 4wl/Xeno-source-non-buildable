/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.text.TextFormatting
 *  org.lwjgl.input.Keyboard
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
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

public class PlainTheme
extends Theme {
    public PlainTheme() {
        super("Plain", false);
    }

    @Override
    public void drawBoolean(BooleanSetting op, Button parent, double offset, int mouseX, int mouseY, boolean hovered) {
        RenderUtils2D.drawRect(parent.parent.getX() - 2, (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, Integer.MIN_VALUE);
        if (op.isEnabled()) {
            RenderUtils2D.drawRect(parent.parent.getX(), (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, Colors.colourInt);
        }
        TextUtil.drawStringWithShadow(op.getName(), parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 1.0), -1);
    }

    @Override
    public void drawMode(ModeSetting op, Button parent, double offset, int mouseX, int mouseY, boolean hovered) {
        RenderUtils2D.drawRect(parent.parent.getX() - 2, (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, Integer.MIN_VALUE);
        TextUtil.drawStringWithShadow(op.getName() + ": " + op.getMode(), parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 1.0), -1);
    }

    @Override
    public void drawSlider(NumberSetting val, Button parent, double offset, int renderWidth, boolean hovered, int mouseX, int mouseY) {
        RenderUtils2D.drawRect(parent.parent.getX() - 2, (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, Integer.MIN_VALUE);
        RenderUtils2D.drawRect(parent.parent.getX() + 2, (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, 0);
        RenderUtils2D.drawRectWH((double)parent.parent.getX(), (double)parent.parent.getY() + offset, (double)renderWidth, (double)ClickGuiVariables.buttonBarHeight, hovered ? Colors.col.brighter().getRGB() : Colors.col.getRGB());
        TextUtil.drawStringWithShadow(val.getName() + ": " + val.getDoubleValue(), parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 1.0), -1);
    }

    @Override
    public void drawColourPicker(ColourComponent cb, ColourPicker op, double offset, boolean open, Button parent, int mouseX, int mouseY) {
        RenderUtils2D.drawRect(parent.parent.getX() - 2, (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, Integer.MIN_VALUE);
        if (open) {
            RenderUtils2D.drawRect(parent.parent.getX(), (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, parent.parent.getX() + parent.parent.getWidth(), (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight + (double)(ClickGuiVariables.buttonBarHeight * ClickGuiVariables.colourMultiplier), 0x70000000);
            cb.drawPicker(op, parent.parent.getX() + 4, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, parent.parent.getX() + parent.parent.getWidth() - 14, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, parent.parent.getX() + 4, (double)parent.parent.getY() + offset + (double)(ClickGuiVariables.buttonBarHeight * 5) - 1.0, mouseX, mouseY);
            TextUtil.drawCenteredString("Rainbow", parent.parent.getX() + parent.parent.getWidth() / 2, (float)((double)parent.parent.getY() + offset + (double)(ClickGuiVariables.buttonBarHeight * 6)), op.getRainbow() ? new Color(op.getColor().getRed(), op.getColor().getGreen(), op.getColor().getBlue(), 255).getRGB() : -1);
        }
        TextUtil.drawStringWithShadow(op.getName(), parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 1.0), -1);
    }

    @Override
    public void drawStringComponent(StringSetting ss, StringComponent.CurrentString cs, Button parent, boolean isListening, boolean hovered, double offset, int x, double y, int mouseX, int mouseY) {
        RenderUtils2D.drawRect(parent.parent.getX() - 2, (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, Integer.MIN_VALUE);
        if (isListening) {
            TextUtil.drawStringWithShadow(ss.getName() + " \u00a77" + cs.getString() + "_", parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 1.0), -1);
        } else {
            TextUtil.drawStringWithShadow(ss.getName() + " \u00a77" + ss.getText(), parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 1.0), -1);
        }
    }

    @Override
    public void drawKeybind(KeybindSetting op, double offset, Button parent, boolean binding, int mouseX, int mouseY) {
        RenderUtils2D.drawRect(parent.parent.getX() - 2, (double)parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, (double)parent.parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, Integer.MIN_VALUE);
        TextUtil.drawStringWithShadow(binding ? "Listening..." : op.name + ": " + Keyboard.getKeyName((int)op.getKeyCode()), parent.parent.getX() + 6, (float)((double)parent.parent.getY() + offset + 1.0), -1);
    }

    @Override
    public void drawFrame(ArrayList<Component> components, Category category, boolean open, int x, int y, int width, int mouseX, int mouseY) {
        int length = ClickGuiVariables.frameBarHeight + 3;
        boolean thing = false;
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
                    if (thing) continue;
                    --length;
                    thing = true;
                }
            }
        }
        RenderUtils2D.drawRectMC(x - 2, y, x + width + 2, y + ClickGuiVariables.frameBarHeight, new Colour(new Colour(Colors.colourInt), 254).getRGB());
        if (open && !components.isEmpty()) {
            for (Component component : components) {
                component.renderComponent(mouseX, mouseY);
            }
        }
        TextUtil.drawStringWithShadow((ClickGuiModule.underline.enabled ? TextFormatting.UNDERLINE : "") + category.name, x + 2, y + 1, -1);
    }

    @Override
    public void drawButton(Frame parent, Module mod, ArrayList<Component> subcomponents, boolean open, double offset, int mouseX, int mouseY, boolean hovered) {
        RenderUtils2D.drawRect(parent.getX() - 2, (double)parent.getY() + offset, parent.getX() + parent.getWidth() + 2, (double)parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, 0x70000000);
        TextUtil.drawStringWithShadow(mod.getName(), parent.getX() + 2, (float)((double)parent.getY() + offset + 1.0), mod.enabled ? Colors.colourInt : -1);
        if (open && !subcomponents.isEmpty()) {
            double off = offset + (double)ClickGuiVariables.buttonBarHeight;
            int aa = 0;
            for (Component comp : subcomponents) {
                aa += ClickGuiVariables.buttonBarHeight;
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
                    if (((ColourComponent)comp).open) {
                        RenderUtils2D.drawRectWH((double)(parent.getX() - 1), (double)parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight + (double)aa, 1.0, (double)(ClickGuiVariables.buttonBarHeight * ClickGuiVariables.colourMultiplier), Colors.colourInt);
                    }
                    off += (double)(ClickGuiVariables.buttonBarHeight + (((ColourComponent)comp).open ? ClickGuiVariables.buttonBarHeight * ClickGuiVariables.colourMultiplier : 0));
                    aa += ((ColourComponent)comp).open ? ClickGuiVariables.buttonBarHeight * (ClickGuiVariables.colourMultiplier - 1) : 0;
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
            RenderUtils2D.drawRect(parent.getX() - 2, (double)parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight, parent.getX() - 1, (double)parent.getY() + offset + (double)ClickGuiVariables.buttonBarHeight + (double)aa, Colors.colourInt);
        }
    }

    @Override
    public void drawDescription(String text, Timer timer, int mouseX, int mouseY) {
        RenderUtils2D.drawRoundedRect(mouseX + 5, mouseY - 15, TextUtil.getStringWidth(text) + 5, 15.0, ClickGuiVariables.frameRoundedRadius, -16777216);
        RenderUtils2D.drawRoundedOutline(mouseX + 5, mouseY - 15, TextUtil.getStringWidth(text) + 5, 15.0, ClickGuiVariables.frameRoundedRadius, 2.0, Colors.colourInt);
        TextUtil.drawStringWithShadow(text, mouseX + 7, mouseY - 13, -1);
    }
}

