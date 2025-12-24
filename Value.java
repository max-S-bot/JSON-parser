package io.github.mxz_schwarz.parser;

import java.util.Deque;
import java.util.LinkedList;

@Deprecated
record Value() {
    private static int endOfObj(String str, int i, Type t) {
        Deque<Character> stack = new LinkedList<>();
        stack.push(t == Type.ARR ? '[' : '{');
        String ch;
        for (;++i < str.length();)
            if (stack.isEmpty())
                return str.indexOf(",", i);
            else if ("[{".contains(ch = str.substring(i, i+1)))
                stack.push(ch.charAt(0));
            else if ("]}".contains(ch) && stack.pop().equals(ch.charAt(0)))
                throw new JSONParseException("Invalid "+(t == Type.ARR ? "array" : "object"));
        throw new JSONParseException("Invalid "+(t == Type.ARR ? "array" : "object"));
    }
}