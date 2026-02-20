package br.com.fiap.techchallenge.core.usecase.impl.order.status;

import br.com.fiap.techchallenge.core.usecase.in.order.status.AcceptOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.domain.model.Order;

public class AcceptOrderUseCaseImpl implements AcceptOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderFinder orderFinder;

    public AcceptOrderUseCaseImpl(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.orderFinder = new OrderFinder(orderRepositoryPort);
    }

    @Override
    public void accept(String orderId) {

        Order order = orderFinder.findById(orderId);

        order.markOrderAsAwaitPayment();

        orderRepositoryPort.save(order);
    }
}
