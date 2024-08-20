package dev.mccue.flake;

/**
 * Thrown to indicate that the application has attempted to convert
 * a string to a {@link Flake}, but that the string does not
 * have the appropriate format.
 */
public final class FlakeFormatException extends IllegalArgumentException {
    @java.io.Serial
    private static final long serialVersionUID = 72;

    /**
     * Constructs a {@code FlakeFormatException} with no detail message.
     */
    public FlakeFormatException () {
        super();
    }

    /**
     * Constructs a {@code FlakeFormatException} with the
     * specified detail message.
     *
     * @param   s   the detail message.
     */
    public FlakeFormatException (String s) {
        super (s);
    }

    /**
     * Factory method for making a {@code FlakeFormatException}
     * given the specified input which caused the error.
     *
     * @param   s   the input causing the error
     */
    static FlakeFormatException forInputString(String s) {
        return new FlakeFormatException("For input string: \"" + s + "\"");
    }
}

