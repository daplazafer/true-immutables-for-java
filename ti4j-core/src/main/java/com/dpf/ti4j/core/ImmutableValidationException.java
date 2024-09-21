package com.dpf.ti4j.core;

import java.io.Serial;

public class ImmutableValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -9191937932935338173L;

    public ImmutableValidationException(String message, Exception exception) {
        super(message, exception);
    }

    public ImmutableValidationException(String message) {
        super(message);
    }
}