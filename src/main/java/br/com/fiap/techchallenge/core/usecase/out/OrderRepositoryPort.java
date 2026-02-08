package br.com.fiap.techchallenge.core.usecase.out;

import br.com.fiap.techchallenge.core.domain.model.Order;

import java.util.Optional;

public interface OrderRepositoryPort {
    
        Order save(Order order);

    Optional<Order> findById(String id);
}

