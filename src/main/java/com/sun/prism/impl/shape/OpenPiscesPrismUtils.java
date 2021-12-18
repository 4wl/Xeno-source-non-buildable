/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathConsumer2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.openpisces.Dasher;
import com.sun.openpisces.Renderer;
import com.sun.openpisces.Stroker;
import com.sun.openpisces.TransformingPathConsumer2D;
import com.sun.prism.BasicStroke;

public class OpenPiscesPrismUtils {
    private static final Renderer savedAARenderer = new Renderer(3, 3);
    private static final Renderer savedRenderer = new Renderer(0, 0);
    private static final Stroker savedStroker = new Stroker(savedRenderer);
    private static final Dasher savedDasher = new Dasher(savedStroker);
    private static TransformingPathConsumer2D.FilterSet transformer = new TransformingPathConsumer2D.FilterSet();

    private static PathConsumer2D initRenderer(BasicStroke basicStroke, BaseTransform baseTransform, Rectangle rectangle, int n, Renderer renderer) {
        int n2 = basicStroke == null && n == 0 ? 0 : 1;
        renderer.reset(rectangle.x, rectangle.y, rectangle.width, rectangle.height, n2);
        PathConsumer2D pathConsumer2D = transformer.getConsumer(renderer, baseTransform);
        if (basicStroke != null) {
            savedStroker.reset(basicStroke.getLineWidth(), basicStroke.getEndCap(), basicStroke.getLineJoin(), basicStroke.getMiterLimit());
            savedStroker.setConsumer(pathConsumer2D);
            pathConsumer2D = savedStroker;
            float[] arrf = basicStroke.getDashArray();
            if (arrf != null) {
                savedDasher.reset(arrf, basicStroke.getDashPhase());
                pathConsumer2D = savedDasher;
            }
        }
        return pathConsumer2D;
    }

    public static void feedConsumer(PathIterator pathIterator, PathConsumer2D pathConsumer2D) {
        float[] arrf = new float[6];
        while (!pathIterator.isDone()) {
            int n = pathIterator.currentSegment(arrf);
            switch (n) {
                case 0: {
                    pathConsumer2D.moveTo(arrf[0], arrf[1]);
                    break;
                }
                case 1: {
                    pathConsumer2D.lineTo(arrf[0], arrf[1]);
                    break;
                }
                case 2: {
                    pathConsumer2D.quadTo(arrf[0], arrf[1], arrf[2], arrf[3]);
                    break;
                }
                case 3: {
                    pathConsumer2D.curveTo(arrf[0], arrf[1], arrf[2], arrf[3], arrf[4], arrf[5]);
                    break;
                }
                case 4: {
                    pathConsumer2D.closePath();
                }
            }
            pathIterator.next();
        }
        pathConsumer2D.pathDone();
    }

    public static Renderer setupRenderer(Shape shape, BasicStroke basicStroke, BaseTransform baseTransform, Rectangle rectangle, boolean bl) {
        PathIterator pathIterator = shape.getPathIterator(null);
        Renderer renderer = bl ? savedAARenderer : savedRenderer;
        OpenPiscesPrismUtils.feedConsumer(pathIterator, OpenPiscesPrismUtils.initRenderer(basicStroke, baseTransform, rectangle, pathIterator.getWindingRule(), renderer));
        return renderer;
    }

    public static Renderer setupRenderer(Path2D path2D, BasicStroke basicStroke, BaseTransform baseTransform, Rectangle rectangle, boolean bl) {
        Renderer renderer = bl ? savedAARenderer : savedRenderer;
        PathConsumer2D pathConsumer2D = OpenPiscesPrismUtils.initRenderer(basicStroke, baseTransform, rectangle, path2D.getWindingRule(), renderer);
        float[] arrf = path2D.getFloatCoordsNoClone();
        byte[] arrby = path2D.getCommandsNoClone();
        int n = path2D.getNumCommands();
        int n2 = 0;
        block7: for (int i = 0; i < n; ++i) {
            switch (arrby[i]) {
                case 0: {
                    pathConsumer2D.moveTo(arrf[n2 + 0], arrf[n2 + 1]);
                    n2 += 2;
                    continue block7;
                }
                case 1: {
                    pathConsumer2D.lineTo(arrf[n2 + 0], arrf[n2 + 1]);
                    n2 += 2;
                    continue block7;
                }
                case 2: {
                    pathConsumer2D.quadTo(arrf[n2 + 0], arrf[n2 + 1], arrf[n2 + 2], arrf[n2 + 3]);
                    n2 += 4;
                    continue block7;
                }
                case 3: {
                    pathConsumer2D.curveTo(arrf[n2 + 0], arrf[n2 + 1], arrf[n2 + 2], arrf[n2 + 3], arrf[n2 + 4], arrf[n2 + 5]);
                    n2 += 6;
                    continue block7;
                }
                case 4: {
                    pathConsumer2D.closePath();
                }
            }
        }
        pathConsumer2D.pathDone();
        return renderer;
    }
}

