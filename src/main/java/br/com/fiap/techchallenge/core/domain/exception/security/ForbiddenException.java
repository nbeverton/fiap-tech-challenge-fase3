package br.com.fiap.techchallenge.core.domain.exception.security;

public class ForbiddenException extends SecurityException {

    public ForbiddenException(String message) {
        super(message);
    }
}
