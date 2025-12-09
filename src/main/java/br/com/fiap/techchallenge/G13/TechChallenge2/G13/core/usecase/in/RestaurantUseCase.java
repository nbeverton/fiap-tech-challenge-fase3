package br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.in;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Restaurant;

public interface RestaurantUseCase {
    Restaurant create(Restaurant restaurant);
    Restaurant update(Long id, Restaurant restaurant);
    void delete(Long id);
    Restaurant findById(Long id);
    List<Restaurant> findAll();
}
