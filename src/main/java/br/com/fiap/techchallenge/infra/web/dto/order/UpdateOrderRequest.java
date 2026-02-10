package br.com.fiap.techchallenge.infra.web.dto.order;

import java.util.List;

public record UpdateOrderRequest(
        List<CreateOrderItemRequest> items
) {}
