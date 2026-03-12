package br.com.fiap.techchallenge.infra.messaging;

import br.com.fiap.techchallenge.core.usecase.in.payment.CheckPaymentStatusUseCase;
import br.com.fiap.techchallenge.core.usecase.out.messaging.dto.PaymentStatusCheckMessage;
import br.com.fiap.techchallenge.infra.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentStatusCheckConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentStatusCheckConsumer.class);

    private final CheckPaymentStatusUseCase checkPaymentStatusUseCase;

    public PaymentStatusCheckConsumer(CheckPaymentStatusUseCase checkPaymentStatusUseCase) {
        this.checkPaymentStatusUseCase = checkPaymentStatusUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_CHECK_QUEUE)
    public void onMessage(PaymentStatusCheckMessage message) {
        log.info("Received payment status check: paymentId={}, attempt={}",
                message.paymentId(), message.attemptNumber());

        checkPaymentStatusUseCase.execute(message);
    }
}
