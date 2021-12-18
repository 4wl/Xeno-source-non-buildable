/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.ConditionalFeature
 *  javafx.beans.InvalidationListener
 *  javafx.event.Event
 *  javafx.event.EventType
 *  javafx.geometry.NodeOrientation
 *  javafx.scene.control.IndexRange
 *  javafx.scene.control.TextInputControl
 *  javafx.scene.input.KeyEvent
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.behavior.TextInputControlBindings;
import com.sun.javafx.scene.control.skin.TextInputControlSkin;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.List;
import javafx.application.ConditionalFeature;
import javafx.beans.InvalidationListener;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyEvent;

public abstract class TextInputControlBehavior<T extends TextInputControl>
extends BehaviorBase<T> {
    protected static final List<KeyBinding> TEXT_INPUT_BINDINGS = new ArrayList<KeyBinding>();
    T textInputControl;
    private KeyEvent lastEvent;
    private InvalidationListener textListener = observable -> this.invalidateBidi();
    private Bidi bidi = null;
    private Boolean mixed = null;
    private Boolean rtlText = null;
    private boolean editing = false;

    public TextInputControlBehavior(T t, List<KeyBinding> list) {
        super(t, list);
        this.textInputControl = t;
        t.textProperty().addListener(this.textListener);
    }

    @Override
    public void dispose() {
        this.textInputControl.textProperty().removeListener(this.textListener);
        super.dispose();
    }

    protected abstract void deleteChar(boolean var1);

    protected abstract void replaceText(int var1, int var2, String var3);

    protected abstract void setCaretAnimating(boolean var1);

    protected abstract void deleteFromLineStart();

    protected void scrollCharacterToVisible(int n) {
    }

    @Override
    protected void callActionForEvent(KeyEvent keyEvent) {
        this.lastEvent = keyEvent;
        super.callActionForEvent(keyEvent);
    }

    @Override
    public void callAction(String string) {
        TextInputControl textInputControl = (TextInputControl)this.getControl();
        boolean bl = false;
        this.setCaretAnimating(false);
        if (textInputControl.isEditable()) {
            this.setEditing(true);
            bl = true;
            if ("InputCharacter".equals(string)) {
                this.defaultKeyTyped(this.lastEvent);
            } else if ("Cut".equals(string)) {
                this.cut();
            } else if ("Paste".equals(string)) {
                this.paste();
            } else if ("DeleteFromLineStart".equals(string)) {
                this.deleteFromLineStart();
            } else if ("DeletePreviousChar".equals(string)) {
                this.deletePreviousChar();
            } else if ("DeleteNextChar".equals(string)) {
                this.deleteNextChar();
            } else if ("DeletePreviousWord".equals(string)) {
                this.deletePreviousWord();
            } else if ("DeleteNextWord".equals(string)) {
                this.deleteNextWord();
            } else if ("DeleteSelection".equals(string)) {
                this.deleteSelection();
            } else if ("Undo".equals(string)) {
                textInputControl.undo();
            } else if ("Redo".equals(string)) {
                textInputControl.redo();
            } else {
                bl = false;
            }
            this.setEditing(false);
        }
        if (!bl) {
            bl = true;
            if ("Copy".equals(string)) {
                textInputControl.copy();
            } else if ("SelectBackward".equals(string)) {
                textInputControl.selectBackward();
            } else if ("SelectForward".equals(string)) {
                textInputControl.selectForward();
            } else if ("SelectLeft".equals(string)) {
                this.selectLeft();
            } else if ("SelectRight".equals(string)) {
                this.selectRight();
            } else if ("PreviousWord".equals(string)) {
                this.previousWord();
            } else if ("NextWord".equals(string)) {
                this.nextWord();
            } else if ("LeftWord".equals(string)) {
                this.leftWord();
            } else if ("RightWord".equals(string)) {
                this.rightWord();
            } else if ("SelectPreviousWord".equals(string)) {
                this.selectPreviousWord();
            } else if ("SelectNextWord".equals(string)) {
                this.selectNextWord();
            } else if ("SelectLeftWord".equals(string)) {
                this.selectLeftWord();
            } else if ("SelectRightWord".equals(string)) {
                this.selectRightWord();
            } else if ("SelectWord".equals(string)) {
                this.selectWord();
            } else if ("SelectAll".equals(string)) {
                textInputControl.selectAll();
            } else if ("Home".equals(string)) {
                textInputControl.home();
            } else if ("End".equals(string)) {
                textInputControl.end();
            } else if ("Forward".equals(string)) {
                textInputControl.forward();
            } else if ("Backward".equals(string)) {
                textInputControl.backward();
            } else if ("Right".equals(string)) {
                this.nextCharacterVisually(true);
            } else if ("Left".equals(string)) {
                this.nextCharacterVisually(false);
            } else if ("Fire".equals(string)) {
                this.fire(this.lastEvent);
            } else if ("Cancel".equals(string)) {
                this.cancelEdit(this.lastEvent);
            } else if ("Unselect".equals(string)) {
                textInputControl.deselect();
            } else if ("SelectHome".equals(string)) {
                this.selectHome();
            } else if ("SelectEnd".equals(string)) {
                this.selectEnd();
            } else if ("SelectHomeExtend".equals(string)) {
                this.selectHomeExtend();
            } else if ("SelectEndExtend".equals(string)) {
                this.selectEndExtend();
            } else if ("ToParent".equals(string)) {
                this.forwardToParent(this.lastEvent);
            } else if ("UseVK".equals(string) && PlatformImpl.isSupported(ConditionalFeature.VIRTUAL_KEYBOARD)) {
                ((TextInputControlSkin)textInputControl.getSkin()).toggleUseVK();
            } else {
                bl = false;
            }
        }
        this.setCaretAnimating(true);
        if (!bl) {
            if ("TraverseNext".equals(string)) {
                this.traverseNext();
            } else if ("TraversePrevious".equals(string)) {
                this.traversePrevious();
            } else {
                super.callAction(string);
            }
        }
    }

    private void defaultKeyTyped(KeyEvent keyEvent) {
        TextInputControl textInputControl = (TextInputControl)this.getControl();
        if (!textInputControl.isEditable() || textInputControl.isDisabled()) {
            return;
        }
        String string = keyEvent.getCharacter();
        if (string.length() == 0) {
            return;
        }
        if ((keyEvent.isControlDown() || keyEvent.isAltDown() || PlatformUtil.isMac() && keyEvent.isMetaDown()) && (!keyEvent.isControlDown() && !PlatformUtil.isMac() || !keyEvent.isAltDown())) {
            return;
        }
        if (string.charAt(0) > '\u001f' && string.charAt(0) != '\u007f' && !keyEvent.isMetaDown()) {
            IndexRange indexRange = textInputControl.getSelection();
            int n = indexRange.getStart();
            int n2 = indexRange.getEnd();
            this.replaceText(n, n2, string);
            this.scrollCharacterToVisible(n);
        }
    }

    private void invalidateBidi() {
        this.bidi = null;
        this.mixed = null;
        this.rtlText = null;
    }

    private Bidi getBidi() {
        if (this.bidi == null) {
            this.bidi = new Bidi(this.textInputControl.textProperty().getValueSafe(), this.textInputControl.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT ? 1 : 0);
        }
        return this.bidi;
    }

    protected boolean isMixed() {
        if (this.mixed == null) {
            this.mixed = this.getBidi().isMixed();
        }
        return this.mixed;
    }

    protected boolean isRTLText() {
        if (this.rtlText == null) {
            Bidi bidi = this.getBidi();
            this.rtlText = bidi.isRightToLeft() || this.isMixed() && this.textInputControl.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
        }
        return this.rtlText;
    }

    private void nextCharacterVisually(boolean bl) {
        if (this.isMixed()) {
            TextInputControlSkin textInputControlSkin = (TextInputControlSkin)this.textInputControl.getSkin();
            textInputControlSkin.nextCharacterVisually(bl);
        } else if (bl != this.isRTLText()) {
            this.textInputControl.forward();
        } else {
            this.textInputControl.backward();
        }
    }

    private void selectLeft() {
        if (this.isRTLText()) {
            this.textInputControl.selectForward();
        } else {
            this.textInputControl.selectBackward();
        }
    }

    private void selectRight() {
        if (this.isRTLText()) {
            this.textInputControl.selectBackward();
        } else {
            this.textInputControl.selectForward();
        }
    }

    private void deletePreviousChar() {
        this.deleteChar(true);
    }

    private void deleteNextChar() {
        this.deleteChar(false);
    }

    protected void deletePreviousWord() {
        TextInputControl textInputControl = (TextInputControl)this.getControl();
        int n = textInputControl.getCaretPosition();
        if (n > 0) {
            textInputControl.previousWord();
            int n2 = textInputControl.getCaretPosition();
            this.replaceText(n2, n, "");
        }
    }

    protected void deleteNextWord() {
        TextInputControl textInputControl = (TextInputControl)this.getControl();
        int n = textInputControl.getCaretPosition();
        if (n < textInputControl.getLength()) {
            this.nextWord();
            int n2 = textInputControl.getCaretPosition();
            this.replaceText(n, n2, "");
        }
    }

    private void deleteSelection() {
        TextInputControl textInputControl = (TextInputControl)this.getControl();
        IndexRange indexRange = textInputControl.getSelection();
        if (indexRange.getLength() > 0) {
            this.deleteChar(false);
        }
    }

    private void cut() {
        TextInputControl textInputControl = (TextInputControl)this.getControl();
        textInputControl.cut();
    }

    private void paste() {
        TextInputControl textInputControl = (TextInputControl)this.getControl();
        textInputControl.paste();
    }

    protected void selectPreviousWord() {
        ((TextInputControl)this.getControl()).selectPreviousWord();
    }

    protected void selectNextWord() {
        TextInputControl textInputControl = (TextInputControl)this.getControl();
        if (PlatformUtil.isMac() || PlatformUtil.isLinux()) {
            textInputControl.selectEndOfNextWord();
        } else {
            textInputControl.selectNextWord();
        }
    }

    private void selectLeftWord() {
        if (this.isRTLText()) {
            this.selectNextWord();
        } else {
            this.selectPreviousWord();
        }
    }

    private void selectRightWord() {
        if (this.isRTLText()) {
            this.selectPreviousWord();
        } else {
            this.selectNextWord();
        }
    }

    protected void selectWord() {
        TextInputControl textInputControl = (TextInputControl)this.getControl();
        textInputControl.previousWord();
        if (PlatformUtil.isWindows()) {
            textInputControl.selectNextWord();
        } else {
            textInputControl.selectEndOfNextWord();
        }
    }

    protected void previousWord() {
        ((TextInputControl)this.getControl()).previousWord();
    }

    protected void nextWord() {
        TextInputControl textInputControl = (TextInputControl)this.getControl();
        if (PlatformUtil.isMac() || PlatformUtil.isLinux()) {
            textInputControl.endOfNextWord();
        } else {
            textInputControl.nextWord();
        }
    }

    private void leftWord() {
        if (this.isRTLText()) {
            this.nextWord();
        } else {
            this.previousWord();
        }
    }

    private void rightWord() {
        if (this.isRTLText()) {
            this.previousWord();
        } else {
            this.nextWord();
        }
    }

    protected void fire(KeyEvent keyEvent) {
    }

    protected void cancelEdit(KeyEvent keyEvent) {
        this.forwardToParent(keyEvent);
    }

    protected void forwardToParent(KeyEvent keyEvent) {
        if (((TextInputControl)this.getControl()).getParent() != null) {
            ((TextInputControl)this.getControl()).getParent().fireEvent((Event)keyEvent);
        }
    }

    private void selectHome() {
        ((TextInputControl)this.getControl()).selectHome();
    }

    private void selectEnd() {
        ((TextInputControl)this.getControl()).selectEnd();
    }

    private void selectHomeExtend() {
        ((TextInputControl)this.getControl()).extendSelection(0);
    }

    private void selectEndExtend() {
        TextInputControl textInputControl = (TextInputControl)this.getControl();
        textInputControl.extendSelection(textInputControl.getLength());
    }

    protected void setEditing(boolean bl) {
        this.editing = bl;
    }

    public boolean isEditing() {
        return this.editing;
    }

    static {
        TEXT_INPUT_BINDINGS.addAll(TextInputControlBindings.BINDINGS);
        TEXT_INPUT_BINDINGS.add(new KeyBinding(null, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Consume"));
    }
}

