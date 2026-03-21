package br.com.fiap.techchallenge.core.usecase.out.event;

import br.com.fiap.techchallenge.infra.messaging.kafka.event.OrderCreatedEvent;

public interface OrderCreatedEventPublisherPort {

    void publish(OrderCreatedEvent event);
}
