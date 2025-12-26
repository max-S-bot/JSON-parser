package io.github.mxz_schwarz.parser;

class Map extends Obj {

    private final java.util.Map<String, Obj> val;

    Map(java.util.Map<String, Obj> val) {
        this.val = java.util.Map.copyOf(val);
    }

    @Override 
    public java.util.Map<String, Obj> asMap() {
        return val;
    }

    @Override 
    Object val() {
        return val;
    }
}
