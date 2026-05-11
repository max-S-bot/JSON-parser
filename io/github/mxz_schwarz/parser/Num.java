package io.github.mxz_schwarz.parser;

/**
 * A wrapper class for JSON numbers.
 * @author max-S-bot
 */
public final class Num<N extends Number> extends Obj {

    private final N val;

    public Num(N val) {
        this.val = val;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Num<N> asNum() {
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public N toNum() {
        return val;
    }

    @Override
    public N toStdLibObj() {
        return val;
    }
    
    @Override 
    Object val() {
        return val;
    }
}
