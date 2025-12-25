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

    public Number asNum() throws JSONException {
        throw new JSONException("not a Number");
    }

    public String asStr() throws JSONException {
        throw new JSONException("not a String");
    }

    public boolean asBool() throws JSONException {
        throw new JSONException("not a boolean");
    }

    public Object asNull() throws JSONException {
       throw new JSONException("not null");
    }

	public Obj[] asArr() throws JSONException {
        throw new JSONException("not an array");
    }

    public List<Obj> asList() throws JSONException {
        throw new JSONException("not an array");
    }

    public Map<String, Obj> asMap() throws JSONException {
        throw new JSONException("not a Map");
    }

    abstract Object val();

    @Override
    public String toString() {
        return Objects.toString(val());
    }

    public boolean equals(Object o) {
        if (o instanceof Obj obj)
            return Objects.equals(val(), obj.val());
        else return false;
    }

}
