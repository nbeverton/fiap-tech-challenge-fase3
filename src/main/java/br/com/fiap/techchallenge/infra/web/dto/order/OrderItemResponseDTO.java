package br.com.fiap.techchallenge.infra.web.dto.order;

import java.math.BigDecimal;

public record OrderItemResponseDTO(
        String menuItemId,
        String name,
        Integer quantity,
        BigDecimal price,
        BigDecimal total
) {
}


