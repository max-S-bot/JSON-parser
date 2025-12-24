package io.github.mxz_schwarz.parser;

import static io.github.mxz_schwarz.parser.Type.*;
import java.util.Map;
import java.util.List;

/**
 * A wrapper {@code class} that can represent any JSON data.
 * This consists mainly of convenience methods to allow  
 * a client to perform less manual casting. 
 * @author max-S-bot
 */
public class Obj {

    static final Obj NULL = new Obj(null, 4, Type.NULL);
    static final Obj TRUE = new Obj(true, 4, BOOL);
    static final Obj FALSE = new Obj(false, 5, BOOL);

    private final Object val;
    final int numChars;
    private final Type type;

    private Obj(Object val, int numChars, Type type) {
        this.val  = val;
        this.numChars = numChars;
        this.type = type;
    } 
    
    Obj(Number val, int numChars) {
        this(val, numChars, NUM);
    }

    Obj(String val, int numChars) {
        this(val, numChars, STR);
    }

    Obj(List<Obj> val, int numChars) {
        this(val, numChars, ARR);
    }

    Obj(Map<String, Obj> val, int numChars) {
        this(val, numChars, OBJ);
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


    @SuppressWarnings("unchecked")
	public Obj[] asArr() throws CheckedJSONParseException {
        if (type == ARR)
            return ((List<Obj>) val).toArray(Obj[]::new);
        else 
            throw new CheckedJSONParseException("not an array");
    }

    @SuppressWarnings("unchecked")
    public List<Obj> asList() throws CheckedJSONParseException {
        if (type == ARR)
            return (List<Obj>) val;
        else 
            throw new CheckedJSONParseException("not an array");
    }

}
