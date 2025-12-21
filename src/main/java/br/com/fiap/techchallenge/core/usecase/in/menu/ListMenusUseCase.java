package br.com.fiap.techchallenge.core.usecase.in.menu;

import br.com.fiap.techchallenge.core.domain.model.Menu;
import java.util.List;

public interface ListMenusUseCase {
    List<Menu> execute(String restaurantId);
}
