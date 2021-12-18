/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.deploy.trace.Trace
 *  com.sun.deploy.ui.AppInfo
 *  javafx.event.ActionEvent
 *  javafx.event.Event
 *  javafx.event.EventHandler
 *  javafx.geometry.HPos
 *  javafx.geometry.Insets
 *  javafx.geometry.Pos
 *  javafx.scene.Cursor
 *  javafx.scene.Node
 *  javafx.scene.control.Button
 *  javafx.scene.control.CheckBox
 *  javafx.scene.control.Hyperlink
 *  javafx.scene.control.Label
 *  javafx.scene.control.ListView
 *  javafx.scene.control.PasswordField
 *  javafx.scene.control.ProgressBar
 *  javafx.scene.control.RadioButton
 *  javafx.scene.control.ScrollPane
 *  javafx.scene.control.TextField
 *  javafx.scene.control.ToggleGroup
 *  javafx.scene.control.Tooltip
 *  javafx.scene.image.ImageView
 *  javafx.scene.layout.BorderPane
 *  javafx.scene.layout.FlowPane
 *  javafx.scene.layout.GridPane
 *  javafx.scene.layout.HBox
 *  javafx.scene.layout.Pane
 *  javafx.scene.layout.Priority
 *  javafx.scene.layout.StackPane
 *  javafx.scene.layout.VBox
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.Paint
 *  javafx.scene.text.Font
 *  javafx.scene.text.FontWeight
 *  javafx.scene.text.Text
 *  javafx.stage.Modality
 *  javafx.stage.Stage
 *  javafx.stage.Window
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.trace.Trace;
import com.sun.deploy.ui.AppInfo;
import com.sun.deploy.uitoolkit.impl.fx.ui.CertificateDialog;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXDialog;
import com.sun.deploy.uitoolkit.impl.fx.ui.MoreInfoDialog;
import com.sun.deploy.uitoolkit.impl.fx.ui.UITextArea;
import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import com.sun.javafx.applet.HostServicesImpl;
import com.sun.javafx.application.HostServicesDelegate;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.TreeMap;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class DialogTemplate {
    int theAnswer = -1;
    final Object responseLock = new Object();
    private static final int RISK_LABEL_WIDTH = 52;
    private static final int RISK_TEXT_WIDTH = 490;
    private EventHandler<ActionEvent> okHandler = new EventHandler<ActionEvent>(){

        public void handle(ActionEvent actionEvent) {
            DialogTemplate.this.userAnswer = 0;
            if (DialogTemplate.this.always != null && DialogTemplate.this.always.isSelected()) {
                DialogTemplate.this.userAnswer = 2;
            }
            if (DialogTemplate.this.stayAliveOnOk) {
                return;
            }
            if (DialogTemplate.this.password != null) {
                DialogTemplate.access$402(DialogTemplate.this, DialogTemplate.this.password.getText().toCharArray());
            }
            if (DialogTemplate.this.pwdName != null) {
                DialogTemplate.this.userName = DialogTemplate.this.pwdName.getText();
            }
            if (DialogTemplate.this.pwdDomain != null) {
                DialogTemplate.this.domain = DialogTemplate.this.pwdDomain.getText();
            }
            if (DialogTemplate.this.scrollList != null) {
                DialogTemplate.this.userAnswer = DialogTemplate.this.scrollList.getSelectionModel().getSelectedIndex();
            }
            DialogTemplate.this.setVisible(false);
        }
    };
    private EventHandler<ActionEvent> cancelHandler = new EventHandler<ActionEvent>(){

        public void handle(ActionEvent actionEvent) {
            if (DialogTemplate.this.throwable != null || DialogTemplate.this.detailPanel != null) {
                DialogTemplate.this.showMoreInfo();
                return;
            }
            DialogTemplate.this.userAnswer = 1;
            if (DialogTemplate.this.scrollList != null) {
                DialogTemplate.this.userAnswer = -1;
            }
            DialogTemplate.this.setVisible(false);
        }
    };
    EventHandler<ActionEvent> acceptRiskHandler = new EventHandler<ActionEvent>(){

        public void handle(ActionEvent actionEvent) {
            boolean bl = DialogTemplate.this.acceptRisk.isSelected();
            DialogTemplate.this.okBtn.setDisable(!bl);
            DialogTemplate.this.okBtn.setDefaultButton(bl);
            DialogTemplate.this.cancelBtn.setDefaultButton(!bl);
            if (DialogTemplate.this.always != null) {
                DialogTemplate.this.always.setSelected(false);
                DialogTemplate.this.always.setDisable(!bl);
            }
        }
    };
    EventHandler<ActionEvent> expandHandler = new EventHandler<ActionEvent>(){

        public void handle(ActionEvent actionEvent) {
            if (actionEvent.getSource() == DialogTemplate.this.expandBtn) {
                DialogTemplate.this.expandPanel.setTop((Node)DialogTemplate.this.collapseBtn);
                DialogTemplate.this.expandPanel.setBottom((Node)DialogTemplate.this.always);
                if (DialogTemplate.this.acceptRisk != null) {
                    DialogTemplate.this.always.setDisable(!DialogTemplate.this.acceptRisk.isSelected());
                }
            } else if (actionEvent.getSource() == DialogTemplate.this.collapseBtn) {
                DialogTemplate.this.expandPanel.setTop((Node)DialogTemplate.this.expandBtn);
                DialogTemplate.this.expandPanel.setBottom(null);
            }
            DialogTemplate.this.dialog.sizeToScene();
        }
    };
    EventHandler moreInfoHandler = new EventHandler(){

        public void handle(Event event) {
            DialogTemplate.this.showMoreInfo();
        }
    };
    EventHandler closeHandler = new EventHandler(){

        public void handle(Event event) {
            DialogTemplate.this.dialog.hide();
        }
    };
    private FXDialog dialog = null;
    private VBox contentPane = null;
    private AppInfo ainfo = null;
    private String topText = null;
    private boolean useErrorIcon = false;
    private boolean useWarningIcon = false;
    private boolean useInfoIcon = false;
    private boolean useBlockedIcon = false;
    private boolean useMixcodeIcon = false;
    private Label progressStatusLabel = null;
    private BorderPane topPanel;
    private Pane centerPanel;
    private BorderPane expandPanel;
    private ImageView topIcon;
    private ImageView securityIcon;
    private Label nameInfo;
    private Label publisherInfo;
    private Label urlInfo;
    private Label mainJNLPInfo;
    private Label documentBaseInfo;
    private Button okBtn;
    private Button cancelBtn;
    private Button expandBtn;
    private Button collapseBtn;
    private CheckBox always;
    private CheckBox acceptRisk;
    private Label mixedCodeLabel;
    private UITextArea masthead1 = null;
    private UITextArea masthead2 = null;
    private static final int ICON_SIZE = 48;
    private int userAnswer = -1;
    static final int DIALOG_WIDTH = 540;
    private final int MAX_LARGE_SCROLL_WIDTH = 600;
    private final String SECURITY_ALERT_HIGH = "security.alert.high.image";
    private final String SECURITY_ALERT_LOW = "security.alert.low.image";
    private static int MAIN_TEXT_WIDTH = 400;
    private final String OK_ACTION = "OK";
    private final int MAX_BUTTONS = 2;
    private int start;
    private int end;
    private Certificate[] certs;
    private String[] alertStrs;
    private String[] infoStrs;
    private int securityInfoCount;
    private Color originalColor;
    private Cursor originalCursor = null;
    protected ProgressBar progressBar = null;
    private boolean stayAliveOnOk = false;
    private String contentString = null;
    private String reason;
    private String cacheUpgradeContentString = null;
    private String contentLabel = null;
    private String alwaysString = null;
    private String mixedCodeString = null;
    private boolean contentScroll = false;
    private boolean includeMasthead = true;
    private boolean includeAppInfo = true;
    private boolean largeScroll = false;
    private Throwable throwable = null;
    private Pane detailPanel = null;
    private char[] pwd = new char[0];
    private String userName;
    private String domain;
    private TextField pwdName;
    private TextField pwdDomain;
    private PasswordField password;
    private ListView scrollList;
    private boolean showDetails = false;
    TreeMap clientAuthCertsMap;
    private boolean majorWarning = false;
    private String okBtnStr;
    private String cancelBtnStr;
    private boolean sandboxApp = false;
    private boolean checkAlways = false;
    private boolean selfSigned = false;
    private boolean isBlockedDialog;

    DialogTemplate(AppInfo appInfo, Stage stage, String string, String string2) {
        Stage stage2 = stage;
        this.dialog = new FXDialog(string, (Window)stage2, false);
        this.contentPane = new VBox(){

            protected double computePrefHeight(double d) {
                double d2 = super.computePrefHeight(d);
                return d2;
            }
        };
        this.dialog.setContentPane((Pane)this.contentPane);
        this.ainfo = appInfo;
        this.topText = string2;
    }

    void setNewSecurityContent(boolean bl, boolean bl2, String string, String string2, String[] arrstring, String[] arrstring2, int n, boolean bl3, Certificate[] arrcertificate, int n2, int n3, boolean bl4, boolean bl5, boolean bl6) {
        this.certs = arrcertificate;
        this.start = n2;
        this.end = n3;
        this.alertStrs = arrstring;
        this.infoStrs = arrstring2;
        this.securityInfoCount = n;
        this.majorWarning = bl4;
        this.okBtnStr = string;
        this.cancelBtnStr = string2;
        this.sandboxApp = bl5;
        this.checkAlways = bl2;
        this.selfSigned = bl6;
        if (arrstring != null && arrstring.length > 0) {
            this.useWarningIcon = true;
        }
        try {
            this.contentPane.setId("security-content-panel");
            this.dialog.initModality(Modality.APPLICATION_MODAL);
            this.contentPane.getChildren().add((Object)this.createSecurityTopPanel());
            this.contentPane.getChildren().add((Object)this.createSecurityCenterPanel());
            if (!bl6) {
                this.contentPane.getChildren().add((Object)this.createSecurityBottomPanel());
            }
            this.dialog.setResizable(false);
            this.dialog.setIconifiable(false);
            if (this.alertStrs == null) {
                this.dialog.hideWindowTitle();
            }
        }
        catch (Throwable throwable) {
            Trace.ignored((Throwable)throwable);
        }
    }

    private Pane createSecurityTopPanel() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop((Node)this.createSecurityTopMastheadPanel());
        borderPane.setBottom((Node)this.createSecurityTopIconLabelsPanel());
        return borderPane;
    }

    private Pane createSecurityTopMastheadPanel() {
        BorderPane borderPane = new BorderPane();
        this.masthead1 = new UITextArea(MAIN_TEXT_WIDTH);
        this.masthead1.setId("security-masthead-label");
        this.masthead1.setText(this.topText.trim());
        borderPane.setLeft((Node)this.masthead1);
        if (this.alertStrs == null) {
            Button button = FXDialog.createCloseButton();
            button.setOnAction(this.closeHandler);
            borderPane.setRight((Node)button);
        }
        return borderPane;
    }

    private Pane createSecurityTopIconLabelsPanel() {
        BorderPane borderPane = new BorderPane();
        this.topIcon = this.alertStrs != null ? ResourceManager.getIcon("warning48s.image") : ResourceManager.getIcon("java48s.image");
        Label label = new Label(null, (Node)this.topIcon);
        label.setId("security-top-icon-label");
        borderPane.setLeft((Node)label);
        GridPane gridPane = new GridPane();
        gridPane.setId("security-top-labels-grid");
        String string = ResourceManager.getMessage("dialog.template.name");
        Label label2 = new Label(string);
        label2.setId("security-name-label");
        String string2 = ResourceManager.getMessage("dialog.template.publisher");
        Label label3 = new Label(string2);
        label3.setId("security-publisher-label");
        String string3 = ResourceManager.getMessage("deployment.ssv.location");
        Label label4 = new Label(string3);
        label4.setId("security-from-label");
        this.nameInfo = new Label();
        this.nameInfo.setId("security-name-value");
        this.publisherInfo = new Label();
        this.urlInfo = new Label();
        if (this.ainfo.getTitle() != null) {
            GridPane.setConstraints((Node)label2, (int)0, (int)0);
            GridPane.setHalignment((Node)label2, (HPos)HPos.LEFT);
            gridPane.getChildren().add((Object)label2);
            GridPane.setConstraints((Node)this.nameInfo, (int)1, (int)0);
            gridPane.getChildren().add((Object)this.nameInfo);
        }
        if (this.ainfo.getVendor() != null) {
            GridPane.setConstraints((Node)label3, (int)0, (int)1);
            GridPane.setHalignment((Node)label3, (HPos)HPos.LEFT);
            gridPane.getChildren().add((Object)label3);
            GridPane.setConstraints((Node)this.publisherInfo, (int)1, (int)1);
            gridPane.getChildren().add((Object)this.publisherInfo);
        }
        if (this.ainfo.getFrom() != null) {
            GridPane.setConstraints((Node)label4, (int)0, (int)2);
            GridPane.setHalignment((Node)label4, (HPos)HPos.LEFT);
            gridPane.getChildren().add((Object)label4);
            GridPane.setConstraints((Node)this.urlInfo, (int)1, (int)2);
            gridPane.getChildren().add((Object)this.urlInfo);
            int n = 3;
            if (this.ainfo.shouldDisplayMainJNLP()) {
                this.mainJNLPInfo = new Label();
                this.mainJNLPInfo.setId("dialog-from-value");
                GridPane.setConstraints((Node)this.mainJNLPInfo, (int)1, (int)n++);
                gridPane.getChildren().add((Object)this.mainJNLPInfo);
            }
            if (this.ainfo.shouldDisplayDocumentBase()) {
                this.documentBaseInfo = new Label();
                this.documentBaseInfo.setId("dialog-from-value");
                GridPane.setConstraints((Node)this.documentBaseInfo, (int)1, (int)n);
                gridPane.getChildren().add((Object)this.documentBaseInfo);
            }
        }
        this.setInfo(this.ainfo);
        borderPane.setCenter((Node)gridPane);
        return borderPane;
    }

    private Pane createSecurityCenterPanel() {
        BorderPane borderPane = new BorderPane();
        if (this.majorWarning) {
            BorderPane borderPane2 = new BorderPane();
            String string = null;
            string = this.selfSigned ? ResourceManager.getMessage("dialog.selfsigned.security.risk.warning") : ResourceManager.getMessage("dialog.security.risk.warning");
            String string2 = ResourceManager.getMessage("security.dialog.notverified.subject");
            Text text = new Text(string.replaceAll(string2, string2.toUpperCase()));
            text.setWrappingWidth((double)(MAIN_TEXT_WIDTH + 120));
            text.setFill((Paint)Color.web((String)"0xCC0000"));
            text.setFont(Font.font((String)"System", (FontWeight)FontWeight.BOLD, (double)15.0));
            borderPane2.setLeft((Node)text);
            borderPane2.setPadding(new Insets(8.0, 0.0, 0.0, 0.0));
            borderPane.setTop((Node)borderPane2);
            borderPane.setCenter((Node)this.createSecurityRiskPanel());
            borderPane.setBottom((Node)this.createSecurityAcceptRiskPanel());
        } else {
            borderPane.setTop((Node)this.createSecurityRiskPanel());
            borderPane.setBottom((Node)this.createSecurityAlwaysPanel());
        }
        return borderPane;
    }

    private Pane createSecurityRiskPanel() {
        BorderPane borderPane = new BorderPane();
        borderPane.setId("security-risk-panel");
        if (this.majorWarning) {
            String string = this.alertStrs[0];
            int n = string.indexOf(" ");
            String string2 = string.substring(0, n);
            String string3 = string.substring(n + 1);
            UITextArea uITextArea = new UITextArea(52.0);
            uITextArea.setText(string2);
            uITextArea.setId("security-risk-label");
            borderPane.setLeft((Node)uITextArea);
            BorderPane borderPane2 = new BorderPane();
            UITextArea uITextArea2 = new UITextArea(490.0);
            uITextArea2.setId("security-risk-value");
            uITextArea2.setText(string3);
            borderPane2.setTop((Node)uITextArea2);
            borderPane2.setBottom((Node)this.createMoreInfoHyperlink());
            borderPane.setRight((Node)borderPane2);
        } else {
            String string = ResourceManager.getMessage(this.sandboxApp ? "sandbox.security.dialog.valid.signed.risk" : "security.dialog.valid.signed.risk");
            UITextArea uITextArea = new UITextArea(450.0);
            uITextArea.setId("security-risk-value");
            uITextArea.setText(string);
            borderPane.setLeft((Node)uITextArea);
        }
        return borderPane;
    }

    private Hyperlink createMoreInfoHyperlink() {
        String string = ResourceManager.getMessage("dialog.template.more.info2");
        Hyperlink hyperlink = new Hyperlink(string);
        hyperlink.setMnemonicParsing(true);
        hyperlink.setId("security-more-info-link");
        hyperlink.setOnAction(this.moreInfoHandler);
        return hyperlink;
    }

    private Pane createSecurityAcceptRiskPanel() {
        BorderPane borderPane = new BorderPane();
        String string = ResourceManager.getMessage("security.dialog.accept.title");
        String string2 = ResourceManager.getMessage("security.dialog.accept.text");
        UITextArea uITextArea = new UITextArea(542.0);
        uITextArea.setId("security-risk-value");
        uITextArea.setText(string);
        borderPane.setTop((Node)uITextArea);
        BorderPane borderPane2 = new BorderPane();
        borderPane2.setId("security-accept-risk-panel");
        this.acceptRisk = new CheckBox(string2);
        this.acceptRisk.setSelected(false);
        this.acceptRisk.setOnAction(this.acceptRiskHandler);
        borderPane2.setLeft((Node)this.acceptRisk);
        borderPane2.setRight((Node)this.createSecurityOkCancelButtons());
        borderPane.setBottom((Node)borderPane2);
        return borderPane;
    }

    private Pane createSecurityOkCancelButtons() {
        HBox hBox = new HBox();
        hBox.getStyleClass().add((Object)"security-button-bar");
        this.okBtn = new Button(ResourceManager.getMessage(this.okBtnStr));
        this.okBtn.setMnemonicParsing(true);
        this.okBtn.setOnAction(this.okHandler);
        hBox.getChildren().add((Object)this.okBtn);
        this.cancelBtn = new Button(this.cancelBtnStr);
        this.cancelBtn.setOnAction(this.cancelHandler);
        this.cancelBtn.setCancelButton(true);
        hBox.getChildren().add((Object)this.cancelBtn);
        if (this.majorWarning) {
            this.okBtn.setDisable(true);
            this.cancelBtn.setDefaultButton(true);
        } else {
            this.okBtn.setDefaultButton(true);
        }
        return hBox;
    }

    private Pane createSecurityAlwaysPanel() {
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft((Node)this.createSecurityAlwaysCheckbox());
        return borderPane;
    }

    private CheckBox createSecurityAlwaysCheckbox() {
        this.alwaysString = ResourceManager.getMessage(this.sandboxApp ? "sandbox.security.dialog.always" : "security.dialog.always");
        this.always = new CheckBox(this.alwaysString);
        if (this.majorWarning) {
            this.always.setId("security-always-trust-checkbox");
        }
        this.always.setSelected(this.sandboxApp && this.checkAlways);
        this.always.setVisible(true);
        return this.always;
    }

    private Pane createSecurityBottomPanel() {
        if (this.majorWarning) {
            return this.createSecurityBottomExpandPanel();
        }
        return this.createSecurityBottomMoreInfoPanel();
    }

    private Pane createSecurityBottomExpandPanel() {
        this.expandPanel = new BorderPane();
        this.expandPanel.setId("security-bottom-panel");
        this.always = this.createSecurityAlwaysCheckbox();
        ImageView imageView = ResourceManager.getIcon("toggle_down2.image");
        String string = ResourceManager.getMessage("security.dialog.show.options");
        this.expandBtn = new Button(string, (Node)imageView);
        this.expandBtn.setId("security-expand-button");
        this.expandBtn.setOnAction(this.expandHandler);
        ImageView imageView2 = ResourceManager.getIcon("toggle_up2.image");
        String string2 = ResourceManager.getMessage("security.dialog.hide.options");
        this.collapseBtn = new Button(string2, (Node)imageView2);
        this.collapseBtn.setId("security-expand-button");
        this.collapseBtn.setOnAction(this.expandHandler);
        this.expandPanel.setTop((Node)this.expandBtn);
        return this.expandPanel;
    }

    private Pane createSecurityBottomMoreInfoPanel() {
        BorderPane borderPane = new BorderPane();
        borderPane.setId("security-bottom-panel");
        HBox hBox = new HBox();
        this.securityIcon = this.alertStrs != null ? ResourceManager.getIcon("yellowShield.image") : ResourceManager.getIcon("blueShield.image");
        hBox.getChildren().add((Object)this.securityIcon);
        BorderPane borderPane2 = new BorderPane();
        borderPane2.setId("security-more-info-panel");
        borderPane2.setCenter((Node)this.createMoreInfoHyperlink());
        hBox.getChildren().add((Object)borderPane2);
        borderPane.setRight((Node)this.createSecurityOkCancelButtons());
        borderPane.setLeft((Node)hBox);
        return borderPane;
    }

    void setSecurityContent(boolean bl, boolean bl2, String string, String string2, String[] arrstring, String[] arrstring2, int n, boolean bl3, Certificate[] arrcertificate, int n2, int n3, boolean bl4) {
        this.certs = arrcertificate;
        this.start = n2;
        this.end = n3;
        this.alertStrs = arrstring;
        this.infoStrs = arrstring2;
        this.securityInfoCount = n;
        this.majorWarning = bl4;
        if (arrstring != null && arrstring.length > 0) {
            this.useWarningIcon = true;
        }
        try {
            this.contentPane.getChildren().add((Object)this.createTopPanel(false));
            this.dialog.initModality(Modality.APPLICATION_MODAL);
            if (bl) {
                this.alwaysString = ResourceManager.getMessage("security.dialog.always");
            }
            this.contentPane.getChildren().add((Object)this.createCenterPanel(bl2, string, string2, -1));
            this.contentPane.getChildren().add((Object)this.createBottomPanel(bl3));
            this.dialog.setResizable(false);
            this.dialog.setIconifiable(false);
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    void setSSVContent(String string, String string2, URL uRL, String string3, String string4, String string5, String string6, String string7) {
        try {
            BorderPane borderPane = new BorderPane();
            borderPane.setId("ssv-content-panel");
            this.dialog.initModality(Modality.APPLICATION_MODAL);
            this.contentPane.getChildren().add((Object)borderPane);
            borderPane.setTop((Node)this.createSSVTopPanel(this.topText, this.ainfo.getTitle(), this.ainfo.getDisplayFrom()));
            BorderPane borderPane2 = this.createSSVRiskPanel(string, string2, uRL);
            final SSVChoicePanel sSVChoicePanel = new SSVChoicePanel(string3, string4, string5);
            BorderPane borderPane3 = new BorderPane();
            borderPane3.setTop((Node)borderPane2);
            borderPane3.setBottom((Node)sSVChoicePanel);
            borderPane.setCenter((Node)borderPane3);
            FlowPane flowPane = new FlowPane(6.0, 0.0);
            flowPane.getStyleClass().add((Object)"button-bar");
            this.okBtn = new Button(string6);
            this.okBtn.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

                public void handle(ActionEvent actionEvent) {
                    if (sSVChoicePanel.getSelection() == 0) {
                        DialogTemplate.this.setUserAnswer(2);
                    } else {
                        DialogTemplate.this.setUserAnswer(0);
                    }
                    DialogTemplate.this.setVisible(false);
                }
            });
            flowPane.getChildren().add((Object)this.okBtn);
            this.cancelBtn = new Button(string7);
            this.cancelBtn.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

                public void handle(ActionEvent actionEvent) {
                    DialogTemplate.this.cancelAction();
                }
            });
            this.cancelBtn.setCancelButton(true);
            flowPane.getChildren().add((Object)this.cancelBtn);
            this.okBtn.setDefaultButton(true);
            borderPane.setBottom((Node)flowPane);
            this.dialog.setResizable(false);
            this.dialog.setIconifiable(false);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    void setSimpleContent(String string, boolean bl, String string2, String string3, String string4, boolean bl2, boolean bl3) {
        this.contentString = string;
        this.contentScroll = bl;
        this.includeMasthead = bl2;
        this.includeAppInfo = bl2;
        this.largeScroll = !bl2;
        this.useWarningIcon = bl3;
        if (string2 != null) {
            String[] arrstring = new String[]{string2};
            if (bl3) {
                this.alertStrs = arrstring;
            } else {
                this.infoStrs = arrstring;
            }
        }
        try {
            this.contentPane.getChildren().add((Object)this.createTopPanel(false));
            this.contentPane.getChildren().add((Object)this.createCenterPanel(false, string3, string4, -1));
            this.contentPane.getChildren().add((Object)this.createBottomPanel(false));
            this.dialog.setResizable(bl);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    void setMixedCodeContent(String string, boolean bl, String string2, String string3, String string4, String string5, boolean bl2, boolean bl3) {
        this.contentString = string;
        this.contentScroll = bl;
        this.includeMasthead = bl2;
        this.includeAppInfo = bl2;
        this.largeScroll = !bl2;
        this.useMixcodeIcon = true;
        this.alertStrs = new String[1];
        String[] arrstring = new String[]{string3};
        this.alertStrs = arrstring;
        this.infoStrs = new String[3];
        String string6 = ResourceManager.getString("security.dialog.mixcode.info1");
        String string7 = ResourceManager.getString("security.dialog.mixcode.info2");
        String string8 = ResourceManager.getString("security.dialog.mixcode.info3");
        String[] arrstring2 = new String[]{string6, string7, string8};
        this.infoStrs = arrstring2;
        try {
            this.contentPane.getChildren().add((Object)this.createTopPanel(false));
            this.mixedCodeString = string2;
            this.contentPane.getChildren().add((Object)this.createCenterPanel(false, string4, string5, -1));
            this.contentPane.getChildren().add((Object)this.createBottomPanel(false));
            this.okBtn.requestFocus();
            boolean bl4 = bl;
            this.dialog.setResizable(bl4);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    void setListContent(String string, ListView listView, boolean bl, String string2, String string3, TreeMap treeMap) {
        this.useWarningIcon = true;
        this.includeAppInfo = false;
        this.clientAuthCertsMap = treeMap;
        this.contentLabel = string;
        this.contentScroll = true;
        this.scrollList = listView;
        this.showDetails = bl;
        try {
            this.contentPane.getChildren().add((Object)this.createTopPanel(false));
            this.contentPane.getChildren().add((Object)this.createCenterPanel(false, string2, string3, -1));
            this.contentPane.getChildren().add((Object)this.createBottomPanel(false));
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    void setApiContent(String string, String string2, String string3, boolean bl, String string4, String string5) {
        this.contentString = string;
        this.contentLabel = string2;
        this.contentScroll = string != null;
        this.alwaysString = string3;
        if (string2 == null && string != null) {
            this.infoStrs = new String[1];
            this.infoStrs[0] = string;
            this.contentString = null;
        }
        this.includeMasthead = true;
        this.includeAppInfo = this.contentString == null;
        this.largeScroll = false;
        try {
            this.contentPane.getChildren().add((Object)this.createTopPanel(false));
            this.contentPane.getChildren().add((Object)this.createCenterPanel(false, string4, string5, -1));
            this.contentPane.getChildren().add((Object)this.createBottomPanel(false));
            this.dialog.setResizable(this.contentScroll);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    void setErrorContent(String string, String string2, String string3, Throwable throwable, Object object, Certificate[] arrcertificate, boolean bl) {
        Pane pane = (Pane)object;
        this.contentString = string;
        this.throwable = throwable;
        this.detailPanel = pane;
        this.certs = arrcertificate;
        if (bl) {
            this.includeAppInfo = false;
        }
        this.useErrorIcon = true;
        try {
            this.contentPane.getChildren().add((Object)this.createTopPanel(false));
            this.contentPane.getChildren().add((Object)this.createCenterPanel(false, string2, string3, -1));
            Pane pane2 = this.createBottomPanel(false);
            if (pane2.getChildren().size() > 0) {
                this.contentPane.getChildren().add((Object)pane2);
            }
            this.dialog.setResizable(false);
        }
        catch (Throwable throwable2) {
            // empty catch block
        }
    }

    void setMultiButtonErrorContent(String string, String string2, String string3, String string4) {
        this.useErrorIcon = true;
        try {
            this.contentPane.getChildren().add((Object)this.createTopPanel(false));
            BorderPane borderPane = new BorderPane();
            borderPane.setId("error-panel");
            borderPane.setTop((Node)this.createInfoPanel(string));
            borderPane.setBottom((Node)this.createThreeButtonsPanel(string2, string3, string4, false));
            this.contentPane.getChildren().add((Object)borderPane);
            this.dialog.setResizable(false);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    void setInfoContent(String string, String string2) {
        this.useInfoIcon = true;
        this.contentString = string;
        try {
            this.contentPane.getChildren().add((Object)this.createTopPanel(false));
            this.contentPane.getChildren().add((Object)this.createCenterPanel(false, string2, null, -1));
            this.dialog.setResizable(false);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    void setPasswordContent(String string, boolean bl, boolean bl2, String string2, String string3, boolean bl3, char[] arrc, String string4, String string5) {
        try {
            this.contentPane.getChildren().add((Object)this.createPasswordPanel(string, bl, bl2, string2, string3, bl3, arrc, string4, string5));
            this.dialog.initModality(Modality.APPLICATION_MODAL);
            this.dialog.setResizable(false);
            this.dialog.setIconifiable(false);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    void setUpdateCheckContent(String string, String string2, String string3, String string4) {
        try {
            this.contentPane.getChildren().add((Object)this.createTopPanel(false));
            this.contentPane.getChildren().add((Object)this.createInfoPanel(string));
            this.contentPane.getChildren().add((Object)this.createThreeButtonsPanel(string2, string3, string4, true));
            this.dialog.setResizable(false);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    void setProgressContent(String string, String string2, String string3, boolean bl, int n) {
        try {
            this.cacheUpgradeContentString = string3;
            this.contentPane.getChildren().add((Object)this.createTopPanel(false));
            this.contentPane.getChildren().add((Object)this.createCenterPanel(false, string, string2, n));
            if (this.cacheUpgradeContentString == null) {
                this.contentPane.getChildren().add((Object)this.createBottomPanel(false));
            }
            this.dialog.setResizable(false);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private Pane createInfoPanel(String string) {
        StackPane stackPane = new StackPane();
        stackPane.setId("info-panel");
        UITextArea uITextArea = new UITextArea(508.0);
        uITextArea.setId("info-panel-text");
        uITextArea.setText(string);
        stackPane.getChildren().add((Object)uITextArea);
        return stackPane;
    }

    private Pane createThreeButtonsPanel(String string, String string2, String string3, boolean bl) {
        FlowPane flowPane = new FlowPane(6.0, 0.0);
        flowPane.getStyleClass().add((Object)"button-bar");
        Button button = new Button(ResourceManager.getMessage(string));
        button.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

            public void handle(ActionEvent actionEvent) {
                DialogTemplate.this.setUserAnswer(0);
                DialogTemplate.this.setVisible(false);
            }
        });
        flowPane.getChildren().add((Object)button);
        Button button2 = new Button(ResourceManager.getMessage(string2));
        button2.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

            public void handle(ActionEvent actionEvent) {
                DialogTemplate.this.setVisible(false);
                DialogTemplate.this.setUserAnswer(1);
            }
        });
        flowPane.getChildren().add((Object)button2);
        Button button3 = null;
        if (string3 != null) {
            button3 = new Button(ResourceManager.getMessage(string3));
            button3.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

                public void handle(ActionEvent actionEvent) {
                    DialogTemplate.this.setVisible(false);
                    DialogTemplate.this.setUserAnswer(3);
                }
            });
            flowPane.getChildren().add((Object)button3);
        }
        if (bl) {
            button3.setTooltip(new Tooltip(ResourceManager.getMessage("autoupdatecheck.masthead")));
        }
        if (button3 != null) {
            DialogTemplate.resizeButtons(button, button2, button3);
        } else {
            DialogTemplate.resizeButtons(button, button2);
        }
        return flowPane;
    }

    private Pane createTopPanel(boolean bl) {
        this.topPanel = new BorderPane();
        this.topPanel.setId("top-panel");
        if (this.includeMasthead) {
            this.masthead1 = new UITextArea(MAIN_TEXT_WIDTH);
            this.masthead1.setId("masthead-label-1");
            String string = this.topText;
            String string2 = null;
            for (String string3 : new VBox[]{"security.dialog.caption.run.question", "security.dialog.caption.continue.question"}) {
                String string4 = ResourceManager.getMessage(string3);
                if (string4 == null || !string.endsWith(string4)) continue;
                string2 = string.substring(0, string.indexOf(string4)).trim();
                string = string4;
                break;
            }
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER_LEFT);
            this.masthead1.setText(string);
            this.masthead1.setAlignment(Pos.CENTER_LEFT);
            vBox.getChildren().add((Object)this.masthead1);
            if (string2 != null) {
                this.masthead2 = new UITextArea(MAIN_TEXT_WIDTH);
                this.masthead2.setId("masthead-label-2");
                this.masthead2.setText(string2);
                this.masthead2.setAlignment(Pos.CENTER_LEFT);
                vBox.getChildren().add((Object)this.masthead2);
            }
            this.topPanel.setLeft((Node)vBox);
            BorderPane.setAlignment((Node)vBox, (Pos)Pos.CENTER_LEFT);
            if (bl) {
                ImageView imageView = ResourceManager.getIcon("progress.background.image");
            } else {
                this.topIcon = ResourceManager.getIcon("java48.image");
                if (this.useErrorIcon) {
                    this.topIcon = ResourceManager.getIcon("error48.image");
                }
                if (this.useInfoIcon) {
                    this.topIcon = ResourceManager.getIcon("info48.image");
                }
                if (this.useMixcodeIcon) {
                    this.topIcon = ResourceManager.getIcon("mixcode.image");
                }
                if (this.useBlockedIcon) {
                    this.topIcon = ResourceManager.getIcon("cert_error48.image");
                }
                if (this.useWarningIcon) {
                    this.topIcon = this.majorWarning ? ResourceManager.getIcon("major-warning48.image") : ResourceManager.getIcon("warning48.image");
                } else if (this.ainfo.getIconRef() != null) {
                    this.topIcon = ResourceManager.getIcon(this.ainfo.getIconRef());
                }
                this.topPanel.setRight((Node)this.topIcon);
            }
        }
        return this.topPanel;
    }

    private Pane createCenterPanel(boolean bl, String string, String string2, int n) {
        ScrollPane scrollPane;
        boolean bl2;
        Object object;
        String string3;
        this.centerPanel = new VBox();
        this.centerPanel.setId("center-panel");
        GridPane gridPane = new GridPane();
        gridPane.setId("center-panel-grid");
        Label label = new Label(ResourceManager.getMessage("dialog.template.name"));
        label.setId("dialog-name-label");
        Label label2 = new Label(ResourceManager.getMessage("dialog.template.publisher"));
        label2.setId("dialog-publisher-label");
        Label label3 = new Label(ResourceManager.getMessage("deployment.ssv.location"));
        label3.setId("dialog-from-label");
        Label label4 = new Label(ResourceManager.getMessage("deployment.ssv.reason"));
        label4.setId("dialog-reason-label");
        this.nameInfo = new Label();
        this.nameInfo.setId("dialog-name-value");
        this.publisherInfo = new Label();
        this.publisherInfo.setId("dialog-publisher-value");
        this.urlInfo = new Label();
        this.urlInfo.setId("dialog-from-value");
        Label label5 = new Label();
        label5.setWrapText(true);
        label5.setId("dialog-reason-value");
        int n2 = 0;
        if (this.ainfo.getTitle() != null) {
            GridPane.setConstraints((Node)label, (int)0, (int)n2);
            GridPane.setHalignment((Node)label, (HPos)HPos.RIGHT);
            gridPane.getChildren().add((Object)label);
            GridPane.setConstraints((Node)this.nameInfo, (int)1, (int)n2++);
            gridPane.getChildren().add((Object)this.nameInfo);
        }
        if (this.ainfo.getVendor() != null) {
            GridPane.setConstraints((Node)label2, (int)0, (int)n2);
            GridPane.setHalignment((Node)label2, (HPos)HPos.RIGHT);
            gridPane.getChildren().add((Object)label2);
            GridPane.setConstraints((Node)this.publisherInfo, (int)1, (int)n2++);
            gridPane.getChildren().add((Object)this.publisherInfo);
        }
        if (this.ainfo.getFrom() != null) {
            GridPane.setConstraints((Node)label3, (int)0, (int)n2);
            GridPane.setHalignment((Node)label3, (HPos)HPos.RIGHT);
            gridPane.getChildren().add((Object)label3);
            GridPane.setConstraints((Node)this.urlInfo, (int)1, (int)n2++);
            gridPane.getChildren().add((Object)this.urlInfo);
            if (this.ainfo.shouldDisplayMainJNLP()) {
                this.mainJNLPInfo = new Label();
                this.mainJNLPInfo.setId("dialog-from-value");
                GridPane.setConstraints((Node)this.mainJNLPInfo, (int)1, (int)n2++);
                gridPane.getChildren().add((Object)this.mainJNLPInfo);
            }
            if (this.ainfo.shouldDisplayDocumentBase()) {
                this.documentBaseInfo = new Label();
                this.documentBaseInfo.setId("dialog-from-value");
                GridPane.setConstraints((Node)this.documentBaseInfo, (int)1, (int)n2++);
                gridPane.getChildren().add((Object)this.documentBaseInfo);
            }
        }
        if (this.reason != null) {
            GridPane.setConstraints((Node)label4, (int)0, (int)n2);
            GridPane.setHalignment((Node)label4, (HPos)HPos.RIGHT);
            gridPane.getChildren().add((Object)label4);
            label5.setText(this.reason);
            GridPane.setConstraints((Node)label5, (int)1, (int)n2++);
            gridPane.getChildren().add((Object)label5);
        }
        this.setInfo(this.ainfo);
        FlowPane flowPane = new FlowPane();
        flowPane.setId("center-checkbox-panel");
        BorderPane borderPane = new BorderPane();
        borderPane.setId("mixed-code-panel");
        BorderPane borderPane2 = new BorderPane();
        borderPane2.setId("center-content-panel");
        VBox.setVgrow((Node)borderPane2, (Priority)Priority.ALWAYS);
        if (this.alwaysString != null) {
            string3 = "security.dialog.always";
            this.always = new CheckBox(this.alwaysString);
            this.always.setSelected(bl);
            flowPane.getChildren().add((Object)this.always);
        }
        if (this.mixedCodeString != null) {
            this.mixedCodeLabel = new Label(this.mixedCodeString);
            string3 = new BorderPane();
            string3.setId("center-more-info-panel");
            object = new Hyperlink(ResourceManager.getMessage("dialog.template.more.info"));
            object.setMnemonicParsing(true);
            object.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

                public void handle(ActionEvent actionEvent) {
                    DialogTemplate.this.showMixedcodeMoreInfo();
                }
            });
            string3.setLeft((Node)object);
            borderPane.setTop((Node)this.mixedCodeLabel);
            borderPane.setBottom((Node)string3);
        }
        boolean bl3 = bl2 = n >= 0;
        if (bl2) {
            this.progressBar = new ProgressBar();
            this.progressBar.setVisible(n <= 100);
        }
        if (this.contentString != null) {
            if (this.contentLabel != null) {
                object = new BorderPane();
                object.setLeft((Node)new Label(this.contentLabel));
                borderPane2.setTop((Node)object);
            }
            if (this.contentScroll) {
                boolean bl4 = this.largeScroll;
                if (this.largeScroll) {
                    object = new Label(this.contentString);
                    object.setPrefWidth(640.0);
                    object.setPrefHeight(240.0);
                } else {
                    object = new Label(this.contentString);
                    object.setPrefWidth(320.0);
                    object.setPrefHeight(48.0);
                }
                scrollPane = new ScrollPane();
                scrollPane.setContent((Node)object);
                scrollPane.setFitToWidth(true);
                VBox.setVgrow((Node)scrollPane, (Priority)Priority.ALWAYS);
                if (bl4) {
                    scrollPane.setMaxWidth(600.0);
                }
                borderPane2.setCenter((Node)scrollPane);
            } else if (this.isBlockedDialog) {
                object = new VBox();
                UITextArea uITextArea = new UITextArea(this.contentString);
                uITextArea.setId("center-content-area");
                uITextArea.setAlignment(Pos.TOP_CENTER);
                uITextArea.setPrefWidth(540.0);
                scrollPane = new Hyperlink(ResourceManager.getMessage("deployment.blocked.moreinfo.hyperlink.text"));
                final String string4 = ResourceManager.getMessage("deployment.blocked.moreinfo.hyperlink.url");
                scrollPane.setMnemonicParsing(true);
                scrollPane.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

                    public void handle(ActionEvent actionEvent) {
                        HostServicesDelegate hostServicesDelegate = HostServicesImpl.getInstance(null);
                        if (hostServicesDelegate != null && string4 != null) {
                            hostServicesDelegate.showDocument(string4);
                        }
                    }
                });
                object.getChildren().add((Object)uITextArea);
                object.getChildren().add((Object)scrollPane);
                borderPane2.setCenter((Node)object);
            } else {
                object = new UITextArea(this.contentString);
                object.setId("center-content-area");
                object.setAlignment(Pos.TOP_LEFT);
                borderPane2.setCenter((Node)object);
            }
            borderPane2.setPadding(new Insets(0.0, 0.0, 12.0, 0.0));
        }
        if (this.scrollList != null) {
            if (this.contentLabel != null) {
                object = new BorderPane();
                object.setLeft((Node)new Label(this.contentLabel));
                borderPane2.setTop((Node)object);
            }
            if (this.contentScroll) {
                object = new ScrollPane();
                object.setContent((Node)this.scrollList);
                VBox.setVgrow((Node)object, (Priority)Priority.ALWAYS);
                borderPane2.setCenter((Node)object);
            }
            if (this.showDetails) {
                object = new Hyperlink(ResourceManager.getMessage("security.more.info.details"));
                object.setMnemonicParsing(true);
                object.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

                    public void handle(ActionEvent actionEvent) {
                        DialogTemplate.this.showCertificateDetails();
                    }
                });
                FlowPane flowPane2 = new FlowPane();
                flowPane2.setPadding(new Insets(12.0, 0.0, 12.0, 0.0));
                flowPane2.setAlignment(Pos.TOP_LEFT);
                flowPane2.getChildren().add(object);
                borderPane2.setBottom((Node)flowPane2);
            }
        }
        object = new FlowPane(6.0, 0.0);
        object.getStyleClass().add((Object)"button-bar");
        object.setId("center-bottom-button-bar");
        this.okBtn = new Button(string == null ? "" : ResourceManager.getMessage(string));
        this.okBtn.setOnAction(this.okHandler);
        object.getChildren().add((Object)this.okBtn);
        this.okBtn.setVisible(string != null);
        this.cancelBtn = new Button(string2 == null ? "" : ResourceManager.getMessage(string2));
        this.cancelBtn.setOnAction(this.cancelHandler);
        object.getChildren().add((Object)this.cancelBtn);
        this.cancelBtn.setVisible(string2 != null);
        if (this.okBtn.isVisible()) {
            this.okBtn.setDefaultButton(true);
        } else {
            this.cancelBtn.setCancelButton(true);
        }
        DialogTemplate.resizeButtons(this.okBtn, this.cancelBtn);
        if (this.isBlockedDialog && borderPane2.getChildren().size() > 0) {
            this.centerPanel.getChildren().add((Object)borderPane2);
        }
        if (this.cacheUpgradeContentString != null) {
            UITextArea uITextArea = new UITextArea(this.cacheUpgradeContentString);
            uITextArea.setId("cache-upgrade-content");
            borderPane2.setTop((Node)uITextArea);
        } else {
            if (this.includeAppInfo) {
                this.centerPanel.getChildren().add((Object)gridPane);
            }
            if (this.alwaysString != null) {
                this.centerPanel.getChildren().add((Object)flowPane);
            }
            if (this.mixedCodeString != null) {
                this.centerPanel.getChildren().add((Object)borderPane);
            }
        }
        if (!this.isBlockedDialog && borderPane2.getChildren().size() > 0) {
            this.centerPanel.getChildren().add((Object)borderPane2);
        }
        BorderPane borderPane3 = new BorderPane();
        borderPane3.setId("center-bottom-panel");
        if (bl2) {
            this.progressStatusLabel = new Label(" ");
            this.progressStatusLabel.getStyleClass().add((Object)"progress-label");
            scrollPane = new BorderPane();
            scrollPane.setId("center-progress-status-panel");
            this.centerPanel.getChildren().add((Object)scrollPane);
            scrollPane.setLeft((Node)this.progressStatusLabel);
            scrollPane.setPadding(new Insets(2.0, 0.0, 2.0, 0.0));
            borderPane3.setCenter((Node)this.progressBar);
        }
        borderPane3.setRight((Node)object);
        this.centerPanel.getChildren().add((Object)borderPane3);
        return this.centerPanel;
    }

    private Pane createBottomPanel(boolean bl) {
        HBox hBox = new HBox();
        hBox.setId("bottom-panel");
        if (this.alertStrs != null || this.infoStrs != null) {
            String string = "security.alert.high.image";
            if (this.alertStrs == null || this.alertStrs.length == 0) {
                string = "security.alert.low.image";
                if (this.always != null) {
                    this.always.setSelected(true);
                }
            } else if (this.mixedCodeString == null) {
                this.okBtn.setDefaultButton(false);
                this.cancelBtn.setCancelButton(true);
            }
            this.securityIcon = ResourceManager.getIcon(string);
            hBox.getChildren().add((Object)this.securityIcon);
            boolean bl2 = false;
            Hyperlink hyperlink = null;
            if (bl) {
                hyperlink = new Hyperlink(ResourceManager.getMessage("dialog.template.more.info"));
                hyperlink.setMnemonicParsing(true);
                hyperlink.setId("bottom-more-info-link");
                hyperlink.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

                    public void handle(ActionEvent actionEvent) {
                        DialogTemplate.this.showMoreInfo();
                    }
                });
            }
            int n = 333;
            UITextArea uITextArea = new UITextArea(n);
            uITextArea.setId("bottom-text");
            if ((this.alertStrs == null || this.alertStrs.length == 0) && this.infoStrs != null && this.infoStrs.length != 0) {
                uITextArea.setText(this.infoStrs[0] != null ? this.infoStrs[0] : " ");
            } else if (this.alertStrs != null && this.alertStrs.length != 0) {
                uITextArea.setText(this.alertStrs[0] != null ? this.alertStrs[0] : " ");
            }
            hBox.getChildren().add((Object)uITextArea);
            if (hyperlink != null) {
                hBox.getChildren().add((Object)hyperlink);
            }
        }
        return hBox;
    }

    private BorderPane createSSVTopPanel(String string, String string2, String string3) {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(16.0, 0.0, 16.0, 16.0));
        Label label = new Label(string);
        label.getStyleClass().add((Object)"ssv-big-bold-label");
        borderPane.setTop((Node)label);
        Label label2 = new Label(ResourceManager.getMessage("dialog.template.name"));
        label2.getStyleClass().add((Object)"ssv-small-bold-label");
        label2.setId("ssv-top-panel-name-label");
        Label label3 = new Label(ResourceManager.getMessage("deployment.ssv.location"));
        label3.getStyleClass().add((Object)"ssv-small-bold-label");
        label3.setId("ssv-top-panel-from-label");
        this.nameInfo = new Label(string2);
        this.nameInfo.getStyleClass().add((Object)"ssv-big-bold-label");
        Label label4 = new Label(string3);
        label4.getStyleClass().add((Object)"ssv-small-label");
        BorderPane[] arrborderPane = new BorderPane[4];
        for (int i = 0; i < 4; ++i) {
            arrborderPane[i] = new BorderPane();
        }
        ImageView imageView = ResourceManager.getIcon("warning48.image");
        arrborderPane[2].setTop((Node)label2);
        arrborderPane[2].setBottom((Node)label3);
        arrborderPane[2].setPadding(new Insets(2.0, 0.0, 0.0, 0.0));
        arrborderPane[3].setTop((Node)this.nameInfo);
        arrborderPane[3].setBottom((Node)label4);
        arrborderPane[1].setLeft((Node)arrborderPane[2]);
        arrborderPane[1].setCenter((Node)arrborderPane[3]);
        arrborderPane[1].setPadding(new Insets(12.0, 0.0, 12.0, 0.0));
        arrborderPane[0].setLeft((Node)imageView);
        arrborderPane[0].setRight((Node)arrborderPane[1]);
        arrborderPane[0].setPadding(new Insets(8.0, 0.0, 0.0, 32.0));
        borderPane.setBottom((Node)arrborderPane[0]);
        return borderPane;
    }

    private BorderPane createSSVRiskPanel(String string, String string2, final URL uRL) {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(8.0, 8.0, 0.0, 8.0));
        int n = string.indexOf(" ");
        if (n < string.length() - 2) {
            String string3 = string.substring(0, n);
            String string4 = string.substring(n + 1);
            BorderPane borderPane2 = new BorderPane();
            Label label = new Label(string3);
            label.getStyleClass().add((Object)"ssv-small-bold-label");
            borderPane2.setTop((Node)label);
            borderPane2.setPadding(new Insets(0.0, 8.0, 0.0, 8.0));
            BorderPane borderPane3 = new BorderPane();
            Label label2 = new Label(string4);
            borderPane3.setLeft((Node)label2);
            label2.getStyleClass().add((Object)"ssv-small-label");
            Hyperlink hyperlink = new Hyperlink(string2);
            hyperlink.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

                public void handle(ActionEvent actionEvent) {
                    HostServicesDelegate hostServicesDelegate = HostServicesImpl.getInstance(null);
                    if (hostServicesDelegate != null && uRL != null) {
                        hostServicesDelegate.showDocument(uRL.toExternalForm());
                    }
                }
            });
            borderPane3.setRight((Node)hyperlink);
            borderPane.setLeft((Node)borderPane2);
            borderPane.setCenter((Node)borderPane3);
        }
        return borderPane;
    }

    private Pane createPasswordPanel(String string, boolean bl, boolean bl2, String string2, String string3, boolean bl3, char[] arrc, String string4, String string5) {
        Label label;
        String string6;
        String string7;
        Label label2 = new Label();
        Label label3 = new Label();
        ImageView imageView = ResourceManager.getIcon("pwd-masthead.png");
        if (bl) {
            string7 = "password.dialog.username";
            label2.setText(ResourceManager.getMessage(string7));
            label2.setMnemonicParsing(true);
            this.pwdName = new TextField();
            this.pwdName.setId("user-name-field");
            this.pwdName.setText(string2);
            label2.setLabelFor((Node)this.pwdName);
            label2.setId("user-name-label");
        }
        string7 = "password.dialog.password";
        Label label4 = new Label(ResourceManager.getMessage(string7));
        this.password = new PasswordField();
        this.password.setText(String.valueOf(arrc));
        label4.setLabelFor((Node)this.password);
        label4.setMnemonicParsing(true);
        label4.setId("password-label");
        if (bl2) {
            string6 = "password.dialog.domain";
            label3.setText(ResourceManager.getMessage(string6));
            this.pwdDomain = new TextField();
            this.pwdDomain.setText(string3);
            label3.setLabelFor((Node)this.pwdDomain);
            label3.setMnemonicParsing(true);
            label3.setId("password-domain-label");
        }
        string6 = new VBox();
        string6.setMaxWidth(imageView.getImage().getWidth());
        string6.getChildren().add((Object)imageView);
        VBox vBox = new VBox(10.0);
        vBox.setId("password-panel");
        Label label5 = new Label();
        label5.setId("password-details");
        label5.setWrapText(true);
        label5.setText(string);
        vBox.getChildren().add((Object)label5);
        GridPane gridPane = new GridPane();
        gridPane.setId("password-panel-grid");
        int n = 0;
        if (bl) {
            GridPane.setConstraints((Node)label2, (int)0, (int)n);
            GridPane.setHalignment((Node)label2, (HPos)HPos.RIGHT);
            gridPane.getChildren().add((Object)label2);
            GridPane.setConstraints((Node)this.pwdName, (int)1, (int)n++);
            gridPane.getChildren().add((Object)this.pwdName);
        }
        GridPane.setConstraints((Node)label4, (int)0, (int)n);
        GridPane.setHalignment((Node)label4, (HPos)HPos.RIGHT);
        gridPane.getChildren().add((Object)label4);
        GridPane.setConstraints((Node)this.password, (int)1, (int)n++);
        gridPane.getChildren().add((Object)this.password);
        if (bl2) {
            GridPane.setConstraints((Node)label3, (int)0, (int)n);
            GridPane.setHalignment((Node)label3, (HPos)HPos.RIGHT);
            gridPane.getChildren().add((Object)label3);
            GridPane.setConstraints((Node)this.pwdDomain, (int)1, (int)n++);
            gridPane.getChildren().add((Object)this.pwdDomain);
        }
        if (bl3) {
            this.always = new CheckBox(ResourceManager.getMessage("password.dialog.save"));
            this.always.setId("password-always-checkbox");
            this.always.setSelected(arrc.length > 0);
            GridPane.setConstraints((Node)this.always, (int)1, (int)n++);
            gridPane.getChildren().add((Object)this.always);
        }
        vBox.getChildren().add((Object)gridPane);
        if (string5 != null) {
            label = new Label();
            label.setId("password-warning");
            label.setWrapText(true);
            label.setText(string5);
            vBox.getChildren().add((Object)label);
        }
        label = new FlowPane(6.0, 0.0);
        label.setPrefWrapLength(300.0);
        label.getStyleClass().add((Object)"button-bar");
        label.setId("password-button-bar");
        this.okBtn = new Button(ResourceManager.getMessage("common.ok_btn"));
        this.okBtn.setOnAction(this.okHandler);
        this.okBtn.setDefaultButton(true);
        this.cancelBtn = new Button(ResourceManager.getMessage("common.cancel_btn"));
        this.cancelBtn.setOnAction(this.cancelHandler);
        DialogTemplate.resizeButtons(this.okBtn, this.cancelBtn);
        label.getChildren().addAll((Object[])new Node[]{this.okBtn, this.cancelBtn});
        vBox.getChildren().add((Object)label);
        if (string4 != null) {
            MessageFormat messageFormat = new MessageFormat(ResourceManager.getMessage("password.dialog.scheme"));
            Object[] arrobject = new Object[]{string4};
            Label label6 = new Label(messageFormat.format(arrobject));
            vBox.getChildren().add((Object)label6);
        }
        string6.getChildren().add((Object)vBox);
        return string6;
    }

    void showMoreInfo() {
        MoreInfoDialog moreInfoDialog = this.throwable == null && this.detailPanel == null ? new MoreInfoDialog(this.dialog, this.alertStrs, this.infoStrs, this.securityInfoCount, this.certs, this.start, this.end, this.sandboxApp) : new MoreInfoDialog(this.dialog, this.detailPanel, this.throwable, this.certs);
        moreInfoDialog.show();
    }

    void showMixedcodeMoreInfo() {
        MoreInfoDialog moreInfoDialog = new MoreInfoDialog(this.dialog, null, this.infoStrs, 0, null, 0, 0, this.sandboxApp);
        moreInfoDialog.show();
    }

    void showCertificateDetails() {
        Certificate[] arrcertificate = null;
        Iterator iterator = this.clientAuthCertsMap.values().iterator();
        for (long i = (long)this.scrollList.getSelectionModel().getSelectedIndex(); i >= 0L && iterator.hasNext(); --i) {
            arrcertificate = (X509Certificate[])iterator.next();
        }
        if (arrcertificate != null) {
            CertificateDialog.showCertificates(this.dialog, arrcertificate, 0, arrcertificate.length);
        }
    }

    public void setVisible(boolean bl) {
        if (bl) {
            final FXDialog fXDialog = this.dialog;
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

    public static void resizeButtons(Button ... arrbutton) {
        int n = arrbutton.length;
        double d = 50.0;
        for (int i = 0; i < n; ++i) {
            if (!(arrbutton[i].prefWidth(-1.0) > d)) continue;
            d = arrbutton[i].prefWidth(-1.0);
        }
    }

    public void cancelAction() {
        this.userAnswer = 1;
        this.setVisible(false);
    }

    int getUserAnswer() {
        return this.userAnswer;
    }

    void setUserAnswer(int n) {
        this.userAnswer = n;
    }

    char[] getPassword() {
        return this.pwd;
    }

    String getUserName() {
        return this.userName;
    }

    String getDomain() {
        return this.domain;
    }

    public boolean isPasswordSaved() {
        return this.always != null && this.always.isSelected();
    }

    public void progress(int n) {
        if (this.progressBar != null) {
            if (n <= 100) {
                this.progressBar.setProgress((double)n / 100.0);
                this.progressBar.setVisible(true);
            } else {
                this.progressBar.setVisible(false);
            }
        }
    }

    public FXDialog getDialog() {
        return this.dialog;
    }

    static String getDisplayMainJNLPTooltip(AppInfo appInfo) {
        try {
            Class<AppInfo> class_ = AppInfo.class;
            Method method = class_.getMethod("getDisplayMainJNLPTooltip", null);
            return (String)method.invoke((Object)appInfo, new Object[0]);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }
    }

    public void setInfo(AppInfo appInfo) {
        URL uRL;
        this.ainfo = appInfo;
        if (this.nameInfo != null) {
            this.nameInfo.setText(appInfo.getTitle());
        }
        if (this.publisherInfo != null) {
            this.publisherInfo.setText(appInfo.getVendor());
        }
        if (this.urlInfo != null) {
            this.urlInfo.setText(appInfo.getDisplayFrom());
            uRL = appInfo.getFrom();
            this.urlInfo.setTooltip(new Tooltip(uRL == null ? "" : uRL.toString()));
        }
        if (this.mainJNLPInfo != null) {
            this.mainJNLPInfo.setText(appInfo.getDisplayMainJNLP());
            this.mainJNLPInfo.setTooltip(new Tooltip(DialogTemplate.getDisplayMainJNLPTooltip(appInfo)));
        }
        if (this.documentBaseInfo != null) {
            this.documentBaseInfo.setText(appInfo.getDisplayDocumentBase());
            uRL = appInfo.getDocumentBase();
            this.documentBaseInfo.setTooltip(new Tooltip(uRL == null ? "" : uRL.toString()));
        }
    }

    void showOk(boolean bl) {
        DialogTemplate.resizeButtons(this.okBtn, this.cancelBtn);
        this.okBtn.setVisible(bl);
    }

    void stayAlive() {
        this.stayAliveOnOk = true;
    }

    public void setProgressStatusText(String string) {
        if (this.progressStatusLabel != null) {
            if (string == null || string.length() == 0) {
                string = " ";
            }
            this.progressStatusLabel.setText(string);
        }
    }

    void setPublisherInfo(String string, String string2, String string3, Object object, boolean bl) {
        this.detailPanel = (Pane)object;
        this.contentString = string;
        this.useInfoIcon = true;
        if (object == null) {
            string3 = null;
        }
        if (bl) {
            this.includeAppInfo = false;
        }
        this.okBtnStr = string2;
        this.cancelBtnStr = string3;
        try {
            this.contentPane.getChildren().add((Object)this.createTopPanel(true));
            this.contentPane.getChildren().add((Object)this.createCenterPanel(false, string2, string3, -1));
            this.dialog.setResizable(false);
            this.dialog.setIconifiable(false);
        }
        catch (Throwable throwable) {
            Trace.ignored((Throwable)throwable);
        }
    }

    void setBlockedDialogInfo(String string, String string2, String string3, String string4, Object object, boolean bl) {
        this.detailPanel = (Pane)object;
        this.contentString = string2;
        this.reason = string;
        this.useBlockedIcon = true;
        this.isBlockedDialog = true;
        if (object == null) {
            string4 = null;
        }
        if (bl) {
            this.includeAppInfo = false;
        }
        this.okBtnStr = string3;
        this.cancelBtnStr = string4;
        try {
            this.contentPane.getChildren().add((Object)this.createTopPanel(true));
            this.contentPane.getChildren().add((Object)this.createCenterPanel(false, string3, string4, -1));
            this.dialog.setResizable(false);
            this.dialog.setIconifiable(false);
        }
        catch (Throwable throwable) {
            Trace.ignored((Throwable)throwable);
        }
    }

    static void showDocument(String string) {
        try {
            Class<?> class_ = Class.forName("com.sun.deploy.config.Platform");
            Method method = class_.getMethod("get", new Class[0]);
            Object object = method.invoke(null, new Object[0]);
            Method method2 = object.getClass().getMethod("showDocument", String.class);
            method2.invoke(object, string);
        }
        catch (Exception exception) {
            Trace.ignored((Throwable)exception);
        }
    }

    static /* synthetic */ char[] access$402(DialogTemplate dialogTemplate, char[] arrc) {
        dialogTemplate.pwd = arrc;
        return arrc;
    }

    private class SSVChoicePanel
    extends BorderPane {
        ToggleGroup group;
        RadioButton button1;
        RadioButton button2;

        public SSVChoicePanel(String string, String string2, String string3) {
            this.setPadding(new Insets(8.0, 16.0, 0.0, 16.0));
            BorderPane borderPane = new BorderPane();
            VBox vBox = new VBox();
            vBox.setSpacing(4.0);
            Label label = new Label(string);
            label.getStyleClass().add((Object)"ssv-small-bold-label");
            borderPane.setCenter((Node)label);
            this.button1 = new RadioButton(string2);
            this.button1.getStyleClass().add((Object)"ssv-small-label");
            this.button2 = new RadioButton(string3);
            this.button2.getStyleClass().add((Object)"ssv-small-label");
            this.group = new ToggleGroup();
            this.button1.setToggleGroup(this.group);
            this.button2.setToggleGroup(this.group);
            this.button1.setSelected(true);
            vBox.getChildren().addAll((Object[])new Node[]{this.button1, this.button2});
            vBox.setPadding(new Insets(0.0, 16.0, 0.0, 32.0));
            this.setTop((Node)borderPane);
            this.setBottom((Node)vBox);
            this.button1.requestFocus();
        }

        public int getSelection() {
            if (this.button2.isSelected()) {
                return 1;
            }
            return 0;
        }
    }
}

