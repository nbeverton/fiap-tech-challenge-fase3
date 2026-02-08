package br.com.fiap.techchallenge.core.usecase.impl.order;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderCommand;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OrderManagementUseCase {

    private final OrderRepositoryPort orderRepository;

    public OrderManagementUseCase(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    // --- CREATE ---
    public Order create(CreateOrderCommand command) {
        Order order = new Order(
                UUID.randomUUID().toString(),
                command.restaurantId(),
                command.userId(),
                command.courierId(),
                command.deliveryAddress(),
                command.description(),
                OrderStatus.PENDING,
                command.totalAmount(),
                command.orderTaxes(),
                Instant.now(),
                Instant.now()
        );
        return orderRepository.save(order);
    }

    // --- LIST ALL ---
    public List<Order> listAll() {
        return orderRepository.findAll();
    }

    // --- UPDATE ---
    public Order update(String orderId, CreateOrderCommand command) {
        Order existing = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        // Cria um novo objeto Order mantendo o status e createdAt
        Order updated = new Order(
                existing.getId(),
                command.restaurantId(),
                command.userId(),
                command.courierId(),
                command.deliveryAddress(),
                command.description(),
                existing.getOrderStatus(),  // mantém o status atual
                command.totalAmount(),
                command.orderTaxes(),
                existing.getCreatedAt(),
                Instant.now() // atualiza o updatedAt
        );

        return orderRepository.save(updated);
    }

    // --- DELETE ---
    public void delete(String orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new RuntimeException("Order not found: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }
}
