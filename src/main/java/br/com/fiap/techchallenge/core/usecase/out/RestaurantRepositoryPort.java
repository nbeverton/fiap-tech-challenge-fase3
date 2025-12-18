package br.com.fiap.techchallenge.core.usecase.out;

import br.com.fiap.techchallenge.core.domain.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepositoryPort {

    Restaurant save(Restaurant restaurant);
    Optional<Restaurant> findById(String id);
    List<Restaurant> findAll();
    void delete(String id);

}