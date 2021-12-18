/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.Observable
 *  javafx.beans.property.ReadOnlyBooleanProperty
 *  javafx.css.PseudoClass
 *  javafx.event.Event
 *  javafx.event.EventHandler
 *  javafx.event.EventType
 *  javafx.geometry.Point2D
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.scene.control.Button
 *  javafx.scene.control.Label
 *  javafx.scene.control.ToolBar
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.layout.BorderPane
 *  javafx.scene.layout.HBox
 *  javafx.scene.layout.Pane
 *  javafx.scene.layout.Priority
 *  javafx.scene.layout.Region
 *  javafx.scene.layout.StackPane
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.Paint
 *  javafx.scene.shape.Rectangle
 *  javafx.stage.Modality
 *  javafx.stage.Screen
 *  javafx.stage.Stage
 *  javafx.stage.StageStyle
 *  javafx.stage.Window
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.javafx.stage.StageHelper;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

class FXDialog
extends Stage {
    private BorderPane root;
    private StackPane decoratedRoot;
    private ToolBar toolBar;
    private HBox windowBtns;
    private Button minButton;
    private Button maxButton;
    private Rectangle resizeCorner;
    private double mouseDragOffsetX = 0.0;
    private double mouseDragOffsetY = 0.0;
    protected Label titleLabel;
    private static final int HEADER_HEIGHT = 28;
    private static final PseudoClass PSEUDO_CLASS_ACTIVE = PseudoClass.getPseudoClass((String)"active");

    FXDialog(String string) {
        this(string, null, false);
    }

    FXDialog(String string, Window window, boolean bl) {
        this(string, window, bl, StageStyle.TRANSPARENT, true);
    }

    FXDialog(String string, Window window, boolean bl, StageStyle stageStyle, boolean bl2) {
        super(stageStyle);
        StageHelper.initSecurityDialog(this, bl2);
        this.setTitle(string);
        if (window != null) {
            this.initOwner(window);
        }
        if (bl) {
            this.initModality(Modality.WINDOW_MODAL);
        }
        this.resizableProperty().addListener(new InvalidationListener(){

            public void invalidated(Observable observable) {
                FXDialog.this.resizeCorner.setVisible(FXDialog.this.isResizable());
                FXDialog.this.maxButton.setVisible(FXDialog.this.isResizable());
            }
        });
        this.root = new BorderPane();
        if (stageStyle == StageStyle.DECORATED) {
            Scene scene = new Scene((Parent)this.root);
            scene.getStylesheets().addAll((Object[])new String[]{FXDialog.class.getResource("deploydialogs.css").toExternalForm()});
            this.setScene(scene);
            return;
        }
        this.decoratedRoot = new StackPane(){

            protected void layoutChildren() {
                super.layoutChildren();
                if (FXDialog.this.resizeCorner != null) {
                    FXDialog.this.resizeCorner.relocate(this.getWidth() - 20.0, this.getHeight() - 20.0);
                }
            }
        };
        this.decoratedRoot.getChildren().add((Object)this.root);
        Scene scene = new Scene((Parent)this.decoratedRoot);
        String string2 = (String)AccessController.doPrivileged(new PrivilegedAction(){

            public Object run() {
                URL uRL = FXDialog.class.getResource("deploydialogs.css");
                return uRL.toExternalForm();
            }
        });
        scene.getStylesheets().addAll((Object[])new String[]{string2});
        scene.setFill((Paint)Color.TRANSPARENT);
        this.setScene(scene);
        this.decoratedRoot.getStyleClass().add((Object)"decorated-root");
        this.focusedProperty().addListener(new InvalidationListener(){

            public void invalidated(Observable observable) {
                boolean bl = ((ReadOnlyBooleanProperty)observable).get();
                FXDialog.this.decoratedRoot.pseudoClassStateChanged(PSEUDO_CLASS_ACTIVE, bl);
            }
        });
        this.toolBar = new ToolBar();
        this.toolBar.setId("window-header");
        this.toolBar.setPrefHeight(28.0);
        this.toolBar.setMinHeight(28.0);
        this.toolBar.setMaxHeight(28.0);
        this.addDragHandlers((Node)this.toolBar);
        this.titleLabel = new Label();
        this.titleLabel.setId("window-title");
        this.titleLabel.setText(this.getTitle());
        this.titleProperty().addListener(new InvalidationListener(){

            public void invalidated(Observable observable) {
                FXDialog.this.titleLabel.setText(FXDialog.this.getTitle());
            }
        });
        Region region = new Region();
        HBox.setHgrow((Node)region, (Priority)Priority.ALWAYS);
        WindowButton windowButton = new WindowButton("close");
        windowButton.setOnAction(new EventHandler(){

            public void handle(Event event) {
                FXDialog.this.hide();
            }
        });
        this.minButton = new WindowButton("minimize");
        this.minButton.setOnAction(new EventHandler(){

            public void handle(Event event) {
                FXDialog.this.setIconified(!FXDialog.this.isIconified());
            }
        });
        this.maxButton = new WindowButton("maximize");
        this.maxButton.setOnAction(new EventHandler(){
            private double restoreX;
            private double restoreY;
            private double restoreW;
            private double restoreH;

            public void handle(Event event) {
                Screen screen = Screen.getPrimary();
                double d = screen.getVisualBounds().getMinX();
                double d2 = screen.getVisualBounds().getMinY();
                double d3 = screen.getVisualBounds().getWidth();
                double d4 = screen.getVisualBounds().getHeight();
                if (this.restoreW == 0.0 || FXDialog.this.getX() != d || FXDialog.this.getY() != d2 || FXDialog.this.getWidth() != d3 || FXDialog.this.getHeight() != d4) {
                    this.restoreX = FXDialog.this.getX();
                    this.restoreY = FXDialog.this.getY();
                    this.restoreW = FXDialog.this.getWidth();
                    this.restoreH = FXDialog.this.getHeight();
                    FXDialog.this.setX(d);
                    FXDialog.this.setY(d2);
                    FXDialog.this.setWidth(d3);
                    FXDialog.this.setHeight(d4);
                } else {
                    FXDialog.this.setX(this.restoreX);
                    FXDialog.this.setY(this.restoreY);
                    FXDialog.this.setWidth(this.restoreW);
                    FXDialog.this.setHeight(this.restoreH);
                }
            }
        });
        this.windowBtns = new HBox(3.0);
        this.windowBtns.getChildren().addAll((Object[])new Node[]{this.minButton, this.maxButton, windowButton});
        this.toolBar.getItems().addAll((Object[])new Node[]{this.titleLabel, region, this.windowBtns});
        this.root.setTop((Node)this.toolBar);
        this.resizeCorner = new Rectangle(10.0, 10.0);
        this.resizeCorner.setId("window-resize-corner");
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>(){
            private double width;
            private double height;
            private Point2D dragAnchor;

            public void handle(MouseEvent mouseEvent) {
                EventType eventType = mouseEvent.getEventType();
                if (eventType == MouseEvent.MOUSE_PRESSED) {
                    this.width = FXDialog.this.getWidth();
                    this.height = FXDialog.this.getHeight();
                    this.dragAnchor = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                } else if (eventType == MouseEvent.MOUSE_DRAGGED) {
                    FXDialog.this.setWidth(Math.max(FXDialog.this.decoratedRoot.minWidth(-1.0), this.width + (mouseEvent.getSceneX() - this.dragAnchor.getX())));
                    FXDialog.this.setHeight(Math.max(FXDialog.this.decoratedRoot.minHeight(-1.0), this.height + (mouseEvent.getSceneY() - this.dragAnchor.getY())));
                }
            }
        };
        this.resizeCorner.setOnMousePressed((EventHandler)eventHandler);
        this.resizeCorner.setOnMouseDragged((EventHandler)eventHandler);
        this.resizeCorner.setManaged(false);
        this.decoratedRoot.getChildren().add((Object)this.resizeCorner);
    }

    void setContentPane(Pane pane) {
        if (pane.getId() == null) {
            pane.setId("content-pane");
        }
        this.root.setCenter((Node)pane);
    }

    public void setIconifiable(boolean bl) {
        this.minButton.setVisible(bl);
    }

    public void hideWindowTitle() {
        if (this.toolBar != null) {
            this.root.setTop(null);
            this.sizeToScene();
            this.addDragHandlers((Node)this.root);
        }
    }

    private void addDragHandlers(Node node) {
        node.setOnMousePressed((EventHandler)new EventHandler<MouseEvent>(){

            public void handle(MouseEvent mouseEvent) {
                FXDialog.this.mouseDragOffsetX = mouseEvent.getSceneX();
                FXDialog.this.mouseDragOffsetY = mouseEvent.getSceneY();
            }
        });
        node.setOnMouseDragged((EventHandler)new EventHandler<MouseEvent>(){

            public void handle(MouseEvent mouseEvent) {
                FXDialog.this.setX(mouseEvent.getScreenX() - FXDialog.this.mouseDragOffsetX);
                FXDialog.this.setY(mouseEvent.getScreenY() - FXDialog.this.mouseDragOffsetY);
            }
        });
    }

    public static Button createCloseButton() {
        return new WindowButton("black-close");
    }

    private static class WindowButton
    extends Button {
        WindowButton(String string) {
            this.getStyleClass().setAll((Object[])new String[]{"window-button"});
            this.setId("window-" + string + "-button");
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().setAll((Object[])new String[]{"graphic"});
            this.setGraphic((Node)stackPane);
            this.setMinSize(17.0, 17.0);
            this.setPrefSize(17.0, 17.0);
        }
    }
}

