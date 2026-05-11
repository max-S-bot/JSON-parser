package io.github.mxz_schwarz.parser;

/**
 * A wrapper class for the null value in JSON.
 * @author max-S-bot
 */
public final class Null extends Obj {
    
    public static final Null NULL = new Null();

    private Null () {}

    @Override
    public Object toNull() {
        return null;
    }

    @Override
    Object val() {
        return null;
    }
}
