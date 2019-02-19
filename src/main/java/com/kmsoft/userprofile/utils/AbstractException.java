package com.kmsoft.userprofile.utils;


public abstract class AbstractException extends Exception {

    private static final long serialVersionUID = -5120202516393737563L;

    /**
     * Constructor
     */
    public AbstractException() {
        super();
    }

    /**
     * Constructor with message
     * 
     * @param message
     *            message
     */
    public AbstractException(final String message) {
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
    public AbstractException(final String message, final Throwable e) {
        super(message, e);
    }
}