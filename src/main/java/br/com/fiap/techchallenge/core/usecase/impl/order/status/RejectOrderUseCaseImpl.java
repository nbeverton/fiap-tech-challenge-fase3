package br.com.fiap.techchallenge.core.usecase.impl.order.status;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.usecase.in.order.status.RejectOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;

public class RejectOrderUseCaseImpl implements RejectOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderFinder orderFinder;

    public RejectOrderUseCaseImpl(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.orderFinder = new OrderFinder(orderRepositoryPort);
    }

    @Override
    public void reject(String orderId) {

        Order order = orderFinder.findById(orderId);

        order.cancel();

        orderRepositoryPort.save(order);
    }
}