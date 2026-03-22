package br.com.fiap.techchallenge.core.usecase.impl.order;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.usecase.in.order.ListOrdersByUserUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;

import java.util.List;

public class ListOrdersByUserUseCaseImpl implements ListOrdersByUserUseCase {

    private final OrderRepositoryPort orderRepository;

    public ListOrdersByUserUseCaseImpl(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> execute(String clientId) {
        return orderRepository.findByUserId(clientId);
    }
}