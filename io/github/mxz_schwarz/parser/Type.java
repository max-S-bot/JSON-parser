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
        static final Map<GenericType, GenericType> CACHE = new HashMap<>();
        static final Type MAP = new Type();
        static final Type ARR = new Type();
        
        final Type rawType;
        final Type genType;

        private GenericType(Type r, Type g) {
            rawType = r;
            genType = g;
        }

        private static GenericType from(Type r, Type t) {
            GenericType genType = new GenericType(r, t);
            if (!CACHE.containsKey(genType))
                CACHE.put(genType, genType);
            return CACHE.get(genType);
        }

        static <T extends Obj, Generic extends Obj & Iterable<T>> 
            GenericType infer(Generic obj) {
            // TODO: make this work
            return from(obj.type(), null);
        }

        public boolean equals(Object o) {
            if (o instanceof GenericType gt) 
                return gt.rawType == rawType 
                && gt.genType.equals(genType);  
            return false;          
        }
    }

    
}