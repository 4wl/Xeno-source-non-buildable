/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.WeakChangeListener
 *  javafx.event.ActionEvent
 *  javafx.event.Event
 *  javafx.event.EventHandler
 *  javafx.geometry.Bounds
 *  javafx.geometry.Point2D
 *  javafx.geometry.Rectangle2D
 *  javafx.scene.Node
 *  javafx.scene.Scene
 *  javafx.scene.control.ComboBox
 *  javafx.scene.control.ContextMenu
 *  javafx.scene.control.PasswordField
 *  javafx.scene.control.TextField
 *  javafx.scene.input.ContextMenuEvent
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseEvent
 *  javafx.stage.Screen
 *  javafx.stage.Window
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.control.behavior.TextInputControlBehavior;
import com.sun.javafx.scene.control.behavior.TwoLevelFocusBehavior;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.util.Utils;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Window;

public class TextFieldBehavior
extends TextInputControlBehavior<TextField> {
    private TextFieldSkin skin;
    private ContextMenu contextMenu = new ContextMenu();
    private TwoLevelFocusBehavior tlFocus;
    private ChangeListener<Scene> sceneListener;
    private ChangeListener<Node> focusOwnerListener;
    private boolean focusGainedByMouseClick = false;
    private boolean shiftDown = false;
    private boolean deferClick = false;

    public TextFieldBehavior(TextField textField) {
        super(textField, (List<KeyBinding>)TEXT_INPUT_BINDINGS);
        if (IS_TOUCH_SUPPORTED) {
            this.contextMenu.getStyleClass().add((Object)"text-input-context-menu");
        }
        this.handleFocusChange();
        textField.focusedProperty().addListener((observableValue, bl, bl2) -> this.handleFocusChange());
        this.focusOwnerListener = (observableValue, node, node2) -> {
            if (node2 == textField) {
                if (!this.focusGainedByMouseClick) {
                    textField.selectRange(textField.getLength(), 0);
                }
            } else {
                textField.selectRange(0, 0);
            }
        };
        WeakChangeListener weakChangeListener = new WeakChangeListener(this.focusOwnerListener);
        this.sceneListener = (observableValue, scene, scene2) -> {
            if (scene != null) {
                scene.focusOwnerProperty().removeListener((ChangeListener)weakChangeListener);
            }
            if (scene2 != null) {
                scene2.focusOwnerProperty().addListener((ChangeListener)weakChangeListener);
            }
        };
        textField.sceneProperty().addListener((ChangeListener)new WeakChangeListener(this.sceneListener));
        if (textField.getScene() != null) {
            textField.getScene().focusOwnerProperty().addListener((ChangeListener)weakChangeListener);
        }
        if (com.sun.javafx.scene.control.skin.Utils.isTwoLevelFocus()) {
            this.tlFocus = new TwoLevelFocusBehavior((Node)textField);
        }
    }

    @Override
    public void dispose() {
        if (this.tlFocus != null) {
            this.tlFocus.dispose();
        }
        super.dispose();
    }

    private void handleFocusChange() {
        TextField textField = (TextField)this.getControl();
        if (textField.isFocused()) {
            if (PlatformUtil.isIOS()) {
                TextInputTypes textInputTypes = TextInputTypes.TEXT_FIELD;
                if (textField.getClass().equals(PasswordField.class)) {
                    textInputTypes = TextInputTypes.PASSWORD_FIELD;
                } else if (textField.getParent().getClass().equals(ComboBox.class)) {
                    textInputTypes = TextInputTypes.EDITABLE_COMBO;
                }
                Bounds bounds = textField.getBoundsInParent();
                double d = bounds.getWidth();
                double d2 = bounds.getHeight();
                Affine3D affine3D = TextFieldBehavior.calculateNodeToSceneTransform((Node)textField);
                String string = textField.getText();
                textField.getScene().getWindow().impl_getPeer().requestInput(string, textInputTypes.ordinal(), d, d2, affine3D.getMxx(), affine3D.getMxy(), affine3D.getMxz(), affine3D.getMxt(), affine3D.getMyx(), affine3D.getMyy(), affine3D.getMyz(), affine3D.getMyt(), affine3D.getMzx(), affine3D.getMzy(), affine3D.getMzz(), affine3D.getMzt());
            }
            if (!this.focusGainedByMouseClick) {
                this.setCaretAnimating(true);
            }
        } else {
            if (PlatformUtil.isIOS() && textField.getScene() != null) {
                textField.getScene().getWindow().impl_getPeer().releaseInput();
            }
            this.focusGainedByMouseClick = false;
            this.setCaretAnimating(false);
        }
    }

    static Affine3D calculateNodeToSceneTransform(Node node) {
        Affine3D affine3D = new Affine3D();
        do {
            affine3D.preConcatenate(node.impl_getLeafTransform());
        } while ((node = node.getParent()) != null);
        return affine3D;
    }

    public void setTextFieldSkin(TextFieldSkin textFieldSkin) {
        this.skin = textFieldSkin;
    }

    @Override
    protected void fire(KeyEvent keyEvent) {
        TextField textField = (TextField)this.getControl();
        EventHandler eventHandler = textField.getOnAction();
        ActionEvent actionEvent = new ActionEvent((Object)textField, null);
        textField.fireEvent((Event)actionEvent);
        textField.commitValue();
        if (eventHandler == null && !actionEvent.isConsumed()) {
            this.forwardToParent(keyEvent);
        }
    }

    @Override
    protected void cancelEdit(KeyEvent keyEvent) {
        TextField textField = (TextField)this.getControl();
        if (textField.getTextFormatter() != null) {
            textField.cancelEdit();
        } else {
            this.forwardToParent(keyEvent);
        }
    }

    @Override
    protected void deleteChar(boolean bl) {
        this.skin.deleteChar(bl);
    }

    @Override
    protected void replaceText(int n, int n2, String string) {
        this.skin.replaceText(n, n2, string);
    }

    @Override
    protected void deleteFromLineStart() {
        TextField textField = (TextField)this.getControl();
        int n = textField.getCaretPosition();
        if (n > 0) {
            this.replaceText(0, n, "");
        }
    }

    @Override
    protected void setCaretAnimating(boolean bl) {
        if (this.skin != null) {
            this.skin.setCaretAnimating(bl);
        }
    }

    private void beep() {
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        TextField textField = (TextField)this.getControl();
        super.mousePressed(mouseEvent);
        if (!textField.isDisabled()) {
            if (!textField.isFocused()) {
                this.focusGainedByMouseClick = true;
                textField.requestFocus();
            }
            this.setCaretAnimating(false);
            if (mouseEvent.isPrimaryButtonDown() && !mouseEvent.isMiddleButtonDown() && !mouseEvent.isSecondaryButtonDown()) {
                HitInfo hitInfo = this.skin.getIndex(mouseEvent.getX(), mouseEvent.getY());
                String string = textField.textProperty().getValueSafe();
                int n = com.sun.javafx.scene.control.skin.Utils.getHitInsertionIndex(hitInfo, string);
                int n2 = textField.getAnchor();
                int n3 = textField.getCaretPosition();
                if (mouseEvent.getClickCount() < 2 && (IS_TOUCH_SUPPORTED || n2 != n3 && (n > n2 && n < n3 || n < n2 && n > n3))) {
                    this.deferClick = true;
                } else if (!(mouseEvent.isControlDown() || mouseEvent.isAltDown() || mouseEvent.isShiftDown() || mouseEvent.isMetaDown())) {
                    switch (mouseEvent.getClickCount()) {
                        case 1: {
                            this.mouseSingleClick(hitInfo);
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
                } else if (mouseEvent.isShiftDown() && !mouseEvent.isControlDown() && !mouseEvent.isAltDown() && !mouseEvent.isMetaDown() && mouseEvent.getClickCount() == 1) {
                    this.shiftDown = true;
                    if (PlatformUtil.isMac()) {
                        textField.extendSelection(n);
                    } else {
                        this.skin.positionCaret(hitInfo, true);
                    }
                }
                this.skin.setForwardBias(hitInfo.isLeading());
            }
        }
        if (this.contextMenu.isShowing()) {
            this.contextMenu.hide();
        }
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        TextField textField = (TextField)this.getControl();
        if (!(textField.isDisabled() || this.deferClick || !mouseEvent.isPrimaryButtonDown() || mouseEvent.isMiddleButtonDown() || mouseEvent.isSecondaryButtonDown() || mouseEvent.isControlDown() || mouseEvent.isAltDown() || mouseEvent.isShiftDown() || mouseEvent.isMetaDown())) {
            this.skin.positionCaret(this.skin.getIndex(mouseEvent.getX(), mouseEvent.getY()), true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        TextField textField = (TextField)this.getControl();
        super.mouseReleased(mouseEvent);
        if (!textField.isDisabled()) {
            this.setCaretAnimating(false);
            if (this.deferClick) {
                this.deferClick = false;
                this.skin.positionCaret(this.skin.getIndex(mouseEvent.getX(), mouseEvent.getY()), this.shiftDown);
                this.shiftDown = false;
            }
            this.setCaretAnimating(true);
        }
    }

    @Override
    public void contextMenuRequested(ContextMenuEvent contextMenuEvent) {
        TextField textField = (TextField)this.getControl();
        if (this.contextMenu.isShowing()) {
            this.contextMenu.hide();
        } else if (textField.getContextMenu() == null) {
            Screen screen;
            double d = contextMenuEvent.getScreenX();
            double d2 = contextMenuEvent.getScreenY();
            double d3 = contextMenuEvent.getSceneX();
            if (IS_TOUCH_SUPPORTED) {
                Point2D point2D;
                if (textField.getSelection().getLength() == 0) {
                    this.skin.positionCaret(this.skin.getIndex(contextMenuEvent.getX(), contextMenuEvent.getY()), false);
                    point2D = this.skin.getMenuPosition();
                } else {
                    point2D = this.skin.getMenuPosition();
                    if (point2D != null && (point2D.getX() <= 0.0 || point2D.getY() <= 0.0)) {
                        this.skin.positionCaret(this.skin.getIndex(contextMenuEvent.getX(), contextMenuEvent.getY()), false);
                        point2D = this.skin.getMenuPosition();
                    }
                }
                if (point2D != null) {
                    Point2D point2D2 = ((TextField)this.getControl()).localToScene(point2D);
                    Scene scene = ((TextField)this.getControl()).getScene();
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
                ((TextField)this.getControl()).getProperties().put((Object)"CONTEXT_MENU_SCREEN_X", (Object)d);
                ((TextField)this.getControl()).getProperties().put((Object)"CONTEXT_MENU_SCENE_X", (Object)d3);
                this.contextMenu.show(this.getControl(), rectangle2D.getMinX(), d2);
            } else if (d + d4 > rectangle2D.getMaxX()) {
                double d6 = d4 - (rectangle2D.getMaxX() - d);
                ((TextField)this.getControl()).getProperties().put((Object)"CONTEXT_MENU_SCREEN_X", (Object)d);
                ((TextField)this.getControl()).getProperties().put((Object)"CONTEXT_MENU_SCENE_X", (Object)d3);
                this.contextMenu.show(this.getControl(), d - d6, d2);
            } else {
                ((TextField)this.getControl()).getProperties().put((Object)"CONTEXT_MENU_SCREEN_X", (Object)0);
                ((TextField)this.getControl()).getProperties().put((Object)"CONTEXT_MENU_SCENE_X", (Object)0);
                this.contextMenu.show(this.getControl(), d5, d2);
            }
        }
        contextMenuEvent.consume();
    }

    protected void mouseSingleClick(HitInfo hitInfo) {
        this.skin.positionCaret(hitInfo, false);
    }

    protected void mouseDoubleClick(HitInfo hitInfo) {
        TextField textField = (TextField)this.getControl();
        textField.previousWord();
        if (PlatformUtil.isWindows()) {
            textField.selectNextWord();
        } else {
            textField.selectEndOfNextWord();
        }
    }

    protected void mouseTripleClick(HitInfo hitInfo) {
        ((TextField)this.getControl()).selectAll();
    }

    static enum TextInputTypes {
        TEXT_FIELD,
        PASSWORD_FIELD,
        EDITABLE_COMBO,
        TEXT_AREA;

    }
}

