package br.com.fiap.techchallenge.core.usecase.in.impl;

import br.com.fiap.techchallenge.core.domain.exception.NotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.usecase.in.RestaurantUseCase;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

import java.util.List;

public class RestaurantUseCaseImpl implements RestaurantUseCase {

    private final RestaurantRepositoryPort restaurantRepositoryPort;

    public RestaurantUseCaseImpl(RestaurantRepositoryPort restaurantRepositoryPort) {
        this.restaurantRepositoryPort = restaurantRepositoryPort;
    }

    public Restaurant create(Restaurant restaurant) {
        return restaurantRepositoryPort.save(restaurant);
    }

    public Restaurant update(String id, Restaurant restaurant) {
        // ensure the id matches and restaurant exists
        Restaurant existing = restaurantRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurante não encontrado"));
        // create a new Restaurant preserving id
        Restaurant toSave = new Restaurant(
                existing.getId(),
                restaurant.getName(),
                restaurant.getAddressId(),
                restaurant.getCuisineType(),
                restaurant.getOpeningHours(),
                restaurant.getUserId(),
                restaurant.getMenu()
        );
        return restaurantRepositoryPort.save(toSave);
    }

    public void delete(String id) {
        restaurantRepositoryPort.delete(id);
    }

    public Restaurant findById(String id) {
        return restaurantRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurante não encontrado"));
    }

    public List<Restaurant> findAll() {
        return restaurantRepositoryPort.findAll();
    }
}
