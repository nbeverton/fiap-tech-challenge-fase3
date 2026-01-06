package br.com.fiap.techchallenge.core.usecase.impl.menu;

import br.com.fiap.techchallenge.core.domain.exception.menu.MenuNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Menu;
import br.com.fiap.techchallenge.core.usecase.in.menu.FindMenuByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.out.MenuRepositoryPort;

public class FindMenuByIdUseCaseImpl implements FindMenuByIdUseCase {

    private final MenuRepositoryPort repository;

    public FindMenuByIdUseCaseImpl(MenuRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Menu execute(String restaurantId, String menuId) {
        return repository.findById(restaurantId, menuId)
                .orElseThrow(() -> new MenuNotFoundException(menuId));
    }
}
