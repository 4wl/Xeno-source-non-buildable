/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.fxml.expression;

import com.sun.javafx.fxml.BeanAdapter;
import com.sun.javafx.fxml.expression.BinaryExpression;
import com.sun.javafx.fxml.expression.KeyPath;
import com.sun.javafx.fxml.expression.LiteralExpression;
import com.sun.javafx.fxml.expression.Operator;
import com.sun.javafx.fxml.expression.UnaryExpression;
import com.sun.javafx.fxml.expression.VariableExpression;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Expression<T> {
    private static final String NULL_KEYWORD = "null";
    private static final String TRUE_KEYWORD = "true";
    private static final String FALSE_KEYWORD = "false";

    public abstract T evaluate(Object var1);

    public abstract void update(Object var1, T var2);

    public abstract boolean isDefined(Object var1);

    public abstract boolean isLValue();

    public List<KeyPath> getArguments() {
        ArrayList<KeyPath> arrayList = new ArrayList<KeyPath>();
        this.getArguments(arrayList);
        return arrayList;
    }

    protected abstract void getArguments(List<KeyPath> var1);

    public static <T> T get(Object object, KeyPath keyPath) {
        if (keyPath == null) {
            throw new NullPointerException();
        }
        return Expression.get(object, keyPath.iterator());
    }

    private static <T> T get(Object object, Iterator<String> iterator) {
        if (iterator == null) {
            throw new NullPointerException();
        }
        Object object2 = iterator.hasNext() ? Expression.get(Expression.get(object, iterator.next()), iterator) : object;
        return (T)object2;
    }

    public static <T> T get(Object object, String string) {
        T t;
        if (string == null) {
            throw new NullPointerException();
        }
        if (object instanceof List) {
            List list = (List)object;
            t = (T)list.get(Integer.parseInt(string));
        } else if (object != null) {
            Map map = object instanceof Map ? (Map)object : new BeanAdapter(object);
            t = (T)map.get(string);
        } else {
            t = null;
        }
        return t;
    }

    public static void set(Object object, KeyPath keyPath, Object object2) {
        if (keyPath == null) {
            throw new NullPointerException();
        }
        Expression.set(object, keyPath.iterator(), object2);
    }

    private static void set(Object object, Iterator<String> iterator, Object object2) {
        if (iterator == null) {
            throw new NullPointerException();
        }
        if (!iterator.hasNext()) {
            throw new IllegalArgumentException();
        }
        String string = iterator.next();
        if (iterator.hasNext()) {
            Expression.set(Expression.get(object, string), iterator, object2);
        } else {
            Expression.set(object, string, object2);
        }
    }

    public static void set(Object object, String string, Object object2) {
        if (string == null) {
            throw new NullPointerException();
        }
        if (object instanceof List) {
            List list = (List)object;
            list.set(Integer.parseInt(string), object2);
        } else if (object != null) {
            Map map = object instanceof Map ? (Map)object : new BeanAdapter(object);
            map.put(string, object2);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static boolean isDefined(Object object, KeyPath keyPath) {
        if (keyPath == null) {
            throw new NullPointerException();
        }
        return Expression.isDefined(object, keyPath.iterator());
    }

    private static boolean isDefined(Object object, Iterator<String> iterator) {
        if (iterator == null) {
            throw new NullPointerException();
        }
        if (!iterator.hasNext()) {
            throw new IllegalArgumentException();
        }
        String string = iterator.next();
        boolean bl = iterator.hasNext() ? Expression.isDefined(Expression.get(object, string), iterator) : Expression.isDefined(object, string);
        return bl;
    }

    public static boolean isDefined(Object object, String string) {
        boolean bl;
        if (string == null) {
            throw new NullPointerException();
        }
        if (object instanceof List) {
            List list = (List)object;
            bl = Integer.parseInt(string) < list.size();
        } else if (object != null) {
            Map map = object instanceof Map ? (Map)object : new BeanAdapter(object);
            bl = map.containsKey(string);
        } else {
            bl = false;
        }
        return bl;
    }

    public static BinaryExpression add(Expression expression, Expression expression2) {
        return new BinaryExpression<Object, Object>(expression, expression2, (object, object2) -> {
            Object object3;
            if (object instanceof String || object2 instanceof String) {
                object3 = object.toString().concat(object2.toString());
            } else {
                Number number = (Number)object;
                Number number2 = (Number)object2;
                if (number instanceof Double || number2 instanceof Double) {
                    object3 = number.doubleValue() + number2.doubleValue();
                } else if (number instanceof Float || number2 instanceof Float) {
                    object3 = Float.valueOf(number.floatValue() + number2.floatValue());
                } else if (number instanceof Long || number2 instanceof Long) {
                    object3 = number.longValue() + number2.longValue();
                } else if (number instanceof Integer || number2 instanceof Integer) {
                    object3 = number.intValue() + number2.intValue();
                } else if (number instanceof Short || number2 instanceof Short) {
                    object3 = number.shortValue() + number2.shortValue();
                } else if (number instanceof Byte || number2 instanceof Byte) {
                    object3 = number.byteValue() + number2.byteValue();
                } else {
                    throw new UnsupportedOperationException();
                }
            }
            return object3;
        });
    }

    public static BinaryExpression add(Expression expression, Object object) {
        return Expression.add(expression, new LiteralExpression<Object>(object));
    }

    public static BinaryExpression add(Object object, Expression expression) {
        return Expression.add(new LiteralExpression<Object>(object), expression);
    }

    public static BinaryExpression add(Object object, Object object2) {
        return Expression.add(new LiteralExpression<Object>(object), new LiteralExpression<Object>(object2));
    }

    public static BinaryExpression subtract(Expression expression, Expression expression2) {
        return new BinaryExpression<Number, Number>(expression, expression2, (number, number2) -> {
            Number number3;
            if (number instanceof Double || number2 instanceof Double) {
                number3 = number.doubleValue() - number2.doubleValue();
            } else if (number instanceof Float || number2 instanceof Float) {
                number3 = Float.valueOf(number.floatValue() - number2.floatValue());
            } else if (number instanceof Long || number2 instanceof Long) {
                number3 = number.longValue() - number2.longValue();
            } else if (number instanceof Integer || number2 instanceof Integer) {
                number3 = number.intValue() - number2.intValue();
            } else if (number instanceof Short || number2 instanceof Short) {
                number3 = number.shortValue() - number2.shortValue();
            } else if (number instanceof Byte || number2 instanceof Byte) {
                number3 = number.byteValue() - number2.byteValue();
            } else {
                throw new UnsupportedOperationException();
            }
            return number3;
        });
    }

    public static BinaryExpression subtract(Expression expression, Number number) {
        return Expression.subtract(expression, new LiteralExpression<Number>(number));
    }

    public static BinaryExpression subtract(Number number, Expression expression) {
        return Expression.subtract(new LiteralExpression<Number>(number), expression);
    }

    public static BinaryExpression subtract(Number number, Number number2) {
        return Expression.subtract(new LiteralExpression<Number>(number), new LiteralExpression<Number>(number2));
    }

    public static BinaryExpression multiply(Expression expression, Expression expression2) {
        return new BinaryExpression<Number, Number>(expression, expression2, (number, number2) -> {
            Number number3;
            if (number instanceof Double || number2 instanceof Double) {
                number3 = number.doubleValue() * number2.doubleValue();
            } else if (number instanceof Float || number2 instanceof Float) {
                number3 = Float.valueOf(number.floatValue() * number2.floatValue());
            } else if (number instanceof Long || number2 instanceof Long) {
                number3 = number.longValue() * number2.longValue();
            } else if (number instanceof Integer || number2 instanceof Integer) {
                number3 = number.intValue() * number2.intValue();
            } else if (number instanceof Short || number2 instanceof Short) {
                number3 = number.shortValue() * number2.shortValue();
            } else if (number instanceof Byte || number2 instanceof Byte) {
                number3 = number.byteValue() * number2.byteValue();
            } else {
                throw new UnsupportedOperationException();
            }
            return number3;
        });
    }

    public static BinaryExpression multiply(Expression expression, Number number) {
        return Expression.multiply(expression, new LiteralExpression<Number>(number));
    }

    public static BinaryExpression multiply(Number number, Expression expression) {
        return Expression.multiply(new LiteralExpression<Number>(number), expression);
    }

    public static BinaryExpression multiply(Number number, Number number2) {
        return Expression.multiply(new LiteralExpression<Number>(number), new LiteralExpression<Number>(number2));
    }

    public static BinaryExpression divide(Expression expression, Expression expression2) {
        return new BinaryExpression<Number, Number>(expression, expression2, (number, number2) -> {
            Number number3;
            if (number instanceof Double || number2 instanceof Double) {
                number3 = number.doubleValue() / number2.doubleValue();
            } else if (number instanceof Float || number2 instanceof Float) {
                number3 = Float.valueOf(number.floatValue() / number2.floatValue());
            } else if (number instanceof Long || number2 instanceof Long) {
                number3 = number.longValue() / number2.longValue();
            } else if (number instanceof Integer || number2 instanceof Integer) {
                number3 = number.intValue() / number2.intValue();
            } else if (number instanceof Short || number2 instanceof Short) {
                number3 = number.shortValue() / number2.shortValue();
            } else if (number instanceof Byte || number2 instanceof Byte) {
                number3 = number.byteValue() / number2.byteValue();
            } else {
                throw new UnsupportedOperationException();
            }
            return number3;
        });
    }

    public static BinaryExpression divide(Expression expression, Number number) {
        return Expression.divide(expression, new LiteralExpression<Number>(number));
    }

    public static BinaryExpression divide(Number number, Expression<Number> expression) {
        return Expression.divide(new LiteralExpression<Number>(number), expression);
    }

    public static BinaryExpression divide(Number number, Number number2) {
        return Expression.divide(new LiteralExpression<Number>(number), new LiteralExpression<Number>(number2));
    }

    public static BinaryExpression modulo(Expression expression, Expression expression2) {
        return new BinaryExpression<Number, Number>(expression, expression2, (number, number2) -> {
            Number number3;
            if (number instanceof Double || number2 instanceof Double) {
                number3 = number.doubleValue() % number2.doubleValue();
            } else if (number instanceof Float || number2 instanceof Float) {
                number3 = Float.valueOf(number.floatValue() % number2.floatValue());
            } else if (number instanceof Long || number2 instanceof Long) {
                number3 = number.longValue() % number2.longValue();
            } else if (number instanceof Integer || number2 instanceof Integer) {
                number3 = number.intValue() % number2.intValue();
            } else if (number instanceof Short || number2 instanceof Short) {
                number3 = number.shortValue() % number2.shortValue();
            } else if (number instanceof Byte || number2 instanceof Byte) {
                number3 = number.byteValue() % number2.byteValue();
            } else {
                throw new UnsupportedOperationException();
            }
            return number3;
        });
    }

    public static BinaryExpression modulo(Expression<Number> expression, Number number) {
        return Expression.modulo(expression, new LiteralExpression<Number>(number));
    }

    public static BinaryExpression modulo(Number number, Expression<Number> expression) {
        return Expression.modulo(new LiteralExpression<Number>(number), expression);
    }

    public static BinaryExpression modulo(Number number, Number number2) {
        return Expression.modulo(new LiteralExpression<Number>(number), new LiteralExpression<Number>(number2));
    }

    public static BinaryExpression equalTo(Expression expression, Expression expression2) {
        return new BinaryExpression<Comparable, Boolean>(expression, expression2, (comparable, comparable2) -> comparable.compareTo(comparable2) == 0);
    }

    public static BinaryExpression equalTo(Expression expression, Object object) {
        return Expression.equalTo(expression, new LiteralExpression<Object>(object));
    }

    public static BinaryExpression equalTo(Object object, Expression expression) {
        return Expression.equalTo(new LiteralExpression<Object>(object), expression);
    }

    public static BinaryExpression equalTo(Object object, Object object2) {
        return Expression.equalTo(new LiteralExpression<Object>(object), new LiteralExpression<Object>(object2));
    }

    public static BinaryExpression notEqualTo(Expression expression, Expression expression2) {
        return new BinaryExpression<Comparable, Boolean>(expression, expression2, (comparable, comparable2) -> comparable.compareTo(comparable2) != 0);
    }

    public static BinaryExpression notEqualTo(Expression expression, Object object) {
        return Expression.notEqualTo(expression, new LiteralExpression<Object>(object));
    }

    public static BinaryExpression notEqualTo(Object object, Expression expression) {
        return Expression.notEqualTo(new LiteralExpression<Object>(object), expression);
    }

    public static BinaryExpression notEqualTo(Object object, Object object2) {
        return Expression.notEqualTo(new LiteralExpression<Object>(object), new LiteralExpression<Object>(object2));
    }

    public static BinaryExpression greaterThan(Expression expression, Expression expression2) {
        return new BinaryExpression<Comparable, Boolean>(expression, expression2, (comparable, comparable2) -> comparable.compareTo(comparable2) > 0);
    }

    public static BinaryExpression greaterThan(Expression expression, Object object) {
        return Expression.greaterThan(expression, new LiteralExpression<Object>(object));
    }

    public static BinaryExpression greaterThan(Object object, Expression expression) {
        return Expression.greaterThan(new LiteralExpression<Object>(object), expression);
    }

    public static BinaryExpression greaterThan(Object object, Object object2) {
        return Expression.greaterThan(new LiteralExpression<Object>(object), new LiteralExpression<Object>(object2));
    }

    public static BinaryExpression greaterThanOrEqualTo(Expression expression, Expression expression2) {
        return new BinaryExpression<Comparable, Boolean>(expression, expression2, (comparable, comparable2) -> comparable.compareTo(comparable2) >= 0);
    }

    public static BinaryExpression greaterThanOrEqualTo(Expression expression, Object object) {
        return Expression.greaterThanOrEqualTo(expression, new LiteralExpression<Object>(object));
    }

    public static BinaryExpression greaterThanOrEqualTo(Object object, Expression expression) {
        return Expression.greaterThanOrEqualTo(new LiteralExpression<Object>(object), expression);
    }

    public static BinaryExpression greaterThanOrEqualTo(Object object, Object object2) {
        return Expression.greaterThanOrEqualTo(new LiteralExpression<Object>(object), new LiteralExpression<Object>(object2));
    }

    public static BinaryExpression lessThan(Expression expression, Expression expression2) {
        return new BinaryExpression<Comparable, Boolean>(expression, expression2, (comparable, comparable2) -> comparable.compareTo(comparable2) < 0);
    }

    public static BinaryExpression lessThan(Expression expression, Object object) {
        return Expression.lessThan(expression, new LiteralExpression<Object>(object));
    }

    public static BinaryExpression lessThan(Object object, Expression expression) {
        return Expression.lessThan(new LiteralExpression<Object>(object), expression);
    }

    public static BinaryExpression lessThan(Object object, Object object2) {
        return Expression.lessThan(new LiteralExpression<Object>(object), new LiteralExpression<Object>(object2));
    }

    public static BinaryExpression lessThanOrEqualTo(Expression expression, Expression expression2) {
        return new BinaryExpression<Comparable, Boolean>(expression, expression2, (comparable, comparable2) -> comparable.compareTo(comparable2) <= 0);
    }

    public static BinaryExpression lessThanOrEqualTo(Expression expression, Object object) {
        return Expression.lessThanOrEqualTo(expression, new LiteralExpression<Object>(object));
    }

    public static BinaryExpression lessThanOrEqualTo(Object object, Expression expression) {
        return Expression.lessThanOrEqualTo(new LiteralExpression<Object>(object), expression);
    }

    public static BinaryExpression lessThanOrEqualTo(Object object, Object object2) {
        return Expression.lessThanOrEqualTo(new LiteralExpression<Object>(object), new LiteralExpression<Object>(object2));
    }

    public static BinaryExpression and(Expression expression, Expression expression2) {
        return new BinaryExpression<Boolean, Boolean>(expression, expression2, (bl, bl2) -> bl != false && bl2 != false);
    }

    public static BinaryExpression and(Expression expression, Boolean bl) {
        return Expression.and(expression, new LiteralExpression<Boolean>(bl));
    }

    public static BinaryExpression and(Boolean bl, Expression expression) {
        return Expression.and(new LiteralExpression<Boolean>(bl), expression);
    }

    public static BinaryExpression and(Boolean bl, Boolean bl2) {
        return Expression.and(new LiteralExpression<Boolean>(bl), new LiteralExpression<Boolean>(bl2));
    }

    public static BinaryExpression or(Expression expression, Expression expression2) {
        return new BinaryExpression<Boolean, Boolean>(expression, expression2, (bl, bl2) -> bl != false || bl2 != false);
    }

    public static BinaryExpression or(Expression expression, Boolean bl) {
        return Expression.or(expression, new LiteralExpression<Boolean>(bl));
    }

    public static BinaryExpression or(Boolean bl, Expression expression) {
        return Expression.or(new LiteralExpression<Boolean>(bl), expression);
    }

    public static BinaryExpression or(Boolean bl, Boolean bl2) {
        return Expression.or(new LiteralExpression<Boolean>(bl), new LiteralExpression<Boolean>(bl2));
    }

    public static UnaryExpression negate(Expression expression) {
        return new UnaryExpression<Number, Number>(expression, number -> {
            Class<?> class_ = number.getClass();
            if (class_ == Byte.class) {
                return (int)(-number.byteValue());
            }
            if (class_ == Short.class) {
                return (int)(-number.shortValue());
            }
            if (class_ == Integer.class) {
                return -number.intValue();
            }
            if (class_ == Long.class) {
                return -number.longValue();
            }
            if (class_ == Float.class) {
                return Float.valueOf(-number.floatValue());
            }
            if (class_ == Double.class) {
                return -number.doubleValue();
            }
            throw new UnsupportedOperationException();
        });
    }

    public static UnaryExpression negate(Number number) {
        return Expression.negate(new LiteralExpression<Number>(number));
    }

    public static UnaryExpression not(Expression expression) {
        return new UnaryExpression<Boolean, Boolean>(expression, bl -> bl == false);
    }

    public static UnaryExpression not(Boolean bl) {
        return Expression.not(new LiteralExpression<Boolean>(bl));
    }

    public static Expression valueOf(String string) {
        Expression expression;
        if (string == null) {
            throw new NullPointerException();
        }
        Parser parser = new Parser();
        try {
            expression = parser.parse(new StringReader(string));
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        return expression;
    }

    private static class Parser {
        private int c = -1;
        private char[] pushbackBuffer = new char[6];
        private static final int PUSHBACK_BUFFER_SIZE = 6;

        private Parser() {
        }

        public Expression parse(Reader reader) throws IOException {
            LinkedList<Token> linkedList = this.tokenize(new PushbackReader(reader, 6));
            LinkedList<LiteralExpression<Object>> linkedList2 = new LinkedList<LiteralExpression<Object>>();
            for (Token token : linkedList) {
                Expression expression;
                block0 : switch (token.type) {
                    case LITERAL: {
                        expression = new LiteralExpression<Object>(token.value);
                        break;
                    }
                    case VARIABLE: {
                        expression = new VariableExpression((KeyPath)token.value);
                        break;
                    }
                    case FUNCTION: {
                        expression = null;
                        break;
                    }
                    case UNARY_OPERATOR: {
                        Operator operator = (Operator)((Object)token.value);
                        Expression expression2 = (Expression)linkedList2.pop();
                        switch (operator) {
                            case NEGATE: {
                                expression = Expression.negate(expression2);
                                break block0;
                            }
                            case NOT: {
                                expression = Expression.not(expression2);
                                break block0;
                            }
                        }
                        throw new UnsupportedOperationException();
                    }
                    case BINARY_OPERATOR: {
                        Operator operator = (Operator)((Object)token.value);
                        Expression expression2 = (Expression)linkedList2.pop();
                        Expression expression3 = (Expression)linkedList2.pop();
                        switch (operator) {
                            case ADD: {
                                expression = Expression.add(expression3, expression2);
                                break block0;
                            }
                            case SUBTRACT: {
                                expression = Expression.subtract(expression3, expression2);
                                break block0;
                            }
                            case MULTIPLY: {
                                expression = Expression.multiply(expression3, expression2);
                                break block0;
                            }
                            case DIVIDE: {
                                expression = Expression.divide(expression3, expression2);
                                break block0;
                            }
                            case MODULO: {
                                expression = Expression.modulo(expression3, expression2);
                                break block0;
                            }
                            case GREATER_THAN: {
                                expression = Expression.greaterThan(expression3, expression2);
                                break block0;
                            }
                            case GREATER_THAN_OR_EQUAL_TO: {
                                expression = Expression.greaterThanOrEqualTo(expression3, expression2);
                                break block0;
                            }
                            case LESS_THAN: {
                                expression = Expression.lessThan(expression3, expression2);
                                break block0;
                            }
                            case LESS_THAN_OR_EQUAL_TO: {
                                expression = Expression.lessThanOrEqualTo(expression3, expression2);
                                break block0;
                            }
                            case EQUAL_TO: {
                                expression = Expression.equalTo(expression3, expression2);
                                break block0;
                            }
                            case NOT_EQUAL_TO: {
                                expression = Expression.notEqualTo(expression3, expression2);
                                break block0;
                            }
                            case AND: {
                                expression = Expression.and(expression3, expression2);
                                break block0;
                            }
                            case OR: {
                                expression = Expression.or(expression3, expression2);
                                break block0;
                            }
                        }
                        throw new UnsupportedOperationException();
                    }
                    default: {
                        throw new UnsupportedOperationException();
                    }
                }
                linkedList2.push((LiteralExpression<Object>)expression);
            }
            if (linkedList2.size() != 1) {
                throw new IllegalArgumentException("Invalid expression.");
            }
            return (Expression)linkedList2.peek();
        }

        private LinkedList<Token> tokenize(PushbackReader pushbackReader) throws IOException {
            LinkedList<Token> linkedList = new LinkedList<Token>();
            LinkedList<Token> linkedList2 = new LinkedList<Token>();
            this.c = pushbackReader.read();
            boolean bl = true;
            while (this.c != -1) {
                Token token;
                block63: {
                    boolean bl2;
                    block70: {
                        block69: {
                            block68: {
                                block67: {
                                    block66: {
                                        block65: {
                                            Comparable<Long> comparable;
                                            int n;
                                            block64: {
                                                block62: {
                                                    while (this.c != -1 && Character.isWhitespace(this.c)) {
                                                        this.c = pushbackReader.read();
                                                    }
                                                    if (this.c == -1) continue;
                                                    if (this.c != 110) break block62;
                                                    if (this.readKeyword(pushbackReader, Expression.NULL_KEYWORD)) {
                                                        token = new Token(TokenType.LITERAL, null);
                                                    } else {
                                                        token = new Token(TokenType.VARIABLE, KeyPath.parse(pushbackReader));
                                                        this.c = pushbackReader.read();
                                                    }
                                                    break block63;
                                                }
                                                if (this.c != 34 && this.c != 39) break block64;
                                                StringBuilder stringBuilder = new StringBuilder();
                                                n = this.c;
                                                this.c = pushbackReader.read();
                                                while (this.c != -1 && this.c != n) {
                                                    if (!Character.isISOControl(this.c)) {
                                                        if (this.c == 92) {
                                                            this.c = pushbackReader.read();
                                                            if (this.c == 98) {
                                                                this.c = 8;
                                                            } else if (this.c == 102) {
                                                                this.c = 12;
                                                            } else if (this.c == 110) {
                                                                this.c = 10;
                                                            } else if (this.c == 114) {
                                                                this.c = 13;
                                                            } else if (this.c == 116) {
                                                                this.c = 9;
                                                            } else if (this.c == 117) {
                                                                comparable = new StringBuilder();
                                                                while (((StringBuilder)comparable).length() < 4) {
                                                                    this.c = pushbackReader.read();
                                                                    ((StringBuilder)comparable).append((char)this.c);
                                                                }
                                                                String string = ((StringBuilder)comparable).toString();
                                                                this.c = (char)Integer.parseInt(string, 16);
                                                            } else if (this.c != 92 && this.c != 47 && this.c != 34 && this.c != 39 && this.c != n) {
                                                                throw new IllegalArgumentException("Unsupported escape sequence.");
                                                            }
                                                        }
                                                        stringBuilder.append((char)this.c);
                                                    }
                                                    this.c = pushbackReader.read();
                                                }
                                                if (this.c != n) {
                                                    throw new IllegalArgumentException("Unterminated string.");
                                                }
                                                this.c = pushbackReader.read();
                                                token = new Token(TokenType.LITERAL, stringBuilder.toString());
                                                break block63;
                                            }
                                            if (!Character.isDigit(this.c)) break block65;
                                            StringBuilder stringBuilder = new StringBuilder();
                                            n = 1;
                                            while (this.c != -1 && (Character.isDigit(this.c) || this.c == 46 || this.c == 101 || this.c == 69)) {
                                                stringBuilder.append((char)this.c);
                                                n &= this.c != 46 ? 1 : 0;
                                                this.c = pushbackReader.read();
                                            }
                                            comparable = n != 0 ? (Number)Long.parseLong(stringBuilder.toString()) : (Number)Double.parseDouble(stringBuilder.toString());
                                            token = new Token(TokenType.LITERAL, comparable);
                                            break block63;
                                        }
                                        if (this.c != 116) break block66;
                                        if (this.readKeyword(pushbackReader, Expression.TRUE_KEYWORD)) {
                                            token = new Token(TokenType.LITERAL, true);
                                        } else {
                                            token = new Token(TokenType.VARIABLE, KeyPath.parse(pushbackReader));
                                            this.c = pushbackReader.read();
                                        }
                                        break block63;
                                    }
                                    if (this.c != 102) break block67;
                                    if (this.readKeyword(pushbackReader, Expression.FALSE_KEYWORD)) {
                                        token = new Token(TokenType.LITERAL, false);
                                    } else {
                                        token = new Token(TokenType.VARIABLE, KeyPath.parse(pushbackReader));
                                        this.c = pushbackReader.read();
                                    }
                                    break block63;
                                }
                                if (!Character.isJavaIdentifierStart(this.c)) break block68;
                                pushbackReader.unread(this.c);
                                token = new Token(TokenType.VARIABLE, KeyPath.parse(pushbackReader));
                                this.c = pushbackReader.read();
                                break block63;
                            }
                            bl2 = true;
                            if (!bl) break block69;
                            switch (this.c) {
                                case 45: {
                                    token = new Token(TokenType.UNARY_OPERATOR, (Object)Operator.NEGATE);
                                    break block70;
                                }
                                case 33: {
                                    token = new Token(TokenType.UNARY_OPERATOR, (Object)Operator.NOT);
                                    break block70;
                                }
                                case 40: {
                                    token = new Token(TokenType.BEGIN_GROUP, null);
                                    break block70;
                                }
                                default: {
                                    throw new IllegalArgumentException("Unexpected character in expression.");
                                }
                            }
                        }
                        switch (this.c) {
                            case 43: {
                                token = new Token(TokenType.BINARY_OPERATOR, (Object)Operator.ADD);
                                break;
                            }
                            case 45: {
                                token = new Token(TokenType.BINARY_OPERATOR, (Object)Operator.SUBTRACT);
                                break;
                            }
                            case 42: {
                                token = new Token(TokenType.BINARY_OPERATOR, (Object)Operator.MULTIPLY);
                                break;
                            }
                            case 47: {
                                token = new Token(TokenType.BINARY_OPERATOR, (Object)Operator.DIVIDE);
                                break;
                            }
                            case 37: {
                                token = new Token(TokenType.BINARY_OPERATOR, (Object)Operator.MODULO);
                                break;
                            }
                            case 61: {
                                this.c = pushbackReader.read();
                                if (this.c == 61) {
                                    token = new Token(TokenType.BINARY_OPERATOR, (Object)Operator.EQUAL_TO);
                                    break;
                                }
                                throw new IllegalArgumentException("Unexpected character in expression.");
                            }
                            case 33: {
                                this.c = pushbackReader.read();
                                if (this.c == 61) {
                                    token = new Token(TokenType.BINARY_OPERATOR, (Object)Operator.NOT_EQUAL_TO);
                                    break;
                                }
                                throw new IllegalArgumentException("Unexpected character in expression.");
                            }
                            case 62: {
                                this.c = pushbackReader.read();
                                if (this.c == 61) {
                                    token = new Token(TokenType.BINARY_OPERATOR, (Object)Operator.GREATER_THAN_OR_EQUAL_TO);
                                    break;
                                }
                                bl2 = false;
                                token = new Token(TokenType.BINARY_OPERATOR, (Object)Operator.GREATER_THAN);
                                break;
                            }
                            case 60: {
                                this.c = pushbackReader.read();
                                if (this.c == 61) {
                                    token = new Token(TokenType.BINARY_OPERATOR, (Object)Operator.LESS_THAN_OR_EQUAL_TO);
                                    break;
                                }
                                bl2 = false;
                                token = new Token(TokenType.BINARY_OPERATOR, (Object)Operator.LESS_THAN);
                                break;
                            }
                            case 38: {
                                this.c = pushbackReader.read();
                                if (this.c == 38) {
                                    token = new Token(TokenType.BINARY_OPERATOR, (Object)Operator.AND);
                                    break;
                                }
                                throw new IllegalArgumentException("Unexpected character in expression.");
                            }
                            case 124: {
                                this.c = pushbackReader.read();
                                if (this.c == 124) {
                                    token = new Token(TokenType.BINARY_OPERATOR, (Object)Operator.OR);
                                    break;
                                }
                                throw new IllegalArgumentException("Unexpected character in expression.");
                            }
                            case 41: {
                                token = new Token(TokenType.END_GROUP, null);
                                break;
                            }
                            default: {
                                throw new IllegalArgumentException("Unexpected character in expression.");
                            }
                        }
                    }
                    if (bl2) {
                        this.c = pushbackReader.read();
                    }
                }
                switch (token.type) {
                    case LITERAL: 
                    case VARIABLE: {
                        linkedList.add(token);
                        break;
                    }
                    case UNARY_OPERATOR: 
                    case BINARY_OPERATOR: {
                        int n = ((Operator)((Object)token.value)).getPriority();
                        while (!linkedList2.isEmpty() && ((Token)linkedList2.peek()).type != TokenType.BEGIN_GROUP && ((Operator)((Object)((Token)linkedList2.peek()).value)).getPriority() >= n && ((Operator)((Object)((Token)linkedList2.peek()).value)).getPriority() != 6) {
                            linkedList.add((Token)linkedList2.pop());
                        }
                        linkedList2.push(token);
                        break;
                    }
                    case BEGIN_GROUP: {
                        linkedList2.push(token);
                        break;
                    }
                    case END_GROUP: {
                        Token token2 = (Token)linkedList2.pop();
                        while (token2.type != TokenType.BEGIN_GROUP) {
                            linkedList.add(token2);
                            token2 = (Token)linkedList2.pop();
                        }
                        break;
                    }
                    default: {
                        throw new UnsupportedOperationException();
                    }
                }
                bl = token.type != TokenType.LITERAL && token.type != TokenType.VARIABLE && token.type != TokenType.END_GROUP;
            }
            while (!linkedList2.isEmpty()) {
                linkedList.add((Token)linkedList2.pop());
            }
            return linkedList;
        }

        private boolean readKeyword(PushbackReader pushbackReader, String string) throws IOException {
            boolean bl;
            int n;
            int n2 = string.length();
            for (n = 0; this.c != -1 && n < n2; ++n) {
                this.pushbackBuffer[n] = (char)this.c;
                if (string.charAt(n) != this.c) break;
                this.c = pushbackReader.read();
            }
            if (n < n2) {
                pushbackReader.unread(this.pushbackBuffer, 0, n + 1);
                bl = false;
            } else {
                bl = true;
            }
            return bl;
        }

        public static enum TokenType {
            LITERAL,
            VARIABLE,
            FUNCTION,
            UNARY_OPERATOR,
            BINARY_OPERATOR,
            BEGIN_GROUP,
            END_GROUP;

        }

        public static class Token {
            public final TokenType type;
            public final Object value;

            public Token(TokenType tokenType, Object object) {
                this.type = tokenType;
                this.value = object;
            }

            public String toString() {
                return this.value.toString();
            }
        }
    }
}

