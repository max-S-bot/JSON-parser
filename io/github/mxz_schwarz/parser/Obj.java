package io.github.mxz_schwarz.parser;

import java.util.Map;
import java.util.Objects;
import java.util.List;

/**
 * A wrapper {@code abstract class} that can represent any JSON data.
 * This consists mainly of convenience methods to allow  
 * a client to perform less manual casting. 
 * @author max-S-bot
 */
public abstract class Obj {

    public Number asNum() throws CheckedJSONParseException {
        throw new CheckedJSONParseException("not a Number");
    }

    public String asStr() throws CheckedJSONParseException {
        throw new CheckedJSONParseException("not a String");
    }

    public boolean asBool() throws CheckedJSONParseException {
        throw new CheckedJSONParseException("not a boolean");
    }

    public Object asNull() throws CheckedJSONParseException {
       throw new CheckedJSONParseException("not null");
    }

	public Obj[] asArr() throws CheckedJSONParseException {
        throw new CheckedJSONParseException("not an array");
    }

    public List<Obj> asList() throws CheckedJSONParseException {
        throw new CheckedJSONParseException("not an array");
    }

    public Map<String, Obj> asMap() throws CheckedJSONParseException {
        throw new CheckedJSONParseException("not a Map");
    }

    abstract int numChars();

    abstract Object val();

    @Override
    public String toString() {
        return val().toString();
    }

    
    public boolean equals(Object o) {
        if (o instanceof Obj obj)
            return Objects.equals(val(), obj.val());
        else return false;
    }

}
