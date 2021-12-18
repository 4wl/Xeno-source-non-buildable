/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.deploy.uitoolkit.ui.ConsoleController
 *  com.sun.deploy.uitoolkit.ui.ConsoleHelper
 *  com.sun.deploy.uitoolkit.ui.ConsoleWindow
 *  com.sun.deploy.util.DeploySysAction
 *  com.sun.deploy.util.DeploySysRun
 *  javafx.application.Platform
 *  javafx.event.ActionEvent
 *  javafx.event.EventHandler
 *  javafx.geometry.Orientation
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.control.Button
 *  javafx.scene.control.Label
 *  javafx.scene.control.ScrollBar
 *  javafx.scene.control.ScrollPane
 *  javafx.scene.control.TextArea
 *  javafx.scene.input.Clipboard
 *  javafx.scene.input.ClipboardContent
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.layout.BorderPane
 *  javafx.scene.layout.FlowPane
 *  javafx.scene.layout.Pane
 *  javafx.stage.StageStyle
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.uitoolkit.impl.fx.ui.FXDialog;
import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import com.sun.deploy.uitoolkit.ui.ConsoleController;
import com.sun.deploy.uitoolkit.ui.ConsoleHelper;
import com.sun.deploy.uitoolkit.ui.ConsoleWindow;
import com.sun.deploy.util.DeploySysAction;
import com.sun.deploy.util.DeploySysRun;
import java.text.MessageFormat;
import java.util.Map;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;

public final class FXConsole
implements ConsoleWindow {
    private final boolean USE_TEXT_AREA = true;
    private final ConsoleController controller;
    private FXDialog dialog;
    private ScrollPane sp;
    private TextArea textArea;
    private Label textAreaLabel;

    public static FXConsole create(final ConsoleController consoleController) throws Exception {
        return (FXConsole)DeploySysRun.execute((DeploySysAction)new DeploySysAction(){

            public Object execute() {
                return new FXConsole(consoleController);
            }
        });
    }

    public FXConsole(ConsoleController consoleController) {
        this.controller = consoleController;
        FXConsole.invokeLater(new Runnable(){

            @Override
            public void run() {
                FXConsole.this.dialog = new FXDialog(ResourceManager.getMessage("console.caption"), null, false, StageStyle.DECORATED, false);
                FXConsole.this.dialog.setResizable(true);
                FXConsole.this.dialog.impl_setImportant(false);
                BorderPane borderPane = new BorderPane();
                FXConsole.this.dialog.setContentPane((Pane)borderPane);
                FXConsole.this.dialog.setWidth(470.0);
                FXConsole.this.dialog.setHeight(430.0);
                FXConsole.this.textArea = new TextArea();
                FXConsole.this.textArea.setEditable(false);
                FXConsole.this.textArea.setWrapText(true);
                FXConsole.this.textArea.getStyleClass().add((Object)"multiline-text");
                borderPane.setCenter((Node)FXConsole.this.textArea);
                EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>(){

                    public void handle(KeyEvent keyEvent) {
                        String string = keyEvent.getCharacter();
                        if (string != null && string.length() == 1) {
                            switch (string.charAt(0)) {
                                case 'j': {
                                    FXConsole.this.dumpJCovData();
                                    break;
                                }
                                case 'v': {
                                    FXConsole.this.dumpThreadStack();
                                    break;
                                }
                                case 'p': {
                                    FXConsole.this.reloadProxyConfig();
                                    break;
                                }
                                case 'r': {
                                    FXConsole.this.reloadPolicyConfig();
                                    break;
                                }
                                case 'x': {
                                    FXConsole.this.clearClassLoaderCache();
                                    break;
                                }
                                case 'l': {
                                    FXConsole.this.showClassLoaderCache();
                                    break;
                                }
                                case 'o': {
                                    FXConsole.this.logging();
                                    break;
                                }
                                case 't': {
                                    FXConsole.this.showThreads();
                                    break;
                                }
                                case 's': {
                                    FXConsole.this.showSystemProperties();
                                    break;
                                }
                                case 'h': {
                                    FXConsole.this.showHelp();
                                    break;
                                }
                                case 'm': {
                                    FXConsole.this.showMemory();
                                    break;
                                }
                                case 'c': {
                                    FXConsole.this.clearConsole();
                                    break;
                                }
                                case 'g': {
                                    FXConsole.this.runGC();
                                    break;
                                }
                                case 'f': {
                                    FXConsole.this.runFinalize();
                                    break;
                                }
                                case 'q': {
                                    FXConsole.this.closeConsole();
                                    break;
                                }
                                case '0': {
                                    FXConsole.this.traceLevel0();
                                    break;
                                }
                                case '1': {
                                    FXConsole.this.traceLevel1();
                                    break;
                                }
                                case '2': {
                                    FXConsole.this.traceLevel2();
                                    break;
                                }
                                case '3': {
                                    FXConsole.this.traceLevel3();
                                    break;
                                }
                                case '4': {
                                    FXConsole.this.traceLevel4();
                                    break;
                                }
                                case '5': {
                                    FXConsole.this.traceLevel5();
                                }
                            }
                        }
                    }
                };
                FXConsole.this.dialog.getScene().setOnKeyTyped((EventHandler)eventHandler);
                FXConsole.this.textArea.setOnKeyTyped((EventHandler)eventHandler);
                Button button = new Button(ResourceManager.getMessage("console.clear"));
                Button button2 = new Button(ResourceManager.getMessage("console.copy"));
                Button button3 = new Button(ResourceManager.getMessage("console.close"));
                FlowPane flowPane = new FlowPane();
                flowPane.getStyleClass().add((Object)"button-bar");
                flowPane.setId("console-dialog-button-bar");
                flowPane.getChildren().add((Object)button);
                flowPane.getChildren().add((Object)new Label("    "));
                flowPane.getChildren().add((Object)button2);
                flowPane.getChildren().add((Object)new Label("    "));
                flowPane.getChildren().add((Object)button3);
                borderPane.setBottom((Node)flowPane);
                button.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

                    public void handle(ActionEvent actionEvent) {
                        FXConsole.this.clearConsole();
                    }
                });
                button2.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

                    public void handle(ActionEvent actionEvent) {
                        FXConsole.this.copyConsole();
                    }
                });
                button3.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

                    public void handle(ActionEvent actionEvent) {
                        FXConsole.this.closeConsole();
                    }
                });
            }
        });
    }

    private void dumpJCovData() {
        if (this.controller.isJCovSupported()) {
            if (this.controller.dumpJCovData()) {
                System.out.println(ResourceManager.getMessage("console.jcov.info"));
            } else {
                System.out.println(ResourceManager.getMessage("console.jcov.error"));
            }
        }
    }

    private void dumpThreadStack() {
        if (this.controller.isDumpStackSupported()) {
            System.out.print(ResourceManager.getMessage("console.dump.stack"));
            System.out.print(ResourceManager.getMessage("console.menu.text.top"));
            ConsoleHelper.dumpAllStacks((ConsoleController)this.controller);
            System.out.print(ResourceManager.getMessage("console.menu.text.tail"));
            System.out.print(ResourceManager.getMessage("console.done"));
        }
    }

    private void showThreads() {
        System.out.print(ResourceManager.getMessage("console.dump.thread"));
        ThreadGroup threadGroup = this.controller.getMainThreadGroup();
        ConsoleHelper.dumpThreadGroup((ThreadGroup)threadGroup);
        System.out.println(ResourceManager.getMessage("console.done"));
    }

    private void reloadPolicyConfig() {
        if (this.controller.isSecurityPolicyReloadSupported()) {
            System.out.print(ResourceManager.getMessage("console.reload.policy"));
            this.controller.reloadSecurityPolicy();
            System.out.println(ResourceManager.getMessage("console.completed"));
        }
    }

    private void reloadProxyConfig() {
        if (this.controller.isProxyConfigReloadSupported()) {
            System.out.println(ResourceManager.getMessage("console.reload.proxy"));
            this.controller.reloadProxyConfig();
            System.out.println(ResourceManager.getMessage("console.done"));
        }
    }

    private void showSystemProperties() {
        ConsoleHelper.displaySystemProperties();
    }

    private void showHelp() {
        ConsoleHelper.displayHelp((ConsoleController)this.controller, (ConsoleWindow)this);
    }

    private void showClassLoaderCache() {
        if (this.controller.isDumpClassLoaderSupported()) {
            System.out.println(this.controller.dumpClassLoaders());
        }
    }

    private void clearClassLoaderCache() {
        if (this.controller.isClearClassLoaderSupported()) {
            this.controller.clearClassLoaders();
            System.out.println(ResourceManager.getMessage("console.clear.classloader"));
        }
    }

    private void clearConsole() {
        this.clear();
    }

    private void copyConsole() {
        int n = this.textArea.getSelection().getStart();
        int n2 = this.textArea.getSelection().getEnd();
        if (n2 - n <= 0) {
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(this.textArea.getText());
            Clipboard.getSystemClipboard().setContent((Map)clipboardContent);
        } else {
            this.textArea.copy();
        }
    }

    private void closeConsole() {
        if (this.controller.isIconifiedOnClose()) {
            this.dialog.setIconified(true);
        } else {
            this.dialog.hide();
        }
        this.controller.notifyConsoleClosed();
    }

    private void showMemory() {
        long l = Runtime.getRuntime().freeMemory() / 1024L;
        long l2 = Runtime.getRuntime().totalMemory() / 1024L;
        long l3 = (long)(100.0 / ((double)l2 / (double)l));
        MessageFormat messageFormat = new MessageFormat(ResourceManager.getMessage("console.memory"));
        Object[] arrobject = new Object[]{new Long(l2), new Long(l), new Long(l3)};
        System.out.print(messageFormat.format(arrobject));
        System.out.println(ResourceManager.getMessage("console.completed"));
    }

    private void runFinalize() {
        System.out.print(ResourceManager.getMessage("console.finalize"));
        System.runFinalization();
        System.out.println(ResourceManager.getMessage("console.completed"));
        this.showMemory();
    }

    private void runGC() {
        System.out.print(ResourceManager.getMessage("console.gc"));
        System.gc();
        System.out.println(ResourceManager.getMessage("console.completed"));
        this.showMemory();
    }

    private void traceLevel0() {
        ConsoleHelper.setTraceLevel((int)0);
    }

    private void traceLevel1() {
        ConsoleHelper.setTraceLevel((int)1);
    }

    private void traceLevel2() {
        ConsoleHelper.setTraceLevel((int)2);
    }

    private void traceLevel3() {
        ConsoleHelper.setTraceLevel((int)3);
    }

    private void traceLevel4() {
        ConsoleHelper.setTraceLevel((int)4);
    }

    private void traceLevel5() {
        ConsoleHelper.setTraceLevel((int)5);
    }

    private void logging() {
        if (this.controller.isLoggingSupported()) {
            System.out.println(ResourceManager.getMessage("console.log") + this.controller.toggleLogging() + ResourceManager.getMessage("console.completed"));
        }
    }

    public void clear() {
        FXConsole.invokeLater(new Runnable(){

            @Override
            public void run() {
                FXConsole.this.textArea.setText("");
                ConsoleHelper.displayVersion((ConsoleController)FXConsole.this.controller, (ConsoleWindow)FXConsole.this);
                FXConsole.this.append("\n");
                ConsoleHelper.displayHelp((ConsoleController)FXConsole.this.controller, (ConsoleWindow)FXConsole.this);
            }
        });
    }

    private static void invokeLater(Runnable runnable) {
        Platform.runLater((Runnable)runnable);
    }

    public void append(final String string) {
        FXConsole.invokeLater(new Runnable(){

            @Override
            public void run() {
                ScrollBar scrollBar = FXConsole.this.getVerticalScrollBar();
                boolean bl = scrollBar == null || !scrollBar.isVisible() || scrollBar.getValue() == scrollBar.getMax();
                int n = FXConsole.this.textArea.getText().length();
                if (n > 1) {
                    FXConsole.this.textArea.insertText(n, string);
                } else {
                    FXConsole.this.textArea.setText(string);
                }
                if (bl) {
                    FXConsole.this.setScrollPosition();
                }
            }
        });
    }

    private void setScrollPosition() {
        ScrollBar scrollBar = this.getVerticalScrollBar();
        if (scrollBar != null) {
            double d = scrollBar.getMax();
            double d2 = scrollBar.getValue();
            if (d2 < d) {
                scrollBar.setValue(d);
            }
        }
    }

    private ScrollBar getVerticalScrollBar() {
        return this.findScrollBar((Parent)this.textArea, true);
    }

    private ScrollBar findScrollBar(Parent parent, boolean bl) {
        if (parent instanceof ScrollBar) {
            ScrollBar scrollBar = (ScrollBar)parent;
            if (scrollBar.getOrientation() == Orientation.VERTICAL == bl) {
                return (ScrollBar)parent;
            }
            return null;
        }
        for (Node node : parent.getChildrenUnmodifiable()) {
            ScrollBar scrollBar;
            if (!(node instanceof Parent) || (scrollBar = this.findScrollBar((Parent)node, bl)) == null) continue;
            return scrollBar;
        }
        return null;
    }

    public void setVisible(final boolean bl) {
        FXConsole.invokeLater(new Runnable(){

            @Override
            public void run() {
                FXConsole.this.setVisibleImpl(bl);
            }
        });
    }

    private void setVisibleImpl(boolean bl) {
        if (this.controller.isIconifiedOnClose()) {
            this.dialog.setIconified(!bl);
            this.dialog.show();
        } else {
            if (this.isVisible() != bl) {
                if (bl) {
                    this.dialog.show();
                } else {
                    this.dialog.hide();
                }
            }
            if (bl) {
                this.dialog.toFront();
            }
        }
    }

    public void setTitle(final String string) {
        FXConsole.invokeLater(new Runnable(){

            @Override
            public void run() {
                FXConsole.this.setTitleImpl(string);
            }
        });
    }

    private void setTitleImpl(String string) {
        this.dialog.setTitle(string);
    }

    public String getRecentLog() {
        return "Not supported yet.";
    }

    public boolean isVisible() {
        if (this.controller.isIconifiedOnClose()) {
            return !this.dialog.isIconified();
        }
        return this.dialog.isShowing();
    }

    public void dispose() {
    }
}

