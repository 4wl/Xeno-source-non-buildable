/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Pos
 *  javafx.scene.Node
 *  javafx.scene.control.DateCell
 *  javafx.scene.control.DatePicker
 *  javafx.scene.control.Label
 *  javafx.scene.layout.BorderPane
 *  javafx.scene.text.Text
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.DatePickerContent;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.time.chrono.IsoChronology;
import java.time.format.DecimalStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

class DatePickerHijrahContent
extends DatePickerContent {
    private Label hijrahMonthYearLabel;

    DatePickerHijrahContent(DatePicker datePicker) {
        super(datePicker);
    }

    @Override
    protected Chronology getPrimaryChronology() {
        return IsoChronology.INSTANCE;
    }

    @Override
    protected BorderPane createMonthYearPane() {
        BorderPane borderPane = super.createMonthYearPane();
        this.hijrahMonthYearLabel = new Label();
        this.hijrahMonthYearLabel.getStyleClass().add((Object)"secondary-label");
        borderPane.setBottom((Node)this.hijrahMonthYearLabel);
        BorderPane.setAlignment((Node)this.hijrahMonthYearLabel, (Pos)Pos.CENTER);
        return borderPane;
    }

    @Override
    protected void updateMonthYearPane() {
        super.updateMonthYearPane();
        Locale locale = this.getLocale();
        HijrahChronology hijrahChronology = HijrahChronology.INSTANCE;
        long l = -1L;
        long l2 = -1L;
        String string = null;
        String string2 = null;
        String string3 = null;
        YearMonth yearMonth = (YearMonth)this.displayedYearMonthProperty().get();
        for (DateCell dateCell : this.dayCells) {
            LocalDate localDate = this.dayCellDate(dateCell);
            if (!yearMonth.equals(YearMonth.from(localDate))) continue;
            try {
                HijrahDate hijrahDate = hijrahChronology.date(localDate);
                long l3 = hijrahDate.getLong(ChronoField.MONTH_OF_YEAR);
                long l4 = hijrahDate.getLong(ChronoField.YEAR);
                if (string3 != null && l3 == l) continue;
                String string4 = this.monthFormatter.withLocale(locale).withChronology(hijrahChronology).withDecimalStyle(DecimalStyle.of(locale)).format(hijrahDate);
                String string5 = this.yearFormatter.withLocale(locale).withChronology(hijrahChronology).withDecimalStyle(DecimalStyle.of(locale)).format(hijrahDate);
                if (string3 == null) {
                    l = l3;
                    l2 = l4;
                    string = string4;
                    string2 = string5;
                    string3 = string + " " + string2;
                    continue;
                }
                if (l4 > l2) {
                    string3 = string + " " + string2 + " - " + string4 + " " + string5;
                    break;
                }
                string3 = string + " - " + string4 + " " + string2;
                break;
            }
            catch (DateTimeException dateTimeException) {
            }
        }
        this.hijrahMonthYearLabel.setText(string3);
    }

    @Override
    protected void createDayCells() {
        super.createDayCells();
        for (DateCell dateCell : this.dayCells) {
            Text text = new Text();
            dateCell.getProperties().put((Object)"DateCell.secondaryText", (Object)text);
        }
    }

    @Override
    void updateDayCells() {
        super.updateDayCells();
        Locale locale = this.getLocale();
        HijrahChronology hijrahChronology = HijrahChronology.INSTANCE;
        int n = -1;
        int n2 = -1;
        int n3 = -1;
        boolean bl = false;
        for (DateCell dateCell : this.dayCells) {
            Text text = (Text)dateCell.getProperties().get((Object)"DateCell.secondaryText");
            dateCell.getStyleClass().add((Object)"hijrah-day-cell");
            text.getStyleClass().setAll((Object[])new String[]{"text", "secondary-text"});
            try {
                HijrahDate hijrahDate = hijrahChronology.date(this.dayCellDate(dateCell));
                String string = this.dayCellFormatter.withLocale(locale).withChronology(hijrahChronology).withDecimalStyle(DecimalStyle.of(locale)).format(hijrahDate);
                text.setText(string);
                dateCell.requestLayout();
            }
            catch (DateTimeException dateTimeException) {
                text.setText(" ");
                dateCell.setDisable(true);
            }
        }
    }
}

