package br.com.fiap.techchallenge.core.usecase.impl.order;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.domain.exception.order.*;
import br.com.fiap.techchallenge.core.domain.exception.restaurant.RestaurantNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.valueobjects.Menu;
import br.com.fiap.techchallenge.core.domain.valueobjects.OrderItem;
import br.com.fiap.techchallenge.core.usecase.in.order.CreateOrderItemCommand;
import br.com.fiap.techchallenge.core.usecase.in.order.UpdateOrderCommand;
import br.com.fiap.techchallenge.core.usecase.in.order.UpdateOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class UpdateOrderUseCaseImpl implements UpdateOrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final RestaurantRepositoryPort restaurantRepository;

    public UpdateOrderUseCaseImpl(
            OrderRepositoryPort orderRepository,
            RestaurantRepositoryPort restaurantRepository
    ) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Order execute(String orderId, UpdateOrderCommand command) {
        Order existing = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!canEditItems(existing.getOrderStatus())) {
            throw new InvalidOrderStatusException(
                    "Order cannot be edited when status is " + existing.getOrderStatus()
            );
        }

        Restaurant restaurant = restaurantRepository.findById(existing.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException(existing.getRestaurantId()));

        List<OrderItem> newItems = buildOrderItemsFromRestaurantMenu(restaurant, command.items());
        BigDecimal newTotalAmount = calculateTotal(newItems);

        Order updated = new Order(
                existing.getId(),
                existing.getRestaurantId(),
                existing.getUserId(),
                existing.getUserAddressId(),
                existing.getDeliveryAddress(),   // snapshot não muda
                newItems,
                newTotalAmount,
                existing.getOrderStatus(),       // status não muda aqui
                existing.getCreatedAt(),
                Instant.now()
        );

        return orderRepository.save(updated);
    }

    // ----------------- helpers locais -----------------

    private boolean canEditItems(OrderStatus status) {
        return status == OrderStatus.CREATED
                || status == OrderStatus.AWAITING_PAYMENT;
    }

    private List<OrderItem> buildOrderItemsFromRestaurantMenu(
            Restaurant restaurant,
            List<CreateOrderItemCommand> itemCommands
    ) {
        if (itemCommands == null || itemCommands.isEmpty()) {
            throw new EmptyOrderItemsException();
        }

        List<OrderItem> items = new ArrayList<>();

        for (CreateOrderItemCommand cmd : itemCommands) {
            if (cmd.menuId() == null || cmd.menuId().isBlank()) {
                throw new MissingMenuIdException();
            }
            if (cmd.quantity() == null || cmd.quantity() <= 0) {
                throw new InvalidOrderItemQuantityException(cmd.menuId());
            }

            Menu menu = findMenuOrThrow(restaurant, cmd.menuId());

            items.add(new OrderItem(
                    menu.getId(),
                    menu.getName(),
                    cmd.quantity(),
                    menu.getPrice()
            ));
        }

        return items;
    }

    private Menu findMenuOrThrow(Restaurant restaurant, String menuId) {
        for (Menu m : restaurant.getMenu()) {
            if (m.getId() != null && m.getId().equals(menuId)) {
                return m;
            }
        }
        throw new MenuDoesNotBelongToRestaurantException(menuId, restaurant.getId());
    }

    private BigDecimal calculateTotal(List<OrderItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            total = total.add(item.getTotal());
        }
        return total;
    }

}
