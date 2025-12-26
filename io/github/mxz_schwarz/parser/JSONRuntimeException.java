package io.github.mxz_schwarz.parser;

/**
 * @see JSONException
 * @author max-S-bot
 */
public class JSONRuntimeException extends RuntimeException {

    /**
     * @param je A {@link JSONException} that is the
     * cause of {@code this}.
     */
    JSONRuntimeException(JSONException je) {
        super(je);
    }

    /**
     * @return A new {@link JSONException} 
     * whose cause is {@code this}
     */
    public JSONException checked() {
        return new JSONException(this);
    }
}
