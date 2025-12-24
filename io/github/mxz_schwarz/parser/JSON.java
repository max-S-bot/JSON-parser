package io.github.mxz_schwarz.parser;

import java.util.HashMap;
import java.util.List;
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

    private static final String WHITESPACE = " \n\r\t";
    private static final java.util.regex.Pattern NAME_REGEX = 
        java.util.regex.Pattern.compile("\"[a-zA-Z][a-zA-Z0-9_]*\"\\s*");
    private static final String INVALID_STR_CHARS = "\b\f\n\r\t";
    private static final String DIGITS = "0123456789"; 

    private JSON() {}

    /**
     * @param jsonStr A {@code String} representing the 
     * JSON data to be parsed.
     * @return A {@code Obj} representing the parsed data.
     * @throws CheckedJSONParseException When {@code jsonStr}
     * does not represent valid JSON data.
     */
    public static Obj parse(String jsonStr) 
        throws CheckedJSONParseException {
        int idx = 0;
        while (WHITESPACE.contains(jsonStr.substring(idx, idx+1)))
            idx++;
        try {
            Obj obj = parseVal(jsonStr, idx);
            idx += obj.numChars();
            if (jsonStr.substring(idx).trim().length() != 0)
                throw new JSONParseException("Invalid JSON");
            return obj;
        } catch (JSONParseException jpe) {
            throw new CheckedJSONParseException(jpe);
        }
    }

    /**
     * @param objStr A {@code String} such that
     * {@code objStr.charAt(idx) == '{'}.
     * @param idx The index at which to start 
     * parsing the object.
     * @return A {@code Obj} that represents the
     * parsed object.
     * @throws JSONParseException
     */
    private static Obj parseObj(String objStr, int idx) {
        int start = idx++;
        java.util.Map<String, Obj> obj = new HashMap<>();
        // I'll refactor so that this no longer 
        // uses regexps to match identifiers.
        for (; objStr.charAt(idx) != '}';) {
            int curIdx = objStr.indexOf(":", idx);
            if (curIdx == -1) 
                throw new JSONParseException("Invalid entry");
            String name = objStr.substring(idx+1, curIdx++);
            if (!NAME_REGEX.matcher(name).matches())
                throw new JSONParseException("Invalid key");
            while (WHITESPACE.contains(objStr.substring(curIdx, curIdx+1)))
                curIdx++;
            obj.put(name, parseVal(objStr, curIdx));
            idx = curIdx + obj.get(name).numChars();
            if (objStr.charAt(idx) != ',' && objStr.charAt(idx) != '}')
                throw new JSONParseException("Invalid object");
        }
        return new Map(obj, idx - start);
    }

    /**
     * @param arrStr A {@code String} such that 
     * {@code arrStr.charAt(idx) == '['}.
     * @param idx The index at which to start parsing
     * {@code arrStr}.
     * @return A {@code Obj} representing the parsed array.
     * @throws JSONParseException When the array being parsed 
     * isn't a valid JSON array.
     */
    private static Obj parseArr(String arrStr, int idx) {
        int start = idx;
        List<Obj> arr = new LinkedList<>();
        for (; arrStr.charAt(idx) != ']'; idx += arr.getLast().numChars()) {
            while (WHITESPACE.contains(arrStr.substring(idx, idx+1)))
                idx++;
            arr.add(parseVal(arrStr, idx));
            while (WHITESPACE.contains(arrStr.substring(idx, idx+1)))
                idx++;
            if (arrStr.charAt(idx) != ',' && arrStr.charAt(idx) != ']')
                throw new JSONParseException("Invalid array");
        }
        return new Arr(arr, idx - start);   
    }   

    /**
     * @param numStr A {@code String} such that
     * {@code (numStr.charAt(idx) >= '0' && numStr.charAt(idx) <= '9')
     *  || numStr.charAt(idx) == '-'}.
     * @param idx The index in {@code numStr} at which the number to
     * be parsed starts.
     * @return A {@code Obj} that describes the {@code Number} 
     * that was parsed. If the number is an integer the {@code Number}
     * is a {@code Long} if it can be precisely represented by one, otherwise
     * it is a {@code BigInteger}. If the number is a decimal, it's parsed 
     * as a {@code Double}(again, if it can be precisely represented by one),
     * otherwise it is a {@code BigDecimal}.
     * @throws JSONParseException When {@code numStr} can't be parsed as a 
     * valid number from the specified index.
     */
    // needs to handle scientific notation.
    private static Obj parseNum(String numStr, int idx) {
        boolean decimal = false;
        StringBuilder num = new StringBuilder();
        do num.append(numStr.charAt(idx++));
        while (DIGITS.contains(numStr.substring(idx, idx+1)) || 
        (!decimal && (decimal = numStr.charAt(idx) == '.')));
        if (num.charAt(num.length()-1) == '.' || 
        (num.charAt(0) == 0 && num.length() > 1 && num.charAt(1) != '.'))
            throw new JSONParseException("Invalid Number");
        Function<String, Number> primParser = decimal ? Double::parseDouble : Long::parseLong;
        Function<String, Number> bigParser = decimal ? BigDecimal::new : BigInteger::new;
        try {
            return new Num(primParser.apply(num.toString()), num.length());
        } catch (NumberFormatException nfe) {
            return new Num(bigParser.apply(num.toString()), num.length());
        }
    }

    /**
     * @param bool A {@code String} such that 
     * {@code bool.charAt(idx) == 't' || bool.charAt(idx) == 'f'}.
     * @param idx The index in {@code bool} at which the 
     * {@code boolean} to be parsed starts.
     * @return An {@code Obj} instance that describes the boolean 
     * that was parsed.
     * @throws JSONParseException When the characters
     * starting at index {@code idx} in {@code bool} are not an 
     * exact match to either {@code "true"} or {@code "false"}
     * (the characters following {@code "true"} and {@code "false"}
     * are completely ignored by this method).
     */
    private static Obj parseBool(String bool, int idx) {
        int end = bool.indexOf("e", idx);
        if (end == -1) 
            throw new JSONParseException("Invalid boolean");
        String b = bool.substring(idx, end);
        if (b.equals("true"))
            return Bool.TRUE;
        else if (b.equals("false"))
            return Bool.FALSE;
        throw new JSONParseException("Invalid boolean");
    }

    /**
     * @param str A {@code String} such that 
     * {@code str.charAt(idx) == '"'}.
     * @param idx The index at which to begin 
     * parsing a {@code String} from {@code str}.
     * @return A {@code Obj} that describes the {@code String}
     * that was parsed.
     * @throws JSONParseException When the end of {@code str}
     * is reached before an unescaped quote, {@code str}
     * contains a character that should have been escaped but wasn't, 
     * or a back slash isn't followed by a valid sequence of characters
     * that can be escaped.
     */
    private static Obj parseStr(String str, int idx) {
        StringBuilder sb = new StringBuilder();
        int start = idx;
        for (; str.charAt(++idx) != '"';) {
            if (str.charAt(idx) == '\\')
                sb.append(switch (str.charAt(++idx)) {
                    case '"' -> '"';
                    case '\\' -> '\\';
                    case '/' -> '/';
                    case 'b' -> '\b';
                    case 'f' -> '\f';
                    case 'n' -> '\n';
                    case 'r' -> '\r';
                    case 't' -> '\t';
                    case 'u' -> (char) ('\u0000' + // this needs more error handling
                    Integer.parseInt(str.substring(idx, idx+=4), 16)); 
                    default -> throw new JSONParseException("Invalid escape character");
                });
            else if (INVALID_STR_CHARS.contains(str.substring(idx, idx+1)))
                throw new JSONParseException("Invalid literal character");
            else if (idx == str.length()-1)
                throw new JSONParseException("Invalid String");
            else
                sb.append(str.charAt(idx));
        }
        return new Str(sb.toString(), idx - start);
    }

    /**
     * @param nullStr A {@code String} such that 
     * {@code nullStr.charAt(idx) == 'n'}.
     * @param idx The index from which to start parsing 
     * {@code null} in {@code nullStr}.
     * @return A {@code Obj} that describes a {@code null} value.
     * @throws JSONParseException When the four characters starting
     * at {@code idx} in {@code nullStr} don't correspond exactly to the 
     * {@code String} {@code "null"}.
     */
    private static Obj parseNull(String nullStr, int idx) {
        if (nullStr.indexOf("null", idx) == idx)
            return Null.NULL;
        else 
            throw new JSONParseException("Invalid null");
    }

    private static Obj parseVal(String jsonStr, int idx) {
        return switch (jsonStr.charAt(idx)) {
            case 't', 'f' -> parseBool(jsonStr, idx);
            case 'n' -> parseNull(jsonStr, idx);
            case '-','0','1','2','3','4','5','6','7','8','9'
                -> parseNum(jsonStr, idx);
            case '"' -> parseStr(jsonStr, idx);
            case '[' -> parseArr(jsonStr, idx);
            case '{' -> parseObj(jsonStr, idx);
            default -> throw new JSONParseException("Invalid value");
        };
    }

    /**
     * @param path A {@code java.nio.file.Path} instance 
     * that corresponds to a JSON file to be parsed.
     * @return A {@code Obj} representing the parsed JSON file.
     * @throws CheckedJSONParseException When 
     * {@code java.nio.file.Files.readString}
     * {@code throws} an {@code IOException} or when {@code parse} 
     * {@code throws} a {@code CheckedJSONParseException}.
     */
    public static Obj parse(java.nio.file.Path path)
        throws CheckedJSONParseException {
        try {
            return parse(java.nio.file.Files.readString(path));
        } catch (java.io.IOException ioe) {
            throw new CheckedJSONParseException(ioe);
        }
    }
}