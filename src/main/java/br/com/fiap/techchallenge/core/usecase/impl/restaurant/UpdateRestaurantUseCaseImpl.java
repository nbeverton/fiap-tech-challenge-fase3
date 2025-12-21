package br.com.fiap.techchallenge.core.usecase.impl.restaurant;

import br.com.fiap.techchallenge.core.domain.exception.NotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.UpdateRestaurantUseCase;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

public class UpdateRestaurantUseCaseImpl implements UpdateRestaurantUseCase {

    private final RestaurantRepositoryPort repository;

    public UpdateRestaurantUseCaseImpl(RestaurantRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Restaurant execute(String id, Restaurant restaurant) {

        Restaurant existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurante não encontrado"));

        Restaurant toSave = new Restaurant(
                existing.getId(),
                restaurant.getName(),
                restaurant.getAddressId(),
                restaurant.getCuisineType(),
                restaurant.getOpeningHours(),
                restaurant.getUserId(),
                restaurant.getMenu()
        );

        return repository.save(toSave);
    }
}
