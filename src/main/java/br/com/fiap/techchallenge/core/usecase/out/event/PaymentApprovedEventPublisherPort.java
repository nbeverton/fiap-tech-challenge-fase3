package br.com.fiap.techchallenge.core.usecase.out.event;

import br.com.fiap.techchallenge.infra.messaging.kafka.event.PaymentApprovedEvent;

public interface PaymentApprovedEventPublisherPort {

    void publish(PaymentApprovedEvent event);
}
