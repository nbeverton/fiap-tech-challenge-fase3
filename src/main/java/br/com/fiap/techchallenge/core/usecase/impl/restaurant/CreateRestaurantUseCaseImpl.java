package br.com.fiap.techchallenge.core.usecase.impl.restaurant;

import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.CreateRestaurantUseCase;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.domain.exception.NotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.BusinessException;

public class CreateRestaurantUseCaseImpl implements CreateRestaurantUseCase {

    private final RestaurantRepositoryPort restaurantRepository;
    private final AddressRepositoryPort addressRepository;

    public CreateRestaurantUseCaseImpl(
            RestaurantRepositoryPort restaurantRepository,
            AddressRepositoryPort addressRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public Restaurant execute(Restaurant restaurant) {

        Address address = addressRepository.findById(restaurant.getAddressId())
                .orElseThrow(() ->
                        new NotFoundException("Endereço não encontrado")
                );

        if (!address.getUserId().equals(restaurant.getUserId())) {
            throw new BusinessException(
                    "O endereço informado não pertence ao usuário"
            );
        }

        return restaurantRepository.save(restaurant);
    }
}

