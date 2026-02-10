package br.com.fiap.techchallenge.infra.web.mapper.order;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.valueobjects.OrderItem;
import br.com.fiap.techchallenge.infra.web.dto.order.CreateOrderResponseDTO;
import br.com.fiap.techchallenge.infra.web.dto.order.OrderItemResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponseMapper {

    public static CreateOrderResponseDTO from(Order order) {

        List<OrderItemResponseDTO> items = order.getItems().stream()
                .map(OrderResponseMapper::mapItem)
                .collect(Collectors.toList());

        return new CreateOrderResponseDTO(
                order.getId(),
                order.getRestaurantId(),
                order.getUserId(),
                order.getUserAddressId(),
                order.getOrderStatus().name(),
                order.getTotalAmount(),
                items
        );
    }

    private static OrderItemResponseDTO mapItem(OrderItem item) {
        return new OrderItemResponseDTO(
                item.getMenuItemId(),
                item.getName(),
                item.getQuantity(),
                item.getPrice(),
                item.getTotal()
        );
    }
}
