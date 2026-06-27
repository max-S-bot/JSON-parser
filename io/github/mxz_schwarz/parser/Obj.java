package io.github.mxz_schwarz.parser;

import java.util.List;
import java.util.Objects;

/**
 * A wrapper {@code abstract class} that acts as the 
 * superclass for all JSON data types.
 * This consists mainly of convenience methods to allow  
 * a client to perform less manual casting. 
 * @author max-S-bot
 */
public sealed abstract class Obj permits Map, Arr, Str, Num, Bool, Null {

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
    
    /**
     * Pretty much casts {@code this} to a {@link Num<N>}
     * @param <N> The generic Type of this instance.
     * @return A {@link Num<N>} when {@code this} is a {@link Num<N>}.
     * @throws JSONException When this is not a {@link Num<N>}
     */
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
     * Returns {@code null} when {@code this}
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
     * Convenience method to cast each element in an 
     * array to a particular type.
     * @param <T> The desired generic type of the {@link List} 
     * being returned.
     * @param clazz The {@link Class} object representing 
     * the desired type.
     * @return A list with generic type {@code T}.
     * @throws JSONException If this {@link Obj} is not an 
     * {@link Arr} or when not every element is a {@code T}.
     */
    public <E> List<E> toList(Class<E> clazz) throws JSONException {
        throw new JSONException("not an array");
    }

    public <E extends Obj> Arr<E> castElems(Class<E> clazz) throws JSONException {
        throw new JSONException("not an array");
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
     * 
     * @param <T> The desired generic type of values
     * in the Map
     * @param clazz The {@link Class} object representing 
     * the desired type.
     * @return A {@link Map} with gener
     * @throws JSONException When {@code this} isn't a 
     * {@link Map} or when not every element is a {@code T}.
     */
    public <E> java.util.Map<String, E> toMap(Class<E> clazz) throws JSONException {
        throw new JSONException("not a Map");
    } 

    public <E extends Obj> Map<E> castVals(Class<E> clazz) throws JSONException {
        throw new JSONException("not a Map");
    }

    public Object toStdLibObj() {
        return val();
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
        return val().hashCode();
    }

}
