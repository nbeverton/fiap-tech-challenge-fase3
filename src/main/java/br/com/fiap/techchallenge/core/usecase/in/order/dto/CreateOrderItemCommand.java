package br.com.fiap.techchallenge.core.usecase.in.order.dto;

public record CreateOrderItemCommand(
        String menuId,
        Integer quantity
) {}
