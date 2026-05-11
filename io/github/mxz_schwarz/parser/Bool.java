package io.github.mxz_schwarz.parser;

/**
 * Wrapper class for JSON booleans.
 * @author max-S-bot
 */
public final class Bool extends Obj {
    public static final Bool TRUE = new Bool();
    public static final Bool FALSE = new Bool();

    private Bool () {}

    @Override
    public boolean toBool() {
        return this == TRUE;
    }

    @Override 
    public Boolean toStdLibObj() {
        return this == TRUE;
    }

    @Override
    Object val() {
        return this == TRUE;
    }
}
