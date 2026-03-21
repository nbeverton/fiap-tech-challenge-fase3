package br.com.fiap.techchallenge.infra.messaging.kafka.event;

import java.time.Instant;

public record OrderCreatedEvent(

        String eventId,
        String eventType,
        Instant occurredAt,
        String orderId
) {
}
