package br.com.fiap.techchallenge.core.usecase.in.order;

import br.com.fiap.techchallenge.core.domain.valueobjects.DeliveryAddressSnapshot;
import br.com.fiap.techchallenge.core.domain.valueobjects.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderCommand(
        String restaurantId,
        String userId,
        String userAddressId,
        DeliveryAddressSnapshot deliveryAddress,
        List<OrderItem> items,
        BigDecimal totalAmount
) {
}
