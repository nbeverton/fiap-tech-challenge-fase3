package br.com.fiap.techchallenge.core.usecase.impl.menu;

import br.com.fiap.techchallenge.core.domain.exception.menu.MenuNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Menu;
import br.com.fiap.techchallenge.core.usecase.in.menu.UpdateMenuUseCase;
import br.com.fiap.techchallenge.core.usecase.out.MenuRepositoryPort;

public class UpdateMenuUseCaseImpl implements UpdateMenuUseCase {

    private final MenuRepositoryPort repository;

    public UpdateMenuUseCaseImpl(MenuRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Menu execute(String restaurantId, String menuId, Menu menu) {

        Menu existing = repository.findById(restaurantId, menuId)
                .orElseThrow(() -> new MenuNotFoundException(menuId));

        Menu toSave = Menu.restore(
                existing.getId(),           // garante que o ID original é preservado
                menu.getName(),
                menu.getDescription(),
                menu.getPrice(),
                menu.isDineInAvailable(),
                menu.getImageUrl()
        );

        return repository.save(restaurantId, toSave);
    }
}
