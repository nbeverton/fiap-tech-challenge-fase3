package br.com.fiap.techchallenge.core.domain.exception.order;

import br.com.fiap.techchallenge.core.domain.security.ForbiddenException;

public class OrderAccessDeniedException extends ForbiddenException {

    public OrderAccessDeniedException() {
        super("Access denied: you cannot access this order");
    }
}
