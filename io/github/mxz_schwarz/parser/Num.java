package io.github.mxz_schwarz.parser;

/**
 * A wrapper class for JSON numbers.
 * @author max-S-bot
 */
public class Num<N extends Number> extends Obj {

    private final N val;

    Num(N val) {
        this.val = val;
    }

    @Override
    public Num<N> asNum() {
        return this;
    }

    @Override
    public N toNum() {
        return val;
    }

    @Override 
    Object val() {
        return val;
    }
}
