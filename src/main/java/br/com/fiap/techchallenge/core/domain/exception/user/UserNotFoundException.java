package br.com.fiap.techchallenge.core.domain.exception.user;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String id) {

        super("User not found with id " + id);
    }
}
