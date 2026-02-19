package br.com.fiap.techchallenge.core.domain.exception.payment;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class OverpaymentException extends BusinessException {
    public OverpaymentException(String message) {
        super(message);
    }
}
