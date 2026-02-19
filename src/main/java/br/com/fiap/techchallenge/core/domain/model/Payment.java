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

        this.createdAt =                        createdAt;

        this.id =                               requireNonBlank(id, "id");
        this.orderId =                          requireNonBlank(orderId, "orderId");
        this.amount =                           requirePositive(amount, "amount");

        this.method =                           requireNonNull(method, "method");
        this.status =                           requireNonNull(status, "status");

        this.transactionId = transactionId;     // opcional
        this.provider = provider;               // opcional
        this.paidAt = paidAt;                   // opcional
        this.failedAt = failedAt;               // opcional
        this.refundedAt = refundedAt;           // opcional
    }


    // ============================
    // Business role
    // ============================
    private String requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidPaymentException(fieldName + " must not be null or blank");
        }
        return value;
    }

    private <T> T requireNonNull(T value, String fieldName) {
        if (value == null) {
            throw new InvalidPaymentException(fieldName + " must not be null");
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

    // ============================
    // Getters
    // ============================
    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getProvider() {
        return provider;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public Instant getFailedAt() {
        return failedAt;
    }

    public Instant getRefundedAt() {
        return refundedAt;
    }


    // ============================
    // Update Status
    // ============================
    public void markAsPaid(){
        this.status = PaymentStatus.PAID;
        this.paidAt = Instant.now();
    }

    public void markAsFailed() {
        this.status = PaymentStatus.FAILED;
        this.failedAt = Instant.now();
    }

    public void markAsRefunded() {
        this.status = PaymentStatus.REFUNDED;
        this.refundedAt = Instant.now();
    }
}
