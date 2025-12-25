package io.github.mxz_schwarz.parser;

public class JSONRuntimeException extends RuntimeException {

    JSONRuntimeException(JSONException je) {
        super(je);
    }

    public JSONException checked() {
        return new JSONException(this);
    }
}
