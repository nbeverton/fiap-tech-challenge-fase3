package br.com.fiap.techchallenge.core.domain.enums;

public enum OrderStatus {
    CREATED,
    AWAITING_PAYMENT,
    PAYMENT_CONFIRMED,
    PAID,
    PREPARING,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELED
}
