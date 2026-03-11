package br.com.fiap.techchallenge.core.usecase.out.external_payment.dto;

import java.math.BigDecimal;

public record ExternalPaymentRequest(
        BigDecimal amount,
        String paymentId,
        String clientId
) {
}
