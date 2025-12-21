package br.com.fiap.techchallenge.core.usecase.in.menu;

import br.com.fiap.techchallenge.core.domain.model.Menu;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;


import java.util.List;

public interface ListMenusByRestaurantUseCase {
    List<Menu> execute(String restaurantId);
}
