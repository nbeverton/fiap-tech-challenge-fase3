package br.com.fiap.techchallenge.core.usecase.in.order.dto;

import java.util.List;

public record UpdateOrderCommand(
        List<CreateOrderItemCommand> items
) {}
