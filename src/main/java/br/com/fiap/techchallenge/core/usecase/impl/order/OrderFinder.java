package br.com.fiap.techchallenge.core.usecase.impl.order;

import br.com.fiap.techchallenge.core.domain.exception.order.OrderNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;

public class OrderFinder {

    private final OrderRepositoryPort repository;

    OrderFinder(OrderRepositoryPort repository) {
        this.repository = repository;
    }

    Order findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
}
