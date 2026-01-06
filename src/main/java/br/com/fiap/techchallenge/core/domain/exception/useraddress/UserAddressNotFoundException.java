package br.com.fiap.techchallenge.core.domain.exception.useraddress;

import br.com.fiap.techchallenge.core.domain.exception.NotFoundException;

public class UserAddressNotFoundException extends NotFoundException {

    public UserAddressNotFoundException(String id) {
        super("UserAddress not found with id: " + id);
    }

}
