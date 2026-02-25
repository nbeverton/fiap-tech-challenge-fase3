package br.com.fiap.techchallenge.core.usecase.impl.payment;

import br.com.fiap.techchallenge.core.domain.exception.order.OrderAccessDeniedException;
import br.com.fiap.techchallenge.core.domain.exception.order.OrderNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.security.AuthContext;
import br.com.fiap.techchallenge.core.usecase.in.payment.ListPaymentsByOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.dto.PaymentView;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ListPaymentsByOrderUseCaseImpl implements ListPaymentsByOrderUseCase {

    private final PaymentRepositoryPort paymentRepository;
    private final OrderRepositoryPort orderRepository;
    private final RestaurantRepositoryPort restaurantRepository;

    public ListPaymentsByOrderUseCaseImpl(
            PaymentRepositoryPort paymentRepository,
            OrderRepositoryPort orderRepository,
            RestaurantRepositoryPort restaurantRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<PaymentView> execute(String orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!canAccess(order)) {
            throw new OrderAccessDeniedException();
        }

        List<Payment> list = paymentRepository.findByOrderId(orderId);

        return list.stream().map(this::toView).toList();
    }

    private boolean canAccess(Order order) {
        if (AuthContext.isAdmin()) {
            return true;
        }

        String requesterId = AuthContext.userId();

        if (AuthContext.isClient()) {
            return requesterId.equals(order.getUserId());
        }

        if (AuthContext.isOwner()) {
            List<Restaurant> myRestaurants = restaurantRepository.findByUserId(requesterId);

            Set<String> myRestaurantIds = myRestaurants.stream()
                    .map(Restaurant::getId)
                    .collect(Collectors.toSet());

            return myRestaurantIds.contains(order.getRestaurantId());
        }

        return false;
    }

    private PaymentView toView(Payment p) {
        return new PaymentView(
                p.getId(), p.getOrderId(), p.getCreatedAt(),
                p.getAmount(), p.getMethod(), p.getStatus(),
                p.getTransactionId(), p.getProvider(),
                p.getPaidAt(), p.getFailedAt(), p.getRefundedAt()
        );
    }
}