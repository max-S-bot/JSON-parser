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

    /**
     * Returns a {@link Number} when {@code this}
     * represents a number.
     * @return The value wrapped in {@code this}.
     * @throws JSONException When {@code this}
     * isn't a {@link Num}.
     */
    public Number asNum() throws JSONException {
        throw new JSONException("not a Number");
    }

    /**
     * Returns a {@link String} when {@code this}
     * represents a string.
     * @return The value wrapped in {@code this}.
     * @throws JSONException When {@code this}
     * isn't a {@link Str}.
     */
    public String asStr() throws JSONException {
        throw new JSONException("not a String");
    }

    /**
     * Returns a {@code boolean} when {@code this}
     * represents a boolean.
     * @return The value wrapped in {@code this}.
     * @throws JSONException When {@code this}
     * isn't a {@link Bool}.
     */
    public boolean asBool() throws JSONException {
        throw new JSONException("not a boolean");
    }

    /**
     * Returns an {@link Object} when {@code this}
     * represents null.
     * @return The value wrapped in {@code this}.
     * @throws JSONException When {@code this}
     * isn't {@link Null}.
     */
    public Object asNull() throws JSONException {
       throw new JSONException("not null");
    }

    /**
     * Returns a {@code Obj[]} when {@code this}
     * represents a an array.
     * @return The value wrapped in {@code this}.
     * @throws JSONException When {@code this}
     * isn't a {@link Arr}.
     */
	public Obj[] asArr() throws JSONException {
        throw new JSONException("not an array");
    }

    /**
     * Returns a {@link List<Obj>} when {@code this}
     * represents an array.
     * @return The value wrapped in {@code this}.
     * @throws JSONException When {@code this}
     * isn't a {@link Arr}.
     */
    public List<Obj> asList() throws JSONException {
        throw new JSONException("not an array");
    }

    /**
     * Returns a {@link Map<String, Obj>} when {@code this}
     * represents a boolean.
     * @return The value wrapped in {@code this}.
     * @throws JSONException When {@code this}
     * isn't a {@link Map}.
     */
    public Map<String, Obj> asMap() throws JSONException {
        throw new JSONException("not a Map");
    }

    /** 
     * @return The value wrapped in {@code this}.
     */
    abstract Object val();

    @Override
    public String toString() {
        return Objects.toString(val());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Obj obj)
            return Objects.equals(val(), obj.val());
        else return false;
    }

}
