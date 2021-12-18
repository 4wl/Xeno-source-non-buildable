/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Application
 *  javafx.beans.InvalidationListener
 *  javafx.beans.Observable
 *  javafx.collections.FXCollections
 *  javafx.geometry.Orientation
 *  javafx.scene.Node
 *  javafx.scene.control.Button
 *  javafx.scene.control.CheckBox
 *  javafx.scene.control.ChoiceBox
 *  javafx.scene.control.Control
 *  javafx.scene.control.ProgressBar
 *  javafx.scene.control.RadioButton
 *  javafx.scene.control.Skin
 *  javafx.scene.control.Slider
 *  javafx.scene.control.TextField
 *  javafx.scene.layout.BorderPane
 *  javafx.scene.layout.Region
 */
package com.sun.javafx.webkit.theme;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import com.sun.javafx.webkit.Accessor;
import com.sun.javafx.webkit.theme.Renderer;
import com.sun.webkit.LoadListenerClient;
import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.RenderTheme;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCSize;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Skin;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

public final class RenderThemeImpl
extends RenderTheme {
    private static final Logger log = Logger.getLogger(RenderThemeImpl.class.getName());
    private Accessor accessor;
    private boolean isDefault;
    private Pool<FormControl> pool;

    public RenderThemeImpl(Accessor accessor) {
        this.accessor = accessor;
        this.pool = new Pool<FormControl>(formControl -> accessor.removeChild((Node)formControl.asControl()), FormControl.class);
        accessor.addViewListener(new ViewListener(this.pool, accessor));
    }

    public RenderThemeImpl() {
        this.isDefault = true;
    }

    private void ensureNotDefault() {
        if (this.isDefault) {
            throw new IllegalStateException("the method should not be called in this context");
        }
    }

    @Override
    protected Ref createWidget(long l, int n, int n2, int n3, int n4, int n5, ByteBuffer byteBuffer) {
        this.ensureNotDefault();
        FormControl formControl = this.pool.get(l);
        WidgetType widgetType = WidgetType.convert(n);
        if (formControl == null || formControl.getType() != widgetType) {
            if (formControl != null) {
                this.accessor.removeChild((Node)formControl.asControl());
            }
            switch (widgetType) {
                case TEXTFIELD: {
                    formControl = new FormTextField();
                    break;
                }
                case BUTTON: {
                    formControl = new FormButton();
                    break;
                }
                case CHECKBOX: {
                    formControl = new FormCheckBox();
                    break;
                }
                case RADIOBUTTON: {
                    formControl = new FormRadioButton();
                    break;
                }
                case MENULIST: {
                    formControl = new FormMenuList();
                    break;
                }
                case MENULISTBUTTON: {
                    formControl = new FormMenuListButton();
                    break;
                }
                case SLIDER: {
                    formControl = new FormSlider();
                    break;
                }
                case PROGRESSBAR: {
                    formControl = new FormProgressBar(WidgetType.PROGRESSBAR);
                    break;
                }
                case METER: {
                    formControl = new FormProgressBar(WidgetType.METER);
                    break;
                }
                default: {
                    log.log(Level.ALL, "unknown widget index: {0}", n);
                    return null;
                }
            }
            formControl.asControl().setFocusTraversable(false);
            this.pool.put(l, formControl, this.accessor.getPage().getUpdateContentCycleID());
            this.accessor.addChild((Node)formControl.asControl());
        }
        formControl.setState(n2);
        Control control = formControl.asControl();
        if (control.getWidth() != (double)n3 || control.getHeight() != (double)n4) {
            control.resize((double)n3, (double)n4);
        }
        if (control.isManaged()) {
            control.setManaged(false);
        }
        if (widgetType == WidgetType.SLIDER) {
            Slider slider = (Slider)control;
            byteBuffer.order(ByteOrder.nativeOrder());
            slider.setOrientation(byteBuffer.getInt() == 0 ? Orientation.HORIZONTAL : Orientation.VERTICAL);
            slider.setMax((double)byteBuffer.getFloat());
            slider.setMin((double)byteBuffer.getFloat());
            slider.setValue((double)byteBuffer.getFloat());
        } else if (widgetType == WidgetType.PROGRESSBAR) {
            ProgressBar progressBar = (ProgressBar)control;
            byteBuffer.order(ByteOrder.nativeOrder());
            progressBar.setProgress(byteBuffer.getInt() == 1 ? (double)byteBuffer.getFloat() : -1.0);
        } else if (widgetType == WidgetType.METER) {
            ProgressBar progressBar = (ProgressBar)control;
            byteBuffer.order(ByteOrder.nativeOrder());
            progressBar.setProgress((double)byteBuffer.getFloat());
            progressBar.setStyle(this.getMeterStyle(byteBuffer.getInt()));
        }
        return new FormControlRef(formControl);
    }

    private String getMeterStyle(int n) {
        switch (n) {
            case 1: {
                return "-fx-accent: yellow";
            }
            case 2: {
                return "-fx-accent: red";
            }
        }
        return "-fx-accent: green";
    }

    @Override
    public void drawWidget(WCGraphicsContext wCGraphicsContext, Ref ref, int n, int n2) {
        Control control;
        this.ensureNotDefault();
        FormControl formControl = ((FormControlRef)ref).asFormControl();
        if (formControl != null && (control = formControl.asControl()) != null) {
            wCGraphicsContext.saveState();
            wCGraphicsContext.translate(n, n2);
            Renderer.getRenderer().render(control, wCGraphicsContext);
            wCGraphicsContext.restoreState();
        }
    }

    @Override
    public WCSize getWidgetSize(Ref ref) {
        this.ensureNotDefault();
        FormControl formControl = ((FormControlRef)ref).asFormControl();
        if (formControl != null) {
            Control control = formControl.asControl();
            return new WCSize((float)control.getWidth(), (float)control.getHeight());
        }
        return new WCSize(0.0f, 0.0f);
    }

    @Override
    protected int getRadioButtonSize() {
        String string = Application.getUserAgentStylesheet();
        if ("MODENA".equalsIgnoreCase(string)) {
            return 20;
        }
        if ("CASPIAN".equalsIgnoreCase(string)) {
            return 19;
        }
        return 20;
    }

    @Override
    protected int getSelectionColor(int n) {
        switch (n) {
            case 0: {
                return -16739329;
            }
            case 1: {
                return -1;
            }
        }
        return 0;
    }

    private static boolean hasState(int n, int n2) {
        return (n & n2) != 0;
    }

    private static final class FormMenuListButton
    extends Button
    implements FormControl {
        private static final int MAX_WIDTH = 20;
        private static final int MIN_WIDTH = 16;

        @Override
        public Control asControl() {
            return this;
        }

        @Override
        public void setState(int n) {
            this.setDisabled(!RenderThemeImpl.hasState(n, 4));
            this.setHover(RenderThemeImpl.hasState(n, 32));
            this.setPressed(RenderThemeImpl.hasState(n, 16));
            if (this.isPressed()) {
                this.arm();
            } else {
                this.disarm();
            }
        }

        private FormMenuListButton() {
            this.setSkin((javafx.scene.control.Skin)new Skin());
            this.setFocusTraversable(false);
            this.getStyleClass().add((Object)"form-select-button");
        }

        public void resize(double d, double d2) {
            d = d2 > 20.0 ? 20.0 : (d2 < 16.0 ? 16.0 : d2);
            super.resize(d, d2);
            this.setTranslateX(-d);
        }

        @Override
        public WidgetType getType() {
            return WidgetType.MENULISTBUTTON;
        }

        private final class Skin
        extends BehaviorSkinBase {
            Skin() {
                super(FormMenuListButton.this, new BehaviorBase<FormMenuListButton>(FormMenuListButton.this, Collections.EMPTY_LIST));
                Region region = new Region();
                region.getStyleClass().add((Object)"arrow");
                region.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
                BorderPane borderPane = new BorderPane();
                borderPane.setCenter((Node)region);
                this.getChildren().add((Object)borderPane);
            }
        }
    }

    private static final class FormMenuList
    extends ChoiceBox
    implements FormControl {
        private FormMenuList() {
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add("");
            this.setItems(FXCollections.observableList(arrayList));
        }

        @Override
        public Control asControl() {
            return this;
        }

        @Override
        public void setState(int n) {
            this.setDisabled(!RenderThemeImpl.hasState(n, 4));
            this.setFocused(RenderThemeImpl.hasState(n, 8));
            this.setHover(RenderThemeImpl.hasState(n, 32) && !this.isDisabled());
        }

        @Override
        public WidgetType getType() {
            return WidgetType.MENULIST;
        }
    }

    private static final class FormProgressBar
    extends ProgressBar
    implements FormControl {
        private final WidgetType type;

        private FormProgressBar(WidgetType widgetType) {
            this.type = widgetType;
        }

        @Override
        public Control asControl() {
            return this;
        }

        @Override
        public void setState(int n) {
            this.setDisabled(!RenderThemeImpl.hasState(n, 4));
            this.setFocused(RenderThemeImpl.hasState(n, 8));
            this.setHover(RenderThemeImpl.hasState(n, 32) && !this.isDisabled());
        }

        @Override
        public WidgetType getType() {
            return this.type;
        }
    }

    private static final class FormSlider
    extends Slider
    implements FormControl {
        private FormSlider() {
        }

        @Override
        public Control asControl() {
            return this;
        }

        @Override
        public void setState(int n) {
            this.setDisabled(!RenderThemeImpl.hasState(n, 4));
            this.setFocused(RenderThemeImpl.hasState(n, 8));
            this.setHover(RenderThemeImpl.hasState(n, 32) && !this.isDisabled());
        }

        @Override
        public WidgetType getType() {
            return WidgetType.SLIDER;
        }
    }

    private static final class FormRadioButton
    extends RadioButton
    implements FormControl {
        private FormRadioButton() {
        }

        @Override
        public Control asControl() {
            return this;
        }

        @Override
        public void setState(int n) {
            this.setDisabled(!RenderThemeImpl.hasState(n, 4));
            this.setFocused(RenderThemeImpl.hasState(n, 8));
            this.setHover(RenderThemeImpl.hasState(n, 32) && !this.isDisabled());
            this.setSelected(RenderThemeImpl.hasState(n, 1));
        }

        @Override
        public WidgetType getType() {
            return WidgetType.RADIOBUTTON;
        }
    }

    private static final class FormCheckBox
    extends CheckBox
    implements FormControl {
        private FormCheckBox() {
        }

        @Override
        public Control asControl() {
            return this;
        }

        @Override
        public void setState(int n) {
            this.setDisabled(!RenderThemeImpl.hasState(n, 4));
            this.setFocused(RenderThemeImpl.hasState(n, 8));
            this.setHover(RenderThemeImpl.hasState(n, 32) && !this.isDisabled());
            this.setSelected(RenderThemeImpl.hasState(n, 1));
        }

        @Override
        public WidgetType getType() {
            return WidgetType.CHECKBOX;
        }
    }

    private static final class FormTextField
    extends TextField
    implements FormControl {
        private FormTextField() {
            this.setStyle("-fx-display-caret: false");
        }

        @Override
        public Control asControl() {
            return this;
        }

        @Override
        public void setState(int n) {
            this.setDisabled(!RenderThemeImpl.hasState(n, 4));
            this.setEditable(RenderThemeImpl.hasState(n, 64));
            this.setFocused(RenderThemeImpl.hasState(n, 8));
            this.setHover(RenderThemeImpl.hasState(n, 32) && !this.isDisabled());
        }

        @Override
        public WidgetType getType() {
            return WidgetType.TEXTFIELD;
        }
    }

    private static final class FormButton
    extends Button
    implements FormControl {
        private FormButton() {
        }

        @Override
        public Control asControl() {
            return this;
        }

        @Override
        public void setState(int n) {
            this.setDisabled(!RenderThemeImpl.hasState(n, 4));
            this.setFocused(RenderThemeImpl.hasState(n, 8));
            this.setHover(RenderThemeImpl.hasState(n, 32) && !this.isDisabled());
            this.setPressed(RenderThemeImpl.hasState(n, 16));
            if (this.isPressed()) {
                this.arm();
            } else {
                this.disarm();
            }
        }

        @Override
        public WidgetType getType() {
            return WidgetType.BUTTON;
        }
    }

    private static interface FormControl
    extends Widget {
        public Control asControl();

        public void setState(int var1);
    }

    static interface Widget {
        public WidgetType getType();
    }

    private static final class FormControlRef
    extends Ref {
        private final WeakReference<FormControl> fcRef;

        private FormControlRef(FormControl formControl) {
            this.fcRef = new WeakReference<FormControl>(formControl);
        }

        private FormControl asFormControl() {
            return (FormControl)this.fcRef.get();
        }
    }

    static class ViewListener
    implements InvalidationListener {
        private final Pool pool;
        private final Accessor accessor;
        private LoadListenerClient loadListener;

        ViewListener(Pool pool, Accessor accessor) {
            this.pool = pool;
            this.accessor = accessor;
        }

        public void invalidated(Observable observable) {
            this.pool.clear();
            if (this.accessor.getPage() != null && this.loadListener == null) {
                this.loadListener = new LoadListenerClient(){

                    @Override
                    public void dispatchLoadEvent(long l, int n, String string, String string2, double d, int n2) {
                        if (n == 0) {
                            pool.clear();
                        }
                    }

                    @Override
                    public void dispatchResourceLoadEvent(long l, int n, String string, String string2, double d, int n2) {
                    }
                };
                this.accessor.getPage().addLoadListenerClient(this.loadListener);
            }
        }
    }

    static final class Pool<T extends Widget> {
        private static final int INITIAL_CAPACITY = 100;
        private int capacity = 100;
        private final LinkedHashMap<Long, Integer> ids = new LinkedHashMap();
        private final Map<Long, WeakReference<T>> pool = new HashMap<Long, WeakReference<T>>();
        private final Notifier<T> notifier;
        private final String type;

        Pool(Notifier<T> notifier, Class<T> class_) {
            this.notifier = notifier;
            this.type = class_.getSimpleName();
        }

        T get(long l) {
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "type: {0}, size: {1}, id: 0x{2}", new Object[]{this.type, this.pool.size(), Long.toHexString(l)});
            }
            assert (this.ids.size() == this.pool.size());
            WeakReference<T> weakReference = this.pool.get(l);
            if (weakReference == null) {
                return null;
            }
            Widget widget = (Widget)weakReference.get();
            if (widget == null) {
                return null;
            }
            Integer n = (Integer)this.ids.remove(l);
            this.ids.put(l, n);
            return (T)widget;
        }

        void put(long l, T t, int n) {
            if (log.isLoggable(Level.FINEST)) {
                log.log(Level.FINEST, "size: {0}, id: 0x{1}, control: {2}", new Object[]{this.pool.size(), Long.toHexString(l), t.getType()});
            }
            if (this.ids.size() >= this.capacity) {
                Long l2 = this.ids.keySet().iterator().next();
                Integer n2 = this.ids.get(l2);
                if (n2 != n) {
                    this.ids.remove(l2);
                    Widget widget = (Widget)this.pool.remove(l2).get();
                    if (widget != null) {
                        this.notifier.notifyRemoved(widget);
                    }
                } else {
                    this.capacity = Math.min(this.capacity, (int)Math.ceil(1.073741823E9)) * 2;
                }
            }
            this.ids.put(l, n);
            this.pool.put(l, new WeakReference<T>(t));
        }

        void clear() {
            if (log.isLoggable(Level.FINE)) {
                log.fine("size: " + this.pool.size() + ", controls: " + this.pool.values());
            }
            if (this.pool.size() == 0) {
                return;
            }
            this.ids.clear();
            for (WeakReference<T> weakReference : this.pool.values()) {
                Widget widget = (Widget)weakReference.get();
                if (widget == null) continue;
                this.notifier.notifyRemoved(widget);
            }
            this.pool.clear();
            this.capacity = 100;
        }

        static interface Notifier<T> {
            public void notifyRemoved(T var1);
        }
    }

    static enum WidgetType {
        TEXTFIELD(0),
        BUTTON(1),
        CHECKBOX(2),
        RADIOBUTTON(3),
        MENULIST(4),
        MENULISTBUTTON(5),
        SLIDER(6),
        PROGRESSBAR(7),
        METER(8),
        SCROLLBAR(9);

        private static final HashMap<Integer, WidgetType> map;
        private final int value;

        private WidgetType(int n2) {
            this.value = n2;
        }

        private static WidgetType convert(int n) {
            return map.get(n);
        }

        static {
            map = new HashMap();
            for (WidgetType widgetType : WidgetType.values()) {
                map.put(widgetType.value, widgetType);
            }
        }
    }
}

