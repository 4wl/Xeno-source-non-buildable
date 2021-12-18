/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Animation
 *  javafx.animation.KeyFrame
 *  javafx.animation.KeyValue
 *  javafx.animation.Timeline
 *  javafx.beans.property.BooleanProperty
 *  javafx.beans.property.IntegerProperty
 *  javafx.beans.property.ObjectProperty
 *  javafx.beans.value.ObservableValue
 *  javafx.beans.value.WritableValue
 *  javafx.collections.FXCollections
 *  javafx.collections.ObservableList
 *  javafx.css.CssMetaData
 *  javafx.css.Styleable
 *  javafx.css.StyleableBooleanProperty
 *  javafx.css.StyleableIntegerProperty
 *  javafx.css.StyleableObjectProperty
 *  javafx.css.StyleableProperty
 *  javafx.geometry.NodeOrientation
 *  javafx.geometry.VPos
 *  javafx.scene.Node
 *  javafx.scene.control.ProgressIndicator
 *  javafx.scene.control.SkinBase
 *  javafx.scene.layout.Pane
 *  javafx.scene.layout.Region
 *  javafx.scene.layout.StackPane
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.Paint
 *  javafx.scene.shape.Arc
 *  javafx.scene.shape.ArcType
 *  javafx.scene.shape.Circle
 *  javafx.scene.shape.Shape
 *  javafx.scene.text.Text
 *  javafx.scene.transform.Scale
 *  javafx.scene.transform.Transform
 *  javafx.util.Duration
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.NodeOrientation;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.util.Duration;

public class ProgressIndicatorSkin
extends BehaviorSkinBase<ProgressIndicator, BehaviorBase<ProgressIndicator>> {
    private ObjectProperty<Paint> progressColor = new StyleableObjectProperty<Paint>(null){

        protected void invalidated() {
            Paint paint = (Paint)this.get();
            if (paint != null && !(paint instanceof Color)) {
                if (this.isBound()) {
                    this.unbind();
                }
                this.set(null);
                throw new IllegalArgumentException("Only Color objects are supported");
            }
            if (ProgressIndicatorSkin.this.spinner != null) {
                ProgressIndicatorSkin.this.spinner.setFillOverride(paint);
            }
            if (ProgressIndicatorSkin.this.determinateIndicator != null) {
                ProgressIndicatorSkin.this.determinateIndicator.setFillOverride(paint);
            }
        }

        public Object getBean() {
            return ProgressIndicatorSkin.this;
        }

        public String getName() {
            return "progressColorProperty";
        }

        public CssMetaData<ProgressIndicator, Paint> getCssMetaData() {
            return PROGRESS_COLOR;
        }
    };
    private IntegerProperty indeterminateSegmentCount = new StyleableIntegerProperty(8){

        protected void invalidated() {
            if (ProgressIndicatorSkin.this.spinner != null) {
                ProgressIndicatorSkin.this.spinner.rebuild();
            }
        }

        public Object getBean() {
            return ProgressIndicatorSkin.this;
        }

        public String getName() {
            return "indeterminateSegmentCount";
        }

        public CssMetaData<ProgressIndicator, Number> getCssMetaData() {
            return INDETERMINATE_SEGMENT_COUNT;
        }
    };
    private final BooleanProperty spinEnabled = new StyleableBooleanProperty(false){

        protected void invalidated() {
            if (ProgressIndicatorSkin.this.spinner != null) {
                ProgressIndicatorSkin.this.spinner.setSpinEnabled(this.get());
            }
        }

        public CssMetaData<ProgressIndicator, Boolean> getCssMetaData() {
            return SPIN_ENABLED;
        }

        public Object getBean() {
            return ProgressIndicatorSkin.this;
        }

        public String getName() {
            return "spinEnabled";
        }
    };
    private static final String DONE = ControlResources.getString("ProgressIndicator.doneString");
    private static final Text doneText = new Text(DONE);
    private IndeterminateSpinner spinner;
    private DeterminateIndicator determinateIndicator;
    private ProgressIndicator control;
    protected Animation indeterminateTransition;
    protected final Duration CLIPPED_DELAY = new Duration(300.0);
    protected final Duration UNCLIPPED_DELAY = new Duration(0.0);
    private static final CssMetaData<ProgressIndicator, Paint> PROGRESS_COLOR;
    private static final CssMetaData<ProgressIndicator, Number> INDETERMINATE_SEGMENT_COUNT;
    private static final CssMetaData<ProgressIndicator, Boolean> SPIN_ENABLED;
    public static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    Paint getProgressColor() {
        return (Paint)this.progressColor.get();
    }

    public ProgressIndicatorSkin(ProgressIndicator progressIndicator) {
        super(progressIndicator, new BehaviorBase<ProgressIndicator>(progressIndicator, Collections.emptyList()));
        this.control = progressIndicator;
        this.registerChangeListener((ObservableValue<?>)progressIndicator.indeterminateProperty(), "INDETERMINATE");
        this.registerChangeListener((ObservableValue<?>)progressIndicator.progressProperty(), "PROGRESS");
        this.registerChangeListener((ObservableValue<?>)progressIndicator.visibleProperty(), "VISIBLE");
        this.registerChangeListener((ObservableValue<?>)progressIndicator.parentProperty(), "PARENT");
        this.registerChangeListener((ObservableValue<?>)progressIndicator.sceneProperty(), "SCENE");
        this.initialize();
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("INDETERMINATE".equals(string)) {
            this.initialize();
        } else if ("PROGRESS".equals(string)) {
            this.updateProgress();
        } else if ("VISIBLE".equals(string)) {
            this.updateAnimation();
        } else if ("PARENT".equals(string)) {
            this.updateAnimation();
        } else if ("SCENE".equals(string)) {
            this.updateAnimation();
        }
    }

    protected void initialize() {
        boolean bl = this.control.isIndeterminate();
        if (bl) {
            this.determinateIndicator = null;
            this.spinner = new IndeterminateSpinner(this.spinEnabled.get(), (Paint)this.progressColor.get());
            this.getChildren().setAll((Object[])new Node[]{this.spinner});
            if (this.control.impl_isTreeVisible() && this.indeterminateTransition != null) {
                this.indeterminateTransition.play();
            }
        } else {
            if (this.spinner != null) {
                if (this.indeterminateTransition != null) {
                    this.indeterminateTransition.stop();
                }
                this.spinner = null;
            }
            this.determinateIndicator = new DeterminateIndicator(this.control, this, (Paint)this.progressColor.get());
            this.getChildren().setAll((Object[])new Node[]{this.determinateIndicator});
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.indeterminateTransition != null) {
            this.indeterminateTransition.stop();
            this.indeterminateTransition = null;
        }
        if (this.spinner != null) {
            this.spinner = null;
        }
        this.control = null;
    }

    protected void updateProgress() {
        if (this.determinateIndicator != null) {
            this.determinateIndicator.updateProgress(this.control.getProgress());
        }
    }

    protected void createIndeterminateTimeline() {
        if (this.spinner != null) {
            this.spinner.rebuildTimeline();
        }
    }

    protected void pauseTimeline(boolean bl) {
        if (((ProgressIndicator)this.getSkinnable()).isIndeterminate()) {
            if (this.indeterminateTransition == null) {
                this.createIndeterminateTimeline();
            }
            if (bl) {
                this.indeterminateTransition.pause();
            } else {
                this.indeterminateTransition.play();
            }
        }
    }

    protected void updateAnimation() {
        boolean bl;
        ProgressIndicator progressIndicator = (ProgressIndicator)this.getSkinnable();
        boolean bl2 = bl = progressIndicator.isVisible() && progressIndicator.getParent() != null && progressIndicator.getScene() != null;
        if (this.indeterminateTransition != null) {
            this.pauseTimeline(!bl);
        } else if (bl) {
            this.createIndeterminateTimeline();
        }
    }

    protected void layoutChildren(double d, double d2, double d3, double d4) {
        if (this.spinner != null && this.control.isIndeterminate()) {
            this.spinner.layoutChildren();
            this.spinner.resizeRelocate(0.0, 0.0, d3, d4);
        } else if (this.determinateIndicator != null) {
            this.determinateIndicator.layoutChildren();
            this.determinateIndicator.resizeRelocate(0.0, 0.0, d3, d4);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLES;
    }

    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return ProgressIndicatorSkin.getClassCssMetaData();
    }

    static {
        doneText.getStyleClass().add((Object)"text");
        PROGRESS_COLOR = new CssMetaData<ProgressIndicator, Paint>("-fx-progress-color", PaintConverter.getInstance(), null){

            public boolean isSettable(ProgressIndicator progressIndicator) {
                ProgressIndicatorSkin progressIndicatorSkin = (ProgressIndicatorSkin)progressIndicator.getSkin();
                return progressIndicatorSkin.progressColor == null || !progressIndicatorSkin.progressColor.isBound();
            }

            public StyleableProperty<Paint> getStyleableProperty(ProgressIndicator progressIndicator) {
                ProgressIndicatorSkin progressIndicatorSkin = (ProgressIndicatorSkin)progressIndicator.getSkin();
                return (StyleableProperty)progressIndicatorSkin.progressColor;
            }
        };
        INDETERMINATE_SEGMENT_COUNT = new CssMetaData<ProgressIndicator, Number>("-fx-indeterminate-segment-count", SizeConverter.getInstance(), (Number)8){

            public boolean isSettable(ProgressIndicator progressIndicator) {
                ProgressIndicatorSkin progressIndicatorSkin = (ProgressIndicatorSkin)progressIndicator.getSkin();
                return progressIndicatorSkin.indeterminateSegmentCount == null || !progressIndicatorSkin.indeterminateSegmentCount.isBound();
            }

            public StyleableProperty<Number> getStyleableProperty(ProgressIndicator progressIndicator) {
                ProgressIndicatorSkin progressIndicatorSkin = (ProgressIndicatorSkin)progressIndicator.getSkin();
                return (StyleableProperty)progressIndicatorSkin.indeterminateSegmentCount;
            }
        };
        SPIN_ENABLED = new CssMetaData<ProgressIndicator, Boolean>("-fx-spin-enabled", BooleanConverter.getInstance(), Boolean.FALSE){

            public boolean isSettable(ProgressIndicator progressIndicator) {
                ProgressIndicatorSkin progressIndicatorSkin = (ProgressIndicatorSkin)progressIndicator.getSkin();
                return progressIndicatorSkin.spinEnabled == null || !progressIndicatorSkin.spinEnabled.isBound();
            }

            public StyleableProperty<Boolean> getStyleableProperty(ProgressIndicator progressIndicator) {
                ProgressIndicatorSkin progressIndicatorSkin = (ProgressIndicatorSkin)progressIndicator.getSkin();
                return (StyleableProperty)progressIndicatorSkin.spinEnabled;
            }
        };
        ArrayList<Object> arrayList = new ArrayList<Object>(SkinBase.getClassCssMetaData());
        arrayList.add(PROGRESS_COLOR);
        arrayList.add(INDETERMINATE_SEGMENT_COUNT);
        arrayList.add(SPIN_ENABLED);
        STYLEABLES = Collections.unmodifiableList(arrayList);
    }

    private final class IndeterminateSpinner
    extends Region {
        private IndicatorPaths pathsG;
        private final List<Double> opacities = new ArrayList<Double>();
        private boolean spinEnabled = false;
        private Paint fillOverride = null;

        private IndeterminateSpinner(boolean bl, Paint paint) {
            this.spinEnabled = bl;
            this.fillOverride = paint;
            this.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            this.getStyleClass().setAll((Object[])new String[]{"spinner"});
            this.pathsG = new IndicatorPaths();
            this.getChildren().add((Object)this.pathsG);
            this.rebuild();
            this.rebuildTimeline();
        }

        public void setFillOverride(Paint paint) {
            this.fillOverride = paint;
            this.rebuild();
        }

        public void setSpinEnabled(boolean bl) {
            this.spinEnabled = bl;
            this.rebuildTimeline();
        }

        private void rebuildTimeline() {
            if (this.spinEnabled) {
                if (ProgressIndicatorSkin.this.indeterminateTransition == null) {
                    ProgressIndicatorSkin.this.indeterminateTransition = new Timeline();
                    ProgressIndicatorSkin.this.indeterminateTransition.setCycleCount(-1);
                    ProgressIndicatorSkin.this.indeterminateTransition.setDelay(ProgressIndicatorSkin.this.UNCLIPPED_DELAY);
                } else {
                    ProgressIndicatorSkin.this.indeterminateTransition.stop();
                    ((Timeline)ProgressIndicatorSkin.this.indeterminateTransition).getKeyFrames().clear();
                }
                ObservableList observableList = FXCollections.observableArrayList();
                observableList.add((Object)new KeyFrame(Duration.millis((double)1.0), new KeyValue[]{new KeyValue((WritableValue)this.pathsG.rotateProperty(), (Object)360)}));
                observableList.add((Object)new KeyFrame(Duration.millis((double)3900.0), new KeyValue[]{new KeyValue((WritableValue)this.pathsG.rotateProperty(), (Object)0)}));
                for (int i = 100; i <= 3900; i += 100) {
                    observableList.add((Object)new KeyFrame(Duration.millis((double)i), actionEvent -> this.shiftColors(), new KeyValue[0]));
                }
                ((Timeline)ProgressIndicatorSkin.this.indeterminateTransition).getKeyFrames().setAll((Collection)observableList);
                ProgressIndicatorSkin.this.indeterminateTransition.playFromStart();
            } else if (ProgressIndicatorSkin.this.indeterminateTransition != null) {
                ProgressIndicatorSkin.this.indeterminateTransition.stop();
                ((Timeline)ProgressIndicatorSkin.this.indeterminateTransition).getKeyFrames().clear();
                ProgressIndicatorSkin.this.indeterminateTransition = null;
            }
        }

        protected void layoutChildren() {
            double d;
            double d2 = ProgressIndicatorSkin.this.control.getWidth() - ProgressIndicatorSkin.this.control.snappedLeftInset() - ProgressIndicatorSkin.this.control.snappedRightInset();
            double d3 = ProgressIndicatorSkin.this.control.getHeight() - ProgressIndicatorSkin.this.control.snappedTopInset() - ProgressIndicatorSkin.this.control.snappedBottomInset();
            double d4 = this.pathsG.prefWidth(-1.0);
            double d5 = this.pathsG.prefHeight(-1.0);
            double d6 = d = d2 / d4;
            if (d * d5 > d3) {
                d6 = d3 / d5;
            }
            double d7 = d4 * d6;
            double d8 = d5 * d6;
            this.pathsG.resizeRelocate((d2 - d7) / 2.0, (d3 - d8) / 2.0, d7, d8);
        }

        private void rebuild() {
            int n = ProgressIndicatorSkin.this.indeterminateSegmentCount.get();
            this.opacities.clear();
            this.pathsG.getChildren().clear();
            double d = 0.8 / (double)(n - 1);
            for (int i = 0; i < n; ++i) {
                Region region = new Region();
                region.setScaleShape(false);
                region.setCenterShape(false);
                region.getStyleClass().addAll((Object[])new String[]{"segment", "segment" + i});
                if (this.fillOverride instanceof Color) {
                    Color color = (Color)this.fillOverride;
                    region.setStyle("-fx-background-color: rgba(" + (int)(255.0 * color.getRed()) + "," + (int)(255.0 * color.getGreen()) + "," + (int)(255.0 * color.getBlue()) + "," + color.getOpacity() + ");");
                } else {
                    region.setStyle(null);
                }
                this.pathsG.getChildren().add((Object)region);
                this.opacities.add(Math.max(0.1, 1.0 - d * (double)i));
            }
        }

        private void shiftColors() {
            if (this.opacities.size() <= 0) {
                return;
            }
            int n = ProgressIndicatorSkin.this.indeterminateSegmentCount.get();
            Collections.rotate(this.opacities, -1);
            for (int i = 0; i < n; ++i) {
                ((Node)this.pathsG.getChildren().get(i)).setOpacity(this.opacities.get(i).doubleValue());
            }
        }

        private class IndicatorPaths
        extends Pane {
            private IndicatorPaths() {
            }

            protected double computePrefWidth(double d) {
                double d2 = 0.0;
                for (Node node : this.getChildren()) {
                    if (!(node instanceof Region)) continue;
                    Region region = (Region)node;
                    if (region.getShape() != null) {
                        d2 = Math.max(d2, region.getShape().getLayoutBounds().getMaxX());
                        continue;
                    }
                    d2 = Math.max(d2, region.prefWidth(d));
                }
                return d2;
            }

            protected double computePrefHeight(double d) {
                double d2 = 0.0;
                for (Node node : this.getChildren()) {
                    if (!(node instanceof Region)) continue;
                    Region region = (Region)node;
                    if (region.getShape() != null) {
                        d2 = Math.max(d2, region.getShape().getLayoutBounds().getMaxY());
                        continue;
                    }
                    d2 = Math.max(d2, region.prefHeight(d));
                }
                return d2;
            }

            protected void layoutChildren() {
                double d = this.getWidth() / this.computePrefWidth(-1.0);
                for (Node node : this.getChildren()) {
                    if (!(node instanceof Region)) continue;
                    Region region = (Region)node;
                    if (region.getShape() != null) {
                        region.resize(region.getShape().getLayoutBounds().getMaxX(), region.getShape().getLayoutBounds().getMaxY());
                        region.getTransforms().setAll((Object[])new Transform[]{new Scale(d, d, 0.0, 0.0)});
                        continue;
                    }
                    region.autosize();
                }
            }
        }
    }

    private class DeterminateIndicator
    extends Region {
        private double textGap = 2.0;
        private int intProgress;
        private int degProgress;
        private Text text;
        private StackPane indicator;
        private StackPane progress;
        private StackPane tick;
        private Arc arcShape;
        private Circle indicatorCircle;

        public DeterminateIndicator(ProgressIndicator progressIndicator, ProgressIndicatorSkin progressIndicatorSkin2, Paint paint) {
            this.getStyleClass().add((Object)"determinate-indicator");
            this.intProgress = (int)Math.round(progressIndicator.getProgress() * 100.0);
            this.degProgress = (int)(360.0 * progressIndicator.getProgress());
            this.getChildren().clear();
            this.text = new Text(progressIndicator.getProgress() >= 1.0 ? DONE : "" + this.intProgress + "%");
            this.text.setTextOrigin(VPos.TOP);
            this.text.getStyleClass().setAll((Object[])new String[]{"text", "percentage"});
            this.indicator = new StackPane();
            this.indicator.setScaleShape(false);
            this.indicator.setCenterShape(false);
            this.indicator.getStyleClass().setAll((Object[])new String[]{"indicator"});
            this.indicatorCircle = new Circle();
            this.indicator.setShape((Shape)this.indicatorCircle);
            this.arcShape = new Arc();
            this.arcShape.setType(ArcType.ROUND);
            this.arcShape.setStartAngle(90.0);
            this.progress = new StackPane();
            this.progress.getStyleClass().setAll((Object[])new String[]{"progress"});
            this.progress.setScaleShape(false);
            this.progress.setCenterShape(false);
            this.progress.setShape((Shape)this.arcShape);
            this.progress.getChildren().clear();
            this.setFillOverride(paint);
            this.tick = new StackPane();
            this.tick.getStyleClass().setAll((Object[])new String[]{"tick"});
            this.getChildren().setAll((Object[])new Node[]{this.indicator, this.progress, this.text, this.tick});
            this.updateProgress(progressIndicator.getProgress());
        }

        private void setFillOverride(Paint paint) {
            if (paint instanceof Color) {
                Color color = (Color)paint;
                this.progress.setStyle("-fx-background-color: rgba(" + (int)(255.0 * color.getRed()) + "," + (int)(255.0 * color.getGreen()) + "," + (int)(255.0 * color.getBlue()) + "," + color.getOpacity() + ");");
            } else {
                this.progress.setStyle(null);
            }
        }

        public boolean usesMirroring() {
            return false;
        }

        private void updateProgress(double d) {
            this.intProgress = (int)Math.round(d * 100.0);
            this.text.setText(d >= 1.0 ? DONE : "" + this.intProgress + "%");
            this.degProgress = (int)(360.0 * d);
            this.arcShape.setLength((double)(-this.degProgress));
            this.requestLayout();
        }

        protected void layoutChildren() {
            double d = doneText.getLayoutBounds().getHeight();
            double d2 = ProgressIndicatorSkin.this.control.snappedLeftInset();
            double d3 = ProgressIndicatorSkin.this.control.snappedRightInset();
            double d4 = ProgressIndicatorSkin.this.control.snappedTopInset();
            double d5 = ProgressIndicatorSkin.this.control.snappedBottomInset();
            double d6 = ProgressIndicatorSkin.this.control.getWidth() - d2 - d3;
            double d7 = ProgressIndicatorSkin.this.control.getHeight() - d4 - d5 - this.textGap - d;
            double d8 = d6 / 2.0;
            double d9 = d7 / 2.0;
            double d10 = Math.floor(Math.min(d8, d9));
            double d11 = this.snapPosition(d2 + d8);
            double d12 = this.snapPosition(d4 + d10);
            double d13 = this.indicator.snappedLeftInset();
            double d14 = this.indicator.snappedRightInset();
            double d15 = this.indicator.snappedTopInset();
            double d16 = this.indicator.snappedBottomInset();
            double d17 = this.snapSize(Math.min(Math.min(d10 - d13, d10 - d14), Math.min(d10 - d15, d10 - d16)));
            this.indicatorCircle.setRadius(d10);
            this.indicator.setLayoutX(d11);
            this.indicator.setLayoutY(d12);
            this.arcShape.setRadiusX(d17);
            this.arcShape.setRadiusY(d17);
            this.progress.setLayoutX(d11);
            this.progress.setLayoutY(d12);
            double d18 = this.progress.snappedLeftInset();
            double d19 = this.progress.snappedRightInset();
            double d20 = this.progress.snappedTopInset();
            double d21 = this.progress.snappedBottomInset();
            double d22 = this.snapSize(Math.min(Math.min(d17 - d18, d17 - d19), Math.min(d17 - d20, d17 - d21)));
            double d23 = Math.ceil(Math.sqrt(d22 * d22 / 2.0));
            double d24 = d22 * (Math.sqrt(2.0) / 2.0);
            this.tick.setLayoutX(d11 - d23);
            this.tick.setLayoutY(d12 - d23);
            this.tick.resize(d23 + d23, d23 + d23);
            this.tick.setVisible(ProgressIndicatorSkin.this.control.getProgress() >= 1.0);
            double d25 = this.text.getLayoutBounds().getWidth();
            double d26 = this.text.getLayoutBounds().getHeight();
            if (ProgressIndicatorSkin.this.control.getWidth() >= d25 && ProgressIndicatorSkin.this.control.getHeight() >= d26) {
                if (!this.text.isVisible()) {
                    this.text.setVisible(true);
                }
                this.text.setLayoutY(this.snapPosition(d12 + d10 + this.textGap));
                this.text.setLayoutX(this.snapPosition(d11 - d25 / 2.0));
            } else if (this.text.isVisible()) {
                this.text.setVisible(false);
            }
        }

        protected double computePrefWidth(double d) {
            double d2 = ProgressIndicatorSkin.this.control.snappedLeftInset();
            double d3 = ProgressIndicatorSkin.this.control.snappedRightInset();
            double d4 = this.indicator.snappedLeftInset();
            double d5 = this.indicator.snappedRightInset();
            double d6 = this.indicator.snappedTopInset();
            double d7 = this.indicator.snappedBottomInset();
            double d8 = this.snapSize(Math.max(Math.max(d4, d5), Math.max(d6, d7)));
            double d9 = this.progress.snappedLeftInset();
            double d10 = this.progress.snappedRightInset();
            double d11 = this.progress.snappedTopInset();
            double d12 = this.progress.snappedBottomInset();
            double d13 = this.snapSize(Math.max(Math.max(d9, d10), Math.max(d11, d12)));
            double d14 = this.tick.snappedLeftInset();
            double d15 = this.tick.snappedRightInset();
            double d16 = d8 + d13 + d14 + d15 + d13 + d8;
            return d2 + Math.max(d16, doneText.getLayoutBounds().getWidth()) + d3;
        }

        protected double computePrefHeight(double d) {
            double d2 = ProgressIndicatorSkin.this.control.snappedTopInset();
            double d3 = ProgressIndicatorSkin.this.control.snappedBottomInset();
            double d4 = this.indicator.snappedLeftInset();
            double d5 = this.indicator.snappedRightInset();
            double d6 = this.indicator.snappedTopInset();
            double d7 = this.indicator.snappedBottomInset();
            double d8 = this.snapSize(Math.max(Math.max(d4, d5), Math.max(d6, d7)));
            double d9 = this.progress.snappedLeftInset();
            double d10 = this.progress.snappedRightInset();
            double d11 = this.progress.snappedTopInset();
            double d12 = this.progress.snappedBottomInset();
            double d13 = this.snapSize(Math.max(Math.max(d9, d10), Math.max(d11, d12)));
            double d14 = this.tick.snappedTopInset();
            double d15 = this.tick.snappedBottomInset();
            double d16 = d8 + d13 + d14 + d15 + d13 + d8;
            return d2 + d16 + this.textGap + doneText.getLayoutBounds().getHeight() + d3;
        }

        protected double computeMaxWidth(double d) {
            return this.computePrefWidth(d);
        }

        protected double computeMaxHeight(double d) {
            return this.computePrefHeight(d);
        }
    }
}

