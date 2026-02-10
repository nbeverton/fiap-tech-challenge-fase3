package br.com.fiap.techchallenge.core.usecase.in.order;

import java.util.List;

public record CreateOrderCommand(
        String restaurantId,
        String userAddressId,
        List<CreateOrderItemCommand> items
) {}
