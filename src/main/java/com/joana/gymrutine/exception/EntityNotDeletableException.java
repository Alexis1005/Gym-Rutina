package com.joana.gymrutine.exception;

public class EntityNotDeletableException extends RuntimeException {
    public EntityNotDeletableException(String message) {
        super(message);
    }

    public EntityNotDeletableException(String message, Throwable cause) {
        super(message, cause);
    }

}
