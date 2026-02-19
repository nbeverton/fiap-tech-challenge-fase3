package br.com.fiap.techchallenge.infra.web.dto.payment;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(
        String id,
        String orderId,
        Instant createdAt,
        BigDecimal amount,
        String method,
        String status,
        String transactionId,
        String provider,
        Instant paidAt,
        Instant failedAt,
        Instant refundedAt
) {}

