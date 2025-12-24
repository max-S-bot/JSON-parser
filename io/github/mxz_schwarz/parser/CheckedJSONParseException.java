package io.github.mxz_schwarz.parser;

/**
 * @see io.github.mxz_schwarz.parser.JSONParseException
 */
public class CheckedJSONParseException extends Exception {

    CheckedJSONParseException(java.io.IOException ioe) {
        super(ioe);
    }

    CheckedJSONParseException(ClassCastException cce) {
        super(cce);
    }

    CheckedJSONParseException(String message) {
        super(message);
    }

    CheckedJSONParseException(JSONParseException jpe) {
        super(jpe);
    }

    public JSONParseException unchecked() {
        return new JSONParseException(this);
    }
}
