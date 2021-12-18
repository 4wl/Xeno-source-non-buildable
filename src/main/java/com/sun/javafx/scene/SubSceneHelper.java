/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Camera
 *  javafx.scene.SubScene
 */
package com.sun.javafx.scene;

import javafx.scene.Camera;
import javafx.scene.SubScene;

public class SubSceneHelper {
    private static SubSceneAccessor subSceneAccessor;

    private SubSceneHelper() {
    }

    public static boolean isDepthBuffer(SubScene subScene) {
        return subSceneAccessor.isDepthBuffer(subScene);
    }

    public static Camera getEffectiveCamera(SubScene subScene) {
        return subSceneAccessor.getEffectiveCamera(subScene);
    }

    public static void setSubSceneAccessor(SubSceneAccessor subSceneAccessor) {
        if (SubSceneHelper.subSceneAccessor != null) {
            throw new IllegalStateException();
        }
        SubSceneHelper.subSceneAccessor = subSceneAccessor;
    }

    private static void forceInit(Class<?> class_) {
        try {
            Class.forName(class_.getName(), true, class_.getClassLoader());
        }
        catch (ClassNotFoundException classNotFoundException) {
            throw new AssertionError((Object)classNotFoundException);
        }
    }

    static {
        SubSceneHelper.forceInit(SubScene.class);
    }

    public static interface SubSceneAccessor {
        public boolean isDepthBuffer(SubScene var1);

        public Camera getEffectiveCamera(SubScene var1);
    }
}

