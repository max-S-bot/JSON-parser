package io.github.mxz_schwarz.parser;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * A wrapper class for JSON objects.
 * @author max-S-bot
 */
public class Map<T extends Obj> extends Obj {

    private final java.util.Map<String, T> val;

    Map(java.util.Map<String, T> val) {
        this.val = java.util.Map.copyOf(val);
    }

    @Override
    public <E> java.util.Map<String, E> toMap(Class<E> clazz) throws JSONException {
        HashMap<String, E> map = new HashMap<>();
        for (Entry<String, T> e : val.entrySet()) 
            try {
                map.put(e.getKey(), clazz.cast(e.getValue().val())); 
            } catch (ClassCastException cce) {
                throw new JSONException(cce);
            }
        return java.util.Map.copyOf(map);
    }

    @Override 
    @SuppressWarnings("unchecked")
    public java.util.Map<String, T> toMap() {
        return val;
    }

    @Override
    public <E extends Obj> Map<E> castVals(Class<E> clazz) {
        HashMap<String, E> map = new HashMap<>();
        for (Entry<String, T> e : val.entrySet()) {
            map.put(e.getKey(), clazz.cast(e.getValue()));
        }
        return new Map<>(map);
    }
    
    @Override 
    Object val() {
        return val;
    }
}
