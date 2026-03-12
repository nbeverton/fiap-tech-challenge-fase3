package br.com.fiap.techchallenge.core.usecase.out.messaging.dto;

import java.time.Instant;

public record PaymentStatusCheckMessage(
        String paymentId,
        String orderId,
        int attemptNumber,
        Instant createdAt
) {
}
