package br.com.fiap.techchallenge.infra.messaging.kafka.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;

public abstract class AbstractKafkaJsonPublisher {

    private final KafkaTemplate<String,String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    protected AbstractKafkaJsonPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }


    protected void publish(String topic, String key, Object event){

        try{
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, key, payload);

        }catch (JsonProcessingException ex){
            throw new IllegalArgumentException("Failed to serialize Kafka event: " + event.getClass().getSimpleName(), ex);
        }
    }
}
