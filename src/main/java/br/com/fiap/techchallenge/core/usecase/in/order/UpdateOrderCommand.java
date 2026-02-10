package br.com.fiap.techchallenge.core.usecase.in.order;

import java.util.List;

public record UpdateOrderCommand(
        List<CreateOrderItemCommand> items
) {}
