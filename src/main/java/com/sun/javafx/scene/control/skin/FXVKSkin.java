/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Animation$Status
 *  javafx.animation.Interpolator
 *  javafx.animation.KeyFrame
 *  javafx.animation.KeyValue
 *  javafx.animation.Timeline
 *  javafx.application.Platform
 *  javafx.beans.InvalidationListener
 *  javafx.beans.Observable
 *  javafx.beans.property.DoubleProperty
 *  javafx.beans.property.SimpleDoubleProperty
 *  javafx.beans.value.WritableValue
 *  javafx.event.Event
 *  javafx.event.EventHandler
 *  javafx.event.EventTarget
 *  javafx.geometry.Bounds
 *  javafx.geometry.HPos
 *  javafx.geometry.Point2D
 *  javafx.geometry.Rectangle2D
 *  javafx.geometry.VPos
 *  javafx.scene.Node
 *  javafx.scene.Scene
 *  javafx.scene.control.ComboBoxBase
 *  javafx.scene.control.Skin
 *  javafx.scene.control.TextArea
 *  javafx.scene.control.TextField
 *  javafx.scene.control.TextInputControl
 *  javafx.scene.input.InputEvent
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseButton
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.input.TouchEvent
 *  javafx.scene.layout.Region
 *  javafx.scene.text.Text
 *  javafx.stage.Popup
 *  javafx.stage.Window
 *  javafx.util.Duration
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.FXVK;
import com.sun.javafx.scene.control.skin.FXVKCharEntities;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import com.sun.javafx.util.Utils;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.WritableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Skin;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

public class FXVKSkin
extends BehaviorSkinBase<FXVK, BehaviorBase<FXVK>> {
    private static final int GAP = 6;
    private List<List<Key>> currentBoard;
    private static HashMap<String, List<List<Key>>> boardMap = new HashMap();
    private int numCols;
    private boolean capsDown = false;
    private boolean shiftDown = false;
    private boolean isSymbol = false;
    long lastTime = -1L;
    private static Popup vkPopup;
    private static Popup secondaryPopup;
    private static FXVK primaryVK;
    private static Timeline slideInTimeline;
    private static Timeline slideOutTimeline;
    private static boolean hideAfterSlideOut;
    private static FXVK secondaryVK;
    private static Timeline secondaryVKDelay;
    private static CharKey secondaryVKKey;
    private static TextInputKey repeatKey;
    private static Timeline repeatInitialDelay;
    private static Timeline repeatSubsequentDelay;
    private static double KEY_REPEAT_DELAY;
    private static double KEY_REPEAT_DELAY_MIN;
    private static double KEY_REPEAT_DELAY_MAX;
    private static double KEY_REPEAT_RATE;
    private static double KEY_REPEAT_RATE_MIN;
    private static double KEY_REPEAT_RATE_MAX;
    private Node attachedNode;
    private String vkType = null;
    FXVK fxvk;
    static final double VK_HEIGHT = 243.0;
    static final double VK_SLIDE_MILLIS = 250.0;
    static final double PREF_PORTRAIT_KEY_WIDTH = 40.0;
    static final double PREF_KEY_HEIGHT = 56.0;
    static boolean vkAdjustWindow;
    static boolean vkLookup;
    private static DoubleProperty winY;
    EventHandler<InputEvent> unHideEventHandler;
    private boolean isVKHidden = false;
    private Double origWindowYPos = null;

    void clearShift() {
        if (this.shiftDown && !this.capsDown) {
            this.shiftDown = false;
            this.updateKeys();
        }
        this.lastTime = -1L;
    }

    void pressShift() {
        long l = System.currentTimeMillis();
        if (this.shiftDown && !this.capsDown) {
            if (this.lastTime > 0L && l - this.lastTime < 400L) {
                this.shiftDown = false;
                this.capsDown = true;
            } else {
                this.shiftDown = false;
                this.capsDown = false;
            }
        } else if (!this.shiftDown && !this.capsDown) {
            this.shiftDown = true;
        } else {
            this.shiftDown = false;
            this.capsDown = false;
        }
        this.updateKeys();
        this.lastTime = l;
    }

    void clearSymbolABC() {
        this.isSymbol = false;
        this.updateKeys();
    }

    void pressSymbolABC() {
        this.isSymbol = !this.isSymbol;
        this.updateKeys();
    }

    void clearStateKeys() {
        this.capsDown = false;
        this.shiftDown = false;
        this.isSymbol = false;
        this.lastTime = -1L;
        this.updateKeys();
    }

    private void updateKeys() {
        for (List<Key> list : this.currentBoard) {
            for (Key key : list) {
                key.update(this.capsDown, this.shiftDown, this.isSymbol);
            }
        }
    }

    private static void startSlideIn() {
        slideOutTimeline.stop();
        slideInTimeline.playFromStart();
    }

    private static void startSlideOut(boolean bl) {
        hideAfterSlideOut = bl;
        slideInTimeline.stop();
        slideOutTimeline.playFromStart();
    }

    private void adjustWindowPosition(Node node) {
        TextAreaSkin textAreaSkin;
        if (!(node instanceof TextInputControl)) {
            return;
        }
        double d = node.localToScene(0.0, 0.0).getY() + node.getScene().getY();
        double d2 = ((TextInputControl)node).getHeight();
        double d3 = d + d2;
        double d4 = Utils.getScreen((Object)node).getBounds().getHeight();
        double d5 = d4 - 243.0;
        double d6 = 0.0;
        double d7 = 0.0;
        double d8 = 0.0;
        double d9 = 10.0;
        if (node instanceof TextField) {
            d6 = d + d2 / 2.0;
            d7 = d3;
            textAreaSkin = this.attachedNode.getParent();
            d8 = textAreaSkin instanceof ComboBoxBase ? Math.min(d9 - d, 0.0) : Math.min(d5 / 2.0 - d6, 0.0);
        } else if (node instanceof TextArea) {
            textAreaSkin = (TextAreaSkin)((TextArea)node).getSkin();
            Bounds bounds = textAreaSkin.getCaretBounds();
            double d10 = bounds.getMinY();
            double d11 = bounds.getMaxY();
            d6 = d + (d10 + d11) / 2.0;
            d7 = d + d11;
            d8 = d2 < d5 ? d5 / 2.0 - (d + d2 / 2.0) : d5 / 2.0 - d6;
            d8 = Math.min(d8, 0.0);
        } else {
            d6 = d + d2 / 2.0;
            d7 = d3;
            d8 = Math.min(d5 / 2.0 - d6, 0.0);
        }
        textAreaSkin = node.getScene().getWindow();
        if (this.origWindowYPos + d7 > d5) {
            textAreaSkin.setY(d8);
        } else {
            textAreaSkin.setY(this.origWindowYPos);
        }
    }

    private void saveWindowPosition(Node node) {
        Window window = node.getScene().getWindow();
        this.origWindowYPos = window.getY();
    }

    private void restoreWindowPosition(Node node) {
        Window window;
        Scene scene;
        if (node != null && (scene = node.getScene()) != null && (window = scene.getWindow()) != null) {
            window.setY(this.origWindowYPos.doubleValue());
        }
    }

    private void registerUnhideHandler(Node node) {
        if (this.unHideEventHandler == null) {
            this.unHideEventHandler = inputEvent -> {
                if (this.attachedNode != null && this.isVKHidden) {
                    double d = Utils.getScreen((Object)this.attachedNode).getBounds().getHeight();
                    if (this.fxvk.getHeight() > 0.0 && vkPopup.getY() > d - this.fxvk.getHeight() && slideInTimeline.getStatus() != Animation.Status.RUNNING) {
                        FXVKSkin.startSlideIn();
                        if (vkAdjustWindow) {
                            this.adjustWindowPosition(this.attachedNode);
                        }
                    }
                }
                this.isVKHidden = false;
            };
        }
        node.addEventHandler(TouchEvent.TOUCH_PRESSED, this.unHideEventHandler);
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, this.unHideEventHandler);
    }

    private void unRegisterUnhideHandler(Node node) {
        if (this.unHideEventHandler != null) {
            node.removeEventHandler(TouchEvent.TOUCH_PRESSED, this.unHideEventHandler);
            node.removeEventHandler(MouseEvent.MOUSE_PRESSED, this.unHideEventHandler);
        }
    }

    private String getNodeVKType(Node node) {
        Object object = node.getProperties().get((Object)"vkType");
        String string = null;
        if (object instanceof String) {
            string = ((String)object).toLowerCase(Locale.ROOT);
        }
        return string != null ? string : "text";
    }

    private void updateKeyboardType(Node node) {
        String string = this.vkType;
        this.vkType = this.getNodeVKType(node);
        if (string == null || !this.vkType.equals(string)) {
            this.rebuildPrimaryVK(this.vkType);
        }
    }

    private void closeSecondaryVK() {
        if (secondaryVK != null) {
            secondaryVK.setAttachedNode(null);
            secondaryPopup.hide();
        }
    }

    private void setupPrimaryVK() {
        this.fxvk.setFocusTraversable(false);
        this.fxvk.setVisible(true);
        if (vkPopup == null) {
            vkPopup = new Popup();
            vkPopup.setAutoFix(false);
        }
        vkPopup.getContent().setAll((Object[])new Node[]{this.fxvk});
        double d = Utils.getScreen((Object)this.fxvk).getBounds().getHeight();
        double d2 = Utils.getScreen((Object)this.fxvk).getBounds().getWidth();
        slideInTimeline.getKeyFrames().setAll((Object[])new KeyFrame[]{new KeyFrame(Duration.millis((double)250.0), new KeyValue[]{new KeyValue((WritableValue)winY, (Object)(d - 243.0), Interpolator.EASE_BOTH)})});
        slideOutTimeline.getKeyFrames().setAll((Object[])new KeyFrame[]{new KeyFrame(Duration.millis((double)250.0), actionEvent -> {
            if (hideAfterSlideOut && vkPopup.isShowing()) {
                vkPopup.hide();
            }
        }, new KeyValue[]{new KeyValue((WritableValue)winY, (Object)d, Interpolator.EASE_BOTH)})});
        this.fxvk.setPrefWidth(d2);
        this.fxvk.setMinWidth(Double.NEGATIVE_INFINITY);
        this.fxvk.setMaxWidth(Double.NEGATIVE_INFINITY);
        this.fxvk.setPrefHeight(243.0);
        this.fxvk.setMinHeight(Double.NEGATIVE_INFINITY);
        if (secondaryVKDelay == null) {
            secondaryVKDelay = new Timeline();
        }
        KeyFrame keyFrame = new KeyFrame(Duration.millis((double)500.0), actionEvent -> {
            if (secondaryVKKey != null) {
                this.showSecondaryVK(secondaryVKKey);
            }
        }, new KeyValue[0]);
        secondaryVKDelay.getKeyFrames().setAll((Object[])new KeyFrame[]{keyFrame});
        if (KEY_REPEAT_RATE > 0.0) {
            repeatInitialDelay = new Timeline(new KeyFrame[]{new KeyFrame(Duration.millis((double)KEY_REPEAT_DELAY), actionEvent -> {
                repeatKey.sendKeyEvents();
                repeatSubsequentDelay.playFromStart();
            }, new KeyValue[0])});
            repeatSubsequentDelay = new Timeline(new KeyFrame[]{new KeyFrame(Duration.millis((double)(1000.0 / KEY_REPEAT_RATE)), actionEvent -> repeatKey.sendKeyEvents(), new KeyValue[0])});
            repeatSubsequentDelay.setCycleCount(-1);
        }
    }

    void prerender(Node node) {
        if (this.fxvk != primaryVK) {
            return;
        }
        this.loadBoard("text");
        this.loadBoard("numeric");
        this.loadBoard("url");
        this.loadBoard("email");
        this.updateKeyboardType(node);
        this.fxvk.setVisible(true);
        if (!vkPopup.isShowing()) {
            Rectangle2D rectangle2D = Utils.getScreen((Object)node).getBounds();
            vkPopup.setX((rectangle2D.getWidth() - this.fxvk.prefWidth(-1.0)) / 2.0);
            winY.set(rectangle2D.getHeight());
            vkPopup.show(node.getScene().getWindow());
        }
    }

    public FXVKSkin(final FXVK fXVK) {
        super(fXVK, new BehaviorBase<FXVK>(fXVK, Collections.emptyList()));
        this.fxvk = fXVK;
        if (fXVK == FXVK.vk) {
            primaryVK = fXVK;
        }
        if (fXVK == primaryVK) {
            this.setupPrimaryVK();
        }
        fXVK.attachedNodeProperty().addListener(new InvalidationListener(){

            public void invalidated(Observable observable) {
                Node node = FXVKSkin.this.attachedNode;
                FXVKSkin.this.attachedNode = fXVK.getAttachedNode();
                if (fXVK != primaryVK) {
                    return;
                }
                FXVKSkin.this.closeSecondaryVK();
                if (FXVKSkin.this.attachedNode != null) {
                    if (node != null) {
                        FXVKSkin.this.unRegisterUnhideHandler(node);
                    }
                    FXVKSkin.this.registerUnhideHandler(FXVKSkin.this.attachedNode);
                    FXVKSkin.this.updateKeyboardType(FXVKSkin.this.attachedNode);
                    if ((node == null || node.getScene() == null || node.getScene().getWindow() != FXVKSkin.this.attachedNode.getScene().getWindow()) && vkPopup.isShowing()) {
                        vkPopup.hide();
                    }
                    if (!vkPopup.isShowing()) {
                        Rectangle2D rectangle2D = Utils.getScreen((Object)FXVKSkin.this.attachedNode).getBounds();
                        vkPopup.setX((rectangle2D.getWidth() - fXVK.prefWidth(-1.0)) / 2.0);
                        if (node == null || FXVKSkin.this.isVKHidden) {
                            winY.set(rectangle2D.getHeight());
                        } else {
                            winY.set(rectangle2D.getHeight() - 243.0);
                        }
                        vkPopup.show(FXVKSkin.this.attachedNode.getScene().getWindow());
                    }
                    if (node == null || FXVKSkin.this.isVKHidden) {
                        FXVKSkin.startSlideIn();
                    }
                    if (vkAdjustWindow) {
                        if (node == null || node.getScene() == null || node.getScene().getWindow() != FXVKSkin.this.attachedNode.getScene().getWindow()) {
                            FXVKSkin.this.saveWindowPosition(FXVKSkin.this.attachedNode);
                        }
                        FXVKSkin.this.adjustWindowPosition(FXVKSkin.this.attachedNode);
                    }
                } else {
                    if (node != null) {
                        FXVKSkin.this.unRegisterUnhideHandler(node);
                    }
                    FXVKSkin.startSlideOut(true);
                    if (vkAdjustWindow) {
                        FXVKSkin.this.restoreWindowPosition(node);
                    }
                }
                FXVKSkin.this.isVKHidden = false;
            }
        });
    }

    private void rebuildSecondaryVK() {
        if (FXVKSkin.secondaryVK.chars != null) {
            int n;
            int n2;
            int n3 = FXVKSkin.secondaryVK.chars.length;
            int n4 = (int)Math.floor(Math.sqrt(Math.max(1, n3 - 2)));
            int n5 = (int)Math.ceil((double)n3 / (double)n4);
            ArrayList<List<Key>> arrayList = new ArrayList<List<Key>>(2);
            for (int i = 0; i < n4 && (n2 = i * n5) < (n = Math.min(n2 + n5, n3)); ++i) {
                ArrayList<CharKey> object = new ArrayList<CharKey>(n5);
                for (int j = n2; j < n; ++j) {
                    CharKey charKey = new CharKey(FXVKSkin.secondaryVK.chars[j], null, null);
                    charKey.col = (j - n2) * 2;
                    charKey.colSpan = 2;
                    for (String string : charKey.getStyleClass()) {
                        charKey.text.getStyleClass().add((Object)(string + "-text"));
                        charKey.altText.getStyleClass().add((Object)(string + "-alttext"));
                        charKey.icon.getStyleClass().add((Object)(string + "-icon"));
                    }
                    if (FXVKSkin.secondaryVK.chars[j] != null && FXVKSkin.secondaryVK.chars[j].length() > 1) {
                        charKey.text.getStyleClass().add((Object)"multi-char-text");
                    }
                    object.add(charKey);
                }
                arrayList.add(object);
            }
            this.currentBoard = arrayList;
            this.getChildren().clear();
            this.numCols = 0;
            for (List<Key> list : this.currentBoard) {
                for (Key key : list) {
                    this.numCols = Math.max(this.numCols, key.col + key.colSpan);
                }
                this.getChildren().addAll(list);
            }
        }
    }

    private void rebuildPrimaryVK(String string) {
        this.currentBoard = this.loadBoard(string);
        this.clearStateKeys();
        this.getChildren().clear();
        this.numCols = 0;
        for (List<Key> list : this.currentBoard) {
            for (Key key : list) {
                this.numCols = Math.max(this.numCols, key.col + key.colSpan);
            }
            this.getChildren().addAll(list);
        }
    }

    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        return d5 + (double)(56 * this.numCols) + d3;
    }

    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        return d2 + 400.0 + d4;
    }

    protected void layoutChildren(double d, double d2, double d3, double d4) {
        int n = this.currentBoard.size();
        double d5 = (d3 - (double)((this.numCols - 1) * 6)) / (double)this.numCols;
        double d6 = (d4 - (double)((n - 1) * 6)) / (double)n;
        double d7 = d2;
        for (List<Key> list : this.currentBoard) {
            for (Key key : list) {
                double d8 = d + (double)key.col * (d5 + 6.0);
                double d9 = (double)key.colSpan * (d5 + 6.0) - 6.0;
                key.resizeRelocate((int)(d8 + 0.5), (int)(d7 + 0.5), d9, d6);
            }
            d7 += d6 + 6.0;
        }
    }

    private void showSecondaryVK(CharKey charKey) {
        if (charKey != null) {
            Node node = primaryVK.getAttachedNode();
            if (secondaryVK == null) {
                secondaryVK = new FXVK();
                secondaryVK.setSkin((Skin)new FXVKSkin(secondaryVK));
                secondaryVK.getStyleClass().setAll((Object[])new String[]{"fxvk-secondary"});
                secondaryPopup = new Popup();
                secondaryPopup.setAutoHide(true);
                secondaryPopup.getContent().add((Object)secondaryVK);
            }
            FXVKSkin.secondaryVK.chars = null;
            ArrayList<String> arrayList = new ArrayList<String>();
            if (!this.isSymbol && charKey.letterChars != null && charKey.letterChars.length() > 0) {
                if (this.shiftDown || this.capsDown) {
                    arrayList.add(charKey.letterChars.toUpperCase());
                } else {
                    arrayList.add(charKey.letterChars);
                }
            }
            if (charKey.altChars != null && charKey.altChars.length() > 0) {
                if (this.shiftDown || this.capsDown) {
                    arrayList.add(charKey.altChars.toUpperCase());
                } else {
                    arrayList.add(charKey.altChars);
                }
            }
            if (charKey.moreChars != null && charKey.moreChars.length > 0) {
                if (this.isSymbol) {
                    for (String string : charKey.moreChars) {
                        if (Character.isLetter(string.charAt(0))) continue;
                        arrayList.add(string);
                    }
                } else {
                    for (String string : charKey.moreChars) {
                        if (!Character.isLetter(string.charAt(0))) continue;
                        if (this.shiftDown || this.capsDown) {
                            arrayList.add(string.toUpperCase());
                            continue;
                        }
                        arrayList.add(string);
                    }
                }
            }
            boolean bl = false;
            for (String string : arrayList) {
                if (string.length() <= 1) continue;
                bl = true;
            }
            FXVKSkin.secondaryVK.chars = arrayList.toArray(new String[arrayList.size()]);
            if (FXVKSkin.secondaryVK.chars.length > 1) {
                if (secondaryVK.getSkin() != null) {
                    ((FXVKSkin)secondaryVK.getSkin()).rebuildSecondaryVK();
                }
                secondaryVK.setAttachedNode(node);
                Object object = (FXVKSkin)primaryVK.getSkin();
                FXVKSkin fXVKSkin = (FXVKSkin)secondaryVK.getSkin();
                int n = FXVKSkin.secondaryVK.chars.length;
                int n2 = (int)Math.floor(Math.sqrt(Math.max(1, n - 2)));
                int n3 = (int)Math.ceil((double)n / (double)n2);
                double d = this.snappedLeftInset() + this.snappedRightInset() + (double)n3 * 40.0 * (double)(bl ? 2 : 1) + (double)((n3 - 1) * 6);
                double d2 = this.snappedTopInset() + this.snappedBottomInset() + (double)n2 * 56.0 + (double)((n2 - 1) * 6);
                secondaryVK.setPrefWidth(d);
                secondaryVK.setMinWidth(Double.NEGATIVE_INFINITY);
                secondaryVK.setPrefHeight(d2);
                secondaryVK.setMinHeight(Double.NEGATIVE_INFINITY);
                Platform.runLater(() -> {
                    Point2D point2D = Utils.pointRelativeTo((Node)charKey, d, d2, HPos.CENTER, VPos.TOP, 5.0, -3.0, true);
                    double d3 = point2D.getX();
                    double d4 = point2D.getY();
                    Scene scene = charKey.getScene();
                    d3 = Math.min(d3, scene.getWindow().getX() + scene.getWidth() - d);
                    secondaryPopup.show(charKey.getScene().getWindow(), d3, d4);
                });
            }
        } else {
            this.closeSecondaryVK();
        }
    }

    private List<List<Key>> loadBoard(String string) {
        List<List<Key>> list = boardMap.get(string);
        if (list != null) {
            return list;
        }
        String string2 = string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase() + "Board.txt";
        try {
            String string3;
            list = new ArrayList<List<Key>>(5);
            ArrayList<CharKey> arrayList = new ArrayList<CharKey>(20);
            InputStream inputStream = FXVKSkin.class.getResourceAsStream(string2);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            int n = 0;
            int n2 = 0;
            int n3 = 1;
            boolean bl = false;
            ArrayList<String> arrayList2 = new ArrayList<String>(10);
            while ((string3 = bufferedReader.readLine()) != null) {
                if (string3.length() == 0 || string3.charAt(0) == '#') continue;
                for (int i = 0; i < string3.length(); ++i) {
                    char c = string3.charAt(i);
                    if (c == ' ') {
                        ++n;
                        continue;
                    }
                    if (c == '[') {
                        n2 = n;
                        arrayList2 = new ArrayList(10);
                        bl = false;
                        continue;
                    }
                    if (c == ']') {
                        Key key;
                        int n4;
                        String string4 = "";
                        String string5 = null;
                        String[] arrstring = null;
                        for (n4 = 0; n4 < arrayList2.size(); ++n4) {
                            arrayList2.set(n4, FXVKCharEntities.get((String)arrayList2.get(n4)));
                        }
                        n4 = arrayList2.size();
                        if (n4 > 0) {
                            string4 = (String)arrayList2.get(0);
                            if (n4 > 1) {
                                string5 = (String)arrayList2.get(1);
                                if (n4 > 2) {
                                    arrstring = arrayList2.subList(2, n4).toArray(new String[n4 - 2]);
                                }
                            }
                        }
                        n3 = n - n2;
                        if (bl) {
                            if ("$shift".equals(string4)) {
                                key = new KeyboardStateKey("", null, "shift"){

                                    @Override
                                    protected void release() {
                                        FXVKSkin.this.pressShift();
                                    }

                                    @Override
                                    public void update(boolean bl, boolean bl2, boolean bl3) {
                                        if (bl3) {
                                            this.setDisable(true);
                                            this.setVisible(false);
                                        } else {
                                            if (bl) {
                                                this.icon.getStyleClass().remove((Object)"shift-icon");
                                                this.icon.getStyleClass().add((Object)"capslock-icon");
                                            } else {
                                                this.icon.getStyleClass().remove((Object)"capslock-icon");
                                                this.icon.getStyleClass().add((Object)"shift-icon");
                                            }
                                            this.setDisable(false);
                                            this.setVisible(true);
                                        }
                                    }
                                };
                                key.getStyleClass().add((Object)"shift");
                            } else if ("$SymbolABC".equals(string4)) {
                                key = new KeyboardStateKey("!#123", "ABC", "symbol"){

                                    @Override
                                    protected void release() {
                                        FXVKSkin.this.pressSymbolABC();
                                    }
                                };
                            } else if ("$backspace".equals(string4)) {
                                key = new KeyCodeKey("backspace", "\b", KeyCode.BACK_SPACE){

                                    @Override
                                    protected void press() {
                                        if (KEY_REPEAT_RATE > 0.0) {
                                            FXVKSkin.this.clearShift();
                                            this.sendKeyEvents();
                                            repeatKey = this;
                                            repeatInitialDelay.playFromStart();
                                        } else {
                                            super.press();
                                        }
                                    }

                                    @Override
                                    protected void release() {
                                        if (KEY_REPEAT_RATE > 0.0) {
                                            repeatInitialDelay.stop();
                                            repeatSubsequentDelay.stop();
                                        } else {
                                            super.release();
                                        }
                                    }
                                };
                                key.getStyleClass().add((Object)"backspace");
                            } else if ("$enter".equals(string4)) {
                                key = new KeyCodeKey("enter", "\n", KeyCode.ENTER);
                                key.getStyleClass().add((Object)"enter");
                            } else if ("$tab".equals(string4)) {
                                key = new KeyCodeKey("tab", "\t", KeyCode.TAB);
                            } else if ("$space".equals(string4)) {
                                key = new CharKey(" ", " ", null, "space");
                            } else if ("$clear".equals(string4)) {
                                key = new SuperKey("clear", "");
                            } else if ("$.org".equals(string4)) {
                                key = new SuperKey(".org", ".org");
                            } else if ("$.com".equals(string4)) {
                                key = new SuperKey(".com", ".com");
                            } else if ("$.net".equals(string4)) {
                                key = new SuperKey(".net", ".net");
                            } else if ("$oracle.com".equals(string4)) {
                                key = new SuperKey("oracle.com", "oracle.com");
                            } else if ("$gmail.com".equals(string4)) {
                                key = new SuperKey("gmail.com", "gmail.com");
                            } else if ("$hide".equals(string4)) {
                                key = new KeyboardStateKey("hide", null, "hide"){

                                    @Override
                                    protected void release() {
                                        FXVKSkin.this.isVKHidden = true;
                                        FXVKSkin.startSlideOut(false);
                                        if (vkAdjustWindow) {
                                            FXVKSkin.this.restoreWindowPosition(FXVKSkin.this.attachedNode);
                                        }
                                    }
                                };
                                key.getStyleClass().add((Object)"hide");
                            } else {
                                key = "$undo".equals(string4) ? new SuperKey("undo", "") : ("$redo".equals(string4) ? new SuperKey("redo", "") : null);
                            }
                        } else {
                            key = new CharKey(string4, string5, arrstring);
                        }
                        if (key == null) continue;
                        key.col = n2;
                        key.colSpan = n3;
                        for (String string6 : key.getStyleClass()) {
                            key.text.getStyleClass().add((Object)(string6 + "-text"));
                            key.altText.getStyleClass().add((Object)(string6 + "-alttext"));
                            key.icon.getStyleClass().add((Object)(string6 + "-icon"));
                        }
                        if (string4 != null && string4.length() > 1) {
                            key.text.getStyleClass().add((Object)"multi-char-text");
                        }
                        if (string5 != null && string5.length() > 1) {
                            key.altText.getStyleClass().add((Object)"multi-char-text");
                        }
                        arrayList.add((CharKey)key);
                        continue;
                    }
                    for (int j = i; j < string3.length(); ++j) {
                        char c2 = string3.charAt(j);
                        boolean bl2 = false;
                        if (c2 == '\\') {
                            ++i;
                            bl2 = true;
                            c2 = string3.charAt(++j);
                        }
                        if (c2 == '$' && !bl2) {
                            bl = true;
                        }
                        if (c2 == '|' && !bl2) {
                            arrayList2.add(string3.substring(i, j));
                            i = j + 1;
                            continue;
                        }
                        if (c2 != ']' && c2 != ' ' || bl2) continue;
                        arrayList2.add(string3.substring(i, j));
                        i = j - 1;
                        break;
                    }
                    ++n;
                }
                n = 0;
                n2 = 0;
                list.add(arrayList);
                arrayList = new ArrayList(20);
            }
            bufferedReader.close();
            boardMap.put(string, list);
            return list;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return Collections.emptyList();
        }
    }

    static {
        slideInTimeline = new Timeline();
        slideOutTimeline = new Timeline();
        hideAfterSlideOut = false;
        KEY_REPEAT_DELAY = 400.0;
        KEY_REPEAT_DELAY_MIN = 100.0;
        KEY_REPEAT_DELAY_MAX = 1000.0;
        KEY_REPEAT_RATE = 25.0;
        KEY_REPEAT_RATE_MIN = 2.0;
        KEY_REPEAT_RATE_MAX = 50.0;
        vkAdjustWindow = false;
        vkLookup = false;
        AccessController.doPrivileged(() -> {
            Double d;
            String string = System.getProperty("com.sun.javafx.vk.adjustwindow");
            if (string != null) {
                vkAdjustWindow = Boolean.valueOf(string);
            }
            if ((string = System.getProperty("com.sun.javafx.sqe.vk.lookup")) != null) {
                vkLookup = Boolean.valueOf(string);
            }
            if ((string = System.getProperty("com.sun.javafx.virtualKeyboard.backspaceRepeatDelay")) != null) {
                d = Double.valueOf(string);
                KEY_REPEAT_DELAY = Math.min(Math.max(d, KEY_REPEAT_DELAY_MIN), KEY_REPEAT_DELAY_MAX);
            }
            if ((string = System.getProperty("com.sun.javafx.virtualKeyboard.backspaceRepeatRate")) != null) {
                d = Double.valueOf(string);
                KEY_REPEAT_RATE = d <= 0.0 ? 0.0 : Math.min(Math.max(d, KEY_REPEAT_RATE_MIN), KEY_REPEAT_RATE_MAX);
            }
            return null;
        });
        winY = new SimpleDoubleProperty();
        winY.addListener(observable -> {
            if (vkPopup != null) {
                vkPopup.setY(winY.get());
            }
        });
    }

    private class KeyboardStateKey
    extends Key {
        private final String defaultText;
        private final String toggledText;

        private KeyboardStateKey(String string, String string2, String string3) {
            this.defaultText = string;
            this.toggledText = string2;
            this.text.setText(this.defaultText);
            if (vkLookup && string3 != null) {
                this.setId(string3);
            }
            this.getStyleClass().add((Object)"special");
        }

        @Override
        public void update(boolean bl, boolean bl2, boolean bl3) {
            if (bl3) {
                this.text.setText(this.toggledText);
            } else {
                this.text.setText(this.defaultText);
            }
        }
    }

    private class KeyCodeKey
    extends SuperKey {
        private KeyCode code;

        private KeyCodeKey(String string, String string2, KeyCode keyCode) {
            super(string, string2);
            this.code = keyCode;
            if (vkLookup) {
                this.setId(string);
            }
        }

        @Override
        protected void sendKeyEvents() {
            Node node = FXVKSkin.this.fxvk.getAttachedNode();
            if (node instanceof EventTarget) {
                node.fireEvent((Event)new KeyEvent(KeyEvent.KEY_PRESSED, KeyEvent.CHAR_UNDEFINED, this.chars, this.code, FXVKSkin.this.shiftDown, false, false, false));
                node.fireEvent((Event)new KeyEvent(KeyEvent.KEY_TYPED, this.chars, "", KeyCode.UNDEFINED, FXVKSkin.this.shiftDown, false, false, false));
                node.fireEvent((Event)new KeyEvent(KeyEvent.KEY_RELEASED, KeyEvent.CHAR_UNDEFINED, this.chars, this.code, FXVKSkin.this.shiftDown, false, false, false));
            }
        }
    }

    private class SuperKey
    extends TextInputKey {
        private SuperKey(String string, String string2) {
            this.chars = string2;
            this.text.setText(string);
            this.getStyleClass().add((Object)"special");
            if (vkLookup) {
                this.setId(string);
            }
        }
    }

    private class CharKey
    extends TextInputKey {
        private final String letterChars;
        private final String altChars;
        private final String[] moreChars;

        private CharKey(String string, String string2, String[] arrstring, String string3) {
            this.letterChars = string;
            this.altChars = string2;
            this.moreChars = arrstring;
            this.chars = this.letterChars;
            this.text.setText(this.chars);
            this.altText.setText(this.altChars);
            if (vkLookup) {
                this.setId((string3 != null ? string3 : this.chars).replaceAll("\\.", ""));
            }
        }

        private CharKey(String string, String string2, String[] arrstring) {
            this(string, string2, arrstring, (String)null);
        }

        @Override
        protected void press() {
            super.press();
            if (this.letterChars.equals(this.altChars) && this.moreChars == null) {
                return;
            }
            if (FXVKSkin.this.fxvk == primaryVK) {
                FXVKSkin.this.showSecondaryVK(null);
                secondaryVKKey = this;
                secondaryVKDelay.playFromStart();
            }
        }

        @Override
        protected void release() {
            super.release();
            if (this.letterChars.equals(this.altChars) && this.moreChars == null) {
                return;
            }
            if (FXVKSkin.this.fxvk == primaryVK) {
                secondaryVKDelay.stop();
            }
        }

        @Override
        public void update(boolean bl, boolean bl2, boolean bl3) {
            if (bl3) {
                this.chars = this.altChars;
                this.text.setText(this.chars);
                if (this.moreChars != null && this.moreChars.length > 0 && !Character.isLetter(this.moreChars[0].charAt(0))) {
                    this.altText.setText(this.moreChars[0]);
                } else {
                    this.altText.setText(null);
                }
            } else {
                this.chars = bl || bl2 ? this.letterChars.toUpperCase() : this.letterChars.toLowerCase();
                this.text.setText(this.chars);
                this.altText.setText(this.altChars);
            }
        }
    }

    private class TextInputKey
    extends Key {
        String chars;

        private TextInputKey() {
            this.chars = "";
        }

        @Override
        protected void press() {
        }

        @Override
        protected void release() {
            if (FXVKSkin.this.fxvk != secondaryVK && secondaryPopup != null && secondaryPopup.isShowing()) {
                return;
            }
            this.sendKeyEvents();
            if (FXVKSkin.this.fxvk == secondaryVK) {
                FXVKSkin.this.showSecondaryVK(null);
            }
            super.release();
        }

        protected void sendKeyEvents() {
            Node node = FXVKSkin.this.fxvk.getAttachedNode();
            if (node instanceof EventTarget && this.chars != null) {
                node.fireEvent((Event)new KeyEvent(KeyEvent.KEY_TYPED, this.chars, "", KeyCode.UNDEFINED, FXVKSkin.this.shiftDown, false, false, false));
            }
        }
    }

    private class Key
    extends Region {
        int col = 0;
        int colSpan = 1;
        protected final Text text;
        protected final Text altText;
        protected final Region icon = new Region();

        protected Key() {
            this.text = new Text();
            this.text.setTextOrigin(VPos.TOP);
            this.altText = new Text();
            this.altText.setTextOrigin(VPos.TOP);
            this.getChildren().setAll((Object[])new Node[]{this.text, this.altText, this.icon});
            this.getStyleClass().setAll((Object[])new String[]{"key"});
            this.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    this.press();
                }
            });
            this.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    this.release();
                }
            });
        }

        protected void press() {
        }

        protected void release() {
            FXVKSkin.this.clearShift();
        }

        public void update(boolean bl, boolean bl2, boolean bl3) {
        }

        protected void layoutChildren() {
            double d = this.snappedLeftInset();
            double d2 = this.snappedTopInset();
            double d3 = this.getWidth() - d - this.snappedRightInset();
            double d4 = this.getHeight() - d2 - this.snappedBottomInset();
            this.text.setVisible(this.icon.getBackground() == null);
            double d5 = this.text.prefWidth(-1.0);
            double d6 = this.text.prefHeight(-1.0);
            this.text.resizeRelocate((double)((int)(d + (d3 - d5) / 2.0 + 0.5)), (double)((int)(d2 + (d4 - d6) / 2.0 + 0.5)), (double)((int)d5), (double)((int)d6));
            this.altText.setVisible(this.icon.getBackground() == null && this.altText.getText().length() > 0);
            d5 = this.altText.prefWidth(-1.0);
            d6 = this.altText.prefHeight(-1.0);
            this.altText.resizeRelocate((double)((int)d) + (d3 - d5) + 0.5, (double)((int)(d2 + (d4 - d6) / 2.0 + 0.5 - d4 / 2.0)), (double)((int)d5), (double)((int)d6));
            this.icon.resizeRelocate(d - 8.0, d2 - 8.0, d3 + 16.0, d4 + 16.0);
        }
    }
}

