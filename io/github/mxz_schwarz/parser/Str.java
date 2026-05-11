package io.github.mxz_schwarz.parser;

/**
 * A wrapper class for JSON strings.
 * @author max-S-bot
 */
public final class Str extends Obj {

    private final String val;
    
    public Str(String val) {
        this.val = val;
    }

    @Override
    public String toStdLibObj() {
        return val;
    }

    @Override
    public String toStr() {
        return val;
    }
    
    @Override 
    Object val() {
        return val;
    }
}
