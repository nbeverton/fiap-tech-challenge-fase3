package br.com.fiap.techchallenge.core.domain.enums;

import br.com.fiap.techchallenge.core.domain.exception.payment.InvalidPaymentException;

import java.util.Arrays;

public enum PaymentMethod {

    CREDIT_CARD,
    DEBIT_CARD,
    PIX,
    CASH;


    public static PaymentMethod fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidPaymentException("paymentMethod must not be null or blank. Allowed: " + Arrays.toString(PaymentMethod.values())
            );
        }
        try {
            return PaymentMethod.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidPaymentException("paymentMethod must not be null or blank. Allowed: " + Arrays.toString(PaymentMethod.values())
            );
        }
    }
}
