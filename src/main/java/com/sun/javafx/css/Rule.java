/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ListChangeListener$Change
 *  javafx.collections.ObservableList
 *  javafx.css.PseudoClass
 *  javafx.css.StyleOrigin
 *  javafx.css.Styleable
 *  javafx.scene.Node
 */
package com.sun.javafx.css;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.css.Declaration;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.StringStore;
import com.sun.javafx.css.Stylesheet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.scene.Node;

public final class Rule {
    private List<Selector> selectors = null;
    private List<Declaration> declarations = null;
    private Observables observables = null;
    private Stylesheet stylesheet;
    private byte[] serializedDecls;
    private final int bssVersion;

    public List<Selector> getUnobservedSelectorList() {
        if (this.selectors == null) {
            this.selectors = new ArrayList<Selector>();
        }
        return this.selectors;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<Declaration> getUnobservedDeclarationList() {
        if (this.declarations == null && this.serializedDecls != null) {
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.serializedDecls);
                DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
                int n = dataInputStream.readShort();
                this.declarations = new ArrayList<Declaration>(n);
                for (int i = 0; i < n; ++i) {
                    Declaration declaration = Declaration.readBinary(this.bssVersion, dataInputStream, this.stylesheet.getStringStore());
                    declaration.rule = this;
                    if (this.stylesheet != null && this.stylesheet.getUrl() != null) {
                        String string = this.stylesheet.getUrl();
                        declaration.fixUrl(string);
                    }
                    this.declarations.add(declaration);
                }
            }
            catch (IOException iOException) {
                this.declarations = new ArrayList<Declaration>();
                assert (false);
                iOException.getMessage();
            }
            finally {
                this.serializedDecls = null;
            }
        }
        return this.declarations;
    }

    public final ObservableList<Declaration> getDeclarations() {
        if (this.observables == null) {
            this.observables = new Observables(this);
        }
        return this.observables.getDeclarations();
    }

    public final ObservableList<Selector> getSelectors() {
        if (this.observables == null) {
            this.observables = new Observables(this);
        }
        return this.observables.getSelectors();
    }

    public Stylesheet getStylesheet() {
        return this.stylesheet;
    }

    void setStylesheet(Stylesheet stylesheet) {
        this.stylesheet = stylesheet;
        if (stylesheet != null && stylesheet.getUrl() != null) {
            String string = stylesheet.getUrl();
            int n = this.declarations != null ? this.declarations.size() : 0;
            for (int i = 0; i < n; ++i) {
                this.declarations.get(i).fixUrl(string);
            }
        }
    }

    public StyleOrigin getOrigin() {
        return this.stylesheet != null ? this.stylesheet.getOrigin() : null;
    }

    public Rule(List<Selector> list, List<Declaration> list2) {
        int n;
        this.selectors = list;
        this.declarations = list2;
        this.serializedDecls = null;
        this.bssVersion = 5;
        int n2 = list != null ? list.size() : 0;
        for (n = 0; n < n2; ++n) {
            Selector selector = list.get(n);
            selector.setRule(this);
        }
        n = list2 != null ? list2.size() : 0;
        for (int i = 0; i < n; ++i) {
            Declaration declaration = list2.get(i);
            declaration.rule = this;
        }
    }

    private Rule(List<Selector> list, byte[] arrby, int n) {
        this.selectors = list;
        this.declarations = null;
        this.serializedDecls = arrby;
        this.bssVersion = n;
        int n2 = list != null ? list.size() : 0;
        for (int i = 0; i < n2; ++i) {
            Selector selector = list.get(i);
            selector.setRule(this);
        }
    }

    long applies(Node node, Set<PseudoClass>[] arrset) {
        long l = 0L;
        for (int i = 0; i < this.selectors.size(); ++i) {
            Selector selector = this.selectors.get(i);
            if (!selector.applies((Styleable)node, arrset, 0)) continue;
            l |= 1L << i;
        }
        return l;
    }

    public String toString() {
        int n;
        StringBuilder stringBuilder = new StringBuilder();
        if (this.selectors.size() > 0) {
            stringBuilder.append(this.selectors.get(0));
        }
        for (n = 1; n < this.selectors.size(); ++n) {
            stringBuilder.append(',');
            stringBuilder.append(this.selectors.get(n));
        }
        stringBuilder.append("{\n");
        n = this.declarations != null ? this.declarations.size() : 0;
        for (int i = 0; i < n; ++i) {
            stringBuilder.append("\t");
            stringBuilder.append(this.declarations.get(i));
            stringBuilder.append("\n");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    final void writeBinary(DataOutputStream dataOutputStream, StringStore stringStore) throws IOException {
        Object object;
        int n = this.selectors != null ? this.selectors.size() : 0;
        dataOutputStream.writeShort(n);
        for (int i = 0; i < n; ++i) {
            object = this.selectors.get(i);
            ((Selector)object).writeBinary(dataOutputStream, stringStore);
        }
        List<Declaration> list = this.getUnobservedDeclarationList();
        if (list != null) {
            object = new ByteArrayOutputStream(5192);
            DataOutputStream dataOutputStream2 = new DataOutputStream((OutputStream)object);
            int n2 = list.size();
            dataOutputStream2.writeShort(n2);
            for (int i = 0; i < n2; ++i) {
                Declaration declaration = this.declarations.get(i);
                declaration.writeBinary(dataOutputStream2, stringStore);
            }
            dataOutputStream.writeInt(((ByteArrayOutputStream)object).size());
            dataOutputStream.write(((ByteArrayOutputStream)object).toByteArray());
        } else {
            dataOutputStream.writeShort(0);
        }
    }

    static Rule readBinary(int n, DataInputStream dataInputStream, String[] arrstring) throws IOException {
        ArrayList<Declaration> arrayList;
        int n2;
        int n3 = dataInputStream.readShort();
        ArrayList<Selector> arrayList2 = new ArrayList<Selector>(n3);
        for (n2 = 0; n2 < n3; ++n2) {
            arrayList = Selector.readBinary(n, dataInputStream, arrstring);
            arrayList2.add((Selector)((Object)arrayList));
        }
        if (n < 4) {
            n2 = dataInputStream.readShort();
            arrayList = new ArrayList<Declaration>(n2);
            for (int i = 0; i < n2; ++i) {
                Declaration declaration = Declaration.readBinary(n, dataInputStream, arrstring);
                arrayList.add(declaration);
            }
            return new Rule(arrayList2, arrayList);
        }
        n2 = dataInputStream.readInt();
        arrayList = (ArrayList<Declaration>)new byte[n2];
        if (n2 > 0) {
            dataInputStream.readFully((byte[])arrayList);
        }
        return new Rule((List<Selector>)arrayList2, (byte[])arrayList, n);
    }

    private static final class Observables {
        private final Rule rule;
        private final ObservableList<Selector> selectorObservableList;
        private final ObservableList<Declaration> declarationObservableList;

        private Observables(Rule rule) {
            this.rule = rule;
            this.selectorObservableList = new TrackableObservableList<Selector>(rule.getUnobservedSelectorList()){

                @Override
                protected void onChanged(ListChangeListener.Change<Selector> change) {
                    while (change.next()) {
                        Selector selector;
                        int n;
                        int n2;
                        List list;
                        if (change.wasAdded()) {
                            list = change.getAddedSubList();
                            n2 = list.size();
                            for (n = 0; n < n2; ++n) {
                                selector = (Selector)list.get(n);
                                selector.setRule(rule);
                            }
                        }
                        if (!change.wasRemoved()) continue;
                        list = change.getAddedSubList();
                        n2 = list.size();
                        for (n = 0; n < n2; ++n) {
                            selector = (Selector)list.get(n);
                            if (selector.getRule() != rule) continue;
                            selector.setRule(null);
                        }
                    }
                }
            };
            this.declarationObservableList = new TrackableObservableList<Declaration>(rule.getUnobservedDeclarationList()){

                @Override
                protected void onChanged(ListChangeListener.Change<Declaration> change) {
                    while (change.next()) {
                        Declaration declaration;
                        int n;
                        int n2;
                        List list;
                        if (change.wasAdded()) {
                            list = change.getAddedSubList();
                            n2 = list.size();
                            for (n = 0; n < n2; ++n) {
                                declaration = (Declaration)list.get(n);
                                declaration.rule = rule;
                                Stylesheet stylesheet = rule.stylesheet;
                                if (stylesheet == null || stylesheet.getUrl() == null) continue;
                                String string = stylesheet.getUrl();
                                declaration.fixUrl(string);
                            }
                        }
                        if (!change.wasRemoved()) continue;
                        list = change.getRemoved();
                        n2 = list.size();
                        for (n = 0; n < n2; ++n) {
                            declaration = (Declaration)list.get(n);
                            if (declaration.rule != rule) continue;
                            declaration.rule = null;
                        }
                    }
                }
            };
        }

        private ObservableList<Selector> getSelectors() {
            return this.selectorObservableList;
        }

        private ObservableList<Declaration> getDeclarations() {
            return this.declarationObservableList;
        }
    }
}

