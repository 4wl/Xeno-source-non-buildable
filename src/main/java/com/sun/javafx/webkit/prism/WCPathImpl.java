/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.prism;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.webkit.graphics.WCPath;
import com.sun.webkit.graphics.WCPathIterator;
import com.sun.webkit.graphics.WCRectangle;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

final class WCPathImpl
extends WCPath<Path2D> {
    private final Path2D path;
    private boolean hasCP = false;
    private static final Logger log = Logger.getLogger(WCPathImpl.class.getName());

    WCPathImpl() {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Create empty WCPathImpl({0})", this.getID());
        }
        this.path = new Path2D();
    }

    WCPathImpl(WCPathImpl wCPathImpl) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Create WCPathImpl({0}) from WCPathImpl({1})", new Object[]{this.getID(), wCPathImpl.getID()});
        }
        this.path = new Path2D(wCPathImpl.path);
        this.hasCP = wCPathImpl.hasCP;
    }

    @Override
    public void addRect(double d, double d2, double d3, double d4) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).addRect({1},{2},{3},{4})", new Object[]{this.getID(), d, d2, d3, d4});
        }
        this.hasCP = true;
        this.path.append(new RoundRectangle2D((float)d, (float)d2, (float)d3, (int)d4, 0.0f, 0.0f), false);
    }

    @Override
    public void addEllipse(double d, double d2, double d3, double d4) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).addEllipse({1},{2},{3},{4})", new Object[]{this.getID(), d, d2, d3, d4});
        }
        this.hasCP = true;
        this.path.append(new Ellipse2D((float)d, (float)d2, (float)d3, (float)d4), false);
    }

    @Override
    public void addArcTo(double d, double d2, double d3, double d4, double d5) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).addArcTo({1},{2},{3},{4})", new Object[]{this.getID(), d, d2, d3, d4});
        }
        Arc2D arc2D = new Arc2D();
        arc2D.setArcByTangent(this.path.getCurrentPoint(), new Point2D((float)d, (float)d2), new Point2D((float)d3, (float)d4), (float)d5);
        this.hasCP = true;
        this.path.append(arc2D, true);
    }

    @Override
    public void addArc(double d, double d2, double d3, double d4, double d5, boolean bl) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).addArc({1},{2},{3},{4},{5},{6})", new Object[]{this.getID(), d, d2, d3, d4, d5, bl});
        }
        this.hasCP = true;
        double d6 = 0.001;
        if (!bl) {
            int n;
            if (d5 < 0.0) {
                if (d5 < Math.PI * -2 - d6) {
                    n = (int)(-d5 / (Math.PI * 2));
                    d5 += (double)n * 2.0 * Math.PI;
                }
                d5 += Math.PI * 2;
            } else if (d5 > Math.PI * 2 + d6) {
                n = (int)(d5 / (Math.PI * 2));
                d5 -= (double)n * 2.0 * Math.PI;
            }
            if (d4 < 0.0) {
                if (d4 < Math.PI * -2 - d6) {
                    n = (int)(-d4 / (Math.PI * 2));
                    d4 += (double)n * 2.0 * Math.PI;
                }
                d4 += Math.PI * 2;
            } else if (d4 > Math.PI * 2 + d6) {
                n = (int)(d4 / (Math.PI * 2));
                d4 -= (double)n * 2.0 * Math.PI;
            }
            double d7 = d4 - d5;
            if (d4 < d5) {
                d7 = Math.abs(d7);
            }
            d5 = (float)(Math.PI * 2 - d5);
            Arc2D arc2D = new Arc2D((float)(d - d3), (float)(d2 - d3), (float)(2.0 * d3), (float)(2.0 * d3), (float)(d5 * 180.0 / Math.PI), (float)(d7 * 180.0 / Math.PI), 0);
            PathIterator pathIterator = ((Shape)arc2D).getPathIterator(null);
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            ArrayList<Float> arrayList2 = new ArrayList<Float>();
            float[] arrf = new float[6];
            while (!pathIterator.isDone()) {
                switch (pathIterator.currentSegment(arrf)) {
                    case 0: {
                        arrayList2.add(Float.valueOf(arrf[1]));
                        arrayList2.add(Float.valueOf(arrf[0]));
                        break;
                    }
                    case 2: {
                        throw new RuntimeException("Unexpected segment: SEG_QUADTO");
                    }
                    case 3: {
                        arrayList2.add(Float.valueOf(arrf[1]));
                        arrayList2.add(Float.valueOf(arrf[0]));
                        arrayList2.add(Float.valueOf(arrf[3]));
                        arrayList2.add(Float.valueOf(arrf[2]));
                        arrayList2.add(Float.valueOf(arrf[5]));
                        arrayList2.add(Float.valueOf(arrf[4]));
                        arrayList.add(3);
                        break;
                    }
                    case 4: {
                        throw new RuntimeException("Unexpected segment: SEG_CLOSE");
                    }
                }
                pathIterator.next();
            }
            arrayList.add(0);
            Path2D path2D = new Path2D();
            int n2 = arrayList.size();
            int n3 = arrayList2.size();
            while (n2 > 0) {
                switch ((Integer)arrayList.get(--n2)) {
                    case 0: {
                        path2D.moveTo(((Float)arrayList2.get(--n3)).floatValue(), ((Float)arrayList2.get(--n3)).floatValue());
                        break;
                    }
                    case 1: {
                        path2D.lineTo(((Float)arrayList2.get(--n3)).floatValue(), ((Float)arrayList2.get(--n3)).floatValue());
                        break;
                    }
                    case 2: {
                        path2D.quadTo(((Float)arrayList2.get(--n3)).floatValue(), ((Float)arrayList2.get(--n3)).floatValue(), ((Float)arrayList2.get(--n3)).floatValue(), ((Float)arrayList2.get(--n3)).floatValue());
                        break;
                    }
                    case 3: {
                        path2D.curveTo(((Float)arrayList2.get(--n3)).floatValue(), ((Float)arrayList2.get(--n3)).floatValue(), ((Float)arrayList2.get(--n3)).floatValue(), ((Float)arrayList2.get(--n3)).floatValue(), ((Float)arrayList2.get(--n3)).floatValue(), ((Float)arrayList2.get(--n3)).floatValue());
                    }
                }
            }
            this.path.append(path2D, true);
        } else {
            int n;
            if (d5 < 0.0) {
                if (d5 < Math.PI * -2 - d6) {
                    n = (int)(-d5 / (Math.PI * 2));
                    d5 += (double)n * 2.0 * Math.PI;
                }
                d5 += Math.PI * 2;
            } else if (d5 > Math.PI * 2 + d6) {
                n = (int)(d5 / (Math.PI * 2));
                d5 -= (double)n * 2.0 * Math.PI;
            }
            if (d4 < 0.0) {
                if (d4 < Math.PI * -2 - d6) {
                    n = (int)(-d4 / (Math.PI * 2));
                    d4 += (double)n * 2.0 * Math.PI;
                }
                d4 += Math.PI * 2;
            } else if (d4 > Math.PI * 2 + d6) {
                n = (int)(d4 / (Math.PI * 2));
                d4 -= (double)n * 2.0 * Math.PI;
            }
            double d8 = d4 - d5;
            if (d4 < d5 && (d8 += Math.PI * 2) < d6) {
                d8 += Math.PI * 2;
            }
            if (Math.abs(d4) > d6) {
                d4 = (float)(Math.PI * 2 - d4);
            }
            this.path.append(new Arc2D((float)(d - d3), (float)(d2 - d3), (float)(2.0 * d3), (float)(2.0 * d3), (float)(d4 * 180.0 / Math.PI), (float)(d8 * 180.0 / Math.PI), 0), true);
        }
    }

    @Override
    public boolean contains(int n, double d, double d2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).contains({1},{2},{3})", new Object[]{this.getID(), n, d, d2});
        }
        int n2 = this.path.getWindingRule();
        this.path.setWindingRule(n);
        boolean bl = this.path.contains((float)d, (float)d2);
        this.path.setWindingRule(n2);
        return bl;
    }

    @Override
    public WCRectangle getBounds() {
        RectBounds rectBounds = this.path.getBounds();
        return new WCRectangle(rectBounds.getMinX(), rectBounds.getMinY(), rectBounds.getWidth(), rectBounds.getHeight());
    }

    @Override
    public void clear() {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).clear()", this.getID());
        }
        this.hasCP = false;
        this.path.reset();
    }

    @Override
    public void moveTo(double d, double d2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).moveTo({1},{2})", new Object[]{this.getID(), d, d2});
        }
        this.hasCP = true;
        this.path.moveTo((float)d, (float)d2);
    }

    @Override
    public void addLineTo(double d, double d2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).addLineTo({1},{2})", new Object[]{this.getID(), d, d2});
        }
        this.hasCP = true;
        this.path.lineTo((float)d, (float)d2);
    }

    @Override
    public void addQuadCurveTo(double d, double d2, double d3, double d4) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).addQuadCurveTo({1},{2},{3},{4})", new Object[]{this.getID(), d, d2, d3, d4});
        }
        this.hasCP = true;
        this.path.quadTo((float)d, (float)d2, (float)d3, (float)d4);
    }

    @Override
    public void addBezierCurveTo(double d, double d2, double d3, double d4, double d5, double d6) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).addBezierCurveTo({1},{2},{3},{4},{5},{6})", new Object[]{this.getID(), d, d2, d3, d4, d5, d6});
        }
        this.hasCP = true;
        this.path.curveTo((float)d, (float)d2, (float)d3, (float)d4, (float)d5, (float)d6);
    }

    @Override
    public void addPath(WCPath wCPath) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).addPath({1})", new Object[]{this.getID(), wCPath.getID()});
        }
        this.hasCP = this.hasCP || ((WCPathImpl)wCPath).hasCP;
        this.path.append(((WCPathImpl)wCPath).path, false);
    }

    @Override
    public void closeSubpath() {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).closeSubpath()", this.getID());
        }
        this.path.closePath();
    }

    @Override
    public boolean hasCurrentPoint() {
        return this.hasCP;
    }

    @Override
    public boolean isEmpty() {
        PathIterator pathIterator = this.path.getPathIterator(null);
        float[] arrf = new float[6];
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(arrf)) {
                case 1: 
                case 2: 
                case 3: {
                    return false;
                }
            }
            pathIterator.next();
        }
        return true;
    }

    @Override
    public int getWindingRule() {
        return 1 - this.path.getWindingRule();
    }

    @Override
    public void setWindingRule(int n) {
        this.path.setWindingRule(1 - n);
    }

    @Override
    public Path2D getPlatformPath() {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).getPath() BEGIN=====", this.getID());
            PathIterator pathIterator = this.path.getPathIterator(null);
            float[] arrf = new float[6];
            while (!pathIterator.isDone()) {
                switch (pathIterator.currentSegment(arrf)) {
                    case 0: {
                        log.log(Level.FINE, "SEG_MOVETO ({0},{1})", new Object[]{Float.valueOf(arrf[0]), Float.valueOf(arrf[1])});
                        break;
                    }
                    case 1: {
                        log.log(Level.FINE, "SEG_LINETO ({0},{1})", new Object[]{Float.valueOf(arrf[0]), Float.valueOf(arrf[1])});
                        break;
                    }
                    case 2: {
                        log.log(Level.FINE, "SEG_QUADTO ({0},{1},{2},{3})", new Object[]{Float.valueOf(arrf[0]), Float.valueOf(arrf[1]), Float.valueOf(arrf[2]), Float.valueOf(arrf[3])});
                        break;
                    }
                    case 3: {
                        log.log(Level.FINE, "SEG_CUBICTO ({0},{1},{2},{3},{4},{5})", new Object[]{Float.valueOf(arrf[0]), Float.valueOf(arrf[1]), Float.valueOf(arrf[2]), Float.valueOf(arrf[3]), Float.valueOf(arrf[4]), Float.valueOf(arrf[5])});
                        break;
                    }
                    case 4: {
                        log.fine("SEG_CLOSE");
                    }
                }
                pathIterator.next();
            }
            log.fine("========getPath() END=====");
        }
        return this.path;
    }

    @Override
    public WCPathIterator getPathIterator() {
        final PathIterator pathIterator = this.path.getPathIterator(null);
        return new WCPathIterator(){

            @Override
            public int getWindingRule() {
                return pathIterator.getWindingRule();
            }

            @Override
            public boolean isDone() {
                return pathIterator.isDone();
            }

            @Override
            public void next() {
                pathIterator.next();
            }

            @Override
            public int currentSegment(double[] arrd) {
                float[] arrf = new float[6];
                int n = pathIterator.currentSegment(arrf);
                for (int i = 0; i < arrd.length; ++i) {
                    arrd[i] = arrf[i];
                }
                return n;
            }
        };
    }

    @Override
    public void translate(double d, double d2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).translate({1}, {2})", new Object[]{this.getID(), d, d2});
        }
        this.path.transform(BaseTransform.getTranslateInstance(d, d2));
    }

    @Override
    public void transform(double d, double d2, double d3, double d4, double d5, double d6) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).transform({1},{2},{3},{4},{5},{6})", new Object[]{this.getID(), d, d2, d3, d4, d5, d6});
        }
        this.path.transform(BaseTransform.getInstance(d, d2, d3, d4, d5, d6));
    }
}

