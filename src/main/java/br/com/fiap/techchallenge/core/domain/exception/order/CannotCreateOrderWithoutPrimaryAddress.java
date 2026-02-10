package br.com.fiap.techchallenge.core.domain.exception.order;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class CannotCreateOrderWithoutPrimaryAddress extends BusinessException {

    public CannotCreateOrderWithoutPrimaryAddress() {

        super("It is not allowed to create the order with secondary address");
    }
}
