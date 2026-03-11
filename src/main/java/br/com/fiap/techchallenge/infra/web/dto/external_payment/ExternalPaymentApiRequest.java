package br.com.fiap.techchallenge.infra.web.dto.external_payment;

import java.math.BigDecimal;

public record ExternalPaymentApiRequest(
        Integer valor,
        String pagamento_id,
        String cliente_id
) {
}
