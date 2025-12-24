package io.github.mxz_schwarz.parser;

import java.util.List;

class Arr extends Obj {

    private final List<Obj> val;
    private final int numChars;

    Arr(List<Obj> val, int numChars) {
        this.val = val;
        this.numChars = numChars;
    }

    @Override
    public Obj[] asArr() {
        return val.toArray(Obj[]::new);
    }

    @Override
    public List<Obj> asList() {
        return val;
    }

    int numChars() {
        return numChars;
    }

    @Override 
    Object val() {
        return val;
    }
}
