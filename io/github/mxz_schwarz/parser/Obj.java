package io.github.mxz_schwarz.parser;

import java.util.function.Function;
import static io.github.mxz_schwarz.parser.Type.*;
import java.util.Map;

// might replace constructors
// with static factory methods
// in order to cache booleans and null.  
public class Obj {

    private final Object val;
    final int numChars;
    private final Type type;
    
    Obj(Number val, int numChars) {
        this.val = val;
        this.numChars = numChars;
        this.type = NUM;
    }

    Obj(String val, int numChars) {
        this.val = val;
        this.numChars = numChars;
        this.type = STR;
    }

    Obj(boolean val, int numChars) {
        this.val = val;
        this.numChars = numChars;
        this.type = BOOL;
    }

    Obj(Object[] val, int numChars) {
        this.val = val;
        this.numChars = numChars;
        this.type = ARR;
    }

    Obj(Map<String, Obj> val, int numChars) {
        this.val = val;
        this.numChars = numChars;
        this.type = OBJ;
    }

    Obj(Object val, int numChars) {
        if (val != null)
            throw new JSONParseException("expected null");
        this.val = val;
        this.numChars = numChars;
        this.type = NULL;
    }

    public Object get() {
        return val;
    }

    public Number asNum() throws CheckedJSONParseException {
        if (type == NUM)
            return (Number) val;
        else 
            throw new CheckedJSONParseException("not a Number");
    }

    public String asStr() throws CheckedJSONParseException {
        if (type == STR) 
            return (String) val;
        else 
            throw new CheckedJSONParseException("not a String");
    }

    public boolean asBool() throws CheckedJSONParseException {
        if (type == BOOL)
            return (Boolean) val;
        else 
            throw new CheckedJSONParseException("not a Boolean");
    }


    public <T> T[] asArr(Function<Integer, T[]> constructor) 
        throws CheckedJSONParseException {
        if (type != ARR)
            throw new CheckedJSONParseException("not an array");
        Object[] objArr = (Object[]) val;
        T[] arr = constructor.apply(objArr.length);
        // do stuff here
        for (int i=0; i<arr.length; i++);
        return arr;
    }

}
