package br.com.fiap.techchallenge.infra.web.dto.order;

public record CreateOrderItemRequest(
        String menuId,
        Integer quantity
) {}
