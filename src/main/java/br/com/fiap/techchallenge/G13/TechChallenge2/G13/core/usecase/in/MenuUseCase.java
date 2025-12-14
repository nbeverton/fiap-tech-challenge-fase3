package br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.in;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Menu;

import java.util.List;

public interface MenuUseCase {

    Menu create(String restaurantId, Menu menu);
    Menu update(String restaurantId, String menuId, Menu menu);
    void delete(String restaurantId, String menuId);
    Menu findById(String restaurantId, String menuId);

    List<Menu> findByRestaurantId(String restaurantId);

}
