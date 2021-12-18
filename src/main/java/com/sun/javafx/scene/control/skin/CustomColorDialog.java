/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.InvalidationListener
 *  javafx.beans.Observable
 *  javafx.beans.binding.Bindings
 *  javafx.beans.binding.ObjectBinding
 *  javafx.beans.property.DoubleProperty
 *  javafx.beans.property.IntegerProperty
 *  javafx.beans.property.ObjectProperty
 *  javafx.beans.property.Property
 *  javafx.beans.property.SimpleDoubleProperty
 *  javafx.beans.property.SimpleIntegerProperty
 *  javafx.beans.property.SimpleObjectProperty
 *  javafx.beans.value.ObservableNumberValue
 *  javafx.beans.value.ObservableValue
 *  javafx.event.EventHandler
 *  javafx.geometry.Insets
 *  javafx.geometry.Orientation
 *  javafx.geometry.Pos
 *  javafx.geometry.Rectangle2D
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.scene.control.Button
 *  javafx.scene.control.Label
 *  javafx.scene.control.Slider
 *  javafx.scene.control.Toggle
 *  javafx.scene.control.ToggleButton
 *  javafx.scene.control.ToggleGroup
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.layout.Background
 *  javafx.scene.layout.BackgroundFill
 *  javafx.scene.layout.ColumnConstraints
 *  javafx.scene.layout.CornerRadii
 *  javafx.scene.layout.GridPane
 *  javafx.scene.layout.HBox
 *  javafx.scene.layout.Pane
 *  javafx.scene.layout.Priority
 *  javafx.scene.layout.Region
 *  javafx.scene.layout.RowConstraints
 *  javafx.scene.layout.StackPane
 *  javafx.scene.layout.VBox
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.CycleMethod
 *  javafx.scene.paint.LinearGradient
 *  javafx.scene.paint.Paint
 *  javafx.scene.paint.Stop
 *  javafx.stage.Modality
 *  javafx.stage.Screen
 *  javafx.stage.Stage
 *  javafx.stage.StageStyle
 *  javafx.stage.Window
 *  javafx.stage.WindowEvent
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.ColorPickerSkin;
import com.sun.javafx.scene.control.skin.IntegerField;
import com.sun.javafx.scene.control.skin.IntegerFieldSkin;
import com.sun.javafx.scene.control.skin.WebColorField;
import com.sun.javafx.scene.control.skin.WebColorFieldSkin;
import com.sun.javafx.util.Utils;
import java.util.Collection;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class CustomColorDialog
extends HBox {
    private final Stage dialog = new Stage();
    private ColorRectPane colorRectPane;
    private ControlsPane controlsPane;
    private ObjectProperty<Color> currentColorProperty = new SimpleObjectProperty((Object)Color.WHITE);
    private ObjectProperty<Color> customColorProperty = new SimpleObjectProperty((Object)Color.TRANSPARENT);
    private Runnable onSave;
    private Runnable onUse;
    private Runnable onCancel;
    private WebColorField webField = null;
    private Scene customScene;
    private final EventHandler<KeyEvent> keyEventListener = keyEvent -> {
        switch (keyEvent.getCode()) {
            case ESCAPE: {
                this.dialog.setScene(null);
                this.dialog.close();
            }
        }
    };
    private InvalidationListener positionAdjuster = new InvalidationListener(){

        public void invalidated(Observable observable) {
            if (Double.isNaN(CustomColorDialog.this.dialog.getWidth()) || Double.isNaN(CustomColorDialog.this.dialog.getHeight())) {
                return;
            }
            CustomColorDialog.this.dialog.widthProperty().removeListener(CustomColorDialog.this.positionAdjuster);
            CustomColorDialog.this.dialog.heightProperty().removeListener(CustomColorDialog.this.positionAdjuster);
            CustomColorDialog.this.fixPosition();
        }
    };

    public CustomColorDialog(Window window) {
        this.getStyleClass().add((Object)"custom-color-dialog");
        if (window != null) {
            this.dialog.initOwner(window);
        }
        this.dialog.setTitle(ColorPickerSkin.getString("customColorDialogTitle"));
        this.dialog.initModality(Modality.APPLICATION_MODAL);
        this.dialog.initStyle(StageStyle.UTILITY);
        this.dialog.setResizable(false);
        this.colorRectPane = new ColorRectPane();
        this.controlsPane = new ControlsPane();
        CustomColorDialog.setHgrow((Node)this.controlsPane, (Priority)Priority.ALWAYS);
        this.customScene = new Scene((Parent)this);
        Scene scene = window.getScene();
        if (scene != null) {
            if (scene.getUserAgentStylesheet() != null) {
                this.customScene.setUserAgentStylesheet(scene.getUserAgentStylesheet());
            }
            this.customScene.getStylesheets().addAll((Collection)scene.getStylesheets());
        }
        this.getChildren().addAll((Object[])new Node[]{this.colorRectPane, this.controlsPane});
        this.dialog.setScene(this.customScene);
        this.dialog.addEventHandler(KeyEvent.ANY, this.keyEventListener);
    }

    public void setCurrentColor(Color color) {
        this.currentColorProperty.set((Object)color);
    }

    Color getCurrentColor() {
        return (Color)this.currentColorProperty.get();
    }

    ObjectProperty<Color> customColorProperty() {
        return this.customColorProperty;
    }

    void setCustomColor(Color color) {
        this.customColorProperty.set((Object)color);
    }

    Color getCustomColor() {
        return (Color)this.customColorProperty.get();
    }

    public Runnable getOnSave() {
        return this.onSave;
    }

    public void setOnSave(Runnable runnable) {
        this.onSave = runnable;
    }

    public Runnable getOnUse() {
        return this.onUse;
    }

    public void setOnUse(Runnable runnable) {
        this.onUse = runnable;
    }

    public Runnable getOnCancel() {
        return this.onCancel;
    }

    public void setOnCancel(Runnable runnable) {
        this.onCancel = runnable;
    }

    public void setOnHidden(EventHandler<WindowEvent> eventHandler) {
        this.dialog.setOnHidden(eventHandler);
    }

    Stage getDialog() {
        return this.dialog;
    }

    public void show() {
        if (this.dialog.getOwner() != null) {
            this.dialog.widthProperty().addListener(this.positionAdjuster);
            this.dialog.heightProperty().addListener(this.positionAdjuster);
            this.positionAdjuster.invalidated(null);
        }
        if (this.dialog.getScene() == null) {
            this.dialog.setScene(this.customScene);
        }
        this.colorRectPane.updateValues();
        this.dialog.show();
    }

    private void fixPosition() {
        Window window = this.dialog.getOwner();
        Screen screen = Utils.getScreen((Object)window);
        Rectangle2D rectangle2D = screen.getBounds();
        double d = window.getX() + window.getWidth();
        double d2 = window.getX() - this.dialog.getWidth();
        double d3 = rectangle2D.getMaxX() >= d + this.dialog.getWidth() ? d : (rectangle2D.getMinX() <= d2 ? d2 : Math.max(rectangle2D.getMinX(), rectangle2D.getMaxX() - this.dialog.getWidth()));
        double d4 = Math.max(rectangle2D.getMinY(), Math.min(rectangle2D.getMaxY() - this.dialog.getHeight(), window.getY()));
        this.dialog.setX(d3);
        this.dialog.setY(d4);
    }

    public void layoutChildren() {
        super.layoutChildren();
        if (this.dialog.getMinWidth() > 0.0 && this.dialog.getMinHeight() > 0.0) {
            return;
        }
        double d = Math.max(0.0, this.computeMinWidth(this.getHeight()) + (this.dialog.getWidth() - this.customScene.getWidth()));
        double d2 = Math.max(0.0, this.computeMinHeight(this.getWidth()) + (this.dialog.getHeight() - this.customScene.getHeight()));
        this.dialog.setMinWidth(d);
        this.dialog.setMinHeight(d2);
    }

    static double clamp(double d) {
        return d < 0.0 ? 0.0 : (d > 1.0 ? 1.0 : d);
    }

    private static LinearGradient createHueGradient() {
        Stop[] arrstop = new Stop[255];
        for (int i = 0; i < 255; ++i) {
            double d = 1.0 - 0.00392156862745098 * (double)i;
            int n = (int)((double)i / 255.0 * 360.0);
            arrstop[i] = new Stop(d, Color.hsb((double)n, (double)1.0, (double)1.0));
        }
        return new LinearGradient(0.0, 1.0, 0.0, 0.0, true, CycleMethod.NO_CYCLE, arrstop);
    }

    private static int doubleToInt(double d) {
        return (int)(d * 255.0 + 0.5);
    }

    private class ControlsPane
    extends VBox {
        private Label currentColorLabel;
        private Label newColorLabel;
        private Region currentColorRect;
        private Region newColorRect;
        private Region currentTransparent;
        private GridPane currentAndNewColor;
        private Region currentNewColorBorder;
        private ToggleButton hsbButton;
        private ToggleButton rgbButton;
        private ToggleButton webButton;
        private HBox hBox;
        private Label[] labels = new Label[4];
        private Slider[] sliders = new Slider[4];
        private IntegerField[] fields = new IntegerField[4];
        private Label[] units = new Label[4];
        private HBox buttonBox;
        private Region whiteBox;
        private GridPane settingsPane = new GridPane();
        private Property<Number>[] bindedProperties = new Property[4];

        public ControlsPane() {
            this.getStyleClass().add((Object)"controls-pane");
            this.currentNewColorBorder = new Region();
            this.currentNewColorBorder.setId("current-new-color-border");
            this.currentTransparent = new Region();
            this.currentTransparent.getStyleClass().addAll((Object[])new String[]{"transparent-pattern"});
            this.currentColorRect = new Region();
            this.currentColorRect.getStyleClass().add((Object)"color-rect");
            this.currentColorRect.setId("current-color");
            this.currentColorRect.backgroundProperty().bind((ObservableValue)new ObjectBinding<Background>(){
                {
                    this.bind(new Observable[]{CustomColorDialog.this.currentColorProperty});
                }

                protected Background computeValue() {
                    return new Background(new BackgroundFill[]{new BackgroundFill((Paint)CustomColorDialog.this.currentColorProperty.get(), CornerRadii.EMPTY, Insets.EMPTY)});
                }
            });
            this.newColorRect = new Region();
            this.newColorRect.getStyleClass().add((Object)"color-rect");
            this.newColorRect.setId("new-color");
            this.newColorRect.backgroundProperty().bind((ObservableValue)new ObjectBinding<Background>(){
                {
                    this.bind(new Observable[]{CustomColorDialog.this.customColorProperty});
                }

                protected Background computeValue() {
                    return new Background(new BackgroundFill[]{new BackgroundFill((Paint)CustomColorDialog.this.customColorProperty.get(), CornerRadii.EMPTY, Insets.EMPTY)});
                }
            });
            this.currentColorLabel = new Label(ColorPickerSkin.getString("currentColor"));
            this.newColorLabel = new Label(ColorPickerSkin.getString("newColor"));
            this.whiteBox = new Region();
            this.whiteBox.getStyleClass().add((Object)"customcolor-controls-background");
            this.hsbButton = new ToggleButton(ColorPickerSkin.getString("colorType.hsb"));
            this.hsbButton.getStyleClass().add((Object)"left-pill");
            this.rgbButton = new ToggleButton(ColorPickerSkin.getString("colorType.rgb"));
            this.rgbButton.getStyleClass().add((Object)"center-pill");
            this.webButton = new ToggleButton(ColorPickerSkin.getString("colorType.web"));
            this.webButton.getStyleClass().add((Object)"right-pill");
            ToggleGroup toggleGroup = new ToggleGroup();
            this.hBox = new HBox();
            this.hBox.setAlignment(Pos.CENTER);
            this.hBox.getChildren().addAll((Object[])new Node[]{this.hsbButton, this.rgbButton, this.webButton});
            Region region = new Region();
            region.setId("spacer1");
            Region region2 = new Region();
            region2.setId("spacer2");
            Region region3 = new Region();
            region3.setId("spacer-side");
            Region region4 = new Region();
            region4.setId("spacer-side");
            Region region5 = new Region();
            region5.setId("spacer-bottom");
            this.currentAndNewColor = new GridPane();
            this.currentAndNewColor.getColumnConstraints().addAll((Object[])new ColumnConstraints[]{new ColumnConstraints(), new ColumnConstraints()});
            ((ColumnConstraints)this.currentAndNewColor.getColumnConstraints().get(0)).setHgrow(Priority.ALWAYS);
            ((ColumnConstraints)this.currentAndNewColor.getColumnConstraints().get(1)).setHgrow(Priority.ALWAYS);
            this.currentAndNewColor.getRowConstraints().addAll((Object[])new RowConstraints[]{new RowConstraints(), new RowConstraints(), new RowConstraints()});
            ((RowConstraints)this.currentAndNewColor.getRowConstraints().get(2)).setVgrow(Priority.ALWAYS);
            VBox.setVgrow((Node)this.currentAndNewColor, (Priority)Priority.ALWAYS);
            this.currentAndNewColor.getStyleClass().add((Object)"current-new-color-grid");
            this.currentAndNewColor.add((Node)this.currentColorLabel, 0, 0);
            this.currentAndNewColor.add((Node)this.newColorLabel, 1, 0);
            this.currentAndNewColor.add((Node)region, 0, 1, 2, 1);
            this.currentAndNewColor.add((Node)this.currentTransparent, 0, 2, 2, 1);
            this.currentAndNewColor.add((Node)this.currentColorRect, 0, 2);
            this.currentAndNewColor.add((Node)this.newColorRect, 1, 2);
            this.currentAndNewColor.add((Node)this.currentNewColorBorder, 0, 2, 2, 1);
            this.currentAndNewColor.add((Node)region2, 0, 3, 2, 1);
            this.settingsPane = new GridPane();
            this.settingsPane.setId("settings-pane");
            this.settingsPane.getColumnConstraints().addAll((Object[])new ColumnConstraints[]{new ColumnConstraints(), new ColumnConstraints(), new ColumnConstraints(), new ColumnConstraints(), new ColumnConstraints(), new ColumnConstraints()});
            ((ColumnConstraints)this.settingsPane.getColumnConstraints().get(0)).setHgrow(Priority.NEVER);
            ((ColumnConstraints)this.settingsPane.getColumnConstraints().get(2)).setHgrow(Priority.ALWAYS);
            ((ColumnConstraints)this.settingsPane.getColumnConstraints().get(3)).setHgrow(Priority.NEVER);
            ((ColumnConstraints)this.settingsPane.getColumnConstraints().get(4)).setHgrow(Priority.NEVER);
            ((ColumnConstraints)this.settingsPane.getColumnConstraints().get(5)).setHgrow(Priority.NEVER);
            this.settingsPane.add((Node)this.whiteBox, 0, 0, 6, 5);
            this.settingsPane.add((Node)this.hBox, 0, 0, 6, 1);
            this.settingsPane.add((Node)region3, 0, 0);
            this.settingsPane.add((Node)region4, 5, 0);
            this.settingsPane.add((Node)region5, 0, 4);
            CustomColorDialog.this.webField = new WebColorField();
            CustomColorDialog.this.webField.getStyleClass().add((Object)"web-field");
            CustomColorDialog.this.webField.setSkin(new WebColorFieldSkin(CustomColorDialog.this.webField));
            CustomColorDialog.this.webField.valueProperty().bindBidirectional((Property)CustomColorDialog.this.customColorProperty);
            CustomColorDialog.this.webField.visibleProperty().bind((ObservableValue)toggleGroup.selectedToggleProperty().isEqualTo((Object)this.webButton));
            this.settingsPane.add((Node)CustomColorDialog.this.webField, 2, 1);
            for (int i = 0; i < 4; ++i) {
                this.labels[i] = new Label();
                this.labels[i].getStyleClass().add((Object)"settings-label");
                this.sliders[i] = new Slider();
                this.fields[i] = new IntegerField();
                this.fields[i].getStyleClass().add((Object)"color-input-field");
                this.fields[i].setSkin(new IntegerFieldSkin(this.fields[i]));
                this.units[i] = new Label(i == 0 ? "\u00b0" : "%");
                this.units[i].getStyleClass().add((Object)"settings-unit");
                if (i > 0 && i < 3) {
                    this.labels[i].visibleProperty().bind((ObservableValue)toggleGroup.selectedToggleProperty().isNotEqualTo((Object)this.webButton));
                }
                if (i < 3) {
                    this.sliders[i].visibleProperty().bind((ObservableValue)toggleGroup.selectedToggleProperty().isNotEqualTo((Object)this.webButton));
                    this.fields[i].visibleProperty().bind((ObservableValue)toggleGroup.selectedToggleProperty().isNotEqualTo((Object)this.webButton));
                    this.units[i].visibleProperty().bind((ObservableValue)toggleGroup.selectedToggleProperty().isEqualTo((Object)this.hsbButton));
                }
                int n = 1 + i;
                if (i == 3) {
                    ++n;
                }
                this.settingsPane.add((Node)this.labels[i], 1, n);
                this.settingsPane.add((Node)this.sliders[i], 2, n);
                this.settingsPane.add((Node)this.fields[i], 3, n);
                this.settingsPane.add((Node)this.units[i], 4, n);
            }
            this.set(3, ColorPickerSkin.getString("opacity_colon"), 100, (Property<Number>)CustomColorDialog.this.colorRectPane.alpha);
            this.hsbButton.setToggleGroup(toggleGroup);
            this.rgbButton.setToggleGroup(toggleGroup);
            this.webButton.setToggleGroup(toggleGroup);
            toggleGroup.selectedToggleProperty().addListener((observableValue, toggle, toggle2) -> {
                if (toggle2 == null) {
                    toggleGroup.selectToggle(toggle);
                } else if (toggle2 == this.hsbButton) {
                    this.showHSBSettings();
                } else if (toggle2 == this.rgbButton) {
                    this.showRGBSettings();
                } else {
                    this.showWebSettings();
                }
            });
            toggleGroup.selectToggle((Toggle)this.hsbButton);
            this.buttonBox = new HBox();
            this.buttonBox.setId("buttons-hbox");
            Button button = new Button(ColorPickerSkin.getString("Save"));
            button.setDefaultButton(true);
            button.setOnAction(actionEvent -> {
                if (CustomColorDialog.this.onSave != null) {
                    CustomColorDialog.this.onSave.run();
                }
                CustomColorDialog.this.dialog.hide();
            });
            Button button2 = new Button(ColorPickerSkin.getString("Use"));
            button2.setOnAction(actionEvent -> {
                if (CustomColorDialog.this.onUse != null) {
                    CustomColorDialog.this.onUse.run();
                }
                CustomColorDialog.this.dialog.hide();
            });
            Button button3 = new Button(ColorPickerSkin.getString("Cancel"));
            button3.setCancelButton(true);
            button3.setOnAction(actionEvent -> {
                CustomColorDialog.this.customColorProperty.set((Object)CustomColorDialog.this.getCurrentColor());
                if (CustomColorDialog.this.onCancel != null) {
                    CustomColorDialog.this.onCancel.run();
                }
                CustomColorDialog.this.dialog.hide();
            });
            this.buttonBox.getChildren().addAll((Object[])new Node[]{button, button2, button3});
            this.getChildren().addAll((Object[])new Node[]{this.currentAndNewColor, this.settingsPane, this.buttonBox});
        }

        private void showHSBSettings() {
            this.set(0, ColorPickerSkin.getString("hue_colon"), 360, (Property<Number>)CustomColorDialog.this.colorRectPane.hue);
            this.set(1, ColorPickerSkin.getString("saturation_colon"), 100, (Property<Number>)CustomColorDialog.this.colorRectPane.sat);
            this.set(2, ColorPickerSkin.getString("brightness_colon"), 100, (Property<Number>)CustomColorDialog.this.colorRectPane.bright);
        }

        private void showRGBSettings() {
            this.set(0, ColorPickerSkin.getString("red_colon"), 255, (Property<Number>)CustomColorDialog.this.colorRectPane.red);
            this.set(1, ColorPickerSkin.getString("green_colon"), 255, (Property<Number>)CustomColorDialog.this.colorRectPane.green);
            this.set(2, ColorPickerSkin.getString("blue_colon"), 255, (Property<Number>)CustomColorDialog.this.colorRectPane.blue);
        }

        private void showWebSettings() {
            this.labels[0].setText(ColorPickerSkin.getString("web_colon"));
        }

        private void set(int n, String string, int n2, Property<Number> property) {
            this.labels[n].setText(string);
            if (this.bindedProperties[n] != null) {
                this.sliders[n].valueProperty().unbindBidirectional(this.bindedProperties[n]);
                this.fields[n].valueProperty().unbindBidirectional(this.bindedProperties[n]);
            }
            this.sliders[n].setMax((double)n2);
            this.sliders[n].valueProperty().bindBidirectional(property);
            this.labels[n].setLabelFor((Node)this.sliders[n]);
            this.fields[n].setMaxValue(n2);
            this.fields[n].valueProperty().bindBidirectional(property);
            this.bindedProperties[n] = property;
        }
    }

    private class ColorRectPane
    extends HBox {
        private Pane colorRect;
        private Pane colorBar;
        private Pane colorRectOverlayOne;
        private Pane colorRectOverlayTwo;
        private Region colorRectIndicator;
        private Region colorBarIndicator;
        private boolean changeIsLocal = false;
        private DoubleProperty hue = new SimpleDoubleProperty(-1.0){

            protected void invalidated() {
                if (!ColorRectPane.this.changeIsLocal) {
                    ColorRectPane.this.changeIsLocal = true;
                    ColorRectPane.this.updateHSBColor();
                    ColorRectPane.this.changeIsLocal = false;
                }
            }
        };
        private DoubleProperty sat = new SimpleDoubleProperty(-1.0){

            protected void invalidated() {
                if (!ColorRectPane.this.changeIsLocal) {
                    ColorRectPane.this.changeIsLocal = true;
                    ColorRectPane.this.updateHSBColor();
                    ColorRectPane.this.changeIsLocal = false;
                }
            }
        };
        private DoubleProperty bright = new SimpleDoubleProperty(-1.0){

            protected void invalidated() {
                if (!ColorRectPane.this.changeIsLocal) {
                    ColorRectPane.this.changeIsLocal = true;
                    ColorRectPane.this.updateHSBColor();
                    ColorRectPane.this.changeIsLocal = false;
                }
            }
        };
        private IntegerProperty red = new SimpleIntegerProperty(-1){

            protected void invalidated() {
                if (!ColorRectPane.this.changeIsLocal) {
                    ColorRectPane.this.changeIsLocal = true;
                    ColorRectPane.this.updateRGBColor();
                    ColorRectPane.this.changeIsLocal = false;
                }
            }
        };
        private IntegerProperty green = new SimpleIntegerProperty(-1){

            protected void invalidated() {
                if (!ColorRectPane.this.changeIsLocal) {
                    ColorRectPane.this.changeIsLocal = true;
                    ColorRectPane.this.updateRGBColor();
                    ColorRectPane.this.changeIsLocal = false;
                }
            }
        };
        private IntegerProperty blue = new SimpleIntegerProperty(-1){

            protected void invalidated() {
                if (!ColorRectPane.this.changeIsLocal) {
                    ColorRectPane.this.changeIsLocal = true;
                    ColorRectPane.this.updateRGBColor();
                    ColorRectPane.this.changeIsLocal = false;
                }
            }
        };
        private DoubleProperty alpha = new SimpleDoubleProperty(100.0){

            protected void invalidated() {
                if (!ColorRectPane.this.changeIsLocal) {
                    ColorRectPane.this.changeIsLocal = true;
                    CustomColorDialog.this.setCustomColor(new Color(CustomColorDialog.this.getCustomColor().getRed(), CustomColorDialog.this.getCustomColor().getGreen(), CustomColorDialog.this.getCustomColor().getBlue(), CustomColorDialog.clamp(ColorRectPane.this.alpha.get() / 100.0)));
                    ColorRectPane.this.changeIsLocal = false;
                }
            }
        };

        private void updateRGBColor() {
            Color color = Color.rgb((int)this.red.get(), (int)this.green.get(), (int)this.blue.get(), (double)CustomColorDialog.clamp(this.alpha.get() / 100.0));
            this.hue.set(color.getHue());
            this.sat.set(color.getSaturation() * 100.0);
            this.bright.set(color.getBrightness() * 100.0);
            CustomColorDialog.this.setCustomColor(color);
        }

        private void updateHSBColor() {
            Color color = Color.hsb((double)this.hue.get(), (double)CustomColorDialog.clamp(this.sat.get() / 100.0), (double)CustomColorDialog.clamp(this.bright.get() / 100.0), (double)CustomColorDialog.clamp(this.alpha.get() / 100.0));
            this.red.set(CustomColorDialog.doubleToInt(color.getRed()));
            this.green.set(CustomColorDialog.doubleToInt(color.getGreen()));
            this.blue.set(CustomColorDialog.doubleToInt(color.getBlue()));
            CustomColorDialog.this.setCustomColor(color);
        }

        private void colorChanged() {
            if (!this.changeIsLocal) {
                this.changeIsLocal = true;
                this.hue.set(CustomColorDialog.this.getCustomColor().getHue());
                this.sat.set(CustomColorDialog.this.getCustomColor().getSaturation() * 100.0);
                this.bright.set(CustomColorDialog.this.getCustomColor().getBrightness() * 100.0);
                this.red.set(CustomColorDialog.doubleToInt(CustomColorDialog.this.getCustomColor().getRed()));
                this.green.set(CustomColorDialog.doubleToInt(CustomColorDialog.this.getCustomColor().getGreen()));
                this.blue.set(CustomColorDialog.doubleToInt(CustomColorDialog.this.getCustomColor().getBlue()));
                this.changeIsLocal = false;
            }
        }

        public ColorRectPane() {
            this.getStyleClass().add((Object)"color-rect-pane");
            CustomColorDialog.this.customColorProperty().addListener((observableValue, color, color2) -> this.colorChanged());
            this.colorRectIndicator = new Region();
            this.colorRectIndicator.setId("color-rect-indicator");
            this.colorRectIndicator.setManaged(false);
            this.colorRectIndicator.setMouseTransparent(true);
            this.colorRectIndicator.setCache(true);
            StackPane stackPane = new StackPane();
            this.colorRect = new StackPane(){

                public Orientation getContentBias() {
                    return Orientation.VERTICAL;
                }

                protected double computePrefWidth(double d) {
                    return d;
                }

                protected double computeMaxWidth(double d) {
                    return d;
                }
            };
            this.colorRect.getStyleClass().addAll((Object[])new String[]{"color-rect", "transparent-pattern"});
            Pane pane = new Pane();
            pane.backgroundProperty().bind((ObservableValue)new ObjectBinding<Background>(){
                {
                    this.bind(new Observable[]{ColorRectPane.this.hue});
                }

                protected Background computeValue() {
                    return new Background(new BackgroundFill[]{new BackgroundFill((Paint)Color.hsb((double)ColorRectPane.this.hue.getValue(), (double)1.0, (double)1.0), CornerRadii.EMPTY, Insets.EMPTY)});
                }
            });
            this.colorRectOverlayOne = new Pane();
            this.colorRectOverlayOne.getStyleClass().add((Object)"color-rect");
            this.colorRectOverlayOne.setBackground(new Background(new BackgroundFill[]{new BackgroundFill((Paint)new LinearGradient(0.0, 0.0, 1.0, 0.0, true, CycleMethod.NO_CYCLE, new Stop[]{new Stop(0.0, Color.rgb((int)255, (int)255, (int)255, (double)1.0)), new Stop(1.0, Color.rgb((int)255, (int)255, (int)255, (double)0.0))}), CornerRadii.EMPTY, Insets.EMPTY)}));
            EventHandler eventHandler = mouseEvent -> {
                double d = mouseEvent.getX();
                double d2 = mouseEvent.getY();
                this.sat.set(CustomColorDialog.clamp(d / this.colorRect.getWidth()) * 100.0);
                this.bright.set(100.0 - CustomColorDialog.clamp(d2 / this.colorRect.getHeight()) * 100.0);
            };
            this.colorRectOverlayTwo = new Pane();
            this.colorRectOverlayTwo.getStyleClass().addAll((Object[])new String[]{"color-rect"});
            this.colorRectOverlayTwo.setBackground(new Background(new BackgroundFill[]{new BackgroundFill((Paint)new LinearGradient(0.0, 0.0, 0.0, 1.0, true, CycleMethod.NO_CYCLE, new Stop[]{new Stop(0.0, Color.rgb((int)0, (int)0, (int)0, (double)0.0)), new Stop(1.0, Color.rgb((int)0, (int)0, (int)0, (double)1.0))}), CornerRadii.EMPTY, Insets.EMPTY)}));
            this.colorRectOverlayTwo.setOnMouseDragged(eventHandler);
            this.colorRectOverlayTwo.setOnMousePressed(eventHandler);
            Pane pane2 = new Pane();
            pane2.setMouseTransparent(true);
            pane2.getStyleClass().addAll((Object[])new String[]{"color-rect", "color-rect-border"});
            this.colorBar = new Pane();
            this.colorBar.getStyleClass().add((Object)"color-bar");
            this.colorBar.setBackground(new Background(new BackgroundFill[]{new BackgroundFill((Paint)CustomColorDialog.createHueGradient(), CornerRadii.EMPTY, Insets.EMPTY)}));
            this.colorBarIndicator = new Region();
            this.colorBarIndicator.setId("color-bar-indicator");
            this.colorBarIndicator.setMouseTransparent(true);
            this.colorBarIndicator.setCache(true);
            this.colorRectIndicator.layoutXProperty().bind((ObservableValue)this.sat.divide(100).multiply((ObservableNumberValue)this.colorRect.widthProperty()));
            this.colorRectIndicator.layoutYProperty().bind((ObservableValue)Bindings.subtract((int)1, (ObservableNumberValue)this.bright.divide(100)).multiply((ObservableNumberValue)this.colorRect.heightProperty()));
            this.colorBarIndicator.layoutYProperty().bind((ObservableValue)this.hue.divide(360).multiply((ObservableNumberValue)this.colorBar.heightProperty()));
            stackPane.opacityProperty().bind((ObservableValue)this.alpha.divide(100));
            EventHandler eventHandler2 = mouseEvent -> {
                double d = mouseEvent.getY();
                this.hue.set(CustomColorDialog.clamp(d / this.colorRect.getHeight()) * 360.0);
            };
            this.colorBar.setOnMouseDragged(eventHandler2);
            this.colorBar.setOnMousePressed(eventHandler2);
            this.colorBar.getChildren().setAll((Object[])new Node[]{this.colorBarIndicator});
            stackPane.getChildren().setAll((Object[])new Node[]{pane, this.colorRectOverlayOne, this.colorRectOverlayTwo});
            this.colorRect.getChildren().setAll((Object[])new Node[]{stackPane, pane2, this.colorRectIndicator});
            HBox.setHgrow((Node)this.colorRect, (Priority)Priority.SOMETIMES);
            this.getChildren().addAll((Object[])new Node[]{this.colorRect, this.colorBar});
        }

        private void updateValues() {
            if (CustomColorDialog.this.getCurrentColor() == null) {
                CustomColorDialog.this.setCurrentColor(Color.TRANSPARENT);
            }
            this.changeIsLocal = true;
            this.hue.set(CustomColorDialog.this.getCurrentColor().getHue());
            this.sat.set(CustomColorDialog.this.getCurrentColor().getSaturation() * 100.0);
            this.bright.set(CustomColorDialog.this.getCurrentColor().getBrightness() * 100.0);
            this.alpha.set(CustomColorDialog.this.getCurrentColor().getOpacity() * 100.0);
            CustomColorDialog.this.setCustomColor(Color.hsb((double)this.hue.get(), (double)CustomColorDialog.clamp(this.sat.get() / 100.0), (double)CustomColorDialog.clamp(this.bright.get() / 100.0), (double)CustomColorDialog.clamp(this.alpha.get() / 100.0)));
            this.red.set(CustomColorDialog.doubleToInt(CustomColorDialog.this.getCustomColor().getRed()));
            this.green.set(CustomColorDialog.doubleToInt(CustomColorDialog.this.getCustomColor().getGreen()));
            this.blue.set(CustomColorDialog.doubleToInt(CustomColorDialog.this.getCustomColor().getBlue()));
            this.changeIsLocal = false;
        }

        protected void layoutChildren() {
            super.layoutChildren();
            this.colorRectIndicator.autosize();
            double d = Math.min(this.colorRect.getWidth(), this.colorRect.getHeight());
            this.colorRect.resize(d, d);
            this.colorBar.resize(this.colorBar.getWidth(), d);
        }
    }
}

