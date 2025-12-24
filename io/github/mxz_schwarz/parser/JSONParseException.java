package io.github.mxz_schwarz.parser;

/**
 * This is meant to be thrown by methods with a visibility 
 * of {@code package private} or lower. When combined with 
 * {@code CheckedJSONParseException}, this is meant to be a 
 * 'publicly checked' {@code Exception}. {@code CheckedJSONParseException}
 * should be thrown by {@code public} or {@code protected} methods. 
 * All public methods in this package ({@code io.github.mxz_schwarz.parser}) 
 * {@code throw} a {@code CheckedJSONParseException}
 * which can be chained in a {@code JSONParseException} by calling 
 * {@code CheckedJSONParseException.unchecked()}, and then unchained with
 * {@code Throwable.getCause()} by a top level method. This provides greater
 * control, type safety and clarity than simply chaining with a {@code RuntimeException}
 * so that intermediate methods don't have to declare that they throw an exception that 
 * they don't intend to handle.
 * @author max-S-bot
 */
public class JSONParseException extends RuntimeException {

    /**
     * @param message A {@code String} explaining {@code this}
     * {@code JSONParseException}.
     */
    JSONParseException(String message) {
        super(message);
    }

    /**
     * @param cjpe A {@code CheckedJSONParseException}
     * which is the cause of {@code this}.
     */
    JSONParseException(CheckedJSONParseException cjpe) {
        super(cjpe);
    }
}
