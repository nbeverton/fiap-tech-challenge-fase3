package br.com.fiap.techchallenge.core.usecase.in.restaurant;

import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

public interface DeleteRestaurantUseCase {
    void execute(String id);
}
