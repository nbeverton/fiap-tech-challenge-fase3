package br.com.fiap.techchallenge.infra.messaging;

import br.com.fiap.techchallenge.core.usecase.in.payment.ExpirePaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.out.messaging.dto.PaymentStatusCheckMessage;
import br.com.fiap.techchallenge.infra.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentExpiredConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentExpiredConsumer.class);

    private final ExpirePaymentUseCase expirePaymentUseCase;

    public PaymentExpiredConsumer(ExpirePaymentUseCase expirePaymentUseCase) {
        this.expirePaymentUseCase = expirePaymentUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_EXPIRED_QUEUE)
    public void onMessage(PaymentStatusCheckMessage message) {
        log.info("Received expired payment: paymentId={}, attempt={}",
                message.paymentId(), message.attemptNumber());

        expirePaymentUseCase.execute(message);
    }
}
