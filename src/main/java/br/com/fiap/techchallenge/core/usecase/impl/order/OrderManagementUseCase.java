package br.com.fiap.techchallenge.core.usecase.impl.order;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.domain.valueobjects.DeliveryAddressSnapshot;
import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderCommand;
import br.com.fiap.techchallenge.core.usecase.in.order.UpdateOrderCommand;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OrderManagementUseCase {

    private final OrderRepositoryPort orderRepository;
    private final UserAddressRepositoryPort userAddressRepository;
    private final AddressRepositoryPort addressRepository;

    public OrderManagementUseCase(
            OrderRepositoryPort orderRepository,
            UserAddressRepositoryPort userAddressRepository,
            AddressRepositoryPort addressRepository
    ) {
        this.orderRepository = orderRepository;
        this.userAddressRepository = userAddressRepository;
        this.addressRepository = addressRepository;
    }

    public Order create(CreateOrderCommand command) {

        // 1) Busca o vínculo user_address pelo ID escolhido no request
        UserAddress userAddress = userAddressRepository.findUserAddressById(command.userAddressId())
                .orElseThrow(() -> new RuntimeException("UserAddress not found: " + command.userAddressId()));

        // 2) Busca o address real
        Address address = addressRepository.findById(userAddress.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found: " + userAddress.getAddressId()));

        // 3) Monta snapshot (foto do endereço no momento do pedido)
        DeliveryAddressSnapshot snapshot = new DeliveryAddressSnapshot(
                address.getStreetName(),
                address.getStreetNumber(),
                address.getNeighborhood(),
                address.getCity(),
                address.getStateProvince(),
                address.getPostalCode(),
                address.getAdditionalInfo()
        );

        Order order = new Order(
                UUID.randomUUID().toString(),
                command.restaurantId(),
                command.userId(),
                command.userAddressId(),
                snapshot,
                command.items(),
                command.totalAmount(),
                OrderStatus.CREATED,
                Instant.now(),
                Instant.now()
        );

        return orderRepository.save(order);
    }

    public List<Order> listAll() {
        return orderRepository.findAll();
    }

    public Order update(String orderId, UpdateOrderCommand command) {
        Order existing = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        Order updated = new Order(
                existing.getId(),
                existing.getRestaurantId(),
                existing.getUserId(),
                existing.getUserAddressId(),
                existing.getDeliveryAddress(),
                command.items(),
                command.totalAmount(),
                existing.getOrderStatus(),
                existing.getCreatedAt(),
                Instant.now()
        );

        return orderRepository.save(updated);
    }

    public void delete(String orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new RuntimeException("Order not found: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }
}
