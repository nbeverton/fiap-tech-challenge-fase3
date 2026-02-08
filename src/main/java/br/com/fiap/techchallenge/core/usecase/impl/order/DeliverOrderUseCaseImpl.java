package br.com.fiap.techchallenge.core.usecase.impl.order;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.usecase.in.order.DeliverOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;

public class DeliverOrderUseCaseImpl implements DeliverOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderFinder orderFinder;

    public DeliverOrderUseCaseImpl(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.orderFinder = new OrderFinder(orderRepositoryPort);
    }

    @Override
    public void deliver(String orderId) {

        Order order = orderFinder.findById(orderId);

        order.deliver();

        orderRepositoryPort.save(order);
    }
}
