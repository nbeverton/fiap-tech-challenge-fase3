package br.com.fiap.techchallenge.core.domain.exception.order;

import br.com.fiap.techchallenge.core.domain.security.ForbiddenException;

public class OrderUserMismatchException extends ForbiddenException {

    public OrderUserMismatchException(String message) {
        super(message);
    }
}
