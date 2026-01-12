package br.com.fiap.techchallenge.core.domain.exception.user;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class UserAlreadyExistsException extends BusinessException {

    public UserAlreadyExistsException(String login) {
        super("User already exists with login: " + login);
    }

}
