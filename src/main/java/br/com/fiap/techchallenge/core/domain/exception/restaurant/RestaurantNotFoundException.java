package br.com.fiap.techchallenge.core.domain.exception.restaurant;

import br.com.fiap.techchallenge.core.domain.exception.NotFoundException;

public class RestaurantNotFoundException extends NotFoundException {

    public RestaurantNotFoundException(String id){
        super("Restaurant not found with id: " + id);
    }

}
