package br.com.fiap.techchallenge.core.usecase.impl.order.status;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.usecase.in.order.status.OutForDeliveryOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;


public class OutForDeliveryOrderUseCaseImpl implements OutForDeliveryOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderFinder orderFinder;

    public OutForDeliveryOrderUseCaseImpl(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.orderFinder = new OrderFinder(orderRepositoryPort);
    }

    @Override
    public void outForDelivery(String orderId) {

        Order order = orderFinder.findById(orderId);

        order.outForDelivery();

        orderRepositoryPort.save(order);
    }
}
