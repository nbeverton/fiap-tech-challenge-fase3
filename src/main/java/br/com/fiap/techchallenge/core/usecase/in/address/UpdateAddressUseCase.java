package br.com.fiap.techchallenge.core.usecase.in.address;

import br.com.fiap.techchallenge.core.domain.model.Address;

public interface UpdateAddressUseCase {

    Address execute(String id, Address address);

}
