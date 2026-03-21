package br.com.fiap.techchallenge.infra.messaging.kafka.publisher;

import br.com.fiap.techchallenge.core.usecase.out.event.PaymentApprovedEventPublisherPort;
import br.com.fiap.techchallenge.infra.messaging.kafka.config.KafkaTopicProperties;
import br.com.fiap.techchallenge.infra.messaging.kafka.event.PaymentApprovedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPaymentApprovedEventPublisher extends AbstractKafkaJsonPublisher implements PaymentApprovedEventPublisherPort {

    private final KafkaTopicProperties kafkaTopicProperties;


    public KafkaPaymentApprovedEventPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper,
            KafkaTopicProperties kafkaTopicProperties
    ){
        super(kafkaTemplate, objectMapper);
        this.kafkaTopicProperties = kafkaTopicProperties;
    }


    @Override
    public void publish(PaymentApprovedEvent event) {

        publish(
                kafkaTopicProperties.getPaymentApproved(),
                event.orderId(),
                event
        );
    }
}
