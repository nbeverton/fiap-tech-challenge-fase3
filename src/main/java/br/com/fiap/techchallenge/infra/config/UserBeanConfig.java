package br.com.fiap.techchallenge.infra.config;

import br.com.fiap.techchallenge.core.usecase.impl.user.*;
import br.com.fiap.techchallenge.core.usecase.in.user.*;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.security.PasswordEncoderPort;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserBeanConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(
            UserRepositoryPort userRepository,
            AddressRepositoryPort addressRepository,
            UserAddressRepositoryPort userAddressRepository,
            PasswordEncoderPort passwordEncoder) {
        return new CreateUserUseCaseImpl(
                userRepository,
                userAddressRepository,
                addressRepository,
                passwordEncoder);
    }

    @Bean
    public FindUserByIdUseCase findUserByIdUseCase(UserRepositoryPort repository) {
        return new FindUserByIdUseCaseImpl(repository);
    }

    @Bean
    public FindAllUsersUseCase findAllUsersUseCase(UserRepositoryPort repository) {
        return new FindAllUsersUseCaseImpl(repository);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(UserRepositoryPort repository) {
        return new UpdateUserUseCaseImpl(repository);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(
            UserRepositoryPort userRepository,
            UserAddressRepositoryPort userAddressRepository,
            RestaurantRepositoryPort restaurantRepository) {
        return new DeleteUserUseCaseImpl(
                userRepository,
                userAddressRepository,
                restaurantRepository);
    }
}
