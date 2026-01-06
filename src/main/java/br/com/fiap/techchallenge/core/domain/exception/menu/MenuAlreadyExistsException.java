package br.com.fiap.techchallenge.core.domain.exception.menu;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class MenuAlreadyExistsException extends BusinessException {

    public MenuAlreadyExistsException(String menuId) {
        super("Menu already exists with id: " + menuId);
    }

}
