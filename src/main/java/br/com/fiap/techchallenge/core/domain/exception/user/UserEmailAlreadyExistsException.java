package br.com.fiap.techchallenge.core.domain.exception.user;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class UserEmailAlreadyExistsException   extends BusinessException {
    public UserEmailAlreadyExistsException(String email) {
        super("User with email '" + email + "' already exists.");
    }
}
