package br.com.fiap.techchallenge.infra.web.dto.external_payment;

public record ExternalPaymentApiStatusResponse(
        String pagamento_id,
        String status
) {
}
