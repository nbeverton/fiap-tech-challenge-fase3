package br.com.fiap.techchallenge.core.domain.exception.payment;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class InvalidPaymentException extends BusinessException {
    public InvalidPaymentException(String message) {
        super(message);
    }
}
