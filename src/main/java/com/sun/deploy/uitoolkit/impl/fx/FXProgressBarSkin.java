/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.control.ProgressBar
 *  javafx.scene.control.ProgressIndicator
 *  javafx.scene.layout.StackPane
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.CycleMethod
 *  javafx.scene.paint.LinearGradient
 *  javafx.scene.paint.Paint
 *  javafx.scene.paint.RadialGradient
 *  javafx.scene.paint.Stop
 *  javafx.scene.shape.Rectangle
 */
package com.sun.deploy.uitoolkit.impl.fx;

import com.sun.javafx.scene.control.skin.ProgressBarSkin;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class FXProgressBarSkin
extends ProgressBarSkin {
    Rectangle topGradient = new Rectangle(0.0, 0.0, (Paint)new RadialGradient(0.05, 0.0, 0.5, 0.0, this.gradientRadius, true, CycleMethod.NO_CYCLE, new Stop[]{new Stop(0.0, Color.rgb((int)255, (int)255, (int)255, (double)0.82)), new Stop(0.13, Color.rgb((int)255, (int)255, (int)255, (double)0.82)), new Stop(0.98, Color.rgb((int)255, (int)255, (int)255, (double)0.0))}));
    Rectangle bottomGradient = new Rectangle(0.0, 0.0, (Paint)new RadialGradient(0.05, 0.0, 0.5, 1.0, this.gradientRadius, true, CycleMethod.NO_CYCLE, new Stop[]{new Stop(0.0, Color.rgb((int)255, (int)255, (int)255, (double)0.82)), new Stop(0.13, Color.rgb((int)255, (int)255, (int)255, (double)0.82)), new Stop(0.98, Color.rgb((int)255, (int)255, (int)255, (double)0.0))}));
    Rectangle verticalLines;
    double gradientMargin = 4.0;
    double gradientRadius = 0.55;
    double gradientTweak = 1.4;

    public FXProgressBarSkin(ProgressBar progressBar) {
        super(progressBar);
        this.topGradient.setManaged(false);
        this.bottomGradient.setManaged(false);
        ((StackPane)this.getChildren().get(1)).getChildren().addAll((Object[])new Node[]{this.topGradient, this.bottomGradient});
        this.verticalLines = new Rectangle(0.0, 0.0, (Paint)new LinearGradient(0.0, 0.0, 14.0, 0.0, false, CycleMethod.REPEAT, new Stop[]{new Stop(0.0, Color.TRANSPARENT), new Stop(0.93, Color.TRANSPARENT), new Stop(0.93, Color.rgb((int)184, (int)184, (int)184, (double)0.2)), new Stop(1.0, Color.rgb((int)184, (int)184, (int)184, (double)0.2))}));
        this.verticalLines.setManaged(false);
        this.getChildren().add((Object)this.verticalLines);
    }

    @Override
    protected void layoutChildren(double d, double d2, double d3, double d4) {
        super.layoutChildren(d, d2, d3, d4);
        if (((ProgressIndicator)this.getSkinnable()).isIndeterminate()) {
            return;
        }
        StackPane stackPane = (StackPane)this.getChildren().get(0);
        StackPane stackPane2 = (StackPane)this.getChildren().get(1);
        if (!stackPane2.getChildren().contains((Object)this.topGradient)) {
            stackPane2.getChildren().add((Object)this.topGradient);
        }
        if (!stackPane2.getChildren().contains((Object)this.bottomGradient)) {
            stackPane2.getChildren().add((Object)this.bottomGradient);
        }
        if (!this.getChildren().contains((Object)this.verticalLines)) {
            this.getChildren().add((Object)this.verticalLines);
        }
        double d5 = Math.floor(d3 / 14.0) * 14.0 / d3;
        double d6 = stackPane2.getWidth() * d5;
        double d7 = stackPane2.getHeight();
        stackPane.resize(stackPane.getWidth() * d5, stackPane.getHeight());
        stackPane2.resize(d6, d7);
        this.topGradient.setX(d + this.gradientMargin);
        this.topGradient.setY(d2 + 0.5);
        this.topGradient.setWidth(d6 - 2.0 * this.gradientMargin);
        this.topGradient.setHeight(d7 * 0.3 / this.gradientRadius * this.gradientTweak);
        this.bottomGradient.setX(d + this.gradientMargin);
        this.bottomGradient.setWidth(d6 - 2.0 * this.gradientMargin);
        double d8 = d7 * 0.21 / this.gradientRadius * this.gradientTweak;
        this.bottomGradient.setY(d7 - d8 - 0.5);
        this.bottomGradient.setHeight(d8);
        this.verticalLines.setX(d);
        this.verticalLines.setY(d2);
        this.verticalLines.setWidth(d3 * d5);
        this.verticalLines.setHeight(d4);
    }
}

