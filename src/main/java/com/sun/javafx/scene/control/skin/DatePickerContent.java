/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Platform
 *  javafx.beans.property.ObjectProperty
 *  javafx.beans.property.SimpleObjectProperty
 *  javafx.beans.value.ChangeListener
 *  javafx.beans.value.ObservableValue
 *  javafx.beans.value.WeakChangeListener
 *  javafx.event.EventHandler
 *  javafx.scene.Node
 *  javafx.scene.Scene
 *  javafx.scene.control.Button
 *  javafx.scene.control.DateCell
 *  javafx.scene.control.DatePicker
 *  javafx.scene.control.Label
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseButton
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.layout.BorderPane
 *  javafx.scene.layout.ColumnConstraints
 *  javafx.scene.layout.GridPane
 *  javafx.scene.layout.HBox
 *  javafx.scene.layout.StackPane
 *  javafx.scene.layout.VBox
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.skin.Utils;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import com.sun.javafx.scene.traversal.Direction;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DatePickerContent
extends VBox {
    protected DatePicker datePicker;
    private Button backMonthButton;
    private Button forwardMonthButton;
    private Button backYearButton;
    private Button forwardYearButton;
    private Label monthLabel;
    private Label yearLabel;
    protected GridPane gridPane;
    private int daysPerWeek;
    private List<DateCell> dayNameCells = new ArrayList<DateCell>();
    private List<DateCell> weekNumberCells = new ArrayList<DateCell>();
    protected List<DateCell> dayCells = new ArrayList<DateCell>();
    private LocalDate[] dayCellDates;
    private DateCell lastFocusedDayCell = null;
    final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM");
    final DateTimeFormatter monthFormatterSO = DateTimeFormatter.ofPattern("LLLL");
    final DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("y");
    final DateTimeFormatter yearWithEraFormatter = DateTimeFormatter.ofPattern("GGGGy");
    final DateTimeFormatter weekNumberFormatter = DateTimeFormatter.ofPattern("w");
    final DateTimeFormatter weekDayNameFormatter = DateTimeFormatter.ofPattern("ccc");
    final DateTimeFormatter dayCellFormatter = DateTimeFormatter.ofPattern("d");
    private ObjectProperty<YearMonth> displayedYearMonth = new SimpleObjectProperty((Object)this, "displayedYearMonth");

    static String getString(String string) {
        return ControlResources.getString("DatePicker." + string);
    }

    DatePickerContent(final DatePicker datePicker) {
        DateCell dateCell;
        int n;
        this.datePicker = datePicker;
        this.getStyleClass().add((Object)"date-picker-popup");
        this.daysPerWeek = this.getDaysPerWeek();
        LocalDate localDate = (LocalDate)datePicker.getValue();
        this.displayedYearMonth.set((Object)(localDate != null ? YearMonth.from(localDate) : YearMonth.now()));
        this.displayedYearMonth.addListener((observableValue, yearMonth, yearMonth2) -> this.updateValues());
        this.getChildren().add((Object)this.createMonthYearPane());
        this.gridPane = new GridPane(){

            protected double computePrefWidth(double d) {
                double d2 = super.computePrefWidth(d);
                int n = DatePickerContent.this.daysPerWeek + (datePicker.isShowWeekNumbers() ? 1 : 0);
                double d3 = this.snapSpace(this.getHgap());
                double d4 = this.snapSpace(this.getInsets().getLeft());
                double d5 = this.snapSpace(this.getInsets().getRight());
                double d6 = d3 * (double)(n - 1);
                double d7 = d2 - d4 - d5 - d6;
                return this.snapSize(d7 / (double)n) * (double)n + d4 + d5 + d6;
            }

            protected void layoutChildren() {
                if (this.getWidth() > 0.0 && this.getHeight() > 0.0) {
                    super.layoutChildren();
                }
            }
        };
        this.gridPane.setFocusTraversable(true);
        this.gridPane.getStyleClass().add((Object)"calendar-grid");
        this.gridPane.setVgap(-1.0);
        this.gridPane.setHgap(-1.0);
        localDate = new WeakChangeListener((observableValue, node, node2) -> {
            if (node2 == this.gridPane) {
                if (node instanceof DateCell) {
                    this.gridPane.impl_traverse(Direction.PREVIOUS);
                } else if (this.lastFocusedDayCell != null) {
                    Platform.runLater(() -> this.lastFocusedDayCell.requestFocus());
                } else {
                    this.clearFocus();
                }
            }
        });
        this.gridPane.sceneProperty().addListener((ChangeListener)new WeakChangeListener((arg_0, arg_1, arg_2) -> DatePickerContent.lambda$new$168((WeakChangeListener)localDate, arg_0, arg_1, arg_2)));
        if (this.gridPane.getScene() != null) {
            this.gridPane.getScene().focusOwnerProperty().addListener((ChangeListener)localDate);
        }
        for (n = 0; n < this.daysPerWeek; ++n) {
            dateCell = new DateCell();
            dateCell.getStyleClass().add((Object)"day-name-cell");
            this.dayNameCells.add(dateCell);
        }
        for (n = 0; n < 6; ++n) {
            dateCell = new DateCell();
            dateCell.getStyleClass().add((Object)"week-number-cell");
            this.weekNumberCells.add(dateCell);
        }
        this.createDayCells();
        this.updateGrid();
        this.getChildren().add((Object)this.gridPane);
        this.refresh();
        this.addEventHandler(KeyEvent.ANY, keyEvent -> {
            Node node = this.getScene().getFocusOwner();
            if (node instanceof DateCell) {
                this.lastFocusedDayCell = (DateCell)node;
            }
            if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                switch (keyEvent.getCode()) {
                    case HOME: {
                        this.goToDate(LocalDate.now(), true);
                        keyEvent.consume();
                        break;
                    }
                    case PAGE_UP: {
                        if (PlatformUtil.isMac() && keyEvent.isMetaDown() || !PlatformUtil.isMac() && keyEvent.isControlDown()) {
                            if (!this.backYearButton.isDisabled()) {
                                this.forward(-1, ChronoUnit.YEARS, true);
                            }
                        } else if (!this.backMonthButton.isDisabled()) {
                            this.forward(-1, ChronoUnit.MONTHS, true);
                        }
                        keyEvent.consume();
                        break;
                    }
                    case PAGE_DOWN: {
                        if (PlatformUtil.isMac() && keyEvent.isMetaDown() || !PlatformUtil.isMac() && keyEvent.isControlDown()) {
                            if (!this.forwardYearButton.isDisabled()) {
                                this.forward(1, ChronoUnit.YEARS, true);
                            }
                        } else if (!this.forwardMonthButton.isDisabled()) {
                            this.forward(1, ChronoUnit.MONTHS, true);
                        }
                        keyEvent.consume();
                    }
                }
                node = this.getScene().getFocusOwner();
                if (node instanceof DateCell) {
                    this.lastFocusedDayCell = (DateCell)node;
                }
            }
            switch (keyEvent.getCode()) {
                case F4: 
                case F10: 
                case UP: 
                case DOWN: 
                case LEFT: 
                case RIGHT: 
                case TAB: {
                    break;
                }
                case ESCAPE: {
                    datePicker.hide();
                    keyEvent.consume();
                    break;
                }
                default: {
                    keyEvent.consume();
                }
            }
        });
    }

    ObjectProperty<YearMonth> displayedYearMonthProperty() {
        return this.displayedYearMonth;
    }

    protected BorderPane createMonthYearPane() {
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add((Object)"month-year-pane");
        HBox hBox = new HBox();
        hBox.getStyleClass().add((Object)"spinner");
        this.backMonthButton = new Button();
        this.backMonthButton.getStyleClass().add((Object)"left-button");
        this.forwardMonthButton = new Button();
        this.forwardMonthButton.getStyleClass().add((Object)"right-button");
        StackPane stackPane = new StackPane();
        stackPane.getStyleClass().add((Object)"left-arrow");
        stackPane.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        this.backMonthButton.setGraphic((Node)stackPane);
        StackPane stackPane2 = new StackPane();
        stackPane2.getStyleClass().add((Object)"right-arrow");
        stackPane2.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        this.forwardMonthButton.setGraphic((Node)stackPane2);
        this.backMonthButton.setOnAction(actionEvent -> this.forward(-1, ChronoUnit.MONTHS, false));
        this.monthLabel = new Label();
        this.monthLabel.getStyleClass().add((Object)"spinner-label");
        this.forwardMonthButton.setOnAction(actionEvent -> this.forward(1, ChronoUnit.MONTHS, false));
        hBox.getChildren().addAll((Object[])new Node[]{this.backMonthButton, this.monthLabel, this.forwardMonthButton});
        borderPane.setLeft((Node)hBox);
        HBox hBox2 = new HBox();
        hBox2.getStyleClass().add((Object)"spinner");
        this.backYearButton = new Button();
        this.backYearButton.getStyleClass().add((Object)"left-button");
        this.forwardYearButton = new Button();
        this.forwardYearButton.getStyleClass().add((Object)"right-button");
        StackPane stackPane3 = new StackPane();
        stackPane3.getStyleClass().add((Object)"left-arrow");
        stackPane3.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        this.backYearButton.setGraphic((Node)stackPane3);
        StackPane stackPane4 = new StackPane();
        stackPane4.getStyleClass().add((Object)"right-arrow");
        stackPane4.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        this.forwardYearButton.setGraphic((Node)stackPane4);
        this.backYearButton.setOnAction(actionEvent -> this.forward(-1, ChronoUnit.YEARS, false));
        this.yearLabel = new Label();
        this.yearLabel.getStyleClass().add((Object)"spinner-label");
        this.forwardYearButton.setOnAction(actionEvent -> this.forward(1, ChronoUnit.YEARS, false));
        hBox2.getChildren().addAll((Object[])new Node[]{this.backYearButton, this.yearLabel, this.forwardYearButton});
        hBox2.setFillHeight(false);
        borderPane.setRight((Node)hBox2);
        return borderPane;
    }

    private void refresh() {
        this.updateMonthLabelWidth();
        this.updateDayNameCells();
        this.updateValues();
    }

    void updateValues() {
        this.updateWeeknumberDateCells();
        this.updateDayCells();
        this.updateMonthYearPane();
    }

    void updateGrid() {
        int n;
        this.gridPane.getColumnConstraints().clear();
        this.gridPane.getChildren().clear();
        int n2 = this.daysPerWeek + (this.datePicker.isShowWeekNumbers() ? 1 : 0);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100.0);
        for (n = 0; n < n2; ++n) {
            this.gridPane.getColumnConstraints().add((Object)columnConstraints);
        }
        for (n = 0; n < this.daysPerWeek; ++n) {
            this.gridPane.add((Node)this.dayNameCells.get(n), n + n2 - this.daysPerWeek, 1);
        }
        if (this.datePicker.isShowWeekNumbers()) {
            for (n = 0; n < 6; ++n) {
                this.gridPane.add((Node)this.weekNumberCells.get(n), 0, n + 2);
            }
        }
        for (n = 0; n < 6; ++n) {
            for (int i = 0; i < this.daysPerWeek; ++i) {
                this.gridPane.add((Node)this.dayCells.get(n * this.daysPerWeek + i), i + n2 - this.daysPerWeek, n + 2);
            }
        }
    }

    void updateDayNameCells() {
        int n = WeekFields.of(this.getLocale()).getFirstDayOfWeek().getValue();
        LocalDate localDate = LocalDate.of(2009, 7, 12 + n);
        for (int i = 0; i < this.daysPerWeek; ++i) {
            String string = this.weekDayNameFormatter.withLocale(this.getLocale()).format(localDate.plus(i, ChronoUnit.DAYS));
            this.dayNameCells.get(i).setText(this.titleCaseWord(string));
        }
    }

    void updateWeeknumberDateCells() {
        if (this.datePicker.isShowWeekNumbers()) {
            Locale locale = this.getLocale();
            LocalDate localDate = ((YearMonth)this.displayedYearMonth.get()).atDay(1);
            for (int i = 0; i < 6; ++i) {
                LocalDate localDate2 = localDate.plus(i, ChronoUnit.WEEKS);
                String string = this.weekNumberFormatter.withLocale(locale).withDecimalStyle(DecimalStyle.of(locale)).format(localDate2);
                this.weekNumberCells.get(i).setText(string);
            }
        }
    }

    void updateDayCells() {
        Locale locale = this.getLocale();
        Chronology chronology = this.getPrimaryChronology();
        int n = this.determineFirstOfMonthDayOfWeek();
        YearMonth yearMonth = (YearMonth)this.displayedYearMonth.get();
        YearMonth yearMonth2 = null;
        YearMonth yearMonth3 = null;
        int n2 = -1;
        int n3 = -1;
        int n4 = -1;
        for (int i = 0; i < 6 * this.daysPerWeek; ++i) {
            DateCell dateCell = this.dayCells.get(i);
            dateCell.getStyleClass().setAll((Object[])new String[]{"cell", "date-cell", "day-cell"});
            dateCell.setDisable(false);
            dateCell.setStyle(null);
            dateCell.setGraphic(null);
            dateCell.setTooltip(null);
            try {
                LocalDate localDate;
                if (n2 == -1) {
                    n2 = yearMonth.lengthOfMonth();
                }
                YearMonth yearMonth4 = yearMonth;
                int n5 = i - n + 1;
                if (i < n) {
                    if (yearMonth2 == null) {
                        yearMonth2 = yearMonth.minusMonths(1L);
                        n3 = yearMonth2.lengthOfMonth();
                    }
                    yearMonth4 = yearMonth2;
                    n5 = i + n3 - n + 1;
                    dateCell.getStyleClass().add((Object)"previous-month");
                } else if (i >= n + n2) {
                    if (yearMonth3 == null) {
                        yearMonth3 = yearMonth.plusMonths(1L);
                        n4 = yearMonth3.lengthOfMonth();
                    }
                    yearMonth4 = yearMonth3;
                    n5 = i - n2 - n + 1;
                    dateCell.getStyleClass().add((Object)"next-month");
                }
                this.dayCellDates[i] = localDate = yearMonth4.atDay(n5);
                ChronoLocalDate chronoLocalDate = chronology.date(localDate);
                dateCell.setDisable(false);
                if (this.isToday(localDate)) {
                    dateCell.getStyleClass().add((Object)"today");
                }
                if (localDate.equals(this.datePicker.getValue())) {
                    dateCell.getStyleClass().add((Object)"selected");
                }
                String string = this.dayCellFormatter.withLocale(locale).withChronology(chronology).withDecimalStyle(DecimalStyle.of(locale)).format(chronoLocalDate);
                dateCell.setText(string);
                dateCell.updateItem(localDate, false);
                continue;
            }
            catch (DateTimeException dateTimeException) {
                dateCell.setText(" ");
                dateCell.setDisable(true);
            }
        }
    }

    private int getDaysPerWeek() {
        ValueRange valueRange = this.getPrimaryChronology().range(ChronoField.DAY_OF_WEEK);
        return (int)(valueRange.getMaximum() - valueRange.getMinimum() + 1L);
    }

    private int getMonthsPerYear() {
        ValueRange valueRange = this.getPrimaryChronology().range(ChronoField.MONTH_OF_YEAR);
        return (int)(valueRange.getMaximum() - valueRange.getMinimum() + 1L);
    }

    private void updateMonthLabelWidth() {
        if (this.monthLabel != null) {
            int n = this.getMonthsPerYear();
            double d = 0.0;
            for (int i = 0; i < n; ++i) {
                YearMonth yearMonth = ((YearMonth)this.displayedYearMonth.get()).withMonth(i + 1);
                String string = this.monthFormatterSO.withLocale(this.getLocale()).format(yearMonth);
                if (Character.isDigit(string.charAt(0))) {
                    string = this.monthFormatter.withLocale(this.getLocale()).format(yearMonth);
                }
                d = Math.max(d, Utils.computeTextWidth(this.monthLabel.getFont(), string, 0.0));
            }
            this.monthLabel.setMinWidth(d);
        }
    }

    protected void updateMonthYearPane() {
        LocalDate localDate;
        Chronology chronology;
        YearMonth yearMonth = (YearMonth)this.displayedYearMonth.get();
        String string = this.formatMonth(yearMonth);
        this.monthLabel.setText(string);
        string = this.formatYear(yearMonth);
        this.yearLabel.setText(string);
        double d = Utils.computeTextWidth(this.yearLabel.getFont(), string, 0.0);
        if (d > this.yearLabel.getMinWidth()) {
            this.yearLabel.setMinWidth(d);
        }
        this.backMonthButton.setDisable(!this.isValidDate(chronology = this.datePicker.getChronology(), localDate = yearMonth.atDay(1), -1, ChronoUnit.DAYS));
        this.forwardMonthButton.setDisable(!this.isValidDate(chronology, localDate, 1, ChronoUnit.MONTHS));
        this.backYearButton.setDisable(!this.isValidDate(chronology, localDate, -1, ChronoUnit.YEARS));
        this.forwardYearButton.setDisable(!this.isValidDate(chronology, localDate, 1, ChronoUnit.YEARS));
    }

    private String formatMonth(YearMonth yearMonth) {
        Locale locale = this.getLocale();
        Chronology chronology = this.getPrimaryChronology();
        try {
            ChronoLocalDate chronoLocalDate = chronology.date(yearMonth.atDay(1));
            String string = this.monthFormatterSO.withLocale(this.getLocale()).withChronology(chronology).format(chronoLocalDate);
            if (Character.isDigit(string.charAt(0))) {
                string = this.monthFormatter.withLocale(this.getLocale()).withChronology(chronology).format(chronoLocalDate);
            }
            return this.titleCaseWord(string);
        }
        catch (DateTimeException dateTimeException) {
            return "";
        }
    }

    private String formatYear(YearMonth yearMonth) {
        Locale locale = this.getLocale();
        Chronology chronology = this.getPrimaryChronology();
        try {
            DateTimeFormatter dateTimeFormatter = this.yearFormatter;
            ChronoLocalDate chronoLocalDate = chronology.date(yearMonth.atDay(1));
            int n = chronoLocalDate.getEra().getValue();
            int n2 = chronology.eras().size();
            if (n2 == 2 && n == 0 || n2 > 2) {
                dateTimeFormatter = this.yearWithEraFormatter;
            }
            String string = dateTimeFormatter.withLocale(this.getLocale()).withChronology(chronology).withDecimalStyle(DecimalStyle.of(this.getLocale())).format(chronoLocalDate);
            return string;
        }
        catch (DateTimeException dateTimeException) {
            return "";
        }
    }

    private String titleCaseWord(String string) {
        int n;
        if (string.length() > 0 && !Character.isTitleCase(n = string.codePointAt(0))) {
            string = new String(new int[]{Character.toTitleCase(n)}, 0, 1) + string.substring(Character.offsetByCodePoints(string, 0, 1));
        }
        return string;
    }

    private int determineFirstOfMonthDayOfWeek() {
        int n = WeekFields.of(this.getLocale()).getFirstDayOfWeek().getValue();
        int n2 = ((YearMonth)this.displayedYearMonth.get()).atDay(1).getDayOfWeek().getValue() - n;
        if (n2 < 0) {
            n2 += this.daysPerWeek;
        }
        return n2;
    }

    private boolean isToday(LocalDate localDate) {
        return localDate.equals(LocalDate.now());
    }

    protected LocalDate dayCellDate(DateCell dateCell) {
        assert (this.dayCellDates != null);
        return this.dayCellDates[this.dayCells.indexOf((Object)dateCell)];
    }

    public void goToDayCell(DateCell dateCell, int n, ChronoUnit chronoUnit, boolean bl) {
        this.goToDate(this.dayCellDate(dateCell).plus(n, chronoUnit), bl);
    }

    protected void forward(int n, ChronoUnit chronoUnit, boolean bl) {
        YearMonth yearMonth = (YearMonth)this.displayedYearMonth.get();
        DateCell dateCell = this.lastFocusedDayCell;
        if (dateCell == null || !this.dayCellDate(dateCell).getMonth().equals(yearMonth.getMonth())) {
            dateCell = this.findDayCellForDate(yearMonth.atDay(1));
        }
        this.goToDayCell(dateCell, n, chronoUnit, bl);
    }

    public void goToDate(LocalDate localDate, boolean bl) {
        if (this.isValidDate(this.datePicker.getChronology(), localDate)) {
            this.displayedYearMonth.set((Object)YearMonth.from(localDate));
            if (bl) {
                this.findDayCellForDate(localDate).requestFocus();
            }
        }
    }

    public void selectDayCell(DateCell dateCell) {
        this.datePicker.setValue((Object)this.dayCellDate(dateCell));
        this.datePicker.hide();
    }

    private DateCell findDayCellForDate(LocalDate localDate) {
        for (int i = 0; i < this.dayCellDates.length; ++i) {
            if (!localDate.equals(this.dayCellDates[i])) continue;
            return this.dayCells.get(i);
        }
        return this.dayCells.get(this.dayCells.size() / 2 + 1);
    }

    void clearFocus() {
        LocalDate localDate = (LocalDate)this.datePicker.getValue();
        if (localDate == null) {
            localDate = LocalDate.now();
        }
        if (YearMonth.from(localDate).equals(this.displayedYearMonth.get())) {
            this.goToDate(localDate, true);
        } else {
            this.backMonthButton.requestFocus();
        }
        if (this.backMonthButton.getWidth() == 0.0) {
            this.backMonthButton.requestLayout();
            this.forwardMonthButton.requestLayout();
            this.backYearButton.requestLayout();
            this.forwardYearButton.requestLayout();
        }
    }

    protected void createDayCells() {
        EventHandler eventHandler = mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.PRIMARY) {
                return;
            }
            DateCell dateCell = (DateCell)mouseEvent.getSource();
            this.selectDayCell(dateCell);
            this.lastFocusedDayCell = dateCell;
        };
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < this.daysPerWeek; ++j) {
                DateCell dateCell = this.createDayCell();
                dateCell.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
                this.dayCells.add(dateCell);
            }
        }
        this.dayCellDates = new LocalDate[6 * this.daysPerWeek];
    }

    private DateCell createDayCell() {
        DateCell dateCell = null;
        if (this.datePicker.getDayCellFactory() != null) {
            dateCell = (DateCell)this.datePicker.getDayCellFactory().call((Object)this.datePicker);
        }
        if (dateCell == null) {
            dateCell = new DateCell();
        }
        return dateCell;
    }

    protected Locale getLocale() {
        return Locale.getDefault(Locale.Category.FORMAT);
    }

    protected Chronology getPrimaryChronology() {
        return this.datePicker.getChronology();
    }

    protected boolean isValidDate(Chronology chronology, LocalDate localDate, int n, ChronoUnit chronoUnit) {
        if (localDate != null) {
            try {
                return this.isValidDate(chronology, localDate.plus(n, chronoUnit));
            }
            catch (DateTimeException dateTimeException) {
                // empty catch block
            }
        }
        return false;
    }

    protected boolean isValidDate(Chronology chronology, LocalDate localDate) {
        try {
            if (localDate != null) {
                chronology.date(localDate);
            }
            return true;
        }
        catch (DateTimeException dateTimeException) {
            return false;
        }
    }

    private static /* synthetic */ void lambda$new$168(WeakChangeListener weakChangeListener, ObservableValue observableValue, Scene scene, Scene scene2) {
        if (scene != null) {
            scene.focusOwnerProperty().removeListener((ChangeListener)weakChangeListener);
        }
        if (scene2 != null) {
            scene2.focusOwnerProperty().addListener((ChangeListener)weakChangeListener);
        }
    }
}

