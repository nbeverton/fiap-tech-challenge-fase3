package br.com.fiap.techchallenge.infra.messaging.kafka.publisher;

import br.com.fiap.techchallenge.core.usecase.out.event.OrderCreatedEventPublisherPort;
import br.com.fiap.techchallenge.infra.messaging.kafka.config.KafkaTopicProperties;
import br.com.fiap.techchallenge.infra.messaging.kafka.event.OrderCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaOrderCreatedEventPublisher extends AbstractKafkaJsonPublisher implements OrderCreatedEventPublisherPort {

    private final KafkaTopicProperties kafkaTopicProperties;


    public KafkaOrderCreatedEventPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper,
            KafkaTopicProperties kafkaTopicProperties
    ){
        super(kafkaTemplate, objectMapper);
        this.kafkaTopicProperties = kafkaTopicProperties;
    }

    @Override
    public void publish(OrderCreatedEvent event) {

        publish(
                kafkaTopicProperties.getOrderCreated(),
                event.orderId(),
                event
        );
    }
}
