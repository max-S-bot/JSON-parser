package io.github.mxz_schwarz.parser;

public class Bool extends Obj {
    static final Bool TRUE = new Bool();
    static final Bool FALSE = new Bool();

    private Bool () {}

    @Override
    public boolean asBool() {
        return this == TRUE;
    }

    @Override
    int numChars() {
        return this == TRUE ? 4 : 5;
    }

    @Override
    Object val() {
        return this == TRUE;
    }
}
