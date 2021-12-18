/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Platform
 *  javafx.beans.InvalidationListener
 *  javafx.beans.value.ObservableValue
 *  javafx.geometry.HPos
 *  javafx.geometry.NodeOrientation
 *  javafx.geometry.Orientation
 *  javafx.geometry.Pos
 *  javafx.geometry.VPos
 *  javafx.scene.AccessibleAttribute
 *  javafx.scene.Node
 *  javafx.scene.Scene
 *  javafx.scene.control.ContentDisplay
 *  javafx.scene.control.Control
 *  javafx.scene.control.Label
 *  javafx.scene.control.Labeled
 *  javafx.scene.control.OverrunStyle
 *  javafx.scene.image.ImageView
 *  javafx.scene.input.KeyCombination
 *  javafx.scene.input.Mnemonic
 *  javafx.scene.shape.Line
 *  javafx.scene.shape.Rectangle
 *  javafx.scene.text.Font
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.TextBinding;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.LabeledText;
import com.sun.javafx.scene.control.skin.Utils;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public abstract class LabeledSkinBase<C extends Labeled, B extends BehaviorBase<C>>
extends BehaviorSkinBase<C, B> {
    LabeledText text;
    boolean invalidText = true;
    Node graphic;
    double textWidth = Double.NEGATIVE_INFINITY;
    double ellipsisWidth = Double.NEGATIVE_INFINITY;
    final InvalidationListener graphicPropertyChangedListener = observable -> {
        this.invalidText = true;
        if (this.getSkinnable() != null) {
            ((Labeled)this.getSkinnable()).requestLayout();
        }
    };
    private Rectangle textClip;
    private double wrapWidth;
    private double wrapHeight;
    public TextBinding bindings;
    Line mnemonic_underscore;
    private boolean containsMnemonic = false;
    private Scene mnemonicScene = null;
    private KeyCombination mnemonicCode;
    private Node labeledNode = null;

    public LabeledSkinBase(C c, B b) {
        super(c, b);
        this.text = new LabeledText((Labeled)c);
        this.updateChildren();
        this.registerChangeListener((ObservableValue<?>)c.ellipsisStringProperty(), "ELLIPSIS_STRING");
        this.registerChangeListener((ObservableValue<?>)c.widthProperty(), "WIDTH");
        this.registerChangeListener((ObservableValue<?>)c.heightProperty(), "HEIGHT");
        this.registerChangeListener((ObservableValue<?>)c.textFillProperty(), "TEXT_FILL");
        this.registerChangeListener((ObservableValue<?>)c.fontProperty(), "FONT");
        this.registerChangeListener((ObservableValue<?>)c.graphicProperty(), "GRAPHIC");
        this.registerChangeListener((ObservableValue<?>)c.contentDisplayProperty(), "CONTENT_DISPLAY");
        this.registerChangeListener((ObservableValue<?>)c.labelPaddingProperty(), "LABEL_PADDING");
        this.registerChangeListener((ObservableValue<?>)c.graphicTextGapProperty(), "GRAPHIC_TEXT_GAP");
        this.registerChangeListener((ObservableValue<?>)c.alignmentProperty(), "ALIGNMENT");
        this.registerChangeListener((ObservableValue<?>)c.mnemonicParsingProperty(), "MNEMONIC_PARSING");
        this.registerChangeListener((ObservableValue<?>)c.textProperty(), "TEXT");
        this.registerChangeListener((ObservableValue<?>)c.textAlignmentProperty(), "TEXT_ALIGNMENT");
        this.registerChangeListener((ObservableValue<?>)c.textOverrunProperty(), "TEXT_OVERRUN");
        this.registerChangeListener((ObservableValue<?>)c.wrapTextProperty(), "WRAP_TEXT");
        this.registerChangeListener((ObservableValue<?>)c.underlineProperty(), "UNDERLINE");
        this.registerChangeListener((ObservableValue<?>)c.lineSpacingProperty(), "LINE_SPACING");
        this.registerChangeListener((ObservableValue<?>)c.sceneProperty(), "SCENE");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("WIDTH".equals(string)) {
            this.updateWrappingWidth();
            this.invalidText = true;
        } else if ("HEIGHT".equals(string)) {
            this.invalidText = true;
        } else if ("FONT".equals(string)) {
            this.textMetricsChanged();
            this.invalidateWidths();
            this.ellipsisWidth = Double.NEGATIVE_INFINITY;
        } else if ("GRAPHIC".equals(string)) {
            this.updateChildren();
            this.textMetricsChanged();
        } else if ("CONTENT_DISPLAY".equals(string)) {
            this.updateChildren();
            this.textMetricsChanged();
        } else if ("LABEL_PADDING".equals(string)) {
            this.textMetricsChanged();
        } else if ("GRAPHIC_TEXT_GAP".equals(string)) {
            this.textMetricsChanged();
        } else if ("ALIGNMENT".equals(string)) {
            ((Labeled)this.getSkinnable()).requestLayout();
        } else if ("MNEMONIC_PARSING".equals(string)) {
            this.containsMnemonic = false;
            this.textMetricsChanged();
        } else if ("TEXT".equals(string)) {
            this.updateChildren();
            this.textMetricsChanged();
            this.invalidateWidths();
        } else if (!"TEXT_ALIGNMENT".equals(string)) {
            if ("TEXT_OVERRUN".equals(string)) {
                this.textMetricsChanged();
            } else if ("ELLIPSIS_STRING".equals(string)) {
                this.textMetricsChanged();
                this.invalidateWidths();
                this.ellipsisWidth = Double.NEGATIVE_INFINITY;
            } else if ("WRAP_TEXT".equals(string)) {
                this.updateWrappingWidth();
                this.textMetricsChanged();
            } else if ("UNDERLINE".equals(string)) {
                this.textMetricsChanged();
            } else if ("LINE_SPACING".equals(string)) {
                this.textMetricsChanged();
            } else if ("SCENE".equals(string)) {
                this.sceneChanged();
            }
        }
    }

    protected double topLabelPadding() {
        return this.snapSize(((Labeled)this.getSkinnable()).getLabelPadding().getTop());
    }

    protected double bottomLabelPadding() {
        return this.snapSize(((Labeled)this.getSkinnable()).getLabelPadding().getBottom());
    }

    protected double leftLabelPadding() {
        return this.snapSize(((Labeled)this.getSkinnable()).getLabelPadding().getLeft());
    }

    protected double rightLabelPadding() {
        return this.snapSize(((Labeled)this.getSkinnable()).getLabelPadding().getRight());
    }

    private void textMetricsChanged() {
        this.invalidText = true;
        ((Labeled)this.getSkinnable()).requestLayout();
    }

    protected void mnemonicTargetChanged() {
        if (this.containsMnemonic) {
            this.removeMnemonic();
            Control control = this.getSkinnable();
            if (control instanceof Label) {
                this.labeledNode = ((Label)control).getLabelFor();
                this.addMnemonic();
            } else {
                this.labeledNode = null;
            }
        }
    }

    private void sceneChanged() {
        Labeled labeled = (Labeled)this.getSkinnable();
        Scene scene = labeled.getScene();
        if (scene != null && this.containsMnemonic) {
            this.addMnemonic();
        }
    }

    private void invalidateWidths() {
        this.textWidth = Double.NEGATIVE_INFINITY;
    }

    void updateDisplayedText() {
        this.updateDisplayedText(-1.0, -1.0);
    }

    private void updateDisplayedText(double d, double d2) {
        if (this.invalidText) {
            String string;
            int n;
            Labeled labeled = (Labeled)this.getSkinnable();
            String string2 = labeled.getText();
            int n2 = -1;
            if (string2 != null && string2.length() > 0) {
                this.bindings = new TextBinding(string2);
                if (!PlatformUtil.isMac() && ((Labeled)this.getSkinnable()).isMnemonicParsing()) {
                    this.labeledNode = labeled instanceof Label ? ((Label)labeled).getLabelFor() : labeled;
                    if (this.labeledNode == null) {
                        this.labeledNode = labeled;
                    }
                    n2 = this.bindings.getMnemonicIndex();
                }
            }
            if (this.containsMnemonic) {
                if (this.mnemonicScene != null && (n2 == -1 || this.bindings != null && !this.bindings.getMnemonicKeyCombination().equals((Object)this.mnemonicCode))) {
                    this.removeMnemonic();
                    this.containsMnemonic = false;
                }
            } else {
                this.removeMnemonic();
            }
            if (string2 != null && string2.length() > 0 && n2 >= 0 && !this.containsMnemonic) {
                this.containsMnemonic = true;
                this.mnemonicCode = this.bindings.getMnemonicKeyCombination();
                this.addMnemonic();
            }
            if (this.containsMnemonic) {
                string2 = this.bindings.getText();
                if (this.mnemonic_underscore == null) {
                    this.mnemonic_underscore = new Line();
                    this.mnemonic_underscore.setStartX(0.0);
                    this.mnemonic_underscore.setStartY(0.0);
                    this.mnemonic_underscore.setEndY(0.0);
                    this.mnemonic_underscore.getStyleClass().clear();
                    this.mnemonic_underscore.getStyleClass().setAll((Object[])new String[]{"mnemonic-underline"});
                }
                if (!this.getChildren().contains((Object)this.mnemonic_underscore)) {
                    this.getChildren().add((Object)this.mnemonic_underscore);
                }
            } else {
                string2 = ((Labeled)this.getSkinnable()).isMnemonicParsing() && PlatformUtil.isMac() && this.bindings != null ? this.bindings.getText() : labeled.getText();
                if (this.mnemonic_underscore != null && this.getChildren().contains((Object)this.mnemonic_underscore)) {
                    Platform.runLater(() -> {
                        this.getChildren().remove((Object)this.mnemonic_underscore);
                        this.mnemonic_underscore = null;
                    });
                }
            }
            int n3 = string2 != null ? string2.length() : 0;
            boolean bl = false;
            if (string2 != null && n3 > 0 && (n = string2.indexOf(10)) > -1 && n < n3 - 1) {
                bl = true;
            }
            boolean bl2 = labeled.getContentDisplay() == ContentDisplay.LEFT || labeled.getContentDisplay() == ContentDisplay.RIGHT;
            double d3 = labeled.getWidth() - this.snappedLeftInset() - this.leftLabelPadding() - this.snappedRightInset() - this.rightLabelPadding();
            d3 = Math.max(d3, 0.0);
            if (d == -1.0) {
                d = d3;
            }
            double d4 = Math.min(this.computeMinLabeledPartWidth(-1.0, this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset()), d3);
            if (bl2 && !this.isIgnoreGraphic()) {
                double d5 = labeled.getGraphic().getLayoutBounds().getWidth() + labeled.getGraphicTextGap();
                d -= d5;
                d4 -= d5;
            }
            this.wrapWidth = Math.max(d4, d);
            boolean bl3 = labeled.getContentDisplay() == ContentDisplay.TOP || labeled.getContentDisplay() == ContentDisplay.BOTTOM;
            double d6 = labeled.getHeight() - this.snappedTopInset() - this.topLabelPadding() - this.snappedBottomInset() - this.bottomLabelPadding();
            d6 = Math.max(d6, 0.0);
            if (d2 == -1.0) {
                d2 = d6;
            }
            double d7 = Math.min(this.computeMinLabeledPartHeight(this.wrapWidth, this.snappedTopInset(), this.snappedRightInset(), this.snappedBottomInset(), this.snappedLeftInset()), d6);
            if (bl3 && labeled.getGraphic() != null) {
                double d8 = labeled.getGraphic().getLayoutBounds().getHeight() + labeled.getGraphicTextGap();
                d2 -= d8;
                d7 -= d8;
            }
            this.wrapHeight = Math.max(d7, d2);
            this.updateWrappingWidth();
            Font font = this.text.getFont();
            OverrunStyle overrunStyle = labeled.getTextOverrun();
            String string3 = labeled.getEllipsisString();
            if (labeled.isWrapText()) {
                string = Utils.computeClippedWrappedText(font, string2, this.wrapWidth, this.wrapHeight, overrunStyle, string3, this.text.getBoundsType());
            } else if (bl) {
                StringBuilder stringBuilder = new StringBuilder();
                String[] arrstring = string2.split("\n");
                for (int i = 0; i < arrstring.length; ++i) {
                    stringBuilder.append(Utils.computeClippedText(font, arrstring[i], this.wrapWidth, overrunStyle, string3));
                    if (i >= arrstring.length - 1) continue;
                    stringBuilder.append('\n');
                }
                string = stringBuilder.toString();
            } else {
                string = Utils.computeClippedText(font, string2, this.wrapWidth, overrunStyle, string3);
            }
            if (string != null && string.endsWith("\n")) {
                string = string.substring(0, string.length() - 1);
            }
            this.text.setText(string);
            this.updateWrappingWidth();
            this.invalidText = false;
        }
    }

    private void addMnemonic() {
        if (this.labeledNode != null) {
            this.mnemonicScene = this.labeledNode.getScene();
            if (this.mnemonicScene != null) {
                this.mnemonicScene.addMnemonic(new Mnemonic(this.labeledNode, this.mnemonicCode));
            }
        }
    }

    private void removeMnemonic() {
        if (this.mnemonicScene != null && this.labeledNode != null) {
            this.mnemonicScene.removeMnemonic(new Mnemonic(this.labeledNode, this.mnemonicCode));
            this.mnemonicScene = null;
        }
    }

    private void updateWrappingWidth() {
        Labeled labeled = (Labeled)this.getSkinnable();
        this.text.setWrappingWidth(0.0);
        if (labeled.isWrapText()) {
            double d = Math.min(this.text.prefWidth(-1.0), this.wrapWidth);
            this.text.setWrappingWidth(d);
        }
    }

    protected void updateChildren() {
        Labeled labeled = (Labeled)this.getSkinnable();
        if (this.graphic != null) {
            this.graphic.layoutBoundsProperty().removeListener(this.graphicPropertyChangedListener);
        }
        this.graphic = labeled.getGraphic();
        if (this.graphic instanceof ImageView) {
            this.graphic.setMouseTransparent(true);
        }
        if (this.isIgnoreGraphic()) {
            if (labeled.getContentDisplay() == ContentDisplay.GRAPHIC_ONLY) {
                this.getChildren().clear();
            } else {
                this.getChildren().setAll((Object[])new Node[]{this.text});
            }
        } else {
            this.graphic.layoutBoundsProperty().addListener(this.graphicPropertyChangedListener);
            if (this.isIgnoreText()) {
                this.getChildren().setAll((Object[])new Node[]{this.graphic});
            } else {
                this.getChildren().setAll((Object[])new Node[]{this.graphic, this.text});
            }
            this.graphic.impl_processCSS(false);
        }
    }

    protected boolean isIgnoreGraphic() {
        return this.graphic == null || !this.graphic.isManaged() || ((Labeled)this.getSkinnable()).getContentDisplay() == ContentDisplay.TEXT_ONLY;
    }

    protected boolean isIgnoreText() {
        Labeled labeled = (Labeled)this.getSkinnable();
        String string = labeled.getText();
        return string == null || string.equals("") || labeled.getContentDisplay() == ContentDisplay.GRAPHIC_ONLY;
    }

    protected double computeMinWidth(double d, double d2, double d3, double d4, double d5) {
        return this.computeMinLabeledPartWidth(d, d2, d3, d4, d5);
    }

    private double computeMinLabeledPartWidth(double d, double d2, double d3, double d4, double d5) {
        boolean bl;
        Labeled labeled = (Labeled)this.getSkinnable();
        ContentDisplay contentDisplay = labeled.getContentDisplay();
        double d6 = labeled.getGraphicTextGap();
        double d7 = 0.0;
        Font font = this.text.getFont();
        OverrunStyle overrunStyle = labeled.getTextOverrun();
        String string = labeled.getEllipsisString();
        String string2 = labeled.getText();
        boolean bl2 = bl = string2 == null || string2.isEmpty();
        if (!bl) {
            if (overrunStyle == OverrunStyle.CLIP) {
                if (this.textWidth == Double.NEGATIVE_INFINITY) {
                    this.textWidth = Utils.computeTextWidth(font, string2.substring(0, 1), 0.0);
                }
                d7 = this.textWidth;
            } else {
                if (this.textWidth == Double.NEGATIVE_INFINITY) {
                    this.textWidth = Utils.computeTextWidth(font, string2, 0.0);
                }
                if (this.ellipsisWidth == Double.NEGATIVE_INFINITY) {
                    this.ellipsisWidth = Utils.computeTextWidth(font, string, 0.0);
                }
                d7 = Math.min(this.textWidth, this.ellipsisWidth);
            }
        }
        Node node = labeled.getGraphic();
        double d8 = this.isIgnoreGraphic() ? d7 : (this.isIgnoreText() ? node.minWidth(-1.0) : (contentDisplay == ContentDisplay.LEFT || contentDisplay == ContentDisplay.RIGHT ? d7 + node.minWidth(-1.0) + d6 : Math.max(d7, node.minWidth(-1.0))));
        return d8 + d5 + this.leftLabelPadding() + d3 + this.rightLabelPadding();
    }

    protected double computeMinHeight(double d, double d2, double d3, double d4, double d5) {
        return this.computeMinLabeledPartHeight(d, d2, d3, d4, d5);
    }

    private double computeMinLabeledPartHeight(double d, double d2, double d3, double d4, double d5) {
        double d6;
        int n;
        Labeled labeled = (Labeled)this.getSkinnable();
        Font font = this.text.getFont();
        String string = labeled.getText();
        if (string != null && string.length() > 0 && (n = string.indexOf(10)) >= 0) {
            string = string.substring(0, n);
        }
        double d7 = labeled.getLineSpacing();
        double d8 = d6 = Utils.computeTextHeight(font, string, 0.0, d7, this.text.getBoundsType());
        if (!this.isIgnoreGraphic()) {
            Node node = labeled.getGraphic();
            d8 = labeled.getContentDisplay() == ContentDisplay.TOP || labeled.getContentDisplay() == ContentDisplay.BOTTOM ? node.minHeight(d) + labeled.getGraphicTextGap() + d6 : Math.max(d6, node.minHeight(d));
        }
        return d2 + d8 + d4 + this.topLabelPadding() - this.bottomLabelPadding();
    }

    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        Labeled labeled = (Labeled)this.getSkinnable();
        Font font = this.text.getFont();
        String string = labeled.getText();
        boolean bl = string == null || string.isEmpty();
        double d6 = d5 + this.leftLabelPadding() + d3 + this.rightLabelPadding();
        double d7 = bl ? 0.0 : Utils.computeTextWidth(font, string, 0.0);
        double d8 = this.graphic == null ? 0.0 : Utils.boundedSize(this.graphic.prefWidth(-1.0), this.graphic.minWidth(-1.0), this.graphic.maxWidth(-1.0));
        Node node = labeled.getGraphic();
        if (this.isIgnoreGraphic()) {
            return d7 + d6;
        }
        if (this.isIgnoreText()) {
            return d8 + d6;
        }
        if (labeled.getContentDisplay() == ContentDisplay.LEFT || labeled.getContentDisplay() == ContentDisplay.RIGHT) {
            return d7 + labeled.getGraphicTextGap() + d8 + d6;
        }
        return Math.max(d7, d8) + d6;
    }

    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        double d6;
        Labeled labeled = (Labeled)this.getSkinnable();
        Font font = this.text.getFont();
        ContentDisplay contentDisplay = labeled.getContentDisplay();
        double d7 = labeled.getGraphicTextGap();
        d -= d5 + this.leftLabelPadding() + d3 + this.rightLabelPadding();
        String string = labeled.getText();
        if (string != null && string.endsWith("\n")) {
            string = string.substring(0, string.length() - 1);
        }
        double d8 = d;
        if (!(this.isIgnoreGraphic() || contentDisplay != ContentDisplay.LEFT && contentDisplay != ContentDisplay.RIGHT)) {
            d8 -= this.graphic.prefWidth(-1.0) + d7;
        }
        double d9 = d6 = Utils.computeTextHeight(font, string, labeled.isWrapText() ? d8 : 0.0, labeled.getLineSpacing(), this.text.getBoundsType());
        if (!this.isIgnoreGraphic()) {
            Node node = labeled.getGraphic();
            d9 = contentDisplay == ContentDisplay.TOP || contentDisplay == ContentDisplay.BOTTOM ? node.prefHeight(d) + d7 + d6 : Math.max(d6, node.prefHeight(d));
        }
        return d2 + d9 + d4 + this.topLabelPadding() + this.bottomLabelPadding();
    }

    protected double computeMaxWidth(double d, double d2, double d3, double d4, double d5) {
        return ((Labeled)this.getSkinnable()).prefWidth(d);
    }

    protected double computeMaxHeight(double d, double d2, double d3, double d4, double d5) {
        return ((Labeled)this.getSkinnable()).prefHeight(d);
    }

    public double computeBaselineOffset(double d, double d2, double d3, double d4) {
        double d5;
        double d6 = d5 = this.text.getBaselineOffset();
        Labeled labeled = (Labeled)this.getSkinnable();
        Node node = labeled.getGraphic();
        if (!this.isIgnoreGraphic()) {
            ContentDisplay contentDisplay = labeled.getContentDisplay();
            if (contentDisplay == ContentDisplay.TOP) {
                d6 = node.prefHeight(-1.0) + labeled.getGraphicTextGap() + d5;
            } else if (contentDisplay == ContentDisplay.LEFT || contentDisplay == ContentDisplay.RIGHT) {
                d6 = d5 + (node.prefHeight(-1.0) - this.text.prefHeight(-1.0)) / 2.0;
            }
        }
        return d + this.topLabelPadding() + d6;
    }

    protected void layoutChildren(double d, double d2, double d3, double d4) {
        this.layoutLabelInArea(d, d2, d3, d4);
    }

    protected void layoutLabelInArea(double d, double d2, double d3, double d4) {
        this.layoutLabelInArea(d, d2, d3, d4, null);
    }

    protected void layoutLabelInArea(double d, double d2, double d3, double d4, Pos pos) {
        double d5;
        double d6;
        double d7;
        double d8;
        Labeled labeled = (Labeled)this.getSkinnable();
        ContentDisplay contentDisplay = labeled.getContentDisplay();
        if (pos == null) {
            pos = labeled.getAlignment();
        }
        HPos hPos = pos == null ? HPos.LEFT : pos.getHpos();
        VPos vPos = pos == null ? VPos.CENTER : pos.getVpos();
        boolean bl = this.isIgnoreGraphic();
        boolean bl2 = this.isIgnoreText();
        d += this.leftLabelPadding();
        d2 += this.topLabelPadding();
        d3 -= this.leftLabelPadding() + this.rightLabelPadding();
        d4 -= this.topLabelPadding() + this.bottomLabelPadding();
        if (bl) {
            d8 = 0.0;
            d7 = 0.0;
        } else if (bl2) {
            if (this.graphic.isResizable()) {
                Orientation orientation = this.graphic.getContentBias();
                if (orientation == Orientation.HORIZONTAL) {
                    d7 = Utils.boundedSize(d3, this.graphic.minWidth(-1.0), this.graphic.maxWidth(-1.0));
                    d8 = Utils.boundedSize(d4, this.graphic.minHeight(d7), this.graphic.maxHeight(d7));
                } else if (orientation == Orientation.VERTICAL) {
                    d8 = Utils.boundedSize(d4, this.graphic.minHeight(-1.0), this.graphic.maxHeight(-1.0));
                    d7 = Utils.boundedSize(d3, this.graphic.minWidth(d8), this.graphic.maxWidth(d8));
                } else {
                    d7 = Utils.boundedSize(d3, this.graphic.minWidth(-1.0), this.graphic.maxWidth(-1.0));
                    d8 = Utils.boundedSize(d4, this.graphic.minHeight(-1.0), this.graphic.maxHeight(-1.0));
                }
                this.graphic.resize(d7, d8);
            } else {
                d7 = this.graphic.getLayoutBounds().getWidth();
                d8 = this.graphic.getLayoutBounds().getHeight();
            }
        } else {
            this.graphic.autosize();
            d7 = this.graphic.getLayoutBounds().getWidth();
            d8 = this.graphic.getLayoutBounds().getHeight();
        }
        if (bl2) {
            d6 = 0.0;
            d5 = 0.0;
            this.text.setText("");
        } else {
            this.updateDisplayedText(d3, d4);
            d5 = this.snapSize(Math.min(this.text.getLayoutBounds().getWidth(), this.wrapWidth));
            d6 = this.snapSize(Math.min(this.text.getLayoutBounds().getHeight(), this.wrapHeight));
        }
        double d9 = bl2 || bl ? 0.0 : labeled.getGraphicTextGap();
        double d10 = Math.max(d7, d5);
        double d11 = Math.max(d8, d6);
        if (contentDisplay == ContentDisplay.TOP || contentDisplay == ContentDisplay.BOTTOM) {
            d11 = d8 + d9 + d6;
        } else if (contentDisplay == ContentDisplay.LEFT || contentDisplay == ContentDisplay.RIGHT) {
            d10 = d7 + d9 + d5;
        }
        double d12 = hPos == HPos.LEFT ? d : (hPos == HPos.RIGHT ? d + (d3 - d10) : d + (d3 - d10) / 2.0);
        double d13 = vPos == VPos.TOP ? d2 : (vPos == VPos.BOTTOM ? d2 + (d4 - d11) : d2 + (d4 - d11) / 2.0);
        double d14 = 0.0;
        double d15 = 0.0;
        double d16 = 0.0;
        if (this.containsMnemonic) {
            Font font = this.text.getFont();
            String string = this.bindings.getText();
            d14 = Utils.computeTextWidth(font, string.substring(0, this.bindings.getMnemonicIndex()), 0.0);
            d15 = Utils.computeTextWidth(font, string.substring(this.bindings.getMnemonicIndex(), this.bindings.getMnemonicIndex() + 1), 0.0);
            d16 = Utils.computeTextHeight(font, "_", 0.0, this.text.getBoundsType());
        }
        if (!(bl && bl2 || this.text.isManaged())) {
            this.text.setManaged(true);
        }
        if (bl && bl2) {
            if (this.text.isManaged()) {
                this.text.setManaged(false);
            }
            this.text.relocate(this.snapPosition(d12), this.snapPosition(d13));
        } else if (bl) {
            this.text.relocate(this.snapPosition(d12), this.snapPosition(d13));
            if (this.containsMnemonic) {
                this.mnemonic_underscore.setEndX(d15 - 2.0);
                this.mnemonic_underscore.relocate(d12 + d14, d13 + d16 - 1.0);
            }
        } else if (bl2) {
            this.text.relocate(this.snapPosition(d12), this.snapPosition(d13));
            this.graphic.relocate(this.snapPosition(d12), this.snapPosition(d13));
            if (this.containsMnemonic) {
                this.mnemonic_underscore.setEndX(d15);
                this.mnemonic_underscore.setStrokeWidth(d16 / 10.0);
                this.mnemonic_underscore.relocate(d12 + d14, d13 + d16 - 1.0);
            }
        } else {
            double d17 = 0.0;
            double d18 = 0.0;
            double d19 = 0.0;
            double d20 = 0.0;
            if (contentDisplay == ContentDisplay.TOP) {
                d17 = d12 + (d10 - d7) / 2.0;
                d19 = d12 + (d10 - d5) / 2.0;
                d18 = d13;
                d20 = d18 + d8 + d9;
            } else if (contentDisplay == ContentDisplay.RIGHT) {
                d19 = d12;
                d17 = d19 + d5 + d9;
                d18 = d13 + (d11 - d8) / 2.0;
                d20 = d13 + (d11 - d6) / 2.0;
            } else if (contentDisplay == ContentDisplay.BOTTOM) {
                d17 = d12 + (d10 - d7) / 2.0;
                d19 = d12 + (d10 - d5) / 2.0;
                d20 = d13;
                d18 = d20 + d6 + d9;
            } else if (contentDisplay == ContentDisplay.LEFT) {
                d17 = d12;
                d19 = d17 + d7 + d9;
                d18 = d13 + (d11 - d8) / 2.0;
                d20 = d13 + (d11 - d6) / 2.0;
            } else if (contentDisplay == ContentDisplay.CENTER) {
                d17 = d12 + (d10 - d7) / 2.0;
                d19 = d12 + (d10 - d5) / 2.0;
                d18 = d13 + (d11 - d8) / 2.0;
                d20 = d13 + (d11 - d6) / 2.0;
            }
            this.text.relocate(this.snapPosition(d19), this.snapPosition(d20));
            if (this.containsMnemonic) {
                this.mnemonic_underscore.setEndX(d15);
                this.mnemonic_underscore.setStrokeWidth(d16 / 10.0);
                this.mnemonic_underscore.relocate(this.snapPosition(d19 + d14), this.snapPosition(d20 + d16 - 1.0));
            }
            this.graphic.relocate(this.snapPosition(d17), this.snapPosition(d18));
        }
        if (this.text != null && (this.text.getLayoutBounds().getHeight() > this.wrapHeight || this.text.getLayoutBounds().getWidth() > this.wrapWidth)) {
            if (this.textClip == null) {
                this.textClip = new Rectangle();
            }
            if (labeled.getEffectiveNodeOrientation() == NodeOrientation.LEFT_TO_RIGHT) {
                this.textClip.setX(this.text.getLayoutBounds().getMinX());
            } else {
                this.textClip.setX(this.text.getLayoutBounds().getMaxX() - this.wrapWidth);
            }
            this.textClip.setY(this.text.getLayoutBounds().getMinY());
            this.textClip.setWidth(this.wrapWidth);
            this.textClip.setHeight(this.wrapHeight);
            if (this.text.getClip() == null) {
                this.text.setClip((Node)this.textClip);
            }
        } else if (this.text.getClip() != null) {
            this.text.setClip(null);
        }
    }

    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case TEXT: {
                Object object;
                Labeled labeled = (Labeled)this.getSkinnable();
                String string = labeled.getAccessibleText();
                if (string != null && !string.isEmpty()) {
                    return string;
                }
                if (this.bindings != null && (object = this.bindings.getText()) != null && !((String)object).isEmpty()) {
                    return object;
                }
                if (labeled != null && (object = labeled.getText()) != null && !((String)object).isEmpty()) {
                    return object;
                }
                if (this.graphic != null && (object = this.graphic.queryAccessibleAttribute(AccessibleAttribute.TEXT, new Object[0])) != null) {
                    return object;
                }
                return null;
            }
            case MNEMONIC: {
                if (this.bindings != null) {
                    return this.bindings.getMnemonic();
                }
                return null;
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }
}

