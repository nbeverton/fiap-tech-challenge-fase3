package br.com.fiap.techchallenge.core.usecase.in.payment.dto;

import br.com.fiap.techchallenge.core.domain.enums.PaymentMethod;
import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentView(

        String id,
        String orderId,
        Instant createdAt,
        BigDecimal amount,
        PaymentMethod method,
        PaymentStatus status,
        String transactionId,
        String provider,
        Instant paidAt,
        Instant failedAt,
        Instant refundedAt
) {}
