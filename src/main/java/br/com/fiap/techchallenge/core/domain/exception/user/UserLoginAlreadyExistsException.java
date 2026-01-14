package br.com.fiap.techchallenge.core.domain.exception.user;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class UserLoginAlreadyExistsException extends BusinessException {
    public UserLoginAlreadyExistsException(String login) {
        super("User with login '" + login + "' already exists.");
    }
}
