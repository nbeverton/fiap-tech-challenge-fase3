package br.com.fiap.techchallenge.infra.messaging.kafka.publisher;

import br.com.fiap.techchallenge.core.usecase.out.event.PaymentPendingEventPublisherPort;
import br.com.fiap.techchallenge.infra.messaging.kafka.config.KafkaTopicProperties;
import br.com.fiap.techchallenge.infra.messaging.kafka.event.PaymentPendingEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPaymentPendingEventPublisher extends AbstractKafkaJsonPublisher implements PaymentPendingEventPublisherPort {

    private final KafkaTopicProperties kafkaTopicProperties;


    public KafkaPaymentPendingEventPublisher(
            KafkaTemplate<String,String> kafkaTemplate,
            ObjectMapper objectMapper,
            KafkaTopicProperties kafkaTopicProperties
    ){
        super(kafkaTemplate, objectMapper);
        this.kafkaTopicProperties = kafkaTopicProperties;
    }


    @Override
    public void publish(PaymentPendingEvent event) {

        publish(
                kafkaTopicProperties.getPaymentPending(),
                event.paymentId(),
                event
        );
    }
}
