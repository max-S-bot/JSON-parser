package io.github.mxz_schwarz.parser;

class JSONParseException extends RuntimeException {

    /**
     * @param message A {@code String} explaining {@code this}
     * {@code JSONParseException}.
     */
    JSONParseException(String message) {
        super(message);
    }
}
