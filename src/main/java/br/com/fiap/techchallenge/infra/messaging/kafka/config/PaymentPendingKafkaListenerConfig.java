package br.com.fiap.techchallenge.infra.messaging.kafka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class PaymentPendingKafkaListenerConfig {


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> paymentPendingKafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            @Value("${payment.reprocessing.fixed-delay-ms:30000}") long fixedDelayMs
    ){
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        DefaultErrorHandler errorHandler =
                new DefaultErrorHandler(new FixedBackOff(fixedDelayMs, FixedBackOff.UNLIMITED_ATTEMPTS));

        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }
}
