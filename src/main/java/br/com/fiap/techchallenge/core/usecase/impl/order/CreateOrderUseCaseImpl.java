package br.com.fiap.techchallenge.core.usecase.impl.order;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderCommand;
import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;

import java.time.Instant;
import java.util.UUID;

public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public CreateOrderUseCaseImpl(OrderRepositoryPort OrderRepositoryPort) {
        this.orderRepositoryPort = OrderRepositoryPort;
    }

    @Override
    public Order create(CreateOrderCommand command) {

        Order order = new Order(
                UUID.randomUUID().toString(),
                command.restaurantId(),
                command.userId(),
                command.userAddressId(),
                command.deliveryAddress(),
                command.items(),
                command.totalAmount(),
                OrderStatus.CREATED,
                Instant.now(),
                Instant.now()
        );

        return orderRepositoryPort.save(order);
    }
}
