package br.com.fiap.techchallenge.core.usecase.impl.restaurant;

import br.com.fiap.techchallenge.core.domain.enums.UserType;
import br.com.fiap.techchallenge.core.domain.exception.BusinessException;
import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.order.OrderUserMismatchException;
import br.com.fiap.techchallenge.core.domain.exception.restaurant.RestaurantAlreadyExistsException;
import br.com.fiap.techchallenge.core.domain.exception.restaurant.RestaurantNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.domain.security.ForbiddenException;
import br.com.fiap.techchallenge.core.domain.security.UnauthorizedException;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.UpdateRestaurantUseCase;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

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

        // 1) Ensure that the restaurant exists
        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));

        // 1.1) Garantir que o restaurant pertence ao usuário autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        String requesterUserId = auth.getPrincipal().toString();

        boolean isAdmin = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN"));

        boolean isOwnerOfRestaurant = existing.getUserId() != null && existing.getUserId().equals(requesterUserId);

        if (!isAdmin && !isOwnerOfRestaurant) {
            throw new ForbiddenException("Forbidden: you cannot update another user's restaurant");
        }

        // 2) Restaurant name must be unique (ignoring the current restaurant)
        restaurantRepository.findByName(input.getName())
                .filter(r -> !r.getId().equals(existing.getId()))
                .ifPresent(r -> {
                    throw new RestaurantAlreadyExistsException(input.getName());
                });

        // 3) Ensure that the address exists
        addressRepository.findById(input.getAddressId())
                .orElseThrow(() -> new AddressNotFoundException(input.getAddressId()));

        // 4) Ensure that the owner exists and has OWNER role
        User owner = userRepository.findById(input.getUserId())
                .orElseThrow(() -> new UserNotFoundException(input.getUserId()));

        if (owner.getUserType() != UserType.OWNER) {
            throw new BusinessException("Only OWNER-type users can own restaurants");
        }

        // 5) Ensure that the address is not linked to another restaurant
        restaurantRepository.findByAddressId(input.getAddressId())
                .filter(r -> !r.getId().equals(existing.getId())) // ignora o próprio
                .ifPresent(r -> {
                    throw new BusinessException("This address is already linked to another restaurant");
                });

        // 6) Build the updated aggregate
        Restaurant toSave = new Restaurant(
                existing.getId(),             // mantém o id original
                input.getName(),
                input.getAddressId(),
                input.getCuisineType(),
                input.getOpeningHours(),
                existing.getUserId(),
                input.getMenu()                 // ou existing.getMenu(), dependendo da regra de negócio
        );

        return restaurantRepository.save(toSave);
    }
}
