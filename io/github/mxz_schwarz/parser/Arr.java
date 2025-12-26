package io.github.mxz_schwarz.parser;

import java.util.List;

class Arr extends Obj {

    private final List<Obj> val;

    Arr(List<Obj> val) {
        this.val = List.copyOf(val);
    }

    @Override
    public Obj[] asArr() {
        return val.toArray(Obj[]::new);
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
