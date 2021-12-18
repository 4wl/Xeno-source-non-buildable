/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.SubScene
 */
package com.sun.javafx.scene;

import com.sun.glass.ui.Accessible;
import javafx.scene.Node;
import javafx.scene.SubScene;

public class NodeHelper {
    private static NodeAccessor nodeAccessor;

    private NodeHelper() {
    }

    public static void layoutNodeForPrinting(Node node) {
        nodeAccessor.layoutNodeForPrinting(node);
    }

    public static boolean isDerivedDepthTest(Node node) {
        return nodeAccessor.isDerivedDepthTest(node);
    }

    public static SubScene getSubScene(Node node) {
        return nodeAccessor.getSubScene(node);
    }

    public static Accessible getAccessible(Node node) {
        return nodeAccessor.getAccessible(node);
    }

    public static void setNodeAccessor(NodeAccessor nodeAccessor) {
        if (NodeHelper.nodeAccessor != null) {
            throw new IllegalStateException();
        }
        NodeHelper.nodeAccessor = nodeAccessor;
    }

    public static NodeAccessor getNodeAccessor() {
        if (nodeAccessor == null) {
            throw new IllegalStateException();
        }
        return nodeAccessor;
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
        NodeHelper.forceInit(Node.class);
    }

    public static interface NodeAccessor {
        public void layoutNodeForPrinting(Node var1);

        public boolean isDerivedDepthTest(Node var1);

        public SubScene getSubScene(Node var1);

        public void setLabeledBy(Node var1, Node var2);

        public Accessible getAccessible(Node var1);
    }
}

