package br.com.fiap.techchallenge.infra.web.dto.order;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderResponseDTO(
        String id,
        String restaurantId,
        String userId,
        String userAddressId,
        String status,
        BigDecimal totalAmount,
        List<OrderItemResponseDTO> items
) { }


