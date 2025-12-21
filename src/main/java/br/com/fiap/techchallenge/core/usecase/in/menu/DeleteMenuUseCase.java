package br.com.fiap.techchallenge.core.usecase.in.menu;

import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;

public interface DeleteMenuUseCase {
    void execute(String restaurantId, String menuId);
}
