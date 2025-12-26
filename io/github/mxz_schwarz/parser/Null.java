package io.github.mxz_schwarz.parser;

public class Null extends Obj {
    
    static final Null NULL = new Null();

    private Null () {}

    @Override
    public Object asNull() {
        return null;
    }

    @Override
    Object val() {
        return null;
    }
}
