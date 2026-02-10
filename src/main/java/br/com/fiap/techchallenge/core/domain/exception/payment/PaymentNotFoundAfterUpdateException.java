package br.com.fiap.techchallenge.core.domain.exception.payment;

public class PaymentNotFoundAfterUpdateException extends RuntimeException {

    public PaymentNotFoundAfterUpdateException(String paymentId) {

        super("payment not found after update: " + paymentId);
    }
}
