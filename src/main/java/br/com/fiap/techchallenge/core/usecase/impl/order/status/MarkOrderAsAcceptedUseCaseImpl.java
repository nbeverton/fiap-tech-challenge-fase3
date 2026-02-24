package br.com.fiap.techchallenge.core.usecase.impl.order.status;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.domain.exception.order.InvalidOrderStatusException;
import br.com.fiap.techchallenge.core.domain.exception.restaurant.RestaurantNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.security.ForbiddenException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.security.AuthContext;
import br.com.fiap.techchallenge.core.usecase.in.order.status.MarkOrderAsAcceptedUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

public class MarkOrderAsAcceptedUseCaseImpl implements MarkOrderAsAcceptedUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final RestaurantRepositoryPort restaurantRepositoryPort;
    private final OrderFinder orderFinder;

    public MarkOrderAsAcceptedUseCaseImpl(OrderRepositoryPort orderRepositoryPort,
                                          RestaurantRepositoryPort restaurantRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.restaurantRepositoryPort = restaurantRepositoryPort;
        this.orderFinder = new OrderFinder(orderRepositoryPort);
    }

    @Override
    public void accept(String orderId) {

        Order order = orderFinder.findById(orderId);

        enforceOwnerOrAdminOnOrderRestaurant(order);

        if (order.getOrderStatus() == OrderStatus.CREATED) {
            order.markOrderAsAwaitPayment();
            orderRepositoryPort.save(order);
        } else if (order.getOrderStatus() == OrderStatus.PAYMENT_CONFIRMED) {
            order.markOrderAsPaid();
            orderRepositoryPort.save(order);
        } else {
            throw new InvalidOrderStatusException(
                    "Order can only be accepted when status is CREATED or PAYMENT_CONFIRMED."
            );
        }
    }

    private void enforceOwnerOrAdminOnOrderRestaurant(Order order) {
        if (AuthContext.isAdmin()) return;

        // must be OWNER (SecurityConfig should already enforce, but keep safe)
        if (!AuthContext.isOwner()) {
            throw new ForbiddenException("Forbidden");
        }

        Restaurant restaurant = restaurantRepositoryPort.findById(order.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException(order.getRestaurantId()));

        String requesterUserId = AuthContext.userId();
        if (restaurant.getUserId() == null || !restaurant.getUserId().equals(requesterUserId)) {
            throw new ForbiddenException("Forbidden: you cannot manage orders from another restaurant");
        }
    }
}