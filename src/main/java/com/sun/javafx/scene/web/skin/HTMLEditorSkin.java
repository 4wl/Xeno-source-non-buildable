/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.ConditionalFeature
 *  javafx.application.Platform
 *  javafx.collections.FXCollections
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ObservableList
 *  javafx.css.PseudoClass
 *  javafx.css.StyleableProperty
 *  javafx.geometry.NodeOrientation
 *  javafx.geometry.Orientation
 *  javafx.print.PrinterJob
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.scene.control.Button
 *  javafx.scene.control.ColorPicker
 *  javafx.scene.control.ComboBox
 *  javafx.scene.control.ListCell
 *  javafx.scene.control.ListView
 *  javafx.scene.control.Separator
 *  javafx.scene.control.TextInputControl
 *  javafx.scene.control.Toggle
 *  javafx.scene.control.ToggleButton
 *  javafx.scene.control.ToggleGroup
 *  javafx.scene.control.ToolBar
 *  javafx.scene.control.Tooltip
 *  javafx.scene.image.Image
 *  javafx.scene.image.ImageView
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.layout.ColumnConstraints
 *  javafx.scene.layout.GridPane
 *  javafx.scene.layout.Priority
 *  javafx.scene.paint.Color
 *  javafx.scene.text.Font
 *  javafx.scene.web.HTMLEditor
 *  javafx.scene.web.WebView
 *  javafx.util.Callback
 */
package com.sun.javafx.scene.web.skin;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.scene.control.skin.ColorPickerSkin;
import com.sun.javafx.scene.control.skin.FXVK;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import com.sun.javafx.scene.web.behavior.HTMLEditorBehavior;
import com.sun.javafx.webkit.Accessor;
import com.sun.webkit.WebPage;
import java.security.AccessController;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.css.StyleableProperty;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

public class HTMLEditorSkin
extends BehaviorSkinBase<HTMLEditor, HTMLEditorBehavior> {
    private GridPane gridPane;
    private ToolBar toolbar1;
    private ToolBar toolbar2;
    private Button cutButton;
    private Button copyButton;
    private Button pasteButton;
    private Button insertHorizontalRuleButton;
    private ToggleGroup alignmentToggleGroup;
    private ToggleButton alignLeftButton;
    private ToggleButton alignCenterButton;
    private ToggleButton alignRightButton;
    private ToggleButton alignJustifyButton;
    private ToggleButton bulletsButton;
    private ToggleButton numbersButton;
    private Button indentButton;
    private Button outdentButton;
    private ComboBox<String> formatComboBox;
    private Map<String, String> formatStyleMap;
    private Map<String, String> styleFormatMap;
    private ComboBox<String> fontFamilyComboBox;
    private ComboBox<String> fontSizeComboBox;
    private Map<String, String> fontSizeMap;
    private Map<String, String> sizeFontMap;
    private ToggleButton boldButton;
    private ToggleButton italicButton;
    private ToggleButton underlineButton;
    private ToggleButton strikethroughButton;
    private ColorPicker fgColorButton;
    private ColorPicker bgColorButton;
    private WebView webView;
    private WebPage webPage;
    private static final String CUT_COMMAND = "cut";
    private static final String COPY_COMMAND = "copy";
    private static final String PASTE_COMMAND = "paste";
    private static final String UNDO_COMMAND = "undo";
    private static final String REDO_COMMAND = "redo";
    private static final String INSERT_HORIZONTAL_RULE_COMMAND = "inserthorizontalrule";
    private static final String ALIGN_LEFT_COMMAND = "justifyleft";
    private static final String ALIGN_CENTER_COMMAND = "justifycenter";
    private static final String ALIGN_RIGHT_COMMAND = "justifyright";
    private static final String ALIGN_JUSTIFY_COMMAND = "justifyfull";
    private static final String BULLETS_COMMAND = "insertUnorderedList";
    private static final String NUMBERS_COMMAND = "insertOrderedList";
    private static final String INDENT_COMMAND = "indent";
    private static final String OUTDENT_COMMAND = "outdent";
    private static final String FORMAT_COMMAND = "formatblock";
    private static final String FONT_FAMILY_COMMAND = "fontname";
    private static final String FONT_SIZE_COMMAND = "fontsize";
    private static final String BOLD_COMMAND = "bold";
    private static final String ITALIC_COMMAND = "italic";
    private static final String UNDERLINE_COMMAND = "underline";
    private static final String STRIKETHROUGH_COMMAND = "strikethrough";
    private static final String FOREGROUND_COLOR_COMMAND = "forecolor";
    private static final String BACKGROUND_COLOR_COMMAND = "backcolor";
    private static final Color DEFAULT_BG_COLOR = Color.WHITE;
    private static final Color DEFAULT_FG_COLOR = Color.BLACK;
    private static final String FORMAT_PARAGRAPH = "<p>";
    private static final String FORMAT_HEADING_1 = "<h1>";
    private static final String FORMAT_HEADING_2 = "<h2>";
    private static final String FORMAT_HEADING_3 = "<h3>";
    private static final String FORMAT_HEADING_4 = "<h4>";
    private static final String FORMAT_HEADING_5 = "<h5>";
    private static final String FORMAT_HEADING_6 = "<h6>";
    private static final String SIZE_XX_SMALL = "1";
    private static final String SIZE_X_SMALL = "2";
    private static final String SIZE_SMALL = "3";
    private static final String SIZE_MEDIUM = "4";
    private static final String SIZE_LARGE = "5";
    private static final String SIZE_X_LARGE = "6";
    private static final String SIZE_XX_LARGE = "7";
    private static final String INSERT_NEW_LINE_COMMAND = "insertnewline";
    private static final String INSERT_TAB_COMMAND = "inserttab";
    private static final String[][] DEFAULT_FORMAT_MAPPINGS = new String[][]{{"<p>", "", "3"}, {"<h1>", "bold", "6"}, {"<h2>", "bold", "5"}, {"<h3>", "bold", "4"}, {"<h4>", "bold", "3"}, {"<h5>", "bold", "2"}, {"<h6>", "bold", "1"}};
    private static final String[] DEFAULT_WINDOWS_7_MAPPINGS = new String[]{"Windows 7", "Segoe UI", "12px", "", "120"};
    private static final String[][] DEFAULT_OS_MAPPINGS = new String[][]{{"Windows XP", "Tahoma", "12px", "", "96"}, {"Windows Vista", "Segoe UI", "12px", "", "96"}, DEFAULT_WINDOWS_7_MAPPINGS, {"Mac OS X", "Lucida Grande", "12px", "", "72"}, {"Linux", "Lucida Sans", "12px", "", "96"}};
    private static final String DEFAULT_OS_FONT = HTMLEditorSkin.getOSMappings()[1];
    private ParentTraversalEngine engine;
    private boolean resetToolbarState = false;
    private String cachedHTMLText = "<html><head></head><body contenteditable=\"true\"></body></html>";
    private ListChangeListener<Node> itemsListener = change -> {
        while (change.next()) {
            if (change.getRemovedSize() <= 0) continue;
            for (Node node : change.getList()) {
                if (!(node instanceof WebView)) continue;
                this.webPage.dispose();
            }
        }
    };
    private ResourceBundle resources;
    private boolean enableAtomicityCheck = false;
    private int atomicityCount = 0;
    private boolean isFirstRun = true;
    private static final int FONT_FAMILY_MENUBUTTON_WIDTH = 150;
    private static final int FONT_FAMILY_MENU_WIDTH = 100;
    private static final int FONT_SIZE_MENUBUTTON_WIDTH = 80;
    private static PseudoClass CONTAINS_FOCUS_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass((String)"contains-focus");

    private static String[] getOSMappings() {
        String string = System.getProperty("os.name");
        for (int i = 0; i < DEFAULT_OS_MAPPINGS.length; ++i) {
            if (!string.equals(DEFAULT_OS_MAPPINGS[i][0])) continue;
            return DEFAULT_OS_MAPPINGS[i];
        }
        return DEFAULT_WINDOWS_7_MAPPINGS;
    }

    public HTMLEditorSkin(HTMLEditor hTMLEditor) {
        super(hTMLEditor, new HTMLEditorBehavior(hTMLEditor));
        this.getChildren().clear();
        this.gridPane = new GridPane();
        this.gridPane.getStyleClass().add((Object)"grid");
        this.getChildren().addAll((Object[])new Node[]{this.gridPane});
        this.toolbar1 = new ToolBar();
        this.toolbar1.getStyleClass().add((Object)"top-toolbar");
        this.gridPane.add((Node)this.toolbar1, 0, 0);
        this.toolbar2 = new ToolBar();
        this.toolbar2.getStyleClass().add((Object)"bottom-toolbar");
        this.gridPane.add((Node)this.toolbar2, 0, 1);
        this.webView = new WebView();
        this.gridPane.add((Node)this.webView, 0, 2);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.ALWAYS);
        this.gridPane.getColumnConstraints().add((Object)columnConstraints);
        this.webPage = Accessor.getPageFor(this.webView.getEngine());
        this.webView.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> Platform.runLater((Runnable)new Runnable(){

            @Override
            public void run() {
                HTMLEditorSkin.this.enableAtomicityCheck = true;
                HTMLEditorSkin.this.updateToolbarState(true);
                HTMLEditorSkin.this.enableAtomicityCheck = false;
            }
        }));
        this.webView.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            this.applyTextFormatting();
            if (keyEvent.getCode() == KeyCode.CONTROL || keyEvent.getCode() == KeyCode.META) {
                return;
            }
            if (keyEvent.getCode() == KeyCode.TAB && !keyEvent.isControlDown()) {
                if (!keyEvent.isShiftDown()) {
                    if (this.getCommandState(BULLETS_COMMAND) || this.getCommandState(NUMBERS_COMMAND)) {
                        this.executeCommand(INDENT_COMMAND, null);
                    } else {
                        this.executeCommand(INSERT_TAB_COMMAND, null);
                    }
                } else if (this.getCommandState(BULLETS_COMMAND) || this.getCommandState(NUMBERS_COMMAND)) {
                    this.executeCommand(OUTDENT_COMMAND, null);
                }
                return;
            }
            if (this.fgColorButton != null && this.fgColorButton.isShowing() || this.bgColorButton != null && this.bgColorButton.isShowing()) {
                return;
            }
            Platform.runLater(() -> {
                if (this.webPage.getClientSelectedText().isEmpty()) {
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.HOME || keyEvent.getCode() == KeyCode.END) {
                        this.updateToolbarState(true);
                    } else if (keyEvent.isControlDown() || keyEvent.isMetaDown()) {
                        if (keyEvent.getCode() == KeyCode.B) {
                            this.keyboardShortcuts(BOLD_COMMAND);
                        } else if (keyEvent.getCode() == KeyCode.I) {
                            this.keyboardShortcuts(ITALIC_COMMAND);
                        } else if (keyEvent.getCode() == KeyCode.U) {
                            this.keyboardShortcuts(UNDERLINE_COMMAND);
                        }
                        this.updateToolbarState(true);
                    } else {
                        boolean bl = this.resetToolbarState = keyEvent.getCode() == KeyCode.ENTER;
                        if (this.resetToolbarState && this.getCommandState(BOLD_COMMAND) != this.boldButton.selectedProperty().getValue().booleanValue()) {
                            this.executeCommand(BOLD_COMMAND, this.boldButton.selectedProperty().getValue().toString());
                        }
                        this.updateToolbarState(false);
                    }
                    this.resetToolbarState = false;
                } else if (keyEvent.isShiftDown() && (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.RIGHT)) {
                    this.updateToolbarState(true);
                }
            });
        });
        this.webView.addEventHandler(KeyEvent.KEY_RELEASED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.CONTROL || keyEvent.getCode() == KeyCode.META) {
                return;
            }
            if (this.fgColorButton != null && this.fgColorButton.isShowing() || this.bgColorButton != null && this.bgColorButton.isShowing()) {
                return;
            }
            Platform.runLater(() -> {
                if (this.webPage.getClientSelectedText().isEmpty()) {
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.HOME || keyEvent.getCode() == KeyCode.END) {
                        this.updateToolbarState(true);
                    } else if (keyEvent.isControlDown() || keyEvent.isMetaDown()) {
                        if (keyEvent.getCode() == KeyCode.B) {
                            this.keyboardShortcuts(BOLD_COMMAND);
                        } else if (keyEvent.getCode() == KeyCode.I) {
                            this.keyboardShortcuts(ITALIC_COMMAND);
                        } else if (keyEvent.getCode() == KeyCode.U) {
                            this.keyboardShortcuts(UNDERLINE_COMMAND);
                        }
                        this.updateToolbarState(true);
                    } else {
                        boolean bl = this.resetToolbarState = keyEvent.getCode() == KeyCode.ENTER;
                        if (!this.resetToolbarState) {
                            this.updateToolbarState(false);
                        }
                    }
                    this.resetToolbarState = false;
                }
            });
        });
        ((HTMLEditor)this.getSkinnable()).focusedProperty().addListener((observableValue, bl, bl2) -> Platform.runLater((Runnable)new Runnable((Boolean)bl2){
            final /* synthetic */ Boolean val$newValue;
            {
                this.val$newValue = bl;
            }

            @Override
            public void run() {
                if (this.val$newValue.booleanValue()) {
                    HTMLEditorSkin.this.webView.requestFocus();
                }
            }
        }));
        this.webView.focusedProperty().addListener((observableValue, bl, bl2) -> {
            this.pseudoClassStateChanged(CONTAINS_FOCUS_PSEUDOCLASS_STATE, (boolean)bl2);
            Platform.runLater((Runnable)new Runnable((Boolean)bl2){
                final /* synthetic */ Boolean val$newValue;
                {
                    this.val$newValue = bl;
                }

                @Override
                public void run() {
                    HTMLEditorSkin.this.updateToolbarState(true);
                    if (PlatformImpl.isSupported(ConditionalFeature.VIRTUAL_KEYBOARD)) {
                        Scene scene = ((HTMLEditor)HTMLEditorSkin.this.getSkinnable()).getScene();
                        if (this.val$newValue.booleanValue()) {
                            FXVK.attach((Node)HTMLEditorSkin.this.webView);
                        } else if (scene == null || scene.getWindow() == null || !scene.getWindow().isFocused() || !(scene.getFocusOwner() instanceof TextInputControl)) {
                            FXVK.detach();
                        }
                    }
                }
            });
        });
        this.webView.getEngine().getLoadWorker().workDoneProperty().addListener((observableValue, number, number2) -> {
            Platform.runLater(() -> this.webView.requestLayout());
            double d = this.webView.getEngine().getLoadWorker().getTotalWork();
            if (number2.doubleValue() == d) {
                this.cachedHTMLText = null;
                Platform.runLater(() -> {
                    this.setContentEditable(true);
                    this.updateToolbarState(true);
                    this.updateNodeOrientation();
                });
            }
        });
        this.enableToolbar(true);
        this.setHTMLText(this.cachedHTMLText);
        this.engine = new ParentTraversalEngine((Parent)this.getSkinnable(), new Algorithm(){

            @Override
            public Node select(Node node, Direction direction, TraversalContext traversalContext) {
                return HTMLEditorSkin.this.cutButton;
            }

            @Override
            public Node selectFirst(TraversalContext traversalContext) {
                return HTMLEditorSkin.this.cutButton;
            }

            @Override
            public Node selectLast(TraversalContext traversalContext) {
                return HTMLEditorSkin.this.cutButton;
            }
        });
        ((HTMLEditor)this.getSkinnable()).setImpl_traversalEngine(this.engine);
        this.webView.setFocusTraversable(true);
        this.gridPane.getChildren().addListener(this.itemsListener);
    }

    public final String getHTMLText() {
        return this.cachedHTMLText != null ? this.cachedHTMLText : this.webPage.getHtml(this.webPage.getMainFrame());
    }

    public final void setHTMLText(String string) {
        this.cachedHTMLText = string;
        this.webPage.load(this.webPage.getMainFrame(), string, "text/html");
        Platform.runLater(() -> this.updateToolbarState(true));
    }

    private void populateToolbars() {
        this.resources = ResourceBundle.getBundle(HTMLEditorSkin.class.getName());
        this.cutButton = this.addButton(this.toolbar1, this.resources.getString("cutIcon"), this.resources.getString(CUT_COMMAND), CUT_COMMAND, "html-editor-cut");
        this.copyButton = this.addButton(this.toolbar1, this.resources.getString("copyIcon"), this.resources.getString(COPY_COMMAND), COPY_COMMAND, "html-editor-copy");
        this.pasteButton = this.addButton(this.toolbar1, this.resources.getString("pasteIcon"), this.resources.getString(PASTE_COMMAND), PASTE_COMMAND, "html-editor-paste");
        this.toolbar1.getItems().add((Object)new Separator(Orientation.VERTICAL));
        this.alignmentToggleGroup = new ToggleGroup();
        this.alignLeftButton = this.addToggleButton(this.toolbar1, this.alignmentToggleGroup, this.resources.getString("alignLeftIcon"), this.resources.getString("alignLeft"), ALIGN_LEFT_COMMAND, "html-editor-align-left");
        this.alignCenterButton = this.addToggleButton(this.toolbar1, this.alignmentToggleGroup, this.resources.getString("alignCenterIcon"), this.resources.getString("alignCenter"), ALIGN_CENTER_COMMAND, "html-editor-align-center");
        this.alignRightButton = this.addToggleButton(this.toolbar1, this.alignmentToggleGroup, this.resources.getString("alignRightIcon"), this.resources.getString("alignRight"), ALIGN_RIGHT_COMMAND, "html-editor-align-right");
        this.alignJustifyButton = this.addToggleButton(this.toolbar1, this.alignmentToggleGroup, this.resources.getString("alignJustifyIcon"), this.resources.getString("alignJustify"), ALIGN_JUSTIFY_COMMAND, "html-editor-align-justify");
        this.toolbar1.getItems().add((Object)new Separator(Orientation.VERTICAL));
        this.outdentButton = this.addButton(this.toolbar1, this.resources.getString("outdentIcon"), this.resources.getString(OUTDENT_COMMAND), OUTDENT_COMMAND, "html-editor-outdent");
        if (this.outdentButton.getGraphic() != null) {
            this.outdentButton.getGraphic().setNodeOrientation(NodeOrientation.INHERIT);
        }
        this.indentButton = this.addButton(this.toolbar1, this.resources.getString("indentIcon"), this.resources.getString(INDENT_COMMAND), INDENT_COMMAND, "html-editor-indent");
        if (this.indentButton.getGraphic() != null) {
            this.indentButton.getGraphic().setNodeOrientation(NodeOrientation.INHERIT);
        }
        this.toolbar1.getItems().add((Object)new Separator(Orientation.VERTICAL));
        ToggleGroup toggleGroup = new ToggleGroup();
        this.bulletsButton = this.addToggleButton(this.toolbar1, toggleGroup, this.resources.getString("bulletsIcon"), this.resources.getString("bullets"), BULLETS_COMMAND, "html-editor-bullets");
        if (this.bulletsButton.getGraphic() != null) {
            this.bulletsButton.getGraphic().setNodeOrientation(NodeOrientation.INHERIT);
        }
        this.numbersButton = this.addToggleButton(this.toolbar1, toggleGroup, this.resources.getString("numbersIcon"), this.resources.getString("numbers"), NUMBERS_COMMAND, "html-editor-numbers");
        this.toolbar1.getItems().add((Object)new Separator(Orientation.VERTICAL));
        this.formatComboBox = new ComboBox();
        this.formatComboBox.getStyleClass().add((Object)"font-menu-button");
        this.formatComboBox.setFocusTraversable(false);
        this.formatComboBox.setMinWidth(Double.NEGATIVE_INFINITY);
        this.toolbar2.getItems().add(this.formatComboBox);
        this.formatStyleMap = new HashMap<String, String>();
        this.styleFormatMap = new HashMap<String, String>();
        this.createFormatMenuItem(FORMAT_PARAGRAPH, this.resources.getString("paragraph"));
        Platform.runLater(() -> this.formatComboBox.setValue((Object)this.resources.getString("paragraph")));
        this.createFormatMenuItem(FORMAT_HEADING_1, this.resources.getString("heading1"));
        this.createFormatMenuItem(FORMAT_HEADING_2, this.resources.getString("heading2"));
        this.createFormatMenuItem(FORMAT_HEADING_3, this.resources.getString("heading3"));
        this.createFormatMenuItem(FORMAT_HEADING_4, this.resources.getString("heading4"));
        this.createFormatMenuItem(FORMAT_HEADING_5, this.resources.getString("heading5"));
        this.createFormatMenuItem(FORMAT_HEADING_6, this.resources.getString("heading6"));
        this.formatComboBox.setTooltip(new Tooltip(this.resources.getString("format")));
        this.formatComboBox.valueProperty().addListener((observableValue, string, string2) -> {
            if (string2 == null) {
                this.formatComboBox.setValue(null);
            } else {
                String string3 = this.formatStyleMap.get(string2);
                this.executeCommand(FORMAT_COMMAND, string3);
                this.updateToolbarState(false);
                for (int i = 0; i < DEFAULT_FORMAT_MAPPINGS.length; ++i) {
                    String[] arrstring = DEFAULT_FORMAT_MAPPINGS[i];
                    if (!arrstring[0].equalsIgnoreCase(string3)) continue;
                    this.executeCommand(FONT_SIZE_COMMAND, arrstring[2]);
                    this.updateToolbarState(false);
                    break;
                }
            }
        });
        this.fontFamilyComboBox = new ComboBox();
        this.fontFamilyComboBox.getStyleClass().add((Object)"font-menu-button");
        this.fontFamilyComboBox.setMinWidth(150.0);
        this.fontFamilyComboBox.setPrefWidth(150.0);
        this.fontFamilyComboBox.setMaxWidth(150.0);
        this.fontFamilyComboBox.setFocusTraversable(false);
        this.fontFamilyComboBox.setTooltip(new Tooltip(this.resources.getString("fontFamily")));
        this.toolbar2.getItems().add(this.fontFamilyComboBox);
        this.fontFamilyComboBox.getProperties().put((Object)"comboBoxRowsToMeasureWidth", (Object)0);
        this.fontFamilyComboBox.setCellFactory((Callback)new Callback<ListView<String>, ListCell<String>>(){

            public ListCell<String> call(ListView<String> listView) {
                ListCell<String> listCell = new ListCell<String>(){

                    public void updateItem(String string, boolean bl) {
                        super.updateItem((Object)string, bl);
                        if (string != null) {
                            this.setText(string);
                            this.setFont(new Font(string, 12.0));
                        }
                    }
                };
                listCell.setMinWidth(100.0);
                listCell.setPrefWidth(100.0);
                listCell.setMaxWidth(100.0);
                return listCell;
            }
        });
        Platform.runLater(() -> {
            ObservableList observableList = FXCollections.observableArrayList((Collection)Font.getFamilies());
            for (String string : observableList) {
                if (DEFAULT_OS_FONT.equals(string)) {
                    this.fontFamilyComboBox.setValue((Object)string);
                }
                this.fontFamilyComboBox.setItems(observableList);
            }
        });
        this.fontFamilyComboBox.valueProperty().addListener((observableValue, string, string2) -> this.executeCommand(FONT_FAMILY_COMMAND, (String)string2));
        this.fontSizeComboBox = new ComboBox();
        this.fontSizeComboBox.getStyleClass().add((Object)"font-menu-button");
        this.fontSizeComboBox.setFocusTraversable(false);
        this.toolbar2.getItems().add(this.fontSizeComboBox);
        this.fontSizeMap = new HashMap<String, String>();
        this.sizeFontMap = new HashMap<String, String>();
        this.createFontSizeMenuItem(SIZE_XX_SMALL, this.resources.getString("extraExtraSmall"));
        this.createFontSizeMenuItem(SIZE_X_SMALL, this.resources.getString("extraSmall"));
        this.createFontSizeMenuItem(SIZE_SMALL, this.resources.getString("small"));
        Platform.runLater(() -> this.fontSizeComboBox.setValue((Object)this.resources.getString("small")));
        this.createFontSizeMenuItem(SIZE_MEDIUM, this.resources.getString("medium"));
        this.createFontSizeMenuItem(SIZE_LARGE, this.resources.getString("large"));
        this.createFontSizeMenuItem(SIZE_X_LARGE, this.resources.getString("extraLarge"));
        this.createFontSizeMenuItem(SIZE_XX_LARGE, this.resources.getString("extraExtraLarge"));
        this.fontSizeComboBox.setTooltip(new Tooltip(this.resources.getString("fontSize")));
        this.fontSizeComboBox.setCellFactory((Callback)new Callback<ListView<String>, ListCell<String>>(){

            public ListCell<String> call(ListView<String> listView) {
                ListCell<String> listCell = new ListCell<String>(){

                    public void updateItem(String string, boolean bl) {
                        super.updateItem((Object)string, bl);
                        if (string != null) {
                            this.setText(string);
                            String string2 = string.replaceFirst("[^0-9.].*$", "");
                            this.setFont(new Font((String)HTMLEditorSkin.this.fontFamilyComboBox.getValue(), Double.valueOf(string2).doubleValue()));
                        }
                    }
                };
                return listCell;
            }
        });
        this.fontSizeComboBox.valueProperty().addListener((observableValue, string, string2) -> {
            String string3 = this.getCommandValue(FONT_SIZE_COMMAND);
            if (!string2.equals(string3)) {
                this.executeCommand(FONT_SIZE_COMMAND, this.fontSizeMap.get(string2));
            }
        });
        this.toolbar2.getItems().add((Object)new Separator(Orientation.VERTICAL));
        this.boldButton = this.addToggleButton(this.toolbar2, null, this.resources.getString("boldIcon"), this.resources.getString(BOLD_COMMAND), BOLD_COMMAND, "html-editor-bold");
        this.boldButton.setOnAction(actionEvent -> {
            if (FORMAT_PARAGRAPH.equals(this.formatStyleMap.get(this.formatComboBox.getValue()))) {
                this.executeCommand(BOLD_COMMAND, this.boldButton.selectedProperty().getValue().toString());
            }
        });
        this.italicButton = this.addToggleButton(this.toolbar2, null, this.resources.getString("italicIcon"), this.resources.getString(ITALIC_COMMAND), ITALIC_COMMAND, "html-editor-italic");
        this.underlineButton = this.addToggleButton(this.toolbar2, null, this.resources.getString("underlineIcon"), this.resources.getString(UNDERLINE_COMMAND), UNDERLINE_COMMAND, "html-editor-underline");
        this.strikethroughButton = this.addToggleButton(this.toolbar2, null, this.resources.getString("strikethroughIcon"), this.resources.getString(STRIKETHROUGH_COMMAND), STRIKETHROUGH_COMMAND, "html-editor-strike");
        this.toolbar2.getItems().add((Object)new Separator(Orientation.VERTICAL));
        this.insertHorizontalRuleButton = this.addButton(this.toolbar2, this.resources.getString("insertHorizontalRuleIcon"), this.resources.getString("insertHorizontalRule"), INSERT_HORIZONTAL_RULE_COMMAND, "html-editor-hr");
        this.insertHorizontalRuleButton.setOnAction(actionEvent -> {
            this.executeCommand(INSERT_NEW_LINE_COMMAND, null);
            this.executeCommand(INSERT_HORIZONTAL_RULE_COMMAND, null);
            this.updateToolbarState(false);
        });
        this.fgColorButton = new ColorPicker();
        this.fgColorButton.getStyleClass().add((Object)"html-editor-foreground");
        this.fgColorButton.setFocusTraversable(false);
        this.toolbar1.getItems().add((Object)this.fgColorButton);
        this.fgColorButton.applyCss();
        ColorPickerSkin colorPickerSkin = (ColorPickerSkin)this.fgColorButton.getSkin();
        String string3 = AccessController.doPrivileged(() -> HTMLEditorSkin.class.getResource(this.resources.getString("foregroundColorIcon")).toString());
        ((StyleableProperty)colorPickerSkin.imageUrlProperty()).applyStyle(null, (Object)string3);
        this.fgColorButton.setValue((Object)DEFAULT_FG_COLOR);
        this.fgColorButton.setTooltip(new Tooltip(this.resources.getString("foregroundColor")));
        this.fgColorButton.setOnAction(actionEvent -> {
            Color color = (Color)this.fgColorButton.getValue();
            if (color != null) {
                this.executeCommand(FOREGROUND_COLOR_COMMAND, this.colorValueToHex(color));
                this.fgColorButton.hide();
            }
        });
        this.bgColorButton = new ColorPicker();
        this.bgColorButton.getStyleClass().add((Object)"html-editor-background");
        this.bgColorButton.setFocusTraversable(false);
        this.toolbar1.getItems().add((Object)this.bgColorButton);
        this.bgColorButton.applyCss();
        ColorPickerSkin colorPickerSkin2 = (ColorPickerSkin)this.bgColorButton.getSkin();
        String string4 = AccessController.doPrivileged(() -> HTMLEditorSkin.class.getResource(this.resources.getString("backgroundColorIcon")).toString());
        ((StyleableProperty)colorPickerSkin2.imageUrlProperty()).applyStyle(null, (Object)string4);
        this.bgColorButton.setValue((Object)DEFAULT_BG_COLOR);
        this.bgColorButton.setTooltip(new Tooltip(this.resources.getString("backgroundColor")));
        this.bgColorButton.setOnAction(actionEvent -> {
            Color color = (Color)this.bgColorButton.getValue();
            if (color != null) {
                this.executeCommand(BACKGROUND_COLOR_COMMAND, this.colorValueToHex(color));
                this.bgColorButton.hide();
            }
        });
    }

    private String colorValueToHex(Color color) {
        return String.format((Locale)null, "#%02x%02x%02x", Math.round(color.getRed() * 255.0), Math.round(color.getGreen() * 255.0), Math.round(color.getBlue() * 255.0));
    }

    private Button addButton(ToolBar toolBar, String string, String string2, String string3, String string4) {
        Button button = new Button();
        button.setFocusTraversable(false);
        button.getStyleClass().add((Object)string4);
        toolBar.getItems().add((Object)button);
        Image image = AccessController.doPrivileged(() -> new Image(HTMLEditorSkin.class.getResource(string).toString()));
        ((StyleableProperty)button.graphicProperty()).applyStyle(null, (Object)new ImageView(image));
        button.setTooltip(new Tooltip(string2));
        button.setOnAction(actionEvent -> {
            this.executeCommand(string3, null);
            this.updateToolbarState(false);
        });
        return button;
    }

    private ToggleButton addToggleButton(ToolBar toolBar, ToggleGroup toggleGroup, String string, String string2, String string3, String string4) {
        ToggleButton toggleButton = new ToggleButton();
        toggleButton.setUserData((Object)string3);
        toggleButton.setFocusTraversable(false);
        toggleButton.getStyleClass().add((Object)string4);
        toolBar.getItems().add((Object)toggleButton);
        if (toggleGroup != null) {
            toggleButton.setToggleGroup(toggleGroup);
        }
        Image image = AccessController.doPrivileged(() -> new Image(HTMLEditorSkin.class.getResource(string).toString()));
        ((StyleableProperty)toggleButton.graphicProperty()).applyStyle(null, (Object)new ImageView(image));
        toggleButton.setTooltip(new Tooltip(string2));
        if (!BOLD_COMMAND.equals(string3)) {
            toggleButton.selectedProperty().addListener((observableValue, bl, bl2) -> {
                if (this.getCommandState(string3) != bl2.booleanValue()) {
                    this.executeCommand(string3, null);
                }
            });
        }
        return toggleButton;
    }

    private void createFormatMenuItem(String string, String string2) {
        this.formatComboBox.getItems().add((Object)string2);
        this.formatStyleMap.put(string2, string);
        this.styleFormatMap.put(string, string2);
    }

    private void createFontSizeMenuItem(String string, String string2) {
        this.fontSizeComboBox.getItems().add((Object)string2);
        this.fontSizeMap.put(string2, string);
        this.sizeFontMap.put(string, string2);
    }

    private void updateNodeOrientation() {
        NodeOrientation nodeOrientation = ((HTMLEditor)this.getSkinnable()).getEffectiveNodeOrientation();
        HTMLDocument hTMLDocument = (HTMLDocument)this.webPage.getDocument(this.webPage.getMainFrame());
        HTMLElement hTMLElement = (HTMLElement)hTMLDocument.getDocumentElement();
        if (hTMLElement.getAttribute("dir") == null) {
            hTMLElement.setAttribute("dir", nodeOrientation == NodeOrientation.RIGHT_TO_LEFT ? "rtl" : "ltr");
        }
    }

    private void updateToolbarState(boolean bl) {
        String string;
        Object object;
        Object object2;
        String string2;
        String string3;
        String string4;
        if (!this.webView.isFocused()) {
            return;
        }
        ++this.atomicityCount;
        this.copyButton.setDisable(!this.isCommandEnabled(CUT_COMMAND));
        this.cutButton.setDisable(!this.isCommandEnabled(COPY_COMMAND));
        this.pasteButton.setDisable(!this.isCommandEnabled(PASTE_COMMAND));
        this.insertHorizontalRuleButton.setDisable(!this.isCommandEnabled(INSERT_HORIZONTAL_RULE_COMMAND));
        if (bl) {
            this.alignLeftButton.setDisable(!this.isCommandEnabled(ALIGN_LEFT_COMMAND));
            this.alignLeftButton.setSelected(this.getCommandState(ALIGN_LEFT_COMMAND));
            this.alignCenterButton.setDisable(!this.isCommandEnabled(ALIGN_CENTER_COMMAND));
            this.alignCenterButton.setSelected(this.getCommandState(ALIGN_CENTER_COMMAND));
            this.alignRightButton.setDisable(!this.isCommandEnabled(ALIGN_RIGHT_COMMAND));
            this.alignRightButton.setSelected(this.getCommandState(ALIGN_RIGHT_COMMAND));
            this.alignJustifyButton.setDisable(!this.isCommandEnabled(ALIGN_JUSTIFY_COMMAND));
            this.alignJustifyButton.setSelected(this.getCommandState(ALIGN_JUSTIFY_COMMAND));
        } else if (this.alignmentToggleGroup.getSelectedToggle() != null && this.isCommandEnabled(string4 = this.alignmentToggleGroup.getSelectedToggle().getUserData().toString()) && !this.getCommandState(string4)) {
            this.executeCommand(string4, null);
        }
        if (this.alignmentToggleGroup.getSelectedToggle() == null) {
            this.alignmentToggleGroup.selectToggle((Toggle)this.alignLeftButton);
        }
        this.bulletsButton.setDisable(!this.isCommandEnabled(BULLETS_COMMAND));
        this.bulletsButton.setSelected(this.getCommandState(BULLETS_COMMAND));
        this.numbersButton.setDisable(!this.isCommandEnabled(NUMBERS_COMMAND));
        this.numbersButton.setSelected(this.getCommandState(NUMBERS_COMMAND));
        this.indentButton.setDisable(!this.isCommandEnabled(INDENT_COMMAND));
        this.outdentButton.setDisable(!this.isCommandEnabled(OUTDENT_COMMAND));
        this.formatComboBox.setDisable(!this.isCommandEnabled(FORMAT_COMMAND));
        string4 = this.getCommandValue(FORMAT_COMMAND);
        if (string4 != null) {
            string3 = "<" + string4 + ">";
            string2 = this.styleFormatMap.get(string3);
            object2 = (String)this.formatComboBox.getValue();
            if (this.resetToolbarState || string3.equals("<>") || string3.equalsIgnoreCase("<div>")) {
                this.formatComboBox.setValue((Object)this.resources.getString("paragraph"));
            } else if (object2 != null && !((String)object2).equalsIgnoreCase(string2)) {
                this.formatComboBox.setValue((Object)string2);
            }
        }
        this.fontFamilyComboBox.setDisable(!this.isCommandEnabled(FONT_FAMILY_COMMAND));
        string3 = this.getCommandValue(FONT_FAMILY_COMMAND);
        if (string3 != null) {
            string2 = string3;
            if (string2.startsWith("'")) {
                string2 = string2.substring(1);
            }
            if (string2.endsWith("'")) {
                string2 = string2.substring(0, string2.length() - 1);
            }
            if ((object2 = this.fontFamilyComboBox.getValue()) instanceof String && !object2.equals(string2)) {
                object = this.fontFamilyComboBox.getItems();
                string = null;
                for (String string5 : object) {
                    if (string5.equals(string2)) {
                        string = string5;
                        break;
                    }
                    if (!string5.equals(DEFAULT_OS_FONT) || !string2.equals("Dialog")) continue;
                    string = string5;
                    break;
                }
                if (string != null) {
                    this.fontFamilyComboBox.setValue(string);
                }
            }
        }
        this.fontSizeComboBox.setDisable(!this.isCommandEnabled(FONT_SIZE_COMMAND));
        string2 = this.getCommandValue(FONT_SIZE_COMMAND);
        if (this.resetToolbarState && string2 == null) {
            this.fontSizeComboBox.setValue((Object)this.sizeFontMap.get(SIZE_SMALL));
        } else if (string2 != null) {
            if (!((String)this.fontSizeComboBox.getValue()).equals(this.sizeFontMap.get(string2))) {
                this.fontSizeComboBox.setValue((Object)this.sizeFontMap.get(string2));
            }
        } else if (!((String)this.fontSizeComboBox.getValue()).equals(this.sizeFontMap.get(SIZE_SMALL))) {
            this.fontSizeComboBox.setValue((Object)this.sizeFontMap.get(SIZE_SMALL));
        }
        this.boldButton.setDisable(!this.isCommandEnabled(BOLD_COMMAND));
        this.boldButton.setSelected(this.getCommandState(BOLD_COMMAND));
        this.italicButton.setDisable(!this.isCommandEnabled(ITALIC_COMMAND));
        this.italicButton.setSelected(this.getCommandState(ITALIC_COMMAND));
        this.underlineButton.setDisable(!this.isCommandEnabled(UNDERLINE_COMMAND));
        this.underlineButton.setSelected(this.getCommandState(UNDERLINE_COMMAND));
        this.strikethroughButton.setDisable(!this.isCommandEnabled(STRIKETHROUGH_COMMAND));
        this.strikethroughButton.setSelected(this.getCommandState(STRIKETHROUGH_COMMAND));
        this.fgColorButton.setDisable(!this.isCommandEnabled(FOREGROUND_COLOR_COMMAND));
        object2 = this.getCommandValue(FOREGROUND_COLOR_COMMAND);
        if (object2 != null) {
            object = Color.web((String)HTMLEditorSkin.rgbToHex((String)object2));
            this.fgColorButton.setValue(object);
        }
        this.bgColorButton.setDisable(!this.isCommandEnabled(BACKGROUND_COLOR_COMMAND));
        object = this.getCommandValue(BACKGROUND_COLOR_COMMAND);
        if (object != null) {
            string = Color.web((String)HTMLEditorSkin.rgbToHex((String)object));
            this.bgColorButton.setValue((Object)string);
        }
        this.atomicityCount = this.atomicityCount == 0 ? 0 : (this.atomicityCount = this.atomicityCount - 1);
    }

    private void enableToolbar(boolean bl) {
        Platform.runLater(() -> {
            if (this.copyButton == null) {
                return;
            }
            if (bl) {
                this.copyButton.setDisable(!this.isCommandEnabled(COPY_COMMAND));
                this.cutButton.setDisable(!this.isCommandEnabled(CUT_COMMAND));
                this.pasteButton.setDisable(!this.isCommandEnabled(PASTE_COMMAND));
            } else {
                this.copyButton.setDisable(true);
                this.cutButton.setDisable(true);
                this.pasteButton.setDisable(true);
            }
            this.insertHorizontalRuleButton.setDisable(!bl);
            this.alignLeftButton.setDisable(!bl);
            this.alignCenterButton.setDisable(!bl);
            this.alignRightButton.setDisable(!bl);
            this.alignJustifyButton.setDisable(!bl);
            this.bulletsButton.setDisable(!bl);
            this.numbersButton.setDisable(!bl);
            this.indentButton.setDisable(!bl);
            this.outdentButton.setDisable(!bl);
            this.formatComboBox.setDisable(!bl);
            this.fontFamilyComboBox.setDisable(!bl);
            this.fontSizeComboBox.setDisable(!bl);
            this.boldButton.setDisable(!bl);
            this.italicButton.setDisable(!bl);
            this.underlineButton.setDisable(!bl);
            this.strikethroughButton.setDisable(!bl);
            this.fgColorButton.setDisable(!bl);
            this.bgColorButton.setDisable(!bl);
        });
    }

    private boolean executeCommand(String string, String string2) {
        if (!this.enableAtomicityCheck || this.enableAtomicityCheck && this.atomicityCount == 0) {
            return this.webPage.executeCommand(string, string2);
        }
        return false;
    }

    private boolean isCommandEnabled(String string) {
        return this.webPage.queryCommandEnabled(string);
    }

    private void setContentEditable(boolean bl) {
        HTMLDocument hTMLDocument = (HTMLDocument)this.webPage.getDocument(this.webPage.getMainFrame());
        HTMLElement hTMLElement = (HTMLElement)hTMLDocument.getDocumentElement();
        HTMLElement hTMLElement2 = (HTMLElement)hTMLElement.getElementsByTagName("body").item(0);
        hTMLElement2.setAttribute("contenteditable", Boolean.toString(bl));
    }

    private boolean getCommandState(String string) {
        return this.webPage.queryCommandState(string);
    }

    private String getCommandValue(String string) {
        return this.webPage.queryCommandValue(string);
    }

    private static String rgbToHex(String string) {
        if (string.startsWith("rgba")) {
            String[] arrstring = string.substring(string.indexOf(40) + 1, string.lastIndexOf(41)).split(",");
            string = String.format("#%02X%02X%02X%02X", Integer.parseInt(arrstring[0].trim()), Integer.parseInt(arrstring[1].trim()), Integer.parseInt(arrstring[2].trim()), Integer.parseInt(arrstring[3].trim()));
            if ("#00000000".equals(string)) {
                return "#FFFFFFFF";
            }
        } else if (string.startsWith("rgb")) {
            String[] arrstring = string.substring(string.indexOf(40) + 1, string.lastIndexOf(41)).split(",");
            string = String.format("#%02X%02X%02X", Integer.parseInt(arrstring[0].trim()), Integer.parseInt(arrstring[1].trim()), Integer.parseInt(arrstring[2].trim()));
        }
        return string;
    }

    private void applyTextFormatting() {
        if (this.getCommandState(BULLETS_COMMAND) || this.getCommandState(NUMBERS_COMMAND)) {
            return;
        }
        if (this.webPage.getClientCommittedTextLength() == 0) {
            String string = this.formatStyleMap.get(this.formatComboBox.getValue());
            String string2 = ((String)this.fontFamilyComboBox.getValue()).toString();
            this.executeCommand(FORMAT_COMMAND, string);
            this.executeCommand(FONT_FAMILY_COMMAND, string2);
        }
    }

    public void keyboardShortcuts(String string) {
        if (BOLD_COMMAND.equals(string)) {
            this.boldButton.fire();
        } else if (ITALIC_COMMAND.equals(string)) {
            this.italicButton.setSelected(!this.italicButton.isSelected());
        } else if (UNDERLINE_COMMAND.equals(string)) {
            this.underlineButton.setSelected(!this.underlineButton.isSelected());
        }
    }

    protected void layoutChildren(double d, double d2, double d3, double d4) {
        if (this.isFirstRun) {
            this.populateToolbars();
            this.isFirstRun = false;
        }
        super.layoutChildren(d, d2, d3, d4);
        double d5 = Math.max(this.toolbar1.prefWidth(-1.0), this.toolbar2.prefWidth(-1.0));
        this.toolbar1.setMinWidth(d5);
        this.toolbar1.setPrefWidth(d5);
        this.toolbar2.setMinWidth(d5);
        this.toolbar2.setPrefWidth(d5);
    }

    public void print(PrinterJob printerJob) {
        this.webView.getEngine().print(printerJob);
    }
}

