package io.github.mxz_schwarz.parser;

/**
 * A wrapper class for JSON numbers.
 * @author max-S-bot
 */
public class Num extends Obj {

    static final Num ZERO = new Num(0L);

    private final Number val;

    Num(Number val) {
        this.val = val;
    }

    @Override
    public Num toNum() {
        return this;
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
