package io.github.mxz_schwarz.parser;

import java.util.function.BiFunction;


public enum Type {
    STR(JSON::parseStr),
    BOOL(JSON::parseBool), 
    NUM(JSON::parseNum),
    ARR(JSON::parseArr),
    OBJ(JSON::parseObj),
    NULL(JSON::parseNull);

    final BiFunction<String, Integer, Obj> parser; 

    private Type(BiFunction<String, Integer, Obj> parser) {
        this.parser = parser;
    }

    static Type from(char ch) {
        return switch (ch) {
            case 't', 'f' -> Type.BOOL;
            case 'n' -> Type.NULL;
            case '-','0','1','2','3','4','5','6','7','8','9'
                -> Type.NUM;
            case '"' -> Type.STR;
            case '[' -> Type.ARR;
            case '{' -> Type.OBJ;
            default -> throw new JSONParseException("Invalid value"),
        };
    }
}