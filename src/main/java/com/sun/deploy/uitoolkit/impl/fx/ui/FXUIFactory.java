/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.deploy.resources.ResourceManager
 *  com.sun.deploy.security.CredentialInfo
 *  com.sun.deploy.trace.Trace
 *  com.sun.deploy.ui.AppInfo
 *  com.sun.deploy.uitoolkit.ui.ConsoleController
 *  com.sun.deploy.uitoolkit.ui.ConsoleWindow
 *  com.sun.deploy.uitoolkit.ui.DialogHook
 *  com.sun.deploy.uitoolkit.ui.ModalityHelper
 *  com.sun.deploy.uitoolkit.ui.NativeMixedCodeDialog
 *  com.sun.deploy.uitoolkit.ui.PluginUIFactory
 *  com.sun.deploy.util.DeploySysAction
 *  com.sun.deploy.util.DeploySysRun
 *  javafx.application.Platform
 *  javafx.collections.FXCollections
 *  javafx.geometry.Pos
 *  javafx.scene.Node
 *  javafx.scene.control.Label
 *  javafx.scene.control.ListView
 *  javafx.scene.control.ScrollPane
 *  javafx.scene.control.SelectionMode
 *  javafx.scene.control.Tab
 *  javafx.scene.control.TabPane
 *  javafx.scene.layout.BorderPane
 *  javafx.scene.layout.HBox
 *  javafx.scene.layout.Pane
 *  javafx.stage.FileChooser
 *  javafx.stage.FileChooser$ExtensionFilter
 *  javafx.stage.Stage
 *  javafx.stage.Window
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.security.CredentialInfo;
import com.sun.deploy.trace.Trace;
import com.sun.deploy.ui.AppInfo;
import com.sun.deploy.uitoolkit.impl.fx.FXPluginToolkit;
import com.sun.deploy.uitoolkit.impl.fx.ui.DialogTemplate;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXAboutDialog;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXConsole;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXModalityHelper;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXSSV3Dialog;
import com.sun.deploy.uitoolkit.impl.fx.ui.MixedCodeInSwing;
import com.sun.deploy.uitoolkit.impl.fx.ui.UITextArea;
import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import com.sun.deploy.uitoolkit.ui.ConsoleController;
import com.sun.deploy.uitoolkit.ui.ConsoleWindow;
import com.sun.deploy.uitoolkit.ui.DialogHook;
import com.sun.deploy.uitoolkit.ui.ModalityHelper;
import com.sun.deploy.uitoolkit.ui.NativeMixedCodeDialog;
import com.sun.deploy.uitoolkit.ui.PluginUIFactory;
import com.sun.deploy.util.DeploySysAction;
import com.sun.deploy.util.DeploySysRun;
import com.sun.javafx.application.PlatformImpl;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.Callable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class FXUIFactory
extends PluginUIFactory {
    public int showMessageDialog(Object object, AppInfo appInfo, int n, String string, String string2, String string3, String string4, String string5, String string6, String string7, String string8) {
        switch (n) {
            default: {
                return FXUIFactory.showContentDialog(object, appInfo, string, string3, true, string5, string6);
            }
            case 0: {
                if (string4 != null) {
                    return FXUIFactory.showErrorDialog(object, appInfo, string, string3, null, string5, string6, null, (Object)FXUIFactory.getDetailPanel(string4), null);
                }
                return FXUIFactory.showErrorDialog(object, appInfo, string, string2, string3, string5, string6, string7);
            }
            case 1: {
                FXUIFactory.showInformationDialog(object, string, string2, string3);
                return -1;
            }
            case 2: {
                return FXUIFactory.showWarningDialog(object, appInfo, string, string2, string3, string5, string6);
            }
            case 3: {
                return FXUIFactory.showConfirmDialog(object, appInfo, string, string2, string4, string5, string6, true);
            }
            case 5: {
                return FXUIFactory.showIntegrationDialog(object, appInfo);
            }
            case 7: {
                return FXUIFactory.showApiDialog(null, appInfo, string, string3, string2, string5, string6, false);
            }
            case 4: 
        }
        return FXUIFactory.showMixedCodeDialog(object, appInfo, string, string2, string3, string4, string5, string6, true, string8);
    }

    public void showExceptionDialog(Object object, AppInfo appInfo, Throwable throwable, String string, String string2, String string3, Certificate[] arrcertificate) {
        if (arrcertificate == null) {
            FXUIFactory.showExceptionDialog(object, throwable, string2, string3, string);
        } else {
            FXUIFactory.showCertificateExceptionDialog(object, appInfo, throwable, string3, string, arrcertificate);
        }
    }

    public CredentialInfo showPasswordDialog(Object object, String string, String string2, boolean bl, boolean bl2, CredentialInfo credentialInfo, boolean bl3, String string3, String string4) {
        return FXUIFactory.showPasswordDialog0(object, string, string2, bl, bl2, credentialInfo, bl3, string3, string4);
    }

    public int showSecurityDialog(AppInfo appInfo, String string, String string2, String string3, URL uRL, boolean bl, boolean bl2, String string4, String string5, String[] arrstring, String[] arrstring2, boolean bl3, Certificate[] arrcertificate, int n, int n2, boolean bl4) {
        return FXUIFactory.showSecurityDialog0(appInfo, string, string2, string3, uRL, bl, bl2, string4, string5, arrstring, arrstring2, bl3, arrcertificate, n, n2, bl4, false, false, false);
    }

    public int showSecurityDialog(AppInfo appInfo, String string, String string2, String string3, URL uRL, boolean bl, boolean bl2, String string4, String string5, String[] arrstring, String[] arrstring2, boolean bl3, Certificate[] arrcertificate, int n, int n2, boolean bl4, boolean bl5, boolean bl6) {
        return FXUIFactory.showSecurityDialog0(appInfo, string, string2, string3, uRL, bl, bl2, string4, string5, arrstring, arrstring2, bl3, arrcertificate, n, n2, bl4, bl5, false, bl6);
    }

    public int showSandboxSecurityDialog(AppInfo appInfo, String string, String string2, String string3, URL uRL, boolean bl, boolean bl2, String string4, String string5, String[] arrstring, String[] arrstring2, boolean bl3, Certificate[] arrcertificate, int n, int n2, boolean bl4, boolean bl5) {
        return FXUIFactory.showSecurityDialog0(appInfo, string, string2, string3, uRL, bl, bl2, string4, string5, arrstring, arrstring2, bl3, arrcertificate, n, n2, bl4, false, true, bl5);
    }

    public void showAboutJavaDialog() {
        Platform.runLater((Runnable)new Runnable(){

            @Override
            public void run() {
                FXAboutDialog.showAboutJavaDialog();
            }
        });
    }

    public int showListDialog(Object object, String string, String string2, String string3, boolean bl, Vector vector, TreeMap treeMap) {
        ListView listView = new ListView();
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setItems(FXCollections.observableList((List)vector));
        if (vector.size() > 0) {
            listView.getSelectionModel().select(0);
        }
        return FXUIFactory.showListDialog0(object, string, string2, string3, bl, listView, treeMap);
    }

    public int showUpdateCheckDialog() {
        return FXUIFactory.showUpdateCheckDialog0();
    }

    public ConsoleWindow getConsole(ConsoleController consoleController) {
        return new FXConsole(consoleController);
    }

    public void setDialogHook(DialogHook dialogHook) {
    }

    public ModalityHelper getModalityHelper() {
        return new FXModalityHelper();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int showErrorDialog(Object object, final AppInfo appInfo, final String string, final String string2, final String string3, final String string4, final String string5, final Throwable throwable, final Object object2, final Certificate[] arrcertificate) {
        final Stage stage = FXUIFactory.beforeDialog((Stage)object);
        try {
            int n = FXPluginToolkit.callAndWait(new Callable<Integer>(){

                @Override
                public Integer call() {
                    DialogTemplate dialogTemplate = new DialogTemplate(appInfo, stage, string, string2);
                    dialogTemplate.setErrorContent(string3, string4, string5, throwable, object2, arrcertificate, false);
                    FXUIFactory.placeWindow((Window)dialogTemplate.getDialog());
                    dialogTemplate.setVisible(true);
                    int n = dialogTemplate.getUserAnswer();
                    return new Integer(n);
                }
            });
            System.err.println("FXUIFactory.showErrorDialog: shutting down the FX toolkit");
            try {
                PlatformImpl.tkExit();
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            int n2 = n;
            return n2;
        }
        catch (Throwable throwable2) {
            int n = -1;
            return n;
        }
        finally {
            FXUIFactory.afterDialog();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int showErrorDialog(Object object, final AppInfo appInfo, String string, final String string2, final String string3, String string4, final String string5, final String string6) {
        final String string7 = string4 == null ? ResourceManager.getString("common.ok_btn") : string4;
        String string8 = string == null ? ResourceManager.getString("error.default.title") : string;
        final Stage stage = FXUIFactory.beforeDialog((Stage)object);
        try {
            int n = FXPluginToolkit.callAndWait(new Callable<Integer>(){

                @Override
                public Integer call() {
                    String string = ResourceManager.getString("error.default.title");
                    DialogTemplate dialogTemplate = new DialogTemplate(appInfo, stage, string, string2);
                    dialogTemplate.setMultiButtonErrorContent(string3, string7, string5, string6);
                    FXUIFactory.placeWindow((Window)dialogTemplate.getDialog());
                    dialogTemplate.setVisible(true);
                    int n = dialogTemplate.getUserAnswer();
                    return new Integer(n);
                }
            });
            return n;
        }
        catch (Throwable throwable) {
            int n = -1;
            return n;
        }
        finally {
            FXUIFactory.afterDialog();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int showContentDialog(Object object, final AppInfo appInfo, final String string, final String string2, final boolean bl, final String string3, final String string4) {
        final Stage stage = FXUIFactory.beforeDialog((Stage)object);
        try {
            int n = FXPluginToolkit.callAndWait(new Callable<Integer>(){

                @Override
                public Integer call() {
                    DialogTemplate dialogTemplate = new DialogTemplate(appInfo, stage, string, null);
                    dialogTemplate.setSimpleContent(string2, bl, null, string3, string4, false, false);
                    FXUIFactory.placeWindow((Window)dialogTemplate.getDialog());
                    dialogTemplate.setVisible(true);
                    int n = dialogTemplate.getUserAnswer();
                    return new Integer(n);
                }
            });
            return n;
        }
        catch (Throwable throwable) {
            int n = -1;
            return n;
        }
        finally {
            FXUIFactory.afterDialog();
        }
    }

    public static void showInformationDialog(final Object object, final String string, final String string2, final String string3) {
        final String string4 = ResourceManager.getString("common.ok_btn");
        final AppInfo appInfo = new AppInfo();
        try {
            FXPluginToolkit.callAndWait(new Callable<Void>(){

                @Override
                public Void call() {
                    DialogTemplate dialogTemplate = new DialogTemplate(appInfo, (Stage)object, string, string2);
                    dialogTemplate.setInfoContent(string3, string4);
                    FXUIFactory.placeWindow((Window)dialogTemplate.getDialog());
                    dialogTemplate.setVisible(true);
                    return null;
                }
            });
        }
        catch (Throwable throwable) {
            Trace.ignored((Throwable)throwable);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int showWarningDialog(Object object, AppInfo appInfo, final String string, final String string2, final String string3, String string4, String string5) {
        final AppInfo appInfo2 = appInfo == null ? new AppInfo() : appInfo;
        final String string6 = string4 == null ? ResourceManager.getString("common.ok_btn") : string4;
        final String string7 = string5 == null ? ResourceManager.getString("common.cancel_btn") : string5;
        final Stage stage = FXUIFactory.beforeDialog((Stage)object);
        try {
            int n = FXPluginToolkit.callAndWait(new Callable<Integer>(){

                @Override
                public Integer call() {
                    DialogTemplate dialogTemplate = new DialogTemplate(appInfo2, stage, string, string2);
                    dialogTemplate.setSimpleContent(string3, false, null, string6, string7, true, true);
                    FXUIFactory.placeWindow((Window)dialogTemplate.getDialog());
                    dialogTemplate.setVisible(true);
                    int n = dialogTemplate.getUserAnswer();
                    return new Integer(n);
                }
            });
            return n;
        }
        catch (Throwable throwable) {
            int n = -1;
            return n;
        }
        finally {
            FXUIFactory.afterDialog();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int showConfirmDialog(Object object, AppInfo appInfo, final String string, final String string2, final String string3, final String string4, final String string5, final boolean bl) {
        final AppInfo appInfo2 = appInfo == null ? new AppInfo() : appInfo;
        final Stage stage = FXUIFactory.beforeDialog((Stage)object);
        try {
            int n = FXPluginToolkit.callAndWait(new Callable<Integer>(){

                @Override
                public Integer call() {
                    DialogTemplate dialogTemplate = new DialogTemplate(appInfo2, stage, string, string2);
                    dialogTemplate.setSimpleContent(null, false, string3, string4, string5, true, bl);
                    FXUIFactory.placeWindow((Window)dialogTemplate.getDialog());
                    dialogTemplate.setVisible(true);
                    int n = dialogTemplate.getUserAnswer();
                    return new Integer(n);
                }
            });
            return n;
        }
        catch (Throwable throwable) {
            int n = -1;
            return n;
        }
        finally {
            FXUIFactory.afterDialog();
        }
    }

    public static int showMixedCodeDialog(Object object, AppInfo appInfo, String string, String string2, String string3, String string4, String string5, String string6, boolean bl, String string7) {
        boolean bl2 = appInfo != null;
        String string8 = "";
        String string9 = "";
        if (bl2) {
            string8 = appInfo.getVendor();
            string9 = appInfo.getTitle();
        }
        String string10 = "security.dialog.nativemixcode." + (bl2 ? "js." : "");
        String string11 = bl2 ? com.sun.deploy.resources.ResourceManager.getString((String)(string10 + "appLabelWebsite")) : "";
        String string12 = bl2 ? com.sun.deploy.resources.ResourceManager.getString((String)(string10 + "appLabelPublisher")) : "";
        String string13 = bl2 ? appInfo.getDisplayFrom() : "";
        try {
            if (NativeMixedCodeDialog.isSupported()) {
                String string14 = com.sun.deploy.resources.ResourceManager.getString((String)"dialog.template.more.info");
                String string15 = com.sun.deploy.resources.ResourceManager.getString((String)"common.close_btn");
                String string16 = com.sun.deploy.resources.ResourceManager.getString((String)"security.more.info.title");
                String string17 = com.sun.deploy.resources.ResourceManager.getString((String)"security.dialog.mixcode.info1") + "\n\n" + com.sun.deploy.resources.ResourceManager.getString((String)"security.dialog.mixcode.info2") + "\n\n" + com.sun.deploy.resources.ResourceManager.getString((String)"security.dialog.mixcode.info3");
                String string18 = com.sun.deploy.resources.ResourceManager.getString((String)"dialog.template.name");
                appInfo = appInfo == null ? new AppInfo() : appInfo;
                return NativeMixedCodeDialog.show((String)string, (String)string2, (String)string3, (String)string4, (String)string5, (String)string6, (String)string14, (String)string15, (String)string16, (String)string17, (String)string18, (String)string9, (String)string11, (String)string13, (String)string12, (String)string8, (String)string7);
            }
            return MixedCodeInSwing.show(object, appInfo, string, string2, string3, string4, string5, string6, bl, bl2, string7);
        }
        catch (Throwable throwable) {
            Trace.ignored((Throwable)throwable);
            return -1;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int showSecurityDialog0(final AppInfo appInfo, final String string, final String string2, final String string3, final URL uRL, final boolean bl, final boolean bl2, final String string4, final String string5, final String[] arrstring, final String[] arrstring2, final boolean bl3, final Certificate[] arrcertificate, final int n, final int n2, final boolean bl4, final boolean bl5, final boolean bl6, final boolean bl7) {
        int n3;
        final Stage stage = FXUIFactory.beforeDialog(null);
        try {
            n3 = FXPluginToolkit.callAndWait(new Callable<Integer>(){

                @Override
                public Integer call() {
                    String string6 = string4;
                    String string22 = string2;
                    String string32 = string;
                    String[] arrstring3 = new String[]{};
                    if (arrstring2 != null) {
                        arrstring3 = arrstring2;
                    }
                    DialogTemplate dialogTemplate = new DialogTemplate(appInfo, stage, string32, string22);
                    int n3 = arrstring3.length;
                    arrstring3 = FXUIFactory.addDetail(arrstring3, appInfo, true, true);
                    appInfo.setVendor(string3);
                    appInfo.setFrom(uRL);
                    if (bl5) {
                        dialogTemplate.setSecurityContent(bl, bl2, string4, string5, arrstring, arrstring3, n3, bl3, arrcertificate, n, n2, bl4);
                    } else {
                        dialogTemplate.setNewSecurityContent(bl, bl2, string6, string5, arrstring, arrstring3, n3, bl3, arrcertificate, n, n2, bl4, bl6, bl7);
                    }
                    FXUIFactory.placeWindow((Window)dialogTemplate.getDialog());
                    dialogTemplate.setVisible(true);
                    int n22 = dialogTemplate.getUserAnswer();
                    return new Integer(n22);
                }
            });
        }
        catch (Throwable throwable) {
            int n4 = -1;
            return n4;
        }
        finally {
            FXUIFactory.afterDialog();
        }
        return n3;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int showIntegrationDialog(Object object, final AppInfo appInfo) {
        final Stage stage = FXUIFactory.beforeDialog((Stage)object);
        try {
            int n = FXPluginToolkit.callAndWait(new Callable<Integer>(){

                @Override
                public Integer call() {
                    String string = ResourceManager.getString("integration.title");
                    boolean bl = appInfo.getDesktopHint() || appInfo.getMenuHint();
                    String string2 = "integration.text.shortcut";
                    string2 = bl ? "integration.text.both" : "integration.text.association";
                    String string3 = ResourceManager.getString(string2);
                    String[] arrstring = new String[]{};
                    String[] arrstring2 = new String[]{};
                    arrstring2 = FXUIFactory.addDetail(arrstring2, appInfo, false, true);
                    String[] arrstring3 = new String[]{};
                    arrstring3 = FXUIFactory.addDetail(arrstring3, appInfo, true, false);
                    boolean bl2 = arrstring2.length + arrstring3.length > 1;
                    String string4 = ResourceManager.getString("common.ok_btn");
                    String string5 = ResourceManager.getString("integration.skip.button");
                    DialogTemplate dialogTemplate = new DialogTemplate(appInfo, stage, string, string3);
                    dialogTemplate.setSecurityContent(false, false, string4, string5, arrstring2, arrstring3, 0, bl2, null, 0, 0, false);
                    FXUIFactory.placeWindow((Window)dialogTemplate.getDialog());
                    dialogTemplate.setVisible(true);
                    int n = dialogTemplate.getUserAnswer();
                    return new Integer(n);
                }
            });
            return n;
        }
        catch (Throwable throwable) {
            int n = -1;
            return n;
        }
        finally {
            FXUIFactory.afterDialog();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int showUpdateCheckDialog0() {
        final String string = ResourceManager.getMessage("autoupdatecheck.caption");
        final String string2 = ResourceManager.getMessage("autoupdatecheck.message");
        final String string3 = ResourceManager.getMessage("autoupdatecheck.masthead");
        final AppInfo appInfo = new AppInfo();
        final Stage stage = FXUIFactory.beforeDialog(null);
        try {
            int n = FXPluginToolkit.callAndWait(new Callable<Integer>(){

                @Override
                public Integer call() {
                    DialogTemplate dialogTemplate = new DialogTemplate(appInfo, stage, string, string3);
                    dialogTemplate.setUpdateCheckContent(string2, "autoupdatecheck.buttonYes", "autoupdatecheck.buttonNo", "autoupdatecheck.buttonAskLater");
                    FXUIFactory.placeWindow((Window)dialogTemplate.getDialog());
                    dialogTemplate.setVisible(true);
                    int n = dialogTemplate.getUserAnswer();
                    return new Integer(n);
                }
            });
            return n;
        }
        catch (Throwable throwable) {
            int n = 3;
            return n;
        }
        finally {
            FXUIFactory.afterDialog();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int showListDialog0(Object object, final String string, final String string2, final String string3, final boolean bl, final ListView listView, final TreeMap treeMap) {
        final String string4 = ResourceManager.getString("common.ok_btn");
        final String string5 = ResourceManager.getString("common.cancel_btn");
        final Stage stage = FXUIFactory.beforeDialog((Stage)object);
        try {
            int n = FXPluginToolkit.callAndWait(new Callable<Integer>(){

                @Override
                public Integer call() {
                    DialogTemplate dialogTemplate = new DialogTemplate(new AppInfo(), stage, string, string2);
                    dialogTemplate.setListContent(string3, listView, bl, string4, string5, treeMap);
                    FXUIFactory.placeWindow((Window)dialogTemplate.getDialog());
                    dialogTemplate.setVisible(true);
                    int n = dialogTemplate.getUserAnswer();
                    return new Integer(n);
                }
            });
            return n;
        }
        catch (Throwable throwable) {
            int n = -1;
            return n;
        }
        finally {
            FXUIFactory.afterDialog();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int showApiDialog(Object object, AppInfo appInfo, final String string, final String string2, final String string3, final String string4, final String string5, final boolean bl) {
        final String string6 = ResourceManager.getString("common.ok_btn");
        final String string7 = ResourceManager.getString("common.cancel_btn");
        final AppInfo appInfo2 = appInfo == null ? new AppInfo() : appInfo;
        final Stage stage = FXUIFactory.beforeDialog((Stage)object);
        try {
            int n = FXPluginToolkit.callAndWait(new Callable<Integer>(){

                @Override
                public Integer call() {
                    DialogTemplate dialogTemplate = new DialogTemplate(appInfo2, stage, string, string2);
                    dialogTemplate.setApiContent(string4, string3, string5, bl, string6, string7);
                    FXUIFactory.placeWindow((Window)dialogTemplate.getDialog());
                    dialogTemplate.setVisible(true);
                    int n = dialogTemplate.getUserAnswer();
                    return new Integer(n);
                }
            });
            return n;
        }
        catch (Throwable throwable) {
            int n = -1;
            return n;
        }
        finally {
            FXUIFactory.afterDialog();
        }
    }

    public static void showExceptionDialog(Object object, Throwable throwable, String string, String string2, String string3) {
        String string4 = ResourceManager.getString("common.ok_btn");
        String string5 = ResourceManager.getString("common.detail.button");
        if (string2 == null) {
            string2 = throwable.toString();
        }
        if (string3 == null) {
            string3 = ResourceManager.getString("error.default.title");
        }
        FXUIFactory.showErrorDialog(object, new AppInfo(), string3, string, string2, string4, string5, throwable, null, null);
    }

    public static void showCertificateExceptionDialog(Object object, AppInfo appInfo, Throwable throwable, String string, String string2, Certificate[] arrcertificate) {
        String string3 = ResourceManager.getString("common.ok_btn");
        String string4 = ResourceManager.getString("common.detail.button");
        if (string == null) {
            string = throwable.toString();
        }
        if (string2 == null) {
            string2 = ResourceManager.getString("error.default.title");
        }
        FXUIFactory.showErrorDialog(object, appInfo, string2, string, null, string3, string4, throwable, null, arrcertificate);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static CredentialInfo showPasswordDialog0(Object object, final String string, final String string2, final boolean bl, final boolean bl2, final CredentialInfo credentialInfo, final boolean bl3, final String string3, final String string4) {
        final Stage stage = FXUIFactory.beforeDialog((Stage)object);
        try {
            CredentialInfo credentialInfo2 = FXPluginToolkit.callAndWait(new Callable<CredentialInfo>(){

                @Override
                public CredentialInfo call() {
                    CredentialInfo credentialInfo3 = null;
                    CredentialInfo credentialInfo2 = credentialInfo;
                    DialogTemplate dialogTemplate = new DialogTemplate(new AppInfo(), stage, string, "");
                    if (credentialInfo2 == null) {
                        credentialInfo2 = new CredentialInfo();
                    }
                    dialogTemplate.setPasswordContent(string2, bl, bl2, credentialInfo2.getUserName(), credentialInfo2.getDomain(), bl3, credentialInfo2.getPassword(), string3, string4);
                    FXUIFactory.placeWindow((Window)dialogTemplate.getDialog());
                    dialogTemplate.setVisible(true);
                    int n = dialogTemplate.getUserAnswer();
                    if (n == 0 || n == 2) {
                        credentialInfo3 = new CredentialInfo();
                        credentialInfo3.setUserName(dialogTemplate.getUserName());
                        credentialInfo3.setDomain(dialogTemplate.getDomain());
                        credentialInfo3.setPassword(dialogTemplate.getPassword());
                        credentialInfo3.setPasswordSaveApproval(dialogTemplate.isPasswordSaved());
                    }
                    return credentialInfo3;
                }
            });
            return credentialInfo2;
        }
        catch (Throwable throwable) {
            CredentialInfo credentialInfo3 = null;
            return credentialInfo3;
        }
        finally {
            FXUIFactory.afterDialog();
        }
    }

    public int showSSVDialog(Object object, AppInfo appInfo, String string, String string2, String string3, String string4, URL uRL, String string5, String string6, String string7, String string8, String string9) {
        return FXUIFactory.showSSVDialog0(object, appInfo, string, string2, string3, string4, uRL, string5, string6, string7, string8, string9);
    }

    private File[] showFileChooser_priv(final String string, final String[] arrstring, final int n, final boolean bl, String string2) {
        return (File[])AccessController.doPrivileged(new PrivilegedAction(){

            public File[] run() {
                File file;
                FileChooser fileChooser = new FileChooser();
                File file2 = file = string == null ? null : new File(string);
                if (file != null && !file.isDirectory()) {
                    file = file.getParentFile();
                }
                fileChooser.setInitialDirectory(file);
                if (arrstring != null) {
                    String[] arrstring2 = new String[arrstring.length];
                    for (int i = 0; i < arrstring.length; ++i) {
                        if (arrstring[i] == null) continue;
                        arrstring2[i] = "*." + arrstring[i];
                    }
                    fileChooser.getExtensionFilters().setAll((Object[])new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter(Arrays.toString(arrstring), Arrays.asList(arrstring2))});
                }
                if (bl) {
                    return fileChooser.showOpenMultipleDialog(null).toArray(new File[0]);
                }
                if (n == 8) {
                    return new File[]{fileChooser.showOpenDialog(null)};
                }
                return new File[]{fileChooser.showSaveDialog(null)};
            }
        });
    }

    public File[] showFileChooser(final String string, final String[] arrstring, final int n, final boolean bl, final String string2) {
        try {
            return FXPluginToolkit.callAndWait(new Callable<File[]>(){

                @Override
                public File[] call() {
                    return FXUIFactory.this.showFileChooser_priv(string, arrstring, n, bl, string2);
                }
            });
        }
        catch (Throwable throwable) {
            Trace.ignored((Throwable)throwable);
            return null;
        }
    }

    public static Pane getDetailPanel(String string) {
        BorderPane borderPane = new BorderPane(){
            {
                this.setId("detail-panel");
                this.setPrefWidth(480.0);
                this.setPrefHeight(300.0);
            }
        };
        TabPane tabPane = new TabPane();
        tabPane.setId("detail-panel-tab-pane");
        tabPane.getStyleClass().add((Object)"floating");
        borderPane.setCenter((Node)tabPane);
        HBox hBox = new HBox();
        hBox.setId("detail-panel-top-pane");
        hBox.setAlignment(Pos.BASELINE_LEFT);
        Label label = new Label(ResourceManager.getString("launcherrordialog.error.label"));
        label.setId("error-dialog-error-label");
        label.setMinWidth(Double.NEGATIVE_INFINITY);
        hBox.getChildren().add((Object)label);
        String[] arrstring = string.split("<split>");
        UITextArea uITextArea = new UITextArea(arrstring[0]);
        uITextArea.setId("detail-panel-msg0");
        uITextArea.setPrefWidth(-1.0);
        hBox.getChildren().add((Object)uITextArea);
        borderPane.setTop((Node)hBox);
        int n = 1;
        while (n + 1 < arrstring.length) {
            Label label2 = new Label();
            label2.getStyleClass().add((Object)"multiline-text");
            label2.setWrapText(true);
            label2.setText(arrstring[n + 1]);
            Tab tab = new Tab();
            tab.setText(arrstring[n]);
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent((Node)label2);
            scrollPane.setFitToWidth(true);
            tab.setContent((Node)scrollPane);
            tabPane.getTabs().add((Object)tab);
            n += 2;
        }
        return borderPane;
    }

    private static Stage beforeDialog(Stage stage) {
        return stage;
    }

    private static void afterDialog() {
    }

    public static String[] addDetail(String[] arrstring, AppInfo appInfo, boolean bl, boolean bl2) {
        String string = appInfo.getTitle();
        if (string == null) {
            string = "";
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i = 0; i < arrstring.length; ++i) {
            arrayList.add(arrstring[i]);
        }
        if (bl) {
            String string2 = null;
            if (appInfo.getDesktopHint() && appInfo.getMenuHint()) {
                string2 = ResourceManager.getString("install.windows.both.message");
            } else if (appInfo.getDesktopHint()) {
                string2 = ResourceManager.getString("install.desktop.message");
            } else if (appInfo.getMenuHint()) {
                string2 = ResourceManager.getString("install.windows.menu.message");
            }
            if (string2 != null) {
                arrayList.add(string2);
            }
        }
        return arrayList.toArray(arrstring);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int showSSVDialog0(Object object, final AppInfo appInfo, final String string, final String string2, final String string3, final String string4, final URL uRL, final String string5, final String string6, final String string7, final String string8, final String string9) {
        final Stage stage = FXUIFactory.beforeDialog((Stage)object);
        try {
            int n = FXPluginToolkit.callAndWait(new Callable<Integer>(){

                @Override
                public Integer call() {
                    DialogTemplate dialogTemplate = new DialogTemplate(appInfo, stage, string, string2);
                    dialogTemplate.setSSVContent(string3, string4, uRL, string5, string6, string7, string8, string9);
                    FXUIFactory.placeWindow((Window)dialogTemplate.getDialog());
                    dialogTemplate.setVisible(true);
                    int n = dialogTemplate.getUserAnswer();
                    return new Integer(n);
                }
            });
            return n;
        }
        catch (Throwable throwable) {
            int n = -1;
            return n;
        }
        finally {
            FXUIFactory.afterDialog();
        }
    }

    public static void placeWindow(Window window) {
        window.centerOnScreen();
    }

    private static Object invokeLater(final Runnable runnable, Integer n) throws Exception {
        if (runnable != null) {
            return (Integer)DeploySysRun.executePrivileged((DeploySysAction)new DeploySysAction(){

                public Object execute() {
                    Platform.runLater((Runnable)runnable);
                    return null;
                }
            }, (Object)new Integer(-1));
        }
        return -1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int showSSV3Dialog(final Object object, final AppInfo appInfo, final int n, final String string, final String string2, final String string3, final String string4, final String string5, final String string6, final String string7, final String string8, final String string9, final String string10, final String string11, final URL uRL) {
        Stage stage;
        try {
            stage = Class.forName("com.sun.deploy.config.Config", false, Thread.currentThread().getContextClassLoader());
            Method method = stage.getDeclaredMethod("getStringProperty", String.class);
            Object object2 = method.invoke((Object)stage, "deployment.sqe.automation.run");
            if (object2 instanceof String && "true".equals((String)object2)) {
                return 0;
            }
        }
        catch (Exception exception) {
            Trace.ignoredException((Exception)exception);
        }
        stage = FXUIFactory.beforeDialog((Stage)object);
        try {
            int n2 = FXPluginToolkit.callAndWait(new Callable<Integer>(){

                @Override
                public Integer call() {
                    return FXSSV3Dialog.showSSV3Dialog(object, appInfo, n, string, string2, string3, string4, string5, string6, string7, string8, string9, string10, string11, uRL);
                }
            });
            return n2;
        }
        catch (Throwable throwable) {
            int n3 = -1;
            return n3;
        }
        finally {
            FXUIFactory.afterDialog();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int showPublisherInfo(Object object, final AppInfo appInfo, final String string, final String string2, final String string3, final String string4, final String string5, final String string6) {
        final Stage stage = FXUIFactory.beforeDialog((Stage)object);
        try {
            int n = FXPluginToolkit.callAndWait(new Callable<Integer>(){

                @Override
                public Integer call() {
                    DialogTemplate dialogTemplate = new DialogTemplate(appInfo, stage, string, string2);
                    Pane pane = null;
                    if (string6 != null) {
                        pane = FXUIFactory.getDetailPanel(string6);
                    }
                    dialogTemplate.setPublisherInfo(string3, string4, string5, (Object)pane, false);
                    FXUIFactory.placeWindow((Window)dialogTemplate.getDialog());
                    dialogTemplate.setVisible(true);
                    int n = dialogTemplate.getUserAnswer();
                    return new Integer(n);
                }
            });
            return n;
        }
        catch (Throwable throwable) {
            int n = -1;
            return n;
        }
        finally {
            FXUIFactory.afterDialog();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int showBlockedDialog(Object object, final AppInfo appInfo, final String string, final String string2, final String string3, final String string4, final String string5, final String string6) {
        final Stage stage = FXUIFactory.beforeDialog((Stage)object);
        try {
            int n = FXPluginToolkit.callAndWait(new Callable<Integer>(){

                @Override
                public Integer call() {
                    DialogTemplate dialogTemplate = new DialogTemplate(appInfo, stage, string, string2);
                    Pane pane = null;
                    if (string6 != null) {
                        pane = FXUIFactory.getDetailPanel(string6);
                    }
                    String string7 = appInfo.getBlockedText();
                    dialogTemplate.setBlockedDialogInfo(string3, string7, string4, string5, (Object)pane, false);
                    FXUIFactory.placeWindow((Window)dialogTemplate.getDialog());
                    dialogTemplate.setVisible(true);
                    int n = dialogTemplate.getUserAnswer();
                    return new Integer(n);
                }
            });
            return n;
        }
        catch (Throwable throwable) {
            Trace.ignored((Throwable)throwable);
            int n = -1;
            return n;
        }
        finally {
            FXUIFactory.afterDialog();
        }
    }
}

