package br.com.fiap.techchallenge.core.usecase.in.restaurant;

import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import java.util.List;

public interface ListRestaurantsUseCase {
    List<Restaurant> execute();
}
