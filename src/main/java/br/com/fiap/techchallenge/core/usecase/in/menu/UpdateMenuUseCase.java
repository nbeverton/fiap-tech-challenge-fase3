package br.com.fiap.techchallenge.core.usecase.in.menu;

import br.com.fiap.techchallenge.core.domain.model.Menu;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;


public interface UpdateMenuUseCase {
    Menu execute(String restaurantId, String menuId, Menu menu);
}
