package br.com.fiap.techchallenge.core.domain.exception.address;

public class AddressNotFoundException extends RuntimeException{

    public AddressNotFoundException(String id) {

        super("Address not found with id: " + id);
    }
}
