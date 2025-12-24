package br.com.fiap.techchallenge.infra.config;

import br.com.fiap.techchallenge.core.usecase.impl.useraddress.*;
import br.com.fiap.techchallenge.core.usecase.in.address.FindAddressByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.user.FindUserByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.useraddress.*;
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
    public DeleteUserAddressUseCase deleteUserAddressUseCase(UserAddressRepositoryPort repository){

        return new DeleteUserAddressUseCaseImpl(repository);
    }


    @Bean
    public FindUserAddressByAddressIdUseCase findUserAddressByAddressIdUseCase(UserAddressRepositoryPort repository){

        return new FindUserAddressByAddressIdUseCaseImpl(repository);
    }


    @Bean
    public FindUserAddressByUserIdUseCase findUserAddressByUserIdUseCase(UserAddressRepositoryPort repository){

        return new FindUserAddressByUserIdUseCaseImpl(repository);
    }


    @Bean
    public UpdateUserAddressUseCase updateUserAddressUseCase(UserAddressRepositoryPort repository){

        return new UpdateUserAddressUseCaseImpl(repository);
    }
}
