package br.com.fiap.techchallenge.infra.web.mapper.order;

import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderCommand;
import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderItemCommand;
import br.com.fiap.techchallenge.core.usecase.in.order.UpdateOrderCommand;
import br.com.fiap.techchallenge.infra.web.dto.order.CreateOrderRequest;
import br.com.fiap.techchallenge.infra.web.dto.order.UpdateOrderRequest;

import java.util.List;

public class OrderWebMapper {

    public static CreateOrderCommand toCommand(CreateOrderRequest request) {

        List<CreateOrderItemCommand> items = request.items().stream()
                .map(i -> new CreateOrderItemCommand(i.menuId(), i.quantity()))
                .toList();

        return new CreateOrderCommand(
                request.restaurantId(),
                request.userAddressId(),
                items
        );
    }

    public static UpdateOrderCommand toUpdateCommand(UpdateOrderRequest request) {

        List<CreateOrderItemCommand> items = request.items().stream()
                .map(i -> new CreateOrderItemCommand(i.menuId(), i.quantity()))
                .toList();

        return new UpdateOrderCommand(items);
    }
}
