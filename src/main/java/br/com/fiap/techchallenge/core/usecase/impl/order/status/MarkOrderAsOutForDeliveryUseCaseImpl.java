package br.com.fiap.techchallenge.core.usecase.impl.order.status;

import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.domain.exception.order.InvalidOrderStatusException;
import br.com.fiap.techchallenge.core.domain.exception.restaurant.RestaurantNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.security.ForbiddenException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.security.AuthContext;
import br.com.fiap.techchallenge.core.usecase.in.order.status.MarkOrderAsOutForDeliveryUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

public class MarkOrderAsOutForDeliveryUseCaseImpl implements MarkOrderAsOutForDeliveryUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final RestaurantRepositoryPort restaurantRepositoryPort;
    private final PaymentRepositoryPort paymentRepositoryPort;
    private final OrderFinder orderFinder;

    public MarkOrderAsOutForDeliveryUseCaseImpl(OrderRepositoryPort orderRepositoryPort,
                                                RestaurantRepositoryPort restaurantRepositoryPort,
                                                PaymentRepositoryPort paymentRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.restaurantRepositoryPort = restaurantRepositoryPort;
        this.paymentRepositoryPort = paymentRepositoryPort;
        this.orderFinder = new OrderFinder(orderRepositoryPort);
    }

    @Override
    public void outForDelivery(String orderId) {

        Order order = orderFinder.findById(orderId);

        enforceOwnerOrAdminOnOrderRestaurant(order);

        // ✅ REGRA: se existe pagamento REFUNDED, não pode sair para entrega
        boolean refunded = paymentRepositoryPort.existsByOrderIdAndStatus(orderId, PaymentStatus.REFUNDED);
        if (refunded) {
            throw new InvalidOrderStatusException("Cannot set Out For Delivery: payment was refunded.");
        }

        order.markOrderAsOutForDelivery();
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