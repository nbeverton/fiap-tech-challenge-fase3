package br.com.fiap.techchallenge.core.usecase.impl.user;

import br.com.fiap.techchallenge.core.domain.exception.user.UserHasRestaurantException;
import br.com.fiap.techchallenge.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.in.user.DeleteUserUseCase;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;

import java.util.List;

public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

    private final UserRepositoryPort userRepository;
    private final UserAddressRepositoryPort userAddressRepository;
    private final RestaurantRepositoryPort restaurantRepository;

    public DeleteUserUseCaseImpl(
            UserRepositoryPort userRepository,
            UserAddressRepositoryPort userAddressRepository,
            RestaurantRepositoryPort restaurantRepository) {
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public void execute(String id) {

        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (user.isOwner() && restaurantRepository.existsByUserId(id)) {
            throw new UserHasRestaurantException(id);
        }

        userAddressRepository.findByUserId(id)
                .forEach(link -> userAddressRepository.deleteById(link.getId()));

        userRepository.deleteById(id);
    }
}
