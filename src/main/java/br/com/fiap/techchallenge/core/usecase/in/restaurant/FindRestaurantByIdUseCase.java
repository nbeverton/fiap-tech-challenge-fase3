package br.com.fiap.techchallenge.core.usecase.in.restaurant;

import br.com.fiap.techchallenge.core.domain.model.Restaurant;

public interface FindRestaurantByIdUseCase {

    Restaurant execute(String id);
}
