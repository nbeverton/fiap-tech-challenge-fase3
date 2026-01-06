package br.com.fiap.techchallenge.core.usecase.impl.menu;

import br.com.fiap.techchallenge.core.domain.exception.menu.MenuNotFoundException;
import br.com.fiap.techchallenge.core.usecase.in.menu.DeleteMenuUseCase;
import br.com.fiap.techchallenge.core.usecase.out.MenuRepositoryPort;

public class DeleteMenuUseCaseImpl implements DeleteMenuUseCase {

    private final MenuRepositoryPort repository;

    public DeleteMenuUseCaseImpl(MenuRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String restaurantId, String menuId) {

        repository.findById(restaurantId, menuId)
                .orElseThrow(() -> new MenuNotFoundException(menuId));

        repository.deleteById(restaurantId, menuId);
    }
}
