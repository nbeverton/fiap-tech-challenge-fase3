package br.com.fiap.techchallenge.core.usecase.impl.order;

import br.com.fiap.techchallenge.core.domain.exception.security.ForbiddenException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.security.AuthContext;
import br.com.fiap.techchallenge.core.usecase.in.order.ListOrdersUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ListOrdersUseCaseImpl implements ListOrdersUseCase {

    private final OrderRepositoryPort orderRepository;
    private final RestaurantRepositoryPort restaurantRepository;

    public ListOrdersUseCaseImpl(OrderRepositoryPort orderRepository, RestaurantRepositoryPort restaurantRepository) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<Order> execute() {

        List<Order> all = orderRepository.findAll();

        if (AuthContext.isAdmin()) {
            return all;
        }

        String requesterId = AuthContext.userId();

        if (AuthContext.isClient()) {
            return all.stream()
                    .filter(o -> requesterId.equals(o.getUserId()))
                    .toList();
        }

        if (AuthContext.isOwner()) {
            List<Restaurant> myRestaurants = restaurantRepository.findByUserId(requesterId);
            Set<String> myRestaurantIds = myRestaurants.stream()
                    .map(Restaurant::getId)
                    .collect(Collectors.toSet());

            return all.stream()
                    .filter(o -> myRestaurantIds.contains(o.getRestaurantId()))
                    .toList();
        }

        throw new ForbiddenException("Forbidden");
    }
}
