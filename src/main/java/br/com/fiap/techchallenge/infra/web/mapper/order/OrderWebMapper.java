package br.com.fiap.techchallenge.infra.web.mapper.order;

import br.com.fiap.techchallenge.core.usecase.in.order.dto.CreateOrderCommand;
import br.com.fiap.techchallenge.core.usecase.in.order.dto.CreateOrderItemCommand;
import br.com.fiap.techchallenge.core.usecase.in.order.dto.UpdateOrderCommand;
import br.com.fiap.techchallenge.infra.web.dto.order.CreateOrderRequest;
import br.com.fiap.techchallenge.infra.web.dto.order.UpdateOrderRequest;

import java.util.List;

public class OrderWebMapper {

    public static CreateOrderCommand toCommand(
            CreateOrderRequest request,
            String clientId
    ) {

        List<CreateOrderItemCommand> items = request.items().stream()
                .map(i -> new CreateOrderItemCommand(
                        i.menuId(),
                        i.quantity()
                ))
                .toList();

        return new CreateOrderCommand(
                clientId,                 // 👈 vem do JWT
                request.restaurantId(),
                request.userAddressId(),
                items
        );
    }

    public static UpdateOrderCommand toUpdateCommand(UpdateOrderRequest request) {

        List<CreateOrderItemCommand> items = request.items().stream()
                .map(i -> new CreateOrderItemCommand(
                        i.menuId(),
                        i.quantity()
                ))
                .toList();

        return new UpdateOrderCommand(items);
    }
}
