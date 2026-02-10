package br.com.fiap.techchallenge.core.usecase.in.order;

public record CreateOrderItemCommand(
        String menuId,
        Integer quantity
) {}
