/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ObservableValue
 *  javafx.scene.Node
 *  javafx.scene.control.Label
 *  javafx.scene.control.Skin
 *  javafx.scene.control.Tooltip
 */
package com.sun.javafx.scene.control.skin;

import java.util.Collection;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.Tooltip;

public class TooltipSkin
implements Skin<Tooltip> {
    private Label tipLabel;
    private Tooltip tooltip;

    public TooltipSkin(Tooltip tooltip) {
        this.tooltip = tooltip;
        this.tipLabel = new Label();
        this.tipLabel.contentDisplayProperty().bind((ObservableValue)tooltip.contentDisplayProperty());
        this.tipLabel.fontProperty().bind((ObservableValue)tooltip.fontProperty());
        this.tipLabel.graphicProperty().bind((ObservableValue)tooltip.graphicProperty());
        this.tipLabel.graphicTextGapProperty().bind((ObservableValue)tooltip.graphicTextGapProperty());
        this.tipLabel.textAlignmentProperty().bind((ObservableValue)tooltip.textAlignmentProperty());
        this.tipLabel.textOverrunProperty().bind((ObservableValue)tooltip.textOverrunProperty());
        this.tipLabel.textProperty().bind((ObservableValue)tooltip.textProperty());
        this.tipLabel.wrapTextProperty().bind((ObservableValue)tooltip.wrapTextProperty());
        this.tipLabel.minWidthProperty().bind((ObservableValue)tooltip.minWidthProperty());
        this.tipLabel.prefWidthProperty().bind((ObservableValue)tooltip.prefWidthProperty());
        this.tipLabel.maxWidthProperty().bind((ObservableValue)tooltip.maxWidthProperty());
        this.tipLabel.minHeightProperty().bind((ObservableValue)tooltip.minHeightProperty());
        this.tipLabel.prefHeightProperty().bind((ObservableValue)tooltip.prefHeightProperty());
        this.tipLabel.maxHeightProperty().bind((ObservableValue)tooltip.maxHeightProperty());
        this.tipLabel.getStyleClass().setAll((Collection)tooltip.getStyleClass());
        this.tipLabel.setStyle(tooltip.getStyle());
        this.tipLabel.setId(tooltip.getId());
    }

    public Tooltip getSkinnable() {
        return this.tooltip;
    }

    public Node getNode() {
        return this.tipLabel;
    }

    public void dispose() {
        this.tooltip = null;
        this.tipLabel = null;
    }
}

