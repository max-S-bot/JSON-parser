package io.github.mxz_schwarz.parser;

/**
 * This {@link Exception} subclass indicates that 
 * the presented text is not valid JSON data.
 * @author max-S-bot
 */
class JSONParseException extends RuntimeException {

    /**
     * @param message A {@link String} explaining {@code this}
     * {@link JSONParseException}.
     */
    JSONParseException(String message) {
        super(message);
    }

    /**
     * @param nfe The cause of {@code this}.
     * @param message A {@link String} explaining {@code nfe}.
     */
    JSONParseException(NumberFormatException nfe, String message) {
        super(message, nfe);
    }
}
