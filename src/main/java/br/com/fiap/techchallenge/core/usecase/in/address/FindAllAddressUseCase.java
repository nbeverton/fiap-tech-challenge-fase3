package br.com.fiap.techchallenge.core.usecase.in.address;

import br.com.fiap.techchallenge.core.domain.model.Address;

import java.util.List;

public interface FindAllAddressUseCase {

    List<Address> execute();

}
