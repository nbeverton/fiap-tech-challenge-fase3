package br.com.fiap.techchallenge.core.domain.exception.restaurant;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class InvalidRestaurantException extends BusinessException {

    public InvalidRestaurantException(String message){
        super(message);
    }

}
