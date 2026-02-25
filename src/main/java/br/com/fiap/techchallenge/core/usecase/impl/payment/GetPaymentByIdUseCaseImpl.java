package br.com.fiap.techchallenge.core.usecase.impl.payment;

import br.com.fiap.techchallenge.core.domain.exception.order.OrderAccessDeniedException;
import br.com.fiap.techchallenge.core.domain.exception.order.OrderNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.payment.PaymentNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.payment.PaymentOrderMismatchException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.security.AuthContext;
import br.com.fiap.techchallenge.core.usecase.in.payment.GetPaymentByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.dto.PaymentView;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GetPaymentByIdUseCaseImpl implements GetPaymentByIdUseCase {

    private final PaymentRepositoryPort paymentRepository;
    private final OrderRepositoryPort orderRepository;
    private final RestaurantRepositoryPort restaurantRepository;

    public GetPaymentByIdUseCaseImpl(
            PaymentRepositoryPort paymentRepository,
            OrderRepositoryPort orderRepository,
            RestaurantRepositoryPort restaurantRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public PaymentView execute(String orderId, String paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        if (!payment.getOrderId().equals(orderId)) {
            throw new PaymentOrderMismatchException("Payment does not belong to this order");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!canAccess(order)) {
            throw new OrderAccessDeniedException();
        }

        return new PaymentView(
                payment.getId(),
                payment.getOrderId(),
                payment.getCreatedAt(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getTransactionId(),
                payment.getProvider(),
                payment.getPaidAt(),
                payment.getFailedAt(),
                payment.getRefundedAt()
        );
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
}