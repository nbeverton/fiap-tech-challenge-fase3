package br.com.fiap.techchallenge.core.usecase.in.impl;

import br.com.fiap.techchallenge.core.domain.model.Menu;
import br.com.fiap.techchallenge.core.usecase.in.MenuUseCase;
import br.com.fiap.techchallenge.core.usecase.out.MenuRepositoryPort;

import java.util.List;

public class MenuUseCaseImpl implements MenuUseCase {

    private final MenuRepositoryPort menuRepositoryPort;

    public MenuUseCaseImpl(MenuRepositoryPort menuRepositoryPort) {
        this.menuRepositoryPort = menuRepositoryPort;
    }

    public Menu create(String restaurantId, Menu menu) {
        return menuRepositoryPort.save(restaurantId, menu);
    }

    public Menu update(String restaurantId, String menuId, Menu menu) {
        // TODO: ajustar regra de update (garantir que o Menu tenha o ID correto)
        return menuRepositoryPort.save(restaurantId, menu);
    }

    public void delete(String restaurantId, String menuId) {
        menuRepositoryPort.deleteById(restaurantId, menuId);
    }

    public Menu findById(String restaurantId, String menuId) {
        // TODO: trocar RuntimeException por exception de domínio (NotFoundException)
        return menuRepositoryPort.findById(restaurantId, menuId)
                .orElseThrow(() -> new RuntimeException("Menu não encontrado"));
    }

    public List<Menu> findByRestaurantId(String restaurantId) {
        return menuRepositoryPort.findByRestaurantId(restaurantId);
    }
}