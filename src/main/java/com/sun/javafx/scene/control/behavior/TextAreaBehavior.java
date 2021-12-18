/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.ObservableValue
 *  javafx.event.EventType
 *  javafx.geometry.Bounds
 *  javafx.geometry.Point2D
 *  javafx.geometry.Rectangle2D
 *  javafx.scene.Node
 *  javafx.scene.Scene
 *  javafx.scene.control.ContextMenu
 *  javafx.scene.control.TextArea
 *  javafx.scene.input.ContextMenuEvent
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseButton
 *  javafx.scene.input.MouseEvent
 *  javafx.stage.Screen
 *  javafx.stage.Window
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.behavior.TextFieldBehavior;
import com.sun.javafx.scene.control.behavior.TextInputControlBehavior;
import com.sun.javafx.scene.control.behavior.TextInputControlBindings;
import com.sun.javafx.scene.control.behavior.TwoLevelFocusBehavior;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.util.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextArea;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Window;

public class TextAreaBehavior
extends TextInputControlBehavior<TextArea> {
    protected static final List<KeyBinding> TEXT_AREA_BINDINGS = new ArrayList<KeyBinding>();
    private TextAreaSkin skin;
    private ContextMenu contextMenu = new ContextMenu();
    private TwoLevelFocusBehavior tlFocus;
    private boolean focusGainedByMouseClick = false;
    private boolean shiftDown = false;
    private boolean deferClick = false;

    public TextAreaBehavior(TextArea textArea) {
        super(textArea, TEXT_AREA_BINDINGS);
        if (IS_TOUCH_SUPPORTED) {
            this.contextMenu.getStyleClass().add((Object)"text-input-context-menu");
        }
        textArea.focusedProperty().addListener((ChangeListener)new ChangeListener<Boolean>(){

            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean bl, Boolean bl2) {
                TextArea textArea = (TextArea)TextAreaBehavior.this.getControl();
                if (textArea.isFocused()) {
                    if (PlatformUtil.isIOS()) {
                        Bounds bounds = textArea.getBoundsInParent();
                        double d = bounds.getWidth();
                        double d2 = bounds.getHeight();
                        Affine3D affine3D = TextFieldBehavior.calculateNodeToSceneTransform((Node)textArea);
                        String string = textArea.textProperty().getValueSafe();
                        textArea.getScene().getWindow().impl_getPeer().requestInput(string, TextFieldBehavior.TextInputTypes.TEXT_AREA.ordinal(), d, d2, affine3D.getMxx(), affine3D.getMxy(), affine3D.getMxz(), affine3D.getMxt(), affine3D.getMyx(), affine3D.getMyy(), affine3D.getMyz(), affine3D.getMyt(), affine3D.getMzx(), affine3D.getMzy(), affine3D.getMzz(), affine3D.getMzt());
                    }
                    if (!TextAreaBehavior.this.focusGainedByMouseClick) {
                        TextAreaBehavior.this.setCaretAnimating(true);
                    }
                } else {
                    if (PlatformUtil.isIOS() && textArea.getScene() != null) {
                        textArea.getScene().getWindow().impl_getPeer().releaseInput();
                    }
                    TextAreaBehavior.this.focusGainedByMouseClick = false;
                    TextAreaBehavior.this.setCaretAnimating(false);
                }
            }
        });
        if (com.sun.javafx.scene.control.skin.Utils.isTwoLevelFocus()) {
            this.tlFocus = new TwoLevelFocusBehavior((Node)textArea);
        }
    }

    @Override
    public void dispose() {
        if (this.tlFocus != null) {
            this.tlFocus.dispose();
        }
        super.dispose();
    }

    public void setTextAreaSkin(TextAreaSkin textAreaSkin) {
        this.skin = textAreaSkin;
    }

    @Override
    public void callAction(String string) {
        TextArea textArea = (TextArea)this.getControl();
        boolean bl = false;
        if (textArea.isEditable()) {
            this.setEditing(true);
            bl = true;
            if ("InsertNewLine".equals(string)) {
                this.insertNewLine();
            } else if ("TraverseOrInsertTab".equals(string)) {
                this.insertTab();
            } else {
                bl = false;
            }
            this.setEditing(false);
        }
        if (!bl) {
            bl = true;
            if ("LineStart".equals(string)) {
                this.lineStart(false, false);
            } else if ("LineEnd".equals(string)) {
                this.lineEnd(false, false);
            } else if ("SelectLineStart".equals(string)) {
                this.lineStart(true, false);
            } else if ("SelectLineStartExtend".equals(string)) {
                this.lineStart(true, true);
            } else if ("SelectLineEnd".equals(string)) {
                this.lineEnd(true, false);
            } else if ("SelectLineEndExtend".equals(string)) {
                this.lineEnd(true, true);
            } else if ("PreviousLine".equals(string)) {
                this.skin.previousLine(false);
            } else if ("NextLine".equals(string)) {
                this.skin.nextLine(false);
            } else if ("SelectPreviousLine".equals(string)) {
                this.skin.previousLine(true);
            } else if ("SelectNextLine".equals(string)) {
                this.skin.nextLine(true);
            } else if ("ParagraphStart".equals(string)) {
                this.skin.paragraphStart(true, false);
            } else if ("ParagraphEnd".equals(string)) {
                this.skin.paragraphEnd(true, PlatformUtil.isWindows(), false);
            } else if ("SelectParagraphStart".equals(string)) {
                this.skin.paragraphStart(true, true);
            } else if ("SelectParagraphEnd".equals(string)) {
                this.skin.paragraphEnd(true, PlatformUtil.isWindows(), true);
            } else if ("PreviousPage".equals(string)) {
                this.skin.previousPage(false);
            } else if ("NextPage".equals(string)) {
                this.skin.nextPage(false);
            } else if ("SelectPreviousPage".equals(string)) {
                this.skin.previousPage(true);
            } else if ("SelectNextPage".equals(string)) {
                this.skin.nextPage(true);
            } else if ("TraverseOrInsertTab".equals(string)) {
                string = "TraverseNext";
                bl = false;
            } else {
                bl = false;
            }
        }
        if (!bl) {
            super.callAction(string);
        }
    }

    private void insertNewLine() {
        TextArea textArea = (TextArea)this.getControl();
        textArea.replaceSelection("\n");
    }

    private void insertTab() {
        TextArea textArea = (TextArea)this.getControl();
        textArea.replaceSelection("\t");
    }

    @Override
    protected void deleteChar(boolean bl) {
        this.skin.deleteChar(bl);
    }

    @Override
    protected void deleteFromLineStart() {
        TextArea textArea = (TextArea)this.getControl();
        int n = textArea.getCaretPosition();
        if (n > 0) {
            this.lineStart(false, false);
            int n2 = textArea.getCaretPosition();
            if (n > n2) {
                this.replaceText(n2, n, "");
            }
        }
    }

    private void lineStart(boolean bl, boolean bl2) {
        this.skin.lineStart(bl, bl2);
    }

    private void lineEnd(boolean bl, boolean bl2) {
        this.skin.lineEnd(bl, bl2);
    }

    @Override
    protected void scrollCharacterToVisible(int n) {
        this.skin.scrollCharacterToVisible(n);
    }

    @Override
    protected void replaceText(int n, int n2, String string) {
        ((TextArea)this.getControl()).replaceText(n, n2, string);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        TextArea textArea = (TextArea)this.getControl();
        super.mousePressed(mouseEvent);
        if (!textArea.isDisabled()) {
            if (!textArea.isFocused()) {
                this.focusGainedByMouseClick = true;
                textArea.requestFocus();
            }
            this.setCaretAnimating(false);
            if (mouseEvent.getButton() == MouseButton.PRIMARY && !mouseEvent.isMiddleButtonDown() && !mouseEvent.isSecondaryButtonDown()) {
                HitInfo hitInfo = this.skin.getIndex(mouseEvent.getX(), mouseEvent.getY());
                int n = com.sun.javafx.scene.control.skin.Utils.getHitInsertionIndex(hitInfo, textArea.textProperty().getValueSafe());
                int n2 = textArea.getAnchor();
                int n3 = textArea.getCaretPosition();
                if (mouseEvent.getClickCount() < 2 && (mouseEvent.isSynthesized() || n2 != n3 && (n > n2 && n < n3 || n < n2 && n > n3))) {
                    this.deferClick = true;
                } else if (!(mouseEvent.isControlDown() || mouseEvent.isAltDown() || mouseEvent.isShiftDown() || mouseEvent.isMetaDown() || mouseEvent.isShortcutDown())) {
                    switch (mouseEvent.getClickCount()) {
                        case 1: {
                            this.skin.positionCaret(hitInfo, false, false);
                            break;
                        }
                        case 2: {
                            this.mouseDoubleClick(hitInfo);
                            break;
                        }
                        case 3: {
                            this.mouseTripleClick(hitInfo);
                            break;
                        }
                    }
                } else if (!(!mouseEvent.isShiftDown() || mouseEvent.isControlDown() || mouseEvent.isAltDown() || mouseEvent.isMetaDown() || mouseEvent.isShortcutDown() || mouseEvent.getClickCount() != 1)) {
                    this.shiftDown = true;
                    if (PlatformUtil.isMac()) {
                        textArea.extendSelection(n);
                    } else {
                        this.skin.positionCaret(hitInfo, true, false);
                    }
                }
            }
            if (this.contextMenu.isShowing()) {
                this.contextMenu.hide();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        TextArea textArea = (TextArea)this.getControl();
        if (!(textArea.isDisabled() || mouseEvent.isSynthesized() || mouseEvent.getButton() != MouseButton.PRIMARY || mouseEvent.isMiddleButtonDown() || mouseEvent.isSecondaryButtonDown() || mouseEvent.isControlDown() || mouseEvent.isAltDown() || mouseEvent.isShiftDown() || mouseEvent.isMetaDown())) {
            this.skin.positionCaret(this.skin.getIndex(mouseEvent.getX(), mouseEvent.getY()), true, false);
        }
        this.deferClick = false;
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        TextArea textArea = (TextArea)this.getControl();
        super.mouseReleased(mouseEvent);
        if (!textArea.isDisabled()) {
            this.setCaretAnimating(false);
            if (this.deferClick) {
                this.deferClick = false;
                this.skin.positionCaret(this.skin.getIndex(mouseEvent.getX(), mouseEvent.getY()), this.shiftDown, false);
                this.shiftDown = false;
            }
            this.setCaretAnimating(true);
        }
    }

    @Override
    public void contextMenuRequested(ContextMenuEvent contextMenuEvent) {
        TextArea textArea = (TextArea)this.getControl();
        if (this.contextMenu.isShowing()) {
            this.contextMenu.hide();
        } else if (textArea.getContextMenu() == null) {
            Screen screen;
            double d = contextMenuEvent.getScreenX();
            double d2 = contextMenuEvent.getScreenY();
            double d3 = contextMenuEvent.getSceneX();
            if (IS_TOUCH_SUPPORTED) {
                Point2D point2D;
                if (textArea.getSelection().getLength() == 0) {
                    this.skin.positionCaret(this.skin.getIndex(contextMenuEvent.getX(), contextMenuEvent.getY()), false, false);
                    point2D = this.skin.getMenuPosition();
                } else {
                    point2D = this.skin.getMenuPosition();
                    if (point2D != null && (point2D.getX() <= 0.0 || point2D.getY() <= 0.0)) {
                        this.skin.positionCaret(this.skin.getIndex(contextMenuEvent.getX(), contextMenuEvent.getY()), false, false);
                        point2D = this.skin.getMenuPosition();
                    }
                }
                if (point2D != null) {
                    Point2D point2D2 = ((TextArea)this.getControl()).localToScene(point2D);
                    Scene scene = ((TextArea)this.getControl()).getScene();
                    Window window = scene.getWindow();
                    screen = new Point2D(window.getX() + scene.getX() + point2D2.getX(), window.getY() + scene.getY() + point2D2.getY());
                    d = screen.getX();
                    d3 = point2D2.getX();
                    d2 = screen.getY();
                }
            }
            this.skin.populateContextMenu(this.contextMenu);
            double d4 = this.contextMenu.prefWidth(-1.0);
            double d5 = d - (IS_TOUCH_SUPPORTED ? d4 / 2.0 : 0.0);
            screen = Utils.getScreenForPoint(d, 0.0);
            Rectangle2D rectangle2D = screen.getBounds();
            if (d5 < rectangle2D.getMinX()) {
                ((TextArea)this.getControl()).getProperties().put((Object)"CONTEXT_MENU_SCREEN_X", (Object)d);
                ((TextArea)this.getControl()).getProperties().put((Object)"CONTEXT_MENU_SCENE_X", (Object)d3);
                this.contextMenu.show(this.getControl(), rectangle2D.getMinX(), d2);
            } else if (d + d4 > rectangle2D.getMaxX()) {
                double d6 = d4 - (rectangle2D.getMaxX() - d);
                ((TextArea)this.getControl()).getProperties().put((Object)"CONTEXT_MENU_SCREEN_X", (Object)d);
                ((TextArea)this.getControl()).getProperties().put((Object)"CONTEXT_MENU_SCENE_X", (Object)d3);
                this.contextMenu.show(this.getControl(), d - d6, d2);
            } else {
                ((TextArea)this.getControl()).getProperties().put((Object)"CONTEXT_MENU_SCREEN_X", (Object)0);
                ((TextArea)this.getControl()).getProperties().put((Object)"CONTEXT_MENU_SCENE_X", (Object)0);
                this.contextMenu.show(this.getControl(), d5, d2);
            }
        }
        contextMenuEvent.consume();
    }

    @Override
    protected void setCaretAnimating(boolean bl) {
        this.skin.setCaretAnimating(bl);
    }

    protected void mouseDoubleClick(HitInfo hitInfo) {
        TextArea textArea = (TextArea)this.getControl();
        textArea.previousWord();
        if (PlatformUtil.isWindows()) {
            textArea.selectNextWord();
        } else {
            textArea.selectEndOfNextWord();
        }
    }

    protected void mouseTripleClick(HitInfo hitInfo) {
        this.skin.paragraphStart(false, false);
        this.skin.paragraphEnd(false, PlatformUtil.isWindows(), true);
    }

    static {
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.HOME, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "LineStart"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.END, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "LineEnd"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "PreviousLine"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "PreviousLine"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "NextLine"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "NextLine"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "PreviousPage"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "NextPage"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.ENTER, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "InsertNewLine"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.TAB, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "TraverseOrInsertTab"));
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.HOME, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectLineStart").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.END, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectLineEnd").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectPreviousLine").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectPreviousLine").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectNextLine").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectNextLine").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectPreviousPage").shift());
        TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectNextPage").shift());
        if (PlatformUtil.isMac()) {
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "LineStart").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "LineStart").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "LineEnd").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "LineEnd").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Home").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Home").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "End").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "End").shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectLineStartExtend").shift().shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectLineStartExtend").shift().shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectLineEndExtend").shift().shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectLineEndExtend").shift().shortcut());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectHomeExtend").shortcut().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectHomeExtend").shortcut().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectEndExtend").shortcut().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectEndExtend").shortcut().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "ParagraphStart").alt());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "ParagraphStart").alt());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "ParagraphEnd").alt());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "ParagraphEnd").alt());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectParagraphStart").alt().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectParagraphStart").alt().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectParagraphEnd").alt().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectParagraphEnd").alt().shift());
        } else {
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "ParagraphStart").ctrl());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "ParagraphStart").ctrl());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "ParagraphEnd").ctrl());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "ParagraphEnd").ctrl());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectParagraphStart").ctrl().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectParagraphStart").ctrl().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectParagraphEnd").ctrl().shift());
            TEXT_AREA_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "SelectParagraphEnd").ctrl().shift());
        }
        TEXT_AREA_BINDINGS.addAll(TextInputControlBindings.BINDINGS);
        TEXT_AREA_BINDINGS.add(new KeyBinding(null, (EventType<KeyEvent>)KeyEvent.KEY_PRESSED, "Consume"));
    }
}

