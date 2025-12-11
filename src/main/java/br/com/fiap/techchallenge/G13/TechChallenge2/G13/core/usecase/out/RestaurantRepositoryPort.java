package br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.out;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Restaurant;

public interface RestaurantRepositoryPort {
    Restaurant save(Restaurant restaurant);
    Optional<Restaurant> findById(Long id);
    List<Restaurant> findAll();
    void delete(Long id);
}