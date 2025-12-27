package io.github.mxz_schwarz.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.LinkedList;
import java.math.BigInteger;
import java.math.BigDecimal;

/**
 * {@code class} that does most of the parsing legwork via the {@code public} 
 * {@link JSON#parse(String)} and {@link JSON#parse(java.nio.file.Path)} methods.
 * @author max-S-bot
 */
public class JSON {

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
    public static Obj parse(String jsonStr) 
        throws JSONException {
        try {
            return new JSON(jsonStr).obj;
        } catch (JSONParseException jpe) {
            throw new JSONException(jpe);
        }
    }

    /**
     * Parses a file containing JSON data.
     * @param path A {@link java.nio.file.Path} instance 
     * that corresponds to a JSON file to be parsed.
     * @return A {@link Obj} representing the parsed JSON file.
     * @throws JSONException When {@link java.nio.file.Files#readString}
     * {@code throws} an {@link java.io.IOException} or when {@link parse} 
     * {@code throws} a {@link JSONException}.
     */
    public static Obj parse(java.nio.file.Path path)
        throws JSONException {
        try {
            return parse(java.nio.file.Files.readString(path));
        } catch (java.io.IOException ioe) {
            throw new JSONException(ioe, "IO exception");
        }
    }

    /**
     * The {@link String} representing the JSON 
     * data to parse.
     */
    private final String jsonStr;
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
    private JSON(String jsonStr) {
        this.jsonStr = jsonStr;
        this.len = jsonStr.length(); 
        skipWS();
        this.obj = parseVal();
        skipWS();
        if (idx != len)
            throw new JSONParseException("Invalid JSON at "+idx);
    }

    /**
     * @return A {@link Map} that represents the
     * parsed object.
     * @throws JSONParseException when the given object is invalid
     */
    private Map parseObj() {
        java.util.Map<String, Obj> obj = new HashMap<>();
        for (boolean flag = false;; flag = true) {
            skipWS();
            if (idx == len)
                throw new JSONParseException("Unexpected end of JSON at "+idx);
            if (jsonStr.charAt(idx) == '}')
                return new Map(obj);
            if (flag && jsonStr.charAt(idx++) != ',')
                throw new JSONParseException("Expected entry delimiter at "+(idx-1));
            skipWS();
            if (idx == len)
                throw new JSONParseException("Unexpected end of JSON at "+idx);
            if (jsonStr.charAt(idx) != '"')
                throw new JSONParseException("Expected identifier at "+idx);
            String name = ((Str) parseVal()).asStr(); 
            skipWS();
            if (jsonStr.charAt(idx++) != ':')
                throw new JSONParseException("Expected entry at "+(idx-1));
            skipWS();
            obj.put(name, parseVal());
        }
    }

    /**
     * @return A {@link Arr} representing the parsed array.
     * @throws JSONParseException When the array being parsed 
     * isn't a valid JSON array.
     */
    private Arr parseArr() {
        List<Obj> arr = new LinkedList<>();
        for (boolean flag = false;; flag = true) {
            skipWS();
            if (idx == len)
                throw new JSONParseException("Unexpected end of JSON at "+idx);
            if (jsonStr.charAt(idx) == ']') 
                return new Arr(arr);  
            if (flag && jsonStr.charAt(idx++) != ',')
                throw new JSONParseException("Expected element delimiter at "+(idx-1));
            skipWS();
            if (idx == len)
                throw new JSONParseException("Unexpected end of JSON at "+idx);
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
        for (idx++; ;idx++)
            if (idx == len)
                throw new JSONParseException("Unexpected end of JSON at "+idx);
            else if (jsonStr.charAt(idx) == '\\')
                sb.append(switch (jsonStr.charAt(++idx)) {
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
                            throw new JSONParseException(nfe, "Expected escape sequence at "+idx);
                        }
                    }
                    default -> throw new JSONParseException("Expected escape sequence at "+idx);
                });
            else if (INVALID_STR_CHARS.contains(jsonStr.charAt(idx)))
                throw new JSONParseException("Unexpected literal character at "+idx);
            else if (jsonStr.charAt(idx) == '"')
                return new Str(sb.toString());
            else
                sb.append(jsonStr.charAt(idx));
    }

    /**
     * @return A {@link Num} that describes the {@link Number} 
     * that was parsed. If the number is an integer the {@link Number}
     * is a {@link Long} if it can be precisely represented by one, otherwise
     * it is a {@link BigInteger}. If the number is a decimal, it's parsed 
     * as a {@link Double} (again, if it can be precisely represented by one),
     * otherwise it is a {@link BigDecimal}.
     * @throws JSONParseException When {@link #jsonStr} can't be parsed as a 
     * valid number from the specified index.
     */
    private Num parseNum() {
        StringBuilder num = new StringBuilder();
        num.append(jsonStr.charAt(idx));
        if (jsonStr.charAt(idx) == '-')
            if (idx+1 == len || !DIGITS.contains(jsonStr.charAt(idx+1)))
                throw new JSONParseException("Expected a number at "+idx);
            else num.append(jsonStr.charAt(++idx));
        if (jsonStr.charAt(idx) == '0')
            if (idx+1 == len)
                return Num.ZERO;
            else if (jsonStr.charAt(idx+1) == '.')
                return parseDecimal(num);
            else if(jsonStr.charAt(idx) == 'e' || jsonStr.charAt(idx) == 'E')
                return parseSciNot(num);
            else return Num.ZERO;
        while (idx+1 != len && DIGITS.contains(jsonStr.charAt(idx+1)))
            num.append(jsonStr.charAt(++idx));
        if (idx+1 != len) 
            if (jsonStr.charAt(idx+1) == 'e' || jsonStr.charAt(idx+1) == 'E')
                return parseSciNot(num);
            else if (jsonStr.charAt(idx+1) == '.')
                return parseDecimal(num);
        try {
            return new Num(Long.parseLong(num.toString()));
        } catch (NumberFormatException nfe) {
            return new Num(new BigInteger(num.toString()));
        }
    }

    private Num parseDecimal(StringBuilder num) {
        if (++idx+1 == len || !DIGITS.contains(jsonStr.charAt(idx+1)))
            throw new JSONParseException("Unexpected trailing decimal at "+idx);
        num.append('.').append(jsonStr.charAt(++idx));
        while (idx+1 != len && DIGITS.contains(jsonStr.charAt(idx+1)))
            num.append(jsonStr.charAt(++idx));
        if (idx+1 != len && (jsonStr.charAt(idx+1) == 'e' || jsonStr.charAt(idx+1) == 'E'))
            return parseSciNot(num);
        try {
            return new Num(Double.parseDouble(num.toString()));
        } catch (NumberFormatException nfe) {
            return new Num(new BigDecimal(num.toString()));
        }
    }

    private Num parseSciNot(StringBuilder num) {
        if (++idx+1 == len)
            throw new JSONParseException("Unexpected end of JSON at "+idx);
        num.append('e');
        if (jsonStr.charAt(idx+1) == '+' || jsonStr.charAt(idx+1) == '-')
            num.append(jsonStr.charAt(++idx));
        if (idx+1 == len)
            throw new JSONParseException("Unexpected end of JSON at "+idx);
        if (!DIGITS.contains(jsonStr.charAt(idx+1)))
            throw new JSONParseException("Expected exponent at "+idx);
        while (idx+1 != len && DIGITS.contains(jsonStr.charAt(idx+1)))
            num.append(jsonStr.charAt(++idx));
        String numStr = num.toString();
        try {
            return new Num(Double.parseDouble(numStr));
        } catch (NumberFormatException nfe) {
            return new Num(new BigDecimal(numStr));
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
        if (jsonStr.charAt(idx) != 't' && jsonStr.charAt(idx) != 'f')
            throw new JSONParseException("Precondition violated at "+idx);
        StringBuilder bool = new StringBuilder();
        while (jsonStr.charAt(idx) != 'e')
            if (++idx != len)
                bool.append(jsonStr.charAt(idx));
            else 
                throw new JSONParseException("Expected boolean at "+idx);
        if (bool.toString().equals("rue"))
            return Bool.TRUE;
        else if (bool.toString().equals("alse"))
            return Bool.FALSE;
        throw new JSONParseException("Expected boolean at "+idx);
    }

    /**
     * @return A {@link Null} that describes a {@code null} value.
     * Ignores the characters after {@code "null"}.
     * @throws JSONParseException When the four characters starting
     * at {@link #idx} in {@link #jsonStr} don't correspond exactly to the 
     * {@link String} {@code "null"}.
     */
    private Null parseNull() {
        if (jsonStr.charAt(idx) != 'n')
            throw new JSONParseException("Precondition violated at "+idx);
        if (jsonStr.indexOf("null", idx)+3 == (idx+=3))
            return Null.NULL;
        else 
            throw new JSONParseException("Expected null at "+idx);
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
            case '{' -> parseObj();
            default -> throw new JSONParseException("Expected value at "+idx);
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
}