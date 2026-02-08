package br.com.fiap.techchallenge.core.usecase.impl.order;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.usecase.in.order.StartPreparingOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;

public class StartPreparingOrderUseCaseImpl implements StartPreparingOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderFinder orderFinder;

    public StartPreparingOrderUseCaseImpl(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.orderFinder = new OrderFinder(orderRepositoryPort);
    }

    @Override
    public void startPreparing(String orderId) {

        Order order = orderFinder.findById(orderId);

        order.startPreparing();

        orderRepositoryPort.save(order);
    }
    
}
