/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.applet2.Applet2Context
 *  com.sun.deploy.trace.Trace
 *  com.sun.deploy.ui.AppInfo
 *  com.sun.deploy.uitoolkit.ToolkitStore
 *  com.sun.deploy.uitoolkit.ui.UIFactory
 *  javafx.event.EventHandler
 *  javafx.geometry.Insets
 *  javafx.scene.Node
 *  javafx.scene.control.Label
 *  javafx.scene.image.ImageView
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.layout.Pane
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.applet2.Applet2Context;
import com.sun.deploy.trace.Trace;
import com.sun.deploy.ui.AppInfo;
import com.sun.deploy.uitoolkit.ToolkitStore;
import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import com.sun.deploy.uitoolkit.ui.UIFactory;
import java.lang.reflect.Method;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class ErrorPane
extends Pane {
    Applet2Context a2c;
    Label label;

    public ErrorPane(Applet2Context applet2Context, Throwable throwable, final boolean bl) {
        this.a2c = applet2Context;
        this.setStyle("-fx-background-color: white; -fx-padding: 4; -fx-border-color: lightgray;");
        ImageView imageView = ResourceManager.getIcon("error.pane.icon");
        this.label = new Label(ResourceManager.getMessage("error.pane.message"), (Node)imageView);
        this.setOnMouseClicked((EventHandler)new EventHandler<MouseEvent>(){

            public void handle(MouseEvent mouseEvent) {
                int n = -1;
                if (bl) {
                    UIFactory uIFactory = ToolkitStore.getUI();
                    AppInfo appInfo = new AppInfo();
                    ToolkitStore.getUI();
                    n = uIFactory.showMessageDialog(null, appInfo, 0, null, ResourceManager.getMessage("applet.error.generic.masthead"), ResourceManager.getMessage("applet.error.generic.body"), null, "applet.error.details.btn", "applet.error.ignore.btn", "applet.error.reload.btn");
                } else {
                    UIFactory uIFactory = ToolkitStore.getUI();
                    AppInfo appInfo = new AppInfo();
                    ToolkitStore.getUI();
                    n = uIFactory.showMessageDialog(null, appInfo, 0, null, ResourceManager.getMessage("applet.error.generic.masthead"), ResourceManager.getMessage("applet.error.generic.body"), null, "applet.error.details.btn", "applet.error.ignore.btn", null);
                }
                ToolkitStore.getUI();
                if (n == 0) {
                    try {
                        Class<?> class_ = Class.forName("sun.plugin.JavaRunTime");
                        Method method = class_.getDeclaredMethod("showJavaConsole", Boolean.TYPE);
                        method.invoke(null, true);
                    }
                    catch (Exception exception) {
                        Trace.ignoredException((Exception)exception);
                    }
                } else {
                    ToolkitStore.getUI();
                    if (n != 1) {
                        ToolkitStore.getUI();
                        if (n == 3) {
                            ErrorPane.this.reloadApplet();
                        }
                    }
                }
            }
        });
        this.getChildren().add((Object)this.label);
    }

    public void layoutChildren() {
        super.layoutChildren();
        Insets insets = this.getInsets();
        this.label.relocate(insets.getLeft(), insets.getTop());
    }

    private void reloadApplet() {
        if (this.a2c != null && this.a2c.getHost() != null) {
            this.a2c.getHost().reloadAppletPage();
        }
    }
}

