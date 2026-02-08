package br.com.fiap.techchallenge.infra.web.mapper.order;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderCommand;
import br.com.fiap.techchallenge.infra.web.dto.order.CreateOrderRequest;

import java.math.BigDecimal;
import java.time.Instant;

public class OrderWebMapper {

   
    public static CreateOrderCommand toCommand(String userId, CreateOrderRequest request) {

        return new CreateOrderCommand(
                request.restaurantId(),
                userId,
                null, // courierId
                request.deliveryAddress(),
                null, // description
                BigDecimal.ZERO, // total
                BigDecimal.ZERO  // taxes
        );
    }
}
