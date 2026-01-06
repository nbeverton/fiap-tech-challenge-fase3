package br.com.fiap.techchallenge.core.domain.exception.user;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class InvalidUserException extends BusinessException {

    public InvalidUserException(String message){
        super(message);
    }

}
