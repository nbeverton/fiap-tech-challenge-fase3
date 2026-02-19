package br.com.fiap.techchallenge.infra.web.dto.payment;

import java.math.BigDecimal;

public record CreatePaymentRequest(

        String orderId,
        BigDecimal amount,
        String method
) {
}
