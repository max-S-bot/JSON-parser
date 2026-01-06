package io.github.mxz_schwarz.parser;

/**
 * A wrapper class for the null value in JSON.
 * @author max-S-bot
 */
public class Null extends Obj {
    
    static final Null NULL = new Null();

    private Null () {}

    @Override
    public Object toNull() {
        return null;
    }

    @Override
    Type type() {
        return Type.NULL;
    }

    @Override
    Object val() {
        return null;
    }
}
