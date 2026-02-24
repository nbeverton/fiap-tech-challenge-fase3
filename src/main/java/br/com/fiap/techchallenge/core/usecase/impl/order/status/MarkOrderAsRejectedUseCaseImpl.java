package br.com.fiap.techchallenge.core.usecase.impl.order.status;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.domain.exception.order.InvalidOrderStatusException;
import br.com.fiap.techchallenge.core.domain.exception.restaurant.RestaurantNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.security.ForbiddenException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.security.AuthContext;
import br.com.fiap.techchallenge.core.usecase.in.order.status.MarkOrderAsRejectedUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

import java.util.List;
import java.util.Set;

public class MarkOrderAsRejectedUseCaseImpl implements MarkOrderAsRejectedUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderFinder orderFinder;
    private final PaymentRepositoryPort paymentRepositoryPort;
    private final RestaurantRepositoryPort restaurantRepositoryPort;

    public MarkOrderAsRejectedUseCaseImpl(OrderRepositoryPort orderRepositoryPort,
                                          PaymentRepositoryPort paymentRepositoryPort,
                                          RestaurantRepositoryPort restaurantRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.orderFinder = new OrderFinder(orderRepositoryPort);
        this.paymentRepositoryPort = paymentRepositoryPort;
        this.restaurantRepositoryPort = restaurantRepositoryPort;
    }

    @Override
    public void reject(String orderId) {

        Order order = orderFinder.findById(orderId);

        // ADMIN pode tudo
        if (AuthContext.isAdmin()) {
            rejectAsAdmin(order);
            return;
        }

        // CLIENT: só pode rejeitar o próprio pedido
        if (AuthContext.isClient()) {
            String requesterId = AuthContext.userId();
            if (!order.getUserId().equals(requesterId)) {
                throw new ForbiddenException("Forbidden: you can only reject your own orders");
            }
            rejectAsClient(order);
            return;
        }

        // OWNER: só pode rejeitar pedidos do próprio restaurante
        if (AuthContext.isOwner()) {
            enforceOwnerOnOrderRestaurant(order);
            rejectAsOwner(order);
            return;
        }

        throw new ForbiddenException("Forbidden");
    }

    // ============================
    // Role-specific logic (keep your business rules)
    // ============================
    private void rejectAsClient(Order order) {
        if (order.getOrderStatus() == OrderStatus.PAYMENT_CONFIRMED
                || order.getOrderStatus() == OrderStatus.PAID) {
            refundAllPaymentsByOrderId(order.getId());
        } else {
            requireStatus(
                    order.getOrderStatus(),
                    Set.of(OrderStatus.CREATED, OrderStatus.AWAITING_PAYMENT, OrderStatus.PAYMENT_CONFIRMED, OrderStatus.PAID),
                    "Order can only be reject when status is CREATED, AWAITING_PAYMENT, PAYMENT_CONFIRMED or PAID."
            );
        }

        order.markOrderAsCancel();
        orderRepositoryPort.save(order);
    }

    private void rejectAsOwner(Order order) {
        if (!(order.getOrderStatus() == OrderStatus.CREATED
                || order.getOrderStatus() == OrderStatus.AWAITING_PAYMENT)) {
            refundAllPaymentsByOrderId(order.getId());
        }

        order.markOrderAsCancel();
        orderRepositoryPort.save(order);
    }

    private void rejectAsAdmin(Order order) {
        // Admin: follow a safe default (same as OWNER behavior)
        if (!(order.getOrderStatus() == OrderStatus.CREATED
                || order.getOrderStatus() == OrderStatus.AWAITING_PAYMENT)) {
            refundAllPaymentsByOrderId(order.getId());
        }

        order.markOrderAsCancel();
        orderRepositoryPort.save(order);
    }

    private void enforceOwnerOnOrderRestaurant(Order order) {
        Restaurant restaurant = restaurantRepositoryPort.findById(order.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException(order.getRestaurantId()));

        String requesterId = AuthContext.userId();
        if (restaurant.getUserId() == null || !restaurant.getUserId().equals(requesterId)) {
            throw new ForbiddenException("Forbidden: you cannot manage orders from another restaurant");
        }
    }

    // ============================
    // Helpers
    // ============================
    private void requireStatus(OrderStatus orderStatus, Set<OrderStatus> allowed, String messageIfInvalid) {
        if (!allowed.contains(orderStatus)) {
            throw new InvalidOrderStatusException(messageIfInvalid);
        }
    }

    private void refundAllPaymentsByOrderId(String orderId){
        List<Payment> payments = paymentRepositoryPort.findByOrderId(orderId);
        payments.forEach(Payment::markAsRefunded);
        paymentRepositoryPort.saveAll(payments);
    }
}