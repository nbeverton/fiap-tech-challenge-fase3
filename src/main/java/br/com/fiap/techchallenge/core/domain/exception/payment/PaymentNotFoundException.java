package br.com.fiap.techchallenge.core.domain.exception.payment;

import br.com.fiap.techchallenge.core.domain.exception.NotFoundException;

public class PaymentNotFoundException extends NotFoundException {

    public PaymentNotFoundException(String paymentId) {
        super("Payment not found: " + paymentId);
    }
}
