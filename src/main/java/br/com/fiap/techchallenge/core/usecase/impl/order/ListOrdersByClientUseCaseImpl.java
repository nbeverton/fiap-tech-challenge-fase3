package br.com.fiap.techchallenge.core.usecase.impl.order;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.usecase.in.order.ListOrdersByClientUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;

import java.util.List;

public class ListOrdersByClientUseCaseImpl implements ListOrdersByClientUseCase {

    private final OrderRepositoryPort orderRepository;

    public ListOrdersByClientUseCaseImpl(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> execute(String clientId) {
        return orderRepository.findByUserId(clientId);
    }
}