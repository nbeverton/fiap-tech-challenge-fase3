package br.com.fiap.techchallenge.infra.web.mapper.order;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.infra.web.dto.order.CreateOrderResponseDTO;

import java.util.List;

public class OrderResponseMapper {

    public static CreateOrderResponseDTO from(Order order) {

        return new CreateOrderResponseDTO(
                order.getId(),
                order.getRestaurantId(),
                order.getUserId(),
                order.getOrderStatus().name(),
                order.getTotalAmount(),
                List.of()
        );
    }
}

