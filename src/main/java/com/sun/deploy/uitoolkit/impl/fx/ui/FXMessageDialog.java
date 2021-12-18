/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Platform
 *  javafx.event.ActionEvent
 *  javafx.event.EventHandler
 *  javafx.scene.Group
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.scene.control.Button
 *  javafx.scene.text.Text
 *  javafx.stage.Stage
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FXMessageDialog {
    Stage stage;
    Scene scene;
    Group group;
    Button button1;
    private final Object responseLock = new Object();
    private int response = -1;

    public FXMessageDialog() {
        this.stage = new Stage();
        this.group = new Group();
        this.group.getChildren().add((Object)new Text("MessageDialog"));
        this.button1 = new Button("Button");
        this.group.getChildren().add((Object)this.button1);
        this.button1.setOnAction((EventHandler)new DialogEventHandler(1));
        this.scene = new Scene((Parent)this.group);
        Platform.runLater((Runnable)new Runnable(){

            @Override
            public void run() {
                FXMessageDialog.this.stage.show();
            }
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getResponse() {
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

    class DialogEventHandler
    implements EventHandler<ActionEvent> {
        int id;

        DialogEventHandler(int n) {
            this.id = n;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void handle(ActionEvent actionEvent) {
            Object object = FXMessageDialog.this.responseLock;
            synchronized (object) {
                FXMessageDialog.this.response = this.id;
                FXMessageDialog.this.responseLock.notifyAll();
            }
        }
    }
}

