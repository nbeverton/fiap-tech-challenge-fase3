package br.com.fiap.techchallenge.core.usecase.impl.address;

import br.com.fiap.techchallenge.core.usecase.in.address.DeleteAddressUseCase;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;

public class DeleteAddressUseCaseImpl implements DeleteAddressUseCase {

    private final AddressRepositoryPort addressRepository;


    public DeleteAddressUseCaseImpl(AddressRepositoryPort addressRepository){
        this.addressRepository = addressRepository;
    }


    @Override
    public void execute(String id) {
        addressRepository.delete(id);
    }
}
