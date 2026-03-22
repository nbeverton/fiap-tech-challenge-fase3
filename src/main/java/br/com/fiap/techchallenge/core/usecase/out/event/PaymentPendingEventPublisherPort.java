package br.com.fiap.techchallenge.core.usecase.out.event;

import br.com.fiap.techchallenge.infra.messaging.kafka.event.PaymentPendingEvent;

public interface PaymentPendingEventPublisherPort {

    void publish(PaymentPendingEvent event);
}
