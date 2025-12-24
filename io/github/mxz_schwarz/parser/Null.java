package io.github.mxz_schwarz.parser;

class Null extends Obj {
    
    static final Null NULL = new Null();

    private Null () {}

    @Override
    public Object asNull() {
        return null;
    }

    @Override
    int numChars() {
        return 4;
    }

    @Override
    Object val() {
        return null;
    }
}
