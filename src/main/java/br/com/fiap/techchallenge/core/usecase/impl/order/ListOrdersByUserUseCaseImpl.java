package br.com.fiap.techchallenge.core.usecase.impl.order;

import br.com.fiap.techchallenge.core.domain.exception.security.UnauthorizedException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.usecase.in.order.ListOrdersByUserUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class ListOrdersByUserUseCaseImpl implements ListOrdersByUserUseCase {

    private final OrderRepositoryPort orderRepository;
    private final RestaurantRepositoryPort restaurantRepository;

    public ListOrdersByUserUseCaseImpl(OrderRepositoryPort orderRepository, RestaurantRepositoryPort restaurantRepository) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<Order> execute(Authentication authentication) {

        String userId = authentication.getPrincipal().toString();

        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();


        if (authorities.contains("ROLE_OWNER")) {

            List<Restaurant> restaurants = restaurantRepository.findByUserId(userId);

            List<Order> ordersByOwner = new ArrayList<>();

            for(Restaurant r : restaurants){

                ordersByOwner.addAll(orderRepository.findByRestaurantId(r.getId()));
            }

            return ordersByOwner;
        }

        if(authorities.contains("ROLE_CLIENT")){

            return orderRepository.findByUserId(userId);
        }

        throw new UnauthorizedException(
                "Access Denied: This endpoint is restricted to Customers (viewing their own orders)" +
                        " and Restaurant Owners (managing their restaurant's orders).");
    }
}