package br.com.fiap.techchallenge.infra.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PAYMENT_CHECK_EXCHANGE = "payment.check.exchange";
    public static final String PAYMENT_CHECK_QUEUE = "payment.check.queue";
    public static final String PAYMENT_WAIT_QUEUE = "payment.wait.queue";
    public static final String PAYMENT_EXPIRED_QUEUE = "payment.expired.queue";

    public static final String ROUTING_KEY_CHECK = "payment.check";
    public static final String ROUTING_KEY_EXPIRED = "payment.expired";

    @Bean
    public DirectExchange paymentCheckExchange() {
        return new DirectExchange(PAYMENT_CHECK_EXCHANGE, true, false);
    }

    @Bean
    public Queue paymentCheckQueue() {
        return QueueBuilder.durable(PAYMENT_CHECK_QUEUE).build();
    }

    @Bean
    public Queue paymentWaitQueue() {
        return QueueBuilder.durable(PAYMENT_WAIT_QUEUE)
                .withArgument("x-dead-letter-exchange", PAYMENT_CHECK_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY_CHECK)
                .build();
    }

    @Bean
    public Queue paymentExpiredQueue() {
        return QueueBuilder.durable(PAYMENT_EXPIRED_QUEUE).build();
    }

    @Bean
    public Binding paymentCheckBinding(Queue paymentCheckQueue, DirectExchange paymentCheckExchange) {
        return BindingBuilder.bind(paymentCheckQueue).to(paymentCheckExchange).with(ROUTING_KEY_CHECK);
    }

    @Bean
    public Binding paymentExpiredBinding(Queue paymentExpiredQueue, DirectExchange paymentCheckExchange) {
        return BindingBuilder.bind(paymentExpiredQueue).to(paymentCheckExchange).with(ROUTING_KEY_EXPIRED);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
