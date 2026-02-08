package br.com.fiap.techchallenge.infra.web.dto.order;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderRequest(
        String restaurantId,
        String deliveryAddress,
        List<CreateOrderItemRequest> items
) {
}
