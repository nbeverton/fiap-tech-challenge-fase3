package br.com.fiap.techchallenge.infra.messaging.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.kafka.topic")
public class KafkaTopicProperties {

    private String orderCreated;
    private String paymentApproved;
    private String paymentPending;


    public String getOrderCreated() {
        return orderCreated;
    }

    public void setOrderCreated(String orderCreated) {
        this.orderCreated = orderCreated;
    }

    public String getPaymentApproved() {
        return paymentApproved;
    }

    public void setPaymentApproved(String paymentApproved) {
        this.paymentApproved = paymentApproved;
    }

    public String getPaymentPending() {
        return paymentPending;
    }

    public void setPaymentPending(String paymentPending) {
        this.paymentPending = paymentPending;
    }
}
