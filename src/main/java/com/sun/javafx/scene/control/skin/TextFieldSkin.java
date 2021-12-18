/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.Observable
 *  javafx.beans.binding.BooleanBinding
 *  javafx.beans.binding.DoubleBinding
 *  javafx.beans.binding.ObjectBinding
 *  javafx.beans.binding.StringBinding
 *  javafx.beans.property.DoubleProperty
 *  javafx.beans.property.SimpleDoubleProperty
 *  javafx.beans.value.ObservableBooleanValue
 *  javafx.beans.value.ObservableDoubleValue
 *  javafx.beans.value.ObservableNumberValue
 *  javafx.beans.value.ObservableValue
 *  javafx.event.EventHandler
 *  javafx.geometry.Bounds
 *  javafx.geometry.HPos
 *  javafx.geometry.Point2D
 *  javafx.geometry.Rectangle2D
 *  javafx.scene.AccessibleAttribute
 *  javafx.scene.Group
 *  javafx.scene.Node
 *  javafx.scene.control.IndexRange
 *  javafx.scene.control.PasswordField
 *  javafx.scene.control.TextField
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.layout.Pane
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.Paint
 *  javafx.scene.shape.Path
 *  javafx.scene.shape.PathElement
 *  javafx.scene.shape.Rectangle
 *  javafx.scene.text.Text
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.PasswordFieldBehavior;
import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import com.sun.javafx.scene.control.skin.TextInputControlSkin;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.tk.FontMetrics;
import java.util.List;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class TextFieldSkin
extends TextInputControlSkin<TextField, TextFieldBehavior> {
    private Pane textGroup = new Pane();
    private Group handleGroup;
    private Rectangle clip = new Rectangle();
    private Text textNode = new Text();
    private Text promptNode;
    private Path selectionHighlightPath = new Path();
    private Path characterBoundingPath = new Path();
    private ObservableBooleanValue usePromptText;
    private DoubleProperty textTranslateX = new SimpleDoubleProperty((Object)this, "textTranslateX");
    private double caretWidth;
    protected ObservableDoubleValue textRight;
    private double pressX;
    private double pressY;
    public static final char BULLET = '\u25cf';

    protected int translateCaretPosition(int n) {
        return n;
    }

    protected Point2D translateCaretPosition(Point2D point2D) {
        return point2D;
    }

    public TextFieldSkin(TextField textField) {
        this(textField, textField instanceof PasswordField ? new PasswordFieldBehavior((PasswordField)textField) : new TextFieldBehavior(textField));
    }

    public TextFieldSkin(final TextField textField, TextFieldBehavior textFieldBehavior) {
        super(textField, textFieldBehavior);
        textFieldBehavior.setTextFieldSkin(this);
        textField.caretPositionProperty().addListener((observableValue, number, number2) -> {
            if (textField.getWidth() > 0.0) {
                this.updateTextNodeCaretPos(textField.getCaretPosition());
                if (!this.isForwardBias()) {
                    this.setForwardBias(true);
                }
                this.updateCaretOff();
            }
        });
        this.forwardBiasProperty().addListener(observable -> {
            if (textField.getWidth() > 0.0) {
                this.updateTextNodeCaretPos(textField.getCaretPosition());
                this.updateCaretOff();
            }
        });
        this.textRight = new DoubleBinding(){
            {
                this.bind(new Observable[]{TextFieldSkin.this.textGroup.widthProperty()});
            }

            protected double computeValue() {
                return TextFieldSkin.this.textGroup.getWidth();
            }
        };
        this.clip.setSmooth(false);
        this.clip.setX(0.0);
        this.clip.widthProperty().bind((ObservableValue)this.textGroup.widthProperty());
        this.clip.heightProperty().bind((ObservableValue)this.textGroup.heightProperty());
        this.textGroup.setClip((Node)this.clip);
        this.textGroup.getChildren().addAll((Object[])new Node[]{this.selectionHighlightPath, this.textNode, new Group(new Node[]{this.caretPath})});
        this.getChildren().add((Object)this.textGroup);
        if (SHOW_HANDLES) {
            this.handleGroup = new Group();
            this.handleGroup.setManaged(false);
            this.handleGroup.getChildren().addAll((Object[])new Node[]{this.caretHandle, this.selectionHandle1, this.selectionHandle2});
            this.getChildren().add((Object)this.handleGroup);
        }
        this.textNode.setManaged(false);
        this.textNode.getStyleClass().add((Object)"text");
        this.textNode.fontProperty().bind((ObservableValue)textField.fontProperty());
        this.textNode.layoutXProperty().bind((ObservableValue)this.textTranslateX);
        this.textNode.textProperty().bind((ObservableValue)new StringBinding(){
            {
                this.bind(new Observable[]{textField.textProperty()});
            }

            protected String computeValue() {
                return TextFieldSkin.this.maskText(textField.textProperty().getValueSafe());
            }
        });
        this.textNode.fillProperty().bind((ObservableValue)this.textFill);
        this.textNode.impl_selectionFillProperty().bind((ObservableValue)new ObjectBinding<Paint>(){
            {
                this.bind(new Observable[]{TextFieldSkin.this.highlightTextFill, TextFieldSkin.this.textFill, textField.focusedProperty()});
            }

            protected Paint computeValue() {
                return textField.isFocused() ? (Paint)TextFieldSkin.this.highlightTextFill.get() : (Paint)TextFieldSkin.this.textFill.get();
            }
        });
        this.updateTextNodeCaretPos(textField.getCaretPosition());
        textField.selectionProperty().addListener(observable -> this.updateSelection());
        this.selectionHighlightPath.setManaged(false);
        this.selectionHighlightPath.setStroke(null);
        this.selectionHighlightPath.layoutXProperty().bind((ObservableValue)this.textTranslateX);
        this.selectionHighlightPath.visibleProperty().bind((ObservableValue)textField.anchorProperty().isNotEqualTo((ObservableNumberValue)textField.caretPositionProperty()).and((ObservableBooleanValue)textField.focusedProperty()));
        this.selectionHighlightPath.fillProperty().bind((ObservableValue)this.highlightFill);
        this.textNode.impl_selectionShapeProperty().addListener(observable -> this.updateSelection());
        this.caretPath.setManaged(false);
        this.caretPath.setStrokeWidth(1.0);
        this.caretPath.fillProperty().bind((ObservableValue)this.textFill);
        this.caretPath.strokeProperty().bind((ObservableValue)this.textFill);
        this.caretPath.opacityProperty().bind((ObservableValue)new DoubleBinding(){
            {
                this.bind(new Observable[]{TextFieldSkin.this.caretVisible});
            }

            protected double computeValue() {
                return TextFieldSkin.this.caretVisible.get() ? 1.0 : 0.0;
            }
        });
        this.caretPath.layoutXProperty().bind((ObservableValue)this.textTranslateX);
        this.textNode.impl_caretShapeProperty().addListener(observable -> {
            this.caretPath.getElements().setAll((Object[])this.textNode.impl_caretShapeProperty().get());
            if (this.caretPath.getElements().size() == 0) {
                this.updateTextNodeCaretPos(textField.getCaretPosition());
            } else if (this.caretPath.getElements().size() != 4) {
                this.caretWidth = Math.round(this.caretPath.getLayoutBounds().getWidth());
            }
        });
        textField.fontProperty().addListener(observable -> {
            textField.requestLayout();
            ((TextField)this.getSkinnable()).requestLayout();
        });
        this.registerChangeListener((ObservableValue<?>)textField.prefColumnCountProperty(), "prefColumnCount");
        if (textField.isFocused()) {
            this.setCaretAnimating(true);
        }
        textField.alignmentProperty().addListener(observable -> {
            if (textField.getWidth() > 0.0) {
                this.updateTextPos();
                this.updateCaretOff();
                textField.requestLayout();
            }
        });
        this.usePromptText = new BooleanBinding(){
            {
                this.bind(new Observable[]{textField.textProperty(), textField.promptTextProperty(), TextFieldSkin.this.promptTextFill});
            }

            protected boolean computeValue() {
                String string = textField.getText();
                String string2 = textField.getPromptText();
                return (string == null || string.isEmpty()) && string2 != null && !string2.isEmpty() && !((Paint)TextFieldSkin.this.promptTextFill.get()).equals((Object)Color.TRANSPARENT);
            }
        };
        this.promptTextFill.addListener(observable -> this.updateTextPos());
        textField.textProperty().addListener(observable -> {
            if (!((TextFieldBehavior)this.getBehavior()).isEditing()) {
                this.updateTextPos();
            }
        });
        if (this.usePromptText.get()) {
            this.createPromptNode();
        }
        this.usePromptText.addListener(observable -> {
            this.createPromptNode();
            textField.requestLayout();
        });
        if (SHOW_HANDLES) {
            this.selectionHandle1.setRotate(180.0);
            EventHandler eventHandler = mouseEvent -> {
                this.pressX = mouseEvent.getX();
                this.pressY = mouseEvent.getY();
                mouseEvent.consume();
            };
            this.caretHandle.setOnMousePressed(eventHandler);
            this.selectionHandle1.setOnMousePressed(eventHandler);
            this.selectionHandle2.setOnMousePressed(eventHandler);
            this.caretHandle.setOnMouseDragged(mouseEvent -> {
                Point2D point2D = new Point2D(this.caretHandle.getLayoutX() + mouseEvent.getX() + this.pressX - this.textNode.getLayoutX(), this.caretHandle.getLayoutY() + mouseEvent.getY() - this.pressY - 6.0);
                HitInfo hitInfo = this.textNode.impl_hitTestChar(this.translateCaretPosition(point2D));
                this.positionCaret(hitInfo, false);
                mouseEvent.consume();
            });
            this.selectionHandle1.setOnMouseDragged((EventHandler)new EventHandler<MouseEvent>(){

                public void handle(MouseEvent mouseEvent) {
                    TextField textField = (TextField)TextFieldSkin.this.getSkinnable();
                    Point2D point2D = TextFieldSkin.this.textNode.localToScene(0.0, 0.0);
                    Point2D point2D2 = new Point2D(mouseEvent.getSceneX() - point2D.getX() + 10.0 - TextFieldSkin.this.pressX + TextFieldSkin.this.selectionHandle1.getWidth() / 2.0, mouseEvent.getSceneY() - point2D.getY() - TextFieldSkin.this.pressY - 6.0);
                    HitInfo hitInfo = TextFieldSkin.this.textNode.impl_hitTestChar(TextFieldSkin.this.translateCaretPosition(point2D2));
                    int n = hitInfo.getCharIndex();
                    if (textField.getAnchor() < textField.getCaretPosition()) {
                        textField.selectRange(textField.getCaretPosition(), textField.getAnchor());
                    }
                    if (n >= 0) {
                        if (n >= textField.getAnchor() - 1) {
                            hitInfo.setCharIndex(Math.max(0, textField.getAnchor() - 1));
                        }
                        TextFieldSkin.this.positionCaret(hitInfo, true);
                    }
                    mouseEvent.consume();
                }
            });
            this.selectionHandle2.setOnMouseDragged((EventHandler)new EventHandler<MouseEvent>(){

                public void handle(MouseEvent mouseEvent) {
                    TextField textField = (TextField)TextFieldSkin.this.getSkinnable();
                    Point2D point2D = TextFieldSkin.this.textNode.localToScene(0.0, 0.0);
                    Point2D point2D2 = new Point2D(mouseEvent.getSceneX() - point2D.getX() + 10.0 - TextFieldSkin.this.pressX + TextFieldSkin.this.selectionHandle2.getWidth() / 2.0, mouseEvent.getSceneY() - point2D.getY() - TextFieldSkin.this.pressY - 6.0);
                    HitInfo hitInfo = TextFieldSkin.this.textNode.impl_hitTestChar(TextFieldSkin.this.translateCaretPosition(point2D2));
                    int n = hitInfo.getCharIndex();
                    if (textField.getAnchor() > textField.getCaretPosition()) {
                        textField.selectRange(textField.getCaretPosition(), textField.getAnchor());
                    }
                    if (n > 0) {
                        if (n <= textField.getAnchor()) {
                            hitInfo.setCharIndex(Math.min(textField.getAnchor() + 1, textField.getLength()));
                        }
                        TextFieldSkin.this.positionCaret(hitInfo, true);
                    }
                    mouseEvent.consume();
                }
            });
        }
    }

    private void updateTextNodeCaretPos(int n) {
        if (n == 0 || this.isForwardBias()) {
            this.textNode.setImpl_caretPosition(n);
        } else {
            this.textNode.setImpl_caretPosition(n - 1);
        }
        this.textNode.impl_caretBiasProperty().set(this.isForwardBias());
    }

    private void createPromptNode() {
        if (this.promptNode != null || !this.usePromptText.get()) {
            return;
        }
        this.promptNode = new Text();
        this.textGroup.getChildren().add(0, (Object)this.promptNode);
        this.promptNode.setManaged(false);
        this.promptNode.getStyleClass().add((Object)"text");
        this.promptNode.visibleProperty().bind((ObservableValue)this.usePromptText);
        this.promptNode.fontProperty().bind((ObservableValue)((TextField)this.getSkinnable()).fontProperty());
        this.promptNode.textProperty().bind((ObservableValue)((TextField)this.getSkinnable()).promptTextProperty());
        this.promptNode.fillProperty().bind((ObservableValue)this.promptTextFill);
        this.updateSelection();
    }

    private void updateSelection() {
        TextField textField = (TextField)this.getSkinnable();
        IndexRange indexRange = textField.getSelection();
        if (indexRange == null || indexRange.getLength() == 0) {
            this.textNode.impl_selectionStartProperty().set(-1);
            this.textNode.impl_selectionEndProperty().set(-1);
        } else {
            this.textNode.impl_selectionStartProperty().set(indexRange.getStart());
            this.textNode.impl_selectionEndProperty().set(indexRange.getStart());
            this.textNode.impl_selectionEndProperty().set(indexRange.getEnd());
        }
        Object[] arrobject = (PathElement[])this.textNode.impl_selectionShapeProperty().get();
        if (arrobject == null) {
            this.selectionHighlightPath.getElements().clear();
        } else {
            this.selectionHighlightPath.getElements().setAll(arrobject);
        }
        if (SHOW_HANDLES && indexRange != null && indexRange.getLength() > 0) {
            int n = textField.getCaretPosition();
            int n2 = textField.getAnchor();
            this.updateTextNodeCaretPos(n2);
            Bounds bounds = this.caretPath.getBoundsInParent();
            if (n < n2) {
                this.selectionHandle2.setLayoutX(bounds.getMinX() - this.selectionHandle2.getWidth() / 2.0);
            } else {
                this.selectionHandle1.setLayoutX(bounds.getMinX() - this.selectionHandle1.getWidth() / 2.0);
            }
            this.updateTextNodeCaretPos(n);
            bounds = this.caretPath.getBoundsInParent();
            if (n < n2) {
                this.selectionHandle1.setLayoutX(bounds.getMinX() - this.selectionHandle1.getWidth() / 2.0);
            } else {
                this.selectionHandle2.setLayoutX(bounds.getMinX() - this.selectionHandle2.getWidth() / 2.0);
            }
        }
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        if ("prefColumnCount".equals(string)) {
            ((TextField)this.getSkinnable()).requestLayout();
        } else {
            super.handleControlPropertyChanged(string);
        }
    }

    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        TextField textField = (TextField)this.getSkinnable();
        double d6 = ((FontMetrics)this.fontMetrics.get()).computeStringWidth("W");
        int n = textField.getPrefColumnCount();
        return (double)n * d6 + d5 + d3;
    }

    protected double computeMinHeight(double d, double d2, double d3, double d4, double d5) {
        return this.computePrefHeight(d, d2, d3, d4, d5);
    }

    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        return d2 + this.textNode.getLayoutBounds().getHeight() + d4;
    }

    protected double computeMaxHeight(double d, double d2, double d3, double d4, double d5) {
        return ((TextField)this.getSkinnable()).prefHeight(d);
    }

    public double computeBaselineOffset(double d, double d2, double d3, double d4) {
        return d + this.textNode.getBaselineOffset();
    }

    private void updateTextPos() {
        double d = this.textTranslateX.get();
        double d2 = this.textNode.getLayoutBounds().getWidth();
        switch (this.getHAlignment()) {
            case CENTER: {
                double d3;
                double d4 = this.textRight.get() / 2.0;
                if (this.usePromptText.get()) {
                    d3 = d4 - this.promptNode.getLayoutBounds().getWidth() / 2.0;
                    this.promptNode.setLayoutX(d3);
                } else {
                    d3 = d4 - d2 / 2.0;
                }
                if (!(d3 + d2 <= this.textRight.get())) break;
                this.textTranslateX.set(d3);
                break;
            }
            case RIGHT: {
                double d5 = this.textRight.get() - d2 - this.caretWidth / 2.0;
                if (d5 > d || d5 > 0.0) {
                    this.textTranslateX.set(d5);
                }
                if (!this.usePromptText.get()) break;
                this.promptNode.setLayoutX(this.textRight.get() - this.promptNode.getLayoutBounds().getWidth() - this.caretWidth / 2.0);
                break;
            }
            default: {
                double d6 = this.caretWidth / 2.0;
                if (d6 < d || d6 + d2 <= this.textRight.get()) {
                    this.textTranslateX.set(d6);
                }
                if (!this.usePromptText.get()) break;
                this.promptNode.layoutXProperty().set(d6);
            }
        }
    }

    protected void updateCaretOff() {
        double d = 0.0;
        double d2 = this.caretPath.getLayoutBounds().getMinX() + this.textTranslateX.get();
        if (d2 < 0.0) {
            d = d2;
        } else if (d2 > this.textRight.get() - this.caretWidth) {
            d = d2 - (this.textRight.get() - this.caretWidth);
        }
        switch (this.getHAlignment()) {
            case CENTER: {
                this.textTranslateX.set(this.textTranslateX.get() - d);
                break;
            }
            case RIGHT: {
                this.textTranslateX.set(Math.max(this.textTranslateX.get() - d, this.textRight.get() - this.textNode.getLayoutBounds().getWidth() - this.caretWidth / 2.0));
                break;
            }
            default: {
                this.textTranslateX.set(Math.min(this.textTranslateX.get() - d, this.caretWidth / 2.0));
            }
        }
        if (SHOW_HANDLES) {
            this.caretHandle.setLayoutX(d2 - this.caretHandle.getWidth() / 2.0 + 1.0);
        }
    }

    public void replaceText(int n, int n2, String string) {
        double d = this.textNode.getBoundsInParent().getMaxX();
        double d2 = this.caretPath.getLayoutBounds().getMaxX() + this.textTranslateX.get();
        ((TextField)this.getSkinnable()).replaceText(n, n2, string);
        this.scrollAfterDelete(d, d2);
    }

    public void deleteChar(boolean bl) {
        boolean bl2;
        double d = this.textNode.getBoundsInParent().getMaxX();
        double d2 = this.caretPath.getLayoutBounds().getMaxX() + this.textTranslateX.get();
        boolean bl3 = bl ? !((TextField)this.getSkinnable()).deletePreviousChar() : (bl2 = !((TextField)this.getSkinnable()).deleteNextChar());
        if (!bl2) {
            this.scrollAfterDelete(d, d2);
        }
    }

    public void scrollAfterDelete(double d, double d2) {
        Bounds bounds = this.textNode.getLayoutBounds();
        Bounds bounds2 = this.textNode.localToParent(bounds);
        Bounds bounds3 = this.clip.getBoundsInParent();
        Bounds bounds4 = this.caretPath.getLayoutBounds();
        switch (this.getHAlignment()) {
            case RIGHT: {
                if (bounds2.getMaxX() > bounds3.getMaxX()) {
                    double d3 = d2 - bounds4.getMaxX() - this.textTranslateX.get();
                    if (bounds2.getMaxX() + d3 < bounds3.getMaxX()) {
                        d3 = d <= bounds3.getMaxX() ? d - bounds2.getMaxX() : bounds3.getMaxX() - bounds2.getMaxX();
                    }
                    this.textTranslateX.set(this.textTranslateX.get() + d3);
                    break;
                }
                this.updateTextPos();
                break;
            }
            default: {
                if (!(bounds2.getMinX() < bounds3.getMinX() + this.caretWidth / 2.0) || !(bounds2.getMaxX() <= bounds3.getMaxX())) break;
                double d4 = d2 - bounds4.getMaxX() - this.textTranslateX.get();
                if (bounds2.getMaxX() + d4 < bounds3.getMaxX()) {
                    d4 = d <= bounds3.getMaxX() ? d - bounds2.getMaxX() : bounds3.getMaxX() - bounds2.getMaxX();
                }
                this.textTranslateX.set(this.textTranslateX.get() + d4);
            }
        }
        this.updateCaretOff();
    }

    public HitInfo getIndex(double d, double d2) {
        Point2D point2D = new Point2D(d - this.textTranslateX.get() - this.snappedLeftInset(), d2 - this.snappedTopInset());
        return this.textNode.impl_hitTestChar(this.translateCaretPosition(point2D));
    }

    public void positionCaret(HitInfo hitInfo, boolean bl) {
        TextField textField = (TextField)this.getSkinnable();
        int n = Utils.getHitInsertionIndex(hitInfo, textField.textProperty().getValueSafe());
        if (bl) {
            textField.selectPositionCaret(n);
        } else {
            textField.positionCaret(n);
        }
        this.setForwardBias(hitInfo.isLeading());
    }

    @Override
    public Rectangle2D getCharacterBounds(int n) {
        double d;
        double d2;
        double d3;
        double d4;
        Bounds bounds;
        if (n == this.textNode.getText().length()) {
            bounds = this.textNode.getBoundsInLocal();
            d4 = bounds.getMaxX();
            d3 = 0.0;
            d2 = 0.0;
            d = bounds.getMaxY();
        } else {
            this.characterBoundingPath.getElements().clear();
            this.characterBoundingPath.getElements().addAll((Object[])this.textNode.impl_getRangeShape(n, n + 1));
            this.characterBoundingPath.setLayoutX(this.textNode.getLayoutX());
            this.characterBoundingPath.setLayoutY(this.textNode.getLayoutY());
            bounds = this.characterBoundingPath.getBoundsInLocal();
            d4 = bounds.getMinX();
            d3 = bounds.getMinY();
            d2 = bounds.isEmpty() ? 0.0 : bounds.getWidth();
            d = bounds.isEmpty() ? 0.0 : bounds.getHeight();
        }
        bounds = this.textGroup.getBoundsInParent();
        return new Rectangle2D(d4 + bounds.getMinX() + this.textTranslateX.get(), d3 + bounds.getMinY(), d2, d);
    }

    @Override
    protected PathElement[] getUnderlineShape(int n, int n2) {
        return this.textNode.impl_getUnderlineShape(n, n2);
    }

    @Override
    protected PathElement[] getRangeShape(int n, int n2) {
        return this.textNode.impl_getRangeShape(n, n2);
    }

    @Override
    protected void addHighlight(List<? extends Node> list, int n) {
        this.textGroup.getChildren().addAll(list);
    }

    @Override
    protected void removeHighlight(List<? extends Node> list) {
        this.textGroup.getChildren().removeAll(list);
    }

    @Override
    public void nextCharacterVisually(boolean bl) {
        if (this.isRTL()) {
            bl = !bl;
        }
        Bounds bounds = this.caretPath.getLayoutBounds();
        if (this.caretPath.getElements().size() == 4) {
            bounds = new Path(new PathElement[]{(PathElement)this.caretPath.getElements().get(0), (PathElement)this.caretPath.getElements().get(1)}).getLayoutBounds();
        }
        double d = bl ? bounds.getMaxX() : bounds.getMinX();
        double d2 = (bounds.getMinY() + bounds.getMaxY()) / 2.0;
        HitInfo hitInfo = this.textNode.impl_hitTestChar(this.translateCaretPosition(new Point2D(d, d2)));
        Path path = new Path(this.textNode.impl_getRangeShape(hitInfo.getCharIndex(), hitInfo.getCharIndex() + 1));
        if (bl && path.getLayoutBounds().getMaxX() > bounds.getMaxX() || !bl && path.getLayoutBounds().getMinX() < bounds.getMinX()) {
            hitInfo.setLeading(!hitInfo.isLeading());
        }
        this.positionCaret(hitInfo, false);
    }

    protected void layoutChildren(double d, double d2, double d3, double d4) {
        super.layoutChildren(d, d2, d3, d4);
        if (this.textNode != null) {
            double d5;
            Bounds bounds = this.textNode.getLayoutBounds();
            double d6 = this.textNode.getBaselineOffset();
            double d7 = bounds.getHeight() - d6;
            switch (((TextField)this.getSkinnable()).getAlignment().getVpos()) {
                case TOP: {
                    d5 = d6;
                    break;
                }
                case CENTER: {
                    d5 = (d6 + this.textGroup.getHeight() - d7) / 2.0;
                    break;
                }
                default: {
                    d5 = this.textGroup.getHeight() - d7;
                }
            }
            this.textNode.setY(d5);
            if (this.promptNode != null) {
                this.promptNode.setY(d5);
            }
            if (((TextField)this.getSkinnable()).getWidth() > 0.0) {
                this.updateTextPos();
                this.updateCaretOff();
            }
        }
        if (SHOW_HANDLES) {
            this.handleGroup.setLayoutX(d + this.textTranslateX.get());
            this.handleGroup.setLayoutY(d2);
            this.selectionHandle1.resize(this.selectionHandle1.prefWidth(-1.0), this.selectionHandle1.prefHeight(-1.0));
            this.selectionHandle2.resize(this.selectionHandle2.prefWidth(-1.0), this.selectionHandle2.prefHeight(-1.0));
            this.caretHandle.resize(this.caretHandle.prefWidth(-1.0), this.caretHandle.prefHeight(-1.0));
            Bounds bounds = this.caretPath.getBoundsInParent();
            this.caretHandle.setLayoutY(bounds.getMaxY() - 1.0);
            this.selectionHandle1.setLayoutY(bounds.getMinY() - this.selectionHandle1.getHeight() + 1.0);
            this.selectionHandle2.setLayoutY(bounds.getMaxY() - 1.0);
        }
    }

    protected HPos getHAlignment() {
        HPos hPos = ((TextField)this.getSkinnable()).getAlignment().getHpos();
        return hPos;
    }

    @Override
    public Point2D getMenuPosition() {
        Point2D point2D = super.getMenuPosition();
        if (point2D != null) {
            point2D = new Point2D(Math.max(0.0, point2D.getX() - this.textNode.getLayoutX() - this.snappedLeftInset() + this.textTranslateX.get()), Math.max(0.0, point2D.getY() - this.textNode.getLayoutY() - this.snappedTopInset()));
        }
        return point2D;
    }

    @Override
    protected String maskText(String string) {
        if (this.getSkinnable() instanceof PasswordField) {
            int n = string.length();
            StringBuilder stringBuilder = new StringBuilder(n);
            for (int i = 0; i < n; ++i) {
                stringBuilder.append('\u25cf');
            }
            return stringBuilder.toString();
        }
        return string;
    }

    protected Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case BOUNDS_FOR_RANGE: 
            case OFFSET_AT_POINT: {
                return this.textNode.queryAccessibleAttribute(accessibleAttribute, arrobject);
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }
}

