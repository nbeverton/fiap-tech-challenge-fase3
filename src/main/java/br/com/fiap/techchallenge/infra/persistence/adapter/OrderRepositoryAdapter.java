package br.com.fiap.techchallenge.infra.persistence.adapter;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.infra.persistence.documents.OrderDocument;
import br.com.fiap.techchallenge.infra.persistence.mapper.order.OrderPersistenceMapper;
import br.com.fiap.techchallenge.infra.persistence.repository.SpringOrderRepository;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final SpringOrderRepository repository;

    public OrderRepositoryAdapter(SpringOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Order save(Order order) {
        OrderDocument doc = OrderPersistenceMapper.toDocument(order);
        OrderDocument saved = repository.save(doc);
        return OrderPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Order> findById(String id) {
        return repository.findById(id)
                .map(OrderPersistenceMapper::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return repository.findAll()
                .stream()
                .map(OrderPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }

        @Override
    public List<Order> findByUserId(String userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(OrderPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }
}
