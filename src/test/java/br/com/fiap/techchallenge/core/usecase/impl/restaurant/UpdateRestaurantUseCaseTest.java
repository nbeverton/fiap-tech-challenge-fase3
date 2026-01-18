package br.com.fiap.techchallenge.core.usecase.impl.restaurant;

import br.com.fiap.techchallenge.core.domain.enums.CuisineType;
import br.com.fiap.techchallenge.core.domain.exception.BusinessException;
import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.restaurant.RestaurantAlreadyExistsException;
import br.com.fiap.techchallenge.core.domain.exception.restaurant.RestaurantNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.domain.model.Client;
import br.com.fiap.techchallenge.core.domain.model.Owner;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
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

class UpdateRestaurantUseCaseTest {

    @Mock
    private RestaurantRepositoryPort restaurantRepository;

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private AddressRepositoryPort addressRepository;

    @InjectMocks
    private UpdateRestaurantUseCaseImpl updateRestaurantUseCase;

    private Restaurant existingRestaurant;
    private Restaurant updatedRestaurant;
    private Owner owner;
    private Address address;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        OpeningHours openingHours = new OpeningHours("10:00", "22:00");

        existingRestaurant = new Restaurant("1",
                "Old Name",
                "address123",
                CuisineType.BRAZILIAN,
                openingHours,
                "user123",
                Collections.emptyList());

        updatedRestaurant = new Restaurant("1",
                "New Name",
                "address456",
                CuisineType.JAPANESE,
                openingHours,
                "user123",
                Collections.emptyList());

        owner = new Owner("user123",
                "Owner Name",
                "owner@email.com",
                "owner",
                "password");

        address = new Address("address456",
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
    void shouldUpdateRestaurantSuccessfully() {
        when(restaurantRepository.findById("1")).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.findByName("New Name")).thenReturn(Optional.empty());
        when(addressRepository.findById("address456")).thenReturn(Optional.of(address));
        when(userRepository.findById("user123")).thenReturn(Optional.of(owner));
        when(restaurantRepository.findByAddressId("address456")).thenReturn(Optional.empty());
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(updatedRestaurant);

        Restaurant result = updateRestaurantUseCase.execute("1", updatedRestaurant);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals(CuisineType.JAPANESE, result.getCuisineType());
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenRestaurantToUpdateNotFound() {
        when(restaurantRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class,
                () -> updateRestaurantUseCase.execute("1", updatedRestaurant));
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenNewNameIsAlreadyTaken() {
        Restaurant anotherRestaurant = new Restaurant("2",
                "New Name",
                "address789",
                CuisineType.ITALIAN,
                new OpeningHours("12:00", "23:00"),
                "user456",
                Collections.emptyList());
        when(restaurantRepository.findById("1")).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.findByName("New Name")).thenReturn(Optional.of(anotherRestaurant));

        assertThrows(RestaurantAlreadyExistsException.class,
                () -> updateRestaurantUseCase.execute("1", updatedRestaurant));
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldNotThrowExceptionWhenNewNameIsTheSame() {
        updatedRestaurant = new Restaurant("1",
                "Old Name",
                "address456",
                CuisineType.JAPANESE,
                new OpeningHours("10:00", "22:00"),
                "user123",
                Collections.emptyList());

        when(restaurantRepository.findById("1")).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.findByName("Old Name")).thenReturn(Optional.of(existingRestaurant));
        when(addressRepository.findById("address456")).thenReturn(Optional.of(address));
        when(userRepository.findById("user123")).thenReturn(Optional.of(owner));
        when(restaurantRepository.findByAddressId("address456")).thenReturn(Optional.empty());
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(updatedRestaurant);

        assertDoesNotThrow(() -> updateRestaurantUseCase.execute("1", updatedRestaurant));
    }

    @Test
    void shouldThrowExceptionWhenNewAddressNotFound() {
        when(restaurantRepository.findById("1")).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.findByName("New Name")).thenReturn(Optional.empty());
        when(addressRepository.findById("address456")).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> updateRestaurantUseCase.execute("1", updatedRestaurant));
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenOwnerNotFound() {
        when(restaurantRepository.findById("1")).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.findByName("New Name")).thenReturn(Optional.empty());
        when(addressRepository.findById("address456")).thenReturn(Optional.of(address));
        when(userRepository.findById("user123")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> updateRestaurantUseCase.execute("1", updatedRestaurant));
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        Client clientUser = new Client("user123",
                "Client Name",
                "client@email.com",
                "client",
                "password");

        when(restaurantRepository.findById("1")).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.findByName("New Name")).thenReturn(Optional.empty());
        when(addressRepository.findById("address456")).thenReturn(Optional.of(address));
        when(userRepository.findById("user123")).thenReturn(Optional.of(clientUser));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> updateRestaurantUseCase.execute("1", updatedRestaurant));
        assertEquals("Only OWNER-type users can own restaurants", exception.getMessage());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenNewAddressIsAlreadyInUse() {
        Restaurant anotherRestaurant = new Restaurant("2",
                "Another Name",
                "address456",
                CuisineType.ITALIAN,
                new OpeningHours("12:00", "23:00"),
                "user456",
                Collections.emptyList());

        when(restaurantRepository.findById("1")).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.findByName("New Name")).thenReturn(Optional.empty());
        when(addressRepository.findById("address456")).thenReturn(Optional.of(address));
        when(userRepository.findById("user123")).thenReturn(Optional.of(owner));
        when(restaurantRepository.findByAddressId("address456")).thenReturn(Optional.of(anotherRestaurant));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> updateRestaurantUseCase.execute("1", updatedRestaurant));
        assertEquals("This address is already linked to another restaurant", exception.getMessage());
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }
}
