package br.com.fiap.techchallenge.core.usecase.impl.restaurant;

import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.CreateRestaurantUseCase;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

public class CreateRestaurantUseCaseImpl implements CreateRestaurantUseCase {

    private final RestaurantRepositoryPort repository;

    public CreateRestaurantUseCaseImpl(RestaurantRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Restaurant execute(Restaurant restaurant) {
        return repository.save(restaurant);
    }
}

