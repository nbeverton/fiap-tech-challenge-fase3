package br.com.fiap.techchallenge.core.domain.exception.security;

public class UnauthorizedException extends SecurityException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
