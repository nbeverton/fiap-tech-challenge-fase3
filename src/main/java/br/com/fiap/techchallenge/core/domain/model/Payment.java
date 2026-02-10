package br.com.fiap.techchallenge.core.domain.model;

import br.com.fiap.techchallenge.core.domain.enums.PaymentMethod;
import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.domain.exception.payment.InvalidPaymentException;

import java.math.BigDecimal;
import java.time.Instant;

public class Payment {

    private final String id;
    private final String orderId;
    private final Instant createdAt;

    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;

    private String transactionId;
    private String provider;
    private Instant paidAt;
    private Instant failedAt;
    private Instant refundedAt;


    public Payment(String id,
                   String orderId,
                   Instant createdAt,
                   BigDecimal amount,
                   PaymentMethod method,
                   PaymentStatus status,
                   String transactionId,
                   String provider,
                   Instant paidAt,
                   Instant failedAt,
                   Instant refundedAt) {

        this.id =                               requireNonBlank(id, "id");
        this.orderId =                          requireNonBlank(orderId, "orderId");
        this.createdAt =                        requirePastOrPresent(createdAt, "createdAt");
        this.amount =                           requirePositive(amount, "amount");
        this.method = method =                  PaymentMethod.valueOf(requireNonBlank(method.toString(), "method"));
        this.status = status =                  PaymentStatus.valueOf(requireNonBlank(status.toString(), "status"));

        this.transactionId = transactionId;     // opcional
        this.provider = provider;               // opcional
        this.paidAt = paidAt;                   // opcional
        this.failedAt = failedAt;               // opcional
        this.refundedAt = refundedAt;           // opcional
    }


    // ============================
    // Regras de negócio
    // ============================
    private String requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidPaymentException(fieldName + " must not be null or blank");
        }
        return value;
    }

    private Instant requirePastOrPresent(Instant value, String fieldName) {
        if (value == null) {
            throw new InvalidPaymentException(fieldName + " must not be null");
        }

        Instant now = Instant.now();
        if (value.isAfter(now)) {
            throw new InvalidPaymentException(fieldName + " must not be in the future");
        }

        return value;
    }

    private BigDecimal requirePositive(BigDecimal value, String fieldName) {
        if (value == null) {
            throw new InvalidPaymentException(fieldName + " must not be null");
        }
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentException(fieldName + " must be greater than 0");
        }
        return value;
    }
}
