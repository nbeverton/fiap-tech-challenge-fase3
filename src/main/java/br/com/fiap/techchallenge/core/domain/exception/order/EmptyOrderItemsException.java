package br.com.fiap.techchallenge.core.domain.exception.order;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class EmptyOrderItemsException extends BusinessException {

    public EmptyOrderItemsException() {

        super("Order must contain at least 1 item.");
    }
}
