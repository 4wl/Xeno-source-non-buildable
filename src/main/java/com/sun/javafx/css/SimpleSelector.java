/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.PseudoClass
 *  javafx.css.Styleable
 *  javafx.geometry.NodeOrientation
 *  javafx.scene.Node
 */
package com.sun.javafx.css;

import com.sun.javafx.css.BitSet;
import com.sun.javafx.css.Match;
import com.sun.javafx.css.PseudoClassState;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.StringStore;
import com.sun.javafx.css.StyleClass;
import com.sun.javafx.css.StyleClassSet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;

public final class SimpleSelector
extends Selector {
    private final String name;
    private final StyleClassSet styleClassSet;
    private final String id;
    private final PseudoClassState pseudoClassState;
    private final boolean matchOnName;
    private final boolean matchOnId;
    private final boolean matchOnStyleClass;
    private final NodeOrientation nodeOrientation;

    public String getName() {
        return this.name;
    }

    public List<String> getStyleClasses() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Iterator iterator = this.styleClassSet.iterator();
        while (iterator.hasNext()) {
            arrayList.add(((StyleClass)iterator.next()).getStyleClassName());
        }
        return Collections.unmodifiableList(arrayList);
    }

    Set<StyleClass> getStyleClassSet() {
        return this.styleClassSet;
    }

    public String getId() {
        return this.id;
    }

    Set<PseudoClass> getPseudoClassStates() {
        return this.pseudoClassState;
    }

    public List<String> getPseudoclasses() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Iterator iterator = this.pseudoClassState.iterator();
        while (iterator.hasNext()) {
            arrayList.add(((PseudoClass)iterator.next()).getPseudoClassName());
        }
        if (this.nodeOrientation == NodeOrientation.RIGHT_TO_LEFT) {
            arrayList.add("dir(rtl)");
        } else if (this.nodeOrientation == NodeOrientation.LEFT_TO_RIGHT) {
            arrayList.add("dir(ltr)");
        }
        return Collections.unmodifiableList(arrayList);
    }

    NodeOrientation getNodeOrientation() {
        return this.nodeOrientation;
    }

    public SimpleSelector(String string, List<String> list, List<String> list2, String string2) {
        Object object;
        this.name = string == null ? "*" : string;
        this.matchOnName = string != null && !"".equals(string) && !"*".equals(string);
        this.styleClassSet = new StyleClassSet();
        int n = list != null ? list.size() : 0;
        for (int i = 0; i < n; ++i) {
            String string3 = list.get(i);
            if (string3 == null || string3.isEmpty()) continue;
            object = StyleClassSet.getStyleClass(string3);
            this.styleClassSet.add(object);
        }
        this.matchOnStyleClass = this.styleClassSet.size() > 0;
        this.pseudoClassState = new PseudoClassState();
        n = list2 != null ? list2.size() : 0;
        NodeOrientation nodeOrientation = NodeOrientation.INHERIT;
        for (int i = 0; i < n; ++i) {
            object = list2.get(i);
            if (object == null || ((String)object).isEmpty()) continue;
            if ("dir(".regionMatches(true, 0, (String)object, 0, 4)) {
                boolean bl = "dir(rtl)".equalsIgnoreCase((String)object);
                nodeOrientation = bl ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT;
                continue;
            }
            PseudoClass pseudoClass = PseudoClassState.getPseudoClass((String)object);
            this.pseudoClassState.add(pseudoClass);
        }
        this.nodeOrientation = nodeOrientation;
        this.id = string2 == null ? "" : string2;
        this.matchOnId = string2 != null && !"".equals(string2);
    }

    @Override
    Match createMatch() {
        int n = this.matchOnId ? 1 : 0;
        int n2 = this.styleClassSet.size();
        return new Match(this, this.pseudoClassState, n, n2);
    }

    @Override
    public boolean applies(Styleable styleable) {
        boolean bl;
        boolean bl2;
        Object object;
        NodeOrientation nodeOrientation;
        if (this.nodeOrientation != NodeOrientation.INHERIT && styleable instanceof Node && ((nodeOrientation = (object = (Node)styleable).getNodeOrientation()) == NodeOrientation.INHERIT ? object.getEffectiveNodeOrientation() != this.nodeOrientation : nodeOrientation != this.nodeOrientation)) {
            return false;
        }
        if (this.matchOnId && !(bl2 = this.id.equals(object = styleable.getId()))) {
            return false;
        }
        if (this.matchOnName && !(bl = this.name.equals(object = styleable.getTypeSelector()))) {
            return false;
        }
        if (this.matchOnStyleClass) {
            int n;
            object = new StyleClassSet();
            nodeOrientation = styleable.getStyleClass();
            int n2 = nodeOrientation.size();
            for (n = 0; n < n2; ++n) {
                String string = (String)nodeOrientation.get(n);
                if (string == null || string.isEmpty()) continue;
                StyleClass styleClass = StyleClassSet.getStyleClass(string);
                ((BitSet)object).add(styleClass);
            }
            n = this.matchStyleClasses((StyleClassSet)object) ? 1 : 0;
            if (n == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    boolean applies(Styleable styleable, Set<PseudoClass>[] arrset, int n) {
        boolean bl = this.applies(styleable);
        if (bl && arrset != null && n < arrset.length) {
            if (arrset[n] == null) {
                arrset[n] = new PseudoClassState();
            }
            arrset[n].addAll((Collection<PseudoClass>)((Object)this.pseudoClassState));
        }
        return bl;
    }

    @Override
    public boolean stateMatches(Styleable styleable, Set<PseudoClass> set) {
        return set != null ? set.containsAll((Collection<?>)((Object)this.pseudoClassState)) : false;
    }

    private boolean matchStyleClasses(StyleClassSet styleClassSet) {
        return styleClassSet.containsAll((Collection)((Object)this.styleClassSet));
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        SimpleSelector simpleSelector = (SimpleSelector)object;
        if (this.name == null ? simpleSelector.name != null : !this.name.equals(simpleSelector.name)) {
            return false;
        }
        if (this.id == null ? simpleSelector.id != null : !this.id.equals(simpleSelector.id)) {
            return false;
        }
        if (!this.styleClassSet.equals(simpleSelector.styleClassSet)) {
            return false;
        }
        return this.pseudoClassState.equals(simpleSelector.pseudoClassState);
    }

    public int hashCode() {
        int n = 7;
        n = 31 * (n + this.name.hashCode());
        n = 31 * (n + this.styleClassSet.hashCode());
        n = 31 * (n + this.styleClassSet.hashCode());
        n = this.id != null ? 31 * (n + this.id.hashCode()) : 0;
        n = 31 * (n + this.pseudoClassState.hashCode());
        return n;
    }

    public String toString() {
        Object object;
        StringBuilder stringBuilder = new StringBuilder();
        if (this.name != null && !this.name.isEmpty()) {
            stringBuilder.append(this.name);
        } else {
            stringBuilder.append("*");
        }
        Iterator iterator = this.styleClassSet.iterator();
        while (iterator.hasNext()) {
            object = (StyleClass)iterator.next();
            stringBuilder.append('.').append(((StyleClass)object).getStyleClassName());
        }
        if (this.id != null && !this.id.isEmpty()) {
            stringBuilder.append('#');
            stringBuilder.append(this.id);
        }
        object = this.pseudoClassState.iterator();
        while (object.hasNext()) {
            PseudoClass pseudoClass = (PseudoClass)object.next();
            stringBuilder.append(':').append(pseudoClass.getPseudoClassName());
        }
        return stringBuilder.toString();
    }

    @Override
    public final void writeBinary(DataOutputStream dataOutputStream, StringStore stringStore) throws IOException {
        super.writeBinary(dataOutputStream, stringStore);
        dataOutputStream.writeShort(stringStore.addString(this.name));
        dataOutputStream.writeShort(this.styleClassSet.size());
        Iterator iterator = this.styleClassSet.iterator();
        while (iterator.hasNext()) {
            StyleClass styleClass = (StyleClass)iterator.next();
            dataOutputStream.writeShort(stringStore.addString(styleClass.getStyleClassName()));
        }
        dataOutputStream.writeShort(stringStore.addString(this.id));
        int n = this.pseudoClassState.size() + (this.nodeOrientation == NodeOrientation.RIGHT_TO_LEFT || this.nodeOrientation == NodeOrientation.LEFT_TO_RIGHT ? 1 : 0);
        dataOutputStream.writeShort(n);
        Iterator iterator2 = this.pseudoClassState.iterator();
        while (iterator2.hasNext()) {
            PseudoClass pseudoClass = (PseudoClass)iterator2.next();
            dataOutputStream.writeShort(stringStore.addString(pseudoClass.getPseudoClassName()));
        }
        if (this.nodeOrientation == NodeOrientation.RIGHT_TO_LEFT) {
            dataOutputStream.writeShort(stringStore.addString("dir(rtl)"));
        } else if (this.nodeOrientation == NodeOrientation.LEFT_TO_RIGHT) {
            dataOutputStream.writeShort(stringStore.addString("dir(ltr)"));
        }
    }

    static SimpleSelector readBinary(int n, DataInputStream dataInputStream, String[] arrstring) throws IOException {
        String string = arrstring[dataInputStream.readShort()];
        int n2 = dataInputStream.readShort();
        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i = 0; i < n2; ++i) {
            arrayList.add(arrstring[dataInputStream.readShort()]);
        }
        String string2 = arrstring[dataInputStream.readShort()];
        int n3 = dataInputStream.readShort();
        ArrayList<String> arrayList2 = new ArrayList<String>();
        for (int i = 0; i < n3; ++i) {
            arrayList2.add(arrstring[dataInputStream.readShort()]);
        }
        return new SimpleSelector(string, arrayList, arrayList2, string2);
    }
}

