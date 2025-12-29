package io.github.mxz_schwarz.parser;

import java.util.List;

/**
 * Wrapper class for JSON arrays.
 * @author max-S-bot
 */
public class Arr<T extends Obj> extends Obj {

    private final List<T> val;

    Arr(List<T> val) {
        this.val = List.copyOf(val);
    }

    @Override 
    public Arr<T> asArr() throws JSONException {
        return this;
    }

    @Override
    public List<T> toList() {
        return val;
    }

    @Override 
    Object val() {
        return val;
    }
}
