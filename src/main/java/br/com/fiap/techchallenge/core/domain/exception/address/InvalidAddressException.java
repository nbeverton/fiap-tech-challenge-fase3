package br.com.fiap.techchallenge.core.domain.exception.address;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class InvalidAddressException extends BusinessException {

    public InvalidAddressException(String message) {
        super(message);
    }

}
