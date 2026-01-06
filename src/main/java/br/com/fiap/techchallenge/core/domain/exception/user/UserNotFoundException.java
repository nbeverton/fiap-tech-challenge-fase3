package br.com.fiap.techchallenge.core.domain.exception.user;

import br.com.fiap.techchallenge.core.domain.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String id) {

        super("User not found with id: " + id);
    }
}
