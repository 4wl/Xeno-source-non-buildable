/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Platform
 *  javafx.collections.ObservableList
 *  javafx.event.EventType
 *  javafx.scene.input.InputMethodEvent
 *  javafx.scene.input.InputMethodRequests
 *  javafx.scene.input.InputMethodTextRun
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.input.ScrollEvent
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Pixels;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.embed.AbstractEvents;
import com.sun.javafx.embed.EmbeddedSceneDTInterface;
import com.sun.javafx.embed.EmbeddedSceneInterface;
import com.sun.javafx.embed.HostDragStartListener;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.scene.input.KeyCodeMap;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.tk.quantum.EmbeddedSceneDnD;
import com.sun.javafx.tk.quantum.EmbeddedState;
import com.sun.javafx.tk.quantum.GlassScene;
import com.sun.javafx.tk.quantum.GlassStage;
import com.sun.javafx.tk.quantum.PaintCollector;
import com.sun.javafx.tk.quantum.PaintRenderJob;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import com.sun.javafx.tk.quantum.UploadingPainter;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;
import java.nio.IntBuffer;
import java.security.AccessController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

final class EmbeddedScene
extends GlassScene
implements EmbeddedSceneInterface {
    private HostInterface host;
    private UploadingPainter painter;
    private PaintRenderJob paintRenderJob;
    private float renderScale;
    private final EmbeddedSceneDnD embeddedDnD;
    private volatile IntBuffer texBits;
    private volatile int texLineStride;
    private volatile float texScaleFactor = 1.0f;

    public EmbeddedScene(HostInterface hostInterface, boolean bl, boolean bl2) {
        super(bl, bl2);
        this.sceneState = new EmbeddedState(this);
        this.host = hostInterface;
        this.embeddedDnD = new EmbeddedSceneDnD(this);
        PaintCollector paintCollector = PaintCollector.getInstance();
        this.painter = new UploadingPainter(this);
        this.paintRenderJob = new PaintRenderJob(this, paintCollector.getRendered(), this.painter);
    }

    @Override
    public void dispose() {
        assert (this.host != null);
        QuantumToolkit.runWithRenderLock(() -> {
            this.host.setEmbeddedScene(null);
            this.host = null;
            this.updateSceneState();
            this.painter = null;
            this.paintRenderJob = null;
            this.texBits = null;
            return null;
        });
        super.dispose();
    }

    @Override
    void setStage(GlassStage glassStage) {
        super.setStage(glassStage);
        assert (this.host != null);
        this.host.setEmbeddedScene(glassStage != null ? this : null);
    }

    @Override
    protected boolean isSynchronous() {
        return false;
    }

    @Override
    public void setRoot(NGNode nGNode) {
        super.setRoot(nGNode);
        this.painter.setRoot(nGNode);
    }

    @Override
    public TKClipboard createDragboard(boolean bl) {
        return this.embeddedDnD.createDragboard(bl);
    }

    @Override
    public void enableInputMethodEvents(boolean bl) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedScene.enableInputMethodEvents " + bl);
        }
    }

    @Override
    public void finishInputMethodComposition() {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedScene.finishInputMethodComposition");
        }
    }

    @Override
    public void setPixelScaleFactor(float f) {
        this.renderScale = f;
        this.entireSceneNeedsRepaint();
    }

    public float getRenderScale() {
        return this.renderScale;
    }

    void uploadPixels(Pixels pixels) {
        this.texBits = (IntBuffer)pixels.getPixels();
        this.texLineStride = pixels.getWidthUnsafe();
        this.texScaleFactor = pixels.getScaleUnsafe();
        if (this.host != null) {
            this.host.repaint();
        }
    }

    @Override
    public void repaint() {
        Toolkit toolkit = Toolkit.getToolkit();
        toolkit.addRenderJob(this.paintRenderJob);
    }

    @Override
    public boolean traverseOut(Direction direction) {
        if (direction == Direction.NEXT) {
            return this.host.traverseFocusOut(true);
        }
        if (direction == Direction.PREVIOUS) {
            return this.host.traverseFocusOut(false);
        }
        return false;
    }

    @Override
    public void setSize(int n, int n2) {
        Platform.runLater(() -> AccessController.doPrivileged(() -> {
            if (this.sceneListener != null) {
                this.sceneListener.changedSize(n, n2);
            }
            return null;
        }, this.getAccessControlContext()));
    }

    @Override
    public boolean getPixels(IntBuffer intBuffer, int n, int n2) {
        return QuantumToolkit.runWithRenderLock(() -> {
            int n3 = n;
            int n4 = n2;
            if (this.getRenderScale() != this.texScaleFactor || this.texBits == null) {
                return false;
            }
            n3 = Math.round((float)n3 * this.texScaleFactor);
            n4 = Math.round((float)n4 * this.texScaleFactor);
            intBuffer.rewind();
            this.texBits.rewind();
            if (intBuffer.capacity() != this.texBits.capacity()) {
                int n5 = Math.min(n3, this.texLineStride);
                int n6 = Math.min(n4, this.texBits.capacity() / this.texLineStride);
                int[] arrn = new int[n5];
                for (int i = 0; i < n6; ++i) {
                    this.texBits.position(i * this.texLineStride);
                    this.texBits.get(arrn, 0, n5);
                    intBuffer.position(i * n3);
                    intBuffer.put(arrn);
                }
                return true;
            }
            intBuffer.put(this.texBits);
            return true;
        });
    }

    @Override
    protected Color getClearColor() {
        if (this.fillPaint != null && this.fillPaint.getType() == Paint.Type.COLOR && ((Color)this.fillPaint).getAlpha() == 0.0f) {
            return (Color)this.fillPaint;
        }
        return super.getClearColor();
    }

    @Override
    public void mouseEvent(int n, int n2, boolean bl, boolean bl2, boolean bl3, int n3, int n4, int n5, int n6, boolean bl4, boolean bl5, boolean bl6, boolean bl7, int n7, boolean bl8) {
        Platform.runLater(() -> AccessController.doPrivileged(() -> {
            if (this.sceneListener == null) {
                return null;
            }
            assert (n != 2);
            if (n == 7) {
                this.sceneListener.scrollEvent((EventType<ScrollEvent>)ScrollEvent.SCROLL, 0.0, -n7, 0.0, 0.0, 40.0, 40.0, 0, 0, 0, 0, 0, n3, n4, n5, n6, bl4, bl5, bl6, bl7, false, false);
            } else {
                EventType<MouseEvent> eventType = AbstractEvents.mouseIDToFXEventID(n);
                this.sceneListener.mouseEvent(eventType, n3, n4, n5, n6, AbstractEvents.mouseButtonToFXMouseButton(n2), bl8, false, bl4, bl5, bl6, bl7, bl, bl2, bl3);
            }
            return null;
        }, this.getAccessControlContext()));
    }

    @Override
    public void inputMethodEvent(EventType<InputMethodEvent> eventType, ObservableList<InputMethodTextRun> observableList, String string, int n) {
        Platform.runLater(() -> AccessController.doPrivileged(() -> {
            if (this.sceneListener != null) {
                this.sceneListener.inputMethodEvent(eventType, observableList, string, n);
            }
            return null;
        }));
    }

    @Override
    public void menuEvent(int n, int n2, int n3, int n4, boolean bl) {
        Platform.runLater(() -> AccessController.doPrivileged(() -> {
            if (this.sceneListener != null) {
                this.sceneListener.menuEvent(n, n2, n3, n4, bl);
            }
            return null;
        }, this.getAccessControlContext()));
    }

    @Override
    public void keyEvent(int n, int n2, char[] arrc, int n3) {
        Platform.runLater(() -> AccessController.doPrivileged(() -> {
            if (this.sceneListener != null) {
                String string;
                boolean bl = (n3 & 1) != 0;
                boolean bl2 = (n3 & 2) != 0;
                boolean bl3 = (n3 & 4) != 0;
                boolean bl4 = (n3 & 8) != 0;
                String string2 = string = new String(arrc);
                KeyEvent keyEvent = new KeyEvent(AbstractEvents.keyIDToFXEventType(n), string, string2, KeyCodeMap.valueOf(n2), bl, bl2, bl3, bl4);
                this.sceneListener.keyEvent(keyEvent);
            }
            return null;
        }, this.getAccessControlContext()));
    }

    @Override
    public void setCursor(Object object) {
        super.setCursor(object);
        this.host.setCursor((CursorFrame)object);
    }

    @Override
    public void setDragStartListener(HostDragStartListener hostDragStartListener) {
        this.embeddedDnD.setDragStartListener(hostDragStartListener);
    }

    @Override
    public EmbeddedSceneDTInterface createDropTarget() {
        return this.embeddedDnD.createDropTarget();
    }

    @Override
    public InputMethodRequests getInputMethodRequests() {
        return this.inputMethodRequests;
    }
}

