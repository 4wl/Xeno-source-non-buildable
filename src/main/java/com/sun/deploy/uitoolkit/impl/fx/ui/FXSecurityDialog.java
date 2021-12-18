/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.deploy.ui.AppInfo
 *  javafx.application.Platform
 *  javafx.event.ActionEvent
 *  javafx.event.EventHandler
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.scene.control.Button
 *  javafx.scene.layout.HBox
 *  javafx.scene.layout.VBox
 *  javafx.scene.text.Text
 *  javafx.stage.Stage
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.ui.AppInfo;
import com.sun.javafx.stage.StageHelper;
import java.net.URL;
import java.security.cert.Certificate;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

class FXSecurityDialog {
    private Stage stage;
    private Scene scene;
    private Button okButton;
    private Button cancelButton;
    private final Object responseLock = new Object();
    private int response = -1;

    FXSecurityDialog(AppInfo appInfo, final String string, final String string2, String string3, URL uRL, boolean bl, boolean bl2, final String string4, final String string5, String[] arrstring, String[] arrstring2, boolean bl3, Certificate[] arrcertificate, int n, int n2, boolean bl4) {
        Platform.runLater((Runnable)new Runnable(){

            @Override
            public void run() {
                FXSecurityDialog.this.stage = new Stage();
                FXSecurityDialog.this.stage.setTitle(string);
                StageHelper.initSecurityDialog(FXSecurityDialog.this.stage, true);
                VBox vBox = new VBox();
                Text text = new Text(string2);
                vBox.getChildren().add((Object)text);
                HBox hBox = new HBox();
                FXSecurityDialog.this.okButton = new Button(string4);
                FXSecurityDialog.this.cancelButton = new Button(string5);
                hBox.getChildren().add((Object)FXSecurityDialog.this.okButton);
                hBox.getChildren().add((Object)FXSecurityDialog.this.cancelButton);
                vBox.getChildren().add((Object)hBox);
                DialogEventHandler dialogEventHandler = new DialogEventHandler();
                FXSecurityDialog.this.okButton.setOnAction((EventHandler)dialogEventHandler);
                FXSecurityDialog.this.cancelButton.setOnAction((EventHandler)dialogEventHandler);
                FXSecurityDialog.this.scene = new Scene((Parent)vBox, 640.0, 480.0);
                FXSecurityDialog.this.stage.setScene(FXSecurityDialog.this.scene);
            }
        });
    }

    void setVisible(boolean bl) {
        Platform.runLater((Runnable)new Runnable(){

            @Override
            public void run() {
                FXSecurityDialog.this.stage.show();
            }
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    int getResponse() {
        Object object = this.responseLock;
        synchronized (object) {
            if (this.response == -1) {
                try {
                    this.responseLock.wait();
                }
                catch (InterruptedException interruptedException) {
                    System.out.println("interupted " + interruptedException);
                }
            }
            return this.response;
        }
    }

    private class DialogEventHandler
    implements EventHandler<ActionEvent> {
        private DialogEventHandler() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void handle(ActionEvent actionEvent) {
            Object object = FXSecurityDialog.this.responseLock;
            synchronized (object) {
                if (actionEvent.getSource() == FXSecurityDialog.this.okButton) {
                    FXSecurityDialog.this.response = 0;
                } else {
                    FXSecurityDialog.this.response = 1;
                }
                FXSecurityDialog.this.responseLock.notifyAll();
                FXSecurityDialog.this.stage.close();
            }
        }
    }
}

