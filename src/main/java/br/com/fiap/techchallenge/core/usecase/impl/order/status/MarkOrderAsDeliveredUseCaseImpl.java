package br.com.fiap.techchallenge.core.usecase.impl.order.status;

import br.com.fiap.techchallenge.core.domain.exception.restaurant.RestaurantNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.security.ForbiddenException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.security.AuthContext;
import br.com.fiap.techchallenge.core.usecase.in.order.status.MarkOrderAsDeliveredUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

public class MarkOrderAsDeliveredUseCaseImpl implements MarkOrderAsDeliveredUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final RestaurantRepositoryPort restaurantRepositoryPort;
    private final OrderFinder orderFinder;

    public MarkOrderAsDeliveredUseCaseImpl(OrderRepositoryPort orderRepositoryPort,
                                           RestaurantRepositoryPort restaurantRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.restaurantRepositoryPort = restaurantRepositoryPort;
        this.orderFinder = new OrderFinder(orderRepositoryPort);
    }

    @Override
    public void deliver(String orderId) {

        Order order = orderFinder.findById(orderId);

        enforceOwnerOrAdminOnOrderRestaurant(order);

        order.markOrderAsDeliver();
        orderRepositoryPort.save(order);
    }

    private void enforceOwnerOrAdminOnOrderRestaurant(Order order) {
        if (AuthContext.isAdmin()) return;
        if (!AuthContext.isOwner()) throw new ForbiddenException("Forbidden");

        Restaurant restaurant = restaurantRepositoryPort.findById(order.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException(order.getRestaurantId()));

        String requesterUserId = AuthContext.userId();
        if (restaurant.getUserId() == null || !restaurant.getUserId().equals(requesterUserId)) {
            throw new ForbiddenException("Forbidden: you cannot manage orders from another restaurant");
        }
    }
}