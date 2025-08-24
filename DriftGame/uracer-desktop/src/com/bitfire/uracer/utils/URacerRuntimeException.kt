package com.bitfire.uracer.utils;

/**
 * Typed runtime exception used throughout uracer
 */
public class URacerRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 110779130456L;

    public URacerRuntimeException(String message) {
        super(message);
    }
}
