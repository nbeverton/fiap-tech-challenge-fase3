package br.com.fiap.techchallenge.core.domain.exception.payment;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class InvalidPaymentStatusException extends BusinessException {
    public InvalidPaymentStatusException(String message) {
        super(message);
    }
}
