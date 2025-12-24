package io.github.mxz_schwarz.parser;

class Map extends Obj {

    private final java.util.Map<String, Obj> val;
    private final int numChars;

    Map(java.util.Map<String, Obj> val, int numChars) {
        this.val = val;
        this.numChars = numChars;
    }

    @Override 
    public java.util.Map<String, Obj> asMap() {
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
