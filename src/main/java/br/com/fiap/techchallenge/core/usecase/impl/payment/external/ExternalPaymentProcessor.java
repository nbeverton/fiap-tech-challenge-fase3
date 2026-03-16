package br.com.fiap.techchallenge.core.usecase.impl.payment.external;

import br.com.fiap.techchallenge.core.usecase.in.order.status.MarkOrderAsPendingPaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.ExternalPaymentGatewayPort;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.dto.ExternalPaymentRequest;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.dto.ExternalPaymentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class ExternalPaymentProcessor {

    private final ExternalPaymentGatewayPort externalPaymentGateway;
    private final MarkOrderAsPendingPaymentUseCase markOrderAsPendingPaymentUseCase;

    public ExternalPaymentProcessor(ExternalPaymentGatewayPort externalPaymentGateway, MarkOrderAsPendingPaymentUseCase markOrderAsPendingPaymentUseCase) {
        this.externalPaymentGateway = externalPaymentGateway;
        this.markOrderAsPendingPaymentUseCase = markOrderAsPendingPaymentUseCase;
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
        markOrderAsPendingPaymentUseCase.execute(orderId, paymentId);

        return CompletableFuture.completedFuture(
                new ExternalPaymentResponse(false, "pending")
        );
    }
}
