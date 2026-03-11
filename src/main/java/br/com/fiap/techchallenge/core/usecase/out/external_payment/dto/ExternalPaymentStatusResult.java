package br.com.fiap.techchallenge.core.usecase.out.external_payment.dto;

public record ExternalPaymentStatusResult(
        String paymentId,
        String rawStatus
) {
}
