package io.github.mxz_schwarz.parser;

class Num extends Obj {

    private final Number val;
    private final int numChars;

    Num(Number val, int numChars) {
        this.val = val;
        this.numChars = numChars;
    }

    @Override
    public Number asNum() {
        return val;
    }

    @Override
    int numChars() {
        return numChars;
    }

    @Override 
    Object val() {
        return val;
    }
}
