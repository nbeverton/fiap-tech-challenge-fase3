package br.com.fiap.techchallenge.infra.web.mapper.order;

import br.com.fiap.techchallenge.core.domain.valueobjects.DeliveryAddressSnapshot;
import br.com.fiap.techchallenge.core.domain.valueobjects.OrderItem;
import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderCommand;
import br.com.fiap.techchallenge.core.usecase.in.order.UpdateOrderCommand;
import br.com.fiap.techchallenge.infra.web.dto.order.CreateOrderItemRequest;
import br.com.fiap.techchallenge.infra.web.dto.order.CreateOrderRequest;
import br.com.fiap.techchallenge.infra.web.dto.order.UpdateOrderRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderWebMapper {

    public static CreateOrderCommand toCommand(String userId, CreateOrderRequest request) {
        List<OrderItem> items = mapItems(request.items());
        BigDecimal totalAmount = calculateTotal(items);

        return new CreateOrderCommand(
                request.restaurantId(),
                userId,
                request.userAddressId(),
                null,
                items,
                totalAmount
        );
    }

    private static List<OrderItem> mapItems(List<CreateOrderItemRequest> requestItems) {
        if (requestItems == null) return new ArrayList<>();

        List<OrderItem> items = new ArrayList<>();
        for (CreateOrderItemRequest i : requestItems) {
            String menuItemId = (i.productId() != null) ? i.productId().toString() : null;

            items.add(new OrderItem(
                    menuItemId,
                    i.name(),
                    i.quantity(),
                    i.price()
            ));
        }
        return items;
    }

    private static BigDecimal calculateTotal(List<OrderItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        if (items == null) return total;

        for (OrderItem item : items) {
            if (item != null && item.getTotal() != null) {
                total = total.add(item.getTotal());
            }
        }
        return total;
    }

    public static UpdateOrderCommand toUpdateCommand(UpdateOrderRequest request) {
        List<OrderItem> items = mapItems(request.items());
        BigDecimal totalAmount = calculateTotal(items);
        return new UpdateOrderCommand(items, totalAmount);
    }
}
