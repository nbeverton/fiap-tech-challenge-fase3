package br.com.fiap.techchallenge.infra.messaging;

import br.com.fiap.techchallenge.core.usecase.out.messaging.PaymentEventPublisherPort;
import br.com.fiap.techchallenge.core.usecase.out.messaging.dto.PaymentStatusCheckMessage;
import br.com.fiap.techchallenge.infra.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitPaymentEventPublisher implements PaymentEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(RabbitPaymentEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitPaymentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishPaymentStatusCheck(PaymentStatusCheckMessage message, long delayMs) {
        MessagePostProcessor postProcessor = msg -> {
            msg.getMessageProperties().setExpiration(String.valueOf(delayMs));
            return msg;
        };

        rabbitTemplate.convertAndSend(
                "",
                RabbitMQConfig.PAYMENT_WAIT_QUEUE,
                message,
                postProcessor
        );

        log.info("Published payment status check: paymentId={}, attempt={}, delay={}ms",
                message.paymentId(), message.attemptNumber(), delayMs);
    }

    @Override
    public void publishPaymentExpired(PaymentStatusCheckMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PAYMENT_CHECK_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_EXPIRED,
                message
        );

        log.info("Published payment expired: paymentId={}, attempt={}",
                message.paymentId(), message.attemptNumber());
    }
}
