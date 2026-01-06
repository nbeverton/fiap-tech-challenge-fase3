package br.com.fiap.techchallenge.core.domain.exception.menu;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class InvalidMenuException extends BusinessException {

    public InvalidMenuException(String message) {
        super(message);
    }


}
