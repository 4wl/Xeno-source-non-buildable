/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableList
 *  javafx.css.ParsedValue
 *  javafx.css.StyleConverter
 *  javafx.css.Styleable
 *  javafx.geometry.Insets
 *  javafx.scene.effect.BlurType
 *  javafx.scene.effect.Effect
 *  javafx.scene.layout.BackgroundPosition
 *  javafx.scene.layout.BackgroundRepeat
 *  javafx.scene.layout.BackgroundSize
 *  javafx.scene.layout.BorderStrokeStyle
 *  javafx.scene.layout.BorderWidths
 *  javafx.scene.layout.CornerRadii
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.CycleMethod
 *  javafx.scene.paint.Paint
 *  javafx.scene.paint.Stop
 *  javafx.scene.shape.StrokeLineCap
 *  javafx.scene.shape.StrokeLineJoin
 *  javafx.scene.shape.StrokeType
 *  javafx.scene.text.Font
 *  javafx.scene.text.FontPosture
 *  javafx.scene.text.FontWeight
 */
package com.sun.javafx.css.parser;

import com.sun.javafx.css.Combinator;
import com.sun.javafx.css.CompoundSelector;
import com.sun.javafx.css.CssError;
import com.sun.javafx.css.Declaration;
import com.sun.javafx.css.FontFace;
import com.sun.javafx.css.ParsedValueImpl;
import com.sun.javafx.css.Rule;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.SimpleSelector;
import com.sun.javafx.css.Size;
import com.sun.javafx.css.SizeUnits;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.DurationConverter;
import com.sun.javafx.css.converters.EffectConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.FontConverter;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.css.converters.URLConverter;
import com.sun.javafx.css.parser.CSSLexer;
import com.sun.javafx.css.parser.DeriveColorConverter;
import com.sun.javafx.css.parser.LadderConverter;
import com.sun.javafx.css.parser.StopConverter;
import com.sun.javafx.css.parser.Token;
import com.sun.javafx.scene.layout.region.BackgroundPositionConverter;
import com.sun.javafx.scene.layout.region.BackgroundSizeConverter;
import com.sun.javafx.scene.layout.region.BorderImageSliceConverter;
import com.sun.javafx.scene.layout.region.BorderImageSlices;
import com.sun.javafx.scene.layout.region.BorderImageWidthConverter;
import com.sun.javafx.scene.layout.region.BorderImageWidthsSequenceConverter;
import com.sun.javafx.scene.layout.region.BorderStrokeStyleSequenceConverter;
import com.sun.javafx.scene.layout.region.BorderStyleConverter;
import com.sun.javafx.scene.layout.region.CornerRadiiConverter;
import com.sun.javafx.scene.layout.region.LayeredBackgroundPositionConverter;
import com.sun.javafx.scene.layout.region.LayeredBackgroundSizeConverter;
import com.sun.javafx.scene.layout.region.LayeredBorderPaintConverter;
import com.sun.javafx.scene.layout.region.LayeredBorderStyleConverter;
import com.sun.javafx.scene.layout.region.Margins;
import com.sun.javafx.scene.layout.region.RepeatStruct;
import com.sun.javafx.scene.layout.region.RepeatStructConverter;
import com.sun.javafx.scene.layout.region.SliceSequenceConverter;
import com.sun.javafx.scene.layout.region.StrokeBorderPaintConverter;
import com.sun.javafx.util.Logging;
import com.sun.javafx.util.Utils;
import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import javafx.collections.ObservableList;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.geometry.Insets;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Effect;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import sun.util.logging.PlatformLogger;

public final class CSSParser {
    private String stylesheetAsText;
    private String sourceOfStylesheet;
    private Styleable sourceOfInlineStyle;
    private static final PlatformLogger LOGGER = Logging.getCSSLogger();
    private final Map<String, String> properties = new HashMap<String, String>();
    private static final ParsedValueImpl<Size, Size> ZERO_PERCENT = new ParsedValueImpl(new Size(0.0, SizeUnits.PERCENT), null);
    private static final ParsedValueImpl<Size, Size> FIFTY_PERCENT = new ParsedValueImpl(new Size(50.0, SizeUnits.PERCENT), null);
    private static final ParsedValueImpl<Size, Size> ONE_HUNDRED_PERCENT = new ParsedValueImpl(new Size(100.0, SizeUnits.PERCENT), null);
    public static final String SPECIAL_REGION_URL_PREFIX = "SPECIAL-REGION-URL:";
    Token currentToken = null;
    private static Stack<String> imports;

    @Deprecated
    public static CSSParser getInstance() {
        return new CSSParser();
    }

    private void setInputSource(String string, String string2) {
        this.stylesheetAsText = string2;
        this.sourceOfStylesheet = string;
        this.sourceOfInlineStyle = null;
    }

    private void setInputSource(String string) {
        this.stylesheetAsText = string;
        this.sourceOfStylesheet = null;
        this.sourceOfInlineStyle = null;
    }

    private void setInputSource(Styleable styleable) {
        this.stylesheetAsText = styleable != null ? styleable.getStyle() : null;
        this.sourceOfStylesheet = null;
        this.sourceOfInlineStyle = styleable;
    }

    public Stylesheet parse(String string) {
        Stylesheet stylesheet = new Stylesheet();
        if (string != null && !string.trim().isEmpty()) {
            this.setInputSource(string);
            try (CharArrayReader charArrayReader = new CharArrayReader(string.toCharArray());){
                this.parse(stylesheet, charArrayReader);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        return stylesheet;
    }

    public Stylesheet parse(String string, String string2) throws IOException {
        Stylesheet stylesheet = new Stylesheet(string);
        if (string2 != null && !string2.trim().isEmpty()) {
            this.setInputSource(string, string2);
            try (CharArrayReader charArrayReader = new CharArrayReader(string2.toCharArray());){
                this.parse(stylesheet, charArrayReader);
            }
        }
        return stylesheet;
    }

    public Stylesheet parse(URL uRL) throws IOException {
        String string = uRL != null ? uRL.toExternalForm() : null;
        Stylesheet stylesheet = new Stylesheet(string);
        if (uRL != null) {
            this.setInputSource(string, null);
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRL.openStream()));){
                this.parse(stylesheet, bufferedReader);
            }
        }
        return stylesheet;
    }

    private void parse(Stylesheet stylesheet, Reader reader) {
        CSSLexer cSSLexer = new CSSLexer();
        cSSLexer.setReader(reader);
        try {
            this.parse(stylesheet, cSSLexer);
        }
        catch (Exception exception) {
            this.reportException(exception);
        }
    }

    public Stylesheet parseInlineStyle(Styleable styleable) {
        String string;
        Stylesheet stylesheet = new Stylesheet();
        String string2 = string = styleable != null ? styleable.getStyle() : null;
        if (string != null && !string.trim().isEmpty()) {
            this.setInputSource(styleable);
            ArrayList<Rule> arrayList = new ArrayList<Rule>();
            try (CharArrayReader charArrayReader = new CharArrayReader(string.toCharArray());){
                CSSLexer cSSLexer = CSSLexer.getInstance();
                cSSLexer.setReader(charArrayReader);
                this.currentToken = this.nextToken(cSSLexer);
                List<Declaration> list = this.declarations(cSSLexer);
                if (list != null && !list.isEmpty()) {
                    Selector selector = Selector.getUniversalSelector();
                    Rule rule = new Rule(Collections.singletonList(selector), list);
                    arrayList.add(rule);
                }
            }
            catch (IOException iOException) {
            }
            catch (Exception exception) {
                this.reportException(exception);
            }
            stylesheet.getRules().addAll(arrayList);
        }
        this.setInputSource((Styleable)null);
        return stylesheet;
    }

    public ParsedValueImpl parseExpr(String string, String string2) {
        if (string == null || string2 == null) {
            return null;
        }
        ParsedValueImpl parsedValueImpl = null;
        this.setInputSource(null, string + ": " + string2);
        char[] arrc = new char[string2.length() + 1];
        System.arraycopy(string2.toCharArray(), 0, arrc, 0, string2.length());
        arrc[arrc.length - 1] = 59;
        try (CharArrayReader charArrayReader = new CharArrayReader(arrc);){
            CSSLexer cSSLexer = CSSLexer.getInstance();
            cSSLexer.setReader(charArrayReader);
            this.currentToken = this.nextToken(cSSLexer);
            Term term = this.expr(cSSLexer);
            parsedValueImpl = this.valueFor(string, term, cSSLexer);
        }
        catch (IOException iOException) {
        }
        catch (ParseException parseException) {
            if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                LOGGER.warning("\"" + string + ": " + string2 + "\" " + parseException.toString());
            }
        }
        catch (Exception exception) {
            this.reportException(exception);
        }
        return parsedValueImpl;
    }

    private CssError createError(String string) {
        CssError cssError = null;
        cssError = this.sourceOfStylesheet != null ? new CssError.StylesheetParsingError(this.sourceOfStylesheet, string) : (this.sourceOfInlineStyle != null ? new CssError.InlineStyleParsingError(this.sourceOfInlineStyle, string) : new CssError.StringParsingError(this.stylesheetAsText, string));
        return cssError;
    }

    private void reportError(CssError cssError) {
        ObservableList<CssError> observableList = null;
        observableList = StyleManager.getErrors();
        if (observableList != null) {
            observableList.add(cssError);
        }
    }

    private void error(Term term, String string) throws ParseException {
        Token token = term != null ? term.token : null;
        ParseException parseException = new ParseException(string, token, this);
        this.reportError(this.createError(parseException.toString()));
        throw parseException;
    }

    private void reportException(Exception exception) {
        StackTraceElement[] arrstackTraceElement;
        if (LOGGER.isLoggable(PlatformLogger.Level.WARNING) && (arrstackTraceElement = exception.getStackTrace()).length > 0) {
            StringBuilder stringBuilder = new StringBuilder("Please report ");
            stringBuilder.append(exception.getClass().getName()).append(" at:");
            int n = 0;
            while (n < arrstackTraceElement.length && this.getClass().getName().equals(arrstackTraceElement[n].getClassName())) {
                stringBuilder.append("\n\t").append(arrstackTraceElement[n++].toString());
            }
            LOGGER.warning(stringBuilder.toString());
        }
    }

    private String formatDeprecatedMessage(Term term, String string) {
        StringBuilder stringBuilder = new StringBuilder("Using deprecated syntax for ");
        stringBuilder.append(string);
        if (this.sourceOfStylesheet != null) {
            stringBuilder.append(" at ").append(this.sourceOfStylesheet).append("[").append(term.token.getLine()).append(',').append(term.token.getOffset()).append("]");
        }
        stringBuilder.append(". Refer to the CSS Reference Guide.");
        return stringBuilder.toString();
    }

    private ParsedValueImpl<Color, Color> colorValueOfString(String string) {
        if (string.startsWith("#") || string.startsWith("0x")) {
            double d = 1.0;
            String string2 = string;
            int n = string.startsWith("#") ? 1 : 2;
            int n2 = string2.length();
            if (n2 - n == 4) {
                d = (float)Integer.parseInt(string2.substring(n2 - 1), 16) / 15.0f;
                string2 = string2.substring(0, n2 - 1);
            } else if (n2 - n == 8) {
                d = (float)Integer.parseInt(string2.substring(n2 - 2), 16) / 255.0f;
                string2 = string2.substring(0, n2 - 2);
            }
            return new ParsedValueImpl<Color, Color>(Color.web((String)string2, (double)d), null);
        }
        try {
            return new ParsedValueImpl<Color, Color>(Color.web((String)string), null);
        }
        catch (IllegalArgumentException illegalArgumentException) {
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        return null;
    }

    private String stripQuotes(String string) {
        return Utils.stripQuotes(string);
    }

    private double clamp(double d, double d2, double d3) {
        if (d2 < d) {
            return d;
        }
        if (d3 < d2) {
            return d3;
        }
        return d2;
    }

    private boolean isSize(Token token) {
        int n = token.getType();
        switch (n) {
            case 13: 
            case 14: 
            case 15: 
            case 16: 
            case 17: 
            case 18: 
            case 19: 
            case 20: 
            case 21: 
            case 22: 
            case 23: 
            case 24: 
            case 25: 
            case 26: {
                return true;
            }
        }
        return token.getType() == 11;
    }

    private Size size(Token token) throws ParseException {
        SizeUnits sizeUnits = SizeUnits.PX;
        int n = 2;
        String string = token.getText().trim();
        int n2 = string.length();
        int n3 = token.getType();
        switch (n3) {
            case 13: {
                sizeUnits = SizeUnits.PX;
                n = 0;
                break;
            }
            case 22: {
                sizeUnits = SizeUnits.PERCENT;
                n = 1;
                break;
            }
            case 15: {
                sizeUnits = SizeUnits.EM;
                break;
            }
            case 16: {
                sizeUnits = SizeUnits.EX;
                break;
            }
            case 21: {
                sizeUnits = SizeUnits.PX;
                break;
            }
            case 14: {
                sizeUnits = SizeUnits.CM;
                break;
            }
            case 18: {
                sizeUnits = SizeUnits.MM;
                break;
            }
            case 17: {
                sizeUnits = SizeUnits.IN;
                break;
            }
            case 20: {
                sizeUnits = SizeUnits.PT;
                break;
            }
            case 19: {
                sizeUnits = SizeUnits.PC;
                break;
            }
            case 23: {
                sizeUnits = SizeUnits.DEG;
                n = 3;
                break;
            }
            case 24: {
                sizeUnits = SizeUnits.GRAD;
                n = 4;
                break;
            }
            case 25: {
                sizeUnits = SizeUnits.RAD;
                n = 3;
                break;
            }
            case 26: {
                sizeUnits = SizeUnits.TURN;
                n = 4;
                break;
            }
            case 45: {
                sizeUnits = SizeUnits.S;
                n = 1;
                break;
            }
            case 46: {
                sizeUnits = SizeUnits.MS;
                break;
            }
            default: {
                if (LOGGER.isLoggable(PlatformLogger.Level.FINEST)) {
                    LOGGER.finest("Expected '<number>'");
                }
                ParseException parseException = new ParseException("Expected '<number>'", token, this);
                this.reportError(this.createError(parseException.toString()));
                throw parseException;
            }
        }
        return new Size(Double.parseDouble(string.substring(0, n2 - n)), sizeUnits);
    }

    private int numberOfTerms(Term term) {
        if (term == null) {
            return 0;
        }
        int n = 0;
        Term term2 = term;
        do {
            ++n;
        } while ((term2 = term2.nextInSeries) != null);
        return n;
    }

    private int numberOfLayers(Term term) {
        if (term == null) {
            return 0;
        }
        int n = 0;
        Term term2 = term;
        do {
            ++n;
            while (term2.nextInSeries != null) {
                term2 = term2.nextInSeries;
            }
        } while ((term2 = term2.nextLayer) != null);
        return n;
    }

    private int numberOfArgs(Term term) {
        if (term == null) {
            return 0;
        }
        int n = 0;
        Term term2 = term.firstArg;
        while (term2 != null) {
            ++n;
            term2 = term2.nextArg;
        }
        return n;
    }

    private Term nextLayer(Term term) {
        if (term == null) {
            return null;
        }
        Term term2 = term;
        while (term2.nextInSeries != null) {
            term2 = term2.nextInSeries;
        }
        return term2.nextLayer;
    }

    ParsedValueImpl valueFor(String string, Term term, CSSLexer cSSLexer) throws ParseException {
        Object object;
        String string2 = string.toLowerCase(Locale.ROOT);
        this.properties.put(string2, string2);
        if (term == null || term.token == null) {
            this.error(term, "Expected value for property '" + string2 + "'");
        }
        if (term.token.getType() == 11) {
            object = term.token.getText();
            if ("inherit".equalsIgnoreCase((String)object)) {
                return new ParsedValueImpl("inherit", null);
            }
            if ("null".equalsIgnoreCase((String)object) || "none".equalsIgnoreCase((String)object)) {
                return new ParsedValueImpl("null", null);
            }
        }
        if ("-fx-fill".equals(string2)) {
            object = this.parse(term);
            if (object.getConverter() == StyleConverter.getUrlConverter()) {
                object = new ParsedValueImpl<ParsedValue[], Paint>(new ParsedValue[]{object}, PaintConverter.ImagePatternConverter.getInstance());
            }
            return object;
        }
        if ("-fx-background-color".equals(string2)) {
            return this.parsePaintLayers(term);
        }
        if ("-fx-background-image".equals(string2)) {
            return this.parseURILayers(term);
        }
        if ("-fx-background-insets".equals(string2)) {
            return this.parseInsetsLayers(term);
        }
        if ("-fx-opaque-insets".equals(string2)) {
            return this.parseInsetsLayer(term);
        }
        if ("-fx-background-position".equals(string2)) {
            return this.parseBackgroundPositionLayers(term);
        }
        if ("-fx-background-radius".equals(string2)) {
            return this.parseCornerRadius(term);
        }
        if ("-fx-background-repeat".equals(string2)) {
            return this.parseBackgroundRepeatStyleLayers(term);
        }
        if ("-fx-background-size".equals(string2)) {
            return this.parseBackgroundSizeLayers(term);
        }
        if ("-fx-border-color".equals(string2)) {
            return this.parseBorderPaintLayers(term);
        }
        if ("-fx-border-insets".equals(string2)) {
            return this.parseInsetsLayers(term);
        }
        if ("-fx-border-radius".equals(string2)) {
            return this.parseCornerRadius(term);
        }
        if ("-fx-border-style".equals(string2)) {
            return this.parseBorderStyleLayers(term);
        }
        if ("-fx-border-width".equals(string2)) {
            return this.parseMarginsLayers(term);
        }
        if ("-fx-border-image-insets".equals(string2)) {
            return this.parseInsetsLayers(term);
        }
        if ("-fx-border-image-repeat".equals(string2)) {
            return this.parseBorderImageRepeatStyleLayers(term);
        }
        if ("-fx-border-image-slice".equals(string2)) {
            return this.parseBorderImageSliceLayers(term);
        }
        if ("-fx-border-image-source".equals(string2)) {
            return this.parseURILayers(term);
        }
        if ("-fx-border-image-width".equals(string2)) {
            return this.parseBorderImageWidthLayers(term);
        }
        if ("-fx-padding".equals(string2)) {
            object = this.parseSize1to4(term);
            return new ParsedValueImpl<ParsedValue[], Insets>((ParsedValue[])object, InsetsConverter.getInstance());
        }
        if ("-fx-label-padding".equals(string2)) {
            object = this.parseSize1to4(term);
            return new ParsedValueImpl<ParsedValue[], Insets>((ParsedValue[])object, InsetsConverter.getInstance());
        }
        if (string2.endsWith("font-family")) {
            return this.parseFontFamily(term);
        }
        if (string2.endsWith("font-size")) {
            object = this.parseFontSize(term);
            if (object == null) {
                this.error(term, "Expected '<font-size>'");
            }
            return object;
        }
        if (string2.endsWith("font-style")) {
            object = this.parseFontStyle(term);
            if (object == null) {
                this.error(term, "Expected '<font-style>'");
            }
            return object;
        }
        if (string2.endsWith("font-weight")) {
            object = this.parseFontWeight(term);
            if (object == null) {
                this.error(term, "Expected '<font-style>'");
            }
            return object;
        }
        if (string2.endsWith("font")) {
            return this.parseFont(term);
        }
        if ("-fx-stroke-dash-array".equals(string2)) {
            object = term;
            int n = this.numberOfTerms((Term)object);
            ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[n];
            int n2 = 0;
            while (object != null) {
                arrparsedValueImpl[n2++] = this.parseSize((Term)object);
                object = object.nextInSeries;
            }
            return new ParsedValueImpl<ParsedValue[], Number[]>(arrparsedValueImpl, SizeConverter.SequenceConverter.getInstance());
        }
        if ("-fx-stroke-line-join".equals(string2)) {
            object = this.parseStrokeLineJoin(term);
            if (object == null) {
                this.error(term, "Expected 'miter', 'bevel' or 'round'");
            }
            return object[0];
        }
        if ("-fx-stroke-line-cap".equals(string2)) {
            object = this.parseStrokeLineCap(term);
            if (object == null) {
                this.error(term, "Expected 'square', 'butt' or 'round'");
            }
            return object;
        }
        if ("-fx-stroke-type".equals(string2)) {
            object = this.parseStrokeType(term);
            if (object == null) {
                this.error(term, "Expected 'centered', 'inside' or 'outside'");
            }
            return object;
        }
        if ("-fx-font-smoothing-type".equals(string2)) {
            object = null;
            int n = -1;
            Token token = term.token;
            if (term.token == null || (n = term.token.getType()) != 10 && n != 11 || (object = term.token.getText()) == null || object.isEmpty()) {
                this.error(term, "Expected STRING or IDENT");
            }
            return new ParsedValueImpl(this.stripQuotes((String)object), null, false);
        }
        return this.parse(term);
    }

    private ParsedValueImpl parse(Term term) throws ParseException {
        if (term.token == null) {
            this.error(term, "Parse error");
        }
        Token token = term.token;
        ParsedValueImpl<Object, Object> parsedValueImpl = null;
        int n = token.getType();
        switch (n) {
            case 13: 
            case 14: 
            case 15: 
            case 16: 
            case 17: 
            case 18: 
            case 19: 
            case 20: 
            case 21: 
            case 22: 
            case 23: 
            case 24: 
            case 25: 
            case 26: {
                if (term.nextInSeries == null) {
                    ParsedValueImpl parsedValueImpl2 = new ParsedValueImpl(this.size(token), null);
                    parsedValueImpl = new ParsedValueImpl(parsedValueImpl2, SizeConverter.getInstance());
                    break;
                }
                ParsedValueImpl<Size, Size>[] arrparsedValueImpl = this.parseSizeSeries(term);
                parsedValueImpl = new ParsedValueImpl<ParsedValue[], Number[]>(arrparsedValueImpl, SizeConverter.SequenceConverter.getInstance());
                break;
            }
            case 45: 
            case 46: {
                ParsedValueImpl parsedValueImpl3 = new ParsedValueImpl(this.size(token), null);
                parsedValueImpl = new ParsedValueImpl(parsedValueImpl3, DurationConverter.getInstance());
                break;
            }
            case 10: 
            case 11: {
                boolean bl;
                boolean bl2 = n == 11;
                String string = this.stripQuotes(token.getText());
                String string2 = string.toLowerCase(Locale.ROOT);
                if ("ladder".equals(string2)) {
                    parsedValueImpl = this.ladder(term);
                    break;
                }
                if ("linear".equals(string2) && term.nextInSeries != null) {
                    parsedValueImpl = this.linearGradient(term);
                    break;
                }
                if ("radial".equals(string2) && term.nextInSeries != null) {
                    parsedValueImpl = this.radialGradient(term);
                    break;
                }
                if ("infinity".equals(string2)) {
                    Size size = new Size(Double.MAX_VALUE, SizeUnits.PX);
                    ParsedValueImpl parsedValueImpl4 = new ParsedValueImpl(size, null);
                    parsedValueImpl = new ParsedValueImpl(parsedValueImpl4, SizeConverter.getInstance());
                    break;
                }
                if ("indefinite".equals(string2)) {
                    Size size = new Size(Double.POSITIVE_INFINITY, SizeUnits.PX);
                    ParsedValueImpl parsedValueImpl5 = new ParsedValueImpl(size, null);
                    parsedValueImpl = new ParsedValueImpl(parsedValueImpl5, DurationConverter.getInstance());
                    break;
                }
                if ("true".equals(string2)) {
                    parsedValueImpl = new ParsedValueImpl<String, Boolean>("true", BooleanConverter.getInstance());
                    break;
                }
                if ("false".equals(string2)) {
                    parsedValueImpl = new ParsedValueImpl<String, Boolean>("false", BooleanConverter.getInstance());
                    break;
                }
                boolean bl3 = bl = bl2 && this.properties.containsKey(string2);
                if (!bl && (parsedValueImpl = this.colorValueOfString(string)) != null) break;
                parsedValueImpl = new ParsedValueImpl(bl ? string2 : string, null, bl2 || bl);
                break;
            }
            case 37: {
                String string = token.getText();
                try {
                    parsedValueImpl = new ParsedValueImpl(Color.web((String)string), null);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    this.error(term, illegalArgumentException.getMessage());
                }
                break;
            }
            case 12: {
                return this.parseFunction(term);
            }
            case 43: {
                return this.parseURI(term);
            }
            default: {
                String string = "Unknown token type: '" + n + "'";
                this.error(term, string);
            }
        }
        return parsedValueImpl;
    }

    private ParsedValueImpl<?, Size> parseSize(Term term) throws ParseException {
        if (term.token == null || !this.isSize(term.token)) {
            this.error(term, "Expected '<size>'");
        }
        ParsedValueImpl parsedValueImpl = null;
        if (term.token.getType() != 11) {
            Size size = this.size(term.token);
            parsedValueImpl = new ParsedValueImpl(size, null);
        } else {
            String string = term.token.getText();
            parsedValueImpl = new ParsedValueImpl(string, null, true);
        }
        return parsedValueImpl;
    }

    private ParsedValueImpl<?, Color> parseColor(Term term) throws ParseException {
        ParsedValueImpl parsedValueImpl = null;
        if (term.token != null && (term.token.getType() == 11 || term.token.getType() == 37 || term.token.getType() == 12)) {
            parsedValueImpl = this.parse(term);
        } else {
            this.error(term, "Expected '<color>'");
        }
        return parsedValueImpl;
    }

    private ParsedValueImpl rgb(Term term) throws ParseException {
        int n;
        Token token;
        Token token2;
        Token token3;
        Token token4;
        String string;
        String string2 = string = term.token != null ? term.token.getText() : null;
        if (string == null || !"rgb".regionMatches(true, 0, string, 0, 3)) {
            this.error(term, "Expected 'rgb' or 'rgba'");
        }
        Term term2 = term;
        term2 = term2.firstArg;
        if (term2 == null) {
            this.error(term, "Expected '<number>' or '<percentage>'");
        }
        if ((token4 = term2.token) == null || token4.getType() != 13 && token4.getType() != 22) {
            this.error(term2, "Expected '<number>' or '<percentage>'");
        }
        term = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term, "Expected '<number>' or '<percentage>'");
        }
        if ((token3 = term2.token) == null || token3.getType() != 13 && token3.getType() != 22) {
            this.error(term2, "Expected '<number>' or '<percentage>'");
        }
        term = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term, "Expected '<number>' or '<percentage>'");
        }
        if ((token2 = term2.token) == null || token2.getType() != 13 && token2.getType() != 22) {
            this.error(term2, "Expected '<number>' or '<percentage>'");
        }
        term = term2;
        term2 = term2.nextArg;
        if (term2 != null) {
            token = term2.token;
            if (token == null || token.getType() != 13) {
                this.error(term2, "Expected '<number>'");
            }
        } else {
            token = null;
        }
        if ((n = token4.getType()) != token3.getType() || n != token2.getType() || n != 13 && n != 22) {
            this.error(term, "Argument type mistmatch");
        }
        String string3 = token4.getText();
        String string4 = token3.getText();
        String string5 = token2.getText();
        double d = 0.0;
        double d2 = 0.0;
        double d3 = 0.0;
        if (n == 13) {
            d = this.clamp(0.0, Double.parseDouble(string3) / 255.0, 1.0);
            d2 = this.clamp(0.0, Double.parseDouble(string4) / 255.0, 1.0);
            d3 = this.clamp(0.0, Double.parseDouble(string5) / 255.0, 1.0);
        } else {
            d = this.clamp(0.0, Double.parseDouble(string3.substring(0, string3.length() - 1)) / 100.0, 1.0);
            d2 = this.clamp(0.0, Double.parseDouble(string4.substring(0, string4.length() - 1)) / 100.0, 1.0);
            d3 = this.clamp(0.0, Double.parseDouble(string5.substring(0, string5.length() - 1)) / 100.0, 1.0);
        }
        String string6 = token != null ? token.getText() : null;
        double d4 = string6 != null ? this.clamp(0.0, Double.parseDouble(string6), 1.0) : 1.0;
        return new ParsedValueImpl(Color.color((double)d, (double)d2, (double)d3, (double)d4), null);
    }

    private ParsedValueImpl hsb(Term term) throws ParseException {
        Token token;
        Token token2;
        Token token3;
        Token token4;
        String string;
        String string2 = string = term.token != null ? term.token.getText() : null;
        if (string == null || !"hsb".regionMatches(true, 0, string, 0, 3)) {
            this.error(term, "Expected 'hsb' or 'hsba'");
        }
        Term term2 = term;
        term2 = term2.firstArg;
        if (term2 == null) {
            this.error(term, "Expected '<number>'");
        }
        if ((token4 = term2.token) == null || token4.getType() != 13) {
            this.error(term2, "Expected '<number>'");
        }
        term = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term, "Expected '<percent>'");
        }
        if ((token3 = term2.token) == null || token3.getType() != 22) {
            this.error(term2, "Expected '<percent>'");
        }
        term = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term, "Expected '<percent>'");
        }
        if ((token2 = term2.token) == null || token2.getType() != 22) {
            this.error(term2, "Expected '<percent>'");
        }
        term = term2;
        term2 = term2.nextArg;
        if (term2 != null) {
            token = term2.token;
            if (token == null || token.getType() != 13) {
                this.error(term2, "Expected '<number>'");
            }
        } else {
            token = null;
        }
        Size size = this.size(token4);
        Size size2 = this.size(token3);
        Size size3 = this.size(token2);
        double d = size.pixels();
        double d2 = this.clamp(0.0, size2.pixels(), 1.0);
        double d3 = this.clamp(0.0, size3.pixels(), 1.0);
        Size size4 = token != null ? this.size(token) : null;
        double d4 = size4 != null ? this.clamp(0.0, size4.pixels(), 1.0) : 1.0;
        return new ParsedValueImpl(Color.hsb((double)d, (double)d2, (double)d3, (double)d4), null);
    }

    private ParsedValueImpl<ParsedValue[], Color> derive(Term term) throws ParseException {
        String string;
        String string2 = string = term.token != null ? term.token.getText() : null;
        if (string == null || !"derive".regionMatches(true, 0, string, 0, 6)) {
            this.error(term, "Expected 'derive'");
        }
        Term term2 = term;
        term2 = term2.firstArg;
        if (term2 == null) {
            this.error(term, "Expected '<color>'");
        }
        ParsedValueImpl<?, Color> parsedValueImpl = this.parseColor(term2);
        Term term3 = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<percent'");
        }
        ParsedValueImpl<?, Size> parsedValueImpl2 = this.parseSize(term2);
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[]{parsedValueImpl, parsedValueImpl2};
        return new ParsedValueImpl<ParsedValue[], Color>(arrparsedValueImpl, DeriveColorConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], Color> ladder(Term term) throws ParseException {
        String string;
        String string2 = string = term.token != null ? term.token.getText() : null;
        if (string == null || !"ladder".regionMatches(true, 0, string, 0, 6)) {
            this.error(term, "Expected 'ladder'");
        }
        if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
            LOGGER.warning(this.formatDeprecatedMessage(term, "ladder"));
        }
        Term term2 = term;
        term2 = term2.nextInSeries;
        if (term2 == null) {
            this.error(term, "Expected '<color>'");
        }
        ParsedValueImpl parsedValueImpl = this.parse(term2);
        Term term3 = term2;
        term2 = term2.nextInSeries;
        if (term2 == null) {
            this.error(term3, "Expected 'stops'");
        }
        if (term2.token == null || term2.token.getType() != 11 || !"stops".equalsIgnoreCase(term2.token.getText())) {
            this.error(term2, "Expected 'stops'");
        }
        term3 = term2;
        term2 = term2.nextInSeries;
        if (term2 == null) {
            this.error(term3, "Expected '(<number>, <color>)'");
        }
        int n = 0;
        Term term4 = term2;
        do {
            ++n;
        } while ((term4 = term4.nextInSeries) != null && term4.token != null && term4.token.getType() == 34);
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[n + 1];
        arrparsedValueImpl[0] = parsedValueImpl;
        int n2 = 1;
        do {
            ParsedValueImpl<ParsedValue[], Stop> parsedValueImpl2;
            if ((parsedValueImpl2 = this.stop(term2)) != null) {
                arrparsedValueImpl[n2++] = parsedValueImpl2;
            }
            term3 = term2;
        } while ((term2 = term2.nextInSeries) != null && term2.token.getType() == 34);
        if (term2 != null) {
            term.nextInSeries = term2;
        } else {
            term.nextInSeries = null;
            term.nextLayer = term3.nextLayer;
        }
        return new ParsedValueImpl<ParsedValue[], Color>(arrparsedValueImpl, LadderConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], Color> parseLadder(Term term) throws ParseException {
        String string;
        String string2 = string = term.token != null ? term.token.getText() : null;
        if (string == null || !"ladder".regionMatches(true, 0, string, 0, 6)) {
            this.error(term, "Expected 'ladder'");
        }
        Term term2 = term;
        term2 = term2.firstArg;
        if (term2 == null) {
            this.error(term, "Expected '<color>'");
        }
        ParsedValueImpl parsedValueImpl = this.parse(term2);
        Term term3 = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<color-stop>[, <color-stop>]+'");
        }
        ParsedValueImpl<ParsedValue[], Stop>[] arrparsedValueImpl = this.parseColorStops(term2);
        ParsedValueImpl[] arrparsedValueImpl2 = new ParsedValueImpl[arrparsedValueImpl.length + 1];
        arrparsedValueImpl2[0] = parsedValueImpl;
        System.arraycopy(arrparsedValueImpl, 0, arrparsedValueImpl2, 1, arrparsedValueImpl.length);
        return new ParsedValueImpl<ParsedValue[], Color>(arrparsedValueImpl2, LadderConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], Stop> stop(Term term) throws ParseException {
        String string;
        String string2 = string = term.token != null ? term.token.getText() : null;
        if (string == null || !"(".equals(string)) {
            this.error(term, "Expected '('");
        }
        Term term2 = null;
        term2 = term.firstArg;
        if (term2 == null) {
            this.error(term, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> parsedValueImpl = this.parseSize(term2);
        Term term3 = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<color>'");
        }
        ParsedValueImpl<?, Color> parsedValueImpl2 = this.parseColor(term2);
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[]{parsedValueImpl, parsedValueImpl2};
        return new ParsedValueImpl<ParsedValue[], Stop>(arrparsedValueImpl, StopConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], Stop>[] parseColorStops(Term term) throws ParseException {
        int n = 1;
        Term term2 = term;
        while (term2 != null) {
            if (term2.nextArg != null) {
                ++n;
                term2 = term2.nextArg;
                continue;
            }
            if (term2.nextInSeries == null) break;
            term2 = term2.nextInSeries;
        }
        if (n < 2) {
            this.error(term, "Expected '<color-stop>'");
        }
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[n];
        Object[] arrobject = new Size[n];
        Arrays.fill(arrobject, null);
        Term term3 = term;
        Term term4 = term;
        Object var8_8 = null;
        for (int i = 0; i < n; ++i) {
            arrparsedValueImpl[i] = this.parseColor(term3);
            term4 = term3;
            Term term5 = term3.nextInSeries;
            if (term5 != null) {
                if (this.isSize(term5.token)) {
                    arrobject[i] = this.size(term5.token);
                    if (var8_8 != null && var8_8 != ((Size)arrobject[i]).getUnits()) {
                        this.error(term5, "Parser unable to handle mixed '<percent>' and '<length>'");
                    }
                } else {
                    this.error(term4, "Expected '<percent>' or '<length>'");
                }
                term4 = term5;
                term3 = term5.nextArg;
                continue;
            }
            term4 = term3;
            term3 = term3.nextArg;
        }
        if (arrobject[0] == null) {
            arrobject[0] = new Size(0.0, SizeUnits.PERCENT);
        }
        if (arrobject[n - 1] == null) {
            arrobject[n - 1] = new Size(100.0, SizeUnits.PERCENT);
        }
        Object object = null;
        for (int i = 1; i < n; ++i) {
            Object object2;
            Object object3 = arrobject[i - 1];
            if (object3 == null) continue;
            if (object == null || ((Size)object).getValue() < ((Size)object3).getValue()) {
                object = object3;
            }
            if ((object2 = arrobject[i]) == null || !(((Size)object2).getValue() < ((Size)object).getValue())) continue;
            arrobject[i] = object;
        }
        Object object4 = null;
        int n2 = -1;
        for (int i = 0; i < n; ++i) {
            Object object5 = arrobject[i];
            if (object5 == null) {
                if (n2 != -1) continue;
                n2 = i;
                continue;
            }
            if (n2 > -1) {
                int n3 = i - n2;
                double d = ((Size)object4).getValue();
                double d2 = (((Size)object5).getValue() - d) / (double)(n3 + 1);
                while (n2 < i) {
                    arrobject[n2++] = new Size(d += d2, ((Size)object5).getUnits());
                }
                n2 = -1;
                object4 = object5;
                continue;
            }
            object4 = object5;
        }
        ParsedValueImpl[] arrparsedValueImpl2 = new ParsedValueImpl[n];
        for (int i = 0; i < n; ++i) {
            arrparsedValueImpl2[i] = new ParsedValueImpl<ParsedValue[], Stop>(new ParsedValueImpl[]{new ParsedValueImpl(arrobject[i], null), arrparsedValueImpl[i]}, StopConverter.getInstance());
        }
        return arrparsedValueImpl2;
    }

    private ParsedValueImpl[] point(Term term) throws ParseException {
        String string;
        if (term.token == null || term.token.getType() != 34) {
            this.error(term, "Expected '(<number>, <number>)'");
        }
        if ((string = term.token.getText()) == null || !"(".equalsIgnoreCase(string)) {
            this.error(term, "Expected '('");
        }
        Term term2 = null;
        term2 = term.firstArg;
        if (term2 == null) {
            this.error(term, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> parsedValueImpl = this.parseSize(term2);
        Term term3 = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> parsedValueImpl2 = this.parseSize(term2);
        return new ParsedValueImpl[]{parsedValueImpl, parsedValueImpl2};
    }

    private ParsedValueImpl parseFunction(Term term) throws ParseException {
        String string;
        String string2 = string = term.token != null ? term.token.getText() : null;
        if (string == null) {
            this.error(term, "Expected function name");
        } else {
            if ("rgb".regionMatches(true, 0, string, 0, 3)) {
                return this.rgb(term);
            }
            if ("hsb".regionMatches(true, 0, string, 0, 3)) {
                return this.hsb(term);
            }
            if ("derive".regionMatches(true, 0, string, 0, 6)) {
                return this.derive(term);
            }
            if ("innershadow".regionMatches(true, 0, string, 0, 11)) {
                return this.innershadow(term);
            }
            if ("dropshadow".regionMatches(true, 0, string, 0, 10)) {
                return this.dropshadow(term);
            }
            if ("linear-gradient".regionMatches(true, 0, string, 0, 15)) {
                return this.parseLinearGradient(term);
            }
            if ("radial-gradient".regionMatches(true, 0, string, 0, 15)) {
                return this.parseRadialGradient(term);
            }
            if ("image-pattern".regionMatches(true, 0, string, 0, 13)) {
                return this.parseImagePattern(term);
            }
            if ("repeating-image-pattern".regionMatches(true, 0, string, 0, 23)) {
                return this.parseRepeatingImagePattern(term);
            }
            if ("ladder".regionMatches(true, 0, string, 0, 6)) {
                return this.parseLadder(term);
            }
            if ("region".regionMatches(true, 0, string, 0, 6)) {
                return this.parseRegion(term);
            }
            this.error(term, "Unexpected function '" + string + "'");
        }
        return null;
    }

    private ParsedValueImpl<String, BlurType> blurType(Term term) throws ParseException {
        if (term == null) {
            return null;
        }
        if (term.token == null || term.token.getType() != 11 || term.token.getText() == null || term.token.getText().isEmpty()) {
            this.error(term, "Expected 'gaussian', 'one-pass-box', 'two-pass-box', or 'three-pass-box'");
        }
        String string = term.token.getText().toLowerCase(Locale.ROOT);
        BlurType blurType = BlurType.THREE_PASS_BOX;
        if ("gaussian".equals(string)) {
            blurType = BlurType.GAUSSIAN;
        } else if ("one-pass-box".equals(string)) {
            blurType = BlurType.ONE_PASS_BOX;
        } else if ("two-pass-box".equals(string)) {
            blurType = BlurType.TWO_PASS_BOX;
        } else if ("three-pass-box".equals(string)) {
            blurType = BlurType.THREE_PASS_BOX;
        } else {
            this.error(term, "Expected 'gaussian', 'one-pass-box', 'two-pass-box', or 'three-pass-box'");
        }
        return new ParsedValueImpl<String, BlurType>(blurType.name(), new EnumConverter<BlurType>(BlurType.class));
    }

    private ParsedValueImpl innershadow(Term term) throws ParseException {
        Term term2;
        String string;
        String string2 = string = term.token != null ? term.token.getText() : null;
        if (!"innershadow".regionMatches(true, 0, string, 0, 11)) {
            this.error(term, "Expected 'innershadow'");
        }
        if ((term2 = term.firstArg) == null) {
            this.error(term, "Expected '<blur-type>'");
        }
        ParsedValueImpl<String, BlurType> parsedValueImpl = this.blurType(term2);
        Term term3 = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<color>'");
        }
        ParsedValueImpl<?, Color> parsedValueImpl2 = this.parseColor(term2);
        term3 = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> parsedValueImpl3 = this.parseSize(term2);
        term3 = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> parsedValueImpl4 = this.parseSize(term2);
        term3 = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> parsedValueImpl5 = this.parseSize(term2);
        term3 = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> parsedValueImpl6 = this.parseSize(term2);
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[]{parsedValueImpl, parsedValueImpl2, parsedValueImpl3, parsedValueImpl4, parsedValueImpl5, parsedValueImpl6};
        return new ParsedValueImpl<ParsedValue[], Effect>(arrparsedValueImpl, EffectConverter.InnerShadowConverter.getInstance());
    }

    private ParsedValueImpl dropshadow(Term term) throws ParseException {
        Term term2;
        String string;
        String string2 = string = term.token != null ? term.token.getText() : null;
        if (!"dropshadow".regionMatches(true, 0, string, 0, 10)) {
            this.error(term, "Expected 'dropshadow'");
        }
        if ((term2 = term.firstArg) == null) {
            this.error(term, "Expected '<blur-type>'");
        }
        ParsedValueImpl<String, BlurType> parsedValueImpl = this.blurType(term2);
        Term term3 = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<color>'");
        }
        ParsedValueImpl<?, Color> parsedValueImpl2 = this.parseColor(term2);
        term3 = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> parsedValueImpl3 = this.parseSize(term2);
        term3 = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> parsedValueImpl4 = this.parseSize(term2);
        term3 = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> parsedValueImpl5 = this.parseSize(term2);
        term3 = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<number>'");
        }
        ParsedValueImpl<?, Size> parsedValueImpl6 = this.parseSize(term2);
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[]{parsedValueImpl, parsedValueImpl2, parsedValueImpl3, parsedValueImpl4, parsedValueImpl5, parsedValueImpl6};
        return new ParsedValueImpl<ParsedValue[], Effect>(arrparsedValueImpl, EffectConverter.DropShadowConverter.getInstance());
    }

    private ParsedValueImpl<String, CycleMethod> cycleMethod(Term term) {
        CycleMethod cycleMethod = null;
        if (term != null && term.token.getType() == 11) {
            String string = term.token.getText().toLowerCase(Locale.ROOT);
            if ("repeat".equals(string)) {
                cycleMethod = CycleMethod.REPEAT;
            } else if ("reflect".equals(string)) {
                cycleMethod = CycleMethod.REFLECT;
            } else if ("no-cycle".equals(string)) {
                cycleMethod = CycleMethod.NO_CYCLE;
            }
        }
        if (cycleMethod != null) {
            return new ParsedValueImpl<String, CycleMethod>(cycleMethod.name(), new EnumConverter<CycleMethod>(CycleMethod.class));
        }
        return null;
    }

    private ParsedValueImpl<ParsedValue[], Paint> linearGradient(Term term) throws ParseException {
        ParsedValueImpl<Object, Object> parsedValueImpl;
        String string;
        String string2 = string = term.token != null ? term.token.getText() : null;
        if (string == null || !"linear".equalsIgnoreCase(string)) {
            this.error(term, "Expected 'linear'");
        }
        if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
            LOGGER.warning(this.formatDeprecatedMessage(term, "linear gradient"));
        }
        Term term2 = term;
        term2 = term2.nextInSeries;
        if (term2 == null) {
            this.error(term, "Expected '(<number>, <number>)'");
        }
        ParsedValueImpl[] arrparsedValueImpl = this.point(term2);
        Term term3 = term2;
        term2 = term2.nextInSeries;
        if (term2 == null) {
            this.error(term3, "Expected 'to'");
        }
        if (term2.token == null || term2.token.getType() != 11 || !"to".equalsIgnoreCase(term2.token.getText())) {
            this.error(term, "Expected 'to'");
        }
        term3 = term2;
        term2 = term2.nextInSeries;
        if (term2 == null) {
            this.error(term3, "Expected '(<number>, <number>)'");
        }
        ParsedValueImpl[] arrparsedValueImpl2 = this.point(term2);
        term3 = term2;
        term2 = term2.nextInSeries;
        if (term2 == null) {
            this.error(term3, "Expected 'stops'");
        }
        if (term2.token == null || term2.token.getType() != 11 || !"stops".equalsIgnoreCase(term2.token.getText())) {
            this.error(term2, "Expected 'stops'");
        }
        term3 = term2;
        term2 = term2.nextInSeries;
        if (term2 == null) {
            this.error(term3, "Expected '(<number>, <number>)'");
        }
        int n = 0;
        Term term4 = term2;
        do {
            ++n;
        } while ((term4 = term4.nextInSeries) != null && term4.token != null && term4.token.getType() == 34);
        ParsedValueImpl[] arrparsedValueImpl3 = new ParsedValueImpl[n];
        int n2 = 0;
        do {
            if ((parsedValueImpl = this.stop(term2)) != null) {
                arrparsedValueImpl3[n2++] = parsedValueImpl;
            }
            term3 = term2;
        } while ((term2 = term2.nextInSeries) != null && term2.token.getType() == 34);
        parsedValueImpl = this.cycleMethod(term2);
        if (parsedValueImpl == null) {
            parsedValueImpl = new ParsedValueImpl(CycleMethod.NO_CYCLE.name(), new EnumConverter<CycleMethod>(CycleMethod.class));
            if (term2 != null) {
                term.nextInSeries = term2;
            } else {
                term.nextInSeries = null;
                term.nextLayer = term3.nextLayer;
            }
        } else {
            term.nextInSeries = term2.nextInSeries;
            term.nextLayer = term2.nextLayer;
        }
        ParsedValueImpl[] arrparsedValueImpl4 = new ParsedValueImpl[5 + arrparsedValueImpl3.length];
        int n3 = 0;
        arrparsedValueImpl4[n3++] = arrparsedValueImpl != null ? arrparsedValueImpl[0] : null;
        arrparsedValueImpl4[n3++] = arrparsedValueImpl != null ? arrparsedValueImpl[1] : null;
        arrparsedValueImpl4[n3++] = arrparsedValueImpl2 != null ? arrparsedValueImpl2[0] : null;
        arrparsedValueImpl4[n3++] = arrparsedValueImpl2 != null ? arrparsedValueImpl2[1] : null;
        arrparsedValueImpl4[n3++] = parsedValueImpl;
        for (int i = 0; i < arrparsedValueImpl3.length; ++i) {
            arrparsedValueImpl4[n3++] = arrparsedValueImpl3[i];
        }
        return new ParsedValueImpl<ParsedValue[], Paint>(arrparsedValueImpl4, PaintConverter.LinearGradientConverter.getInstance());
    }

    private ParsedValueImpl parseLinearGradient(Term term) throws ParseException {
        int n;
        Term term2;
        String string;
        String string2 = string = term.token != null ? term.token.getText() : null;
        if (!"linear-gradient".regionMatches(true, 0, string, 0, 15)) {
            this.error(term, "Expected 'linear-gradient'");
        }
        if ((term2 = term.firstArg) == null || term2.token == null || term2.token.getText().isEmpty()) {
            this.error(term, "Expected 'from <point> to <point>' or 'to <side-or-corner>' or '<cycle-method>' or '<color-stop>'");
        }
        Term term3 = term2;
        ParsedValueImpl[] arrparsedValueImpl = null;
        ParsedValueImpl[] arrparsedValueImpl2 = null;
        if ("from".equalsIgnoreCase(term2.token.getText())) {
            term3 = term2;
            term2 = term2.nextInSeries;
            if (term2 == null) {
                this.error(term3, "Expected '<point>'");
            }
            ParsedValueImpl<?, Size> parsedValueImpl = this.parseSize(term2);
            term3 = term2;
            term2 = term2.nextInSeries;
            if (term2 == null) {
                this.error(term3, "Expected '<point>'");
            }
            ParsedValueImpl<?, Size> parsedValueImpl2 = this.parseSize(term2);
            arrparsedValueImpl = new ParsedValueImpl[]{parsedValueImpl, parsedValueImpl2};
            term3 = term2;
            term2 = term2.nextInSeries;
            if (term2 == null) {
                this.error(term3, "Expected 'to'");
            }
            if (term2.token == null || term2.token.getType() != 11 || !"to".equalsIgnoreCase(term2.token.getText())) {
                this.error(term3, "Expected 'to'");
            }
            term3 = term2;
            term2 = term2.nextInSeries;
            if (term2 == null) {
                this.error(term3, "Expected '<point>'");
            }
            parsedValueImpl = this.parseSize(term2);
            term3 = term2;
            term2 = term2.nextInSeries;
            if (term2 == null) {
                this.error(term3, "Expected '<point>'");
            }
            parsedValueImpl2 = this.parseSize(term2);
            arrparsedValueImpl2 = new ParsedValueImpl[]{parsedValueImpl, parsedValueImpl2};
            term3 = term2;
            term2 = term2.nextArg;
        } else if ("to".equalsIgnoreCase(term2.token.getText())) {
            term3 = term2;
            term2 = term2.nextInSeries;
            if (term2 == null || term2.token == null || term2.token.getType() != 11 || term2.token.getText().isEmpty()) {
                this.error(term3, "Expected '<side-or-corner>'");
            }
            int n2 = 0;
            int n3 = 0;
            int n4 = 0;
            n = 0;
            String string3 = term2.token.getText().toLowerCase(Locale.ROOT);
            if ("top".equals(string3)) {
                n3 = 100;
                n = 0;
            } else if ("bottom".equals(string3)) {
                n3 = 0;
                n = 100;
            } else if ("right".equals(string3)) {
                n2 = 0;
                n4 = 100;
            } else if ("left".equals(string3)) {
                n2 = 100;
                n4 = 0;
            } else {
                this.error(term2, "Invalid '<side-or-corner>'");
            }
            term3 = term2;
            if (term2.nextInSeries != null) {
                term2 = term2.nextInSeries;
                if (term2.token != null && term2.token.getType() == 11 && !term2.token.getText().isEmpty()) {
                    String string4 = term2.token.getText().toLowerCase(Locale.ROOT);
                    if ("right".equals(string4) && n2 == 0 && n4 == 0) {
                        n2 = 0;
                        n4 = 100;
                    } else if ("left".equals(string4) && n2 == 0 && n4 == 0) {
                        n2 = 100;
                        n4 = 0;
                    } else if ("top".equals(string4) && n3 == 0 && n == 0) {
                        n3 = 100;
                        n = 0;
                    } else if ("bottom".equals(string4) && n3 == 0 && n == 0) {
                        n3 = 0;
                        n = 100;
                    } else {
                        this.error(term2, "Invalid '<side-or-corner>'");
                    }
                } else {
                    this.error(term3, "Expected '<side-or-corner>'");
                }
            }
            arrparsedValueImpl = new ParsedValueImpl[]{new ParsedValueImpl(new Size(n2, SizeUnits.PERCENT), null), new ParsedValueImpl(new Size(n3, SizeUnits.PERCENT), null)};
            arrparsedValueImpl2 = new ParsedValueImpl[]{new ParsedValueImpl(new Size(n4, SizeUnits.PERCENT), null), new ParsedValueImpl(new Size(n, SizeUnits.PERCENT), null)};
            term3 = term2;
            term2 = term2.nextArg;
        }
        if (arrparsedValueImpl == null && arrparsedValueImpl2 == null) {
            arrparsedValueImpl = new ParsedValueImpl[]{new ParsedValueImpl(new Size(0.0, SizeUnits.PERCENT), null), new ParsedValueImpl(new Size(0.0, SizeUnits.PERCENT), null)};
            arrparsedValueImpl2 = new ParsedValueImpl[]{new ParsedValueImpl(new Size(0.0, SizeUnits.PERCENT), null), new ParsedValueImpl(new Size(100.0, SizeUnits.PERCENT), null)};
        }
        if (term2 == null || term2.token == null || term2.token.getText().isEmpty()) {
            this.error(term3, "Expected '<cycle-method>' or '<color-stop>'");
        }
        CycleMethod cycleMethod = CycleMethod.NO_CYCLE;
        if ("reflect".equalsIgnoreCase(term2.token.getText())) {
            cycleMethod = CycleMethod.REFLECT;
            term3 = term2;
            term2 = term2.nextArg;
        } else if ("repeat".equalsIgnoreCase(term2.token.getText())) {
            cycleMethod = CycleMethod.REFLECT;
            term3 = term2;
            term2 = term2.nextArg;
        }
        if (term2 == null || term2.token == null || term2.token.getText().isEmpty()) {
            this.error(term3, "Expected '<color-stop>'");
        }
        ParsedValueImpl<ParsedValue[], Stop>[] arrparsedValueImpl3 = this.parseColorStops(term2);
        ParsedValueImpl[] arrparsedValueImpl4 = new ParsedValueImpl[5 + arrparsedValueImpl3.length];
        n = 0;
        arrparsedValueImpl4[n++] = arrparsedValueImpl != null ? arrparsedValueImpl[0] : null;
        arrparsedValueImpl4[n++] = arrparsedValueImpl != null ? arrparsedValueImpl[1] : null;
        arrparsedValueImpl4[n++] = arrparsedValueImpl2 != null ? arrparsedValueImpl2[0] : null;
        arrparsedValueImpl4[n++] = arrparsedValueImpl2 != null ? arrparsedValueImpl2[1] : null;
        arrparsedValueImpl4[n++] = new ParsedValueImpl(cycleMethod.name(), new EnumConverter<CycleMethod>(CycleMethod.class));
        for (int i = 0; i < arrparsedValueImpl3.length; ++i) {
            arrparsedValueImpl4[n++] = arrparsedValueImpl3[i];
        }
        return new ParsedValueImpl<ParsedValue[], Paint>(arrparsedValueImpl4, PaintConverter.LinearGradientConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], Paint> radialGradient(Term term) throws ParseException {
        ParsedValueImpl<Object, Object> parsedValueImpl;
        Object object;
        ParsedValueImpl[] arrparsedValueImpl;
        Object object2;
        String string;
        String string2 = string = term.token != null ? term.token.getText() : null;
        if (string == null || !"radial".equalsIgnoreCase(string)) {
            this.error(term, "Expected 'radial'");
        }
        if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
            LOGGER.warning(this.formatDeprecatedMessage(term, "radial gradient"));
        }
        Term term2 = term;
        Term term3 = term;
        term2 = term2.nextInSeries;
        if (term2 == null) {
            this.error(term, "Expected 'focus-angle <number>', 'focus-distance <number>', 'center (<number>,<number>)' or '<size>'");
        }
        if (term2.token == null) {
            this.error(term2, "Expected 'focus-angle <number>', 'focus-distance <number>', 'center (<number>,<number>)' or '<size>'");
        }
        ParsedValueImpl<?, Size> parsedValueImpl2 = null;
        if (term2.token.getType() == 11 && "focus-angle".equals(object2 = term2.token.getText().toLowerCase(Locale.ROOT))) {
            term3 = term2;
            term2 = term2.nextInSeries;
            if (term2 == null) {
                this.error(term3, "Expected '<number>'");
            }
            if (term2.token == null) {
                this.error(term3, "Expected '<number>'");
            }
            parsedValueImpl2 = this.parseSize(term2);
            term3 = term2;
            term2 = term2.nextInSeries;
            if (term2 == null) {
                this.error(term3, "Expected 'focus-distance <number>', 'center (<number>,<number>)' or '<size>'");
            }
            if (term2.token == null) {
                this.error(term2, "Expected 'focus-distance <number>', 'center (<number>,<number>)' or '<size>'");
            }
        }
        object2 = null;
        if (term2.token.getType() == 11 && "focus-distance".equals(arrparsedValueImpl = term2.token.getText().toLowerCase(Locale.ROOT))) {
            term3 = term2;
            term2 = term2.nextInSeries;
            if (term2 == null) {
                this.error(term3, "Expected '<number>'");
            }
            if (term2.token == null) {
                this.error(term3, "Expected '<number>'");
            }
            object2 = this.parseSize(term2);
            term3 = term2;
            term2 = term2.nextInSeries;
            if (term2 == null) {
                this.error(term3, "Expected  'center (<number>,<number>)' or '<size>'");
            }
            if (term2.token == null) {
                this.error(term2, "Expected  'center (<number>,<number>)' or '<size>'");
            }
        }
        arrparsedValueImpl = null;
        if (term2.token.getType() == 11 && "center".equals(object = term2.token.getText().toLowerCase(Locale.ROOT))) {
            term3 = term2;
            term2 = term2.nextInSeries;
            if (term2 == null) {
                this.error(term3, "Expected '(<number>,<number>)'");
            }
            if (term2.token == null || term2.token.getType() != 34) {
                this.error(term2, "Expected '(<number>,<number>)'");
            }
            arrparsedValueImpl = this.point(term2);
            term3 = term2;
            term2 = term2.nextInSeries;
            if (term2 == null) {
                this.error(term3, "Expected '<size>'");
            }
            if (term2.token == null) {
                this.error(term2, "Expected '<size>'");
            }
        }
        object = this.parseSize(term2);
        term3 = term2;
        term2 = term2.nextInSeries;
        if (term2 == null) {
            this.error(term3, "Expected 'stops' keyword");
        }
        if (term2.token == null || term2.token.getType() != 11) {
            this.error(term2, "Expected 'stops' keyword");
        }
        if (!"stops".equalsIgnoreCase(term2.token.getText())) {
            this.error(term2, "Expected 'stops'");
        }
        term3 = term2;
        term2 = term2.nextInSeries;
        if (term2 == null) {
            this.error(term3, "Expected '(<number>, <number>)'");
        }
        int n = 0;
        Term term4 = term2;
        do {
            ++n;
        } while ((term4 = term4.nextInSeries) != null && term4.token != null && term4.token.getType() == 34);
        ParsedValueImpl[] arrparsedValueImpl2 = new ParsedValueImpl[n];
        int n2 = 0;
        do {
            if ((parsedValueImpl = this.stop(term2)) != null) {
                arrparsedValueImpl2[n2++] = parsedValueImpl;
            }
            term3 = term2;
        } while ((term2 = term2.nextInSeries) != null && term2.token.getType() == 34);
        parsedValueImpl = this.cycleMethod(term2);
        if (parsedValueImpl == null) {
            parsedValueImpl = new ParsedValueImpl(CycleMethod.NO_CYCLE.name(), new EnumConverter<CycleMethod>(CycleMethod.class));
            if (term2 != null) {
                term.nextInSeries = term2;
            } else {
                term.nextInSeries = null;
                term.nextLayer = term3.nextLayer;
            }
        } else {
            term.nextInSeries = term2.nextInSeries;
            term.nextLayer = term2.nextLayer;
        }
        ParsedValueImpl[] arrparsedValueImpl3 = new ParsedValueImpl[6 + arrparsedValueImpl2.length];
        int n3 = 0;
        arrparsedValueImpl3[n3++] = parsedValueImpl2;
        arrparsedValueImpl3[n3++] = object2;
        arrparsedValueImpl3[n3++] = arrparsedValueImpl != null ? arrparsedValueImpl[0] : null;
        arrparsedValueImpl3[n3++] = arrparsedValueImpl != null ? arrparsedValueImpl[1] : null;
        arrparsedValueImpl3[n3++] = object;
        arrparsedValueImpl3[n3++] = parsedValueImpl;
        for (int i = 0; i < arrparsedValueImpl2.length; ++i) {
            arrparsedValueImpl3[n3++] = arrparsedValueImpl2[i];
        }
        return new ParsedValueImpl<ParsedValue[], Paint>(arrparsedValueImpl3, PaintConverter.RadialGradientConverter.getInstance());
    }

    private ParsedValueImpl parseRadialGradient(Term term) throws ParseException {
        ParsedValueImpl<ParsedValue[], Stop>[] arrparsedValueImpl;
        Object object;
        Term term2;
        String string;
        String string2 = string = term.token != null ? term.token.getText() : null;
        if (!"radial-gradient".regionMatches(true, 0, string, 0, 15)) {
            this.error(term, "Expected 'radial-gradient'");
        }
        if ((term2 = term.firstArg) == null || term2.token == null || term2.token.getText().isEmpty()) {
            this.error(term, "Expected 'focus-angle <angle>' or 'focus-distance <percentage>' or 'center <point>' or 'radius [<length> | <percentage>]'");
        }
        Term term3 = term2;
        ParsedValueImpl parsedValueImpl = null;
        ParsedValueImpl parsedValueImpl2 = null;
        ParsedValueImpl[] arrparsedValueImpl2 = null;
        ParsedValueImpl<?, Size> parsedValueImpl3 = null;
        if ("focus-angle".equalsIgnoreCase(term2.token.getText())) {
            term3 = term2;
            term2 = term2.nextInSeries;
            if (term2 == null || !this.isSize(term2.token)) {
                this.error(term3, "Expected '<angle>'");
            }
            object = this.size(term2.token);
            switch (((Size)object).getUnits()) {
                case DEG: 
                case RAD: 
                case GRAD: 
                case TURN: 
                case PX: {
                    break;
                }
                default: {
                    this.error(term2, "Expected [deg | rad | grad | turn ]");
                }
            }
            parsedValueImpl = new ParsedValueImpl(object, null);
            term3 = term2;
            term2 = term2.nextArg;
            if (term2 == null) {
                this.error(term3, "Expected 'focus-distance <percentage>' or 'center <point>' or 'radius [<length> | <percentage>]'");
            }
        }
        if ("focus-distance".equalsIgnoreCase(term2.token.getText())) {
            term3 = term2;
            term2 = term2.nextInSeries;
            if (term2 == null || !this.isSize(term2.token)) {
                this.error(term3, "Expected '<percentage>'");
            }
            object = this.size(term2.token);
            switch (((Size)object).getUnits()) {
                case PERCENT: {
                    break;
                }
                default: {
                    this.error(term2, "Expected '%'");
                }
            }
            parsedValueImpl2 = new ParsedValueImpl(object, null);
            term3 = term2;
            term2 = term2.nextArg;
            if (term2 == null) {
                this.error(term3, "Expected 'center <center>' or 'radius <length>'");
            }
        }
        if ("center".equalsIgnoreCase(term2.token.getText())) {
            term3 = term2;
            term2 = term2.nextInSeries;
            if (term2 == null) {
                this.error(term3, "Expected '<point>'");
            }
            object = this.parseSize(term2);
            term3 = term2;
            term2 = term2.nextInSeries;
            if (term2 == null) {
                this.error(term3, "Expected '<point>'");
            }
            arrparsedValueImpl = this.parseSize(term2);
            arrparsedValueImpl2 = new ParsedValueImpl[]{object, arrparsedValueImpl};
            term3 = term2;
            term2 = term2.nextArg;
            if (term2 == null) {
                this.error(term3, "Expected 'radius [<length> | <percentage>]'");
            }
        }
        if ("radius".equalsIgnoreCase(term2.token.getText())) {
            term3 = term2;
            term2 = term2.nextInSeries;
            if (term2 == null || !this.isSize(term2.token)) {
                this.error(term3, "Expected '[<length> | <percentage>]'");
            }
            parsedValueImpl3 = this.parseSize(term2);
            term3 = term2;
            term2 = term2.nextArg;
            if (term2 == null) {
                this.error(term3, "Expected 'radius [<length> | <percentage>]'");
            }
        }
        object = CycleMethod.NO_CYCLE;
        if ("reflect".equalsIgnoreCase(term2.token.getText())) {
            object = CycleMethod.REFLECT;
            term3 = term2;
            term2 = term2.nextArg;
        } else if ("repeat".equalsIgnoreCase(term2.token.getText())) {
            object = CycleMethod.REFLECT;
            term3 = term2;
            term2 = term2.nextArg;
        }
        if (term2 == null || term2.token == null || term2.token.getText().isEmpty()) {
            this.error(term3, "Expected '<color-stop>'");
        }
        arrparsedValueImpl = this.parseColorStops(term2);
        ParsedValueImpl[] arrparsedValueImpl3 = new ParsedValueImpl[6 + arrparsedValueImpl.length];
        int n = 0;
        arrparsedValueImpl3[n++] = parsedValueImpl;
        arrparsedValueImpl3[n++] = parsedValueImpl2;
        arrparsedValueImpl3[n++] = arrparsedValueImpl2 != null ? arrparsedValueImpl2[0] : null;
        arrparsedValueImpl3[n++] = arrparsedValueImpl2 != null ? arrparsedValueImpl2[1] : null;
        arrparsedValueImpl3[n++] = parsedValueImpl3;
        arrparsedValueImpl3[n++] = new ParsedValueImpl(object.name(), new EnumConverter<CycleMethod>(CycleMethod.class));
        for (int i = 0; i < arrparsedValueImpl.length; ++i) {
            arrparsedValueImpl3[n++] = arrparsedValueImpl[i];
        }
        return new ParsedValueImpl<ParsedValue[], Paint>(arrparsedValueImpl3, PaintConverter.RadialGradientConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], Paint> parseImagePattern(Term term) throws ParseException {
        Token token;
        Term term2;
        String string;
        String string2 = string = term.token != null ? term.token.getText() : null;
        if (!"image-pattern".regionMatches(true, 0, string, 0, 13)) {
            this.error(term, "Expected 'image-pattern'");
        }
        if ((term2 = term.firstArg) == null || term2.token == null || term2.token.getText().isEmpty()) {
            this.error(term, "Expected '<uri-string>'");
        }
        Term term3 = term2;
        String string3 = term2.token.getText();
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[]{new ParsedValueImpl<String, String>(string3, StringConverter.getInstance()), null};
        ParsedValueImpl<ParsedValue[], String> parsedValueImpl = new ParsedValueImpl<ParsedValue[], String>(arrparsedValueImpl, URLConverter.getInstance());
        if (term2.nextArg == null) {
            ParsedValueImpl[] arrparsedValueImpl2 = new ParsedValueImpl[]{parsedValueImpl};
            return new ParsedValueImpl<ParsedValue[], Paint>(arrparsedValueImpl2, PaintConverter.ImagePatternConverter.getInstance());
        }
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<size>'");
        }
        ParsedValueImpl<?, Size> parsedValueImpl2 = this.parseSize(term2);
        term3 = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<size>'");
        }
        ParsedValueImpl<?, Size> parsedValueImpl3 = this.parseSize(term2);
        term3 = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<size>'");
        }
        ParsedValueImpl<?, Size> parsedValueImpl4 = this.parseSize(term2);
        term3 = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<size>'");
        }
        ParsedValueImpl<?, Size> parsedValueImpl5 = this.parseSize(term2);
        if (term2.nextArg == null) {
            ParsedValueImpl[] arrparsedValueImpl3 = new ParsedValueImpl[]{parsedValueImpl, parsedValueImpl2, parsedValueImpl3, parsedValueImpl4, parsedValueImpl5};
            return new ParsedValueImpl<ParsedValue[], Paint>(arrparsedValueImpl3, PaintConverter.ImagePatternConverter.getInstance());
        }
        term3 = term2;
        term2 = term2.nextArg;
        if (term2 == null) {
            this.error(term3, "Expected '<boolean>'");
        }
        if ((token = term2.token) == null || token.getText() == null) {
            this.error(term2, "Expected '<boolean>'");
        }
        ParsedValueImpl[] arrparsedValueImpl4 = new ParsedValueImpl[]{parsedValueImpl, parsedValueImpl2, parsedValueImpl3, parsedValueImpl4, parsedValueImpl5, new ParsedValueImpl(Boolean.parseBoolean(token.getText()), null)};
        return new ParsedValueImpl<ParsedValue[], Paint>(arrparsedValueImpl4, PaintConverter.ImagePatternConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], Paint> parseRepeatingImagePattern(Term term) throws ParseException {
        Term term2;
        String string;
        String string2 = string = term.token != null ? term.token.getText() : null;
        if (!"repeating-image-pattern".regionMatches(true, 0, string, 0, 23)) {
            this.error(term, "Expected 'repeating-image-pattern'");
        }
        if ((term2 = term.firstArg) == null || term2.token == null || term2.token.getText().isEmpty()) {
            this.error(term, "Expected '<uri-string>'");
        }
        String string3 = term2.token.getText();
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[]{new ParsedValueImpl<String, String>(string3, StringConverter.getInstance()), null};
        ParsedValueImpl<ParsedValue[], String> parsedValueImpl = new ParsedValueImpl<ParsedValue[], String>(arrparsedValueImpl, URLConverter.getInstance());
        ParsedValueImpl[] arrparsedValueImpl2 = new ParsedValueImpl[]{parsedValueImpl};
        return new ParsedValueImpl<ParsedValue[], Paint>(arrparsedValueImpl2, PaintConverter.RepeatingImagePatternConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<?, Paint>[], Paint[]> parsePaintLayers(Term term) throws ParseException {
        int n = this.numberOfLayers(term);
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[n];
        Term term2 = term;
        int n2 = 0;
        do {
            if (term2.token == null || term2.token.getText() == null || term2.token.getText().isEmpty()) {
                this.error(term2, "Expected '<paint>'");
            }
            arrparsedValueImpl[n2++] = this.parse(term2);
        } while ((term2 = this.nextLayer(term2)) != null);
        return new ParsedValueImpl<ParsedValue<?, Paint>[], Paint[]>(arrparsedValueImpl, PaintConverter.SequenceConverter.getInstance());
    }

    private ParsedValueImpl<?, Size>[] parseSize1to4(Term term) throws ParseException {
        Term term2 = term;
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[4];
        int n = 0;
        while (n < 4 && term2 != null) {
            arrparsedValueImpl[n++] = this.parseSize(term2);
            term2 = term2.nextInSeries;
        }
        if (n < 2) {
            arrparsedValueImpl[1] = arrparsedValueImpl[0];
        }
        if (n < 3) {
            arrparsedValueImpl[2] = arrparsedValueImpl[0];
        }
        if (n < 4) {
            arrparsedValueImpl[3] = arrparsedValueImpl[1];
        }
        return arrparsedValueImpl;
    }

    private ParsedValueImpl<ParsedValue<ParsedValue[], Insets>[], Insets[]> parseInsetsLayers(Term term) throws ParseException {
        int n = this.numberOfLayers(term);
        Term term2 = term;
        int n2 = 0;
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[n];
        while (term2 != null) {
            ParsedValueImpl<?, Size>[] arrparsedValueImpl2 = this.parseSize1to4(term2);
            arrparsedValueImpl[n2++] = new ParsedValueImpl<ParsedValue[], Insets>(arrparsedValueImpl2, InsetsConverter.getInstance());
            while (term2.nextInSeries != null) {
                term2 = term2.nextInSeries;
            }
            term2 = this.nextLayer(term2);
        }
        return new ParsedValueImpl<ParsedValue<ParsedValue[], Insets>[], Insets[]>(arrparsedValueImpl, InsetsConverter.SequenceConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], Insets> parseInsetsLayer(Term term) throws ParseException {
        Term term2 = term;
        ParsedValueImpl<ParsedValue[], Insets> parsedValueImpl = null;
        while (term2 != null) {
            ParsedValueImpl<?, Size>[] arrparsedValueImpl = this.parseSize1to4(term2);
            parsedValueImpl = new ParsedValueImpl<ParsedValue[], Insets>(arrparsedValueImpl, InsetsConverter.getInstance());
            while (term2.nextInSeries != null) {
                term2 = term2.nextInSeries;
            }
            term2 = this.nextLayer(term2);
        }
        return parsedValueImpl;
    }

    private ParsedValueImpl<ParsedValue<ParsedValue[], Margins>[], Margins[]> parseMarginsLayers(Term term) throws ParseException {
        int n = this.numberOfLayers(term);
        Term term2 = term;
        int n2 = 0;
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[n];
        while (term2 != null) {
            ParsedValueImpl<?, Size>[] arrparsedValueImpl2 = this.parseSize1to4(term2);
            arrparsedValueImpl[n2++] = new ParsedValueImpl<ParsedValue[], Margins>(arrparsedValueImpl2, Margins.Converter.getInstance());
            while (term2.nextInSeries != null) {
                term2 = term2.nextInSeries;
            }
            term2 = this.nextLayer(term2);
        }
        return new ParsedValueImpl<ParsedValue<ParsedValue[], Margins>[], Margins[]>(arrparsedValueImpl, Margins.SequenceConverter.getInstance());
    }

    private ParsedValueImpl<Size, Size>[] parseSizeSeries(Term term) throws ParseException {
        if (term.token == null) {
            this.error(term, "Parse error");
        }
        ArrayList arrayList = new ArrayList();
        Term term2 = term;
        while (term2 != null) {
            Token token = term2.token;
            int n = token.getType();
            switch (n) {
                case 13: 
                case 14: 
                case 15: 
                case 16: 
                case 17: 
                case 18: 
                case 19: 
                case 20: 
                case 21: 
                case 22: 
                case 23: 
                case 24: 
                case 25: 
                case 26: {
                    ParsedValueImpl parsedValueImpl = new ParsedValueImpl(this.size(token), null);
                    arrayList.add(parsedValueImpl);
                    break;
                }
                default: {
                    this.error(term, "expected series of <size>");
                }
            }
            term2 = term2.nextInSeries;
        }
        return arrayList.toArray((T[])new ParsedValueImpl[arrayList.size()]);
    }

    private ParsedValueImpl<ParsedValue<ParsedValue<?, Size>[][], CornerRadii>[], CornerRadii[]> parseCornerRadius(Term term) throws ParseException {
        int n = this.numberOfLayers(term);
        Term term2 = term;
        int n2 = 0;
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[n];
        while (term2 != null) {
            int n3;
            int n4 = 0;
            Term term3 = term2;
            while (term3 != null) {
                if (term3.token.getType() == 32) {
                    term3 = term3.nextInSeries;
                    break;
                }
                ++n4;
                term3 = term3.nextInSeries;
            }
            int n5 = 0;
            while (term3 != null) {
                if (term3.token.getType() == 32) {
                    this.error(term3, "unexpected SOLIDUS");
                    break;
                }
                ++n5;
                term3 = term3.nextInSeries;
            }
            if (n4 == 0 || n4 > 4 || n5 > 4) {
                this.error(term, "expected [<length>|<percentage>]{1,4} [/ [<length>|<percentage>]{1,4}]?");
            }
            int n6 = 0;
            ParsedValueImpl[][] arrparsedValueImpl2 = new ParsedValueImpl[2][4];
            ParsedValueImpl parsedValueImpl = new ParsedValueImpl(new Size(0.0, SizeUnits.PX), null);
            for (n3 = 0; n3 < 4; ++n3) {
                arrparsedValueImpl2[0][n3] = parsedValueImpl;
                arrparsedValueImpl2[1][n3] = parsedValueImpl;
            }
            n3 = 0;
            int n7 = 0;
            Term term4 = term2;
            while (n3 <= 4 && n7 <= 4 && term2 != null) {
                if (term2.token.getType() == 32) {
                    ++n6;
                } else {
                    ParsedValueImpl<?, Size> parsedValueImpl2 = this.parseSize(term2);
                    if (n6 == 0) {
                        arrparsedValueImpl2[n6][n3++] = parsedValueImpl2;
                    } else {
                        arrparsedValueImpl2[n6][n7++] = parsedValueImpl2;
                    }
                }
                term4 = term2;
                term2 = term2.nextInSeries;
            }
            if (n3 != 0) {
                if (n3 < 2) {
                    arrparsedValueImpl2[0][1] = arrparsedValueImpl2[0][0];
                }
                if (n3 < 3) {
                    arrparsedValueImpl2[0][2] = arrparsedValueImpl2[0][0];
                }
                if (n3 < 4) {
                    arrparsedValueImpl2[0][3] = arrparsedValueImpl2[0][1];
                }
            } else assert (false);
            if (n7 != 0) {
                if (n7 < 2) {
                    arrparsedValueImpl2[1][1] = arrparsedValueImpl2[1][0];
                }
                if (n7 < 3) {
                    arrparsedValueImpl2[1][2] = arrparsedValueImpl2[1][0];
                }
                if (n7 < 4) {
                    arrparsedValueImpl2[1][3] = arrparsedValueImpl2[1][1];
                }
            } else {
                arrparsedValueImpl2[1][0] = arrparsedValueImpl2[0][0];
                arrparsedValueImpl2[1][1] = arrparsedValueImpl2[0][1];
                arrparsedValueImpl2[1][2] = arrparsedValueImpl2[0][2];
                arrparsedValueImpl2[1][3] = arrparsedValueImpl2[0][3];
            }
            if (parsedValueImpl.equals((Object)arrparsedValueImpl2[0][0]) || parsedValueImpl.equals((Object)arrparsedValueImpl2[1][0])) {
                ParsedValueImpl parsedValueImpl3 = parsedValueImpl;
                arrparsedValueImpl2[0][0] = parsedValueImpl3;
                arrparsedValueImpl2[1][0] = parsedValueImpl3;
            }
            if (parsedValueImpl.equals((Object)arrparsedValueImpl2[0][1]) || parsedValueImpl.equals((Object)arrparsedValueImpl2[1][1])) {
                ParsedValueImpl parsedValueImpl4 = parsedValueImpl;
                arrparsedValueImpl2[0][1] = parsedValueImpl4;
                arrparsedValueImpl2[1][1] = parsedValueImpl4;
            }
            if (parsedValueImpl.equals((Object)arrparsedValueImpl2[0][2]) || parsedValueImpl.equals((Object)arrparsedValueImpl2[1][2])) {
                ParsedValueImpl parsedValueImpl5 = parsedValueImpl;
                arrparsedValueImpl2[0][2] = parsedValueImpl5;
                arrparsedValueImpl2[1][2] = parsedValueImpl5;
            }
            if (parsedValueImpl.equals((Object)arrparsedValueImpl2[0][3]) || parsedValueImpl.equals((Object)arrparsedValueImpl2[1][3])) {
                ParsedValueImpl parsedValueImpl6 = parsedValueImpl;
                arrparsedValueImpl2[0][3] = parsedValueImpl6;
                arrparsedValueImpl2[1][3] = parsedValueImpl6;
            }
            arrparsedValueImpl[n2++] = new ParsedValueImpl(arrparsedValueImpl2, null);
            term2 = this.nextLayer(term4);
        }
        return new ParsedValueImpl<ParsedValue<ParsedValue<?, Size>[][], CornerRadii>[], CornerRadii[]>(arrparsedValueImpl, CornerRadiiConverter.getInstance());
    }

    private static boolean isPositionKeyWord(String string) {
        return "center".equalsIgnoreCase(string) || "top".equalsIgnoreCase(string) || "bottom".equalsIgnoreCase(string) || "left".equalsIgnoreCase(string) || "right".equalsIgnoreCase(string);
    }

    private ParsedValueImpl<ParsedValue[], BackgroundPosition> parseBackgroundPosition(Term term) throws ParseException {
        ParsedValueImpl[] arrparsedValueImpl;
        ParsedValueImpl<Size, Size> parsedValueImpl;
        Object object;
        Object object2;
        Object object3;
        Object object4;
        if (term.token == null || term.token.getText() == null || term.token.getText().isEmpty()) {
            this.error(term, "Expected '<bg-position>'");
        }
        Object object5 = term;
        Object object6 = term.token;
        Object object7 = ((Term)object5).nextInSeries;
        Object object8 = object7 != null ? ((Term)object7).token : null;
        Object object9 = object7 != null ? ((Term)object7).nextInSeries : null;
        Object object10 = object9 != null ? ((Term)object9).token : null;
        Object object11 = object9 != null ? ((Term)object9).nextInSeries : null;
        Token token = object4 = object11 != null ? ((Term)object11).token : null;
        if (object6 != null && object8 != null && object10 == null && object4 == null) {
            object3 = object6.getText();
            object2 = object8.getText();
            if (("top".equals(object3) || "bottom".equals(object3)) && ("left".equals(object2) || "right".equals(object2) || "center".equals(object2))) {
                object = object8;
                object8 = object6;
                object6 = object;
                object = object7;
                object7 = object5;
                object5 = object;
            }
        } else if (object6 != null && object8 != null && object10 != null) {
            object3 = null;
            object2 = null;
            if (object4 != null) {
                if (("top".equals(object6.getText()) || "bottom".equals(object6.getText())) && ("left".equals(object10.getText()) || "right".equals(object10.getText()))) {
                    object3 = new Term[]{object9, object11, object5, object7};
                    object2 = new Token[]{object10, object4, object6, object8};
                }
            } else if ("top".equals(object6.getText()) || "bottom".equals(object6.getText())) {
                if ("left".equals(object8.getText()) || "right".equals(object8.getText())) {
                    object3 = new Term[]{object7, object9, object5, null};
                    object2 = new Token[]{object8, object10, object6, null};
                } else {
                    object3 = new Term[]{object9, object5, object7, null};
                    object2 = new Token[]{object10, object6, object8, null};
                }
            }
            if (object3 != null) {
                object5 = object3[0];
                object7 = object3[1];
                object9 = object3[2];
                object11 = object3[3];
                object6 = object2[0];
                object8 = object2[1];
                object10 = object2[2];
                object4 = object2[3];
            }
        }
        object = parsedValueImpl = ZERO_PERCENT;
        object2 = parsedValueImpl;
        object3 = parsedValueImpl;
        if (object6 == null && object8 == null && object10 == null && object4 == null) {
            this.error(term, "No value found for background-position");
        } else if (object6 != null && object8 == null && object10 == null && object4 == null) {
            arrparsedValueImpl = object6.getText();
            if ("center".equals(arrparsedValueImpl)) {
                parsedValueImpl = FIFTY_PERCENT;
                object2 = ZERO_PERCENT;
                object3 = FIFTY_PERCENT;
                object = ZERO_PERCENT;
            } else if ("left".equals(arrparsedValueImpl)) {
                parsedValueImpl = ZERO_PERCENT;
                object2 = ZERO_PERCENT;
                object3 = FIFTY_PERCENT;
                object = ZERO_PERCENT;
            } else if ("right".equals(arrparsedValueImpl)) {
                parsedValueImpl = ONE_HUNDRED_PERCENT;
                object2 = ZERO_PERCENT;
                object3 = FIFTY_PERCENT;
                object = ZERO_PERCENT;
            } else if ("top".equals(arrparsedValueImpl)) {
                parsedValueImpl = FIFTY_PERCENT;
                object2 = ZERO_PERCENT;
                object3 = ZERO_PERCENT;
                object = ZERO_PERCENT;
            } else if ("bottom".equals(arrparsedValueImpl)) {
                parsedValueImpl = FIFTY_PERCENT;
                object2 = ZERO_PERCENT;
                object3 = ONE_HUNDRED_PERCENT;
                object = ZERO_PERCENT;
            } else {
                parsedValueImpl = this.parseSize((Term)object5);
                object2 = ZERO_PERCENT;
                object3 = FIFTY_PERCENT;
                object = ZERO_PERCENT;
            }
        } else if (object6 != null && object8 != null && object10 == null && object4 == null) {
            arrparsedValueImpl = object6.getText().toLowerCase(Locale.ROOT);
            String string = object8.getText().toLowerCase(Locale.ROOT);
            if (!CSSParser.isPositionKeyWord((String)arrparsedValueImpl)) {
                parsedValueImpl = this.parseSize((Term)object5);
                object2 = ZERO_PERCENT;
                if ("top".equals(string)) {
                    object3 = ZERO_PERCENT;
                    object = ZERO_PERCENT;
                } else if ("bottom".equals(string)) {
                    object3 = ONE_HUNDRED_PERCENT;
                    object = ZERO_PERCENT;
                } else if ("center".equals(string)) {
                    object3 = FIFTY_PERCENT;
                    object = ZERO_PERCENT;
                } else if (!CSSParser.isPositionKeyWord(string)) {
                    object3 = this.parseSize((Term)object7);
                    object = ZERO_PERCENT;
                } else {
                    this.error((Term)object7, "Expected 'top', 'bottom', 'center' or <size>");
                }
            } else if (arrparsedValueImpl.equals("left") || arrparsedValueImpl.equals("right")) {
                parsedValueImpl = arrparsedValueImpl.equals("right") ? ONE_HUNDRED_PERCENT : ZERO_PERCENT;
                object2 = ZERO_PERCENT;
                if (!CSSParser.isPositionKeyWord(string)) {
                    object3 = this.parseSize((Term)object7);
                    object = ZERO_PERCENT;
                } else if (string.equals("top") || string.equals("bottom") || string.equals("center")) {
                    if (string.equals("top")) {
                        object3 = ZERO_PERCENT;
                        object = ZERO_PERCENT;
                    } else if (string.equals("center")) {
                        object3 = FIFTY_PERCENT;
                        object = ZERO_PERCENT;
                    } else {
                        object3 = ONE_HUNDRED_PERCENT;
                        object = ZERO_PERCENT;
                    }
                } else {
                    this.error((Term)object7, "Expected 'top', 'bottom', 'center' or <size>");
                }
            } else if (arrparsedValueImpl.equals("center")) {
                parsedValueImpl = FIFTY_PERCENT;
                object2 = ZERO_PERCENT;
                if (string.equals("top")) {
                    object3 = ZERO_PERCENT;
                    object = ZERO_PERCENT;
                } else if (string.equals("bottom")) {
                    object3 = ONE_HUNDRED_PERCENT;
                    object = ZERO_PERCENT;
                } else if (string.equals("center")) {
                    object3 = FIFTY_PERCENT;
                    object = ZERO_PERCENT;
                } else if (!CSSParser.isPositionKeyWord(string)) {
                    object3 = this.parseSize((Term)object7);
                    object = ZERO_PERCENT;
                } else {
                    this.error((Term)object7, "Expected 'top', 'bottom', 'center' or <size>");
                }
            }
        } else if (object6 != null && object8 != null && object10 != null && object4 == null) {
            arrparsedValueImpl = object6.getText().toLowerCase(Locale.ROOT);
            String string = object8.getText().toLowerCase(Locale.ROOT);
            String string2 = object10.getText().toLowerCase(Locale.ROOT);
            if (!CSSParser.isPositionKeyWord((String)arrparsedValueImpl) || "center".equals(arrparsedValueImpl)) {
                parsedValueImpl = "center".equals(arrparsedValueImpl) ? FIFTY_PERCENT : this.parseSize((Term)object5);
                object2 = ZERO_PERCENT;
                if (!CSSParser.isPositionKeyWord(string2)) {
                    if ("top".equals(string)) {
                        object3 = this.parseSize((Term)object9);
                        object = ZERO_PERCENT;
                    } else if ("bottom".equals(string)) {
                        object3 = ZERO_PERCENT;
                        object = this.parseSize((Term)object9);
                    } else {
                        this.error((Term)object7, "Expected 'top' or 'bottom'");
                    }
                } else {
                    this.error((Term)object9, "Expected <size>");
                }
            } else if ("left".equals(arrparsedValueImpl) || "right".equals(arrparsedValueImpl)) {
                if (!CSSParser.isPositionKeyWord(string)) {
                    if ("left".equals(arrparsedValueImpl)) {
                        parsedValueImpl = this.parseSize((Term)object7);
                        object2 = ZERO_PERCENT;
                    } else {
                        parsedValueImpl = ZERO_PERCENT;
                        object2 = this.parseSize((Term)object7);
                    }
                    if ("top".equals(string2)) {
                        object3 = ZERO_PERCENT;
                        object = ZERO_PERCENT;
                    } else if ("bottom".equals(string2)) {
                        object3 = ONE_HUNDRED_PERCENT;
                        object = ZERO_PERCENT;
                    } else if ("center".equals(string2)) {
                        object3 = FIFTY_PERCENT;
                        object = ZERO_PERCENT;
                    } else {
                        this.error((Term)object9, "Expected 'top', 'bottom' or 'center'");
                    }
                } else {
                    if ("left".equals(arrparsedValueImpl)) {
                        parsedValueImpl = ZERO_PERCENT;
                        object2 = ZERO_PERCENT;
                    } else {
                        parsedValueImpl = ONE_HUNDRED_PERCENT;
                        object2 = ZERO_PERCENT;
                    }
                    if (!CSSParser.isPositionKeyWord(string2)) {
                        if ("top".equals(string)) {
                            object3 = this.parseSize((Term)object9);
                            object = ZERO_PERCENT;
                        } else if ("bottom".equals(string)) {
                            object3 = ZERO_PERCENT;
                            object = this.parseSize((Term)object9);
                        } else {
                            this.error((Term)object7, "Expected 'top' or 'bottom'");
                        }
                    } else {
                        this.error((Term)object9, "Expected <size>");
                    }
                }
            }
        } else {
            arrparsedValueImpl = object6.getText().toLowerCase(Locale.ROOT);
            String string = object8.getText().toLowerCase(Locale.ROOT);
            String string3 = object10.getText().toLowerCase(Locale.ROOT);
            String string4 = object4.getText().toLowerCase(Locale.ROOT);
            if ((arrparsedValueImpl.equals("left") || arrparsedValueImpl.equals("right")) && (string3.equals("top") || string3.equals("bottom")) && !CSSParser.isPositionKeyWord(string) && !CSSParser.isPositionKeyWord(string4)) {
                if (arrparsedValueImpl.equals("left")) {
                    parsedValueImpl = this.parseSize((Term)object7);
                    object2 = ZERO_PERCENT;
                } else {
                    parsedValueImpl = ZERO_PERCENT;
                    object2 = this.parseSize((Term)object7);
                }
                if (string3.equals("top")) {
                    object3 = this.parseSize((Term)object11);
                    object = ZERO_PERCENT;
                } else {
                    object3 = ZERO_PERCENT;
                    object = this.parseSize((Term)object11);
                }
            } else {
                this.error(term, "Expected 'left' or 'right' followed by <size> followed by 'top' or 'bottom' followed by <size>");
            }
        }
        arrparsedValueImpl = new ParsedValueImpl[]{object3, object2, object, parsedValueImpl};
        return new ParsedValueImpl<ParsedValue[], BackgroundPosition>(arrparsedValueImpl, BackgroundPositionConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<ParsedValue[], BackgroundPosition>[], BackgroundPosition[]> parseBackgroundPositionLayers(Term term) throws ParseException {
        int n = this.numberOfLayers(term);
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[n];
        int n2 = 0;
        Term term2 = term;
        while (term2 != null) {
            arrparsedValueImpl[n2++] = this.parseBackgroundPosition(term2);
            term2 = this.nextLayer(term2);
        }
        return new ParsedValueImpl<ParsedValue<ParsedValue[], BackgroundPosition>[], BackgroundPosition[]>(arrparsedValueImpl, LayeredBackgroundPositionConverter.getInstance());
    }

    private ParsedValueImpl<String, BackgroundRepeat>[] parseRepeatStyle(Term term) throws ParseException {
        String string;
        BackgroundRepeat backgroundRepeat;
        BackgroundRepeat backgroundRepeat2 = backgroundRepeat = BackgroundRepeat.NO_REPEAT;
        Term term2 = term;
        if (term2.token == null || term2.token.getType() != 11 || term2.token.getText() == null || term2.token.getText().isEmpty()) {
            this.error(term2, "Expected '<repeat-style>'");
        }
        if ("repeat-x".equals(string = term2.token.getText().toLowerCase(Locale.ROOT))) {
            backgroundRepeat2 = BackgroundRepeat.REPEAT;
            backgroundRepeat = BackgroundRepeat.NO_REPEAT;
        } else if ("repeat-y".equals(string)) {
            backgroundRepeat2 = BackgroundRepeat.NO_REPEAT;
            backgroundRepeat = BackgroundRepeat.REPEAT;
        } else if ("repeat".equals(string)) {
            backgroundRepeat2 = BackgroundRepeat.REPEAT;
            backgroundRepeat = BackgroundRepeat.REPEAT;
        } else if ("space".equals(string)) {
            backgroundRepeat2 = BackgroundRepeat.SPACE;
            backgroundRepeat = BackgroundRepeat.SPACE;
        } else if ("round".equals(string)) {
            backgroundRepeat2 = BackgroundRepeat.ROUND;
            backgroundRepeat = BackgroundRepeat.ROUND;
        } else if ("no-repeat".equals(string)) {
            backgroundRepeat2 = BackgroundRepeat.NO_REPEAT;
            backgroundRepeat = BackgroundRepeat.NO_REPEAT;
        } else if ("stretch".equals(string)) {
            backgroundRepeat2 = BackgroundRepeat.NO_REPEAT;
            backgroundRepeat = BackgroundRepeat.NO_REPEAT;
        } else {
            this.error(term2, "Expected  '<repeat-style>' " + string);
        }
        term2 = term2.nextInSeries;
        if (term2 != null && term2.token != null && term2.token.getType() == 11 && term2.token.getText() != null && !term2.token.getText().isEmpty()) {
            string = term2.token.getText().toLowerCase(Locale.ROOT);
            if ("repeat-x".equals(string)) {
                this.error(term2, "Unexpected 'repeat-x'");
            } else if ("repeat-y".equals(string)) {
                this.error(term2, "Unexpected 'repeat-y'");
            } else if ("repeat".equals(string)) {
                backgroundRepeat = BackgroundRepeat.REPEAT;
            } else if ("space".equals(string)) {
                backgroundRepeat = BackgroundRepeat.SPACE;
            } else if ("round".equals(string)) {
                backgroundRepeat = BackgroundRepeat.ROUND;
            } else if ("no-repeat".equals(string)) {
                backgroundRepeat = BackgroundRepeat.NO_REPEAT;
            } else if ("stretch".equals(string)) {
                backgroundRepeat = BackgroundRepeat.NO_REPEAT;
            } else {
                this.error(term2, "Expected  '<repeat-style>'");
            }
        }
        return new ParsedValueImpl[]{new ParsedValueImpl(backgroundRepeat2.name(), new EnumConverter<BackgroundRepeat>(BackgroundRepeat.class)), new ParsedValueImpl(backgroundRepeat.name(), new EnumConverter<BackgroundRepeat>(BackgroundRepeat.class))};
    }

    private ParsedValueImpl<ParsedValue<String, BackgroundRepeat>[][], RepeatStruct[]> parseBorderImageRepeatStyleLayers(Term term) throws ParseException {
        int n = this.numberOfLayers(term);
        ParsedValueImpl[][] arrarrparsedValueImpl = new ParsedValueImpl[n][];
        int n2 = 0;
        Term term2 = term;
        while (term2 != null) {
            arrarrparsedValueImpl[n2++] = this.parseRepeatStyle(term2);
            term2 = this.nextLayer(term2);
        }
        return new ParsedValueImpl<ParsedValue<String, BackgroundRepeat>[][], RepeatStruct[]>(arrarrparsedValueImpl, RepeatStructConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<String, BackgroundRepeat>[][], RepeatStruct[]> parseBackgroundRepeatStyleLayers(Term term) throws ParseException {
        int n = this.numberOfLayers(term);
        ParsedValueImpl[][] arrarrparsedValueImpl = new ParsedValueImpl[n][];
        int n2 = 0;
        Term term2 = term;
        while (term2 != null) {
            arrarrparsedValueImpl[n2++] = this.parseRepeatStyle(term2);
            term2 = this.nextLayer(term2);
        }
        return new ParsedValueImpl<ParsedValue<String, BackgroundRepeat>[][], RepeatStruct[]>(arrarrparsedValueImpl, RepeatStructConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], BackgroundSize> parseBackgroundSize(Term term) throws ParseException {
        ParsedValueImpl[] arrparsedValueImpl;
        ParsedValueImpl<Size, Size> parsedValueImpl = null;
        ParsedValueImpl<Object, Size> parsedValueImpl2 = null;
        boolean bl = false;
        boolean bl2 = false;
        Term term2 = term;
        if (term2.token == null) {
            this.error(term2, "Expected '<bg-size>'");
        }
        if (term2.token.getType() == 11) {
            ParsedValueImpl[] arrparsedValueImpl2 = arrparsedValueImpl = term2.token.getText() != null ? term2.token.getText().toLowerCase(Locale.ROOT) : null;
            if (!"auto".equals(arrparsedValueImpl)) {
                if ("cover".equals(arrparsedValueImpl)) {
                    bl = true;
                } else if ("contain".equals(arrparsedValueImpl)) {
                    bl2 = true;
                } else if ("stretch".equals(arrparsedValueImpl)) {
                    parsedValueImpl2 = ONE_HUNDRED_PERCENT;
                    parsedValueImpl = ONE_HUNDRED_PERCENT;
                } else {
                    this.error(term2, "Expected 'auto', 'cover', 'contain', or  'stretch'");
                }
            }
        } else if (this.isSize(term2.token)) {
            parsedValueImpl2 = this.parseSize(term2);
            parsedValueImpl = null;
        } else {
            this.error(term2, "Expected '<bg-size>'");
        }
        term2 = term2.nextInSeries;
        if (term2 != null) {
            if (bl || bl2) {
                this.error(term2, "Unexpected '<bg-size>'");
            }
            if (term2.token.getType() == 11) {
                ParsedValueImpl[] arrparsedValueImpl3 = arrparsedValueImpl = term2.token.getText() != null ? term2.token.getText().toLowerCase(Locale.ROOT) : null;
                if ("auto".equals(arrparsedValueImpl)) {
                    parsedValueImpl = null;
                } else if ("cover".equals(arrparsedValueImpl)) {
                    this.error(term2, "Unexpected 'cover'");
                } else if ("contain".equals(arrparsedValueImpl)) {
                    this.error(term2, "Unexpected 'contain'");
                } else if ("stretch".equals(arrparsedValueImpl)) {
                    parsedValueImpl = ONE_HUNDRED_PERCENT;
                } else {
                    this.error(term2, "Expected 'auto' or 'stretch'");
                }
            } else if (this.isSize(term2.token)) {
                parsedValueImpl = this.parseSize(term2);
            } else {
                this.error(term2, "Expected '<bg-size>'");
            }
        }
        arrparsedValueImpl = new ParsedValueImpl[]{parsedValueImpl2, parsedValueImpl, new ParsedValueImpl<String, Boolean>(bl ? "true" : "false", BooleanConverter.getInstance()), new ParsedValueImpl<String, Boolean>(bl2 ? "true" : "false", BooleanConverter.getInstance())};
        return new ParsedValueImpl<ParsedValue[], BackgroundSize>(arrparsedValueImpl, BackgroundSizeConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<ParsedValue[], BackgroundSize>[], BackgroundSize[]> parseBackgroundSizeLayers(Term term) throws ParseException {
        int n = this.numberOfLayers(term);
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[n];
        int n2 = 0;
        Term term2 = term;
        while (term2 != null) {
            arrparsedValueImpl[n2++] = this.parseBackgroundSize(term2);
            term2 = this.nextLayer(term2);
        }
        return new ParsedValueImpl<ParsedValue<ParsedValue[], BackgroundSize>[], BackgroundSize[]>(arrparsedValueImpl, LayeredBackgroundSizeConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<?, Paint>[], Paint[]> parseBorderPaint(Term term) throws ParseException {
        Term term2 = term;
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[4];
        int n = 0;
        while (term2 != null) {
            if (term2.token == null || arrparsedValueImpl.length <= n) {
                this.error(term2, "Expected '<paint>'");
            }
            arrparsedValueImpl[n++] = this.parse(term2);
            term2 = term2.nextInSeries;
        }
        if (n < 2) {
            arrparsedValueImpl[1] = arrparsedValueImpl[0];
        }
        if (n < 3) {
            arrparsedValueImpl[2] = arrparsedValueImpl[0];
        }
        if (n < 4) {
            arrparsedValueImpl[3] = arrparsedValueImpl[1];
        }
        return new ParsedValueImpl<ParsedValue<?, Paint>[], Paint[]>(arrparsedValueImpl, StrokeBorderPaintConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<ParsedValue<?, Paint>[], Paint[]>[], Paint[][]> parseBorderPaintLayers(Term term) throws ParseException {
        int n = this.numberOfLayers(term);
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[n];
        int n2 = 0;
        Term term2 = term;
        while (term2 != null) {
            arrparsedValueImpl[n2++] = this.parseBorderPaint(term2);
            term2 = this.nextLayer(term2);
        }
        return new ParsedValueImpl<ParsedValue<ParsedValue<?, Paint>[], Paint[]>[], Paint[][]>(arrparsedValueImpl, LayeredBorderPaintConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]> parseBorderStyleSeries(Term term) throws ParseException {
        Term term2 = term;
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[4];
        int n = 0;
        while (term2 != null) {
            arrparsedValueImpl[n++] = this.parseBorderStyle(term2);
            term2 = term2.nextInSeries;
        }
        if (n < 2) {
            arrparsedValueImpl[1] = arrparsedValueImpl[0];
        }
        if (n < 3) {
            arrparsedValueImpl[2] = arrparsedValueImpl[0];
        }
        if (n < 4) {
            arrparsedValueImpl[3] = arrparsedValueImpl[1];
        }
        return new ParsedValueImpl<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]>(arrparsedValueImpl, BorderStrokeStyleSequenceConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]>[], BorderStrokeStyle[][]> parseBorderStyleLayers(Term term) throws ParseException {
        int n = this.numberOfLayers(term);
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[n];
        int n2 = 0;
        Term term2 = term;
        while (term2 != null) {
            arrparsedValueImpl[n2++] = this.parseBorderStyleSeries(term2);
            term2 = this.nextLayer(term2);
        }
        return new ParsedValueImpl<ParsedValue<ParsedValue<ParsedValue[], BorderStrokeStyle>[], BorderStrokeStyle[]>[], BorderStrokeStyle[][]>(arrparsedValueImpl, LayeredBorderStyleConverter.getInstance());
    }

    private String getKeyword(Term term) {
        if (term != null && term.token != null && term.token.getType() == 11 && term.token.getText() != null && !term.token.getText().isEmpty()) {
            return term.token.getText().toLowerCase(Locale.ROOT);
        }
        return null;
    }

    private ParsedValueImpl<ParsedValue[], BorderStrokeStyle> parseBorderStyle(Term term) throws ParseException {
        ParsedValueImpl[] arrparsedValueImpl;
        ParsedValue<ParsedValue[], Number[]> parsedValue = null;
        ParsedValueImpl parsedValueImpl = null;
        ParsedValueImpl<String, StrokeType> parsedValueImpl2 = null;
        ParsedValueImpl parsedValueImpl3 = null;
        ParsedValueImpl parsedValueImpl4 = null;
        ParsedValueImpl<String, StrokeLineCap> parsedValueImpl5 = null;
        Term term2 = term;
        parsedValue = this.dashStyle(term2);
        Term term3 = term2;
        term2 = term2.nextInSeries;
        String string = this.getKeyword(term2);
        if ("phase".equals(string)) {
            term3 = term2;
            term2 = term2.nextInSeries;
            if (term2 == null || term2.token == null || !this.isSize(term2.token)) {
                this.error(term2, "Expected '<size>'");
            }
            arrparsedValueImpl = this.parseSize(term2);
            parsedValueImpl = new ParsedValueImpl((ParsedValue<?, Size>)arrparsedValueImpl, SizeConverter.getInstance());
            term3 = term2;
            term2 = term2.nextInSeries;
        }
        if ((parsedValueImpl2 = this.parseStrokeType(term2)) != null) {
            term3 = term2;
            term2 = term2.nextInSeries;
        }
        if ("line-join".equals(string = this.getKeyword(term2))) {
            term3 = term2;
            term2 = term2.nextInSeries;
            arrparsedValueImpl = this.parseStrokeLineJoin(term2);
            if (arrparsedValueImpl != null) {
                parsedValueImpl3 = arrparsedValueImpl[0];
                parsedValueImpl4 = arrparsedValueImpl[1];
            } else {
                this.error(term2, "Expected 'miter <size>?', 'bevel' or 'round'");
            }
            term3 = term2;
            term2 = term2.nextInSeries;
            string = this.getKeyword(term2);
        }
        if ("line-cap".equals(string)) {
            term3 = term2;
            term2 = term2.nextInSeries;
            parsedValueImpl5 = this.parseStrokeLineCap(term2);
            if (parsedValueImpl5 == null) {
                this.error(term2, "Expected 'square', 'butt' or 'round'");
            }
            term3 = term2;
            term2 = term2.nextInSeries;
        }
        if (term2 != null) {
            term.nextInSeries = term2;
        } else {
            term.nextInSeries = null;
            term.nextLayer = term3.nextLayer;
        }
        arrparsedValueImpl = new ParsedValue[]{parsedValue, parsedValueImpl, parsedValueImpl2, parsedValueImpl3, parsedValueImpl4, parsedValueImpl5};
        return new ParsedValueImpl<ParsedValue[], BorderStrokeStyle>(arrparsedValueImpl, BorderStyleConverter.getInstance());
    }

    private ParsedValue<ParsedValue[], Number[]> dashStyle(Term term) throws ParseException {
        if (term.token == null) {
            this.error(term, "Expected '<dash-style>'");
        }
        int n = term.token.getType();
        ParsedValue<ParsedValue[], Number[]> parsedValue = null;
        if (n == 11) {
            parsedValue = this.borderStyle(term);
        } else if (n == 12) {
            parsedValue = this.segments(term);
        } else {
            this.error(term, "Expected '<dash-style>'");
        }
        return parsedValue;
    }

    private ParsedValue<ParsedValue[], Number[]> borderStyle(Term term) throws ParseException {
        String string;
        if (term.token == null || term.token.getType() != 11 || term.token.getText() == null || term.token.getText().isEmpty()) {
            this.error(term, "Expected '<border-style>'");
        }
        if ("none".equals(string = term.token.getText().toLowerCase(Locale.ROOT))) {
            return BorderStyleConverter.NONE;
        }
        if ("hidden".equals(string)) {
            return BorderStyleConverter.NONE;
        }
        if ("dotted".equals(string)) {
            return BorderStyleConverter.DOTTED;
        }
        if ("dashed".equals(string)) {
            return BorderStyleConverter.DASHED;
        }
        if ("solid".equals(string)) {
            return BorderStyleConverter.SOLID;
        }
        if ("double".equals(string)) {
            this.error(term, "Unsupported <border-style> 'double'");
        } else if ("groove".equals(string)) {
            this.error(term, "Unsupported <border-style> 'groove'");
        } else if ("ridge".equals(string)) {
            this.error(term, "Unsupported <border-style> 'ridge'");
        } else if ("inset".equals(string)) {
            this.error(term, "Unsupported <border-style> 'inset'");
        } else if ("outset".equals(string)) {
            this.error(term, "Unsupported <border-style> 'outset'");
        } else {
            this.error(term, "Unsupported <border-style> '" + string + "'");
        }
        return BorderStyleConverter.SOLID;
    }

    private ParsedValueImpl<ParsedValue[], Number[]> segments(Term term) throws ParseException {
        Term term2;
        String string;
        String string2 = string = term.token != null ? term.token.getText() : null;
        if (!"segments".regionMatches(true, 0, string, 0, 8)) {
            this.error(term, "Expected 'segments'");
        }
        if ((term2 = term.firstArg) == null) {
            this.error(null, "Expected '<size>'");
        }
        int n = this.numberOfArgs(term);
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[n];
        int n2 = 0;
        while (term2 != null) {
            arrparsedValueImpl[n2++] = this.parseSize(term2);
            term2 = term2.nextArg;
        }
        return new ParsedValueImpl<ParsedValue[], Number[]>(arrparsedValueImpl, SizeConverter.SequenceConverter.getInstance());
    }

    private ParsedValueImpl<String, StrokeType> parseStrokeType(Term term) throws ParseException {
        String string = this.getKeyword(term);
        if ("centered".equals(string) || "inside".equals(string) || "outside".equals(string)) {
            return new ParsedValueImpl<String, StrokeType>(string, new EnumConverter<StrokeType>(StrokeType.class));
        }
        return null;
    }

    private ParsedValueImpl[] parseStrokeLineJoin(Term term) throws ParseException {
        String string = this.getKeyword(term);
        if ("miter".equals(string) || "bevel".equals(string) || "round".equals(string)) {
            Term term2;
            ParsedValueImpl parsedValueImpl = new ParsedValueImpl(string, new EnumConverter<StrokeLineJoin>(StrokeLineJoin.class));
            ParsedValueImpl parsedValueImpl2 = null;
            if ("miter".equals(string) && (term2 = term.nextInSeries) != null && term2.token != null && this.isSize(term2.token)) {
                term.nextInSeries = term2.nextInSeries;
                ParsedValueImpl<?, Size> parsedValueImpl3 = this.parseSize(term2);
                parsedValueImpl2 = new ParsedValueImpl(parsedValueImpl3, SizeConverter.getInstance());
            }
            return new ParsedValueImpl[]{parsedValueImpl, parsedValueImpl2};
        }
        return null;
    }

    private ParsedValueImpl<String, StrokeLineCap> parseStrokeLineCap(Term term) throws ParseException {
        String string = this.getKeyword(term);
        if ("square".equals(string) || "butt".equals(string) || "round".equals(string)) {
            return new ParsedValueImpl<String, StrokeLineCap>(string, new EnumConverter<StrokeLineCap>(StrokeLineCap.class));
        }
        return null;
    }

    private ParsedValueImpl<ParsedValue[], BorderImageSlices> parseBorderImageSlice(Term term) throws ParseException {
        Term term2 = term;
        if (term2.token == null || !this.isSize(term2.token)) {
            this.error(term2, "Expected '<size>'");
        }
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[4];
        Boolean bl = Boolean.FALSE;
        int n = 0;
        while (n < 4 && term2 != null) {
            arrparsedValueImpl[n++] = this.parseSize(term2);
            term2 = term2.nextInSeries;
            if (term2 == null || term2.token == null || term2.token.getType() != 11 || !"fill".equalsIgnoreCase(term2.token.getText())) continue;
            bl = Boolean.TRUE;
            break;
        }
        if (n < 2) {
            arrparsedValueImpl[1] = arrparsedValueImpl[0];
        }
        if (n < 3) {
            arrparsedValueImpl[2] = arrparsedValueImpl[0];
        }
        if (n < 4) {
            arrparsedValueImpl[3] = arrparsedValueImpl[1];
        }
        ParsedValueImpl[] arrparsedValueImpl2 = new ParsedValueImpl[]{new ParsedValueImpl<ParsedValue[], Insets>(arrparsedValueImpl, InsetsConverter.getInstance()), new ParsedValueImpl(bl, null)};
        return new ParsedValueImpl<ParsedValue[], BorderImageSlices>(arrparsedValueImpl2, BorderImageSliceConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<ParsedValue[], BorderImageSlices>[], BorderImageSlices[]> parseBorderImageSliceLayers(Term term) throws ParseException {
        int n = this.numberOfLayers(term);
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[n];
        int n2 = 0;
        Term term2 = term;
        while (term2 != null) {
            arrparsedValueImpl[n2++] = this.parseBorderImageSlice(term2);
            term2 = this.nextLayer(term2);
        }
        return new ParsedValueImpl<ParsedValue<ParsedValue[], BorderImageSlices>[], BorderImageSlices[]>(arrparsedValueImpl, SliceSequenceConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], BorderWidths> parseBorderImageWidth(Term term) throws ParseException {
        Term term2 = term;
        if (term2.token == null || !this.isSize(term2.token)) {
            this.error(term2, "Expected '<size>'");
        }
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[4];
        int n = 0;
        while (n < 4 && term2 != null) {
            arrparsedValueImpl[n++] = this.parseSize(term2);
            term2 = term2.nextInSeries;
            if (term2 == null || term2.token == null || term2.token.getType() != 11) continue;
        }
        if (n < 2) {
            arrparsedValueImpl[1] = arrparsedValueImpl[0];
        }
        if (n < 3) {
            arrparsedValueImpl[2] = arrparsedValueImpl[0];
        }
        if (n < 4) {
            arrparsedValueImpl[3] = arrparsedValueImpl[1];
        }
        return new ParsedValueImpl<ParsedValue[], BorderWidths>(arrparsedValueImpl, BorderImageWidthConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<ParsedValue[], BorderWidths>[], BorderWidths[]> parseBorderImageWidthLayers(Term term) throws ParseException {
        int n = this.numberOfLayers(term);
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[n];
        int n2 = 0;
        Term term2 = term;
        while (term2 != null) {
            arrparsedValueImpl[n2++] = this.parseBorderImageWidth(term2);
            term2 = this.nextLayer(term2);
        }
        return new ParsedValueImpl<ParsedValue<ParsedValue[], BorderWidths>[], BorderWidths[]>(arrparsedValueImpl, BorderImageWidthsSequenceConverter.getInstance());
    }

    private ParsedValueImpl<String, String> parseRegion(Term term) throws ParseException {
        Term term2;
        String string;
        String string2 = string = term.token != null ? term.token.getText() : null;
        if (!"region".regionMatches(true, 0, string, 0, 6)) {
            this.error(term, "Expected 'region'");
        }
        if ((term2 = term.firstArg) == null) {
            this.error(term, "Expected 'region(\"<styleclass-or-id-string>\")'");
        }
        if (term2.token == null || term2.token.getType() != 10 || term2.token.getText() == null || term2.token.getText().isEmpty()) {
            this.error(term, "Expected 'region(\"<styleclass-or-id-string>\")'");
        }
        String string3 = SPECIAL_REGION_URL_PREFIX + Utils.stripQuotes(term2.token.getText());
        return new ParsedValueImpl<String, String>(string3, StringConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], String> parseURI(Term term) throws ParseException {
        if (term == null) {
            this.error(term, "Expected 'url(\"<uri-string>\")'");
        }
        if (term.token == null || term.token.getType() != 43 || term.token.getText() == null || term.token.getText().isEmpty()) {
            this.error(term, "Expected 'url(\"<uri-string>\")'");
        }
        String string = term.token.getText();
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[]{new ParsedValueImpl<String, String>(string, StringConverter.getInstance()), null};
        return new ParsedValueImpl<ParsedValue[], String>(arrparsedValueImpl, URLConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<ParsedValue[], String>[], String[]> parseURILayers(Term term) throws ParseException {
        int n = this.numberOfLayers(term);
        Term term2 = term;
        int n2 = 0;
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[n];
        while (term2 != null) {
            arrparsedValueImpl[n2++] = this.parseURI(term2);
            term2 = this.nextLayer(term2);
        }
        return new ParsedValueImpl<ParsedValue<ParsedValue[], String>[], String[]>(arrparsedValueImpl, URLConverter.SequenceConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue<?, Size>, Number> parseFontSize(Term term) throws ParseException {
        Object object;
        if (term == null) {
            return null;
        }
        Token token = term.token;
        if (token == null || !this.isSize(token)) {
            this.error(term, "Expected '<font-size>'");
        }
        Size size = null;
        if (token.getType() == 11) {
            object = token.getText().toLowerCase(Locale.ROOT);
            double d = -1.0;
            if ("inherit".equals(object)) {
                d = 100.0;
            } else if ("xx-small".equals(object)) {
                d = 60.0;
            } else if ("x-small".equals(object)) {
                d = 75.0;
            } else if ("small".equals(object)) {
                d = 80.0;
            } else if ("medium".equals(object)) {
                d = 100.0;
            } else if ("large".equals(object)) {
                d = 120.0;
            } else if ("x-large".equals(object)) {
                d = 150.0;
            } else if ("xx-large".equals(object)) {
                d = 200.0;
            } else if ("smaller".equals(object)) {
                d = 80.0;
            } else if ("larger".equals(object)) {
                d = 120.0;
            }
            if (d > -1.0) {
                size = new Size(d, SizeUnits.PERCENT);
            }
        }
        if (size == null) {
            size = this.size(token);
        }
        object = new ParsedValueImpl(size, null);
        return new ParsedValueImpl((String)object, FontConverter.FontSizeConverter.getInstance());
    }

    private ParsedValueImpl<String, FontPosture> parseFontStyle(Term term) throws ParseException {
        if (term == null) {
            return null;
        }
        Token token = term.token;
        if (token == null || token.getType() != 11 || token.getText() == null || token.getText().isEmpty()) {
            this.error(term, "Expected '<font-style>'");
        }
        String string = token.getText().toLowerCase(Locale.ROOT);
        String string2 = FontPosture.REGULAR.name();
        if ("normal".equals(string)) {
            string2 = FontPosture.REGULAR.name();
        } else if ("italic".equals(string)) {
            string2 = FontPosture.ITALIC.name();
        } else if ("oblique".equals(string)) {
            string2 = FontPosture.ITALIC.name();
        } else if ("inherit".equals(string)) {
            string2 = "inherit";
        } else {
            return null;
        }
        return new ParsedValueImpl<String, FontPosture>(string2, FontConverter.FontStyleConverter.getInstance());
    }

    private ParsedValueImpl<String, FontWeight> parseFontWeight(Term term) throws ParseException {
        if (term == null) {
            return null;
        }
        Token token = term.token;
        if (token == null || token.getText() == null || token.getText().isEmpty()) {
            this.error(term, "Expected '<font-weight>'");
        }
        String string = token.getText().toLowerCase(Locale.ROOT);
        String string2 = FontWeight.NORMAL.name();
        if ("inherit".equals(string)) {
            string2 = FontWeight.NORMAL.name();
        } else if ("normal".equals(string)) {
            string2 = FontWeight.NORMAL.name();
        } else if ("bold".equals(string)) {
            string2 = FontWeight.BOLD.name();
        } else if ("bolder".equals(string)) {
            string2 = FontWeight.BOLD.name();
        } else if ("lighter".equals(string)) {
            string2 = FontWeight.LIGHT.name();
        } else if ("100".equals(string)) {
            string2 = FontWeight.findByWeight((int)100).name();
        } else if ("200".equals(string)) {
            string2 = FontWeight.findByWeight((int)200).name();
        } else if ("300".equals(string)) {
            string2 = FontWeight.findByWeight((int)300).name();
        } else if ("400".equals(string)) {
            string2 = FontWeight.findByWeight((int)400).name();
        } else if ("500".equals(string)) {
            string2 = FontWeight.findByWeight((int)500).name();
        } else if ("600".equals(string)) {
            string2 = FontWeight.findByWeight((int)600).name();
        } else if ("700".equals(string)) {
            string2 = FontWeight.findByWeight((int)700).name();
        } else if ("800".equals(string)) {
            string2 = FontWeight.findByWeight((int)800).name();
        } else if ("900".equals(string)) {
            string2 = FontWeight.findByWeight((int)900).name();
        } else {
            this.error(term, "Expected '<font-weight>'");
        }
        return new ParsedValueImpl<String, FontWeight>(string2, FontConverter.FontWeightConverter.getInstance());
    }

    private ParsedValueImpl<String, String> parseFontFamily(Term term) throws ParseException {
        String string;
        if (term == null) {
            return null;
        }
        Token token = term.token;
        String string2 = null;
        if (token == null || token.getType() != 11 && token.getType() != 10 || (string2 = token.getText()) == null || string2.isEmpty()) {
            this.error(term, "Expected '<font-family>'");
        }
        if ("inherit".equals(string = this.stripQuotes(string2.toLowerCase(Locale.ROOT)))) {
            return new ParsedValueImpl<String, String>("inherit", StringConverter.getInstance());
        }
        if ("serif".equals(string) || "sans-serif".equals(string) || "cursive".equals(string) || "fantasy".equals(string) || "monospace".equals(string)) {
            return new ParsedValueImpl<String, String>(string, StringConverter.getInstance());
        }
        return new ParsedValueImpl<String, String>(token.getText(), StringConverter.getInstance());
    }

    private ParsedValueImpl<ParsedValue[], Font> parseFont(Term term) throws ParseException {
        ParsedValueImpl<ParsedValue<?, Size>, Number> parsedValueImpl;
        Term term2;
        Object object;
        Object object2 = term.nextInSeries;
        term.nextInSeries = null;
        while (object2 != null) {
            object = ((Term)object2).nextInSeries;
            ((Term)object2).nextInSeries = term;
            term = object2;
            object2 = object;
        }
        object = term.token;
        int n = ((Token)object).getType();
        if (n != 11 && n != 10) {
            this.error(term, "Expected '<font-family>'");
        }
        ParsedValueImpl<String, String> parsedValueImpl2 = this.parseFontFamily(term);
        Term term3 = term;
        term3 = term3.nextInSeries;
        if (term3 == null) {
            this.error(term, "Expected '<size>'");
        }
        if (term3.token == null || !this.isSize(term3.token)) {
            this.error(term3, "Expected '<size>'");
        }
        if ((term2 = term3.nextInSeries) != null && term2.token != null && term2.token.getType() == 32) {
            term = term2;
            term3 = term2.nextInSeries;
            if (term3 == null) {
                this.error(term, "Expected '<size>'");
            }
            if (term3.token == null || !this.isSize(term3.token)) {
                this.error(term3, "Expected '<size>'");
            }
            object = term3.token;
        }
        if ((parsedValueImpl = this.parseFontSize(term3)) == null) {
            this.error(term, "Expected '<size>'");
        }
        ParsedValueImpl<String, FontPosture> parsedValueImpl3 = null;
        ParsedValueImpl<String, FontWeight> parsedValueImpl4 = null;
        String string = null;
        while ((term3 = term3.nextInSeries) != null) {
            if (term3.token == null || term3.token.getType() != 11 || term3.token.getText() == null || term3.token.getText().isEmpty()) {
                this.error(term3, "Expected '<font-weight>', '<font-style>' or '<font-variant>'");
            }
            if (parsedValueImpl3 == null && (parsedValueImpl3 = this.parseFontStyle(term3)) != null) continue;
            if (string == null && "small-caps".equalsIgnoreCase(term3.token.getText())) {
                string = term3.token.getText();
                continue;
            }
            if (parsedValueImpl4 != null || (parsedValueImpl4 = this.parseFontWeight(term3)) == null) continue;
        }
        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[]{parsedValueImpl2, parsedValueImpl, parsedValueImpl4, parsedValueImpl3};
        return new ParsedValueImpl<ParsedValue[], Font>(arrparsedValueImpl, FontConverter.getInstance());
    }

    private Token nextToken(CSSLexer cSSLexer) {
        Token token = null;
        while ((token = cSSLexer.nextToken()) != null && token.getType() == 40 || token.getType() == 41) {
        }
        if (LOGGER.isLoggable(PlatformLogger.Level.FINEST)) {
            LOGGER.finest(token.toString());
        }
        return token;
    }

    private void parse(Stylesheet stylesheet, CSSLexer cSSLexer) {
        Object object;
        String string;
        List<Selector> list;
        this.currentToken = this.nextToken(cSSLexer);
        while (this.currentToken != null && this.currentToken.getType() == 47) {
            this.currentToken = this.nextToken(cSSLexer);
            if (this.currentToken == null || this.currentToken.getType() != 11) {
                list = new ParseException("Expected IDENT", this.currentToken, this);
                String string2 = ((ParseException)((Object)list)).toString();
                CssError cssError = this.createError(string2);
                if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                    LOGGER.warning(cssError.toString());
                }
                this.reportError(cssError);
                do {
                    this.currentToken = cSSLexer.nextToken();
                } while (this.currentToken != null && this.currentToken.getType() == 30 || this.currentToken.getType() == 40 || this.currentToken.getType() == 41);
                continue;
            }
            list = this.currentToken.getText().toLowerCase(Locale.ROOT);
            if ("font-face".equals(list)) {
                FontFace fontFace = this.fontFace(cSSLexer);
                if (fontFace != null) {
                    stylesheet.getFontFaces().add(fontFace);
                }
                this.currentToken = this.nextToken(cSSLexer);
                continue;
            }
            if (!"import".equals(list)) continue;
            if (imports == null) {
                imports = new Stack();
            }
            if (!imports.contains(this.sourceOfStylesheet)) {
                imports.push(this.sourceOfStylesheet);
                Stylesheet stylesheet2 = this.handleImport(cSSLexer);
                if (stylesheet2 != null) {
                    stylesheet.importStylesheet(stylesheet2);
                }
                imports.pop();
                if (imports.isEmpty()) {
                    imports = null;
                }
            } else {
                int n = this.currentToken.getLine();
                int n2 = this.currentToken.getOffset();
                string = MessageFormat.format("Recursive @import at {2} [{0,number,#},{1,number,#}]", n, n2, imports.peek());
                object = this.createError(string);
                if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                    LOGGER.warning(((CssError)object).toString());
                }
                this.reportError((CssError)object);
            }
            do {
                this.currentToken = cSSLexer.nextToken();
            } while (this.currentToken != null && this.currentToken.getType() == 30 || this.currentToken.getType() == 40 || this.currentToken.getType() == 41);
        }
        while (this.currentToken != null && this.currentToken.getType() != -1) {
            list = this.selectors(cSSLexer);
            if (list == null) {
                return;
            }
            if (this.currentToken == null || this.currentToken.getType() != 28) {
                int n = this.currentToken != null ? this.currentToken.getLine() : -1;
                int n3 = this.currentToken != null ? this.currentToken.getOffset() : -1;
                string = MessageFormat.format("Expected LBRACE at [{0,number,#},{1,number,#}]", n, n3);
                object = this.createError(string);
                if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                    LOGGER.warning(((CssError)object).toString());
                }
                this.reportError((CssError)object);
                this.currentToken = null;
                return;
            }
            this.currentToken = this.nextToken(cSSLexer);
            List<Declaration> list2 = this.declarations(cSSLexer);
            if (list2 == null) {
                return;
            }
            if (this.currentToken != null && this.currentToken.getType() != 29) {
                int n = this.currentToken.getLine();
                int n4 = this.currentToken.getOffset();
                object = MessageFormat.format("Expected RBRACE at [{0,number,#},{1,number,#}]", n, n4);
                CssError cssError = this.createError((String)object);
                if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                    LOGGER.warning(cssError.toString());
                }
                this.reportError(cssError);
                this.currentToken = null;
                return;
            }
            stylesheet.getRules().add(new Rule(list, list2));
            this.currentToken = this.nextToken(cSSLexer);
        }
        this.currentToken = null;
    }

    private FontFace fontFace(CSSLexer cSSLexer) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        ArrayList<FontFace.FontFaceSrc> arrayList = new ArrayList<FontFace.FontFaceSrc>();
        do {
            this.currentToken = this.nextToken(cSSLexer);
            if (this.currentToken.getType() != 11) continue;
            String string = this.currentToken.getText();
            this.currentToken = this.nextToken(cSSLexer);
            this.currentToken = this.nextToken(cSSLexer);
            if ("src".equalsIgnoreCase(string)) {
                while (this.currentToken != null && this.currentToken.getType() != 30 && this.currentToken.getType() != 29 && this.currentToken.getType() != -1) {
                    int n;
                    Object object;
                    if (this.currentToken.getType() == 11) {
                        arrayList.add(new FontFace.FontFaceSrc(FontFace.FontFaceSrcType.REFERENCE, this.currentToken.getText()));
                    } else if (this.currentToken.getType() == 43) {
                        int n2;
                        Object object2;
                        ParsedValueImpl[] arrparsedValueImpl = new ParsedValueImpl[]{new ParsedValueImpl<String, String>(this.currentToken.getText(), StringConverter.getInstance()), new ParsedValueImpl(this.sourceOfStylesheet, null)};
                        ParsedValueImpl<ParsedValue[], String> parsedValueImpl = new ParsedValueImpl<ParsedValue[], String>(arrparsedValueImpl, URLConverter.getInstance());
                        String string2 = (String)parsedValueImpl.convert(null);
                        object = null;
                        try {
                            object2 = new URI(string2);
                            object = ((URI)object2).toURL();
                        }
                        catch (MalformedURLException | URISyntaxException exception) {
                            n2 = this.currentToken.getLine();
                            int n3 = this.currentToken.getOffset();
                            String string3 = MessageFormat.format("Could not resolve @font-face url [{2}] at [{0,number,#},{1,number,#}]", n2, n3, string2);
                            CssError cssError = this.createError(string3);
                            if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                                LOGGER.warning(cssError.toString());
                            }
                            this.reportError(cssError);
                            while (this.currentToken != null) {
                                int n4 = this.currentToken.getType();
                                if (n4 == 29 || n4 == -1) {
                                    return null;
                                }
                                this.currentToken = this.nextToken(cSSLexer);
                            }
                        }
                        object2 = null;
                        while (true) {
                            this.currentToken = this.nextToken(cSSLexer);
                            int n5 = n2 = this.currentToken != null ? this.currentToken.getType() : -1;
                            if (n2 == 12) {
                                if (!"format(".equalsIgnoreCase(this.currentToken.getText())) break;
                                continue;
                            }
                            if (n2 == 11 || n2 == 10) {
                                object2 = Utils.stripQuotes(this.currentToken.getText());
                                continue;
                            }
                            if (n2 != 35) break;
                        }
                        arrayList.add(new FontFace.FontFaceSrc(FontFace.FontFaceSrcType.URL, ((URL)object).toExternalForm(), (String)object2));
                    } else if (this.currentToken.getType() == 12) {
                        if ("local(".equalsIgnoreCase(this.currentToken.getText())) {
                            this.currentToken = this.nextToken(cSSLexer);
                            StringBuilder stringBuilder = new StringBuilder();
                            while (this.currentToken != null && this.currentToken.getType() != 35 && this.currentToken.getType() != -1) {
                                stringBuilder.append(this.currentToken.getText());
                                this.currentToken = this.nextToken(cSSLexer);
                            }
                            n = 0;
                            int n6 = stringBuilder.length();
                            if (stringBuilder.charAt(n) == '\'' || stringBuilder.charAt(n) == '\"') {
                                ++n;
                            }
                            if (stringBuilder.charAt(n6 - 1) == '\'' || stringBuilder.charAt(n6 - 1) == '\"') {
                                --n6;
                            }
                            object = stringBuilder.substring(n, n6);
                            arrayList.add(new FontFace.FontFaceSrc(FontFace.FontFaceSrcType.LOCAL, (String)object));
                        } else {
                            int n7 = this.currentToken.getLine();
                            n = this.currentToken.getOffset();
                            String string4 = MessageFormat.format("Unknown @font-face src type [" + this.currentToken.getText() + ")] at [{0,number,#},{1,number,#}]", n7, n);
                            object = this.createError(string4);
                            if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                                LOGGER.warning(((CssError)object).toString());
                            }
                            this.reportError((CssError)object);
                        }
                    } else if (this.currentToken.getType() != 36) {
                        int n8 = this.currentToken.getLine();
                        n = this.currentToken.getOffset();
                        String string5 = MessageFormat.format("Unexpected TOKEN [" + this.currentToken.getText() + "] at [{0,number,#},{1,number,#}]", n8, n);
                        object = this.createError(string5);
                        if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                            LOGGER.warning(((CssError)object).toString());
                        }
                        this.reportError((CssError)object);
                    }
                    this.currentToken = this.nextToken(cSSLexer);
                }
                continue;
            }
            StringBuilder stringBuilder = new StringBuilder();
            while (this.currentToken != null && this.currentToken.getType() != 30 && this.currentToken.getType() != -1) {
                stringBuilder.append(this.currentToken.getText());
                this.currentToken = this.nextToken(cSSLexer);
            }
            hashMap.put(string, stringBuilder.toString());
        } while (this.currentToken != null && this.currentToken.getType() != 29 && this.currentToken.getType() != -1);
        return new FontFace(hashMap, arrayList);
    }

    private Stylesheet handleImport(CSSLexer cSSLexer) {
        Object object;
        ParsedValueImpl[] arrparsedValueImpl;
        this.currentToken = this.nextToken(cSSLexer);
        if (this.currentToken == null || this.currentToken.getType() == -1) {
            return null;
        }
        int n = this.currentToken.getType();
        String string = null;
        if (n == 10 || n == 43) {
            string = this.currentToken.getText();
        }
        Stylesheet stylesheet = null;
        String string2 = this.sourceOfStylesheet;
        if (string != null) {
            arrparsedValueImpl = new ParsedValueImpl[]{new ParsedValueImpl<String, String>(string, StringConverter.getInstance()), new ParsedValueImpl(this.sourceOfStylesheet, null)};
            object = new ParsedValueImpl<ParsedValue[], String>(arrparsedValueImpl, URLConverter.getInstance());
            String string3 = (String)object.convert(null);
            stylesheet = StyleManager.loadStylesheet(string3);
            this.sourceOfStylesheet = string2;
        }
        if (stylesheet == null) {
            arrparsedValueImpl = MessageFormat.format("Could not import {0}", string);
            object = this.createError((String)arrparsedValueImpl);
            if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                LOGGER.warning(object.toString());
            }
            this.reportError((CssError)object);
        }
        return stylesheet;
    }

    private List<Selector> selectors(CSSLexer cSSLexer) {
        ArrayList<Selector> arrayList = new ArrayList<Selector>();
        while (true) {
            Selector selector;
            if ((selector = this.selector(cSSLexer)) == null) {
                while (this.currentToken != null && this.currentToken.getType() != 29 && this.currentToken.getType() != -1) {
                    this.currentToken = this.nextToken(cSSLexer);
                }
                this.currentToken = this.nextToken(cSSLexer);
                if (this.currentToken != null && this.currentToken.getType() != -1) continue;
                this.currentToken = null;
                return null;
            }
            arrayList.add(selector);
            if (this.currentToken == null || this.currentToken.getType() != 36) break;
            this.currentToken = this.nextToken(cSSLexer);
        }
        return arrayList;
    }

    private Selector selector(CSSLexer cSSLexer) {
        Combinator combinator;
        ArrayList<Combinator> arrayList = null;
        ArrayList<SimpleSelector> arrayList2 = null;
        SimpleSelector simpleSelector = this.simpleSelector(cSSLexer);
        if (simpleSelector == null) {
            return null;
        }
        while ((combinator = this.combinator(cSSLexer)) != null) {
            if (arrayList == null) {
                arrayList = new ArrayList<Combinator>();
            }
            arrayList.add(combinator);
            SimpleSelector simpleSelector2 = this.simpleSelector(cSSLexer);
            if (simpleSelector2 == null) {
                return null;
            }
            if (arrayList2 == null) {
                arrayList2 = new ArrayList<SimpleSelector>();
                arrayList2.add(simpleSelector);
            }
            arrayList2.add(simpleSelector2);
        }
        if (this.currentToken != null && this.currentToken.getType() == 41) {
            this.currentToken = this.nextToken(cSSLexer);
        }
        if (arrayList2 == null) {
            return simpleSelector;
        }
        return new CompoundSelector(arrayList2, arrayList);
    }

    private SimpleSelector simpleSelector(CSSLexer cSSLexer) {
        String string = "*";
        String string2 = "";
        ArrayList<String> arrayList = null;
        ArrayList<String> arrayList2 = null;
        while (true) {
            int n = this.currentToken != null ? this.currentToken.getType() : 0;
            switch (n) {
                case 11: 
                case 33: {
                    string = this.currentToken.getText();
                    break;
                }
                case 38: {
                    this.currentToken = this.nextToken(cSSLexer);
                    if (this.currentToken != null && this.currentToken.getType() == 11) {
                        if (arrayList == null) {
                            arrayList = new ArrayList<String>();
                        }
                        arrayList.add(this.currentToken.getText());
                        break;
                    }
                    this.currentToken = Token.INVALID_TOKEN;
                    return null;
                }
                case 37: {
                    string2 = this.currentToken.getText().substring(1);
                    break;
                }
                case 31: {
                    this.currentToken = this.nextToken(cSSLexer);
                    if (this.currentToken != null && arrayList2 == null) {
                        arrayList2 = new ArrayList<String>();
                    }
                    if (this.currentToken.getType() == 11) {
                        arrayList2.add(this.currentToken.getText());
                    } else if (this.currentToken.getType() == 12) {
                        String string3 = this.functionalPseudo(cSSLexer);
                        arrayList2.add(string3);
                    } else {
                        this.currentToken = Token.INVALID_TOKEN;
                    }
                    if (this.currentToken.getType() != 0) break;
                    return null;
                }
                case -1: 
                case 27: 
                case 28: 
                case 36: 
                case 40: 
                case 41: {
                    return new SimpleSelector(string, arrayList, arrayList2, string2);
                }
                default: {
                    return null;
                }
            }
            this.currentToken = cSSLexer.nextToken();
            if (!LOGGER.isLoggable(PlatformLogger.Level.FINEST)) continue;
            LOGGER.finest(this.currentToken.toString());
        }
    }

    private String functionalPseudo(CSSLexer cSSLexer) {
        StringBuilder stringBuilder = new StringBuilder(this.currentToken.getText());
        block4: while (true) {
            this.currentToken = this.nextToken(cSSLexer);
            switch (this.currentToken.getType()) {
                case 10: 
                case 11: {
                    stringBuilder.append(this.currentToken.getText());
                    continue block4;
                }
                case 35: {
                    stringBuilder.append(')');
                    return stringBuilder.toString();
                }
            }
            break;
        }
        this.currentToken = Token.INVALID_TOKEN;
        return null;
    }

    private Combinator combinator(CSSLexer cSSLexer) {
        Combinator combinator = null;
        while (true) {
            int n = this.currentToken != null ? this.currentToken.getType() : 0;
            switch (n) {
                case 40: {
                    if (combinator != null || !" ".equals(this.currentToken.getText())) break;
                    combinator = Combinator.DESCENDANT;
                    break;
                }
                case 27: {
                    combinator = Combinator.CHILD;
                    break;
                }
                case 11: 
                case 31: 
                case 33: 
                case 37: 
                case 38: {
                    return combinator;
                }
                default: {
                    return null;
                }
            }
            this.currentToken = cSSLexer.nextToken();
            if (!LOGGER.isLoggable(PlatformLogger.Level.FINEST)) continue;
            LOGGER.finest(this.currentToken.toString());
        }
    }

    private List<Declaration> declarations(CSSLexer cSSLexer) {
        ArrayList<Declaration> arrayList = new ArrayList<Declaration>();
        do {
            Declaration declaration;
            if ((declaration = this.declaration(cSSLexer)) != null) {
                arrayList.add(declaration);
            } else {
                while (this.currentToken != null && this.currentToken.getType() != 30 && this.currentToken.getType() != 29 && this.currentToken.getType() != -1) {
                    this.currentToken = this.nextToken(cSSLexer);
                }
                if (this.currentToken != null && this.currentToken.getType() != 30) {
                    return arrayList;
                }
            }
            while (this.currentToken != null && this.currentToken.getType() == 30) {
                this.currentToken = this.nextToken(cSSLexer);
            }
        } while (this.currentToken != null && this.currentToken.getType() == 11);
        return arrayList;
    }

    private Declaration declaration(CSSLexer cSSLexer) {
        boolean bl;
        int n;
        int n2 = n = this.currentToken != null ? this.currentToken.getType() : 0;
        if (this.currentToken == null || this.currentToken.getType() != 11) {
            return null;
        }
        String string = this.currentToken.getText();
        this.currentToken = this.nextToken(cSSLexer);
        if (this.currentToken == null || this.currentToken.getType() != 31) {
            int n3 = this.currentToken.getLine();
            int n4 = this.currentToken.getOffset();
            String string2 = MessageFormat.format("Expected COLON at [{0,number,#},{1,number,#}]", n3, n4);
            CssError cssError = this.createError(string2);
            if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                LOGGER.warning(cssError.toString());
            }
            this.reportError(cssError);
            return null;
        }
        this.currentToken = this.nextToken(cSSLexer);
        Term term = this.expr(cSSLexer);
        ParsedValueImpl parsedValueImpl = null;
        try {
            parsedValueImpl = term != null ? this.valueFor(string, term, cSSLexer) : null;
        }
        catch (ParseException parseException) {
            Token token = parseException.tok;
            int n5 = token != null ? token.getLine() : -1;
            int n6 = token != null ? token.getOffset() : -1;
            String string3 = MessageFormat.format("{2} while parsing ''{3}'' at [{0,number,#},{1,number,#}]", n5, n6, parseException.getMessage(), string);
            CssError cssError = this.createError(string3);
            if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                LOGGER.warning(cssError.toString());
            }
            this.reportError(cssError);
            return null;
        }
        boolean bl2 = bl = this.currentToken.getType() == 39;
        if (bl) {
            this.currentToken = this.nextToken(cSSLexer);
        }
        Declaration declaration = parsedValueImpl != null ? new Declaration(string.toLowerCase(Locale.ROOT), parsedValueImpl, bl) : null;
        return declaration;
    }

    private Term expr(CSSLexer cSSLexer) {
        Term term;
        Term term2 = term = this.term(cSSLexer);
        while (true) {
            int n;
            int n2 = n = term2 != null && this.currentToken != null ? this.currentToken.getType() : 0;
            if (n == 0) {
                this.skipExpr(cSSLexer);
                return null;
            }
            if (n == 30 || n == 39 || n == 29 || n == -1) {
                return term;
            }
            if (n == 36) {
                this.currentToken = this.nextToken(cSSLexer);
                term2 = term2.nextLayer = this.term(cSSLexer);
                continue;
            }
            term2 = term2.nextInSeries = this.term(cSSLexer);
        }
    }

    private void skipExpr(CSSLexer cSSLexer) {
        int n;
        do {
            this.currentToken = this.nextToken(cSSLexer);
            int n2 = n = this.currentToken != null ? this.currentToken.getType() : 0;
        } while (n != 30 && n != 29 && n != -1);
    }

    private Term term(CSSLexer cSSLexer) {
        int n = this.currentToken != null ? this.currentToken.getType() : 0;
        switch (n) {
            case 13: 
            case 14: 
            case 15: 
            case 16: 
            case 17: 
            case 18: 
            case 19: 
            case 20: 
            case 21: 
            case 22: 
            case 23: 
            case 24: 
            case 25: 
            case 26: 
            case 45: 
            case 46: {
                break;
            }
            case 10: {
                break;
            }
            case 11: {
                break;
            }
            case 37: {
                break;
            }
            case 12: 
            case 34: {
                Term term;
                Term term2 = new Term(this.currentToken);
                this.currentToken = this.nextToken(cSSLexer);
                term2.firstArg = term = this.term(cSSLexer);
                while (true) {
                    int n2;
                    int n3 = n2 = this.currentToken != null ? this.currentToken.getType() : 0;
                    if (n2 == 35) {
                        this.currentToken = this.nextToken(cSSLexer);
                        return term2;
                    }
                    if (n2 == 36) {
                        this.currentToken = this.nextToken(cSSLexer);
                        term = term.nextArg = this.term(cSSLexer);
                        continue;
                    }
                    term = term.nextInSeries = this.term(cSSLexer);
                }
            }
            case 43: {
                break;
            }
            case 32: {
                break;
            }
            default: {
                int n4 = this.currentToken != null ? this.currentToken.getLine() : -1;
                int n5 = this.currentToken != null ? this.currentToken.getOffset() : -1;
                String string = this.currentToken != null ? this.currentToken.getText() : "";
                String string2 = MessageFormat.format("Unexpected token {0}{1}{0} at [{2,number,#},{3,number,#}]", "'", string, n4, n5);
                CssError cssError = this.createError(string2);
                if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                    LOGGER.warning(cssError.toString());
                }
                this.reportError(cssError);
                return null;
            }
        }
        Term term = new Term(this.currentToken);
        this.currentToken = this.nextToken(cSSLexer);
        return term;
    }

    static class Term {
        final Token token;
        Term nextInSeries;
        Term nextLayer;
        Term firstArg;
        Term nextArg;

        Term(Token token) {
            this.token = token;
            this.nextLayer = null;
            this.nextInSeries = null;
            this.firstArg = null;
            this.nextArg = null;
        }

        Term() {
            this(null);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            if (this.token != null) {
                stringBuilder.append(String.valueOf(this.token.getText()));
            }
            if (this.nextInSeries != null) {
                stringBuilder.append("<nextInSeries>");
                stringBuilder.append(this.nextInSeries.toString());
                stringBuilder.append("</nextInSeries>\n");
            }
            if (this.nextLayer != null) {
                stringBuilder.append("<nextLayer>");
                stringBuilder.append(this.nextLayer.toString());
                stringBuilder.append("</nextLayer>\n");
            }
            if (this.firstArg != null) {
                stringBuilder.append("<args>");
                stringBuilder.append(this.firstArg.toString());
                if (this.nextArg != null) {
                    stringBuilder.append(this.nextArg.toString());
                }
                stringBuilder.append("</args>");
            }
            return stringBuilder.toString();
        }
    }

    private static final class ParseException
    extends Exception {
        private final Token tok;
        private final String source;

        ParseException(String string) {
            this(string, null, null);
        }

        ParseException(String string, Token token, CSSParser cSSParser) {
            super(string);
            this.tok = token;
            this.source = cSSParser.sourceOfStylesheet != null ? cSSParser.sourceOfStylesheet : (cSSParser.sourceOfInlineStyle != null ? cSSParser.sourceOfInlineStyle.toString() : (cSSParser.stylesheetAsText != null ? cSSParser.stylesheetAsText : "?"));
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(super.getMessage());
            stringBuilder.append(this.source);
            if (this.tok != null) {
                stringBuilder.append(": ").append(this.tok.toString());
            }
            return stringBuilder.toString();
        }
    }
}

