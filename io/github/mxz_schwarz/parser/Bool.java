package io.github.mxz_schwarz.parser;

public class Bool extends Obj {
    static final Bool TRUE = new Bool(true);
    static final Bool FALSE = new Bool(false);

    private final boolean val;

    private Bool (boolean val) {
        this.val = val;
    }

    @Override
    public boolean asBool() {
        return val;
    }

    @Override
    int numChars() {
        return val ? 4 : 5;
    }

    @Override
    Object val() {
        return val;
    }
}
