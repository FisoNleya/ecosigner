package com.sixthradix.econetsigner.exceptions;

public class ApplicationRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ApplicationRequestException(String message) {
        super(message);
    }
}
