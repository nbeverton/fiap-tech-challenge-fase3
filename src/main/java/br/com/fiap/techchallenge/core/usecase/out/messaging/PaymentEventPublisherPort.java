package br.com.fiap.techchallenge.core.usecase.out.messaging;

import br.com.fiap.techchallenge.core.usecase.out.messaging.dto.PaymentStatusCheckMessage;

public interface PaymentEventPublisherPort {

    void publishPaymentStatusCheck(PaymentStatusCheckMessage message, long delayMs);

    void publishPaymentExpired(PaymentStatusCheckMessage message);
}
