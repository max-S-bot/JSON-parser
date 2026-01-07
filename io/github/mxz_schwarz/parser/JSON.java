package io.github.mxz_schwarz.parser;

import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Function;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;

/**
 * {@code class} that does most of the parsing legwork via the {@code public} 
 * {@code JSON.parse} methods.
 * @author max-S-bot
 */
public class JSON<N extends Number> {

    /**
     * {@link Set} of whitespace characters that 
     * are ignored between JSON tokens.
     */
    private static final Set<Character> WHITESPACE = Set.of(' ', '\n', '\r', '\t');

    /**
     * {@link Set} of digits. 
     */
    private static final Set<Character> DIGITS = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    
    /**
     * A {@link Set} of the literal {@code char}s that 
     * are not permitted in a JSON string.  
     */
    private static final Set<Character> INVALID_STR_CHARS = Set.of('\b', '\f', '\n', '\r', '\t');

    /**
     * Parses a {@link String} representing JSON data.
     * @param jsonStr A {@link String} representing the 
     * JSON data to be parsed.
     * @return A {@link Obj} representing the parsed data.
     * @throws JSONException When {@link #jsonStr}
     * does not represent valid JSON data.
     */
    public static Obj parse(String jsonStr) throws JSONException {
        return parse(jsonStr, Double::parseDouble);
    }

    public static <N extends Number> Obj parse(String jsonStr, 
        Function<String, N> numParser) 
        throws JSONException {
        try {
            return new JSON<N>(jsonStr, numParser).obj;
        } catch (JSONParseException jpe) {
            throw new JSONException(jpe);
        }
    }

    /**
     * Parses a file containing JSON data.
     * @param path A {@link Path} instance 
     * that corresponds to a JSON file to be parsed.
     * @return A {@link Obj} representing the parsed JSON file.
     * @throws JSONException When {@link Files#readString}
     * {@code throws} an {@link java.io.IOException} or when {@link parse} 
     * {@code throws} a {@link JSONException}.
     */
    public static Obj parse(Path path) throws JSONException {
        return parse(path, Double::parseDouble);
    }

    public static <N extends Number> Obj parse(Path path, 
        Function<String, N> numParser) throws JSONException {
        try {
            return parse(Files.readString(path));
        } catch (IOException ioe) {
            throw new JSONException(ioe, "IO exception");
        }
    }

    /**
     * The {@link String} representing the JSON 
     * data to parse.
     */
    private final String jsonStr;
    /**
     * The function this JSON instance will use
     *  to parse numbers.
     */
    private final Function<String, N> numParser;
    /**
     * The {@link Obj} representing 
     * the parsed JSON data.
     */
    private final Obj obj;
    /**
     * The current position
     * in the JSON data being parsed.
     */
    private int idx = 0;
    /**
     * The length of jsonStr
     */
    private final int len;

    /**
     * @param jsonStr The {@link String}
     * representing the JSON data that 
     * this {@link JSON} instance will parse.
     */
    private JSON(String jsonStr, Function<String, N> numParser) {
        this.jsonStr = jsonStr;
        this.len = jsonStr.length(); 
        this.numParser = numParser;
        skipWS();
        this.obj = parseVal();
        skipWS();
        if (idx != len)
            throw new JSONParseException("Invalid JSON");
    }

    /**
     * @return A {@link Map} that represents the
     * parsed object.
     * @throws JSONParseException when the given object is invalid
     */
    private Map<? extends Obj> parseMap() {
        java.util.Map<String, Obj> map = new HashMap<>();
        idx++;
        for (boolean flag = false;; flag = true) {
            skipWS();
            throwIfEnd();
            if (jsonStr.charAt(idx) == '}')
                return new Map<>(map);
            if (flag && jsonStr.charAt(idx++) != ',')
                throw new JSONParseException("Expected entry delimiter");
            skipWS();
            throwIfEnd();
            if (jsonStr.charAt(idx) != '"')
                throw new JSONParseException("Expected identifier");
            String name = ((Str) parseVal()).toStr(); 
            skipWS();
            if (jsonStr.charAt(idx++) != ':')
                throw new JSONParseException("Expected entry");
            skipWS();
            map.put(name, parseVal());
        }
    }

    /**
     * @return A {@link Arr} representing the parsed array.
     * @throws JSONParseException When the array being parsed 
     * isn't a valid JSON array.
     */
    private Arr<? extends Obj> parseArr() {
        List<Obj> arr = new LinkedList<>();
        idx++;
        for (boolean flag = false;; flag = true) {
            skipWS();
            throwIfEnd();
            if (jsonStr.charAt(idx) == ']') 
                return new Arr<>(arr);
            if (flag && jsonStr.charAt(idx++) != ',')
                throw new JSONParseException("Expected element delimiter");
            skipWS();
            throwIfEnd();
            arr.add(parseVal());
        }  
    }   

    /**
     * @return A {@link Str} that describes the {@link String}
     * that was parsed. Ignores characters following the end quote.
     * @throws JSONParseException When the end of {@link #jsonStr}
     * is reached before an unescaped quote, {@link #jsonStr}
     * contains a character that should have been escaped but wasn't, 
     * or a back slash isn't followed by a valid sequence of characters
     * that can be escaped.
     */
    private Str parseStr() {
        StringBuilder sb = new StringBuilder();
        for (idx++; ;idx++) {
            throwIfEnd();
            if (jsonStr.charAt(idx) == '\\') {
                if (++idx == len)
                    throw new JSONParseException("Unexpected end of JSON");
                sb.append(switch (jsonStr.charAt(idx)) {
                    case '"' -> '"';
                    case '\\' -> '\\';
                    case '/' -> '/';
                    case 'b' -> '\b';
                    case 'f' -> '\f';
                    case 'n' -> '\n';
                    case 'r' -> '\r';
                    case 't' -> '\t';
                    case 'u' -> {
                        try {
                            yield (char) (Integer.parseInt(jsonStr.substring(++idx, (idx+=3)+1), 16));
                        } catch (NumberFormatException nfe) {
                            throw new JSONParseException(nfe, "Expected escape sequence");
                        } catch (IndexOutOfBoundsException ioobe) {
                            throw new JSONParseException("Unexpected end of JSON");
                        }
                    }
                    default -> throw new JSONParseException("Expected escape sequence");
                });
            } else if (INVALID_STR_CHARS.contains(jsonStr.charAt(idx)))
                throw new JSONParseException("Unexpected literal character");
            else if (jsonStr.charAt(idx) == '"')
                return new Str(sb.toString());
            else
                sb.append(jsonStr.charAt(idx));
        }
    }

    /**
     * @return A {@link Num} that describes the {@link Number} 
     * that was parsed. The 
     * @throws JSONParseException When {@link #jsonStr} can't be parsed as a 
     * valid number from the specified index.
     */
    private Num<N> parseNum() {
        StringBuilder num = new StringBuilder();
        num.append(jsonStr.charAt(idx));
        if (jsonStr.charAt(idx) == '-')
            if (idx+1 == len || !DIGITS.contains(jsonStr.charAt(idx+1)))
                throw new JSONParseException("Expected a number");
            else num.append(jsonStr.charAt(++idx));
        if (jsonStr.charAt(idx) == '0')
            if (idx+1 == len)
                return new Num<N>(numParser.apply("0"));
            else if (jsonStr.charAt(idx+1) == '.')
                return parseDecimal(num);
            else if(jsonStr.charAt(idx) == 'e' || jsonStr.charAt(idx) == 'E')
                return parseSciNot(num);
            else return new Num<N>(numParser.apply("0"));
        while (idx+1 != len && DIGITS.contains(jsonStr.charAt(idx+1)))
            num.append(jsonStr.charAt(++idx));
        if (idx+1 != len) 
            if (jsonStr.charAt(idx+1) == 'e' || jsonStr.charAt(idx+1) == 'E')
                return parseSciNot(num);
            else if (jsonStr.charAt(idx+1) == '.')
                return parseDecimal(num);
        try {
            return new Num<N>(numParser.apply(num.toString()));
        } catch (NumberFormatException nfe) {
            throw new JSONParseException(nfe, "Could not parse number");
        }
    }

    /**
     * @param num A {@link StringBuilder} representing the 
     * digits that have been parsed so far. 
     * @return The parsed {@link Num}
     * @throws JSONParseException When
     */
    private Num<N> parseDecimal(StringBuilder num) {
        if (++idx+1 == len || !DIGITS.contains(jsonStr.charAt(idx+1)))
            throw new JSONParseException("Unexpected trailing decimal");
        num.append('.').append(jsonStr.charAt(++idx));
        while (idx+1 != len && DIGITS.contains(jsonStr.charAt(idx+1)))
            num.append(jsonStr.charAt(++idx));
        if (idx+1 != len && (jsonStr.charAt(idx+1) == 'e' || jsonStr.charAt(idx+1) == 'E'))
            return parseSciNot(num);
        try {
            return new Num<N>(numParser.apply(num.toString()));
        } catch (NumberFormatException nfe) {
            throw new JSONParseException(nfe, "Could not parse number");
        }
    }

    /**
     * @param num The non scientific notation part of the 
     * number to be parsed 
     * @return The parsed {@link Num}
     */
    private Num<N> parseSciNot(StringBuilder num) {
        if (++idx+1 == len)
            throw new JSONParseException("Unexpected end of JSON");
        num.append('e');
        if (jsonStr.charAt(idx+1) == '+' || jsonStr.charAt(idx+1) == '-')
            num.append(jsonStr.charAt(++idx));
        if (idx+1 == len)
            throw new JSONParseException("Unexpected end of JSON");
        if (!DIGITS.contains(jsonStr.charAt(idx+1)))
            throw new JSONParseException("Expected exponent");
        while (idx+1 != len && DIGITS.contains(jsonStr.charAt(idx+1)))
            num.append(jsonStr.charAt(++idx));
        String numStr = num.toString();
        try {
            return new Num<N>(numParser.apply(numStr));
        } catch (NumberFormatException nfe) {
            throw new JSONParseException(nfe, "Could not parse number");
        }
    }

    /**
     * @return An {@link Bool} that describes the boolean 
     * that was parsed.
     * @throws JSONParseException When the characters
     * starting at index {@link #idx} in {@link #jsonStr} are not an 
     * exact match to either {@code "true"} or {@code "false"}
     * (the characters following {@code "true"} and {@code "false"}
     * are completely ignored by this method).
     */
    private Bool parseBool() {
        StringBuilder bool = new StringBuilder();
        while (jsonStr.charAt(idx) != 'e')
            if (++idx != len)
                bool.append(jsonStr.charAt(idx));
            else 
                throw new JSONParseException("Expected boolean");
        if (bool.toString().equals("rue"))
            return Bool.TRUE;
        else if (bool.toString().equals("alse"))
            return Bool.FALSE;
        throw new JSONParseException("Expected boolean");
    }

    /**
     * @return A {@link Null} that describes a {@code null} value.
     * Ignores the characters after {@code "null"}.
     * @throws JSONParseException When the four characters starting
     * at {@link #idx} in {@link #jsonStr} don't correspond exactly to the 
     * {@link String} {@code "null"}.
     */
    private Null parseNull() {
        try {
            if (jsonStr.indexOf("null", idx)+3 == (idx+=3))
                return Null.NULL;
            else 
                throw new JSONParseException("Expected null");
        } catch (IndexOutOfBoundsException ioobe) {
            throw new JSONParseException("Unexpected end of JSON");
        }
    }

    /**
     * @return An {@link Obj} representing the JSON value 
     * that was parsed.
     * @throws JSONParseException When the {@code jsonStr.charAt(idx)}
     * does not correspond to the start character of a JSON value.
     */
    private Obj parseVal() {
        Obj val = switch (jsonStr.charAt(idx)) {
            case 't', 'f' -> parseBool();
            case 'n' -> parseNull();
            case '-','0','1','2','3','4','5','6','7','8','9'
                -> parseNum();
            case '"' -> parseStr();
            case '[' -> parseArr();
            case '{' -> parseMap();
            default -> throw new JSONParseException("Expected value");
        };
        idx++;
        return val;
    }

    /**
     * Increments {@link #idx} until either {@code jsonStr.charAt(idx)}
     * isn't a member of {@link #WHITESPACE} or {@code idx == len}.
     */
    private void skipWS() {
        while(idx != len)
            if (WHITESPACE.contains(jsonStr.charAt(idx)))
                idx++;
            else break;
    }

    /**
     * @throws JSONParseException When {@code idx == len}
     */
    private void throwIfEnd() {
        if (idx == len)
            throw new JSONParseException("Unexpected end of JSON");
    }

    @SuppressWarnings("rawtypes")
    public static String stringify(Obj obj) {
        if (obj == null) throw new NullPointerException();
        return switch (obj) {
            case Map map -> stringifyMap(map);
            case Arr arr -> stringifyArr(arr);
            case Str str -> stringifyStr(str);
            default -> obj.toString();
        };
    }

    private static String stringifyMap(Map<? extends Obj> map) {
        StringBuilder sb = new StringBuilder().append('{');
        for (java.util.Map.Entry<String, ? extends Obj> e : map)
            // method chaining is fun
            sb.append('"').append(e.getKey()).append('"').append(':')
                .append(stringify(e.getValue())).append(',');
        return sb.delete(sb.length()-1, sb.length()).append('}').toString();
    }
    
    private static String stringifyArr(Arr<? extends Obj> arr) {
        StringBuilder sb = new StringBuilder().append('[');
        for (Obj e : arr)
            sb.append(stringify(e)).append(',');
        return sb.delete(sb.length()-1, sb.length()).append(']').toString();
    }

    private static String stringifyStr(Str str) {
        StringBuilder sb = new StringBuilder().append('"');
        for (char ch : str.toString().toCharArray()) {
            sb.append(switch(ch) {
                case '\b' -> "\\b";
                case '\f' -> "\\f";
                case '\n' -> "\\n";
                case '\r' -> "\\r";
                case '\t' -> "\\t";
                case '\\' -> "\\\\";
                case '"' -> "\\\"";
                default -> ch;
            });
        }
        return sb.append('"').toString();
    }
}