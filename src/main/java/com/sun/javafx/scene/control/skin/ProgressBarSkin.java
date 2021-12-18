/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Transition
 *  javafx.beans.InvalidationListener
 *  javafx.beans.binding.When
 *  javafx.beans.property.BooleanProperty
 *  javafx.beans.property.DoubleProperty
 *  javafx.beans.value.ObservableBooleanValue
 *  javafx.beans.value.ObservableNumberValue
 *  javafx.beans.value.ObservableValue
 *  javafx.css.CssMetaData
 *  javafx.css.Styleable
 *  javafx.css.StyleableBooleanProperty
 *  javafx.css.StyleableDoubleProperty
 *  javafx.css.StyleableProperty
 *  javafx.scene.Node
 *  javafx.scene.control.ProgressBar
 *  javafx.scene.control.ProgressIndicator
 *  javafx.scene.control.SkinBase
 *  javafx.scene.layout.Background
 *  javafx.scene.layout.BackgroundFill
 *  javafx.scene.layout.Region
 *  javafx.scene.layout.StackPane
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.Paint
 *  javafx.util.Duration
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.skin.ProgressIndicatorSkin;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.Transition;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class ProgressBarSkin
extends ProgressIndicatorSkin {
    private DoubleProperty indeterminateBarLength = null;
    private BooleanProperty indeterminateBarEscape = null;
    private BooleanProperty indeterminateBarFlip = null;
    private DoubleProperty indeterminateBarAnimationTime = null;
    private StackPane bar;
    private StackPane track;
    private Region clipRegion;
    private double barWidth;
    boolean wasIndeterminate = false;

    private DoubleProperty indeterminateBarLengthProperty() {
        if (this.indeterminateBarLength == null) {
            this.indeterminateBarLength = new StyleableDoubleProperty(60.0){

                public Object getBean() {
                    return ProgressBarSkin.this;
                }

                public String getName() {
                    return "indeterminateBarLength";
                }

                public CssMetaData<ProgressBar, Number> getCssMetaData() {
                    return StyleableProperties.INDETERMINATE_BAR_LENGTH;
                }
            };
        }
        return this.indeterminateBarLength;
    }

    private Double getIndeterminateBarLength() {
        return this.indeterminateBarLength == null ? 60.0 : this.indeterminateBarLength.get();
    }

    private BooleanProperty indeterminateBarEscapeProperty() {
        if (this.indeterminateBarEscape == null) {
            this.indeterminateBarEscape = new StyleableBooleanProperty(true){

                public Object getBean() {
                    return ProgressBarSkin.this;
                }

                public String getName() {
                    return "indeterminateBarEscape";
                }

                public CssMetaData<ProgressBar, Boolean> getCssMetaData() {
                    return StyleableProperties.INDETERMINATE_BAR_ESCAPE;
                }
            };
        }
        return this.indeterminateBarEscape;
    }

    private Boolean getIndeterminateBarEscape() {
        return this.indeterminateBarEscape == null ? true : this.indeterminateBarEscape.get();
    }

    private BooleanProperty indeterminateBarFlipProperty() {
        if (this.indeterminateBarFlip == null) {
            this.indeterminateBarFlip = new StyleableBooleanProperty(true){

                public Object getBean() {
                    return ProgressBarSkin.this;
                }

                public String getName() {
                    return "indeterminateBarFlip";
                }

                public CssMetaData<ProgressBar, Boolean> getCssMetaData() {
                    return StyleableProperties.INDETERMINATE_BAR_FLIP;
                }
            };
        }
        return this.indeterminateBarFlip;
    }

    private Boolean getIndeterminateBarFlip() {
        return this.indeterminateBarFlip == null ? true : this.indeterminateBarFlip.get();
    }

    private DoubleProperty indeterminateBarAnimationTimeProperty() {
        if (this.indeterminateBarAnimationTime == null) {
            this.indeterminateBarAnimationTime = new StyleableDoubleProperty(2.0){

                public Object getBean() {
                    return ProgressBarSkin.this;
                }

                public String getName() {
                    return "indeterminateBarAnimationTime";
                }

                public CssMetaData<ProgressBar, Number> getCssMetaData() {
                    return StyleableProperties.INDETERMINATE_BAR_ANIMATION_TIME;
                }
            };
        }
        return this.indeterminateBarAnimationTime;
    }

    private double getIndeterminateBarAnimationTime() {
        return this.indeterminateBarAnimationTime == null ? 2.0 : this.indeterminateBarAnimationTime.get();
    }

    public ProgressBarSkin(ProgressBar progressBar) {
        super((ProgressIndicator)progressBar);
        this.barWidth = (double)((int)(progressBar.getWidth() - this.snappedLeftInset() - this.snappedRightInset()) * 2) * Math.min(1.0, Math.max(0.0, progressBar.getProgress())) / 2.0;
        InvalidationListener invalidationListener = observable -> this.updateProgress();
        progressBar.widthProperty().addListener(invalidationListener);
        this.initialize();
        ((ProgressIndicator)this.getSkinnable()).requestLayout();
    }

    @Override
    protected void initialize() {
        this.track = new StackPane();
        this.track.getStyleClass().setAll((Object[])new String[]{"track"});
        this.bar = new StackPane();
        this.bar.getStyleClass().setAll((Object[])new String[]{"bar"});
        this.getChildren().setAll((Object[])new Node[]{this.track, this.bar});
        this.clipRegion = new Region();
        this.bar.backgroundProperty().addListener((observableValue, background, background2) -> {
            if (background2 != null && !background2.getFills().isEmpty()) {
                BackgroundFill[] arrbackgroundFill = new BackgroundFill[background2.getFills().size()];
                for (int i = 0; i < background2.getFills().size(); ++i) {
                    BackgroundFill backgroundFill = (BackgroundFill)background2.getFills().get(i);
                    arrbackgroundFill[i] = new BackgroundFill((Paint)Color.BLACK, backgroundFill.getRadii(), backgroundFill.getInsets());
                }
                this.clipRegion.setBackground(new Background(arrbackgroundFill));
            }
        });
    }

    @Override
    protected void createIndeterminateTimeline() {
        if (this.indeterminateTransition != null) {
            this.indeterminateTransition.stop();
        }
        ProgressIndicator progressIndicator = (ProgressIndicator)this.getSkinnable();
        double d = progressIndicator.getWidth() - (this.snappedLeftInset() + this.snappedRightInset());
        double d2 = this.getIndeterminateBarEscape() != false ? -this.getIndeterminateBarLength().doubleValue() : 0.0;
        double d3 = this.getIndeterminateBarEscape() != false ? d : d - this.getIndeterminateBarLength();
        this.indeterminateTransition = new IndeterminateTransition(d2, d3, this);
        this.indeterminateTransition.setCycleCount(-1);
        this.clipRegion.translateXProperty().bind((ObservableValue)new When((ObservableBooleanValue)this.bar.scaleXProperty().isEqualTo(-1.0, 1.0E-100)).then((ObservableNumberValue)this.bar.translateXProperty().subtract(d).add((ObservableNumberValue)this.indeterminateBarLengthProperty())).otherwise((ObservableNumberValue)this.bar.translateXProperty().negate()));
    }

    @Override
    protected void updateProgress() {
        ProgressIndicator progressIndicator = (ProgressIndicator)this.getSkinnable();
        boolean bl = progressIndicator.isIndeterminate();
        if (!bl || !this.wasIndeterminate) {
            this.barWidth = (double)((int)(progressIndicator.getWidth() - this.snappedLeftInset() - this.snappedRightInset()) * 2) * Math.min(1.0, Math.max(0.0, progressIndicator.getProgress())) / 2.0;
            ((ProgressIndicator)this.getSkinnable()).requestLayout();
        }
        this.wasIndeterminate = bl;
    }

    public double computeBaselineOffset(double d, double d2, double d3, double d4) {
        return Double.NEGATIVE_INFINITY;
    }

    protected double computePrefWidth(double d, double d2, double d3, double d4, double d5) {
        return Math.max(100.0, d5 + this.bar.prefWidth(((ProgressIndicator)this.getSkinnable()).getWidth()) + d3);
    }

    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        return d2 + this.bar.prefHeight(d) + d4;
    }

    protected double computeMaxWidth(double d, double d2, double d3, double d4, double d5) {
        return ((ProgressIndicator)this.getSkinnable()).prefWidth(d);
    }

    protected double computeMaxHeight(double d, double d2, double d3, double d4, double d5) {
        return ((ProgressIndicator)this.getSkinnable()).prefHeight(d);
    }

    @Override
    protected void layoutChildren(double d, double d2, double d3, double d4) {
        ProgressIndicator progressIndicator = (ProgressIndicator)this.getSkinnable();
        boolean bl = progressIndicator.isIndeterminate();
        this.clipRegion.resizeRelocate(0.0, 0.0, d3, d4);
        this.track.resizeRelocate(d, d2, d3, d4);
        this.bar.resizeRelocate(d, d2, bl ? this.getIndeterminateBarLength() : this.barWidth, d4);
        this.track.setVisible(true);
        if (bl) {
            this.createIndeterminateTimeline();
            if (((ProgressIndicator)this.getSkinnable()).impl_isTreeVisible()) {
                this.indeterminateTransition.play();
            }
            this.bar.setClip((Node)this.clipRegion);
        } else if (this.indeterminateTransition != null) {
            this.indeterminateTransition.stop();
            this.indeterminateTransition = null;
            this.bar.setClip(null);
            this.bar.setScaleX(1.0);
            this.bar.setTranslateX(0.0);
            this.clipRegion.translateXProperty().unbind();
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return ProgressBarSkin.getClassCssMetaData();
    }

    private static class IndeterminateTransition
    extends Transition {
        private final WeakReference<ProgressBarSkin> skin;
        private final double startX;
        private final double endX;
        private final boolean flip;

        public IndeterminateTransition(double d, double d2, ProgressBarSkin progressBarSkin) {
            this.startX = d;
            this.endX = d2;
            this.skin = new WeakReference<ProgressBarSkin>(progressBarSkin);
            this.flip = progressBarSkin.getIndeterminateBarFlip();
            progressBarSkin.getIndeterminateBarEscape();
            this.setCycleDuration(Duration.seconds((double)(progressBarSkin.getIndeterminateBarAnimationTime() * (double)(this.flip ? 2 : 1))));
        }

        protected void interpolate(double d) {
            ProgressBarSkin progressBarSkin = (ProgressBarSkin)((Object)this.skin.get());
            if (progressBarSkin == null) {
                this.stop();
            } else if (d <= 0.5 || !this.flip) {
                progressBarSkin.bar.setScaleX(-1.0);
                progressBarSkin.bar.setTranslateX(this.startX + (double)(this.flip ? 2 : 1) * d * (this.endX - this.startX));
            } else {
                progressBarSkin.bar.setScaleX(1.0);
                progressBarSkin.bar.setTranslateX(this.startX + 2.0 * (1.0 - d) * (this.endX - this.startX));
            }
        }
    }

    private static class StyleableProperties {
        private static final CssMetaData<ProgressBar, Number> INDETERMINATE_BAR_LENGTH = new CssMetaData<ProgressBar, Number>("-fx-indeterminate-bar-length", SizeConverter.getInstance(), (Number)60.0){

            public boolean isSettable(ProgressBar progressBar) {
                ProgressBarSkin progressBarSkin = (ProgressBarSkin)progressBar.getSkin();
                return progressBarSkin.indeterminateBarLength == null || !progressBarSkin.indeterminateBarLength.isBound();
            }

            public StyleableProperty<Number> getStyleableProperty(ProgressBar progressBar) {
                ProgressBarSkin progressBarSkin = (ProgressBarSkin)progressBar.getSkin();
                return (StyleableProperty)progressBarSkin.indeterminateBarLengthProperty();
            }
        };
        private static final CssMetaData<ProgressBar, Boolean> INDETERMINATE_BAR_ESCAPE = new CssMetaData<ProgressBar, Boolean>("-fx-indeterminate-bar-escape", BooleanConverter.getInstance(), Boolean.TRUE){

            public boolean isSettable(ProgressBar progressBar) {
                ProgressBarSkin progressBarSkin = (ProgressBarSkin)progressBar.getSkin();
                return progressBarSkin.indeterminateBarEscape == null || !progressBarSkin.indeterminateBarEscape.isBound();
            }

            public StyleableProperty<Boolean> getStyleableProperty(ProgressBar progressBar) {
                ProgressBarSkin progressBarSkin = (ProgressBarSkin)progressBar.getSkin();
                return (StyleableProperty)progressBarSkin.indeterminateBarEscapeProperty();
            }
        };
        private static final CssMetaData<ProgressBar, Boolean> INDETERMINATE_BAR_FLIP = new CssMetaData<ProgressBar, Boolean>("-fx-indeterminate-bar-flip", BooleanConverter.getInstance(), Boolean.TRUE){

            public boolean isSettable(ProgressBar progressBar) {
                ProgressBarSkin progressBarSkin = (ProgressBarSkin)progressBar.getSkin();
                return progressBarSkin.indeterminateBarFlip == null || !progressBarSkin.indeterminateBarFlip.isBound();
            }

            public StyleableProperty<Boolean> getStyleableProperty(ProgressBar progressBar) {
                ProgressBarSkin progressBarSkin = (ProgressBarSkin)progressBar.getSkin();
                return (StyleableProperty)progressBarSkin.indeterminateBarFlipProperty();
            }
        };
        private static final CssMetaData<ProgressBar, Number> INDETERMINATE_BAR_ANIMATION_TIME = new CssMetaData<ProgressBar, Number>("-fx-indeterminate-bar-animation-time", SizeConverter.getInstance(), (Number)2.0){

            public boolean isSettable(ProgressBar progressBar) {
                ProgressBarSkin progressBarSkin = (ProgressBarSkin)progressBar.getSkin();
                return progressBarSkin.indeterminateBarAnimationTime == null || !progressBarSkin.indeterminateBarAnimationTime.isBound();
            }

            public StyleableProperty<Number> getStyleableProperty(ProgressBar progressBar) {
                ProgressBarSkin progressBarSkin = (ProgressBarSkin)progressBar.getSkin();
                return (StyleableProperty)progressBarSkin.indeterminateBarAnimationTimeProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            ArrayList<Object> arrayList = new ArrayList<Object>(SkinBase.getClassCssMetaData());
            arrayList.add(INDETERMINATE_BAR_LENGTH);
            arrayList.add(INDETERMINATE_BAR_ESCAPE);
            arrayList.add(INDETERMINATE_BAR_FLIP);
            arrayList.add(INDETERMINATE_BAR_ANIMATION_TIME);
            STYLEABLES = Collections.unmodifiableList(arrayList);
        }
    }
}

