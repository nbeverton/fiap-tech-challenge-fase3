package br.com.fiap.techchallenge.infra.web.dto.external_payment;

import java.math.BigDecimal;

public record ExternalPaymentApiRequest(
        BigDecimal amount,
        String paymentId,
        String clientId
) {
}
