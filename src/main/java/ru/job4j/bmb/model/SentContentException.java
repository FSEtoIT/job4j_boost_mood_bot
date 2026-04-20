package ru.job4j.bmb.model;

/**
 * Exception thrown when sending content fails.
 */
public class SentContentException extends RuntimeException {

    /**
     * Constructs a new SentContentException.
     *
     * @param message error message
     * @param throwable root cause
     */
    public SentContentException(final String message,
                                final Throwable throwable) {
        super(message, throwable);
    }
}
