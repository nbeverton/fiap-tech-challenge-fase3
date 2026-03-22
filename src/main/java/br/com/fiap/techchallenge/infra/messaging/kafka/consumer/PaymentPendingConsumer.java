package br.com.fiap.techchallenge.infra.messaging.kafka.consumer;

import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.domain.exception.payment.PaymentNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.usecase.in.payment.external.ReprocessPendingPaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;
import br.com.fiap.techchallenge.infra.messaging.kafka.event.PaymentPendingEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentPendingConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentPendingConsumer.class);

    private final ObjectMapper objectMapper;
    private final ReprocessPendingPaymentUseCase reprocessPendingPaymentUseCase;
    private final PaymentRepositoryPort paymentRepository;


    public PaymentPendingConsumer(
            ObjectMapper objectMapper,
            ReprocessPendingPaymentUseCase reprocessPendingPaymentUseCase,
            PaymentRepositoryPort paymentRepositoryPort) {

        this.objectMapper = objectMapper;
        this.reprocessPendingPaymentUseCase = reprocessPendingPaymentUseCase;
        this.paymentRepository = paymentRepositoryPort;
    }


    @KafkaListener(
            id = "paymentPendingConsumer",
            topics = "${app.kafka.topic.payment-pending}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "paymentPendingKafkaListenerContainerFactory"
    )
    public void consume(String payload){

        PaymentPendingEvent event = deserialize(payload);

        log.info("Received event from topic pagamento.pendente: {}", payload);

        reprocessPendingPaymentUseCase.execute(event.paymentId());

        Payment payment = paymentRepository.findById(event.paymentId())
                .orElseThrow(()-> new PaymentNotFoundException(event.paymentId()));

        if(isTechnicalPending(payment)){
            log.warn("Payment {} is still pending. A new retry will happen after backoff.", event.paymentId());
            throw new IllegalStateException("Payment still pending after reprocessing attempt: " + event.paymentId());
        }

        log.info("Payment {} reprocessed successfully.", event.paymentId());
    }


    private PaymentPendingEvent deserialize(String payload){

        try{
            return objectMapper.readValue(payload, PaymentPendingEvent.class);

        }catch (JsonProcessingException ex){
            throw new IllegalStateException("Failed to deserialize PaymentPendingEvent", ex);
        }
    }

    private boolean isTechnicalPending(Payment payment){
        return payment.getStatus() == PaymentStatus.PENDING
                && payment.getFailedAt() != null
                && payment.getPaidAt() == null
                && payment.getTransactionId() == null
                && payment.getProvider() == null;
    }
}
