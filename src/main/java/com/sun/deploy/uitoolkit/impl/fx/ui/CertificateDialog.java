/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.ReadOnlyObjectWrapper
 *  javafx.beans.value.ObservableValue
 *  javafx.collections.FXCollections
 *  javafx.collections.ListChangeListener
 *  javafx.collections.ListChangeListener$Change
 *  javafx.collections.ObservableList
 *  javafx.event.ActionEvent
 *  javafx.event.EventHandler
 *  javafx.geometry.Orientation
 *  javafx.scene.Node
 *  javafx.scene.control.Button
 *  javafx.scene.control.Label
 *  javafx.scene.control.MultipleSelectionModel
 *  javafx.scene.control.ScrollPane
 *  javafx.scene.control.SelectionMode
 *  javafx.scene.control.SplitPane
 *  javafx.scene.control.TableColumn
 *  javafx.scene.control.TableColumn$CellDataFeatures
 *  javafx.scene.control.TableView
 *  javafx.scene.control.TableView$TableViewSelectionModel
 *  javafx.scene.control.TextArea
 *  javafx.scene.control.TreeItem
 *  javafx.scene.control.TreeView
 *  javafx.scene.layout.BorderPane
 *  javafx.scene.layout.FlowPane
 *  javafx.scene.layout.Pane
 *  javafx.stage.Stage
 *  javafx.stage.Window
 *  javafx.util.Callback
 *  sun.misc.HexDumpEncoder
 */
package com.sun.deploy.uitoolkit.impl.fx.ui;

import com.sun.deploy.uitoolkit.impl.fx.ui.FXDialog;
import com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.Format;
import java.text.MessageFormat;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import sun.misc.HexDumpEncoder;
import sun.security.x509.SerialNumber;

public class CertificateDialog {
    public static void showCertificates(Stage stage, Certificate[] arrcertificate, int n, int n2) {
        final FXDialog fXDialog = new FXDialog(ResourceManager.getMessage("cert.dialog.caption"), (Window)stage, true);
        fXDialog.setWidth(800.0);
        fXDialog.setHeight(600.0);
        BorderPane borderPane = new BorderPane();
        fXDialog.setContentPane((Pane)borderPane);
        borderPane.setCenter(CertificateDialog.getComponents(stage, arrcertificate, n, n2));
        FlowPane flowPane = new FlowPane();
        flowPane.getStyleClass().add((Object)"button-bar");
        Button button = new Button(ResourceManager.getMessage("cert.dialog.close"));
        button.setDefaultButton(true);
        button.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

            public void handle(ActionEvent actionEvent) {
                fXDialog.hide();
            }
        });
        flowPane.getChildren().add((Object)button);
        borderPane.setBottom((Node)flowPane);
        fXDialog.show();
    }

    private static Node getComponents(Stage stage, Certificate[] arrcertificate, int n, int n2) {
        SplitPane splitPane = new SplitPane();
        if (arrcertificate.length > n && arrcertificate[n] instanceof X509Certificate) {
            TreeView treeView = CertificateDialog.buildCertChainTree(arrcertificate, n, n2);
            final TableView tableView = new TableView();
            final TextArea textArea = new TextArea();
            textArea.setEditable(false);
            final MultipleSelectionModel multipleSelectionModel = treeView.getSelectionModel();
            multipleSelectionModel.getSelectedItems().addListener((ListChangeListener)new ListChangeListener<TreeItem<CertificateInfo>>(){

                public void onChanged(ListChangeListener.Change<? extends TreeItem<CertificateInfo>> change) {
                    ObservableList observableList = multipleSelectionModel.getSelectedItems();
                    if (observableList != null && observableList.size() == 1) {
                        TreeItem treeItem = (TreeItem)observableList.get(0);
                        CertificateInfo certificateInfo = (CertificateInfo)treeItem.getValue();
                        CertificateDialog.showCertificateInfo(certificateInfo.getCertificate(), tableView, textArea);
                    }
                }
            });
            TableColumn tableColumn = new TableColumn();
            tableColumn.setText(ResourceManager.getMessage("cert.dialog.field"));
            tableColumn.setCellValueFactory((Callback)new Callback<TableColumn.CellDataFeatures<Row, Object>, ObservableValue<Object>>(){

                public ObservableValue<Object> call(TableColumn.CellDataFeatures<Row, Object> cellDataFeatures) {
                    return new ReadOnlyObjectWrapper((Object)((Row)cellDataFeatures.getValue()).field);
                }
            });
            TableColumn tableColumn2 = new TableColumn();
            tableColumn2.setText(ResourceManager.getMessage("cert.dialog.value"));
            tableColumn2.setCellValueFactory((Callback)new Callback<TableColumn.CellDataFeatures<Row, Object>, ObservableValue<Object>>(){

                public ObservableValue<Object> call(TableColumn.CellDataFeatures<Row, Object> cellDataFeatures) {
                    return new ReadOnlyObjectWrapper((Object)((Row)cellDataFeatures.getValue()).value);
                }
            });
            tableView.getColumns().addAll((Object[])new TableColumn[]{tableColumn, tableColumn2});
            final TableView.TableViewSelectionModel tableViewSelectionModel = tableView.getSelectionModel();
            tableViewSelectionModel.setSelectionMode(SelectionMode.SINGLE);
            tableViewSelectionModel.getSelectedItems().addListener((ListChangeListener)new ListChangeListener<String>(){

                public void onChanged(ListChangeListener.Change<? extends String> change) {
                    ObservableList observableList = tableViewSelectionModel.getSelectedItems();
                    if (observableList != null && observableList.size() == 1) {
                        String string = ((Row)observableList.get((int)0)).value;
                        textArea.setText(string);
                    }
                }
            });
            treeView.setMinWidth(Double.NEGATIVE_INFINITY);
            treeView.setMinHeight(Double.NEGATIVE_INFINITY);
            ScrollPane scrollPane = CertificateDialog.makeScrollPane((Node)treeView);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            splitPane.getItems().add((Object)scrollPane);
            SplitPane splitPane2 = new SplitPane();
            splitPane2.setOrientation(Orientation.VERTICAL);
            splitPane2.getItems().add((Object)tableView);
            textArea.setPrefWidth(320.0);
            textArea.setPrefHeight(120.0);
            splitPane2.getItems().add((Object)textArea);
            splitPane2.setDividerPosition(0, 0.8);
            splitPane.getItems().add((Object)splitPane2);
            splitPane.setDividerPosition(0, 0.4);
            multipleSelectionModel.select(0);
        }
        return splitPane;
    }

    private static ScrollPane makeScrollPane(Node node) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(node);
        if (node instanceof Label) {
            scrollPane.setFitToWidth(true);
        }
        return scrollPane;
    }

    private static TreeView buildCertChainTree(Certificate[] arrcertificate, int n, int n2) {
        TreeItem treeItem = null;
        TreeItem treeItem2 = null;
        for (int i = n; i < arrcertificate.length && i < n2; ++i) {
            TreeItem treeItem3 = new TreeItem((Object)new CertificateInfo((X509Certificate)arrcertificate[i]));
            if (treeItem == null) {
                treeItem = treeItem3;
            } else {
                treeItem2.getChildren().add((Object)treeItem3);
            }
            treeItem2 = treeItem3;
        }
        TreeView treeView = new TreeView();
        treeView.setShowRoot(true);
        treeView.setRoot(treeItem);
        treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        return treeView;
    }

    private static void showCertificateInfo(X509Certificate x509Certificate, TableView tableView, TextArea textArea) {
        Object object;
        String string = "V" + x509Certificate.getVersion();
        String string2 = "[xxxxx-xxxxx]";
        String string3 = null;
        String string4 = null;
        try {
            object = new SerialNumber(x509Certificate.getSerialNumber());
            string2 = "[" + ((SerialNumber)object).getNumber() + "]";
            string3 = CertificateDialog.getCertFingerPrint("MD5", x509Certificate);
            string4 = CertificateDialog.getCertFingerPrint("SHA1", x509Certificate);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        object = "[" + x509Certificate.getSigAlgName() + "]";
        String string5 = CertificateDialog.formatDNString(x509Certificate.getIssuerDN().toString());
        String string6 = "[From: " + x509Certificate.getNotBefore() + ",\n To: " + x509Certificate.getNotAfter() + "]";
        String string7 = CertificateDialog.formatDNString(x509Certificate.getSubjectDN().toString());
        HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
        String string8 = hexDumpEncoder.encodeBuffer(x509Certificate.getSignature());
        ObservableList observableList = FXCollections.observableArrayList((Object[])new Row[]{new Row(ResourceManager.getMessage("cert.dialog.field.Version"), string), new Row(ResourceManager.getMessage("cert.dialog.field.SerialNumber"), string2), new Row(ResourceManager.getMessage("cert.dialog.field.SignatureAlg"), (String)object), new Row(ResourceManager.getMessage("cert.dialog.field.Issuer"), string5), new Row(ResourceManager.getMessage("cert.dialog.field.Validity"), string6), new Row(ResourceManager.getMessage("cert.dialog.field.Subject"), string7), new Row(ResourceManager.getMessage("cert.dialog.field.Signature"), string8), new Row(ResourceManager.getMessage("cert.dialog.field.md5Fingerprint"), string3), new Row(ResourceManager.getMessage("cert.dialog.field.sha1Fingerprint"), string4)});
        tableView.setItems(observableList);
        tableView.getSelectionModel().select(8, null);
    }

    public static String formatDNString(String string) {
        int n = string.length();
        boolean bl = false;
        boolean bl2 = false;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < n; ++i) {
            char c = string.charAt(i);
            if (c == '\"' || c == '\'') {
                boolean bl3 = bl2 = !bl2;
            }
            if (c == ',' && !bl2) {
                stringBuffer.append(",\n");
                continue;
            }
            stringBuffer.append(c);
        }
        return stringBuffer.toString();
    }

    public static String getCertFingerPrint(String string, X509Certificate x509Certificate) throws Exception {
        byte[] arrby = x509Certificate.getEncoded();
        MessageDigest messageDigest = MessageDigest.getInstance(string);
        byte[] arrby2 = messageDigest.digest(arrby);
        return CertificateDialog.toHexString(arrby2);
    }

    private static void byte2hex(byte by, StringBuffer stringBuffer) {
        char[] arrc = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        int n = (by & 0xF0) >> 4;
        int n2 = by & 0xF;
        stringBuffer.append(arrc[n]);
        stringBuffer.append(arrc[n2]);
    }

    private static String toHexString(byte[] arrby) {
        StringBuffer stringBuffer = new StringBuffer();
        int n = arrby.length;
        for (int i = 0; i < n; ++i) {
            CertificateDialog.byte2hex(arrby[i], stringBuffer);
            if (i >= n - 1) continue;
            stringBuffer.append(":");
        }
        return stringBuffer.toString();
    }

    public static class CertificateInfo {
        X509Certificate cert;

        public CertificateInfo(X509Certificate x509Certificate) {
            this.cert = x509Certificate;
        }

        public X509Certificate getCertificate() {
            return this.cert;
        }

        private String extractAliasName(X509Certificate x509Certificate) {
            Object[] arrobject;
            Object object;
            String string = ResourceManager.getMessage("security.dialog.unknown.subject");
            String string2 = ResourceManager.getMessage("security.dialog.unknown.issuer");
            try {
                object = x509Certificate.getSubjectDN();
                arrobject = x509Certificate.getIssuerDN();
                String string3 = object.getName();
                String string4 = arrobject.getName();
                string = this.extractFromQuote(string3, "CN=");
                if (string == null) {
                    string = this.extractFromQuote(string3, "O=");
                }
                if (string == null) {
                    string = ResourceManager.getMessage("security.dialog.unknown.subject");
                }
                if ((string2 = this.extractFromQuote(string4, "CN=")) == null) {
                    string2 = this.extractFromQuote(string4, "O=");
                }
                if (string2 == null) {
                    string2 = ResourceManager.getMessage("security.dialog.unknown.issuer");
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            object = new MessageFormat(ResourceManager.getMessage("security.dialog.certShowName"));
            arrobject = new Object[]{string, string2};
            return ((Format)object).format(arrobject);
        }

        private String extractFromQuote(String string, String string2) {
            if (string == null) {
                return null;
            }
            int n = string.indexOf(string2);
            int n2 = 0;
            if (n >= 0) {
                if ((n2 = string.charAt(n += string2.length()) == '\"' ? string.indexOf(34, ++n) : string.indexOf(44, n)) < 0) {
                    return string.substring(n);
                }
                return string.substring(n, n2);
            }
            return null;
        }

        public String toString() {
            return this.extractAliasName(this.cert);
        }
    }

    private static class Row {
        public String field;
        public String value;

        Row(String string, String string2) {
            this.field = string;
            this.value = string2;
        }
    }
}

