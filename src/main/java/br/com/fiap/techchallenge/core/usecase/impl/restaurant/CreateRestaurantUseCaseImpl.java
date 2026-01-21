package br.com.fiap.techchallenge.core.usecase.impl.restaurant;

import br.com.fiap.techchallenge.core.domain.exception.BusinessException;
import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.restaurant.RestaurantAlreadyExistsException;
import br.com.fiap.techchallenge.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.CreateRestaurantUseCase;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;

public class CreateRestaurantUseCaseImpl implements CreateRestaurantUseCase {

    private final RestaurantRepositoryPort restaurantRepository;
    private final UserRepositoryPort userRepository;
    private final AddressRepositoryPort addressRepository;

    public CreateRestaurantUseCaseImpl(
            RestaurantRepositoryPort restaurantRepository,
            UserRepositoryPort userRepository,
            AddressRepositoryPort addressRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public Restaurant execute(Restaurant restaurant) {

        // 0) Restaurant name must be unique
        restaurantRepository.findByName(restaurant.getName())
                .ifPresent(existing -> {
                    throw new RestaurantAlreadyExistsException(restaurant.getName());
                });

        // 1) Ensure that the address exists
        addressRepository.findById(restaurant.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException(restaurant.getAddressId()));

        // 2) Ensure that the user exists
        User owner = userRepository.findById(restaurant.getUserId())
                .orElseThrow(() -> new UserNotFoundException(restaurant.getUserId()));

        // 3) Ensure that the user has OWNER role
        if (!owner.isOwner()) {
            throw new BusinessException("Only OWNER-type users can create restaurants");
        }

        // 4) Ensure that the address is not linked to another restaurant
        restaurantRepository.findByAddressId(restaurant.getAddressId())
                .ifPresent(r -> {
                    throw new BusinessException("This address is already linked to another restaurant");
                });

        // 5) Persist the restaurant
        return restaurantRepository.save(restaurant);
    }

}
