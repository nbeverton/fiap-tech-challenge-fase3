package br.com.fiap.techchallenge.core.domain.exception.order;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class InvalidOrderStatusException extends BusinessException {

    public InvalidOrderStatusException(String message) {
        super(message);
    }
}
