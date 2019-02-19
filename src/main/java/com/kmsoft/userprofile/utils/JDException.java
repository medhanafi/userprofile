package com.kmsoft.userprofile.utils;

public class JDException extends AbstractException {

    private static final long serialVersionUID = -5120202516398837563L;

    /**
     * Constructor
     */
    public JDException() {
        super();
    }

    /**
     * Constructor with message
     * 
     * @param message
     *            message
     */
    public JDException(final String message) {
        super(message);
    }

    /**
     * Constructor with message and exception
     * 
     * @param message
     *            message
     * @param e
     *            exception
     */
    public JDException(final String message, final Throwable e) {
        super(message, e);
    }
}