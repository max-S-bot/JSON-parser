package io.github.mxz_schwarz.parser;

/**
 * @see io.github.mxz_schwarz.parser.JSONParseException
 * @author max-S-bot
 */
public class JSONException extends Exception {

    /**
     * @param ioe A {@code java.io.IOException} which is 
     * the cause of {@code this}.
     */
    JSONException(java.io.IOException ioe, String message) {
        super(message);
        addSuppressed(ioe);
    }

    /**
     * @param message A {@code String} explaining {@code this}
     * {@code JSONException}.
     */
    JSONException(String message) {
        super(message);
    }

    /**
     * @param jpe A {@code JSONParseException} which is 
     * the cause of {@code this}.
     */
    JSONException(JSONParseException jpe) {
        super(jpe);
    }

    /**
     * @param jre A {@code JSONRuntimeException} which is 
     * the cause of {@code this}.
     */
    JSONException(JSONRuntimeException jre) {
        super(jre);
    }

    /**
     * @return A new {@code JSONParseException} 
     * whose cause is {@code this}
     */
    public JSONRuntimeException unchecked() {
        return new JSONRuntimeException(this);
    }
}
