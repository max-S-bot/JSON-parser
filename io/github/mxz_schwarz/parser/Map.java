package io.github.mxz_schwarz.parser;

/**
 * A wrapper class for JSON objects.
 * @author max-S-bot
 */
public class Map<T extends Obj> extends Obj implements Iterable<T> {

    private final java.util.Map<String, T> val;

    Map(java.util.Map<String, T> val) {
        this.val = java.util.Map.copyOf(val);
    }

    @Override 
    @SuppressWarnings("unchecked")
    public java.util.Map<String, T> toMap() {
        return val;
    }

    @Override 
    @SuppressWarnings("unchecked")
    public Map<T> asMap() {
        return this;
    }

    @Override 
    Object val() {
        return val;
    }

    @Override 
    Type type() {
        return null;
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return val.values().iterator();
    }
}
