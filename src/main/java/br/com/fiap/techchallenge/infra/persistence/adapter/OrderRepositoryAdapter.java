package br.com.fiap.techchallenge.infra.persistence.adapter;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.infra.persistence.documents.OrderDocument;
import br.com.fiap.techchallenge.infra.persistence.repository.SpringOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final SpringOrderRepository springOrderRepository;

    public OrderRepositoryAdapter(SpringOrderRepository springOrderRepository) {
        this.springOrderRepository = springOrderRepository;
    }

    @Override
    public Order save(Order order) {

        OrderDocument document = toDocument(order);

        OrderDocument saved = springOrderRepository.save(document);

        return toDomain(saved);
    }

    @Override
    public Optional<Order> findById(String id) {

        return springOrderRepository
                .findById(id)
                .map(this::toDomain);
    }

    private OrderDocument toDocument(Order order) {

        OrderDocument document = new OrderDocument();

        document.setId(order.getId());
        document.setRestaurantId(order.getRestaurantId());
        document.setUserId(order.getUserId());
        document.setCourierId(order.getCourierId());
        document.setDeliveryAddress(order.getDeliveryAddress());
        document.setDescription(order.getDescription());
        document.setOrderStatus(order.getOrderStatus());
        document.setTotalAmount(order.getTotalAmount());
        document.setOrderTaxes(order.getOrderTaxes());
        document.setCreatedAt(order.getCreatedAt());
        document.setUpdatedAt(order.getUpdatedAt());

        return document;
    }

    private Order toDomain(OrderDocument d) {

        return new Order(
                d.getId(),
                d.getRestaurantId(),
                d.getUserId(),
                d.getCourierId(),
                d.getDeliveryAddress(),
                d.getDescription(),
                d.getOrderStatus(),
                d.getTotalAmount(),
                d.getOrderTaxes(),
                d.getCreatedAt(),
                d.getUpdatedAt()
        );
    }
}

