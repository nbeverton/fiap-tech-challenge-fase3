package br.com.fiap.techchallenge.infra.messaging.kafka.event;

import java.time.Instant;

public record PaymentApprovedEvent(

        String eventId,
        String eventType,
        Instant occurredAt,
        String paymentId,
        String orderId
) {
}
