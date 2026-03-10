package br.com.fiap.techchallenge.infra.web.dto.external_payment;

public record ExternalPaymentApiStatusResponse(
        String paymentId,
        String status
) {
}
