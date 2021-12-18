/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.ObservableList
 *  javafx.scene.Node
 *  javafx.scene.control.Accordion
 *  javafx.scene.control.Skin
 *  javafx.scene.control.TitledPane
 *  javafx.scene.shape.Rectangle
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.AccordionBehavior;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.TitledPaneSkin;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Skin;
import javafx.scene.control.TitledPane;
import javafx.scene.shape.Rectangle;

public class AccordionSkin
extends BehaviorSkinBase<Accordion, AccordionBehavior> {
    private TitledPane firstTitledPane;
    private Rectangle clipRect;
    private boolean forceRelayout = false;
    private boolean relayout = false;
    private double previousHeight = 0.0;
    private TitledPane expandedPane = null;
    private TitledPane previousPane = null;
    private Map<TitledPane, ChangeListener<Boolean>> listeners = new HashMap<TitledPane, ChangeListener<Boolean>>();

    public AccordionSkin(Accordion accordion) {
        super(accordion, new AccordionBehavior(accordion));
        accordion.getPanes().addListener(change -> {
            if (this.firstTitledPane != null) {
                this.firstTitledPane.getStyleClass().remove((Object)"first-titled-pane");
            }
            if (!accordion.getPanes().isEmpty()) {
                this.firstTitledPane = (TitledPane)accordion.getPanes().get(0);
                this.firstTitledPane.getStyleClass().add((Object)"first-titled-pane");
            }
            this.getChildren().setAll((Collection)accordion.getPanes());
            while (change.next()) {
                this.removeTitledPaneListeners(change.getRemoved());
                this.initTitledPaneListeners(change.getAddedSubList());
            }
            this.forceRelayout = true;
        });
        if (!accordion.getPanes().isEmpty()) {
            this.firstTitledPane = (TitledPane)accordion.getPanes().get(0);
            this.firstTitledPane.getStyleClass().add((Object)"first-titled-pane");
        }
        this.clipRect = new Rectangle(accordion.getWidth(), accordion.getHeight());
        ((Accordion)this.getSkinnable()).setClip((Node)this.clipRect);
        this.initTitledPaneListeners((List<? extends TitledPane>)accordion.getPanes());
        this.getChildren().setAll((Collection)accordion.getPanes());
        ((Accordion)this.getSkinnable()).requestLayout();
        this.registerChangeListener((ObservableValue<?>)((Accordion)this.getSkinnable()).widthProperty(), "WIDTH");
        this.registerChangeListener((ObservableValue<?>)((Accordion)this.getSkinnable()).heightProperty(), "HEIGHT");
    }

    @Override
    protected void handleControlPropertyChanged(String string) {
        super.handleControlPropertyChanged(string);
        if ("WIDTH".equals(string)) {
            this.clipRect.setWidth(((Accordion)this.getSkinnable()).getWidth());
        } else if ("HEIGHT".equals(string)) {
            this.clipRect.setHeight(((Accordion)this.getSkinnable()).getHeight());
            this.relayout = true;
        }
    }

    protected double computeMinHeight(double d, double d2, double d3, double d4, double d5) {
        double d6 = 0.0;
        if (this.expandedPane != null) {
            d6 += this.expandedPane.minHeight(d);
        }
        if (this.previousPane != null && !this.previousPane.equals((Object)this.expandedPane)) {
            d6 += this.previousPane.minHeight(d);
        }
        for (Node node : this.getChildren()) {
            TitledPane titledPane = (TitledPane)node;
            if (titledPane.equals((Object)this.expandedPane) || titledPane.equals((Object)this.previousPane)) continue;
            Skin skin = ((TitledPane)node).getSkin();
            if (skin instanceof TitledPaneSkin) {
                TitledPaneSkin titledPaneSkin = (TitledPaneSkin)skin;
                d6 += titledPaneSkin.getTitleRegionSize(d);
                continue;
            }
            d6 += titledPane.minHeight(d);
        }
        return d6 + d2 + d4;
    }

    protected double computePrefHeight(double d, double d2, double d3, double d4, double d5) {
        double d6 = 0.0;
        if (this.expandedPane != null) {
            d6 += this.expandedPane.prefHeight(d);
        }
        if (this.previousPane != null && !this.previousPane.equals((Object)this.expandedPane)) {
            d6 += this.previousPane.prefHeight(d);
        }
        for (Node node : this.getChildren()) {
            TitledPane titledPane = (TitledPane)node;
            if (titledPane.equals((Object)this.expandedPane) || titledPane.equals((Object)this.previousPane)) continue;
            Skin skin = ((TitledPane)node).getSkin();
            if (skin instanceof TitledPaneSkin) {
                TitledPaneSkin titledPaneSkin = (TitledPaneSkin)skin;
                d6 += titledPaneSkin.getTitleRegionSize(d);
                continue;
            }
            d6 += titledPane.prefHeight(d);
        }
        return d6 + d2 + d4;
    }

    protected void layoutChildren(double d, double d2, double d3, double d4) {
        boolean bl = this.forceRelayout || this.relayout && this.previousHeight != d4;
        this.forceRelayout = false;
        this.previousHeight = d4;
        double d5 = 0.0;
        for (TitledPane titledPane : ((Accordion)this.getSkinnable()).getPanes()) {
            if (titledPane.equals((Object)this.expandedPane)) continue;
            TitledPaneSkin titledPaneSkin = (TitledPaneSkin)titledPane.getSkin();
            d5 += this.snapSize(titledPaneSkin.getTitleRegionSize(d3));
        }
        double d6 = d4 - d5;
        for (TitledPane titledPane : ((Accordion)this.getSkinnable()).getPanes()) {
            double d7;
            Skin skin = titledPane.getSkin();
            if (skin instanceof TitledPaneSkin) {
                ((TitledPaneSkin)skin).setMaxTitledPaneHeightForAccordion(d6);
                d7 = this.snapSize(((TitledPaneSkin)skin).getTitledPaneHeightForAccordion());
            } else {
                d7 = titledPane.prefHeight(d3);
            }
            titledPane.resize(d3, d7);
            boolean bl2 = true;
            if (!bl && this.previousPane != null && this.expandedPane != null) {
                ObservableList observableList = ((Accordion)this.getSkinnable()).getPanes();
                int n = observableList.indexOf((Object)this.previousPane);
                int n2 = observableList.indexOf((Object)this.expandedPane);
                int n3 = observableList.indexOf((Object)titledPane);
                if (n < n2) {
                    if (n3 <= n2) {
                        titledPane.relocate(d, d2);
                        d2 += d7;
                        bl2 = false;
                    }
                } else if (n > n2) {
                    if (n3 <= n) {
                        titledPane.relocate(d, d2);
                        d2 += d7;
                        bl2 = false;
                    }
                } else {
                    titledPane.relocate(d, d2);
                    d2 += d7;
                    bl2 = false;
                }
            }
            if (!bl2) continue;
            titledPane.relocate(d, d2);
            d2 += d7;
        }
    }

    private void initTitledPaneListeners(List<? extends TitledPane> list) {
        for (TitledPane titledPane : list) {
            titledPane.setExpanded(titledPane == ((Accordion)this.getSkinnable()).getExpandedPane());
            if (titledPane.isExpanded()) {
                this.expandedPane = titledPane;
            }
            ChangeListener<Boolean> changeListener = this.expandedPropertyListener(titledPane);
            titledPane.expandedProperty().addListener(changeListener);
            this.listeners.put(titledPane, changeListener);
        }
    }

    private void removeTitledPaneListeners(List<? extends TitledPane> list) {
        for (TitledPane titledPane : list) {
            if (!this.listeners.containsKey((Object)titledPane)) continue;
            titledPane.expandedProperty().removeListener(this.listeners.get((Object)titledPane));
            this.listeners.remove((Object)titledPane);
        }
    }

    private ChangeListener<Boolean> expandedPropertyListener(TitledPane titledPane) {
        return (observableValue, bl, bl2) -> {
            this.previousPane = this.expandedPane;
            Accordion accordion = (Accordion)this.getSkinnable();
            if (bl2.booleanValue()) {
                if (this.expandedPane != null) {
                    this.expandedPane.setExpanded(false);
                }
                if (titledPane != null) {
                    accordion.setExpandedPane(titledPane);
                }
                this.expandedPane = accordion.getExpandedPane();
            } else {
                this.expandedPane = null;
                accordion.setExpandedPane(null);
            }
        };
    }
}

