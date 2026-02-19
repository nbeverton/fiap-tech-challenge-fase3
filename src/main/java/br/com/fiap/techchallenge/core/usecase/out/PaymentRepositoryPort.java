package br.com.fiap.techchallenge.core.usecase.out;

import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.domain.model.Payment;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PaymentRepositoryPort {

    Payment save(Payment payment);

    Optional<Payment> findById(String paymentId);

    List<Payment> findByOrderId(String orderId);

    void updateStatusAndProviderData(
            String paymentId,
            PaymentStatus status,
            String transactionId,
            String provider,
            Instant paid,
            Instant failedAt,
            Instant refundedAt
    );
}
