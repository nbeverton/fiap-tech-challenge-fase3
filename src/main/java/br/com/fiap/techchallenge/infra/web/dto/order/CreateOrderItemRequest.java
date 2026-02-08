package br.com.fiap.techchallenge.infra.web.dto.order;

import java.math.BigDecimal;

public record CreateOrderItemRequest(
    
    Long productId,
    String name,
    Integer quantity,
    BigDecimal price
) {
    
}
