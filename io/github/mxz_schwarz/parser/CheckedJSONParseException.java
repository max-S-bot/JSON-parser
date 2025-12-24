package io.github.mxz_schwarz.parser;

/**
 * @see io.github.mxz_schwarz.parser.JSONParseException
 * @author max-S-bot
 */
public class CheckedJSONParseException extends Exception {

    /**
     * @param ioe A {@code java.io.IOException} which is 
     * the cause of {@code this}.
     */
    CheckedJSONParseException(java.io.IOException ioe) {
        super(ioe);
    }

    /**
     * @param message A {@code String} explaining {@code this}
     * {@code CheckedJSONParseException}.
     */
    CheckedJSONParseException(String message) {
        super(message);
    }

    /**
     * @param jpe A {@code JSONParseException} which is 
     * the cause of {@code this}.
     */
    CheckedJSONParseException(JSONParseException jpe) {
        super(jpe);
    }

    /**
     * @return A new {@code JSONParseException} 
     * whose cause is {@code this}
     */
    public JSONParseException unchecked() {
        return new JSONParseException(this);
    }
}
