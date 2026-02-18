package br.com.fiap.techchallenge.core.usecase.in.order;

import java.util.List;

public record CreateOrderCommand(
        String clientId,          // 👈 vem do JWT
        String restaurantId,
        String userAddressId,
        List<CreateOrderItemCommand> items
) {}
