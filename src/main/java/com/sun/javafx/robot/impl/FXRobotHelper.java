/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableList
 *  javafx.event.EventType
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseButton
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.input.ScrollEvent
 *  javafx.scene.input.ScrollEvent$HorizontalTextScrollUnits
 *  javafx.scene.input.ScrollEvent$VerticalTextScrollUnits
 *  javafx.scene.paint.Color
 *  javafx.stage.Stage
 */
package com.sun.javafx.robot.impl;

import com.sun.javafx.robot.FXRobotImage;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FXRobotHelper {
    static FXRobotInputAccessor inputAccessor;
    static FXRobotSceneAccessor sceneAccessor;
    static FXRobotStageAccessor stageAccessor;
    static FXRobotImageConvertor imageConvertor;

    public static ObservableList<Node> getChildren(Parent parent) {
        if (sceneAccessor == null) {
            // empty if block
        }
        return sceneAccessor.getChildren(parent);
    }

    public static ObservableList<Stage> getStages() {
        if (stageAccessor == null) {
            // empty if block
        }
        return stageAccessor.getStages();
    }

    public static Color argbToColor(int n) {
        int n2 = n >> 24;
        float f = (float)(n2 &= 0xFF) / 255.0f;
        int n3 = n >> 16;
        int n4 = n >> 8;
        int n5 = n;
        return Color.rgb((int)(n3 &= 0xFF), (int)(n4 &= 0xFF), (int)(n5 &= 0xFF), (double)f);
    }

    public static void setInputAccessor(FXRobotInputAccessor fXRobotInputAccessor) {
        if (inputAccessor != null) {
            System.out.println("Warning: Input accessor is already set: " + inputAccessor);
            Thread.dumpStack();
        }
        inputAccessor = fXRobotInputAccessor;
    }

    public static void setSceneAccessor(FXRobotSceneAccessor fXRobotSceneAccessor) {
        if (sceneAccessor != null) {
            System.out.println("Warning: Scene accessor is already set: " + sceneAccessor);
            Thread.dumpStack();
        }
        sceneAccessor = fXRobotSceneAccessor;
    }

    public static void setImageConvertor(FXRobotImageConvertor fXRobotImageConvertor) {
        if (imageConvertor != null) {
            System.out.println("Warning: Image convertor is already set: " + imageConvertor);
            Thread.dumpStack();
        }
        imageConvertor = fXRobotImageConvertor;
    }

    public static void setStageAccessor(FXRobotStageAccessor fXRobotStageAccessor) {
        if (stageAccessor != null) {
            System.out.println("Warning: Stage accessor already set: " + stageAccessor);
            Thread.dumpStack();
        }
        stageAccessor = fXRobotStageAccessor;
    }

    public static abstract class FXRobotSceneAccessor {
        public abstract void processKeyEvent(Scene var1, KeyEvent var2);

        public abstract void processMouseEvent(Scene var1, MouseEvent var2);

        public abstract void processScrollEvent(Scene var1, ScrollEvent var2);

        public abstract ObservableList<Node> getChildren(Parent var1);

        public abstract Object renderToImage(Scene var1, Object var2);
    }

    public static abstract class FXRobotInputAccessor {
        public abstract int getCodeForKeyCode(KeyCode var1);

        public abstract KeyCode getKeyCodeForCode(int var1);

        public abstract KeyEvent createKeyEvent(EventType<? extends KeyEvent> var1, KeyCode var2, String var3, String var4, boolean var5, boolean var6, boolean var7, boolean var8);

        public abstract MouseEvent createMouseEvent(EventType<? extends MouseEvent> var1, int var2, int var3, int var4, int var5, MouseButton var6, int var7, boolean var8, boolean var9, boolean var10, boolean var11, boolean var12, boolean var13, boolean var14, boolean var15);

        public abstract ScrollEvent createScrollEvent(EventType<? extends ScrollEvent> var1, int var2, int var3, ScrollEvent.HorizontalTextScrollUnits var4, int var5, ScrollEvent.VerticalTextScrollUnits var6, int var7, int var8, int var9, int var10, int var11, boolean var12, boolean var13, boolean var14, boolean var15);
    }

    public static abstract class FXRobotImageConvertor {
        public abstract FXRobotImage convertToFXRobotImage(Object var1);
    }

    public static abstract class FXRobotStageAccessor {
        public abstract ObservableList<Stage> getStages();
    }
}

