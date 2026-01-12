package br.com.fiap.techchallenge.core.domain.exception.restaurant;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class RestaurantAlreadyExistsException extends BusinessException {

    public RestaurantAlreadyExistsException(String name) {
        super("Restaurant already exists with name: " + name);
    }

}
