package io.github.mxz_schwarz.parser;

class Num extends Obj {

    private final Number val;

    Num(Number val) {
        this.val = val;
    }

    @Override
    public Number asNum() {
        return val;
    }

    @Override 
    Object val() {
        return val;
    }
}
