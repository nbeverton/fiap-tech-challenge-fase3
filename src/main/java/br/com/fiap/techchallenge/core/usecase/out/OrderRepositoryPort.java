package br.com.fiap.techchallenge.core.usecase.out;

import br.com.fiap.techchallenge.core.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {

    // Criar ou atualizar
    Order save(Order order);

    // Buscar por ID
    Optional<Order> findById(String id);

    // Listar todos
    List<Order> findAll();

    // Deletar por ID
    void deleteById(String id);

    // Verificar existência
    boolean existsById(String id);

    // Listar por usuário
    List<Order> findByUserId(String userId);
}
