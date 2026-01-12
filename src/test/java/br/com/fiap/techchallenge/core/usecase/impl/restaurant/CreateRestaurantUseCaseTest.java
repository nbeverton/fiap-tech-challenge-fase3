package br.com.fiap.techchallenge.core.usecase.impl.restaurant;

import br.com.fiap.techchallenge.core.domain.enums.CuisineType;
import br.com.fiap.techchallenge.core.domain.exception.BusinessException;
import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.restaurant.RestaurantAlreadyExistsException;
import br.com.fiap.techchallenge.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.*;
import br.com.fiap.techchallenge.core.domain.valueobjects.OpeningHours;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateRestaurantUseCaseTest {

    @Mock
    private RestaurantRepositoryPort restaurantRepository;

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private AddressRepositoryPort addressRepository;

    @InjectMocks
    private CreateRestaurantUseCaseImpl createRestaurantUseCase;

    private Restaurant restaurant;
    private Owner owner;
    private Address address;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        OpeningHours openingHours = new OpeningHours("10:00", "22:00");
        restaurant = new Restaurant("1",
                "Restaurant Name",
                "address123", CuisineType.BRAZILIAN,
                openingHours,
                "user123",
                Collections.emptyList());

        owner = new Owner("user123",
                "Owner Name",
                "owner@email.com",
                "owner",
                "password");
        address = new Address("address123",
                "01001000",
                "Praça da Sé",
                1,
                "lado ímpar",
                "Sé",
                "São Paulo",
                "SP",
                "Brasil");
    }

    @Test
    void shouldCreateRestaurantSuccessfully() {
        when(restaurantRepository.findByName(restaurant.getName())).thenReturn(Optional.empty());
        when(addressRepository.findById(restaurant.getAddressId())).thenReturn(Optional.of(address));
        when(userRepository.findById(restaurant.getUserId())).thenReturn(Optional.of(owner));
        when(restaurantRepository.findByAddressId(restaurant.getAddressId())).thenReturn(Optional.empty());
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        Restaurant createdRestaurant = createRestaurantUseCase.execute(restaurant);

        assertNotNull(createdRestaurant);
        assertEquals(restaurant.getName(), createdRestaurant.getName());
        verify(restaurantRepository, times(1)).save(restaurant);
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNameAlreadyExists() {
        when(restaurantRepository.findByName(restaurant.getName())).thenReturn(Optional.of(restaurant));

        assertThrows(RestaurantAlreadyExistsException.class, () -> createRestaurantUseCase.execute(restaurant));
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFound() {
        when(restaurantRepository.findByName(restaurant.getName())).thenReturn(Optional.empty());
        when(addressRepository.findById(restaurant.getAddressId())).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> createRestaurantUseCase.execute(restaurant));
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(restaurantRepository.findByName(restaurant.getName())).thenReturn(Optional.empty());
        when(addressRepository.findById(restaurant.getAddressId())).thenReturn(Optional.of(address));
        when(userRepository.findById(restaurant.getUserId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> createRestaurantUseCase.execute(restaurant));
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        User clientUser = new Client("user123",
                "Client Name",
                "client@email.com",
                "client",
                "password");
        when(restaurantRepository.findByName(restaurant.getName())).thenReturn(Optional.empty());
        when(addressRepository.findById(restaurant.getAddressId())).thenReturn(Optional.of(address));
        when(userRepository.findById(restaurant.getUserId())).thenReturn(Optional.of(clientUser));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> createRestaurantUseCase.execute(restaurant));
        assertEquals("Only OWNER-type users can create restaurants", exception.getMessage());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenAddressIsAlreadyInUse() {
        when(restaurantRepository.findByName(restaurant.getName())).thenReturn(Optional.empty());
        when(addressRepository.findById(restaurant.getAddressId())).thenReturn(Optional.of(address));
        when(userRepository.findById(restaurant.getUserId())).thenReturn(Optional.of(owner));
        when(restaurantRepository.findByAddressId(restaurant.getAddressId())).thenReturn(Optional.of(restaurant));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> createRestaurantUseCase.execute(restaurant));
        assertEquals("This address is already linked to another restaurant", exception.getMessage());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }
}
