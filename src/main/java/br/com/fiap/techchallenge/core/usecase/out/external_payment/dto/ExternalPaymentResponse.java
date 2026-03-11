package br.com.fiap.techchallenge.core.usecase.out.external_payment.dto;

public record ExternalPaymentResponse(
        boolean accepted,
        String rawStatus
) {
}
