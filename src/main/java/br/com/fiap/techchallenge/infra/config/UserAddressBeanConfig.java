package br.com.fiap.techchallenge.infra.config;

import br.com.fiap.techchallenge.core.usecase.impl.useraddress.*;
import br.com.fiap.techchallenge.core.usecase.in.address.FindAddressByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.user.FindUserByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.useraddress.*;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserAddressBeanConfig {

    @Bean
    public CreateUserAddressUseCase createUserAddressUseCase(
            UserAddressRepositoryPort repository,
            FindUserByIdUseCase findUserByIdUseCase,
            FindAddressByIdUseCase findAddressByIdUseCase
    ){

        return new CreateUserAddressUseCaseImpl(
                repository,
                findUserByIdUseCase,
                findAddressByIdUseCase
        );
    }

    @Bean
    public AddAddressToUserUseCase addAddressToUserUseCase(
            FindUserByIdUseCase findUserByIdUseCase,
            AddressRepositoryPort addressRepository,
            UserAddressRepositoryPort userAddressRepository
    ){

        return new AddAddressToUserUseCaseImpl(
                findUserByIdUseCase,
                addressRepository,
                userAddressRepository
        );
    }


    @Bean
    public DeleteUserAddressUseCase deleteUserAddressUseCase(
            UserAddressRepositoryPort userAddressRepository,
            AddressRepositoryPort addressRepository
    ){

        return new DeleteUserAddressUseCaseImpl(
                userAddressRepository,
                addressRepository
        );
    }


    @Bean
    public FindUserAddressByAddressIdUseCase findUserAddressByAddressIdUseCase(UserAddressRepositoryPort repository){

        return new FindUserAddressByAddressIdUseCaseImpl(repository);
    }


    @Bean
    public GetUserAddressesSummaryUseCase getUserAddressesSummaryUseCase(
            FindUserByIdUseCase findUserByIdUseCase,
            UserAddressRepositoryPort userAddressRepositoryPort,
            AddressRepositoryPort addressRepositoryPort
    ){

        return new GetUserAddressesSummaryUseCaseImpl(
                findUserByIdUseCase,
                userAddressRepositoryPort,
                addressRepositoryPort
        );
    }


    @Bean
    public UpdateUserAddressUseCase updateUserAddressUseCase(UserAddressRepositoryPort repository){

        return new UpdateUserAddressUseCaseImpl(repository);
    }


    @Bean
    public UpdateUserAddressForUserUseCase updateUserAddressForUserUseCase(
            UserAddressRepositoryPort userAddressRepositoryPort,
            AddressRepositoryPort addressRepositoryPort
    ){
        return new UpdateUserAddressForUserUseCaseImpl(
                userAddressRepositoryPort,
                addressRepositoryPort
        );
    }
}
