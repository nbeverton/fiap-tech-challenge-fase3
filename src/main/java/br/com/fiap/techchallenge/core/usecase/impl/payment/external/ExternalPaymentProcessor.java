package br.com.fiap.techchallenge.core.usecase.impl.payment.external;

import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.usecase.in.order.status.MarkOrderAsPendingPaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.event.PaymentPendingEventPublisherPort;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.ExternalPaymentGatewayPort;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.dto.ExternalPaymentRequest;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.dto.ExternalPaymentResponse;
import br.com.fiap.techchallenge.infra.messaging.kafka.event.PaymentPendingEvent;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class ExternalPaymentProcessor {

    private final ExternalPaymentGatewayPort externalPaymentGateway;
    private final MarkOrderAsPendingPaymentUseCase markOrderAsPendingPaymentUseCase;
    private final PaymentPendingEventPublisherPort paymentPendingEventPublisher;
    private final PaymentRepositoryPort paymentRepository;

    public ExternalPaymentProcessor(ExternalPaymentGatewayPort externalPaymentGateway, MarkOrderAsPendingPaymentUseCase markOrderAsPendingPaymentUseCase, PaymentPendingEventPublisherPort paymentPendingEventPublisher, PaymentRepositoryPort paymentRepository) {
        this.externalPaymentGateway = externalPaymentGateway;
        this.markOrderAsPendingPaymentUseCase = markOrderAsPendingPaymentUseCase;
        this.paymentPendingEventPublisher = paymentPendingEventPublisher;
        this.paymentRepository = paymentRepository;
    }


    @CircuitBreaker(name = "externalPaymentProcessor", fallbackMethod = "submitPaymentFallback")
    @TimeLimiter(name = "externalPaymentProcessor")
    public CompletableFuture<ExternalPaymentResponse> submitPayment(
            ExternalPaymentRequest request,
            String orderId,
            String paymentId
    ) {
        return CompletableFuture.supplyAsync(() ->
                externalPaymentGateway.submitPayment(request)
        );
    }

    private CompletableFuture<ExternalPaymentResponse> submitPaymentFallback(
            ExternalPaymentRequest request,
            String orderId,
            String paymentId,
            Throwable ex
    ) {

        Payment existingPayment = paymentRepository.findById(paymentId).orElse(null);
        boolean firstTechnicalFailure = existingPayment == null || existingPayment.getFailedAt() == null;

        markOrderAsPendingPaymentUseCase.execute(orderId, paymentId);

        if(firstTechnicalFailure){
            paymentPendingEventPublisher.publish(
                    new PaymentPendingEvent(
                            UUID.randomUUID().toString(),
                            "pagamento.pendente",
                            Instant.now(),
                            paymentId
                    )
            );
        }

        return CompletableFuture.completedFuture(
                new ExternalPaymentResponse(false, "pending")
        );
    }
}
