package com.fiap.core.exception;

public class AuthLoginError extends RuntimeException {
    public AuthLoginError(String msg) {
        super(msg);
    }
}
