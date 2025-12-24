package br.com.fiap.techchallenge.core.usecase.impl.useraddress;

import br.com.fiap.techchallenge.core.usecase.in.useraddress.DeleteUserAddressUseCase;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;

public class DeleteUserAddressUseCaseImpl implements DeleteUserAddressUseCase {

    private final UserAddressRepositoryPort userAddressRepository;

    public DeleteUserAddressUseCaseImpl(UserAddressRepositoryPort userAddressRepository) {
        this.userAddressRepository = userAddressRepository;
    }

    @Override
    public void execute(String id) {

        userAddressRepository.deleteById(id);
    }
}
