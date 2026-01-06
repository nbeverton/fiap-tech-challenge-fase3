package br.com.fiap.techchallenge.core.domain.exception.address;

import br.com.fiap.techchallenge.core.domain.exception.NotFoundException;

public class AddressNotFoundException extends NotFoundException {

    public AddressNotFoundException(String id) {

        super("Address not found with id: " + id);
    }
}
