package br.com.fiap.techchallenge.core.usecase.impl.restaurant;

import br.com.fiap.techchallenge.core.domain.enums.CuisineType;
import br.com.fiap.techchallenge.core.domain.exception.restaurant.RestaurantNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.valueobjects.OpeningHours;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindRestaurantByIdUseCaseTest {

    @Mock
    private RestaurantRepositoryPort restaurantRepository;

    @InjectMocks
    private FindRestaurantByIdUseCaseImpl findRestaurantByIdUseCase;

    private Restaurant existingRestaurant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        OpeningHours openingHours = new OpeningHours("10:00", "22:00");
        existingRestaurant = new Restaurant("1",
                "Restaurant Name",
                "address123",
                CuisineType.BRAZILIAN,
                openingHours,
                "user123",
                Collections.emptyList());
    }

    @Test
    void shouldFindRestaurantByIdSuccessfully() {
        when(restaurantRepository.findById("1")).thenReturn(Optional.of(existingRestaurant));

        Restaurant foundRestaurant = findRestaurantByIdUseCase.execute("1");

        assertNotNull(foundRestaurant);
        assertEquals(existingRestaurant.getId(), foundRestaurant.getId());
        assertEquals(existingRestaurant.getName(), foundRestaurant.getName());
        verify(restaurantRepository, times(1)).findById("1");
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        when(restaurantRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> findRestaurantByIdUseCase.execute("1"));
        verify(restaurantRepository, times(1)).findById("1");
    }
}
