package br.com.fiap.techchallenge.core.domain.exception.order;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class InvalidOrderItemQuantityException extends BusinessException {

    public InvalidOrderItemQuantityException(String menuId) {
        super("Quantity must be greater than 0 for menuId = " + menuId);
    }
}
