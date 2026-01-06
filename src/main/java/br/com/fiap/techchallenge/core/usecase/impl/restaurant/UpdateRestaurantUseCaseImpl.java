package br.com.fiap.techchallenge.core.usecase.impl.restaurant;

import br.com.fiap.techchallenge.core.domain.enums.UserType;
import br.com.fiap.techchallenge.core.domain.exception.BusinessException;
import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.restaurant.RestaurantNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.UpdateRestaurantUseCase;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;

public class UpdateRestaurantUseCaseImpl implements UpdateRestaurantUseCase {

    private final RestaurantRepositoryPort restaurantRepository;
    private final UserRepositoryPort userRepository;
    private final AddressRepositoryPort addressRepository;

    public UpdateRestaurantUseCaseImpl(
            RestaurantRepositoryPort restaurantRepository,
            UserRepositoryPort userRepository,
            AddressRepositoryPort addressRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }


    @Override
    public Restaurant execute(String id, Restaurant input) {

        // 1) Garante que o restaurante existe
        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));

        // 2) Garante que o endereço existe
        addressRepository.findById(input.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException(input.getAddressId()));

        // 3) Garante que o owner existe e é OWNER
        User owner = userRepository.findById(input.getUserId())
                .orElseThrow(() -> new UserNotFoundException(input.getUserId()));

        if (owner.getUserType() != UserType.OWNER) {
            throw new BusinessException("Only OWNER-type users can own restaurants");
        }

        // 4) Garante que o endereço não está vinculado a outro restaurante
        restaurantRepository.findByAddressId(input.getAddressId())
                .filter(r -> !r.getId().equals(existing.getId())) // ignora o próprio
                .ifPresent(r -> {
                    throw new BusinessException("This address is already linked to another restaurant");
                });

        // 5) Monta o agregado atualizado
        Restaurant toSave = new Restaurant(
                existing.getId(),             // mantém o id original
                input.getName(),
                input.getAddressId(),
                input.getCuisineType(),
                input.getOpeningHours(),
                input.getUserId(),
                existing.getMenu()            // ou input.getMenu(), dependendo da regra de negócio
        );

        return restaurantRepository.save(toSave);
    }
}
