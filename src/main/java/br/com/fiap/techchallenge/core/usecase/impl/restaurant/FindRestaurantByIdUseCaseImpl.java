package br.com.fiap.techchallenge.core.usecase.impl.restaurant;

import br.com.fiap.techchallenge.core.domain.exception.restaurant.RestaurantNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.FindRestaurantByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

public class FindRestaurantByIdUseCaseImpl implements FindRestaurantByIdUseCase {

    private final RestaurantRepositoryPort repository;

    public FindRestaurantByIdUseCaseImpl(RestaurantRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Restaurant execute(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
    }
}
