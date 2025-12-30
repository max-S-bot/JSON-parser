package io.github.mxz_schwarz.parser;

import java.util.Objects;
import java.util.List;

/**
 * A wrapper {@code abstract class} that acts as the superclass 
 * for all JSON data types.
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
    public <N extends Number> N toNum() throws JSONException {
        throw new JSONException("not a Number");
    }
    
    public <N extends Number> Num<N> asNum() throws JSONException {
        throw new JSONException("not a Num");
    }

    /**
     * Returns a {@link String} when {@code this}
     * represents a string.
     * @return The value wrapped in {@code this}.
     * @throws JSONException When {@code this}
     * isn't a {@link Str}.
     */
    public String toStr() throws JSONException {
        throw new JSONException("not a String");
    }

    /**
     * Returns a {@code boolean} when {@code this}
     * represents a boolean.
     * @return The value wrapped in {@code this}.
     * @throws JSONException When {@code this}
     * isn't a {@link Bool}.
     */
    public boolean toBool() throws JSONException {
        throw new JSONException("not a boolean");
    }

    /**
     * Returns an {@link Object} when {@code this}
     * represents null.
     * @return The value wrapped in {@code this}.
     * @throws JSONException When {@code this}
     * isn't {@link Null}.
     */
    public Object toNull() throws JSONException {
       throw new JSONException("not null");
    }

    /**
     * Returns a {@link List<Obj>} when {@code this}
     * represents an array.
     * @return The value wrapped in {@code this}.
     * @throws JSONException When {@code this}
     * isn't a {@link Arr}.
     */
    public <T extends Obj> List<T> toList() throws JSONException {
        throw new JSONException("not an array");
    }

    /**
     * Returns {@code this} if it's an {@link Arr}.
     * @return {@code this}.
     * @throws JSONException When {@code this} isn't 
     * an {@link Arr}.
     */
    public <T extends Obj> Arr<T> asArr() throws JSONException {
        throw new JSONException("not an Arr");
    }

    /**
     * Returns a {@link java.util.Map<String, Obj>} 
     * when {@code this} represents a boolean.
     * @return The value wrapped in {@code this}.
     * @throws JSONException When {@code this}
     * isn't a {@link Map}.
     */
    public <T extends Obj> java.util.Map<String, T> toMap() throws JSONException {
        throw new JSONException("not a Map");
    }

    /**
     * Return {@code this} if it's a {@link Map}.
     * @return {@code this}.
     * @throws JSONException When {@code this}
     * is not a {@link Map}.
     */
    public <T extends Obj> Map<T> asMap() throws JSONException {
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

    @Override 
    public int hashCode() {
        return Objects.hashCode(val());
    }

}
