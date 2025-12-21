package br.com.fiap.techchallenge.core.usecase.impl.menu;

import br.com.fiap.techchallenge.core.domain.model.Menu;
import br.com.fiap.techchallenge.core.usecase.in.menu.ListMenusByRestaurantUseCase;
import br.com.fiap.techchallenge.core.usecase.out.MenuRepositoryPort;

import java.util.List;

public class ListMenusByRestaurantUseCaseImpl implements ListMenusByRestaurantUseCase {

    private final MenuRepositoryPort repository;

    public ListMenusByRestaurantUseCaseImpl(MenuRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<Menu> execute(String restaurantId) {
        return repository.findByRestaurantId(restaurantId);
    }
}
