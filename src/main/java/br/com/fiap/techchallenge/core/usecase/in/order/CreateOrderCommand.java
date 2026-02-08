package br.com.fiap.techchallenge.core.usecase.in.order;

import java.math.BigDecimal;

public record CreateOrderCommand(
        String restaurantId,
        String userId,
        String courierId,
        String deliveryAddress,
        String description,
        BigDecimal totalAmount,
        BigDecimal orderTaxes
) {
}
