package io.github.mxz_schwarz.parser;

import java.util.Map;
import java.util.HashMap;

class Type {
    static final Type STR = new Type();
    static final Type NUM = new Type();
    static final Type BOOL = new Type();
    static final Type NULL = new Type();
    
    private Type() {} 

    static class GenericType extends Type {
        private static final Map<Type, GenericType> CACHE = new HashMap<>();

        final Type type;

        private GenericType (Type t) {
            type = t;
        }

        private static GenericType from(Type t) {
            if (!CACHE.containsKey(t))
                CACHE.put(t, new GenericType(t));
            return CACHE.get(t);
        }

        static <T extends Obj, Generic extends Obj & Iterable<T>> 
            GenericType infer(Generic obj) {
            
            return null;
        }
    }

    
}