/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Camera
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 */
package com.sun.javafx.scene;

import com.sun.glass.ui.Accessible;
import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public final class SceneHelper {
    private static SceneAccessor sceneAccessor;

    private SceneHelper() {
    }

    public static void setPaused(boolean bl) {
        sceneAccessor.setPaused(bl);
    }

    public static void parentEffectiveOrientationInvalidated(Scene scene) {
        sceneAccessor.parentEffectiveOrientationInvalidated(scene);
    }

    public static Camera getEffectiveCamera(Scene scene) {
        return sceneAccessor.getEffectiveCamera(scene);
    }

    public static Scene createPopupScene(Parent parent) {
        return sceneAccessor.createPopupScene(parent);
    }

    public static Accessible getAccessible(Scene scene) {
        return sceneAccessor.getAccessible(scene);
    }

    public static void setSceneAccessor(SceneAccessor sceneAccessor) {
        if (SceneHelper.sceneAccessor != null) {
            throw new IllegalStateException();
        }
        SceneHelper.sceneAccessor = sceneAccessor;
    }

    public static SceneAccessor getSceneAccessor() {
        if (sceneAccessor == null) {
            throw new IllegalStateException();
        }
        return sceneAccessor;
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
        SceneHelper.forceInit(Scene.class);
    }

    public static interface SceneAccessor {
        public void setPaused(boolean var1);

        public void parentEffectiveOrientationInvalidated(Scene var1);

        public Camera getEffectiveCamera(Scene var1);

        public Scene createPopupScene(Parent var1);

        public void setTransientFocusContainer(Scene var1, Node var2);

        public Accessible getAccessible(Scene var1);
    }
}

