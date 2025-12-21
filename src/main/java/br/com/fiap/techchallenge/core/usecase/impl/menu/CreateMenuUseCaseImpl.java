package br.com.fiap.techchallenge.core.usecase.impl.menu;

import br.com.fiap.techchallenge.core.domain.model.Menu;
import br.com.fiap.techchallenge.core.usecase.in.menu.CreateMenuUseCase;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.MenuRepositoryPort;


public class CreateMenuUseCaseImpl implements CreateMenuUseCase {

    private final MenuRepositoryPort repository;

    public CreateMenuUseCaseImpl(MenuRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Menu execute(String restaurantId, Menu menu) {
        return repository.save(restaurantId, menu);
    }
}
