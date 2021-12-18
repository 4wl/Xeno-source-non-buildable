/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.MouseButton
 */
package com.sun.javafx.robot;

import com.sun.javafx.robot.FXRobotImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public abstract class FXRobot {
    protected boolean autoWait = false;

    public abstract void waitForIdle();

    public void setAutoWaitForIdle(boolean bl) {
        this.autoWait = bl;
    }

    public abstract void keyPress(KeyCode var1);

    public abstract void keyRelease(KeyCode var1);

    public abstract void keyType(KeyCode var1, String var2);

    public abstract void mouseMove(int var1, int var2);

    public abstract void mousePress(MouseButton var1, int var2);

    public abstract void mouseRelease(MouseButton var1, int var2);

    public abstract void mouseClick(MouseButton var1, int var2);

    public void mousePress(MouseButton mouseButton) {
        this.mousePress(mouseButton, 1);
    }

    public void mouseRelease(MouseButton mouseButton) {
        this.mouseRelease(mouseButton, 1);
    }

    public void mouseClick(MouseButton mouseButton) {
        this.mouseClick(mouseButton, 1);
    }

    public abstract void mouseDrag(MouseButton var1);

    public abstract void mouseWheel(int var1);

    public abstract int getPixelColor(int var1, int var2);

    public abstract FXRobotImage getSceneCapture(int var1, int var2, int var3, int var4);
}

