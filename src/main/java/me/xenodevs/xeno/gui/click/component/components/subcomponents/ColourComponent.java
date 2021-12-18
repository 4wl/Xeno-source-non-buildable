/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.xenodevs.xeno.gui.click.component.components.subcomponents;

import java.awt.Color;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.click.ClickGui;
import me.xenodevs.xeno.gui.click.ClickGuiVariables;
import me.xenodevs.xeno.gui.click.component.Component;
import me.xenodevs.xeno.gui.click.component.components.Button;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import org.lwjgl.input.Mouse;

public class ColourComponent
extends Component {
    public boolean open;
    private boolean hovered;
    private ColourPicker op;
    private Button parent;
    public double offset;
    private int x;
    private double y;
    private Color finalColor;
    boolean pickingColor = false;
    boolean pickingHue = false;
    boolean pickingAlpha = false;

    public ColourComponent(ColourPicker option, Button button, double offset) {
        this.op = option;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = (double)button.parent.getY() + button.offset;
        this.offset = offset;
    }

    @Override
    public void renderComponent(int mouseX, int mouseY) {
        ClickGuiVariables.colourMultiplier = 6;
        ClickGui.theme.drawColourPicker(this, this.op, this.offset, this.open, this.parent, mouseX, mouseY);
    }

    @Override
    public void setOff(double newOff) {
        this.offset = newOff;
    }

    @Override
    public int getHeight() {
        return this.open ? ClickGuiVariables.buttonBarHeight * 7 : ClickGuiVariables.buttonBarHeight;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = this.isMouseOnButton(mouseX, mouseY);
        this.y = (double)this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 1 && this.parent.open) {
            for (Component comp : this.parent.parent.getComponents()) {
                if (!(comp instanceof Button) || !((Button)comp).isOpen()) continue;
                for (Component comp2 : ((Button)comp).getSubcomponents()) {
                    if (!(comp2 instanceof ColourComponent) || !((ColourComponent)comp2).open || comp2 == this) continue;
                    this.parent.parent.refresh();
                }
            }
            this.open = !this.open;
            this.parent.parent.refresh();
            if (ClickGuiModule.clickSound.enabled) {
                GuiUtil.clickSound();
            }
        }
        this.doRainbow(mouseX, mouseY, button);
    }

    public void drawPicker(ColourPicker subColor, double pickerX, double pickerY, double hueSliderX, double hueSliderY, double alphaSliderX, double alphaSliderY, int mouseX, int mouseY) {
        float restrictedX;
        float[] color = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        try {
            color = new float[]{Color.RGBtoHSB(subColor.getColor().getRed(), subColor.getColor().getGreen(), subColor.getColor().getBlue(), null)[0], Color.RGBtoHSB(subColor.getColor().getRed(), subColor.getColor().getGreen(), subColor.getColor().getBlue(), null)[1], Color.RGBtoHSB(subColor.getColor().getRed(), subColor.getColor().getGreen(), subColor.getColor().getBlue(), null)[2], (float)subColor.getColor().getAlpha() / 255.0f};
        }
        catch (Exception exception) {
            // empty catch block
        }
        float pickerWidth = 72.0f;
        float pickerHeight = 50.0f;
        float hueSliderWidth = 11.0f;
        float hueSliderHeight = 57.0f;
        float alphaSliderHeight = 11.0f;
        float alphaSliderWidth = 66.0f;
        if (!(this.pickingColor || this.pickingHue || this.pickingAlpha)) {
            if (Mouse.isButtonDown((int)0) && this.mouseOver(pickerX, pickerY, pickerX + (double)pickerWidth, pickerY + (double)pickerHeight, mouseX, mouseY)) {
                this.pickingColor = true;
            } else if (Mouse.isButtonDown((int)0) && this.mouseOver(hueSliderX, hueSliderY, hueSliderX + (double)hueSliderWidth, hueSliderY + (double)hueSliderHeight, mouseX, mouseY)) {
                this.pickingHue = true;
            } else if (Mouse.isButtonDown((int)0) && this.mouseOver(alphaSliderX, alphaSliderY, alphaSliderX + (double)alphaSliderWidth, alphaSliderY + (double)alphaSliderHeight, mouseX, mouseY)) {
                this.pickingAlpha = true;
            }
        }
        if (this.pickingHue) {
            float restrictedY = (float)Math.min(Math.max(hueSliderY, (double)mouseY), hueSliderY + (double)hueSliderHeight);
            color[0] = (restrictedY - (float)hueSliderY) / hueSliderHeight;
            color[0] = Math.min(0.97f, color[0]);
        }
        if (this.pickingAlpha) {
            restrictedX = (float)Math.min(Math.max(alphaSliderX, (double)mouseX), alphaSliderX + (double)alphaSliderWidth);
            color[3] = 1.0f - (restrictedX - (float)alphaSliderX) / alphaSliderWidth;
        }
        if (this.pickingColor) {
            restrictedX = (float)Math.min(Math.max(pickerX, (double)mouseX), pickerX + (double)pickerWidth);
            float restrictedY = (float)Math.min(Math.max(pickerY, (double)mouseY), pickerY + (double)pickerHeight);
            color[1] = (restrictedX - (float)pickerX) / pickerWidth;
            color[2] = 1.0f - (restrictedY - (float)pickerY) / pickerHeight;
            color[2] = (float)Math.max(0.04000002, (double)color[2]);
            color[1] = (float)Math.max(0.022222223, (double)color[1]);
        }
        int selectedColor = Color.HSBtoRGB(color[0], 1.0f, 1.0f);
        float selectedRed = (float)(selectedColor >> 16 & 0xFF) / 255.0f;
        float selectedGreen = (float)(selectedColor >> 8 & 0xFF) / 255.0f;
        float selectedBlue = (float)(selectedColor & 0xFF) / 255.0f;
        RenderUtils2D.drawPickerBase(pickerX, pickerY, pickerWidth, pickerHeight, selectedRed, selectedGreen, selectedBlue, 255.0f);
        this.drawHueSlider(hueSliderX, hueSliderY, hueSliderWidth, hueSliderHeight, color[0]);
        int cursorX = (int)(pickerX + (double)(color[1] * pickerWidth));
        int cursorY = (int)(pickerY + (double)pickerHeight - (double)(color[2] * pickerHeight));
        RenderUtils2D.drawRectMC(cursorX - 2, cursorY - 2, cursorX + 2, cursorY + 2, Colors.colourInt);
        this.finalColor = ColourComponent.alphaIntegrate(new Color(Color.HSBtoRGB(color[0], color[1], color[2])), color[3]);
        this.drawAlphaSlider(alphaSliderX, alphaSliderY, alphaSliderWidth, alphaSliderHeight, (float)this.finalColor.getRed() / 255.0f, (float)this.finalColor.getGreen() / 255.0f, (float)this.finalColor.getBlue() / 255.0f, color[3]);
        this.op.setValue(new Colour(this.finalColor));
    }

    public static Color alphaIntegrate(Color color, float alpha) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        return new Color(red, green, blue, alpha);
    }

    public void drawHueSlider(double x, double y, double width, double height, float hue) {
        int step = 0;
        if (height > width) {
            RenderUtils2D.drawRect(x, y, x + width, y + 4.0, -65536);
            y += 4.0;
            for (int colorIndex = 0; colorIndex < 6; ++colorIndex) {
                int previousStep = Color.HSBtoRGB((float)step / 6.0f, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float)(step + 1) / 6.0f, 1.0f, 1.0f);
                RenderUtils2D.drawGradientRect(x, y + (double)step * (height / 6.0), x + width, y + (double)(step + 1) * (height / 6.0), previousStep, nextStep, false);
                ++step;
            }
            int sliderMinY = (int)(y + height * (double)hue) - 4;
            RenderUtils2D.drawRect(x, sliderMinY - 1, x + width, sliderMinY + 1, -1);
        } else {
            for (int colorIndex = 0; colorIndex < 6; ++colorIndex) {
                int previousStep = Color.HSBtoRGB((float)step / 6.0f, 1.0f, 1.0f);
                int nextStep = Color.HSBtoRGB((float)(step + 1) / 6.0f, 1.0f, 1.0f);
                RenderUtils2D.gradient(x + (double)step * (width / 6.0), y, x + (double)(step + 1) * (width / 6.0), y + height, previousStep, nextStep, true);
                ++step;
            }
            int sliderMinX = (int)(x + width * (double)hue);
            RenderUtils2D.drawRect(sliderMinX - 1, y, sliderMinX + 1, y + height, -1);
        }
    }

    public void drawAlphaSlider(double x, double y, float width, float height, float red, float green, float blue, float alpha) {
        boolean left = true;
        float checkerBoardSquareSize = height / 2.0f;
        for (float squareIndex = -checkerBoardSquareSize; squareIndex < width; squareIndex += checkerBoardSquareSize) {
            if (!left) {
                RenderUtils2D.drawRect(x + (double)squareIndex, y, x + (double)squareIndex + (double)checkerBoardSquareSize, y + (double)height, -1);
                RenderUtils2D.drawRect(x + (double)squareIndex, y + (double)checkerBoardSquareSize, x + (double)squareIndex + (double)checkerBoardSquareSize, y + (double)height, -7303024);
                if (squareIndex < width - checkerBoardSquareSize) {
                    double minX = x + (double)squareIndex + (double)checkerBoardSquareSize;
                    double maxX = Math.min(x + (double)width, x + (double)squareIndex + (double)(checkerBoardSquareSize * 2.0f));
                    RenderUtils2D.drawRect(minX, y, maxX, y + (double)height, -7303024);
                    RenderUtils2D.drawRect(minX, y + (double)checkerBoardSquareSize, maxX, y + (double)height, -1);
                }
            }
            left = !left;
        }
        RenderUtils2D.drawLeftGradientRect(x, y, x + (double)width, y + (double)height, new Color(red, green, blue, 1.0f).getRGB(), 0);
        int sliderMinX = (int)(x + (double)width - (double)(width * alpha));
        RenderUtils2D.drawRect(sliderMinX - 1, y, sliderMinX + 1, y + (double)height, -1);
    }

    public void doRainbow(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.mouseOver(this.parent.parent.getX(), (double)this.parent.parent.getY() + this.offset + (double)(ClickGuiVariables.buttonBarHeight * 6) - 2.0, this.parent.parent.getX() + this.parent.parent.getWidth(), (double)this.parent.parent.getY() + this.offset + (double)(ClickGuiVariables.buttonBarHeight * 7), mouseX, mouseY)) {
            this.op.setRainbow(!this.op.getRainbow());
            if (ClickGuiModule.clickSound.enabled) {
                GuiUtil.clickSound();
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        this.pickingColor = false;
        this.pickingHue = false;
        this.pickingAlpha = false;
    }

    public boolean mouseOver(double minX, double minY, double maxX, double maxY, int mX, int mY) {
        return (double)mX >= minX && (double)mY >= minY && (double)mX <= maxX && (double)mY <= maxY;
    }

    public boolean isMouseOnButton(double x, double y) {
        return x > (double)this.x && x < (double)(this.x + ClickGuiVariables.frameWidth) && y > this.y && y < this.y + (double)ClickGuiVariables.buttonBarHeight;
    }
}

