package io.github.mxz_schwarz.parser;

class Str extends Obj{

    private final String val;
    private final int numChars;
    
    Str(String val, int numChars) {
        this.val = val;
        this.numChars = numChars;
    }

    @Override
    public String asStr() {
        return val;
    }

    @Override
    int numChars() {
        return numChars;
    }

    @Override 
    Object val() {
        return val;
    }
}
