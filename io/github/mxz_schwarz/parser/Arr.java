package io.github.mxz_schwarz.parser;

import java.util.List;
import java.util.ArrayList;

/**
 * Wrapper class for JSON arrays.
 * @author max-S-bot
 */
public class Arr<T extends Obj> extends Obj {

    private final List<T> val;

    Arr(List<T> val) {
        this.val = List.copyOf(val);
    }

    @Override
    public <E> List<E> toList(Class<E> clazz) throws JSONException {
        List<E> list = new ArrayList<E>(val.size());
        for (Obj e : val) 
            try {
                list.add(clazz.cast(e.val()));
            } catch (ClassCastException cce) {
                throw new JSONException(cce);
            }
        return List.copyOf(list);
    }

    @Override
    public <E extends Obj> Arr<E> castElems(Class<E> clazz) throws JSONException {
        List<E> list = new ArrayList<>(val.size());
        for (T t : val)
            try {
                list.add(clazz.cast(t));
            } catch (ClassCastException cce) {
                throw new JSONException(cce);
            }
        return new Arr<>(list);
    }

    @Override
    @SuppressWarnings("unchecked") 
    public List<T> toList() {
        return val;
    }

    @Override 
    Object val() {
        return val;
    }
}
