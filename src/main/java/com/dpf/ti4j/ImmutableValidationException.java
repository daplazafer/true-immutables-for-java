package com.dpf.ti4j;

import java.io.Serial;

class ImmutableValidationException extends Exception {

    @Serial
    private static final long serialVersionUID = -9191937932935338173L;

    public ImmutableValidationException(String message, Exception exception) {
        super(message, exception);
    }

    public ImmutableValidationException(String message) {
        super(message);
    }
}