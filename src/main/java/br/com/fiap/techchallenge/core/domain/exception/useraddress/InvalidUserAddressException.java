package br.com.fiap.techchallenge.core.domain.exception.useraddress;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class InvalidUserAddressException extends BusinessException {

    public InvalidUserAddressException(String message) {
        super(message);
    }

}
