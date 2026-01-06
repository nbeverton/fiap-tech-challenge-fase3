package br.com.fiap.techchallenge.core.domain.exception.menu;

import br.com.fiap.techchallenge.core.domain.exception.NotFoundException;

public class MenuNotFoundException extends NotFoundException {

    public MenuNotFoundException(String menuId) {
        super("Menu not found with id: " + menuId);
    }

}
