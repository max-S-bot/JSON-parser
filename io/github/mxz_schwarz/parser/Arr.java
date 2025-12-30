package io.github.mxz_schwarz.parser;

import java.util.List;

/**
 * Wrapper class for JSON arrays.
 * @author max-S-bot
 */
public class Arr<T extends Obj> extends Obj implements Iterable<T> {

    private final List<T> val;

    Arr(List<T> val) {
        this.val = List.copyOf(val);
    }

    @Override 
    @SuppressWarnings("unchecked")
    public Arr<T> asArr() throws JSONException {
        return this;
    }

    @Override 
    @SuppressWarnings("unchecked")
    public List<T> toList() {
        return val;
    }

    @Override 
    Object val() {
        return val;
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return val.iterator();
    }
}
