package br.com.fiap.techchallenge.core.usecase.impl.order;

import br.com.fiap.techchallenge.core.domain.exception.order.OrderNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.usecase.in.order.MarkAsPaidOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;

public class MarkAsPaidOrderUseCaseImpl implements MarkAsPaidOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public MarkAsPaidOrderUseCaseImpl(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    @Override
    public void markAsPaid(String orderId) {

        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.markAsPaid();

        orderRepositoryPort.save(order);
    }
}
