/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Point2D
 *  javafx.geometry.Point3D
 *  javafx.scene.Camera
 *  javafx.scene.Node
 */
package com.sun.javafx.scene;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Node;

public class CameraHelper {
    private static CameraAccessor cameraAccessor;

    private CameraHelper() {
    }

    public static Point2D project(Camera camera, Point3D point3D) {
        return cameraAccessor.project(camera, point3D);
    }

    public static Point2D pickNodeXYPlane(Camera camera, Node node, double d, double d2) {
        return cameraAccessor.pickNodeXYPlane(camera, node, d, d2);
    }

    public static Point3D pickProjectPlane(Camera camera, double d, double d2) {
        return cameraAccessor.pickProjectPlane(camera, d, d2);
    }

    public static void setCameraAccessor(CameraAccessor cameraAccessor) {
        if (CameraHelper.cameraAccessor != null) {
            throw new IllegalStateException();
        }
        CameraHelper.cameraAccessor = cameraAccessor;
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
        CameraHelper.forceInit(Camera.class);
    }

    public static interface CameraAccessor {
        public Point2D project(Camera var1, Point3D var2);

        public Point2D pickNodeXYPlane(Camera var1, Node var2, double var3, double var5);

        public Point3D pickProjectPlane(Camera var1, double var2, double var4);
    }
}

