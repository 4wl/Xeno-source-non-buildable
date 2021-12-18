/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.event.Event
 *  javafx.event.EventHandler
 *  javafx.geometry.Pos
 *  javafx.scene.control.Button
 *  javafx.scene.control.Hyperlink
 *  javafx.scene.control.Label
 *  javafx.scene.image.ImageView
 *  javafx.scene.layout.Pane
 *  javafx.scene.layout.StackPane
 *  javafx.scene.layout.VBox
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.uitoolkit.impl.fx.ui.FXDialog;
import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class FXAboutDialog {
    static void showAboutJavaDialog() {
        final FXDialog fXDialog = new FXDialog(ResourceManager.getMessage("dialogfactory.aboutDialogTitle"));
        fXDialog.setResizable(false);
        VBox vBox = new VBox();
        VBox vBox2 = new VBox();
        vBox2.setId("about-dialog-top-panel");
        vBox.getChildren().add((Object)vBox2);
        String string = System.getProperty("java.version");
        int n = string.indexOf(".");
        String string2 = string.substring(n + 1, string.indexOf(".", n + 1));
        ImageView imageView = ResourceManager.getIcon("about.java" + ("6".equals(string2) ? "6" : "") + ".image");
        vBox2.getChildren().add((Object)imageView);
        VBox vBox3 = new VBox();
        vBox3.setId("about-dialog-center-panel");
        vBox2.getChildren().add((Object)vBox3);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(FXAboutDialog.getVersionStr());
        stringBuilder.append("\n");
        stringBuilder.append(ResourceManager.getMessage("about.copyright"));
        stringBuilder.append("\n \n");
        stringBuilder.append(ResourceManager.getMessage("about.prompt.info"));
        Label label = new Label();
        label.setWrapText(true);
        label.setText(stringBuilder.toString());
        label.setPrefWidth(imageView.prefWidth(-1.0) - 16.0);
        label.setMinWidth(Double.NEGATIVE_INFINITY);
        vBox3.getChildren().add((Object)label);
        final String string3 = ResourceManager.getMessage("about.home.link");
        Hyperlink hyperlink = new Hyperlink(string3);
        hyperlink.setOnAction(new EventHandler(){

            public void handle(Event event) {
                FXAboutDialog.browserToUrl(string3);
            }
        });
        vBox3.getChildren().add((Object)hyperlink);
        ImageView imageView2 = ResourceManager.getIcon("sun.logo.image");
        vBox3.getChildren().add((Object)imageView2);
        StackPane stackPane = new StackPane();
        stackPane.getStyleClass().add((Object)"button-bar");
        stackPane.setId("about-dialog-button-bar");
        vBox.getChildren().add((Object)stackPane);
        Button button = new Button(ResourceManager.getMessage("about.option.close"));
        button.setDefaultButton(true);
        button.setOnAction(new EventHandler(){

            public void handle(Event event) {
                fXDialog.close();
            }
        });
        button.setAlignment(Pos.TOP_LEFT);
        stackPane.getChildren().add((Object)button);
        fXDialog.setContentPane((Pane)vBox);
        fXDialog.show();
        button.requestFocus();
    }

    private static void browserToUrl(String string) {
        try {
            Desktop.getDesktop().browse(new URI(string));
        }
        catch (URISyntaxException uRISyntaxException) {
            uRISyntaxException.printStackTrace();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    private static String getVersionStr() {
        int n;
        String string = System.getProperty("java.version");
        int n2 = string.indexOf(".");
        String string2 = string.substring(n2 + 1, string.indexOf(".", n2 + 1));
        int n3 = string.lastIndexOf("_");
        String string3 = null;
        if (n3 != -1 && (string3 = (n = string.indexOf("-")) != -1 ? string.substring(n3 + 1, n) : string.substring(n3 + 1, string.length())).startsWith("0")) {
            string3 = string3.substring(1);
        }
        String string4 = null;
        string4 = string3 != null ? MessageFormat.format(ResourceManager.getMessage("about.java.version.update"), string2, string3) : MessageFormat.format(ResourceManager.getMessage("about.java.version"), string2);
        String string5 = MessageFormat.format(ResourceManager.getMessage("about.java.build"), System.getProperty("java.runtime.version"));
        return string4 + " " + string5;
    }
}

