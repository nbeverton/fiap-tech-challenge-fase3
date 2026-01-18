package br.com.fiap.techchallenge.core.domain.exception.user;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class UserHasRestaurantException extends BusinessException {

    public UserHasRestaurantException(String userId) {
        super("User with id " + userId + " has associated restaurants");
    }
}



