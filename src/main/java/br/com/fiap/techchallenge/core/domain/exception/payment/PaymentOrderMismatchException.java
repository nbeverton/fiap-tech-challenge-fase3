package br.com.fiap.techchallenge.core.domain.exception.payment;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class PaymentOrderMismatchException extends BusinessException {
    public PaymentOrderMismatchException(String message) {
        super(message);
    }
}
