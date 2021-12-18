/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableSet
 *  javafx.css.PseudoClass
 *  javafx.css.Styleable
 */
package com.sun.javafx.css;

import com.sun.javafx.css.Combinator;
import com.sun.javafx.css.Match;
import com.sun.javafx.css.PseudoClassState;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.SimpleSelector;
import com.sun.javafx.css.StringStore;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javafx.collections.ObservableSet;
import javafx.css.PseudoClass;
import javafx.css.Styleable;

public final class CompoundSelector
extends Selector {
    private final List<SimpleSelector> selectors;
    private final List<Combinator> relationships;
    private int hash = -1;

    public List<SimpleSelector> getSelectors() {
        return this.selectors;
    }

    public List<Combinator> getRelationships() {
        return this.relationships;
    }

    public CompoundSelector(List<SimpleSelector> list, List<Combinator> list2) {
        this.selectors = list != null ? Collections.unmodifiableList(list) : Collections.EMPTY_LIST;
        this.relationships = list2 != null ? Collections.unmodifiableList(list2) : Collections.EMPTY_LIST;
    }

    private CompoundSelector() {
        this(null, null);
    }

    @Override
    Match createMatch() {
        PseudoClassState pseudoClassState = new PseudoClassState();
        int n = 0;
        int n2 = 0;
        int n3 = this.selectors.size();
        for (int i = 0; i < n3; ++i) {
            Selector selector = this.selectors.get(i);
            Match match = selector.createMatch();
            pseudoClassState.addAll((Collection)((Object)match.pseudoClasses));
            n += match.idCount;
            n2 += match.styleClassCount;
        }
        return new Match(this, pseudoClassState, n, n2);
    }

    @Override
    public boolean applies(Styleable styleable) {
        return this.applies(styleable, this.selectors.size() - 1, null, 0);
    }

    @Override
    boolean applies(Styleable styleable, Set<PseudoClass>[] arrset, int n) {
        assert (arrset == null || n < arrset.length);
        if (arrset != null && arrset.length <= n) {
            return false;
        }
        PseudoClassState[] arrpseudoClassState = arrset != null ? new PseudoClassState[arrset.length] : null;
        boolean bl = this.applies(styleable, this.selectors.size() - 1, (Set<PseudoClass>[])arrpseudoClassState, n);
        if (bl && arrpseudoClassState != null) {
            for (int i = 0; i < arrset.length; ++i) {
                Set<PseudoClass> set = arrset[i];
                PseudoClassState pseudoClassState = arrpseudoClassState[i];
                if (set != null) {
                    set.addAll((Collection<PseudoClass>)((Object)pseudoClassState));
                    continue;
                }
                arrset[i] = pseudoClassState;
            }
        }
        return bl;
    }

    private boolean applies(Styleable styleable, int n, Set<PseudoClass>[] arrset, int n2) {
        if (n < 0) {
            return false;
        }
        if (!this.selectors.get(n).applies(styleable, arrset, n2)) {
            return false;
        }
        if (n == 0) {
            return true;
        }
        Combinator combinator = this.relationships.get(n - 1);
        if (combinator == Combinator.CHILD) {
            Styleable styleable2 = styleable.getStyleableParent();
            if (styleable2 == null) {
                return false;
            }
            return this.applies(styleable2, n - 1, arrset, ++n2);
        }
        for (Styleable styleable3 = styleable.getStyleableParent(); styleable3 != null; styleable3 = styleable3.getStyleableParent()) {
            boolean bl;
            if (!(bl = this.applies(styleable3, n - 1, arrset, ++n2))) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean stateMatches(Styleable styleable, Set<PseudoClass> set) {
        return this.stateMatches(styleable, set, this.selectors.size() - 1);
    }

    private boolean stateMatches(Styleable styleable, Set<PseudoClass> set, int n) {
        if (n < 0) {
            return false;
        }
        if (!this.selectors.get(n).stateMatches(styleable, set)) {
            return false;
        }
        if (n == 0) {
            return true;
        }
        Combinator combinator = this.relationships.get(n - 1);
        if (combinator == Combinator.CHILD) {
            Styleable styleable2 = styleable.getStyleableParent();
            if (styleable2 == null) {
                return false;
            }
            if (this.selectors.get(n - 1).applies(styleable2)) {
                ObservableSet observableSet = styleable2.getPseudoClassStates();
                return this.stateMatches(styleable2, (Set<PseudoClass>)observableSet, n - 1);
            }
        } else {
            for (Styleable styleable3 = styleable.getStyleableParent(); styleable3 != null; styleable3 = styleable3.getStyleableParent()) {
                if (!this.selectors.get(n - 1).applies(styleable3)) continue;
                ObservableSet observableSet = styleable3.getPseudoClassStates();
                return this.stateMatches(styleable3, (Set<PseudoClass>)observableSet, n - 1);
            }
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == -1) {
            int n;
            int n2 = this.selectors.size();
            for (n = 0; n < n2; ++n) {
                this.hash = 31 * (this.hash + this.selectors.get(n).hashCode());
            }
            n2 = this.relationships.size();
            for (n = 0; n < n2; ++n) {
                this.hash = 31 * (this.hash + this.relationships.get(n).hashCode());
            }
        }
        return this.hash;
    }

    public boolean equals(Object object) {
        int n;
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        CompoundSelector compoundSelector = (CompoundSelector)object;
        if (compoundSelector.selectors.size() != this.selectors.size()) {
            return false;
        }
        int n2 = this.selectors.size();
        for (n = 0; n < n2; ++n) {
            if (compoundSelector.selectors.get(n).equals(this.selectors.get(n))) continue;
            return false;
        }
        if (compoundSelector.relationships.size() != this.relationships.size()) {
            return false;
        }
        n2 = this.relationships.size();
        for (n = 0; n < n2; ++n) {
            if (compoundSelector.relationships.get(n).equals((Object)this.relationships.get(n))) continue;
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.selectors.get(0));
        for (int i = 1; i < this.selectors.size(); ++i) {
            stringBuilder.append((Object)this.relationships.get(i - 1));
            stringBuilder.append(this.selectors.get(i));
        }
        return stringBuilder.toString();
    }

    @Override
    public final void writeBinary(DataOutputStream dataOutputStream, StringStore stringStore) throws IOException {
        int n;
        super.writeBinary(dataOutputStream, stringStore);
        dataOutputStream.writeShort(this.selectors.size());
        for (n = 0; n < this.selectors.size(); ++n) {
            this.selectors.get(n).writeBinary(dataOutputStream, stringStore);
        }
        dataOutputStream.writeShort(this.relationships.size());
        for (n = 0; n < this.relationships.size(); ++n) {
            dataOutputStream.writeByte(this.relationships.get(n).ordinal());
        }
    }

    public static CompoundSelector readBinary(int n, DataInputStream dataInputStream, String[] arrstring) throws IOException {
        int n2;
        int n3 = dataInputStream.readShort();
        ArrayList<SimpleSelector> arrayList = new ArrayList<SimpleSelector>();
        for (n2 = 0; n2 < n3; ++n2) {
            arrayList.add((SimpleSelector)Selector.readBinary(n, dataInputStream, arrstring));
        }
        n2 = dataInputStream.readShort();
        ArrayList<Combinator> arrayList2 = new ArrayList<Combinator>();
        for (int i = 0; i < n2; ++i) {
            byte by = dataInputStream.readByte();
            if (by == Combinator.CHILD.ordinal()) {
                arrayList2.add(Combinator.CHILD);
                continue;
            }
            if (by == Combinator.DESCENDANT.ordinal()) {
                arrayList2.add(Combinator.DESCENDANT);
                continue;
            }
            assert (false) : "error deserializing CompoundSelector: Combinator = " + by;
            arrayList2.add(Combinator.DESCENDANT);
        }
        return new CompoundSelector(arrayList, arrayList2);
    }
}

