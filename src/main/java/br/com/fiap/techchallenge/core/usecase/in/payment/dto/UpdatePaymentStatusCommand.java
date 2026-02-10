package br.com.fiap.techchallenge.core.usecase.in.payment.dto;

import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;

import java.time.Instant;

public record UpdatePaymentStatusCommand(

        String paymentId,
        PaymentStatus newStatus,
        String transactionId,
        String provider,
        Instant occurredAt
) {}
