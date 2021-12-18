/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ChatAllowedCharacters
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package me.xenodevs.xeno.gui.click.component.components.subcomponents;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.click.ClickGui;
import me.xenodevs.xeno.gui.click.ClickGuiVariables;
import me.xenodevs.xeno.gui.click.component.Component;
import me.xenodevs.xeno.gui.click.component.components.Button;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.settings.StringSetting;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class StringComponent
extends Component {
    private boolean hovered;
    private boolean isListening;
    private StringSetting op;
    private Button parent;
    public double offset;
    private int x;
    private double y;
    private CurrentString currentString = new CurrentString("");

    public StringComponent(StringSetting option, Button button, double offset) {
        this.op = option;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = (double)button.parent.getY() + button.offset;
        this.offset = offset;
    }

    @Override
    public void renderComponent(int mouseX, int mouseY) {
        GL11.glPushMatrix();
        ClickGui.theme.drawStringComponent(this.op, this.currentString, this.parent, this.isListening, this.isMouseOnButton(mouseX, mouseY), this.offset, this.x, this.y, mouseX, mouseY);
        GL11.glPopMatrix();
    }

    @Override
    public int getHeight() {
        return ClickGuiVariables.buttonBarHeight;
    }

    @Override
    public void setOff(double newOff) {
        this.offset = newOff;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = this.isMouseOnButton(mouseX, mouseY);
        this.y = (double)this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        if (this.isListening) {
            if (key == 1) {
                return;
            }
            if (key == 28) {
                this.enterString();
            } else if (key == 14) {
                this.setString(StringComponent.removeLastChar(this.currentString.getString()));
            } else if (key == 47 && (Keyboard.isKeyDown((int)157) || Keyboard.isKeyDown((int)29))) {
                try {
                    this.setString(this.currentString.getString() + Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ChatAllowedCharacters.isAllowedCharacter((char)typedChar)) {
                this.setString(this.currentString.getString() + typedChar);
            }
            if (key == 28) {
                this.isListening = false;
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            boolean bl = this.isListening = !this.isListening;
            if (ClickGuiModule.clickSound.enabled) {
                GuiUtil.clickSound();
            }
        }
    }

    private void enterString() {
        if (this.currentString.getString().isEmpty()) {
            this.op.setText(this.op.getDefaultValue());
        } else {
            this.op.setText(this.currentString.getString());
        }
        this.setString("");
    }

    public void setString(String newString) {
        this.currentString = new CurrentString(newString);
    }

    public static String removeLastChar(String str) {
        String output = "";
        if (str != null && str.length() > 0) {
            output = str.substring(0, str.length() - 1);
        }
        return output;
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + ClickGuiVariables.frameWidth && (double)y > this.y && (double)y < this.y + (double)ClickGuiVariables.buttonBarHeight;
    }

    public static class CurrentString {
        private final String string;

        public CurrentString(String string) {
            this.string = string;
        }

        public String getString() {
            return this.string;
        }
    }
}

