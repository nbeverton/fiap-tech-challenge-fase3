package br.com.fiap.techchallenge.core.usecase.in.menu;

import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

import br.com.fiap.techchallenge.core.domain.model.Menu;

public interface CreateMenuUseCase {
    Menu execute(String restaurantId, Menu menu);
}
