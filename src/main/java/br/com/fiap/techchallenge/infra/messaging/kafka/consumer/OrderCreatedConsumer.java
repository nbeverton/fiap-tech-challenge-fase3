package br.com.fiap.techchallenge.infra.messaging.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedConsumer.class);


    @KafkaListener(
            id = "orderCreatedConsumer",
            topics = "${app.kafka.topic.order-created}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(String payload){
        log.info("Received event from topic pedido.criado: {}", payload);
    }
}
