/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.deploy.ui.AppInfo
 *  javafx.event.ActionEvent
 *  javafx.event.EventHandler
 *  javafx.geometry.Insets
 *  javafx.scene.Node
 *  javafx.scene.control.Button
 *  javafx.scene.control.CheckBox
 *  javafx.scene.control.Hyperlink
 *  javafx.scene.control.Label
 *  javafx.scene.control.Tooltip
 *  javafx.scene.layout.BorderPane
 *  javafx.scene.layout.GridPane
 *  javafx.scene.layout.HBox
 *  javafx.scene.layout.Pane
 *  javafx.scene.layout.VBox
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.Paint
 *  javafx.scene.text.Font
 *  javafx.scene.text.FontWeight
 *  javafx.scene.text.Text
 *  javafx.stage.Stage
 *  javafx.stage.Window
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.ui.AppInfo;
import com.sun.deploy.uitoolkit.impl.fx.ui.DialogTemplate;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXDialog;
import com.sun.deploy.uitoolkit.impl.fx.ui.MoreInfoDialog;
import com.sun.deploy.uitoolkit.impl.fx.ui.UITextArea;
import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

public class FXSSV3Dialog {
    private static final int MAIN_TEXT_WIDTH = 460;
    private static final int RISK_TEXT_WIDTH = 540;
    private static final int MAX_URL_WIDTH = 360;
    private AppInfo ainfo;
    private String masthead;
    private String mainText;
    private String location;
    private String prompt;
    private String multiPrompt;
    private String multiText;
    private String runKey;
    private String updateText;
    private String cancelText;
    private URL updateURL;
    private String locationURL = "";
    private String mainJNLPURL;
    private String documentBaseURL;
    private String locationTooltip = "";
    private String mainJNLPTooltip;
    private String documentBaseTooltip;
    private int userAnswer = 1;
    private FXDialog dialog;
    private Button runButton;
    private Button updateButton;
    private Button cancelButton;
    private CheckBox multiClickCheckBox;

    public static int showSSV3Dialog(Object object, AppInfo appInfo, int n, String string, String string2, String string3, String string4, String string5, String string6, String string7, String string8, String string9, String string10, String string11, URL uRL) {
        FXSSV3Dialog fXSSV3Dialog = new FXSSV3Dialog(object, FXSSV3Dialog.getMessage(string));
        fXSSV3Dialog.ainfo = appInfo;
        fXSSV3Dialog.masthead = FXSSV3Dialog.getMessage(string2);
        fXSSV3Dialog.mainText = FXSSV3Dialog.getMessage(string3);
        fXSSV3Dialog.location = FXSSV3Dialog.getMessage(string4);
        fXSSV3Dialog.prompt = FXSSV3Dialog.getMessage(string5);
        fXSSV3Dialog.multiPrompt = FXSSV3Dialog.getMessage(string6);
        fXSSV3Dialog.multiText = FXSSV3Dialog.getMessage(string7);
        fXSSV3Dialog.runKey = string8;
        fXSSV3Dialog.updateText = FXSSV3Dialog.getMessage(string9);
        fXSSV3Dialog.cancelText = FXSSV3Dialog.getMessage(string10);
        fXSSV3Dialog.updateURL = uRL;
        fXSSV3Dialog.initComponents();
        fXSSV3Dialog.setVisible(true);
        return fXSSV3Dialog.getAnswer();
    }

    private FXSSV3Dialog(Object object, String string) {
        Stage stage = null;
        if (object instanceof Stage) {
            stage = (Stage)object;
        }
        this.dialog = new FXDialog(string, (Window)stage, true);
    }

    private void initComponents() {
        try {
            try {
                this.locationURL = this.ainfo.getDisplayFrom();
                this.locationTooltip = this.ainfo.getFrom().toString();
            }
            catch (Exception exception) {
                this.locationURL = "";
            }
            if (this.ainfo.shouldDisplayMainJNLP()) {
                this.mainJNLPURL = this.ainfo.getDisplayMainJNLP();
                this.mainJNLPTooltip = DialogTemplate.getDisplayMainJNLPTooltip(this.ainfo);
            }
            if (this.ainfo.shouldDisplayDocumentBase()) {
                this.documentBaseURL = this.ainfo.getDisplayDocumentBase();
                this.documentBaseTooltip = this.ainfo.getDocumentBase().toString();
            }
            this.dialog.setResizable(false);
            this.dialog.setIconifiable(false);
            Pane pane = this.createContentPane();
            pane.getChildren().add((Object)this.createMastHead());
            pane.getChildren().add((Object)this.createMainContent());
            pane.getChildren().add((Object)this.createOkCancelPanel());
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private Pane createContentPane() {
        VBox vBox = new VBox(){

            protected double computePrefHeight(double d) {
                double d2 = super.computePrefHeight(d);
                return d2;
            }
        };
        vBox.setId("ssv3-content-panel");
        this.dialog.setContentPane((Pane)vBox);
        return vBox;
    }

    private Node createMastHead() {
        UITextArea uITextArea = new UITextArea(this.masthead);
        uITextArea.setId("security-masthead-label");
        return uITextArea;
    }

    private Pane createMainContent() {
        Pane pane = this.createWarningPanel();
        BorderPane borderPane = new BorderPane();
        borderPane.setTop((Node)pane);
        if (this.multiText == null) {
            UITextArea uITextArea = new UITextArea(460.0);
            uITextArea.setText(this.prompt);
            uITextArea.setId("ssv3-prompt");
            borderPane.setBottom((Node)this.createWarningMorePrompt((Node)uITextArea));
        }
        return borderPane;
    }

    private Pane createWarningPanel() {
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft((Node)this.createShieldIcon());
        VBox vBox = this.createLocationPanel();
        borderPane.setCenter((Node)vBox);
        return borderPane;
    }

    private VBox createLocationPanel() {
        VBox vBox = new VBox();
        Text text = new Text(this.mainText);
        text.setId("ssv3-main-text");
        text.setWrappingWidth(460.0);
        vBox.getChildren().add((Object)text);
        Label label = new Label(this.location);
        label.setText(this.location);
        label.setId("ssv3-location-label");
        Label label2 = new Label(this.locationURL);
        label2.setTooltip(new Tooltip(this.locationTooltip));
        label2.setId("ssv3-location-url");
        GridPane gridPane = new GridPane();
        gridPane.setId("ssv3-location-label-url");
        gridPane.add((Node)label, 0, 0);
        gridPane.add((Node)label2, 1, 0);
        int n = 1;
        if (this.mainJNLPURL != null) {
            label2 = new Label(this.mainJNLPURL);
            label2.setTooltip(new Tooltip(this.mainJNLPTooltip));
            label2.setMaxWidth(360.0);
            label2.setId("ssv3-location-url");
            gridPane.add((Node)label2, 1, n++);
        }
        if (this.documentBaseURL != null) {
            label2 = new Label(this.documentBaseURL);
            label2.setTooltip(new Tooltip(this.documentBaseTooltip));
            label2.setMaxWidth(360.0);
            label2.setId("ssv3-location-url");
            gridPane.add((Node)label2, 1, n);
        }
        vBox.getChildren().add((Object)gridPane);
        return vBox;
    }

    private Pane createShieldIcon() {
        Label label = new Label(null, (Node)ResourceManager.getIcon("warning48s.image"));
        label.setId("ssv3-shield");
        VBox vBox = new VBox();
        vBox.getChildren().add((Object)label);
        return vBox;
    }

    private Pane createOkCancelPanel() {
        HBox hBox = new HBox();
        hBox.getStyleClass().add((Object)"security-button-bar");
        this.runButton = new Button(FXSSV3Dialog.getMessage(this.runKey));
        this.runButton.setMnemonicParsing(true);
        Button button = null;
        hBox.getChildren().add((Object)this.runButton);
        this.runButton.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

            public void handle(ActionEvent actionEvent) {
                FXSSV3Dialog.this.runAction();
            }
        });
        this.cancelButton = new Button(this.cancelText);
        this.cancelButton.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

            public void handle(ActionEvent actionEvent) {
                FXSSV3Dialog.this.userAnswer = 1;
                FXSSV3Dialog.this.closeDialog();
            }
        });
        this.cancelButton.setCancelButton(true);
        if (this.updateText != null) {
            this.updateButton = new Button(this.updateText);
            this.updateButton.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

                public void handle(ActionEvent actionEvent) {
                    FXSSV3Dialog.this.updateAction();
                }
            });
            hBox.getChildren().add((Object)this.updateButton);
            button = this.updateButton;
        } else {
            button = this.cancelButton;
        }
        hBox.getChildren().add((Object)this.cancelButton);
        VBox vBox = new VBox();
        this.createMultSelectionBox(vBox);
        vBox.getChildren().add((Object)hBox);
        this.setDefaultButton(button);
        return vBox;
    }

    private void createMultSelectionBox(VBox vBox) {
        if (this.multiPrompt != null && this.multiText != null) {
            this.runButton.setDisable(true);
            Label label = new Label(this.multiPrompt);
            label.setId("ssv3-multi-click");
            VBox vBox2 = this.createWarningMorePrompt((Node)label);
            VBox vBox3 = new VBox(8.0);
            vBox3.getChildren().add((Object)vBox2);
            vBox3.getChildren().add((Object)label);
            HBox hBox = new HBox();
            hBox.getChildren().add((Object)vBox3);
            vBox.getChildren().add((Object)hBox);
            this.multiClickCheckBox = new CheckBox(this.multiText);
            this.multiClickCheckBox.setId("ssv3-checkbox");
            vBox.getChildren().add((Object)this.multiClickCheckBox);
            this.multiClickCheckBox.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

                public void handle(ActionEvent actionEvent) {
                    FXSSV3Dialog.this.runButton.setDisable(!FXSSV3Dialog.this.multiClickCheckBox.isSelected());
                    if (FXSSV3Dialog.this.multiClickCheckBox.isSelected()) {
                        FXSSV3Dialog.this.setDefaultButton(FXSSV3Dialog.this.runButton);
                    } else if (FXSSV3Dialog.this.updateButton != null) {
                        FXSSV3Dialog.this.setDefaultButton(FXSSV3Dialog.this.updateButton);
                    } else if (FXSSV3Dialog.this.cancelButton != null) {
                        FXSSV3Dialog.this.setDefaultButton(FXSSV3Dialog.this.cancelButton);
                    }
                }
            });
        }
    }

    private void setDefaultButton(Button button) {
        this.runButton.setDefaultButton(false);
        if (this.updateButton != null) {
            this.updateButton.setDefaultButton(false);
        }
        this.cancelButton.setDefaultButton(false);
        button.setDefaultButton(true);
    }

    private void runAction() {
        this.userAnswer = 0;
        this.closeDialog();
    }

    private void updateAction() {
        DialogTemplate.showDocument(this.updateURL.toExternalForm());
    }

    private void closeDialog() {
        this.setVisible(false);
    }

    public void setVisible(boolean bl) {
        if (bl) {
            final FXDialog fXDialog = this.dialog;
            this.dialog.centerOnScreen();
            Runnable runnable = new Runnable(){

                @Override
                public void run() {
                    fXDialog.showAndWait();
                }
            };
            runnable.run();
        } else {
            this.dialog.hide();
        }
    }

    private int getAnswer() {
        return this.userAnswer;
    }

    private VBox createWarningMorePrompt(Node node) {
        VBox vBox = new VBox();
        vBox.getChildren().add((Object)this.getSecurityWarning());
        vBox.getChildren().add((Object)this.getMoreInfoButton());
        vBox.getChildren().add((Object)node);
        return vBox;
    }

    private Hyperlink getMoreInfoButton() {
        Hyperlink hyperlink = null;
        hyperlink = new Hyperlink(FXSSV3Dialog.getMessage("dialog.template.more.info2"));
        hyperlink.setMnemonicParsing(true);
        hyperlink.setId("bottom-more-info-link");
        hyperlink.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

            public void handle(ActionEvent actionEvent) {
                FXSSV3Dialog.this.showMoreInfo();
            }
        });
        return hyperlink;
    }

    private static String getMessage(String string) {
        if (string == null) {
            return null;
        }
        return ResourceManager.getMessage(string);
    }

    private BorderPane getSecurityWarning() {
        BorderPane borderPane = new BorderPane();
        Text text = new Text(FXSSV3Dialog.getMessage("dialog.unsigned.security.risk.warning"));
        text.setFill((Paint)Color.web((String)"0xCC0000"));
        text.setFont(Font.font((String)"System", (FontWeight)FontWeight.BOLD, (double)15.0));
        text.setWrappingWidth(540.0);
        borderPane.setLeft((Node)text);
        borderPane.setPadding(new Insets(8.0, 0.0, 0.0, 0.0));
        return borderPane;
    }

    private void showMoreInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        if (FXSSV3Dialog.isLocalApp(this.ainfo)) {
            stringBuilder.append(FXSSV3Dialog.getMessage("sandbox.security.info.local.description"));
        } else {
            stringBuilder.append(FXSSV3Dialog.getMessage("sandbox.security.info.description"));
        }
        stringBuilder.append("\n\n");
        if (this.updateText != null) {
            stringBuilder.append(FXSSV3Dialog.getMessage("deployment.dialog.ssv3.more.insecure"));
            stringBuilder.append("\n\n");
        }
        if (FXSSV3Dialog.isLocalApp(this.ainfo)) {
            stringBuilder.append(FXSSV3Dialog.getMessage("deployment.dialog.ssv3.more.local"));
            stringBuilder.append("\n\n");
        }
        if (this.multiText != null) {
            stringBuilder.append(FXSSV3Dialog.getMessage("deployment.dialog.ssv3.more.multi"));
            stringBuilder.append("\n\n");
        }
        stringBuilder.append(FXSSV3Dialog.getMessage("deployment.dialog.ssv3.more.general"));
        MoreInfoDialog moreInfoDialog = new MoreInfoDialog(this.dialog, new String[]{stringBuilder.toString()}, null, 0, null, 0, 0, false);
        moreInfoDialog.showAndWait();
    }

    private static boolean isLocalApp(AppInfo appInfo) {
        URL uRL = appInfo.getFrom();
        return uRL != null && uRL.getProtocol().equals("file");
    }
}

