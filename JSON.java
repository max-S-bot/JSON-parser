package io.github.mxz_schwarz.parser;

import java.util.Map;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.regex.*;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.function.Function;
import static io.github.mxz_schwarz.parser.Type.*;

public class JSON {

    private static final String WHITESPACE = " \n\r\t";
    private static final Pattern NAME_REGEX = Pattern.compile("\\s*\"[a-zA-Z][a-zA-Z0-9_]*\"\\s*");
    private static final String INVALID_STR_CHARS = "\b\f\n\r\t";

    private JSON() {}

    public static Obj parse(String jsonStr) 
        throws CheckedJSONParseException {
        int idx = 0;
        while (WHITESPACE.contains(jsonStr.substring(idx, idx+1)))
            idx++;
        try {
            return parseObj(jsonStr, idx);
        } catch (JSONParseException jpe) {
            throw new CheckedJSONParseException(jpe);
        }
    }

    static Obj parseObj(String objStr, int idx) {
        Map<String, Obj> obj = new HashMap<>();
        int start = idx;
        int n = objStr.length();
        for (; idx != n;) {
            int curIdx = objStr.indexOf(":", idx);
            if (curIdx == -1) 
                throw new JSONParseException("Invalid entry");
            String name = objStr.substring(idx+1, curIdx++);
            if (!NAME_REGEX.matcher(name).matches())
                throw new JSONParseException("Invalid key");
            while (WHITESPACE.contains(objStr.substring(curIdx, curIdx+1)))
                curIdx++;
            Type type = Type.from(objStr.charAt(curIdx)); 
            obj.put(name, type.parser.apply(objStr, curIdx));
            idx = curIdx + obj.get(name).numChars;
        }
        //fix
        return new Obj(obj, idx-start);
    }

    static Obj parseArr(String arrStr, int idx) {
        final int n = arrStr.length();
        List<Obj> arr = new LinkedList<>();
        for (; idx != n;) {
            while (WHITESPACE.contains(arrStr.substring(idx, idx+1)))
                idx++;
            Type type = Type.from(arrStr.charAt(idx));
            arr.add(type.parser.apply(arrStr, idx));
            idx = idx + arr.getLast().numChars;
        }
        // fix
        return new Obj(arr.toArray(Obj[]::new), -1);   
    }   

    /**
     * 
     * @param numStr a {@code String} such that
     * {@code (numStr.charAt(idx) >= '0' && numStr.charAt(idx) <= '9')
     *  || numStr.charAt(idx) == '-'}
     * @param idx the index in {@code numStr} at which the number to
     * be parsed starts
     * @return an {@code Obj} instance that describes the {@code Number} 
     * that was parsed. If the number is an integer the {@code Number}
     * returns a {@code Long} if it can be precisely represented by one.
     * If the number is a decimal, the 
     *  
     * @throws JSONParseException when
     */
    static Obj parseNum(String numStr, int idx) {
        int end = numStr.indexOf(",", idx);
        // needs more stuff to handle whether this is 
        // in an object or an array.
        if (end == -1) end = numStr.indexOf("}", idx);

        boolean decimal = numStr.indexOf(".", idx) != -1; 
        Function<String, Number> primParser = decimal ? Double::parseDouble : Long::parseLong;
        Function<String, Number> bigParser = decimal ? BigDecimal::new : BigInteger::new;
        try {
            return new Obj(primParser.apply(num.toString()), -1, NUM);
        } catch (NumberFormatException nfe) {
            return new Obj(bigParser.apply(num.toString()), -1, NUM);
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
    static Obj parseBool(String bool, int idx) {
        int end = bool.indexOf("e", idx);
        if (end == -1) 
            throw new JSONParseException("Invalid boolean");
        String b = bool.substring(idx, end);
        if (b.equals("true"))
            return new Obj(true, 4);
        else if (b.equals("false"))
            return new Obj(false, 5);
        throw new JSONParseException("Invalid boolean");
    }

    /**
     * @param str {@code String} such that 
     * {@code str.charAt(idx) == '"'}
     * @param idx the index at which to begin 
     * parsing a {@code String} from {@code str}
     * @return an Obj
     * @throws JSONParseException When the end of {@code str}
     * is reached before an unescaped quote, {@code str}
     * contains a character that should have been escaped but wasn't, 
     * or a back slash isn't followed by a valid sequence of characters
     * that can be escaped.
     */
    static Obj parseStr(String str, int idx) {
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
                    default -> throw new JSONParseException("Invalid character");
                });
            else if (INVALID_STR_CHARS.contains(str.substring(idx, idx+1)))
                throw new JSONParseException("Invalid literal character");
            else if (idx == str.length()-1)
                throw new JSONParseException("Invalid String");
            else
                sb.append(str.charAt(idx));
        }
        return new Obj(sb.toString(), idx-start);
    }
    /**
     * @param nullStr A {@code String} such that 
     * {@code nullStr.charAt(idx) == 'n'}
     * @param idx the index from which to start parsing {@code null}
     * @return an {@code Obj} instance that describes a {@code null} value;
     * @throws JSONParseException When the four characters starting
     * at {@code idx} in {@code nullStr} don't correspond exactly to the 
     * {@code String} {@code "null"}
     */
    static Obj parseNull(String nullStr, int idx) {
        if (nullStr.indexOf("null", idx) == idx)
            return new Obj((Object) null, 4);
        else 
            throw new JSONParseException("invalid null");
    }

    /**
     * 
     * @param path a {@code java.nio.file.Path} instance 
     * that corresponds to a JSON file to be parsed.
     * @return an {@code Obj} representing the parsed JSON file.
     * @throws CheckedJSONParseException when 
     * {@code java.nio.file.Files.readString}
     * throws an IOException or when {@code parse} 
     * throws a {@code CheckedJSONParseException}
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