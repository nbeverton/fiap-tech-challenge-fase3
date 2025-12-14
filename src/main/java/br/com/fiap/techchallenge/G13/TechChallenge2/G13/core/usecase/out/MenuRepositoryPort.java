package br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.out;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuRepositoryPort {

    Menu save(String restaurantId, Menu menu);
    void deleteById(String restaurantId, String menuId);
    Optional<Menu> findById(String restaurantId, String menuId);
    List<Menu> findByRestaurantId(String restaurantId);

}
