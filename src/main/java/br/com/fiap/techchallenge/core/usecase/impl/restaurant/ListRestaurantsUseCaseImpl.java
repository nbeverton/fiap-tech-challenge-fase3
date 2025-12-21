package br.com.fiap.techchallenge.core.usecase.impl.restaurant;

import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.ListRestaurantsUseCase;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

import java.util.List;

public class ListRestaurantsUseCaseImpl implements ListRestaurantsUseCase {

    private final RestaurantRepositoryPort repository;

    public ListRestaurantsUseCaseImpl(RestaurantRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<Restaurant> execute() {
        return repository.findAll();
    }
}
