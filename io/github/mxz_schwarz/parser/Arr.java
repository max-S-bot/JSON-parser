package io.github.mxz_schwarz.parser;

import java.util.List;

/**
 * Wrapper class for JSON arrays.
 * @author max-S-bot
 */
public class Arr<T extends Obj> extends Obj {

    private final List<Obj> val;

    Arr(List<Obj> val) {
        this.val = List.copyOf(val);
    }


    @Override 
    public Arr<T> toArr() throws JSONException {
        return this;
    }

    @Override
    public List<Obj> asList() {
        return val;
    }

    @Override 
    Object val() {
        return val;
    }
}
