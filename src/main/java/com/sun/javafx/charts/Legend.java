/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.BooleanProperty
 *  javafx.beans.property.BooleanPropertyBase
 *  javafx.beans.property.ObjectProperty
 *  javafx.beans.property.ObjectPropertyBase
 *  javafx.beans.property.StringProperty
 *  javafx.beans.property.StringPropertyBase
 *  javafx.collections.FXCollections
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ObservableList
 *  javafx.geometry.Orientation
 *  javafx.geometry.Pos
 *  javafx.scene.Node
 *  javafx.scene.control.ContentDisplay
 *  javafx.scene.control.Label
 *  javafx.scene.layout.Region
 *  javafx.scene.layout.TilePane
 */
package com.sun.javafx.charts;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;

public class Legend
extends TilePane {
    private static final int GAP = 5;
    private ListChangeListener<LegendItem> itemsListener = change -> {
        this.getChildren().clear();
        for (LegendItem legendItem : this.getItems()) {
            this.getChildren().add((Object)legendItem.label);
        }
        if (this.isVisible()) {
            this.requestLayout();
        }
    };
    private BooleanProperty vertical = new BooleanPropertyBase(false){

        protected void invalidated() {
            Legend.this.setOrientation(this.get() ? Orientation.VERTICAL : Orientation.HORIZONTAL);
        }

        public Object getBean() {
            return Legend.this;
        }

        public String getName() {
            return "vertical";
        }
    };
    private ObjectProperty<ObservableList<LegendItem>> items = new ObjectPropertyBase<ObservableList<LegendItem>>(){
        ObservableList<LegendItem> oldItems = null;

        protected void invalidated() {
            if (this.oldItems != null) {
                this.oldItems.removeListener(Legend.this.itemsListener);
            }
            Legend.this.getChildren().clear();
            ObservableList observableList = (ObservableList)this.get();
            if (observableList != null) {
                observableList.addListener(Legend.this.itemsListener);
                for (LegendItem legendItem : observableList) {
                    Legend.this.getChildren().add((Object)legendItem.label);
                }
            }
            this.oldItems = (ObservableList)this.get();
            Legend.this.requestLayout();
        }

        public Object getBean() {
            return Legend.this;
        }

        public String getName() {
            return "items";
        }
    };

    public final boolean isVertical() {
        return this.vertical.get();
    }

    public final void setVertical(boolean bl) {
        this.vertical.set(bl);
    }

    public final BooleanProperty verticalProperty() {
        return this.vertical;
    }

    public final void setItems(ObservableList<LegendItem> observableList) {
        this.itemsProperty().set(observableList);
    }

    public final ObservableList<LegendItem> getItems() {
        return (ObservableList)this.items.get();
    }

    public final ObjectProperty<ObservableList<LegendItem>> itemsProperty() {
        return this.items;
    }

    public Legend() {
        super(5.0, 5.0);
        this.setTileAlignment(Pos.CENTER_LEFT);
        this.setItems((ObservableList<LegendItem>)FXCollections.observableArrayList());
        this.getStyleClass().setAll((Object[])new String[]{"chart-legend"});
    }

    protected double computePrefWidth(double d) {
        return this.getItems().size() > 0 ? super.computePrefWidth(d) : 0.0;
    }

    protected double computePrefHeight(double d) {
        return this.getItems().size() > 0 ? super.computePrefHeight(d) : 0.0;
    }

    public static class LegendItem {
        private Label label = new Label();
        private StringProperty text = new StringPropertyBase(){

            protected void invalidated() {
                label.setText(this.get());
            }

            public Object getBean() {
                return this;
            }

            public String getName() {
                return "text";
            }
        };
        private ObjectProperty<Node> symbol = new ObjectPropertyBase<Node>((Node)new Region()){

            protected void invalidated() {
                Node node = (Node)this.get();
                if (node != null) {
                    node.getStyleClass().setAll((Object[])new String[]{"chart-legend-item-symbol"});
                }
                label.setGraphic(node);
            }

            public Object getBean() {
                return this;
            }

            public String getName() {
                return "symbol";
            }
        };

        public final String getText() {
            return this.text.getValue();
        }

        public final void setText(String string) {
            this.text.setValue(string);
        }

        public final StringProperty textProperty() {
            return this.text;
        }

        public final Node getSymbol() {
            return (Node)this.symbol.getValue();
        }

        public final void setSymbol(Node node) {
            this.symbol.setValue((Object)node);
        }

        public final ObjectProperty<Node> symbolProperty() {
            return this.symbol;
        }

        public LegendItem(String string) {
            this.setText(string);
            this.label.getStyleClass().add((Object)"chart-legend-item");
            this.label.setAlignment(Pos.CENTER_LEFT);
            this.label.setContentDisplay(ContentDisplay.LEFT);
            this.label.setGraphic(this.getSymbol());
            this.getSymbol().getStyleClass().setAll((Object[])new String[]{"chart-legend-item-symbol"});
        }

        public LegendItem(String string, Node node) {
            this(string);
            this.setSymbol(node);
        }
    }
}

