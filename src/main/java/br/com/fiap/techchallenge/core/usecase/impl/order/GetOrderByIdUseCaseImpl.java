package br.com.fiap.techchallenge.core.usecase.impl.order;

import br.com.fiap.techchallenge.core.domain.exception.order.OrderAccessDeniedException;
import br.com.fiap.techchallenge.core.domain.exception.order.OrderNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.security.AuthContext;
import br.com.fiap.techchallenge.core.usecase.in.order.GetOrderByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GetOrderByIdUseCaseImpl implements GetOrderByIdUseCase {

    private final OrderRepositoryPort orderRepository;
    private final RestaurantRepositoryPort restaurantRepository;

    public GetOrderByIdUseCaseImpl(
            OrderRepositoryPort orderRepository,
            RestaurantRepositoryPort restaurantRepository
    ) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Order execute(String orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (AuthContext.isAdmin()) {
            return order;
        }

        String requesterId = AuthContext.userId();

        if (AuthContext.isClient() && requesterId.equals(order.getUserId())) {
            return order;
        }

        if (AuthContext.isOwner()) {
            List<Restaurant> myRestaurants = restaurantRepository.findByUserId(requesterId);

            Set<String> myRestaurantIds = myRestaurants.stream()
                    .map(Restaurant::getId)
                    .collect(Collectors.toSet());

            if (myRestaurantIds.contains(order.getRestaurantId())) {
                return order;
            }
        }

        throw new OrderAccessDeniedException();
    }
}