/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.ActionEvent
 *  javafx.event.EventHandler
 *  javafx.geometry.Insets
 *  javafx.geometry.Pos
 *  javafx.scene.Node
 *  javafx.scene.control.Button
 *  javafx.scene.control.Hyperlink
 *  javafx.scene.control.Label
 *  javafx.scene.control.ScrollPane
 *  javafx.scene.control.Separator
 *  javafx.scene.control.TextArea
 *  javafx.scene.image.ImageView
 *  javafx.scene.layout.BorderPane
 *  javafx.scene.layout.HBox
 *  javafx.scene.layout.Pane
 *  javafx.scene.layout.Priority
 *  javafx.scene.layout.VBox
 *  javafx.stage.Stage
 *  javafx.stage.Window
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.uitoolkit.impl.fx.ui.CertificateDialog;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXDialog;
import com.sun.deploy.uitoolkit.impl.fx.ui.UITextArea;
import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.cert.Certificate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

class MoreInfoDialog
extends FXDialog {
    private Hyperlink details;
    private String[] alerts;
    private String[] infos;
    private int securityInfoCount;
    private Certificate[] certs;
    private int start;
    private int end;
    private boolean sandboxApp = false;
    private final String WARNING_ICON = "warning16.image";
    private final String INFO_ICON = "info16.image";
    private final int VERTICAL_STRUT = 18;
    private final int HORIZONTAL_STRUT = 12;
    private final int TEXT_WIDTH = 326;

    MoreInfoDialog(Stage stage, String[] arrstring, String[] arrstring2, int n, Certificate[] arrcertificate, int n2, int n3, boolean bl) {
        super(ResourceManager.getMessage("security.more.info.title"), (Window)stage, true);
        this.alerts = arrstring;
        this.infos = arrstring2;
        this.securityInfoCount = n;
        this.certs = arrcertificate;
        this.start = n2;
        this.end = n3;
        this.sandboxApp = bl;
        this.initComponents(null, null);
        this.setResizable(false);
    }

    MoreInfoDialog(Stage stage, Pane pane, Throwable throwable, Certificate[] arrcertificate) {
        super(ResourceManager.getMessage("security.more.info.title"));
        this.certs = arrcertificate;
        this.start = 0;
        this.end = arrcertificate == null ? 0 : arrcertificate.length;
        this.initComponents(pane, throwable);
    }

    private void initComponents(Pane pane, Throwable throwable) {
        VBox vBox = new VBox();
        vBox.setId("more-info-dialog");
        if (pane != null) {
            VBox.setVgrow((Node)pane, (Priority)Priority.ALWAYS);
            vBox.getChildren().add((Object)pane);
        } else if (throwable != null) {
            BorderPane borderPane = new BorderPane();
            Label label = new Label(ResourceManager.getString("exception.details.label"));
            borderPane.setLeft((Node)label);
            vBox.getChildren().add((Object)borderPane);
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            throwable.printStackTrace(printWriter);
            TextArea textArea = new TextArea(stringWriter.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setPrefWidth(480.0);
            textArea.setPrefHeight(240.0);
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent((Node)textArea);
            scrollPane.setFitToWidth(true);
            VBox.setVgrow((Node)scrollPane, (Priority)Priority.ALWAYS);
            vBox.getChildren().add((Object)scrollPane);
            if (this.certs != null) {
                vBox.getChildren().add((Object)this.getLinkPanel());
            }
        } else {
            Pane pane2 = this.getSecurityPanel();
            if (pane2.getChildren().size() > 0) {
                VBox.setVgrow((Node)pane2, (Priority)Priority.ALWAYS);
                vBox.getChildren().add((Object)pane2);
            }
            vBox.getChildren().add((Object)this.getIntegrationPanel());
        }
        vBox.getChildren().add((Object)this.getBtnPanel());
        this.setContentPane((Pane)vBox);
    }

    private Pane getSecurityPanel() {
        int n;
        VBox vBox = new VBox();
        boolean bl = this.certs == null;
        int n2 = bl || this.alerts == null ? 0 : 1;
        int n3 = n = this.alerts == null ? 0 : this.alerts.length;
        if (n > n2) {
            vBox.getChildren().add((Object)this.blockPanel("warning16.image", this.alerts, n2, n));
        }
        if ((n = this.securityInfoCount) > n2) {
            vBox.getChildren().add((Object)this.blockPanel("info16.image", this.infos, n2, n));
        }
        if (this.certs != null) {
            vBox.getChildren().add((Object)this.getLinkPanel());
        }
        return vBox;
    }

    private Pane getLinkPanel() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(8.0, 0.0, 0.0, 0.0));
        hBox.setAlignment(Pos.TOP_RIGHT);
        String string = this.sandboxApp ? "sandbox.security.more.info.details" : "security.more.info.details";
        this.details = new Hyperlink(ResourceManager.getMessage(string));
        this.details.setMnemonicParsing(true);
        this.details.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

            public void handle(ActionEvent actionEvent) {
                MoreInfoDialog.this.showCertDetails();
            }
        });
        hBox.getChildren().add((Object)this.details);
        return hBox;
    }

    private Pane getIntegrationPanel() {
        int n = this.securityInfoCount;
        int n2 = this.infos == null ? 0 : this.infos.length;
        return this.blockPanel("info16.image", this.infos, n, n2);
    }

    private Pane getBtnPanel() {
        HBox hBox = new HBox();
        hBox.setId("more-info-dialog-button-panel");
        Button button = new Button(ResourceManager.getMessage("common.close_btn"));
        button.setCancelButton(true);
        button.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

            public void handle(ActionEvent actionEvent) {
                MoreInfoDialog.this.dismissAction();
            }
        });
        button.setDefaultButton(true);
        hBox.getChildren().add((Object)button);
        return hBox;
    }

    private Pane blockPanel(String string, String[] arrstring, int n, int n2) {
        VBox vBox = new VBox(5.0);
        if (arrstring != null) {
            for (int i = n; i < n2; ++i) {
                HBox hBox = new HBox(12.0);
                hBox.setAlignment(Pos.TOP_LEFT);
                ImageView imageView = ResourceManager.getIcon(string);
                UITextArea uITextArea = new UITextArea(326.0);
                uITextArea.setWrapText(true);
                uITextArea.setId("more-info-text-block");
                uITextArea.setText(arrstring[i]);
                if (i > n) {
                    imageView.setVisible(false);
                }
                hBox.getChildren().add((Object)imageView);
                hBox.getChildren().add((Object)uITextArea);
                vBox.getChildren().add((Object)hBox);
                if (i >= n2 - 1) continue;
                vBox.getChildren().add((Object)new Separator());
            }
        }
        return vBox;
    }

    private void showCertDetails() {
        CertificateDialog.showCertificates(this, this.certs, this.start, this.end);
    }

    private void dismissAction() {
        this.hide();
    }
}

