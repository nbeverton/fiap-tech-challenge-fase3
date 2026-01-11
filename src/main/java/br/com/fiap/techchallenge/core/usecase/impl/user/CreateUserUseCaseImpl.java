package br.com.fiap.techchallenge.core.usecase.impl.user;

import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.in.user.CreateUserInput;
import br.com.fiap.techchallenge.core.usecase.in.user.CreateUserUseCase;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;

public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final UserAddressRepositoryPort userAddressRepository;
    private final AddressRepositoryPort addressRepository;

    public CreateUserUseCaseImpl(UserRepositoryPort userRepositoryPort, UserAddressRepositoryPort userAddressRepositoryPort, AddressRepositoryPort addressRepositoryPort) {
        this.userRepository = userRepositoryPort;
        this.userAddressRepository = userAddressRepositoryPort;
        this.addressRepository = addressRepositoryPort;
    }


    @Override
    public User execute(CreateUserInput input) {

        //1. Create User
        User user = new User(
                input.name(),
                input.userType(),
                input.email(),
                input.login(),
                input.password()
        ){};

        //2. Persist the user
        User savedUser = userRepository.save(user);

        //3. Validates if the address exists
        Address address = addressRepository.findById(input.addressId())
                .orElseThrow(()->
                        new AddressNotFoundException(input.addressId())
                );

        //4. Creates the UserAddress link (first address is always main/primary)
        UserAddress userAddress = new UserAddress(
                savedUser.getId(),
                input.addressId(),
                input.addressType(),
                input.label(),
                true
        );

        //5. Persists the link
        userAddressRepository.save(userAddress);

        //6. Returns the created user
        return savedUser;
    }
}
