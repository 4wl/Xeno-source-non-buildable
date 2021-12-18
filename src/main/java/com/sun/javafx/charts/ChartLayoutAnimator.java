/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Animation
 *  javafx.animation.AnimationTimer
 *  javafx.animation.KeyFrame
 *  javafx.animation.SequentialTransition
 *  javafx.animation.Timeline
 *  javafx.event.ActionEvent
 *  javafx.event.EventHandler
 *  javafx.scene.Parent
 *  javafx.scene.chart.Axis
 */
package com.sun.javafx.charts;

import java.util.HashMap;
import java.util.Map;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.chart.Axis;

public final class ChartLayoutAnimator
extends AnimationTimer
implements EventHandler<ActionEvent> {
    private Parent nodeToLayout;
    private final Map<Object, Animation> activeTimeLines = new HashMap<Object, Animation>();
    private final boolean isAxis;

    public ChartLayoutAnimator(Parent parent) {
        this.nodeToLayout = parent;
        this.isAxis = parent instanceof Axis;
    }

    public void handle(long l) {
        if (this.isAxis) {
            ((Axis)this.nodeToLayout).requestAxisLayout();
        } else {
            this.nodeToLayout.requestLayout();
        }
    }

    public void handle(ActionEvent actionEvent) {
        this.activeTimeLines.remove(actionEvent.getSource());
        if (this.activeTimeLines.isEmpty()) {
            this.stop();
        }
        this.handle(0L);
    }

    public void stop(Object object) {
        Animation animation = this.activeTimeLines.remove(object);
        if (animation != null) {
            animation.stop();
        }
        if (this.activeTimeLines.isEmpty()) {
            this.stop();
        }
    }

    public Object animate(KeyFrame ... arrkeyFrame) {
        Timeline timeline = new Timeline();
        timeline.setAutoReverse(false);
        timeline.setCycleCount(1);
        timeline.getKeyFrames().addAll((Object[])arrkeyFrame);
        timeline.setOnFinished((EventHandler)this);
        if (this.activeTimeLines.isEmpty()) {
            this.start();
        }
        this.activeTimeLines.put((Object)timeline, (Animation)timeline);
        timeline.play();
        return timeline;
    }

    public Object animate(Animation animation) {
        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().add((Object)animation);
        sequentialTransition.setOnFinished((EventHandler)this);
        if (this.activeTimeLines.isEmpty()) {
            this.start();
        }
        this.activeTimeLines.put((Object)sequentialTransition, (Animation)sequentialTransition);
        sequentialTransition.play();
        return sequentialTransition;
    }
}

