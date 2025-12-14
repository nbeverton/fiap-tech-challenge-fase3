package br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.in;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Restaurant;

import java.util.List;

public interface RestaurantUseCase {

    Restaurant create(Restaurant restaurant);
    Restaurant update(String id, Restaurant restaurant);
    void delete(String id);
    Restaurant findById(String id);
    List<Restaurant> findAll();

}
