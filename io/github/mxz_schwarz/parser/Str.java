package io.github.mxz_schwarz.parser;

public class Str extends Obj {

    private final String val;
    
    Str(String val) {
        this.val = val;
    }

    @Override
    public String asStr() {
        return val;
    }

    @Override 
    Object val() {
        return val;
    }
}
