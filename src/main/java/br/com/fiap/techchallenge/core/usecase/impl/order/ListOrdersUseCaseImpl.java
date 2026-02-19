package br.com.fiap.techchallenge.core.usecase.impl.order;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.usecase.in.order.ListOrdersUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;

import java.util.List;

public class ListOrdersUseCaseImpl implements ListOrdersUseCase {

    private final OrderRepositoryPort orderRepository;

    public ListOrdersUseCaseImpl(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> execute() {
        return orderRepository.findAll();
    }

}
