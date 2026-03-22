package br.com.fiap.techchallenge.infra.messaging.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentApprovedConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentApprovedConsumer.class);

    @KafkaListener(
            id = "paymentApprovedConsumer",
            topics = "${app.kafka.topic.payment-approved}",
            groupId = "${app.kafka.group.payment-approved}"
    )
    public void consume(String payload){
        log.info("Received event from topic pagamento.aprovado: {}", payload);
    }
}
