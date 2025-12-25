package io.github.mxz_schwarz.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.LinkedList;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.function.Function;

/**
 * {@code class} that does most of the parsing 
 * legwork via the public overloaded {@code JSON.parse} methods.
 * @author max-S-bot
 */
public class JSON {

    private static final Set<Character> WHITESPACE = Set.of(' ', '\n', '\r', '\t');
    private static final Set<Character> DIGITS = Set.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'); 
    private static final Set<Character> INVALID_STR_CHARS = Set.of('\b', '\f', '\n', '\r', '\t');

    /**
     * @param jsonStr A {@code String} representing the 
     * JSON data to be parsed.
     * @return A {@code Obj} representing the parsed data.
     * @throws CheckedJSONException When {@code jsonStr}
     * does not represent valid JSON data.
     */
    public static Obj parse(String jsonStr) 
        throws CheckedJSONException {
        try {
            return new JSON(jsonStr).obj;
        } catch (JSONParseException jpe) {
            throw jpe.checked();
        }
    }

    /**
     * @param path A {@code java.nio.file.Path} instance 
     * that corresponds to a JSON file to be parsed.
     * @return A {@code Obj} representing the parsed JSON file.
     * @throws CheckedJSONException When 
     * {@code java.nio.file.Files.readString}
     * {@code throws} an {@code IOException} or when {@code parse} 
     * {@code throws} a {@code CheckedJSONException}.
     */
    public static Obj parse(java.nio.file.Path path)
        throws CheckedJSONException {
        try {
            return parse(java.nio.file.Files.readString(path));
        } catch (java.io.IOException ioe) {
            throw new CheckedJSONException(ioe);
        }
    }

    private final String jsonStr;
    private final Obj obj;
    private int idx = 0;

    private JSON(String jsonStr) {
        this.jsonStr = jsonStr;
        skipWS();
        this.obj = parseVal();
        skipWS();
        if (idx != jsonStr.length())
            throw new JSONParseException("Invalid JSON at "+idx);
    }

    /**
     * @return A {@code Map} that represents the
     * parsed object.
     * @throws JSONParseException when the given object is invalid
     */
    private Map parseObj() {
        java.util.Map<String, Obj> obj = new HashMap<>();
        skipWS();
        if (idx >= jsonStr.length())
            throw new JSONParseException("Invalid object at "+idx);
        if (jsonStr.charAt(idx) == '}')
            return new Map(obj);
        Str name = parseStr();
        skipWS();
        if (jsonStr.charAt(idx++) != ':')
            throw new JSONParseException("Invalid object at "+idx-1);
        skipWS();
        obj.put(name.asStr(), parseVal());
        for (;;) {
            skipWS();
            if (idx >= jsonStr.length())
                throw new JSONParseException("Invalid object at "+idx);
            if (jsonStr.charAt(idx) == '}')
                return new Map(obj);
            if (jsonStr.charAt(idx) != ',')
                throw new JSONParseException("Invalid object at "+idx);
            skipWS();
            if (jsonStr.charAt(idx) != '"')
                throw new JSONParseException("Invalid object at "+idx);
            name = parseStr();
            skipWS();
            if (jsonStr.charAt(idx) != ':')
                throw new JSONParseException("Invalid object at "+idx);
            skipWS();
            obj.put(name.asStr(), parseVal());
        }
    }

    /**
     * @return A {@code Arr} representing the parsed array.
     * @throws JSONParseException When the array being parsed 
     * isn't a valid JSON array.
     */
    private Arr parseArr() {
        List<Obj> arr = new LinkedList<>();
        skipWS();
        if (idx >= jsonStr.length())
            throw new JSONParseException("Invalid array at index: "+idx);
        if (jsonStr.charAt(idx) == ']')
            return new Arr(arr);
        arr.add(parseVal());
        for (;;) {
            skipWS();
            if (idx >= jsonStr.length())
                throw new JSONParseException("Invalid array at index: "+idx);
            if (jsonStr.charAt(idx) == ']') 
                return new Arr(arr);  
            if (jsonStr.charAt(idx) != ',')
                throw new JSONParseException("Invalid array at index: "+idx);
            skipWS();
            arr.add(parseVal());
        }  
    }   

    /**
     * @return A {@code Num} that describes the {@code Number} 
     * that was parsed. If the number is an integer the {@code Number}
     * is a {@code Long} if it can be precisely represented by one, otherwise
     * it is a {@code BigInteger}. If the number is a decimal, it's parsed 
     * as a {@code Double}(again, if it can be precisely represented by one),
     * otherwise it is a {@code BigDecimal}.
     * @throws JSONParseException When {@code jsonStr} can't be parsed as a 
     * valid number from the specified index.
     */
    // needs to handle scientific notation.
    private Num parseNum() {
        boolean decimal = false;
        StringBuilder num = new StringBuilder();
        do num.append(jsonStr.charAt(idx++));
        while (DIGITS.contains(jsonStr.charAt(idx)) || 
        (!decimal && (decimal = jsonStr.charAt(idx) == '.')));
        if (num.charAt(num.length()-1) == '.' || 
        (num.charAt(0) == 0 && num.length() > 1 && num.charAt(1) != '.'))
            throw new JSONParseException("Invalid Number at "+idx);
        Function<String, Number> primParser = decimal ? Double::parseDouble : Long::parseLong;
        Function<String, Number> bigParser = decimal ? BigDecimal::new : BigInteger::new;
        try {
            return new Num(primParser.apply(num.toString()));
        } catch (NumberFormatException nfe) {
            return new Num(bigParser.apply(num.toString()));
        }
    }

    /**
     * @return An {@code Bool} that describes the boolean 
     * that was parsed.
     * @throws JSONParseException When the characters
     * starting at index {@code idx} in {@code jsonStr} are not an 
     * exact match to either {@code "true"} or {@code "false"}
     * (the characters following {@code "true"} and {@code "false"}
     * are completely ignored by this method).
     */
    private Bool parseBool() {
        int end = jsonStr.indexOf("e", idx);
        if (end == -1) 
            throw new JSONParseException("Invalid boolean at "+idx);
        String b = jsonStr.substring(idx, end);
        if (b.equals("true"))
            return Bool.TRUE;
        else if (b.equals("false"))
            return Bool.FALSE;
        throw new JSONParseException("Invalid boolean at "+idx);
    }

    /**
     * @return A {@code Obj} that describes the {@code String}
     * that was parsed. Ignores characters following the end quote.
     * @throws JSONParseException When the end of {@code jsonStr}
     * is reached before an unescaped quote, {@code jsonStr}
     * contains a character that should have been escaped but wasn't, 
     * or a back slash isn't followed by a valid sequence of characters
     * that can be escaped.
     */
    private Str parseStr() {
        StringBuilder sb = new StringBuilder();
        for (; jsonStr.charAt(++idx) != '"';) {
            if (jsonStr.charAt(idx) == '\\')
                sb.append(switch (jsonStr.charAt(++idx)) {
                    case '"' -> '"';
                    case '\\' -> '\\';
                    case '/' -> '/';
                    case 'b' -> '\b';
                    case 'f' -> '\f';
                    case 'n' -> '\n';
                    case 'r' -> '\r';
                    case 't' -> '\t';
                    case 'u' -> (char) ('\u0000' + // this needs more error handling
                    Integer.parseInt(jsonStr.substring(idx, idx+=4), 16)); 
                    default -> throw new JSONParseException("Invalid escape sequence at "+idx);
                });
            else if (INVALID_STR_CHARS.contains(jsonStr.charAt(idx)))
                throw new JSONParseException("Invalid literal character at "+idx);
            else if (idx == jsonStr.length()-1)
                throw new JSONParseException("Invalid String at "+idx);
            else
                sb.append(jsonStr.charAt(idx));
        }
        return new Str(sb.toString());
    }

    /**
     * @return A {@code Obj} that describes a {@code null} value.
     * Ignores the characters after {@code "null"}.
     * @throws JSONParseException When the four characters starting
     * at {@code idx} in {@code jsonStr} don't correspond exactly to the 
     * {@code String} {@code "null"}.
     */
    private Null parseNull() {
        if (jsonStr.indexOf("null", idx) == idx)
            return Null.NULL;
        else 
            throw new JSONParseException("Invalid null at "+idx);
    }

    /**
     * @return An {@code Obj} representing the JSON value 
     * that was parsed.
     * @throws JSONParseException When the {@code jsonStr.charAt(idx)}
     * does not correspond to the start character of a JSON value.
     */
    private Obj parseVal() {
        return switch (jsonStr.charAt(idx)) {
            case 't', 'f' -> parseBool();
            case 'n' -> parseNull();
            case '-','0','1','2','3','4','5','6','7','8','9'
                -> parseNum();
            case '"' -> parseStr();
            case '[' -> parseArr();
            case '{' -> parseObj();
            default -> throw new JSONParseException("Invalid value at index: "+idx);
        };
    }

    private void skipWS() {
        while(idx < jsonStr.length())
            if (WHITESPACE.contains(jsonStr.charAt(idx)))
                idx++;
            else break;
    }
}