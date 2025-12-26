package io.github.mxz_schwarz.parser;

/**
 * A checked {@link Exception} that can be conveniently chained into an
 * unchecked{@link JSONRuntimeException} via {@link JSONException#unchecked()}
 * and back into a checked {@link JSONException} with 
 * {@link JSONRuntimeException#checked()}. Instances of {@link JSONException} 
 * should be thrown by {@code public} or {@code protected} executables. Instances
 *  of {@link JSONRuntimeException} should be thrown by executables with
 * visibility of {@code package private} or lower and then caught in {@code public}
 * or {@code protected} executables in order to chain them in a {@link JSONException}.
 * @author max-S-bot
 */
public class JSONException extends Exception {

    /**
     * @param ioe A {@link java.io.IOException} which is 
     * the cause of {@code this}.
     */
    JSONException(java.io.IOException ioe, String message) {
        super(message, ioe);
    }

    /**
     * @param message A {@link String} explaining {@code this}
     * {@link JSONException}.
     */
    JSONException(String message) {
        super(message);
    }

    /**
     * @param jpe A {@link JSONParseException} which is 
     * the cause of {@code this}.
     */
    JSONException(JSONParseException jpe) {
        super(jpe);
    }

    /**
     * @param jre A {@link JSONRuntimeException} which is 
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
